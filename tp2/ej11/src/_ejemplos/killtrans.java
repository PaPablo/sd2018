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


 
public class killtrans {


	public static PGXADataSource getDataSource()
	throws SQLException
	{
	 PGXADataSource xaDS = new PGXADataSource();

	 xaDS.setServerName("localhost");
	 xaDS.setDatabaseName("sitio1");		
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
	  xaCon = xaDS.getXAConnection("cristian", "cristian");
	  xaRes = xaCon.getXAResource();

 	  System.out.println("Conecto!");

	  con = xaCon.getConnection();


 	  xid = new MyXid(111, new byte[]{0x01}, new byte[]{0x02});
 
	        xaRes.rollback(xid);
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
