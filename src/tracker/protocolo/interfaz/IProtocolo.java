/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tracker.protocolo.interfaz;


/**
 *
 * @author Eliam Zapata
 */
public interface IProtocolo {
    
    /**
     *Metodo Obterner nombre
     * @return el nombre del protocolo
     */
    public String getName();
    
    /**
     * Metodo Obtener Puerto
     * @return el puerto que esta usando el protocolo
     */
    public int getPuerto();
    
    /**
     * Metodo iniciar Servicios del protocolo
     */
    void initServicio();
    
}
