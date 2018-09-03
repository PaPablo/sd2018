/*
 *  client.c
 */

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <fcntl.h>
#include "prototipo.h"

#define LARGOBUF  16
#define MAX_FILE_NAME_LENGTH 50
#define MAX_BYTES 9

int s;	//nro de socket

typedef struct {
    char *filename;
    RD rd;
    int open;
} R_FILE;

R_FILE files[100];
int last_file = 0;

// Get remote file from local array
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
    printf("%s %d %d\n", from, binicio, cbytes);
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

char *get_filename(){
    char temp[MAX_FILE_NAME_LENGTH], *nombrearchivo;
    printf("Nombre de archivo: ");
    bzero(temp, MAX_FILE_NAME_LENGTH);
    nombrearchivo =  malloc(MAX_FILE_NAME_LENGTH);

    fgets(nombrearchivo, MAX_FILE_NAME_LENGTH, stdin);
    strncpy(temp, nombrearchivo, strlen(nombrearchivo)-1);
    strcpy(nombrearchivo, temp);

    return nombrearchivo;
}

int get_cant_bytes(){
    char *cantidadbytes;
    printf("Cantidad de bytes a leer: ");
    cantidadbytes = malloc(MAX_BYTES);
    fgets(cantidadbytes, MAX_BYTES, stdin);

    return atoi(cantidadbytes);
}

int get_initial_byte(){
    char *byteinicial;
    printf("Byte de origen: ");
    byteinicial = malloc(MAX_BYTES);
    fgets(byteinicial, MAX_BYTES, stdin);

    return atoi(byteinicial);
}

void handle_leer (){

    char *nombrearchivo = get_filename();

    int cantidadbytes = get_cant_bytes();

    int byteinicial = get_initial_byte();

    printf("%s %d %d\n", nombrearchivo, byteinicial, cantidadbytes);

    leer(nombrearchivo, byteinicial, cantidadbytes);
}

void handle_escribir (){

    char *nombrearchivo = get_filename();

    int cantidadbytes = get_cant_bytes();

    escribir(nombrearchivo, cantidadbytes);

}

short get_flags(){
    char *flags;
    printf("Ingrese modo de apertura del archivo: ");
    flags = malloc(3);
    fgets(flags, 2, stdin);

    if (strcmp("r",flags) == 0){
        return O_RDONLY;
    }
    else if(strcmp("w", flags) == 0){
        return O_WRONLY | O_CREAT | O_TRUNC;
    }
    else if(strcmp("a", flags) == 0){
        return O_WRONLY | O_CREAT | O_APPEND;
    }
    else if(strcmp("r+", flags) == 0){
        return O_RDWR;
    }
    else if(strcmp("w+", flags) == 0){
        return O_RDWR | O_CREAT | O_TRUNC;
    }
    else if(strcmp("a+", flags) == 0){
        return O_RDWR | O_CREAT | O_APPEND;
    }

    return -1;
}

void handle_abrir (){
    char *nombrearchivo = get_filename();

    short flags = get_flags();

    abrir(nombrearchivo, flags);
}

void handle_cerrar (){

}

void handle_otro(){
    printf("No hay opcion con dicho número");
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
        printf("Ingrese su opción: ");

        bzero(orden,20);  
        fflush(stdin);
        fgets(orden,20,stdin);
        int i_orden = atoi(orden);

        handle_orden(i_orden);

    }
}

