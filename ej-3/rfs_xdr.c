/*
 * Please do not edit this file.
 * It was generated using rpcgen.
 */

#include "rfs.h"

bool_t
xdr_file_data (XDR *xdrs, file_data *objp)
{
	register int32_t *buf;

	 if (!xdr_bytes (xdrs, (char **)&objp->file_data_val, (u_int *) &objp->file_data_len, ~0))
		 return FALSE;
	return TRUE;
}

bool_t
xdr_open_record (XDR *xdrs, open_record *objp)
{
	register int32_t *buf;

	 if (!xdr_string (xdrs, &objp->file_name, ~0))
		 return FALSE;
	 if (!xdr_int (xdrs, &objp->flags))
		 return FALSE;
	return TRUE;
}

bool_t
xdr_read_record (XDR *xdrs, read_record *objp)
{
	register int32_t *buf;

	 if (!xdr_int (xdrs, &objp->fd))
		 return FALSE;
	 if (!xdr_int (xdrs, &objp->count))
		 return FALSE;
	return TRUE;
}

bool_t
xdr_write_record (XDR *xdrs, write_record *objp)
{
	register int32_t *buf;

	 if (!xdr_file_data (xdrs, &objp->buf))
		 return FALSE;
	 if (!xdr_int (xdrs, &objp->fd))
		 return FALSE;
	return TRUE;
}
