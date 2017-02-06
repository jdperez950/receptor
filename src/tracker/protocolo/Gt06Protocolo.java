/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tracker.protocolo;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import tracker.protocolo.interfaz.BaseProtocolo;

/**
 *
 * @author Elian Zapata
 */
public class Gt06Protocolo extends BaseProtocolo{
    
    /**
     * Metodo constructor GT06 
     * @param name Nombre del Protocolo
     * @param puerto puerto del Protocolo
     */
    public Gt06Protocolo(String name,int puerto) {
        super(name,puerto);
    }

    @Override
    public void initServicio() {
        this.initSockets();
    }
    
    
    /***
     * Metodo iniciar Sockets
     */
    private void initSockets(){
        
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    aceptarConexiones();
                } catch (IOException exception) {
                    System.out.println(".run()");
                }
            }
        }).start(); 
        
    }
    
    /***
     * Metodo Aceptar Conexiones
     * @throws IOException 
     */
    private void aceptarConexiones()  throws IOException{
        this.getPuerto();
        this.getName();
        
        ServerSocket socketentrada = new ServerSocket(this.getPuerto());     
        // socketentrada.setReceiveBufferSize(300);

       int n_conexiones = 0;
       while (true){
          System.out.println("esperando conexion"); 
          Socket conexion = socketentrada.accept();
          System.out.println("conexion aceptada"); 
          n_conexiones++;
          System.out.println("conexion_numero: " +n_conexiones);

          new Thread(
              new Gt06ProtocoloTread(conexion, this.getName(),n_conexiones)
          ).start();
       }
    }
    
}
