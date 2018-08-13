/* A simple server in the internet domain using TCP
   The port number is passed as an argument */
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <sys/types.h> 
#include <sys/socket.h>
#include <unistd.h>
#include <netinet/in.h>

#define BUFFER_SIZE 256

void error(char *msg)
{
    perror(msg);
    exit(1);
}

int main(int argc, char *argv[])
{
    int sockfd, newsockfd, portno, clilen;
    char buffer[BUFFER_SIZE];
    char answer[BUFFER_SIZE];
    struct sockaddr_in serv_addr, cli_addr;


    // Chequear nro de argumentos
    if (argc < 2) {
        fprintf(stderr,"ERROR, no port provided\n");
        exit(1);
    }

    // socket
    sockfd = socket(AF_INET, SOCK_STREAM, 0);
    if (sockfd < 0) 
        error("ERROR opening socket");

    bzero((char *) &serv_addr, sizeof(serv_addr));

    // puerto sobre el que escucha
    portno = atoi(argv[1]);

    // datos del puerto
    serv_addr.sin_family = AF_INET;
    serv_addr.sin_addr.s_addr = INADDR_ANY;
    serv_addr.sin_port = htons(portno);

    // ligazón del puerto
    if (bind(sockfd, (struct sockaddr *) &serv_addr,
                sizeof(serv_addr)) < 0) 
        error("ERROR on binding");


    listen(sockfd,5);
    // listen 
    clilen = sizeof(cli_addr);

    // Ya tiene el cliente y le crea un socket para
    // poder hablarle
    // Error catch incluído
    while(1){
        newsockfd = accept(sockfd,
                (struct sockaddr *) &cli_addr, 
                &clilen);

        if (newsockfd < 0) 
            error("ERROR on accept");

        while ((recv(newsockfd, buffer, BUFFER_SIZE, 0)) > 0){
            printf("Mensaje recibido: %s", buffer);
            /*Armamos la respuesta*/
            sprintf(answer, "SERVIDOR DICE: %s", buffer);
            send(newsockfd, answer, strlen(answer), 0);
            /*Limpiamos los buffers*/
            bzero(buffer, BUFFER_SIZE);
            bzero(answer, BUFFER_SIZE);
        }
        /*Cuando se desconecta el cliente, cerramos su socket*/
        close(newsockfd);
    }


    return 0; 
}
