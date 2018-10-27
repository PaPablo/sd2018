CC = gcc
CFLAGS = -Wall -std=c99
OUTFILE = bin/chat-server
OBJ = ./obj/chat-server.o
SRC = ./src/chat-server.c


$(OUTFILE): $(OBJ)
	$(CC) -o $(OUTFILE) $(OBJ) $(CFLAGS)
$(OBJ): $(SRC)
	$(CC) -o $(OBJ) -c $(SRC) $(CFLAGS)

clean:
	$(RM) ./obj/* ./bin/*
