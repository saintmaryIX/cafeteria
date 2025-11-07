package edu.upc.dsa.models;

public class Prestamo {
    private String idPrestamo;
    private String idLector;
    private String isbnLibro;
    private String fechaPrestamo;
    private String fechaDevolucion;
    private String estado; // "En trámite", "Finalizado", etc.

    // 🔹 Constructor vacío
    public Prestamo() {}

    // 🔹 Constructor con parámetros
    public Prestamo(String idPrestamo, String idLector, String isbnLibro,
                    String fechaPrestamo, String fechaDevolucion, String estado) {
        this.idPrestamo = idPrestamo;
        this.idLector = idLector;
        this.isbnLibro = isbnLibro;
        this.fechaPrestamo = fechaPrestamo;
        this.fechaDevolucion = fechaDevolucion;
        this.estado = estado;
    }

    // 🔸 Getters y Setters
    public String getIdPrestamo() {
        return idPrestamo;
    }

    public void setIdPrestamo(String idPrestamo) {
        this.idPrestamo = idPrestamo;
    }

    public String getIdLector() {
        return idLector;
    }

    public void setIdLector(String idLector) {
        this.idLector = idLector;
    }

    public String getIsbnLibro() {
        return isbnLibro;
    }

    public void setIsbnLibro(String isbnLibro) {
        this.isbnLibro = isbnLibro;
    }

    public String getFechaPrestamo() {
        return fechaPrestamo;
    }

    public void setFechaPrestamo(String fechaPrestamo) {
        this.fechaPrestamo = fechaPrestamo;
    }

    public String getFechaDevolucion() {
        return fechaDevolucion;
    }

    public void setFechaDevolucion(String fechaDevolucion) {
        this.fechaDevolucion = fechaDevolucion;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }
}
