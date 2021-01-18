/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servidor;

import productos.Producto;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
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
        ArrayList<Producto> catalogo = c.getCatalogoNoBase();

        try {
            //Se crea un socket server ligado al puerto 7000
            ServerSocket s = new ServerSocket(puerto);
            //Se crea un bucle infinito para recibir solicitudes de conexión de clientes
            for (;;) {
                //Se crea un socket para la solicitud del cliente aceptada
                Socket cl = s.accept();
                //Se muestra información del cliente
                System.out.println("Conexión establecida desde " + cl.getInetAddress() + ":" + cl.getPort());
                //Se define un flujo de datos para el archivo recibido

                String mensaje = "Bienvenido cliente\n";

                //Se liga un PrintWriter al flujo de salida del socket
                PrintWriter pw = new PrintWriter(new OutputStreamWriter(cl.getOutputStream()), true);
                //Se imprime el mensaje con un salto de línea
                pw.println(mensaje);
                //Se libera el flujo
                pw.flush();
                //Se cierra el flujo
               // pw.close();

                Carrito carrito = new Carrito();
                String respuesta = "";
                String opciones = "Opciones:\n1.Ver catalogo\n2.Ver carrito\n";
                while (!respuesta.equalsIgnoreCase("salir")) {
                    pw.println(opciones);
                    pw.flush();

                    BufferedReader comandoCliente = new BufferedReader(new InputStreamReader(cl.getInputStream()));
                    //Se lee el mensaje recibido
                    respuesta = comandoCliente.readLine();

                    switch (respuesta) {
                        case "1": //ver catalogo
                            mensaje = c.viewCatalogo();
                            mensaje += "Para agregar un producto al carrito escriba el nombre del producto\n";
                            pw.println(mensaje);
                            pw.flush();
                            comandoCliente = new BufferedReader(new InputStreamReader(cl.getInputStream()));
                            String compra = comandoCliente.readLine();

                            if (c.verificarCompra(compra)) {

                                mensaje = "Ingrese la cantidad";
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

                            pw.println(mensaje);
                            
                            pw.flush();
                            break;
                        case "2": //ver carrito
                            pw = new PrintWriter(new OutputStreamWriter(cl.getOutputStream()));
                            
                            break;
                    }
                }
                pw.close();
                cl.close();
            }//Fin for
        } catch (Exception e) {
            e.printStackTrace();
        }//Fin bloque try-catch

    }
}
