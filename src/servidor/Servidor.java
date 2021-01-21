package servidor;

import com.itextpdf.text.DocumentException;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import productos.Carrito;

public class Servidor {

    public static void main(String[] args) {
        //Puerto sobre el que va a trabajar el servidor
        int puerto = 7000;
        //Inicializar catalogo
        Catalogo c = new Catalogo();

        try {
            ServerSocket s = new ServerSocket(puerto);
            for (;;) {
                PrintWriter pw;
                DataOutputStream dos = null;
                DataInputStream dis = null;
                try (Socket cl = s.accept()) {
                    System.out.println("Conexión establecida desde " + cl.getInetAddress() + ":" + cl.getPort());

                    String mensaje;

                    pw = new PrintWriter(new OutputStreamWriter(cl.getOutputStream()), true);                  

                    Carrito carrito = new Carrito();
                    String respuesta = "";
                    String opciones = "Opciones:"
                            + "\n1.Ver catalogo"
                            + "\n2.Ver carrito\n\t";
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
                                String compra = comandoCliente.readLine();

                                if (c.verificarCompra(compra)) {

                                    mensaje = "Ingrese la cantidad\n\t";
                                    pw.println(mensaje);
                                    pw.flush();

                                    try {
                                        int cantidad = Integer.parseInt(comandoCliente.readLine());

                                        if (c.verificarStock(compra, cantidad)) {
                                            carrito.addProduct(c.getCompra(compra, cantidad));
                                            mensaje = "Producto agregado exitosamente\n";
                                        } else {
                                            mensaje = "Stock insuficiente, el producto no ha sido agregado a su carrito\n";
                                        }
                                    } catch (IOException | NumberFormatException e) {
                                        mensaje = "Cantidad no valida, el producto no ha sido agregado a su carrito\n";
                                    }
                                } else {
                                    mensaje = "Producto no valido\n";
                                }
                                break;

                            case "2": //ver carrito
                                mensaje = carrito.verCarrito();
                                mensaje += "Opciones disponibles:"
                                        + "\n1.Realizar compra"
                                        + "\n2.Cancelar compra"
                                        + "\n3.Quitar un producto"
                                        + "\n4.Modificar cantidad de un producto"
                                        + "\n5.Regresar\n\t";
                                pw.println(mensaje);
                                respuesta = comandoCliente.readLine();
                                switch (respuesta) {
                                    case "1":
                                        c.vender(carrito.getCarrito());
                                        mensaje = "Compra realizada correctamente\n";

                                        /*ENVIAR RECIBO*/
                                        PDF pdf = new PDF();
                                        String file = null;
                                        try {
                                            file = pdf.generarPDF(carrito);
                                        } catch (FileNotFoundException ex) {
                                            Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
                                        } catch (DocumentException ex) {
                                            Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
                                        }

                                        mensaje += "Enviando recibo \n\t";
                                        pw.println(mensaje);
                                        pw.flush();
                                        //*********************
                                        File f = new File(file);
                                        String archivo = f.getAbsolutePath();
                                        String nombre = f.getName();
                                        long tam = f.length();
                                        dos = new DataOutputStream(cl.getOutputStream());
                                        dis = new DataInputStream(new FileInputStream(archivo));
                                        dos.writeUTF(nombre);
                                        dos.flush();
                                        dos.writeLong(tam);
                                        dos.flush();
                                        byte b[] = new byte[1024];
                                        long enviados = 0;
                                        int porcentaje,
                                         n;
                                        while (enviados < tam) {
                                            n = dis.read(b);
                                            dos.write(b, 0, n);
                                            dos.flush();
                                            enviados = enviados + n;
                                            porcentaje = (int) (enviados * 100 / tam);
                                            System.out.println("Enviando: " + porcentaje + "%");
                                        }// Fin while

                                        mensaje = "";
                                        carrito = new Carrito();
                                        break;

                                    case "2":
                                        carrito = new Carrito();
                                        mensaje = "Carrito vaciado\n";
                                        break;
                                    case "3":
                                        mensaje = "Ingrese el indice del producto que desea quitar\n\t";
                                        pw.println(mensaje);
                                        pw.flush();
                                        try {
                                            int prod = Integer.parseInt(comandoCliente.readLine());
                                            if (carrito.removeProduct(prod)) {
                                                mensaje = "Producto eliminado correctamente\n"
                                                        + "Su carrito ahora contiene:\n";
                                                mensaje += carrito.verCarrito();
                                            } else {
                                                mensaje = "No existe un producto con indice " + prod + " en su carrito\n";
                                            }
                                        } catch (IOException | NumberFormatException e) {
                                            mensaje = "El índice debe ser un valor numérico\n";
                                        }
                                        break;

                                    case "4":
                                        mensaje = "Ingrese el índice del producto que desea modificar\n\t";
                                        pw.println(mensaje);
                                        pw.flush();

                                        try {
                                            int prod = Integer.parseInt(comandoCliente.readLine());

                                            if (carrito.isValid(prod)) {
                                                mensaje = "Ingrese la cantidad que desea comprar\n\t";
                                                pw.println(mensaje);
                                                pw.flush();

                                                try {
                                                    int cantidad = Integer.parseInt(comandoCliente.readLine());

                                                    if (c.verificarStock(carrito.getNombre(prod), cantidad)) {
                                                        carrito.modificarCantidad(prod, cantidad);
                                                        mensaje = "Producto modificado exitosamente\n"
                                                                + "Su carrito ahora contiene:\n";
                                                        mensaje += carrito.verCarrito();
                                                    } else {
                                                        mensaje = "No hay suficientes unidades en stock! Cambios no realizados\n";
                                                    }

                                                } catch (IOException | NumberFormatException e) {
                                                    mensaje = "La cantidad debe ser un valor numérico\n";
                                                }

                                            } else {
                                                mensaje = "No existe un producto con indice " + prod + " en su carrito\n";
                                            }
                                        } catch (IOException | NumberFormatException e) {
                                            mensaje = "El índice debe ser un valor numérico\n";
                                        }
                                        break;
                                    default:
                                        mensaje = "\n";
                                        break;
                                }
                                break;

                            default:
                                mensaje = "Opción no válida!\n";
                                break;
                        }
                        mensaje += "Desea continuar?"
                                + "\nY.Si"
                                + "\nN.No\n\t";
                        pw.println(mensaje);
                        pw.flush();
                        respuesta = comandoCliente.readLine().equalsIgnoreCase("n") ? "salir" : "cualquiercosa";
                    }
                    try {
                        dos.close();
                        dis.close();
                    } catch(Exception e){
                        
                    }
                    pw.close();
                    cl.close();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
