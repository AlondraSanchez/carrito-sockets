/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package productos;

import java.util.ArrayList;

/**
 *
 * @author user1
 */
public class Carrito {

    private ArrayList<Producto> carrito;

    public Carrito() {
        carrito = new ArrayList<>();
    }

    public void addProduct(Producto p) {
        carrito.add(p);
    }

    public void removeProduct(int index) {
        carrito.remove(index);
    }

    public void modificarCantidad(int index, int cantidad) {
        carrito.get(index).setCantidad(cantidad);
    }

    public String verCarrito() {
        String mensaje = "-----------------------------------------\n";
        for (Producto producto : carrito) {
            mensaje += "Nombre: " + producto.getNombre() + "\n";
            mensaje += "\tPrecio: " + producto.getPrecio() + "\n";
            mensaje += "\tDescripcion: " + producto.getDescripcion() + "\n";
            mensaje += "\tCantidad en stock: " + producto.getCantidad() + "\n";
            mensaje += "\tImagen disponible en " + producto.getUrlImagen() + "\n";
            mensaje += "-----------------------------------------\n";
        }
        return mensaje;
    }

}
