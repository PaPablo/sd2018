BIN = bin/client bin/server
CC = gcc

all: $(BIN)

clean:
	rm -f $(OBJ) $(BIN)

bin/client: src/client.c
	$(CC) -o bin/client src/client.c src/clstub.c

bin/server: src/server.c
	$(CC) -o bin/server src/server.c src/svstub.c
