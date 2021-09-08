package ir.sharif.gamein2021.ClientHandler.db;

import java.util.ArrayList;

public interface DBSet<T> {
    T get(long id);
    ArrayList<T> all();
    void add(T t);
    void update(T t);
}
