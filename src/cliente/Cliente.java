package cliente;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

public class Cliente {

    public static void main(String[] args) {
        try {
            BufferedReader br1 = new BufferedReader(new InputStreamReader(System.in));
            System.out.println("Escriba la dirección del servidor: ");
            String host = br1.readLine();
            System.out.println("Escriba el puerto: ");
            int pto = Integer.parseInt(br1.readLine());
            Socket cl = new Socket(host, pto);
            String peticion = "";
            BufferedReader br2 = new BufferedReader(new InputStreamReader(cl.getInputStream()));
            PrintWriter pw = new PrintWriter(new OutputStreamWriter(cl.getOutputStream()));
            String mensaje;
            while (!peticion.equalsIgnoreCase("salir") && !peticion.equalsIgnoreCase("n")) {
                br2 = new BufferedReader(new InputStreamReader(cl.getInputStream()));
                boolean bandera = false;
                while (!(mensaje = br2.readLine()).equals("\t")) {
                    if (mensaje.contains("Enviando recibo")) {
                        bandera = true;
                    }
                    System.out.println(mensaje);
                }

                if (!bandera) {

                    peticion = br1.readLine();

                    pw.println(peticion);
                    pw.flush();
                } else {
                    DataInputStream dis = new DataInputStream(cl.getInputStream());
                    byte b[] = new byte[1024];
                    String nombre = dis.readUTF();
                    System.out.println("Recibiendo el archivo: " + nombre);
                    long tam = dis.readLong();
                    DataOutputStream dos = new DataOutputStream(new FileOutputStream(nombre));
                    long recibidos = 0;
                    int n;
                    while (recibidos < tam) {
                        n = dis.read(b);
                        dos.write(b, 0, n);
                        dos.flush();
                        recibidos += n;
                    }
                    System.out.println("Archivo recibido.");

                    peticion = "ok";

                    pw.println(peticion);
                    pw.flush();

                }

            }

            pw.close();
            br1.close();
            br2.close();
            cl.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
