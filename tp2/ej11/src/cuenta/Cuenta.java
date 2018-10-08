package cuenta;

import java.sql.Timestamp;

public class Cuenta {
        
    private int id;
    private String titular;
    private Timestamp fechaCreacion;
    private boolean bloqueada;
    private double saldo;

    public Cuenta(int id, String titular, Timestamp fechaCreacion, boolean bloqueada, double saldo) {
        this.id = id;
        this.titular = titular;
        this.fechaCreacion = fechaCreacion;
        this.bloqueada = bloqueada;
        this.saldo = saldo;
    }

    public String toString() {
        return String.format(
                "ID: %d\nTitular: %s\nFecha de creacion: %s\nEsta bloqueada?: %s\n Saldo: %f",
                this.id,
                this.titular,
                this.fechaCreacion,
                this.bloqueada,
                this.saldo);
    }

    public double getSaldo() {
        return this.saldo;
    }

    public boolean canExtract(double amount) {
        return (this.saldo - amount) > 0;
    }

    public double extract(double amount) {
        this.saldo -= amount;
        return this.saldo;
    }

    public double deposit(double amount) {
        this.saldo += amount;
        return this.saldo;
    }
}
