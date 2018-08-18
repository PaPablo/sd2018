/*
 *	prototipo.h
 */

typedef int RD;

/*
 *	Funciones remotas
 */


RD rmtopen( int s, short flags, const char *pathname );

int rmtread( int s,  RD rd, void *data, int qty, int binicio );

int rmtwrite( int s,  RD rd, const void *data, int qty );

int rmtclose( int s,  RD rd );

/*
 *	Funciones de Stubs
 */

int send_packet( int sockfd, const void *p, int qty );

int receive_packet( int sockfd, void *p, int lim );

