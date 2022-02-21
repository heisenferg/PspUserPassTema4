/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package Servidor;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author heise
 */
public class Servidor extends Thread {

    static final int PORT = 2000;
    Socket skCliente;
    static final String USUARIO = "fernando";
    static final String CONTRA = "secreta";

    public Servidor(Socket sCliente) {
        skCliente = sCliente;
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            // Iniciamos servidor
            ServerSocket sServer = new ServerSocket(PORT);
            System.out.println("Escuchando puerto " + PORT);

            while (true) {
                //Conexión de cliente
                Socket sCliente = sServer.accept();
                System.out.println("Cliente nuevo conectado.");

                new Servidor(sCliente).start();
            }
        } catch (IOException ex) {
            Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void run() {
        try {
            // Flujos
            DataInputStream entrada = new DataInputStream(skCliente.getInputStream());
            DataOutputStream salida = new DataOutputStream(skCliente.getOutputStream());

            String user, pass;
            //Pedimos, recibimos y mostramos user
            salida.writeUTF("Escribe tu usuario: \n");
            user = entrada.readUTF();
            System.out.println("Usuario del cliente: " + user);

            // Pedimos y recibimos contrasñea
            salida.writeUTF("Introduce tu contraseña: \n");
            pass = entrada.readUTF();

            // Validamos usuario y contraseña
            if (user.equals(USUARIO) && pass.equals(CONTRA)) {
                int estado = 1;
                
                do {
                    String comando= "";
                    switch (estado){
                        case 1:
                            String opciones = "Introduce un comando (ls/get/exit)\n"
                                    + "ls: Ver directorio. \n"
                                    + "get: Ver un fichero.\n"
                                    + "exit: Salir.";
                            salida.writeUTF(opciones);
                            // Recibimos el comando y realizamos la acción
                            comando = entrada.readUTF();
                            if (comando.equals("ls")){
                                System.out.println("El cliente quiere ver el contenido del directorio.");
                                // Listamos ficheros del directorio
                                String directorio = "./src";
                                File ficherosDirect = new File(directorio);
                                File[] ficheros = ficherosDirect.listFiles();
                                for (int i = 0; i < ficheros.length; i++) {
                                    salida.writeUTF(ficheros[i].getName());
                                }
                                comando="";
                                estado = 1;
                              //  break;
                            } else if (comando.equals("get")){
                                System.out.println("El cliente quiere ver un fichero.");
                                salida.writeUTF("Introduce el nombre delfichero.");
                                String archivoPedido = entrada.readUTF();
                                System.out.println("El cliente pide ver " + archivoPedido);
                                
                                File archivo = new File(archivoPedido);
                                FileReader fileReader = new FileReader (archivo);
                                BufferedReader br = new BufferedReader(fileReader);
                                String linea;
                                while ((linea=br.readLine()) != null){
                                    salida.writeUTF(linea);
                                }
                                
                                System.out.println("Fichero " + archivoPedido + " enviado.");
                                estado = 1;
                             //   break;
                            } else if (comando.equals("exit")){
                                estado = -1;
                                break;
                            }
                            
                            
                    }
                } while (estado !=-1);
                skCliente.close();
                System.out.println("Cliente desconectado");
            } else {
                System.out.println("Usuario o contraseña incorrectos.");
                salida.writeUTF("Usuario o contraseña incorrectos.");
            }

            // Cerramos conexión
            skCliente.close();
            System.out.println("Cliente desconecado correctamente.");

        } catch (IOException ex) {
            Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
