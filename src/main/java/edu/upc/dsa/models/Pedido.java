package edu.upc.dsa.models;

import edu.upc.dsa.models.*;
import java.util.*;

import java.util.HashMap;

public class Pedido {

    private User usuario;
    String id;
    private HashMap<Producto, Integer> pedido_unuser;

    public Pedido() {
        this.pedido_unuser = new HashMap<>();
    }

    public Pedido(User usuarioP, String idP) {
        this.usuario = usuarioP;
        this.pedido_unuser = new HashMap<>();
        this.id = idP;
    }

    // Añadir producto al pedido
    public void addProductTOpedido_unuser(Producto producto, int quantity) {
        this.pedido_unuser.put(producto, quantity);
    }

    public HashMap<Producto, Integer> getPedido_unuser() {
        return pedido_unuser;
    }

    public User getUsuario() {
        return usuario;
    }

    public String getid() {
        return this.id;
    }

    public void setid(String id) {
        this.id = id;
    }
}


