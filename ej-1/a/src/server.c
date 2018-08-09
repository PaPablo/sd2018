/* A simple server in the internet domain using TCP
   The port number is passed as an argument */
#include <stdio.h>
#include <stdlib.h>
#include <sys/types.h> 
#include <sys/socket.h>
#include <netinet/in.h>

void error(char *msg)
{
    perror(msg);
    exit(1);
}

int main(int argc, char *argv[])
{
    int sockfd, newsockfd, portno, clilen;
    char buffer[256];
    char answer[256];
    struct sockaddr_in serv_addr, cli_addr;
    int n;


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
        do {

            if (newsockfd < 0) 
                error("ERROR on accept");

            // Limpiamos buffer y leemos
            bzero(buffer,256);

            n = read(newsockfd,buffer,255);

            if (n < 0) error("ERROR reading from socket");

            snprintf(answer, sizeof(answer), "Respuesta: %s", buffer);

            n = write(newsockfd,"I got your message",18);

            if (n < 0)
                error("ERROR writing to socket");

        } while(strlen(buffer) != 0);

    }


    return 0; 
}
