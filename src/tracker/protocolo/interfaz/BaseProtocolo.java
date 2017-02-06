/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tracker.protocolo.interfaz;

/**
 *
 * @author Diseño
 */
public abstract class BaseProtocolo implements IProtocolo{
    
    private final String name;
    private final int puerto;
    
    public BaseProtocolo(String name,int puerto) {
        this.name = name;
        this.puerto=puerto;
    }

    @Override
    public int getPuerto() {
        return puerto;
    }
    
    @Override
    public String getName() {
        return name;
    }
}
