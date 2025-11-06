package edu.upc.dsa;

        ;
import edu.upc.dsa.models.*;
import java.util.*;

public interface pedidosMANAGER {

    public void crearPedido(User usuario, HashMap<Producto, Integer> pedido_unuserP);
    public void añadirProductoalmercado(Producto nuevo);
    public Pedido servirPedido();

}
