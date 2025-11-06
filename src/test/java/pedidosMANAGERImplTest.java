package edu.upc.dsa;

import edu.upc.dsa.models.*;
import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

@SuppressWarnings("ALL")

public class pedidosMANAGERImplTest {

    private static final Logger logger = Logger.getLogger(pedidosMANAGERImplTest.class);
    private pedidosMANAGERImpl manager;

    @Before
    public void setUp() {
        logger.info("🧪 Iniciando setUp del test...");

        // 🔹 Inicializamos el singleton
        manager = pedidosMANAGERImpl.getInstance();

        // 🔹 Limpiamos estructuras para evitar interferencias entre tests
        manager.pedidoscola.clear();
        manager.productosqsevenden.clear();
        manager.usuarioscreados = new ArrayList<>();

        // 🔹 Creamos productos disponibles en el mercado
        Producto cafe = new Producto("Café", 2, 10);
        Producto bocadillo = new Producto("Bocadillo", 3, 5);
        Producto croissant = new Producto("Croissant", 4, 2);

        manager.añadirProductoalmercado(cafe);
        manager.añadirProductoalmercado(bocadillo);
        manager.añadirProductoalmercado(croissant);

        // 🔹 Creamos usuarios del sistema
        User user1 = new User("Juan");
        User user2 = new User("Ana");
        manager.usuarioscreados.add(user1);
        manager.usuarioscreados.add(user2);
    }

    @After
    public void tearDown() {
        logger.info("🧹 Limpiando después del test...");
        manager.pedidoscola.clear();
        manager.productosqsevenden.clear();
        manager.usuarioscreados.clear();
    }

    // ============================================================
    // 🔸 Test 1: Añadir pedido correctamente
    // ============================================================
    @Test
    public void testCrearPedidoCorrectamente() {
        logger.info("🚀 Iniciando testCrearPedidoCorrectamente...");

        // 🧾 Preparar pedido (2 cafés y 1 bocadillo)
        HashMap<Producto, Integer> pedidoJuan = new HashMap<>();
        pedidoJuan.put(new Producto("Café", 2, 10), 2);
        pedidoJuan.put(new Producto("Bocadillo", 3, 5), 1);

        User userJuan = manager.usuarioscreados.get(0);

        manager.crearPedido(userJuan, pedidoJuan);

        // ✅ Comprobaciones
        Assert.assertEquals("Debe haber 1 pedido en la cola", 1, manager.pedidoscola.size());

        Pedido pedido = manager.pedidoscola.peek();
        Assert.assertNotNull("El pedido no debe ser nulo", pedido);
        Assert.assertEquals("El pedido debe pertenecer a Juan", "Juan", pedido.getUsuario().getNombreuser());
        logger.info("✅ testCrearPedidoCorrectamente finalizado con éxito.");
    }

    // ============================================================
    // 🔸 Test 2: Crear pedido con producto inexistente
    // ============================================================
    @Test
    public void testCrearPedidoProductoInexistente() {
        logger.info("🚨 Iniciando testCrearPedidoProductoInexistente...");

        HashMap<Producto, Integer> pedidoErroneo = new HashMap<>();
        pedidoErroneo.put(new Producto("Zumo", 3, 1), 1); // No existe en la tienda

        User userAna = manager.usuarioscreados.get(1);
        manager.crearPedido(userAna, pedidoErroneo);

        // ✅ No debe haberse añadido ningún pedido
        Assert.assertEquals("No debe haberse añadido el pedido", 0, manager.pedidoscola.size());
        logger.info("✅ testCrearPedidoProductoInexistente finalizado correctamente.");
    }

    // ============================================================
    // 🔸 Test 3: Servir pedido correctamente
    // ============================================================
    @Test
    public void testServirPedido() {
        logger.info("🚚 Iniciando testServirPedido...");

        // Creamos un pedido primero
        HashMap<Producto, Integer> pedidoJuan = new HashMap<>();
        pedidoJuan.put(new Producto("Café", 2, 10), 1);

        User userJuan = manager.usuarioscreados.get(0);
        manager.crearPedido(userJuan, pedidoJuan);

        // ✅ Debe haber 1 pedido en cola
        Assert.assertEquals(1, manager.pedidoscola.size());

        // 🚀 Servir el pedido
        Pedido pedidoServido = manager.servirPedido();

        // ✅ Comprobaciones
        Assert.assertNotNull("El pedido servido no debe ser nulo", pedidoServido);
        Assert.assertEquals("La cola debe quedar vacía", 0, manager.pedidoscola.size());
        Assert.assertEquals("El pedido debe pertenecer al usuario correcto", "Juan", pedidoServido.getUsuario().getNombreuser());
        Assert.assertEquals("El usuario debe tener 1 pedido servido", 1, userJuan.getPedidosServidos().size());
        logger.info("✅ testServirPedido finalizado correctamente.");
    }

    // ============================================================
    // 🔸 Test 4: Servir sin pedidos pendientes
    // ============================================================
    @Test
    public void testServirSinPedidos() {
        logger.info("⚠️ Iniciando testServirSinPedidos...");

        // La cola está vacía
        Assert.assertTrue(manager.pedidoscola.isEmpty());

        // Intentamos servir
        Pedido pedido = manager.servirPedido();

        // ✅ Debe devolver null
        Assert.assertNull("No debe haber pedido para servir", pedido);
        logger.info("✅ testServirSinPedidos finalizado correctamente.");
    }
}

