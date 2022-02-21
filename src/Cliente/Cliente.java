/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Cliente;

import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author heise
 */
public class Cliente {

    static final String HOST = "localhost";
    static final int PORT = 2000;

    public Cliente() {

        try {
            Socket sCliente = new Socket(HOST, PORT);

            // Flujos
            DataInputStream entrada = new DataInputStream(sCliente.getInputStream());
            DataOutputStream salida = new DataOutputStream(sCliente.getOutputStream());

            //Lee info del Servidor.
            System.out.println(entrada.readUTF());
            // Se le solicita usuario
            Scanner teclado = new Scanner(System.in);
            String user = teclado.next();
            salida.writeUTF(user);
            // Se le solicita contraseña
            System.out.println(entrada.readUTF());
            String pass = teclado.next();
            salida.writeUTF(pass);

            int estado = 1;

            do {
                   String opcion;
                switch (estado) {
                    case 1:
                        // Leemos del servidor opciones y se la comunicanos
                        String respuesta = entrada.readUTF();
                        System.out.println(respuesta);
                        opcion = teclado.next();
                        salida.writeUTF(opcion);

                        if (opcion.equals("ls")) {
                            for (int i = 0; i < respuesta.length(); i++) {
                                String datos = entrada.readUTF();
                                System.out.println(datos);
                            }
                            estado = 1;
                            break;
                        } 
                        if (opcion.equals("get")) {
                            String recibo = entrada.readUTF();
                            System.out.println(recibo);
                            String nombreFichero = teclado.next();
                            salida.writeUTF(nombreFichero);

                            // Sacar fichero en cliente:
                            String lineas="";
                            System.out.println("FICHERO:" + nombreFichero);
                            System.out.println("-------------------------");
                            do {
                                lineas = entrada.readUTF();
                                System.out.println(lineas);
                            } while (lineas!=null);
                                estado = 1;
                                break;
                        } 
                        if (opcion.equals("exit")){
                            estado = -1;
                            break;
                        }
                }
            } while (estado != -1);
                sCliente.close();
                System.out.println("Cliente desconectado.");
        } catch (IOException ex) {
            Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
    
    public static void main(String[] args) {
        new Cliente();
    }
}
