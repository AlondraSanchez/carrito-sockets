/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cliente;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

/**
 *
 * @author user1
 */
public class Cliente {

    public static void main(String[] args) {
        try {
            //Se define un flujo de lectura de la entrada estándar
            BufferedReader br1 = new BufferedReader(new InputStreamReader(System.in));
            //Se solicita la dirección del serviror
            System.out.println("Escriba la dirección del servidor: ");
            String host = br1.readLine();
            //Se solicita el puerto (1234 en nuestro caso)
            System.out.println("Escriba el puerto: ");
            int pto = Integer.parseInt(br1.readLine());
            //Se crea un socket
            Socket cl = new Socket(host, pto);
            String peticion = "";
            BufferedReader br2 = new BufferedReader(new InputStreamReader(cl.getInputStream()));
            PrintWriter pw = new PrintWriter(new OutputStreamWriter(cl.getOutputStream()));
            String mensaje;
            while (!peticion.equalsIgnoreCase("salir") && !peticion.equalsIgnoreCase("n")) {
                br2 = new BufferedReader(new InputStreamReader(cl.getInputStream()));
                //Se lee el mensaje recibido
                boolean bandera = false;
                while (!(mensaje = br2.readLine()).equals("\t")) {
                    if (mensaje.contains("Enviando recibo")) {
                        bandera = true;
                    }
                    System.out.println(mensaje);
                }

                //System.out.println(mensaje);
                if (!bandera) {

                    peticion = br1.readLine();

                    //Se imprime el mensaje con un salto de línea
                    pw.println(peticion);
                    //Se libera el flujo
                    pw.flush();
                    //Se cierra el flujo
                } else {
                    DataInputStream dis = new DataInputStream(cl.getInputStream());
                    //Arreglo de bytes donde se especifica la cantidad de bytes que se irán recibiendo
                    byte b[] = new byte[1024];
                    //Se obtiene el nombre del archivo recibido
                    String nombre = dis.readUTF();
                    System.out.println("Recibiendo el archivo: " + nombre);
                    //Se lee la longitud del archivo recibido
                    long tam = dis.readLong();
                    //Se crea un flujo de datos para escribir el archivo recibido
                    DataOutputStream dos = new DataOutputStream(new FileOutputStream(nombre));
                    //Contador para los bytes recibidos
                    long recibidos = 0;
                    //Variable para obtener el flujo de bytes del cliente
                    int n;
                    //Bucle para recibir los datos que envía el cliente
                    while (recibidos < tam) {
                        //Se lee un flujo de bytes
                        n = dis.read(b);
                        //Se escribe la información en el archivo creado
                        dos.write(b, 0, n);
                        //Se libera el flujo de información
                        dos.flush();
                        //Aumenta el contador de bytes recibidos
                        recibidos += n;
                    }//Fin while
                    System.out.println("Archivo recibido.");
//                    dos.close();
//                    dis.close();

                    peticion = "ok";

                    //Se imprime el mensaje con un salto de línea
                    pw.println(peticion);
                    //Se libera el flujo
                    pw.flush();
                    //Se cierra el flujo

                }

            }

            //Se cierran los flujos de entrada
            pw.close();
            br1.close();
            br2.close();
            //Se cierra el socket
            cl.close();
        } catch (Exception e) {
            //Se imprime la excepcion obtenida
            e.printStackTrace();
        }//Fin try-catch
    }
}
