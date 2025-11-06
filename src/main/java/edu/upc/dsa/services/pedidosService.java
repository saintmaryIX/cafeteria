package edu.upc.dsa.services;

import edu.upc.dsa.pedidosMANAGER;
import edu.upc.dsa.pedidosMANAGERImpl;
import edu.upc.dsa.models.*;
import io.swagger.annotations.*;

import javax.ws.rs.*;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.*;

@Api(value = "/pedidos", description = "Endpoint para gestionar pedidos y productos")
@Path("/pedidos")
public class pedidosService {

    private final pedidosMANAGER manager;

    public pedidosService() {
        this.manager = pedidosMANAGERImpl.getInstance();

        // 🔹 Datos de ejemplo para no iniciar vacío
        if (this.manager.getProductosqsevenden().isEmpty()) {
            Producto cafe = new Producto("Café", 2, 10);
            Producto bocadillo = new Producto("Bocadillo", 3, 5);
            Producto croissant = new Producto("Croissant", 4, 3);

            this.manager.añadirProductoalmercado(cafe);
            this.manager.añadirProductoalmercado(bocadillo);
            this.manager.añadirProductoalmercado(croissant);

            User u1 = new User("Juan");
            User u2 = new User("Ana");
            List<User> lista = new ArrayList<>();
            lista.add(u1);
            lista.add(u2);
            this.manager.setUsuarioscreados(lista);
        }
    }

    // ============================================================
    // 🔸 GET: Lista de productos disponibles
    // ============================================================
    @GET
    @ApiOperation(value = "Obtener todos los productos del mercado", notes = "Devuelve la lista de productos disponibles actualmente")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Operación exitosa", response = Producto.class, responseContainer = "List")
    })
    @Path("/productos")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getProductos() {
        List<Producto> productos = this.manager.getProductosqsevenden();
        GenericEntity<List<Producto>> entity = new GenericEntity<List<Producto>>(productos) {};
        return Response.status(200).entity(entity).build();
    }

    // ============================================================
    // 🔸 POST: Crear nuevo pedido
    // ============================================================
    @POST
    @ApiOperation(value = "Crear un nuevo pedido", notes = "Crea un pedido para un usuario con los productos seleccionados")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Pedido creado correctamente", response = Pedido.class),
            @ApiResponse(code = 404, message = "Usuario o producto no encontrado"),
            @ApiResponse(code = 500, message = "Error de validación en la petición")
    })
    @Path("/nuevo")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response crearPedido(PedidoRequest pedidoRequest) {
        if (pedidoRequest.getUsuario() == null || pedidoRequest.getProductos() == null) {
            return Response.status(500).entity("Petición no válida").build();
        }

        // Buscamos el usuario en la lista
        User user = null;
        for (User u : this.manager.getUsuarioscreados()) {
            if (u.getNombreuser().equals(pedidoRequest.getUsuario())) {
                user = u;
                break;
            }
        }

        if (user == null) {
            return Response.status(404).entity("Usuario no encontrado").build();
        }

        HashMap<Producto, Integer> mapa = new HashMap<>();
        for (ProductoCantidad pc : pedidoRequest.getProductos()) {
            Producto p = null;
            for (Producto tienda : this.manager.getProductosqsevenden()) {
                if (tienda.getNombreproduct().equals(pc.getNombre())) {
                    p = tienda;
                    break;
                }
            }
            if (p == null) {
                return Response.status(404).entity("Producto " + pc.getNombre() + " no existe").build();
            }
            mapa.put(p, pc.getCantidad());
        }

        this.manager.crearPedido(user, mapa);
        return Response.status(201).entity("Pedido creado correctamente").build();
    }

    // ============================================================
    // 🔸 PUT: Servir pedido
    // ============================================================
    @PUT
    @ApiOperation(value = "Servir el siguiente pedido en cola", notes = "Sirve el primer pedido pendiente")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Pedido servido", response = Pedido.class),
            @ApiResponse(code = 404, message = "No hay pedidos pendientes")
    })
    @Path("/servir")
    @Produces(MediaType.APPLICATION_JSON)
    public Response servirPedido() {
        Pedido pedidoServido = this.manager.servirPedido();
        if (pedidoServido == null) {
            return Response.status(404).entity("No hay pedidos pendientes").build();
        }
        return Response.status(200).entity(pedidoServido).build();
    }

    // ============================================================
    // 🔸 GET: Pedidos completados de un usuario
    // ============================================================
    @GET
    @ApiOperation(value = "Obtener pedidos servidos de un usuario", notes = "Devuelve los pedidos que ya han sido entregados a un usuario")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Operación exitosa", response = Pedido.class, responseContainer = "List"),
            @ApiResponse(code = 404, message = "Usuario no encontrado")
    })
    @Path("/usuarios/{nombre}/completados")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPedidosCompletados(@PathParam("nombre") String nombre) {
        for (User u : this.manager.getUsuarioscreados()) {
            if (u.getNombreuser().equals(nombre)) {
                List<Pedido> pedidos = u.getPedidosServidos();
                GenericEntity<List<Pedido>> entity = new GenericEntity<List<Pedido>>(pedidos) {};
                return Response.status(200).entity(entity).build();
            }
        }
        return Response.status(404).entity("Usuario no encontrado").build();
    }

    // ============================================================
    // 🔸 Clase interna: PedidoRequest (para POST)
    // ============================================================
    public static class PedidoRequest {
        private String usuario;
        private List<ProductoCantidad> productos;

        public PedidoRequest() {}

        public String getUsuario() { return usuario; }
        public void setUsuario(String usuario) { this.usuario = usuario; }

        public List<ProductoCantidad> getProductos() { return productos; }
        public void setProductos(List<ProductoCantidad> productos) { this.productos = productos; }
    }

    public static class ProductoCantidad {
        private String nombre;
        private int cantidad;

        public ProductoCantidad() {}

        public String getNombre() { return nombre; }
        public void setNombre(String nombre) { this.nombre = nombre; }

        public int getCantidad() { return cantidad; }
        public void setCantidad(int cantidad) { this.cantidad = cantidad; }
    }
}

