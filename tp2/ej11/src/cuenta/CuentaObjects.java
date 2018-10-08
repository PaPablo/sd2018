package cuenta;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import db.ORM;

public class CuentaObjects implements ORM<Cuenta, Integer> {

    private Connection conn;

    public CuentaObjects(Connection conn) {
        this.conn = conn;
    }

    public Cuenta getById(Integer id) {
        try {
            Cuenta _cuenta = null;
            Statement _stmt = this.conn.createStatement();
            //Esas inyecciones SQL sí se pueden ver
            ResultSet _rows = _stmt.executeQuery(String.format(
                        "SELECT * FROM cuentas WHERE id = %d",
                        id
                        ));
            if(_rows.next()) {
                _cuenta = new Cuenta(
                        _rows.getInt("id"),
                        _rows.getString("titular"),
                        _rows.getTimestamp("fecha_creacion"),
                        _rows.getBoolean("bloqueada"),
                        _rows.getDouble("saldo")
                        );
            } 

            _stmt.close();
            return _cuenta;

        } catch (SQLException e) {
            return null;
        }

    }
    
}
