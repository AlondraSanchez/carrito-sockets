/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servidor;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import productos.Carrito;

/**
 *
 * @author user1
 */
public class Servidor {

    public static void main(String[] args) {
        //Puerto sobre el que va a trabajar el servidor
        int puerto = 7000;
        //Inicializar catalogo
        Catalogo c = new Catalogo();

        try {
            ServerSocket s = new ServerSocket(puerto);
            for (;;) {
                Socket cl = s.accept();
                System.out.println("Conexión establecida desde " + cl.getInetAddress() + ":" + cl.getPort());

                String mensaje = "Bienvenido cliente";

                PrintWriter pw = new PrintWriter(new OutputStreamWriter(cl.getOutputStream()), true);
                pw.println(mensaje);
                pw.flush();

                Carrito carrito = new Carrito();
                String respuesta = "";
                String opciones = "Opciones:"
                        + "\n1.Ver catalogo"
                        + "\n2.Ver carrito"
                        + "\n3.Realizar compra"
                        + "\n4.Cancelar compra\n\t";
                while (!respuesta.equalsIgnoreCase("salir")) {
                    pw.println(opciones);
                    pw.flush();

                    BufferedReader comandoCliente = new BufferedReader(new InputStreamReader(cl.getInputStream()));
                    //Se lee el mensaje recibido
                    respuesta = comandoCliente.readLine();

                    switch (respuesta) {
                        case "1": //ver catalogo
                            mensaje = c.viewCatalogo();
                            mensaje += "Para agregar un producto al carrito escriba el nombre del producto\n\t";
                            pw.println(mensaje);
                            pw.flush();
                            comandoCliente = new BufferedReader(new InputStreamReader(cl.getInputStream()));
                            String compra = comandoCliente.readLine();

                            if (c.verificarCompra(compra)) {

                                mensaje = "Ingrese la cantidad\n\t";
                                pw.println(mensaje);
                                pw.flush();

                                comandoCliente = new BufferedReader(new InputStreamReader(cl.getInputStream()));
                                try {
                                    int cantidad = Integer.parseInt(comandoCliente.readLine());

                                    if (c.verificarStock(compra, cantidad)) {
                                        carrito.addProduct(c.getCompra(compra, cantidad));
                                        mensaje = "Producto agregado exitosamente";
                                    } else {
                                        mensaje = "Stock insuficiente, el producto no ha sido agregado a su carrito";
                                    }
                                } catch (Exception e) {
                                    mensaje = "Cantidad no valida, el producto no ha sido agregado a su carrito";
                                }
                            } else {
                                mensaje = "Producto no valido";
                            }
                            break;
                        case "2": //ver carrito
                            mensaje = carrito.verCarrito();
                            break;
                        case "3": // Hacer compra
                            
                            break;
                        case "4": // Cancelar compra
                            carrito = new Carrito();
                            mensaje = "Carrito vaciado";
                            break;
                    }
                    mensaje += "\nDesea continuar?"
                            + "\nY.Si"
                            + "\nN.No\n\t";
                    pw.println(mensaje);
                    pw.flush();
                    respuesta = comandoCliente.readLine().equalsIgnoreCase("n") ? "salir" : "cualquiercosa";
                }
                pw.close();
                cl.close();
            }//Fin for
        } catch (Exception e) {
            e.printStackTrace();
        }//Fin bloque try-catch

    }
}
