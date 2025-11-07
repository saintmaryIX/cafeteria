package edu.upc.dsa;

import edu.upc.dsa.models.*;
import java.util.*;

import edu.upc.dsa.exceptions.*;

import edu.upc.dsa.utils.RandomUtils;
import org.apache.log4j.Logger;

public interface bibliotecaMANAGER {

    public void añadirLector(Lector lector);
    public void almacenarLibro(Libro libro);
    public void catalogarLibro();
    public void prestarLibro(String idLector, String isbn)
            throws LectorNoEncontradoException, LibroNoEncontradoException, StockInsuficienteException;
    public List<Prestamo> consultarPrestamosDeLector(String idLector); //coge la lista de prestamos y filtra por el id del lector




    public HashMap<String, Integer> getCatalogo();
    public List<Lector> getLectores();
    public Queue<Stack<Libro>> getMontones();
    public List<Prestamo> getPrestamos();







}
