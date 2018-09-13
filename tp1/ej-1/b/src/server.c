/* A simple server in the internet domain using TCP
   The port number is passed as an argument */
#include <stdio.h>
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
    struct sockaddr_in serv_addr, cli_addr;
    int n,cant;
    if (argc < 3) {
        fprintf(stderr,"ERROR, no port or no cant provided\n");
        exit(1);
    }

    // tamaño del buffer
    cant = atoi(argv[2]);
    char buffer[cant];

    // cuestiones de conexión
    sockfd = socket(AF_INET, SOCK_STREAM, 0);
    if (sockfd < 0) 
        error("ERROR opening socket");

    bzero((char *) &serv_addr, sizeof(serv_addr));
    portno = atoi(argv[1]);
    serv_addr.sin_family = AF_INET;
    serv_addr.sin_addr.s_addr = INADDR_ANY;
    serv_addr.sin_port = htons(portno);

    // bind
    if (bind(sockfd, (struct sockaddr *) &serv_addr,
                sizeof(serv_addr)) < 0) 
        error("ERROR on binding");
    listen(sockfd,5);
    clilen = sizeof(cli_addr);

    // nueva conexión
    newsockfd = accept(sockfd, 
            (struct sockaddr *) &cli_addr, 
            &clilen);

    if (newsockfd < 0) 
        error("ERROR on accept");

    bzero(buffer,cant);

    // Leemos desde el buffer
    n = read(newsockfd,buffer,cant);

    printf("Paso 1: La función read devolvió %d bytes.\n",n);
    printf("--\n");


    // Escribimos cant bytes en el buffer
    cant = strlen(buffer);
    n = write(newsockfd,buffer,cant);

    printf("Paso 2: Se escribieron %d bytes.\n",cant);
    printf("Paso 2: La función write devolvió %d bytes.\n",n);
    printf("--\n");
    if (n < 0) error("ERROR writing to socket");
    close(newsockfd);
    close(sockfd);
    return 0; 
}
