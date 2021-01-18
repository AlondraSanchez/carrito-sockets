/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servidor;

import productos.Producto;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 *
 * @author user1
 */
public class Catalogo {

    private ArrayList<Producto> catalogo;

//    public Catalogo() {
//        catalogo = new ArrayList<>();
//        Producto p = new Producto();
//        p.setCantidad(5);
//        p.setDescripcion("Es una koka");
//        p.setNombre("Koka");
//        p.setPrecio(15.0);
//        p.setUrlImagen("");
//        catalogo.add(p);
//        p = new Producto();
//        p.setCantidad(5);
//        p.setDescripcion("Son cheetos");
//        p.setNombre("Cheetos");
//        p.setPrecio(10.0);
//        p.setUrlImagen("");
//        catalogo.add(p);
//    }

    public boolean verificarStock(String nombre, int cantidad) {
        return catalogo.stream().filter((producto) -> (producto.getNombre().equalsIgnoreCase(nombre))).anyMatch((producto) -> (producto.getCantidad() >= cantidad));
    }

    public boolean verificarCompra(String nombre) {
        return catalogo.stream().anyMatch((producto) -> (producto.getNombre().equalsIgnoreCase(nombre)));
    }

    public Producto getCompra(String nombre, int cantidad) {
        Producto p = new Producto();
        catalogo.stream().filter((producto) -> (producto.getNombre().equalsIgnoreCase(nombre))).map((producto) -> {
            p.setNombre(producto.getNombre());
            return producto;
        }).map((producto) -> {
            p.setCantidad(cantidad);
            p.setDescripcion(producto.getDescripcion());
            return producto;
        }).map((producto) -> {
            p.setPrecio(producto.getPrecio());
            return producto;
        }).forEachOrdered((producto) -> {
            p.setUrlImagen(producto.getUrlImagen());
        });
        return p;
    }

    public String viewCatalogo() {
        String mensaje = "-----------------------------------------\n";
        for (Producto producto : catalogo) {
            mensaje += "Nombre: " + producto.getNombre() + "\n";
            mensaje += "\tPrecio: " + producto.getPrecio() + "\n";
            mensaje += "\tDescripcion: " + producto.getDescripcion() + "\n";
            mensaje += "\tCantidad en stock: " + producto.getCantidad() + "\n";
            mensaje += "\tImagen disponible en " + producto.getUrlImagen() + "\n";
            mensaje += "-----------------------------------------\n";
        }
        return mensaje;
    }
    
    public ArrayList<Producto> getCatalogoNoBase() {
        return catalogo;
    }
    
    public void vender(ArrayList<Producto> venta){
        venta.forEach((Producto productoVenta) -> {
            catalogo.stream().filter((productoCatalogo) -> (productoVenta.getNombre().equals(productoCatalogo.getNombre()))).forEachOrdered((productoCatalogo) -> {
                productoCatalogo.setCantidad(productoCatalogo.getCantidad()-productoVenta.getCantidad());
            });
        });
    }

    public Catalogo() {
        catalogo = new ArrayList<>();
        Connection conn;
        try {
            // The newInstance() call is a work around for some
            // broken Java implementations
            Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException ex) {
        }
        try {
            PreparedStatement pr;
            ResultSet rs;

            conn = DriverManager.getConnection("jdbc:mysql://localhost/tienda?user=root&password=root&useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC&&&useSSL=false");

            String sql = "select * from Producto";
            //Se ejecuta la sentencia
            pr = conn.prepareStatement(sql);
            rs = pr.executeQuery();
            //Entra al bucle cuando hay resultados
            while (rs.next()) {
                Producto p = new Producto();
                p.setNombre(rs.getString("nombre"));
                p.setCantidad(rs.getInt("cantidad"));
                p.setUrlImagen(rs.getString("urlImagen"));
                p.setPrecio((rs.getDouble("precio")));
                p.setDescripcion(rs.getString("descripcion"));
                catalogo.add(p);
            }
        } catch (SQLException ex) {
            // handle any errors
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
        }
    }
}
