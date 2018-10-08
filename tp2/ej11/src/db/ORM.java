package db;

import java.sql.SQLException;

public interface ORM<T,U> {
   public T getById(U _id) throws SQLException; 
}
