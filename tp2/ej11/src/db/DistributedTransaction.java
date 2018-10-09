package db;

import javax.transaction.xa.Xid;

public class DistributedTransaction {
    private MyXid _xid;

    public DistributedTransaction(int formatId, byte gtrid[], byte bqual[]) {
       this._xid = new MyXid(formatId, gtrid, bqual); 
    }    

    public Xid getXid() {
        return this._xid;
    }
}
