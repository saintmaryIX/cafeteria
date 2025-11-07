package edu.upc.dsa.models;

public class Libro {
    private String id;
    private String isbn;
    private String titulo;
    private String editorial;
    private int añoPublicacion;
    private int edicion;
    private String autor;
    private String tematica;

    // 🔹 Constructor vacío
    public Libro() {}

    // 🔹 Constructor con parámetros
    public Libro(String id, String isbn, String titulo, String editorial,
                 int añoPublicacion, int edicion, String autor, String tematica) {
        this.id = id;
        this.isbn = isbn;
        this.titulo = titulo;
        this.editorial = editorial;
        this.añoPublicacion = añoPublicacion;
        this.edicion = edicion;
        this.autor = autor;
        this.tematica = tematica;
    }

    // 🔸 Getters y Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getEditorial() {
        return editorial;
    }

    public void setEditorial(String editorial) {
        this.editorial = editorial;
    }

    public int getAñoPublicacion() {
        return añoPublicacion;
    }

    public void setAñoPublicacion(int añoPublicacion) {
        this.añoPublicacion = añoPublicacion;
    }

    public int getEdicion() {
        return edicion;
    }

    public void setEdicion(int edicion) {
        this.edicion = edicion;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public String getTematica() {
        return tematica;
    }

    public void setTematica(String tematica) {
        this.tematica = tematica;
    }
}
