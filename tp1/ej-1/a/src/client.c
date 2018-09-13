#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <string.h>
#include <sys/types.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include <netdb.h> 

#define BUFFER_SIZE 256

void error(char *msg)
{
    perror(msg);
    exit(0);
}

int main(int argc, char *argv[])
{
    int sockfd, portno, n;
    struct sockaddr_in serv_addr;
    struct hostent *server;

    char buffer[BUFFER_SIZE];

    if (argc < 3) {
        fprintf(stderr,"Usage %s: <hostname> <port>\n", argv[0]);
        exit(0);
    }

    // Nro de puerto para conectarse a
    portno = atoi(argv[2]);
    sockfd = socket(AF_INET, SOCK_STREAM, 0);
    if (sockfd < 0) 
        error("ERROR opening socket");

    // Hostname
    server = gethostbyname(argv[1]);
    if (server == NULL) {
        fprintf(stderr,"ERROR - no such host\n");
        exit(0);
    }

    bzero((char *) &serv_addr, sizeof(serv_addr));
    serv_addr.sin_family = AF_INET;
    bcopy((char *)server->h_addr, 
            (char *)&serv_addr.sin_addr.s_addr,
            server->h_length);
    serv_addr.sin_port = htons(portno);

    // Conexión
    if (connect(sockfd, (struct sockaddr *) &serv_addr, sizeof(serv_addr)) < 0) 
        error("ERROR connecting");

    // Pedimos el mensaje
    printf("Please enter the message: ");
    bzero(buffer,BUFFER_SIZE);

    while(fgets(buffer, BUFFER_SIZE-1, stdin) != NULL){

        // Escribimos el mensaje en el socket
        n = send(sockfd, buffer, strlen(buffer), 0);

        if (n < 0) 
            error("ERROR writing to socket");

        // Recibimos la respuesta del servidor
        bzero(buffer, BUFFER_SIZE);
        n = recv(sockfd, buffer, BUFFER_SIZE-1, 0);

        if (n < 0) 
            error("ERROR reading from socket");

        // Mostramos en pantalla la respuesta
        printf("%s", buffer);

        bzero(buffer, BUFFER_SIZE);

    }

    return 0;
}
