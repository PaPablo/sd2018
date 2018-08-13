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

static void leer(char *from, int binicio, int cbytes )    // char *to, 
	{
	RD rd;
	FILE *f;
	int qty,c_restantes,p_actual;
	char buffer[ LARGOBUF ], *to;
	to = "alfa.txt";   //defino destino local de datos

	printf( "transfiriendo %s remoto al %s local\n", from, to );
	//Aperturas
	rd = rmtopen( s, O_RDONLY, from);
	if (rd >= 0) {
	  printf( "Abrió el remoto: %d\n", rd );
	  f = fopen( to, "w" );
	  if( f == NULL || rd < 0 )  //Hay error en aperturas
		{
		perror( "leer" );
		exit(1);
	  }

	  //Determinar bytes a leer
	  c_restantes = cbytes;
	  p_actual = binicio;
	  while (c_restantes > 0) {
	    if (c_restantes < sizeof( buffer ))
		  qty = c_restantes;
	    else
		  qty = sizeof( buffer ); 
	    qty = rmtread( s, rd, buffer, qty, p_actual);
	    fwrite( buffer, 1, qty, f );
	    c_restantes -= qty;
	    p_actual +=qty;
	  }
	  printf( "Cerrando el remoto %d\n", rmtclose( s, rd ) );
	  fclose( f );
	}
	else
	  printf( "Lo siento..!! No pudo abrirse el %s remoto.\n", to );
}

static void escribir( const char *to,  int cbytes)
	{
	RD rd;
	FILE *f;
	int qty,c_restantes,p_actual;
	char buffer[ LARGOBUF ], *from;
	from =  "beta.txt";   //defino origen local de datos

	printf( "transfiriendo %s local al %s remoto\n", from, to );
	//Aperturas
	rd = rmtopen( s, O_CREAT | O_WRONLY | O_APPEND, to);
	if (rd >= 0) {
	  printf( "Abrió el remoto: %d\n", rd );
	  f = fopen( from, "r" );
	  if( f == NULL || rd < 0 )  //Hay error en aperturas
		{
		perror( "escribir" );
		exit( 1 );
	  }

	  /*Determinar bytes a escribir. Toma una cantidad determinada de bytes
	  desde el inicio del file.*/ 
	  c_restantes = cbytes;
	  p_actual = 0;
	  while (c_restantes > 0) {
	    if (c_restantes < sizeof( buffer ))
		  qty = c_restantes;
	    else
		  qty = sizeof( buffer ); 
	    fseek(f, p_actual, SEEK_SET);
	    qty = fread( buffer, 1, qty, f );
	    rmtwrite( s, rd, buffer, qty );	    
	    c_restantes -= qty;
	    p_actual +=qty;

	  }
	  printf( "Cerrando el remoto %d\n", rmtclose( s, rd ) );
	  fclose( f );
	  }
	else
	  printf( "Lo siento..!! No pudo abrirse el %s remoto.\n", to );
	}

void main( void )
    {
    char orden[20];
    char nombrearchivo[50],temp[50];
    char byteinicial[9], cantidadbytes[9];

    //int  largo;
    s = sockcl_init( "localhost" , "8888" ); 
    if (s < 0) {
	    fprintf(stderr,"No abrió el Socket\n");
	    exit(0);
    }

    for (;;)
	{
	printf("Escriba Comando (leer/escribir): ");
	bzero(orden,20);  
	fgets(orden,20,stdin);

	printf("Escriba Nombre de Archivo a leer/escribir (/path/archivo): ");
	bzero(temp,50);  
	fgets(nombrearchivo,50,stdin);
	strncpy(temp, nombrearchivo, strlen(nombrearchivo) - 1);
	strcpy(nombrearchivo, temp);

	printf("Escriba cantidad de bytes a transferir (1 a 7 cifras): ");
	fgets(cantidadbytes,9,stdin);
	
	if (orden[0] == 'l' || orden[0] == 'L')
	  {
	  printf("Escriba byte de inicio del Origen (1 a 7 cifras): ");
	  fgets(byteinicial,9,stdin);

	  //Llamo la lectura. El buffer es local a la función
	  leer(nombrearchivo, atoi(byteinicial), atoi(cantidadbytes)); 
	  }
	else
	    //Llamo la escritura. El buffer es local a la función
	    escribir(nombrearchivo,  atoi(cantidadbytes));
	}
    }

