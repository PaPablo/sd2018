package db;

public interface ORM<T,U> {
   public T getById(U _id); 
}
