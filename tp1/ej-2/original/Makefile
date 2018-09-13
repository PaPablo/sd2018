BIN = bin/client bin/server
CC = gcc

all: $(BIN)

clean:
	rm -f $(OBJ) $(BIN)

bin/client:
	$(CC) -o bin/client src/client.c src/clstub.c

bin/server: 
	$(CC) -o bin/server src/server.c src/svstub.c
