/*
 * This is sample code generated by rpcgen.
 * These are only templates and you can use them
 * as a guideline for developing your own functions.
 */

#include "rfs.h"
#include <sys/types.h>
#include <sys/stat.h>
#include <fcntl.h>
#include <unistd.h>
#include <ctype.h>
#include <libgen.h>
#include <string.h>

char *strlower(char *);
void imprimir_ayuda(char *[]);

#define READ_CLI_CMD "leer"
#define WRITE_CLI_CMD "escribir"
#define BUFFER_SIZE 8096

/* Handle para las llamadas remotas (creado/asignado en rfs_1() */
CLIENT *clnt;

/* Crea/asigna el handle */
void rfs_1(char *host) {
#ifndef DEBUG
    clnt = clnt_create (host, RFS, RFS_VERS_1, "udp");
    if (clnt == NULL) {
        clnt_pcreateerror (host);
        exit (1);
    }
#endif /* DEBUG */
}

/* Destruye el handle */
void rfs_1_end() {
#ifndef DEBUG
    clnt_destroy (clnt);
#endif /* DEBUG */
}

/* Wrapper para rfs_open */
int wrap_rfs_open(char *file_name, int flags) {
    open_record rfs_open_1_arg;
    int *result_1;
    /* define archivo a leer */
    rfs_open_1_arg.file_name = file_name; 
    /* define permisos */
    rfs_open_1_arg.flags = flags;
    result_1 = rfs_open_1(&rfs_open_1_arg, clnt);
    if (result_1 == (int *) NULL)
        clnt_perror (clnt, "Fallo llamada open");
    return *result_1;
}

/* Wrapper para rfs_read */
file_data *wrap_rfs_read(int fd, int count) {
    read_record rfs_read_1_arg;
    file_data *result_2;
    rfs_read_1_arg.fd = fd;
    rfs_read_1_arg.count = count;

    result_2 = rfs_read_1(&rfs_read_1_arg, clnt);
    if (result_2 == (file_data *) NULL)
        clnt_perror (clnt, "Fallo llamada read");
    return result_2;
}

/* Wrapper para rfs_close */
int wrap_rfs_close(int fd) {
    int *result_3;
    int rfs_close_1_arg = fd;
    result_3 = rfs_close_1(&rfs_close_1_arg, clnt);
    if (result_3 == (int *) NULL) {
        clnt_perror (clnt, "Fallo llamada close");
    }
    return *result_3;
}

/*Wrapper para rfs_write*/
int wrap_rfs_write(write_record *r) {
    int *result_4;
    result_4 = rfs_write_1(r, clnt);
    if (result_4 == (int *) NULL) {
        clnt_perror(clnt, "Fallo llamada write");
    }
    return *(int *)result_4;
}


int main (int argc, char *argv[]) {
    char *host, 
         *command, 
         *file_name;
    int fd, 
        n;
    file_data *remote_file_read;

    char *write_file = "un_archivo_escrito.txt";
    char *cadena = "una cadena para escribir en el archivo\n";
    write_record wr;

    /* Se deben pasar nombre de host y de archivo => argc=3 */
    if (argc < 4) {
        imprimir_ayuda(argv);
        return 1;
    }
    /* nombre del host remoto */
    host = argv[1];
    rfs_1 (host);

    /*comando "leer" "escribir"*/
    command = argv[2];
    strlower(command);

    /* creacion/asignacion del handle */
    file_name = argv[3];

    if(strcmp(command, READ_CLI_CMD) == 0){
        //LEER ARCHIVO REMOTO
        fd = wrap_rfs_open(file_name, O_RDWR);
        if (fd == -1) {
            printf("Error al abrir el archivo\n");
            return 1;
        }
        do {
            remote_file_read = wrap_rfs_read(fd, BUFFER_SIZE);
            for (int i=0; i < remote_file_read->file_data_len; i++)
                printf("%c", remote_file_read->file_data_val[i]);
        } while (remote_file_read->file_data_len != 0);
    } else if (strcmp(command, WRITE_CLI_CMD) == 0){
        //ESCRIBIR ARCHIVO REMOTO

        char *output_filename = argc == 5?
            argv[4] :
            basename(file_name);

        fd = wrap_rfs_open(output_filename, O_RDWR | O_CREAT | O_TRUNC);
        if (fd == -1) { 
            printf("Error al abrir el archivo\n"); 
            return 1; 
        }

        FILE *file = fopen(file_name, "rb");
        if (file == NULL) {
            printf("No se pudo escribir el archivo %s\n", file_name);
            return 1;
        }

        char buff[BUFFER_SIZE];
        wr.fd = fd;
        while(fgets(buff, BUFFER_SIZE, file) != 0) {
            wr.buf.file_data_val = buff;
            wr.buf.file_data_len = strlen(buff);
            if(wrap_rfs_write(&wr) == -1) {
                printf("Error al escribrir\n");
                return 1;
            }
        }


    } else {
        printf("NO SE COMPRENDIÓ EL COMANDO\n");
        return 1;
    }

    /* cierre del archivo */
    wrap_rfs_close(fd);
    /* destruccion del handle */
    rfs_1_end();
    return 0;
}

char *strlower(char *str) {
    for (int i = 0; str[i]; i++) {
       str[i] = tolower(str[i]);
    }

    return str;
}

void imprimir_ayuda(char *argv[]) {
    printf("USO %s: <HOST_SERVIDOR> <COMANDO> <ARCHIVO> [ARCHIVO_ESCRITO]\n", argv[0]);
    printf("\tCOMANDOS POSIBLES: leer | escribir\n");
    printf("\t<ARCHIVO> indica la ruta archivo que se desea leer/escribir \n");
    printf("\t[ARCHIVO_ESCRITO] indica el nombre que tendrá el archivo escrito (opcional)\n");
}
