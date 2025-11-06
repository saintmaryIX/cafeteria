package edu.upc.dsa;

        ;
import edu.upc.dsa.models.*;
import java.util.*;

public interface pedidosMANAGER {

    public void crearPedido(User usuario, HashMap<Producto, Integer> pedido_unuserP);
    public void añadirProductoalmercado(Producto nuevo);
    public Pedido servirPedido();

    public Queue<Pedido> getPedidoscola();

    public List<Producto> getProductosqsevenden();

    public List<User> getUsuarioscreados();

    public void setUsuarioscreados(List<User> usuarioscreados);


}
