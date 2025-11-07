package edu.upc.dsa;

import edu.upc.dsa.models.*;
import edu.upc.dsa.exceptions.*;
import org.apache.log4j.Logger;
import org.junit.*;

import java.util.*;

@SuppressWarnings("ALL")
public class bibliotecaMANAGERImplTest {

    private static final Logger logger = Logger.getLogger(bibliotecaMANAGERImplTest.class);
    private bibliotecaMANAGERImpl manager;

    @Before
    public void setUp() {
        logger.info("Iniciando setUp del test...");
        manager = bibliotecaMANAGERImpl.getInstance();

        // Limpiamos las estructuras
        manager.getLectores().clear();
        manager.getPrestamos().clear();
        manager.getCatalogo().clear();
        manager.getMontones().clear();

        // Añadimos lectores de ejemplo
        Lector lector1 = new Lector("L001", "Juan", "García", "11111111A", "1990-05-12", "Madrid", "Calle A");
        Lector lector2 = new Lector("L002", "Ana", "Pérez", "22222222B", "1992-03-10", "Barcelona", "Calle B");

        manager.getLectores().add(lector1);
        manager.getLectores().add(lector2);

        // Añadimos libros al catálogo con stock inicial
        manager.getCatalogo().put("ISBN001", 2);
        manager.getCatalogo().put("ISBN002", 0); // sin stock
    }

    @After
    public void tearDown() {
        logger.info("Limpiando después del test...");
        manager.getLectores().clear();
        manager.getPrestamos().clear();
        manager.getCatalogo().clear();
        manager.getMontones().clear();
    }

    // ============================================================
    // Test 1: Consultar préstamos de un lector (solo filtrado)
    // ============================================================
    @Test
    public void testConsultarPrestamosDeLector() {
        logger.info("Iniciando testConsultarPrestamosDeLector...");

        // Añadimos préstamos manualmente
        manager.getPrestamos().add(new Prestamo("P001", "L001", "ISBN111", "2025-11-07", "2025-11-21", "En trámite"));
        manager.getPrestamos().add(new Prestamo("P002", "L001", "ISBN222", "2025-11-07", "2025-11-21", "Finalizado"));

        List<Prestamo> prestamosLector1 = manager.consultarPrestamosDeLector("L001");

        Assert.assertNotNull("La lista de préstamos no debe ser nula", prestamosLector1);
        Assert.assertEquals("El lector L001 debería tener 2 préstamos", 2, prestamosLector1.size());

        logger.info("testConsultarPrestamosDeLector finalizado correctamente.");
    }

    // ============================================================
    // Test 2: Excepción si el lector no existe
    // ============================================================
    @Test(expected = LectorNoEncontradoException.class)
    public void testPrestarLibroLectorNoExiste() throws Exception {
        logger.info("Iniciando testPrestarLibroLectorNoExiste...");
        manager.prestarLibro("L999", "ISBN001"); // Lector no existe
    }

    // ============================================================
    // Test 3: Excepción si el libro no está en el catálogo
    // ============================================================
    @Test(expected = LibroNoEncontradoException.class)
    public void testPrestarLibroNoEncontrado() throws Exception {
        logger.info("Iniciando testPrestarLibroNoEncontrado...");
        manager.prestarLibro("L001", "ISBN999"); // Libro inexistente
    }

    // ============================================================
    // Test 4: Excepción si no hay stock
    // ============================================================
    @Test(expected = StockInsuficienteException.class)
    public void testPrestarLibroSinStock() throws Exception {
        logger.info("Iniciando testPrestarLibroSinStock...");
        manager.prestarLibro("L001", "ISBN002"); // Stock = 0
    }

    // ============================================================
    // Test 5: Prestar libro correctamente
    // ============================================================
    @Test
    public void testPrestarLibroCorrectamente() throws Exception {
        logger.info("Iniciando testPrestarLibroCorrectamente...");

        int stockAntes = manager.getCatalogo().get("ISBN001");
        manager.prestarLibro("L001", "ISBN001");
        int stockDespues = manager.getCatalogo().get("ISBN001");

        Assert.assertEquals("El stock debe disminuir en 1", stockAntes - 1, stockDespues);
        Assert.assertEquals("Debe haberse creado un préstamo", 1, manager.getPrestamos().size());

        logger.info("testPrestarLibroCorrectamente finalizado correctamente.");
    }

    // ============================================================
    // Test 6: Almacenar libros en montones
    // ============================================================
    @Test
    public void testAlmacenarLibro() {
        logger.info("Iniciando testAlmacenarLibro...");

        for (int i = 1; i <= 12; i++) {
            Libro libro = new Libro("ID" + i, "ISBN" + i, "Libro" + i, "Editorial", 2025, 1, "Autor", "Ficción");
            manager.almacenarLibro(libro);
        }

        Assert.assertEquals("Debe haber 2 montones", 2, manager.getMontones().size());

        Stack<Libro> primerMonton = null;
        Stack<Libro> ultimoMonton = null;
        int i = 0;
        for (Stack<Libro> m : manager.getMontones()) {
            if (i == 0) primerMonton = m;
            ultimoMonton = m;
            i++;
        }

        Assert.assertNotNull("El primer montón no debe ser nulo", primerMonton);
        Assert.assertEquals("El primer montón debe tener 10 libros", 10, primerMonton.size());

        Assert.assertNotNull("El último montón no debe ser nulo", ultimoMonton);
        Assert.assertEquals("El último montón debe tener 2 libros", 2, ultimoMonton.size());

        logger.info("testAlmacenarLibro finalizado correctamente.");
    }

    // ============================================================
    // Test 7: Catalogar libros
    // ============================================================
    @Test
    public void testCatalogarLibro() {
        logger.info("Iniciando testCatalogarLibro...");

        for (int i = 1; i <= 3; i++) {
            Libro libro = new Libro("ID" + i, "ISBN" + i, "Libro" + i, "Editorial", 2025, 1, "Autor", "Ficción");
            manager.almacenarLibro(libro);
        }

        manager.catalogarLibro();
        manager.catalogarLibro();
        manager.catalogarLibro();

        Assert.assertTrue("No debe haber montones después de catalogar todo", manager.getMontones().isEmpty());
        Assert.assertEquals("Debe haber 3 libros catalogados", 5, manager.getCatalogo().size());

        for (int i = 1; i <= 3; i++) {
            int ejemplares = manager.getCatalogo().get("ISBN" + i);
            Assert.assertEquals("Cada libro debe tener 1 ejemplar", 1, ejemplares);
        }

        logger.info("testCatalogarLibro finalizado correctamente.");
    }
}
