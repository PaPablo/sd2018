/*cliente2 (modificado)
 * Pasar: NombreServer port Cant_Chars_del buffer
 */

#include <stdio.h>
#include <sys/types.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include <netdb.h> 

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
    int a,b,cant;

    if (argc < 4) {
        fprintf(stderr,"usage %s hostname port cant_chars_buffer\n", argv[0]);
        exit(0);
    }

    cant = atoi(argv[3]);;

    // 65 es A en ascii
    b = 65;

    // declara buffer de tamaño cant
    char buffer[cant];

    // coloca caracteres en orden sobre buffer
    for (a=0 ; a<cant-1 ; a++) {
        buffer[a] = b++;
        if (b > 80)
            b = 65;
    }
    buffer[a] = '\0';  // finalizo la cadena

    // cuestiones de la conexión
    portno = atoi(argv[2]);
    sockfd = socket(AF_INET, SOCK_STREAM, 0);
    if (sockfd < 0) 
        error("ERROR opening socket");

    //nombre del server
    server = gethostbyname(argv[1]);
    if (server == NULL) {
        fprintf(stderr,"ERROR, no such host\n");
        exit(0);
    }
    bzero((char *) &serv_addr, sizeof(serv_addr));
    serv_addr.sin_family = AF_INET;
    bcopy((char *)server->h_addr, 
            (char *)&serv_addr.sin_addr.s_addr,
            server->h_length);
    serv_addr.sin_port = htons(portno);

    // conectamo
    if (connect(sockfd,&serv_addr,sizeof(serv_addr)) < 0) 
        error("ERROR connecting");

    // escribimos en el socket
    n = write(sockfd,buffer,cant);

    printf("Paso 1: Se escribieron %d bytes.\n",cant);
    printf("Paso 1: La función write devolvió %d bytes.\n",n);
    printf("--\n");
    if (n < 0) error("ERROR writing to socket");

    // vacía el buffer
    bzero(buffer,cant);

    // leemos lo que haya en el socket
    n = read(sockfd,buffer,cant);
    printf("Paso 2: La función read devolvió %d bytes.\n",n);
    //printf("%s\n",buffer);
    return 0;
}
