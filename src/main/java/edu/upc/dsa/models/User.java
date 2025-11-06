package edu.upc.dsa.models;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class User {
    private String nombreuser;
    private List<Pedido> pedidosServidos;  // 🔹 lista de pedidos atendidos del usuario

    // 🔸 Constructor
    public User(String nombreuser) {
        this.nombreuser = nombreuser;
        this.pedidosServidos = new ArrayList<>();
    }

    // 🔸 Getter y setter
    public String getNombreuser() {
        return nombreuser;
    }

    public void setNombreuser(String nombreuser) {
        this.nombreuser = nombreuser;
    }

    public List<Pedido> getPedidosServidos() {
        return pedidosServidos;
    }

    // 🔹 Añadir un pedido a la lista del usuario
    public void addPedido(Pedido pedido) {
        if (pedido != null) {
            this.pedidosServidos.add(pedido);
        }
    }

}
