
/*
 *	strustub.h
 *	Estructuras del Middleware
 */

typedef int OPC;

#define MAX_NAME	50
#define MAX_DATA	512

enum { ROPEN, RREAD, RWRITE, RCLOSE  }; //MAX_OPCODES

/*	Client -> server	*/

typedef struct
  {
  short flags;
  char pathname[ MAX_NAME ];
  } CLSV_ROPEN;

typedef struct
  {
  RD rd;
  int qty;
  int binicio;
  } CLSV_RREAD;

typedef struct
  {
  RD rd;
  int qty;
  char data[ MAX_DATA ];
  } CLSV_RWRITE;

typedef struct
  {
  RD rd;
  } CLSV_RCLOSE;

/*	Server -> client	*/

typedef struct
  {
  RD rd;
  } SVCL_ROPEN;

typedef struct
  {
  int status;
  char data[ MAX_DATA ];
  } SVCL_RREAD;

typedef struct
  {
  int status;
  } SVCL_RWRITE;

typedef struct
  {
  int status;
  } SVCL_RCLOSE;


typedef union
  {
  CLSV_ROPEN	 clsv_ropen;
  CLSV_RREAD	 clsv_rread;
  CLSV_RWRITE	 clsv_rwrite;
  CLSV_RCLOSE	 clsv_rclose;
  SVCL_ROPEN	 svcl_ropen;
  SVCL_RREAD	 svcl_rread;
  SVCL_RWRITE	 svcl_rwrite;
  SVCL_RCLOSE	 svcl_rclose;
  } DATA;

  typedef struct
    {
    OPC opc;
    DATA data;
    } CLSVBUFF;

