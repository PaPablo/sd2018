
/*
 *  server.c
 */

#include <stdio.h>
#include <stdlib.h>
#include <fcntl.h>
#include <unistd.h>
#include "prototipo.h"

//RD myfile;

RD rmtopen( int s,  short flags, const char *pathname )
	{
		RD myfile;
		myfile = open( pathname, flags, 0644 );
		if( myfile < 0 )
			return -1;
		return myfile;
	}

int rmtread( int s,  RD rd, void *data, int qty, int binicio )
	{
	lseek(rd, binicio, SEEK_SET);
	return read( rd, data, qty );
	}

int rmtwrite( int s,  RD rd, const void *data, int qty )
	{
	int val;
	return write( rd, data, qty );
	val = fsync(rd);
	return val;
	}

int rmtclose( int s,  RD rd )
	{
	int status;
	status = close(rd);
	//myfile = NULL;
	return status;
	}