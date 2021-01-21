package productos;

import java.util.ArrayList;

public class Carrito {

    private final ArrayList<Producto> carrito;

    public ArrayList<Producto> getCarrito() {
        return carrito;
    }

    public Carrito() {
        carrito = new ArrayList<>();
    }

    public void addProduct(Producto p) {
        carrito.add(p);
    }

    public boolean removeProduct(int index) {
        try {
            carrito.remove(index);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public String getNombre(int indice){
        return carrito.get(indice).getNombre();
    }
    public void modificarCantidad(int index, int cantidad) {
        carrito.get(index).setCantidad(cantidad);
    }

    public String verCarrito() {
        String mensaje = "-----------------------------------------\n";
        mensaje += carrito.size() > 0 ? "" : "Carrito vacío!\n";
        int i = 0;
        for (Producto producto : carrito) {
            mensaje += "Producto #" + i + "\n";
            mensaje += "\tNombre: " + producto.getNombre() + "\n";
            mensaje += "\tPrecio: " + producto.getPrecio() + "\n";
            mensaje += "\tDescripcion: " + producto.getDescripcion() + "\n";
            mensaje += "\tCantidad: " + producto.getCantidad() + "\n";
            mensaje += "\tImagen disponible en " + producto.getUrlImagen() + "\n";
            mensaje += "-----------------------------------------\n";
            i++;
        }
        return mensaje;
    }

    public boolean isValid(int prod) {
        return carrito.size()>prod;
    }
}
