/*
 *  client.c
 */

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <fcntl.h>
#include "prototipo.h"

#define LARGOBUF  16
int s;	//nro de socket

typedef struct {
    char *filename;
    RD rd;
    int open;
} R_FILE;

R_FILE files[100];
int last_file = 0;

R_FILE *get_rd(R_FILE *files, char *filename){
    for(int i = 0; i <= last_file; i++){
        if (strncmp(files[i].filename, filename, strlen(filename)) == 0){
            return &files[i];
        }
    }
    return -1;
}

static void leer(char *from, int binicio, int cbytes )    // char *to, 
{
    R_FILE *r_file;
    FILE *f;
    int qty=-1,c_restantes,p_actual;
    char buffer[ LARGOBUF ], *to;
    to = "alfa.pdf";   //defino destino local de datos

    printf( "transfiriendo %s remoto al %s local\n", from, to );
    //Aperturas
    
    r_file = get_rd(files, from);

    if (r_file == -1){
        printf("No pudo abrirse el remoto \"%s\"\n", from);
    }
    else {

        printf( "Abrió el remoto: %d\n", r_file->rd );

        f = fopen( to, "w" );

        if( f == NULL || r_file->rd < 0 )  //Hay error en aperturas
        {
            perror( "leer" );
            exit(1);
        }

        //Determinar bytes a leer
        c_restantes = cbytes;
        p_actual = binicio;
        while (c_restantes > 0 && qty != 0) {
            if (c_restantes < sizeof( buffer ))
                qty = c_restantes;
            else
                qty = sizeof( buffer ); 
            qty = rmtread( s, r_file->rd, buffer, qty, p_actual);
            fwrite( buffer, 1, qty, f );
            c_restantes -= qty;
            p_actual +=qty;
        }
        printf( "Cerrando el remoto %d\n", rmtclose( s, r_file->rd ) );
        fclose( f );
    }
}

static void escribir( const char *to,  int cbytes)
{
    R_FILE *r_file;
    FILE *f;
    int qty,c_restantes,p_actual;
    char buffer[ LARGOBUF ], *from;

    from =  "beta.txt";   //defino origen local de datos

    printf( "transfiriendo %s local al %s remoto\n", from, to );
    //Aperturas
    r_file = get_rd(files, to);

    if (r_file == -1) {
        printf("No se puede abrir remoto \"%s\"\n", to);

    }
    else {
        printf( "Abrió el remoto: %d\n", r_file->rd );
        f = fopen( from, "r" );
        if( f == NULL || r_file->rd < 0 )  //Hay error en aperturas
        {
            perror( "escribir" );
            exit( 1 );
        }

        /*Determinar bytes a escribir. Toma una cantidad determinada de bytes
          desde el inicio del file.*/ 
        c_restantes = cbytes;
        p_actual = 0;
        while (c_restantes > 0 && qty != 0) {
            if (c_restantes < sizeof( buffer ))
                qty = c_restantes;
            else
                qty = sizeof( buffer ); 
            fseek(f, p_actual, SEEK_SET);
            qty = fread( buffer, 1, qty, f );
            rmtwrite( s, r_file->rd, buffer, qty );	    
            c_restantes -= qty;
            p_actual +=qty;

        }
        printf( "Cerrando el remoto %d\n", rmtclose( s, r_file->rd ) );
        fclose( f );
    }
}

R_FILE *abrir(const char* file, short flags){
    RD rd;

    // Get remote descriptor from file
    rd = rmtopen(s, flags, file);

    if (rd < 0) {
        printf("No pudo abrirse %s\n", file);
        return -1;
    }

    // Remote file
    R_FILE *r_file;

    // Fill r_file structure
    r_file->filename = malloc(strlen(file));
    strncpy(r_file->filename, file, strlen(file));

    // asign rd
    r_file->rd = rd;
    // mark as opened
    r_file->open = 1;

    //place in local array
    files[last_file++] = *r_file;

    return r_file;
}

int cerrar(const char* file){

    // Get remote file entry from local array
    R_FILE *r_file = get_rd(files, file);
    
    if (r_file == -1){
        printf("%s no ha sido abierto\n");
        return -1;
    }

    // Close file
    rmtclose(s, r_file->rd);
    
    // Mark as closed
    r_file->open = 0;

    return r_file->rd;

}

void handle_leer (){

}

void handle_escribir (){

}

void handle_abrir (){

}

void handle_cerrar (){

}

void handle_otro(){
    print("No hay opcion con dicho número");
}
void handle_orden(int orden){
    switch (orden) {
        case 1:
            handle_abrir();
            break;
        case 2:
            handle_leer();
            break;
        case 3:
            handle_escribir();
            break;
        case 4:
            handle_cerrar();
            break;
        default:
            handle_otro();
    }

}

void main( void ){

    char orden[20];
    char nombrearchivo[50],temp[50];
    char byteinicial[9], cantidadbytes[9];

    //int  largo;
    s = sockcl_init( "localhost" , "8889" ); 
    if (s < 0) {
        fprintf(stderr,"No abrió el Socket\n");
        exit(0);
    }

    for (;;)
    {
        printf("Escriba Comando:\n");
        printf("1 - Abrir archivo\n");
        printf("2 - Leer archivo\n");
        printf("3 - Escribir archivo\n");
        printf("4 - Cerrar archivo\n");

        bzero(orden,20);  
        fgets(orden,20,stdin);
        int i_orden = atoi(orden);

    }
}

