package db;

import java.sql.Connection;
import java.sql.SQLException;
import javax.sql.XAConnection;
import javax.transaction.xa.XAException;
import javax.transaction.xa.XAResource;

import org.postgresql.xa.*;

public class PGConnection {
       
    private PGXADataSource _dataSource;
    private XAConnection _xaConnection;
    private XAResource _xaRes;
    private Connection _conn;
	private String host;
	private int port;
	private String dbName;

    public PGConnection(String host, int port, String dbName) throws SQLException {
        this._dataSource = new PGXADataSource();
        this._dataSource.setServerName(host);
        this._dataSource.setPortNumber(port);
        this._dataSource.setDatabaseName(dbName);
        this.host = host;
        this.port = port;
        this.dbName = dbName;
    } 

    public Connection connect(String user, String password) throws SQLException {
        this._xaConnection = this._dataSource.getXAConnection(user, password);
        this._conn = this._xaConnection.getConnection();
        this._xaRes = this._xaConnection.getXAResource();
        return this._conn;
    }

    public void close() throws SQLException {
        this._xaConnection.close();
        this._conn.close();
    }

    public void startTransaction(DistributedTransaction t) throws XAException {
        this._xaRes.start(t.getXid(), XAResource.TMNOFLAGS);
    }

    public void endTransaction(DistributedTransaction t) throws XAException {
        this._xaRes.end(t.getXid(), XAResource.TMSUCCESS);
    }

    public int prepareForCommit(DistributedTransaction t) throws XAException {
        return this._xaRes.prepare(t.getXid());
    }

    public boolean canPrepareForCommit(DistributedTransaction t) throws XAException {
        return this._xaRes.prepare(t.getXid()) == XAResource.XA_OK;
    }

    public void commit(DistributedTransaction t) throws XAException {
        this._xaRes.commit(t.getXid(), false);
    }

    public void rollback(DistributedTransaction t) throws XAException {
        this._xaRes.rollback(t.getXid());
    }


}
