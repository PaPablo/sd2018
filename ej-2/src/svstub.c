/*
 *	svstub.c
 */

#include <stdio.h>
#include <stdlib.h>
#include <sys/types.h> 
#include <sys/socket.h>
#include <netinet/in.h>
#include <string.h>


#include "prototipo.h"
#include "strustub.h"

CLSVBUFF clsvbuff;
int s;
char bufcom[ MAX_DATA ];

void error(char *msg)
{
    perror(msg);
    exit(0);
}

/*
 *INTERFASE CON CAPA TRANSPORTE
*/

//Emisión
int send_packet( int sockfd, const void *p, int qty )
    {
    int n;
    memcpy( bufcom, p, qty );
    n = write(sockfd,bufcom,qty);
    if (n < 0) 
         error("ERROR writing to socket");
    bzero(bufcom, MAX_DATA);
    return n;
    }

//Recepción
int receive_packet( int sockfd, void *p, int lim )
    {
    int n;
    n = read(sockfd,bufcom,lim);
    if (n < 0) 
         error("ERROR reading from socket");
    else
	{
	memcpy( p, bufcom, n );
	bzero( bufcom, MAX_DATA);
	}
    return n;
    }

//INICIALIZACION
int socksv_init(char *arg1)
{
     int sockfd, newsockfd, portno, clilen;

     struct sockaddr_in serv_addr, cli_addr;
     int n;

     sockfd = socket(AF_INET, SOCK_STREAM, 0);
     if (sockfd < 0) 
        error("ERROR opening socket");
     bzero((char *) &serv_addr, sizeof(serv_addr));
     portno = atoi(arg1);
     serv_addr.sin_family = AF_INET;
     serv_addr.sin_addr.s_addr = INADDR_ANY;
     serv_addr.sin_port = htons(portno);
     if (bind(sockfd, (struct sockaddr *) &serv_addr,
              sizeof(serv_addr)) < 0) 
              error("ERROR on binding");
     listen(sockfd,5);
     clilen = sizeof(cli_addr);
     newsockfd = accept(sockfd, 
                 (struct sockaddr *) &cli_addr, 
                 &clilen);
     if (newsockfd < 0) 
          error("ERROR on accept");

    //Una vez conectado, pruebo leer y escribir.
     n = read(newsockfd,bufcom,MAX_DATA);
     if (n < 0)
	error("ERROR reading from socket");
     else
	printf("mensaje: %s\n",bufcom);
     bzero(bufcom, MAX_DATA);

     n = write(newsockfd,"Hola Cliente..Aqui estoy",24);
     if (n < 0) error("ERROR writing to socket");

     return newsockfd;
}


/*
 *CAPA MIDDLEWARE (STUB)
 */


static int process_ropen( DATA *p, int qty )
    {
    p->svcl_ropen.rd = rmtopen( s, p->clsv_ropen.flags, p->clsv_ropen.pathname );
    return sizeof( SVCL_ROPEN );
    }

static int process_rread( DATA *p, int qty )
    {
    p->svcl_rread.status = rmtread( s, p->clsv_rread.rd, p->svcl_rread.data, p->clsv_rread.qty, p->clsv_rread.binicio );
    return sizeof( SVCL_RREAD );
    }

static int process_rwrite( DATA *p, int qty )
    {
    p->svcl_rwrite.status = rmtwrite( s, p->clsv_rwrite.rd, p->clsv_rwrite.data, p->clsv_rwrite.qty );
    return sizeof( SVCL_RWRITE );
    }

static int process_rclose( DATA *p, int qty )
    {
    p->svcl_rclose.status = rmtclose( s, p->clsv_rclose.rd );
    return sizeof( SVCL_RCLOSE );
    }

static int
 (*proc[])( DATA *p, int qty ) = { process_ropen, process_rread, process_rwrite, process_rclose };

static int process_server( CLSVBUFF *p, int qty )
    {
    int opcode;

    opcode = p->opc;
    qty = (*proc[opcode])( &p->data, qty - sizeof( OPC ) );
    p->opc = opcode;
    return qty + sizeof( OPC );
    }


static void do_server( void )
    {
    int qty;

    qty = receive_packet( s, &clsvbuff, sizeof( clsvbuff ) );
    qty = process_server( &clsvbuff, qty );
    send_packet( s, &clsvbuff, qty );
    }


void main( void )
    {
	//int s;
	s = socksv_init( "8888" ); 
	if (s < 0) {
	    fprintf(stderr,"No abrió el Socket\n");
	    exit(0);
	}

    for(;;)
      do_server();
    }





