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

        PGConnection connDeposito = null,
                     connExtraccion = null;

        CuentaObjects ormDeposito = null,
                      ormExtraccion = null;

        Cuenta cuentaDeposito = null,
               cuentaExtraccion = null;

        try {

            connDeposito = new PGConnection(
                    "localhost",
                    5550,
                    "banco");
            connExtraccion = new PGConnection(
                    "localhost",
                    5560,
                    "banco");

            ormDeposito = new CuentaObjects(connDeposito
                    .connect("admin", "admin"));
            ormExtraccion = new CuentaObjects(connExtraccion
                    .connect("admin", "admin"));

        } catch(SQLException e) {
            System.out.println(String.format(
                        "[ERROR] NO SE PUDO CONECTAR [%s]", 
                        e));
            System.exit(1);
        }

        System.out.println("*** CONECTADO ***");


        try {
            //Ingresar monto
            while(true) {
                System.out.print("Ingrese un monto a transferir: ");
                try {
                    monto = Integer.parseInt(sc.nextLine());
                    if(monto <= 0) {
                        System.out.println(String.format(
                                    "Debe ingresar un monto mayor que 0 (monto ingresado [%d])",
                                    monto));
                        continue;
                    }
                    break;
                } catch(InputMismatchException 
                        | NumberFormatException e){
                    System.out.println(String.format(
                                "Debe ingresar un monto válido",
                                monto));
                }
            }

            //Ingresar cuenta a extraer
            while(true) {
                System.out.print("Ingrese el ID de la cuenta de la cual extraer: ");
                try {
                    idCuentaExtraccion = Integer.parseInt(sc.nextLine());
                    cuentaExtraccion = ormExtraccion.getById(idCuentaExtraccion);

                    //La cuenta debe existir
                    if (cuentaExtraccion == null) {
                        System.out.println(String.format(
                                    "La cuenta ingresada no existe (ID ingresado [%d])",
                                    idCuentaExtraccion));
                        continue;
                    }

                    //No debe estar bloqueada
                    if (cuentaExtraccion.isBlocked()) {
                        System.out.println(String.format(
                                    "La cuenta %d está bloqueada. No se puede extraer", 
                                    idCuentaExtraccion));
                        continue;

                    }

                    //Se debe poder extraer el monto indicado
                    if (!cuentaExtraccion.canExtract(monto)) {
                        System.out.println(String.format(
                                    "La cuenta %d no tiene tanto saldo para extrar\n[Saldo de la cuenta: %f\nSaldo a extraer: %d]",
                                    idCuentaExtraccion,
                                    cuentaExtraccion.getSaldo(),
                                    monto));
                        continue;
                    }

                    break;
                } catch(InputMismatchException 
                        | NumberFormatException e){
                    System.out.println(String.format(
                                "Debe ingresar un ID válido",
                                idCuentaExtraccion));
                }
            }

            //Ingresar cuenta a depositar
            while(true) {
                System.out.print("Ingrese el ID de la cuenta a la cual depositar: ");
                try {
                    idCuentaDeposito = Integer.parseInt(sc.nextLine());
                    cuentaDeposito = ormDeposito.getById(idCuentaDeposito);

                    //La cuenta debe existir
                    if (cuentaDeposito == null) {
                        System.out.println(String.format(
                                    "La cuenta ingresada no existe (ID ingresado [%d])",
                                    idCuentaDeposito));
                        continue;
                    }
                    
                    //No debe estar bloqueada
                    if (cuentaDeposito.isBlocked()) {
                        System.out.println(String.format(
                                    "La cuenta %d está bloqueada. No se puede extraer", 
                                    idCuentaExtraccion));
                        continue;
                    }

                    break;
                } catch(InputMismatchException 
                        | NumberFormatException e){
                    System.out.println(String.format(
                                "Debe ingresar un ID válido",
                                idCuentaDeposito));
                }
            }
        } catch(NoSuchElementException e) {
            System.out.println();
            System.out.println("Hasta luego!");
            System.exit(0);
        }




        //Intentar hacer operación
        try {
            DistributedTransaction t = new DistributedTransaction(
                    101, 
                    new byte[]{0x01},
                    new byte[]{0x02});

            //COMIENZO TRANSACCION
            connDeposito.startTransaction(t);
            connExtraccion.startTransaction(t);

            System.out.println("*** TRANSACCION COMENZADA ***");


            //DEPOSITAR
            cuentaDeposito.deposit(monto);
            //EXTRAER
            cuentaExtraccion.extract(monto);

            connDeposito.endTransaction(t);
            connExtraccion.endTransaction(t);
            System.out.println("*** TRANSACCION FINALIZADA ***");
            //FIN TRANSACCION

            System.out.println("*** CUENTA QUE SE EXTRAJO ***");
            System.out.println(cuentaExtraccion);
            System.out.println("*** CUENTA QUE SE DEPOSITO ***");
            System.out.println(cuentaDeposito);
            boolean canCommit = false;

            while(true) {
                System.out.print("¿Desea confirmar la operación? [y/n]: ");
                String input = sc.nextLine().trim().toLowerCase();
                if(input == "y" || input == "yes") {
                    canCommit = true;
                    break;
                } else if (input == "n" || input == "no") {
                    break;
                }
            }

            //VERIFICAR SI ANDUVO BIEN PARA COMMITEAR
            if (connDeposito.canPrepareForCommit(t) 
                    && connExtraccion.canPrepareForCommit(t)
                    && canCommit) {
                System.out.println("*** COMMITEANDO ***");
                connDeposito.commit(t);
                connExtraccion.commit(t);
            } else {
                System.out.println("*** ROLLBACKKKK ***");
                connDeposito.rollback(t);
                connExtraccion.rollback(t);
            }
        } catch (XAException e) {
            System.out.println(String.format(
                        "[ERROR] pinchose la transaccion [%s]",
                        e));
        }


        try {
            System.out.println("*** CERRANDO ***");
            connDeposito.close();
            connExtraccion.close();
        } catch (SQLException e) {
            System.out.println("boe");
        }

        System.out.println("todo joya amiguito :)");
    } 
}
