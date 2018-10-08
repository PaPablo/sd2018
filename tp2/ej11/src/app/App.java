package app;

import java.util.InputMismatchException;
import java.util.Scanner;

import java.sql.*;
import javax.sql.*;
import javax.transaction.xa.*;
import org.postgresql.xa.*;

import db.*;
import cuenta.*;

public class App {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int monto = 0, 
            idCuentaExtraccion = 0,
            idCuentaDeposito = 0;


        while(true) {
            System.out.print("Ingrese un monto a transferir: ");
            try {
                monto = sc.nextInt();
                if(monto <= 0) {
                    System.out.println(String.format(
                                "Debe ingresar un monto válido (monto ingresado [%d])",
                                monto));
                    continue;
                }
                break;
            } catch(InputMismatchException e){
                System.out.println(String.format(
                            "Debe ingresar un monto válido (monto ingresado [%s])",
                            monto));
            }
        }

        while(true) {
            System.out.print("Ingrese el ID de la cuenta de la cual extraer: ");
            try {
                idCuentaExtraccion = sc.nextInt();
                if(idCuentaExtraccion <= 0) {
                    System.out.println(String.format(
                                "Debe ingresar un ID válido (ID ingresado [%d])",
                                idCuentaExtraccion));
                    continue;
                }

                break;
            } catch(InputMismatchException e){
                System.out.println(String.format(
                            "Debe ingresar un ID válido (ID ingresado [%s])",
                            idCuentaExtraccion));
            }
        }

        while(true) {
            System.out.print("Ingrese el ID de la cuenta a la cual depositar: ");
            try {
                idCuentaDeposito = sc.nextInt();
                if(idCuentaDeposito <= 0) {
                    System.out.println(String.format(
                                "Debe ingresar un ID válido (ID ingresado [%d])",
                                idCuentaDeposito));
                    continue;
                }
                break;
            } catch(InputMismatchException e){
                System.out.println(String.format(
                            "Debe ingresar un ID válido (ID ingresado [%s])",
                            idCuentaDeposito));
            }
        }




        PGConnection connDeposito = null,
                     connExtraccion = null;
        //PGXADataSource pgXaDataSourceExtraccion = null,
                       //pgXaDataSourceDeposito = null;
        //XADataSource xaDataSourceExtraccion = null, 
                     //xaDataSourceDeposito = null;
        //XAConnection xaConnExtraccion = null,
                     //xaConnDeposito = null;
        //XAResource xaResExtraccion = null,
                   //xaResDeposito = null;
        //Connection connExtraccion = null, 
                   //connDeposito = null;

        try {

            connDeposito = new PGConnection(
                    "localhost",
                    5550,
                    "banco");
            connExtraccion = new PGConnection(
                    "localhost",
                    5560,
                    "banco");

            //pgXaDataSourceExtraccion = new PGXADataSource();
            //pgXaDataSourceExtraccion.setServerName("localhost");
            //pgXaDataSourceExtraccion.setPortNumber(5550);
            //pgXaDataSourceExtraccion.setDatabaseName("banco");

            //xaDataSourceExtraccion = pgXaDataSourceExtraccion;


            //pgXaDataSourceDeposito = new PGXADataSource();
            //pgXaDataSourceDeposito.setServerName("localhost");
            //pgXaDataSourceDeposito.setPortNumber(5560);
            //pgXaDataSourceDeposito.setDatabaseName("banco");

            //xaDataSourceDeposito = pgXaDataSourceDeposito;


            connDeposito.connect("admin", "admin");
            connExtraccion.connect("admin", "admin");
            //xaConnExtraccion = xaDataSourceExtraccion
                //.getXAConnection("admin", "admin");

            //xaConnDeposito = xaDataSourceDeposito
                //.getXAConnection("admin", "admin");

            //xaResExtraccion = xaConnExtraccion.getXAResource();
            //xaResDeposito = xaConnDeposito.getXAResource();

            //connExtraccion = xaConnExtraccion.getConnection();
            //connDeposito = xaConnDeposito.getConnection();

            //CuentaObjects orm = new CuentaObjects(connExtraccion);
            //Cuenta c = orm.getById(idCuentaExtraccion);
            //System.out.println(c);

        } catch(SQLException e) {
            System.out.println(String.format(
                        "[ERROR] NO SE PUDO CONECTAR [%s]", 
                        e));
            System.exit(1);
        }

        System.out.println("*** CONECTADO ***");

        try {
            DistributedTransaction t = new DistributedTransaction(
                    101, 
                    new byte[]{0x01},
                    new byte[]{0x02});
            //Xid xid = new MyXid(101, new byte[]{0x01}, new byte[]{0x02});

            //COMIENZO TRANSACCION
            connDeposito.startTransaction(t);
            connExtraccion.startTransaction(t);

            System.out.println("*** TRANSACCION COMENZADA ***");
            //xaResExtraccion.start(xid, XAResource.TMNOFLAGS);
            //xaResDeposito.start(xid, XAResource.TMNOFLAGS);

            /*
             * Hackz hackz hackz que modifican las BDs
             * */


            connDeposito.endTransaction(t);
            connExtraccion.endTransaction(t);
            System.out.println("*** TRANSACCION FINALIZADA ***");
            //xaResExtraccion.end(xid, XAResource.TMSUCCESS);
            //xaResDeposito.end(xid, XAResource.TMSUCCESS);
            //FIN TRANSACCION

            //VERIFICAR SI ANDUVO BIEN PARA COMMITEAR
            if (connDeposito.canPrepareForCommit(t) 
                    && connExtraccion.canPrepareForCommit(t)) {
                System.out.println("*** COMMITEANDO ***");
                connDeposito.commit(t);
                connExtraccion.commit(t);
            } else {
                System.out.println("*** ROLLBACKKKK ***");
                connDeposito.rollback(t);
                connExtraccion.rollback(t);
            }
            //int retXAExtraccion = xaResExtraccion.prepare(xid);
            //int retXADeposito = xaResDeposito.prepare(xid);

            //if (retXAExtraccion == XAResource.XA_OK && retXADeposito == XAResource.XA_OK) {
                //System.out.println("*** COMMITEANDO ***");
                //xaResExtraccion.commit(xid, false);
                //xaResDeposito.commit(xid, false);
            //} else {
                ////ROLLBACK
                //System.out.println("*** ROLLBACKKKK ***");
                //xaResExtraccion.commit(xid, false);
                //xaResExtraccion.rollback(xid);
                //xaResDeposito.rollback(xid);
            //}
        } catch (XAException e) {
            System.out.println(String.format(
                        "[ERROR] pinchose la transaccion [%s]",
                        e));
        }


        /*
        try {

            Statement stmt;
            ResultSet rows;

            System.out.println("*** CONN EXTRACCION ***");
            stmt = connExtraccion.createStatement();
            stmt.pre
            rows = stmt.executeQuery("SELECT * FROM cuentas");
            while (rows.next()) {
                System.out.println(String.format(
                            "[%d] TITULAR %s",
                            rows.getRow(),
                            rows.getString("titular")));
            }

            System.out.println("*** CONN DEPOSITO ***");
            stmt = connDeposito.createStatement();
            rows = stmt.executeQuery("SELECT * FROM cuentas");
            while (rows.next()) {
                System.out.println(String.format(
                            "[%d] TITULAR %s",
                            rows.getRow(),
                            rows.getString("titular")));
            }

            stmt.close();

        } catch(SQLException e){
            System.out.println(String.format(
                        "[ERROR] NO SE PUDO RECUPERAR LA INFORMACION [%s]",
                        e));
            System.exit(1);
        }
        */

        try {
            System.out.println("*** CERRANDO ***");
            connDeposito.close();
            connExtraccion.close();
            //connExtraccion.close();
            //connDeposito.close();

            //xaConnExtraccion.close();
            //xaConnDeposito.close();
        } catch (SQLException e) {
            System.out.println("boe");
        }

        System.out.println("todo joya amiguito :)");
    } 
}
