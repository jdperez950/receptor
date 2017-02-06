/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tracker.factory;

import tracker.protocolo.Gt06Protocolo;
import tracker.protocolo.interfaz.IProtocolo;

/**
 *
 * @author Eliam Zapata
 */
public class DAOFactoryProtocolo {
    private static final int GT06=5023;
    private IProtocolo proto;

    public DAOFactoryProtocolo() {
        this.initProtocolo(GT06);
    }
    
    /**
     * Metodo iniciar Protocolo individual
     * @param varp Valor del protocolo
     */
    private void initProtocolo(int varp){
        
        IProtocolo prot =new Gt06Protocolo("Multithreaded Server_"+varp,varp);
        prot.initServicio();
        proto=prot;
    }
    
    
}
