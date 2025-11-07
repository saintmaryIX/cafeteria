package edu.upc.dsa;

import java.util.*;
import edu.upc.dsa.models.*;

import edu.upc.dsa.exceptions.*;

import edu.upc.dsa.models.Lector;
import edu.upc.dsa.utils.RandomUtils;
import org.apache.log4j.Logger;


@SuppressWarnings("ALL")

public class bibliotecaMANAGERImpl implements bibliotecaMANAGER {

    private static final Logger logger = Logger.getLogger(bibliotecaMANAGERImpl.class);

    private static bibliotecaMANAGERImpl instance;

    public HashMap<String, Integer> catalogo; //hashmap
    public List<Lector> lectores;
    public Queue<Stack<Libro>> montones;
    public List<Prestamo> prestamos;


    private bibliotecaMANAGERImpl() {
        this.catalogo = new HashMap<>();
        this.lectores = new ArrayList<>();
        this.montones = new LinkedList<>();
        this.prestamos = new ArrayList<>();
    }

    public static bibliotecaMANAGERImpl getInstance(){
        if(instance == null){
            instance = new bibliotecaMANAGERImpl();
        }
        return instance;
    }

    @Override //buena práctica hacer override cuando impelementas una interfaz
    public void añadirLector(Lector lector) {
        logger.info("Iniciando añadirLector con ID: " + lector.getId());

        // Buscar si ya existe un lector con ese ID
        Lector lectorExistente = null;
        for (Lector l : lectores) {
            if (l.getId().equals(lector.getId())) {
                lectorExistente = l;
                break;
            }
        }

        // Si existe → actualizamos sus datos
        if (lectorExistente != null) {
            logger.info("El lector ya existe. Actualizando sus datos...");
            lectorExistente.setNombre(lector.getNombre());
            lectorExistente.setApellidos(lector.getApellidos());
            lectorExistente.setDni(lector.getDni());
            lectorExistente.setFechaNacimiento(lector.getFechaNacimiento());
            lectorExistente.setLugarNacimiento(lector.getLugarNacimiento());
            lectorExistente.setDireccion(lector.getDireccion());
            logger.info("Datos del lector actualizados correctamente.");
        }
        // Si no existe → lo añadimos
        else {
            lectores.add(lector);
            logger.info("Nuevo lector añadido: " + lector.getNombre() + " " + lector.getApellidos());
        }

        logger.info(" Fin de añadirLector. Total lectores: " + lectores.size());
    }

    @Override
    public void almacenarLibro(Libro libro) {
        logger.info("Iniciando almacenarLibro con ID: " + libro.getId() + " (ISBN: " + libro.getIsbn() + ")");

        // 1. Si no hay montones, creamos el primero
        if (montones.isEmpty()) {
            Stack<Libro> nuevoMunt = new Stack<>();
            nuevoMunt.push(libro);
            montones.add(nuevoMunt);
            logger.info("Primer montón creado y libro añadido. Total montones: " + montones.size());
            return;
        }

        // 2. Recuperamos el último montón (el de detrás de la cola)
        Stack<Libro> ultimoMunt = null;
        for (Stack<Libro> m : montones) {
            ultimoMunt = m; // al final del bucle, 'ultimoMunt' apunta al último
        }

        // 3. ⃣ Si el último montón tiene menos de 10 libros, añadimos ahí
        if (ultimoMunt.size() < 10) {
            ultimoMunt.push(libro);
            logger.info("Libro añadido al último montón. Libros en el montón: " + ultimoMunt.size());
        }
        // 4️. Si está lleno, creamos un nuevo montón
        else {
            Stack<Libro> nuevoMunt = new Stack<>();
            nuevoMunt.push(libro);
            montones.add(nuevoMunt);
            logger.info("Nuevo montón creado porque el anterior estaba lleno. Total montones: " + montones.size());
        }
    }

    @Override
    public void catalogarLibro() {
        logger.info("Iniciando catalogarLibro...");

        // 1️. Comprobamos si hay montones
        if (montones.isEmpty()) {
            logger.error("No hay libros pendientes de catalogar (almacén vacío).");
            return;
        }

        // 2️. Tomamos el primer montón (el de la cabeza de la cola)
        Stack<Libro> primerMunt = montones.peek();

        // 3️. Sacamos el libro de arriba (LIFO)
        Libro libroCatalogado = primerMunt.pop();
        logger.info("Libro extraído del montón: " + libroCatalogado.getTitulo() + " (ISBN: " + libroCatalogado.getIsbn() + ")");

        // 4️. Si el montón queda vacío, lo quitamos de la cola
        if (primerMunt.isEmpty()) {
            montones.poll();
            logger.info("El primer montón ha quedado vacío y se ha eliminado de la cola.");
        }

        // 5️. Añadimos el libro al catálogo
        String isbn = libroCatalogado.getIsbn();
        if (catalogo.containsKey(isbn)) {
            // Ya existe → aumentamos número de ejemplares
            int actuales = catalogo.get(isbn);
            catalogo.put(isbn, actuales + 1);
            logger.info("Ejemplar añadido al catálogo existente. Total ejemplares de " + isbn + ": " + (actuales + 1));
        } else {
            // No existe → lo añadimos con 1 ejemplar
            catalogo.put(isbn, 1);
            logger.info(" Nuevo libro catalogado con ISBN: " + isbn + " (1 ejemplar disponible)");
        }
    }

    @Override
    public void prestarLibro(String idLector, String isbn) throws LectorNoEncontradoException, LibroNoEncontradoException, StockInsuficienteException{
        logger.info(" Iniciando prestarLibro - Lector: " + idLector + ", ISBN: " + isbn);

        // 1️. Buscar el lector
        Lector lectorEncontrado = null;
        for (Lector l : lectores) {
            if (l.getId().equals(idLector)) {
                lectorEncontrado = l;
                break;
            }
        }

        if (lectorEncontrado == null) {
            logger.error("No existe ningún lector con ID: " + idLector);
            throw new LectorNoEncontradoException("No existe el lector con ID " + idLector);
            // return;
        }

        // 2️. Verificar que el libro está en el catálogo
        if (!catalogo.containsKey(isbn)) {
            logger.error("El libro con ISBN " + isbn + " no está catalogado.");
            throw new LibroNoEncontradoException("El libro con ISBN " + isbn + " no está catalogado");
            // return;
        }

        int ejemplaresDisponibles = catalogo.get(isbn);

        // 3️. Verificar disponibilidad
        if (ejemplaresDisponibles <= 0) {
            logger.error(" No hay ejemplares disponibles del libro con ISBN " + isbn);
            throw new StockInsuficienteException("No hay ejemplares disponibles del libro " + isbn);
            // return;
        }

        // 4️.  Restar un ejemplar disponible
        catalogo.put(isbn, ejemplaresDisponibles - 1);
        logger.info(" Ejemplar prestado. Restan " + (ejemplaresDisponibles - 1) + " disponibles.");

        // 5. Crear el nuevo préstamo
        String idPrestamo = RandomUtils.getId();
        String fechaPrestamo = java.time.LocalDate.now().toString();
        String fechaDevolucion = java.time.LocalDate.now().plusDays(14).toString(); // ejemplo: 14 días de préstamo

        Prestamo nuevoPrestamo = new Prestamo(idPrestamo, idLector, isbn, fechaPrestamo, fechaDevolucion, "En trámite");
        prestamos.add(nuevoPrestamo);

        logger.info(" Préstamo creado con éxito. ID: " + idPrestamo);
    }

    @Override
    public List<Prestamo> consultarPrestamosDeLector(String idLector) {
        logger.info(" IN consultarPrestamosDeLector - idLector=" + idLector);

        List<Prestamo> resultado = new ArrayList<>();
        for (Prestamo p : prestamos) {
            if (p.getIdLector().equals(idLector)) {
                resultado.add(p);
            }
        }

        logger.info(" OUT consultarPrestamosDeLector - total=" + resultado.size());
        return resultado;
    }

    @Override
    //getters de las estructuras de datos
    public HashMap<String, Integer> getCatalogo() {
        return catalogo;
    }

    public List<Lector> getLectores() {
        return lectores;
    }

    public Queue<Stack<Libro>> getMontones() {
        return montones;
    }


    public List<Prestamo> getPrestamos() {
        return prestamos;
    }




}
