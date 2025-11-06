package edu.upc.dsa.models;

import edu.upc.dsa.models.*;
import java.util.*;

public class Producto {

    int Precio;
    int cantidad_almacen;
    String nombreproduct;

    public Producto() {}

    public Producto(String nombreproductP, int PrecioP, int cantidad_almacenP) {
        this.Precio = PrecioP;
        this.cantidad_almacen = cantidad_almacenP;
        this.nombreproduct = nombreproductP;
    }

    public int getPrecio() {
        return this.Precio;
    }

    public void setPrecio(int PrecioP) {
        this.Precio = PrecioP;
    }

    public int getCantidad_almacen() { return this.cantidad_almacen;}

    public void setCantidad_almacen(int cantidad_almacenP) { this.cantidad_almacen = cantidad_almacenP;}

    public String getNombreproduct() {
        return this.nombreproduct;
    }

    public void setNombreproduct(String nombreproductP) {
        this.nombreproduct = nombreproductP;
    }


}
