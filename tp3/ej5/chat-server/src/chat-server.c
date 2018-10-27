#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <sys/file.h>

// Nombre del archivo con todo el texto del chat
#define CHATFNAME "data/chat.txt"
// Cantidad maxima de caracteres de una linea
#define MAXLINE 500

int main(int argc, char *argv[]) {
    char *querystr,
         linein[MAXLINE];
    FILE *chatfile;
    int fd_chatfile;
    
    // Definicion del tipo de respuesta
    printf("Content-type: text/html\n\n");
    // Variable de ambiente con el Query_String para un GET
    printf("Eeesa como va\n");
    querystr = getenv("QUERY_STRING");

    // Abrir el archivo
    chatfile = fopen(CHATFNAME, "a+");
    if (chatfile == NULL)
        printf("<h1>Error opening the file</h1>\n");
    
    // Obtener el fd del archivo y bloquearlo
    fd_chatfile = fileno(chatfile);
    if (flock(fd_chatfile, LOCK_EX) != 0)
        printf("<p> Error on flock()</p>\n");

    while (linein == fgets(linein, MAXLINE, chatfile))
        printf("%s", linein);

    fprintf(chatfile, "%s\n", strchr(querystr, '=')+1);
    // Unlock and close file
    flock(fd_chatfile, LOCK_UN);
    fclose(chatfile);

    printf("%s\n", strchr(querystr, '=')+1);
    return 0;
}
