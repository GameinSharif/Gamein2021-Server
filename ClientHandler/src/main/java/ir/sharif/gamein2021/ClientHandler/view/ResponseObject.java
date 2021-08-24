package ir.sharif.gamein2021.ClientHandler.view;

import java.io.Serializable;

public class ResponseObject<T> implements Serializable {
    public int type;
    public T data;


    public ResponseObject(int type, T data) {
        this.type = type;
        this.data = data;
    }

    public ResponseObject(int type) {
        this.type = type;
    }
}
