package edu.upc.dsa;



import edu.upc.dsa.models.*;
import java.util.*;

import edu.upc.dsa.utils.RandomUtils;
import org.apache.log4j.Logger;


@SuppressWarnings("ALL")

public class pedidosMANAGERImpl implements pedidosMANAGER {

    private static final Logger logger = Logger.getLogger(pedidosMANAGERImpl.class);

    private static pedidosMANAGERImpl instance;

    public Queue<Pedido> pedidoscola; //la cola
    public List<Producto> productosqsevenden; //la cola
    public List<User> usuarioscreados;


    private pedidosMANAGERImpl() {
        this.pedidoscola = new LinkedList<>();
        this.productosqsevenden = new ArrayList<>();
    }

    public static pedidosMANAGERImpl getInstance(){
        if(instance == null){
            instance = new pedidosMANAGERImpl();
        }
        return instance;
    }

    public void añadirProductoalmercado(Producto nuevo) {
        for (Producto p : productosqsevenden) {
            if (p.getNombreproduct().equals(nuevo.getNombreproduct())) {
                logger.error("Producto ya existe");
                return;
            }
        }
        productosqsevenden.add(nuevo);
    }


    public void crearPedido(User usuario, HashMap<Producto, Integer> pedido_unuserP){
        //checkeo si el user ha sido creado

        boolean userExiste = false;
        for (User u : usuarioscreados) {
            if (u.getNombreuser().equals(usuario.getNombreuser())) {
                userExiste = true;
                break;
            }
        }

        if (!userExiste) {
            logger.error("❌ El usuario " + usuario.getNombreuser() + " no está registrado en el sistema.");
            return;
             // cancelamos el pedido directamente
        }

        String id = RandomUtils.getId();
        Pedido nuevopedido = new Pedido(usuario, id); // pedido creado

        for (Map.Entry<Producto, Integer> entrada : pedido_unuserP.entrySet()) {
            Producto productoPedido = entrada.getKey();        // productoPedido es una clave del HashMap
            int cantidadPedida = entrada.getValue();            // cantidad que se quiere de ese producto

            Producto productoEnTienda = null;                   // luego usaré esto para guardar el producto real de la lista

            // 1️⃣ miro si el producto que me pasan existe en la lista de productos que se venden
            for (Producto p : productosqsevenden) {
                if (p.getNombreproduct().equals(productoPedido.getNombreproduct())) { //si encuentra producto

                    // log avisando que el producto sí existe
                    logger.info("El producto " + p.getNombreproduct() + " existe en la tienda.");

                    productoEnTienda = p; // cojo el producto que ya existe en la lista

                    // 2️⃣ compruebo si hay suficiente cantidad disponible del producto
                    if (cantidadPedida > productoEnTienda.getCantidad_almacen()) {
                        // log avisando que no hay suficiente cantidad
                        logger.error("No hay suficiente stock de " + productoEnTienda.getNombreproduct() +
                                ". Pedidas: " + cantidadPedida +
                                ", disponibles: " + productoEnTienda.getCantidad_almacen());
                        // no se puede crear el pedido
                        return;
                    }

                    // 3️⃣ si hay suficiente cantidad, resto del stock
                    productoEnTienda.setCantidad_almacen(productoEnTienda.getCantidad_almacen() - cantidadPedida);

                    // 4️⃣ y añado ese producto al pedido del usuario
                    nuevopedido.addProductTOpedido_unuser(productoEnTienda, cantidadPedida);

                    // salgo del bucle interno porque ya encontré el producto
                    break;
                }
            }

            // 5️⃣ si no se encontró el producto en la tienda, aviso y corto la función
            if (productoEnTienda == null) {
               logger.error("no existe el producto q me has dicho en la tienda");
                return;
            }
        }

        pedidoscola.add(nuevopedido);

    }

    public Pedido servirPedido() {
        logger.info("🚚 Intentando servir el siguiente pedido...");

        // 1️⃣ Comprobar si hay pedidos pendientes
        if (pedidoscola.isEmpty()) {
            logger.warn("⚠️ No hay pedidos pendientes para servir.");
            return null;
        }

        // 2️⃣ Sacar el primer pedido de la cola
        Pedido pedidoServido = pedidoscola.poll();
        // poll() lo saca y lo devuelve
        logger.info("✅ Pedido " + pedidoServido.getid() + " servido.");


        // 5️⃣ Añadir el pedido a la lista de pedidos servidos del usuario
        User usuario = pedidoServido.getUsuario();
        usuario.addPedido(pedidoServido);
        logger.info("📦 Pedido " + pedidoServido.getid() + " añadido al historial del usuario " + usuario.getNombreuser());

        // 6️⃣ Devolver el pedido servido
        return pedidoServido;
    }

    // ✅ Getters y setters para los tests
    public Queue<Pedido> getPedidoscola() {
        return pedidoscola;
    }

    public List<Producto> getProductosqsevenden() {
        return productosqsevenden;
    }

    public List<User> getUsuarioscreados() {
        return usuarioscreados;
    }

    public void setUsuarioscreados(List<User> usuarioscreados) {
        this.usuarioscreados = usuarioscreados;
    }



}
