/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cliente;

import java.io.BufferedReader;
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
            String mensaje = br2.readLine();
            System.out.println(mensaje);
            while (!peticion.equalsIgnoreCase("salir")) {
                br2 = new BufferedReader(new InputStreamReader(cl.getInputStream()));
                //Se lee el mensaje recibido
                mensaje = br2.readLine();
                System.out.println(mensaje);

                peticion = br1.readLine();
                
                //Se imprime el mensaje con un salto de línea
                pw.println(peticion);
                //Se libera el flujo
                pw.flush();
                //Se cierra el flujo
                
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