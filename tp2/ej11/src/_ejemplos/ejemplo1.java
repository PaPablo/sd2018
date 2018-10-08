package _ejemplos;

import javax.transaction.xa.*;
import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.*;

 
import javax.sql.DataSource;
import javax.sql.XADataSource;
 
import javax.sql.XAConnection;
import org.postgresql.xa.*;


 
public class ejemplo1 {


	public static PGXADataSource getDataSource()
	throws SQLException
	{
	 PGXADataSource xaDS = new PGXADataSource();

	 xaDS.setServerName("localhost");
     xaDS.setPortNumber(5550);
	 xaDS.setDatabaseName("banco");		
	 xaDS.setUser("postgres");				
	 return xaDS;
	}

	
	public static void main(String[] argv) {

	 XADataSource xaDS;
	 XAConnection xaCon;
	 XAResource xaRes;
	 Xid xid;
	 Connection con;
	 Statement stmt;
	 int ret;

	 try { 
	  xaDS = getDataSource();
	  //xaCon = xaDS.getXAConnection("jtatest", "jtatest");
	  xaCon = xaDS.getXAConnection("admin", "admin");
	  xaRes = xaCon.getXAResource();

 	  System.out.println("Conecto!");

	  con = xaCon.getConnection();
	  stmt = con.createStatement();


 	  xid = new MyXid(101, new byte[]{0x01}, new byte[]{0x02});
 
      String insertStmt = "INSERT INTO cuentas (id, titular, bloqueada, saldo) VALUES (101, 'nueva cuenta', False, 100000)";
      System.out.println(String.format(
                  "Ejecutando [%s]",
                  insertStmt
                  ));
	  xaRes.start(xid, XAResource.TMNOFLAGS);
 	  stmt.executeUpdate(insertStmt);
	  xaRes.end(xid, XAResource.TMSUCCESS);
	
	  ret = xaRes.prepare(xid);
	  if (ret == XAResource.XA_OK) {
	      /*ret2 = xaRes2.prepare(xid);*/
		  
	      xaRes.commit(xid, false);
	  }
	  stmt.close();
	  con.close();
	  xaCon.close();
	 
	 }
	 catch (XAException e) {
	  e.printStackTrace();
	 }
	 catch (SQLException e) {
	  e.printStackTrace();
	 }
	 finally {
	 }
	}
}
