/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tracker.Controller;

import tracker.Controller.Service.ServiceLocator;
import tracker.facade.Business;

/**
 *
 * @author Diseño
 */
public class Controller {
    private ServiceLocator sLocator;

    
    public Controller() {
        sLocator = ServiceLocator.getInstance();
    }
    
    public Controller(String [] param){
        sLocator=ServiceLocator.getInstance(param);
    }
    public Controller(String [] param,int n){
        sLocator=ServiceLocator.getInstance(param,n);
    }
    
    private Business getLocator(){
        return  ServiceLocator.getInstance().getBusinessFacadeInstance();
    }
    public boolean openSession(){
        return iniciarPeer();
    }
    
    private boolean iniciarPeer(){
        return true;
    }
    
    public void sentenciaSistema(){
        this.getLocator().initServicio();
    }
    
    public void conexion(){
        this.getLocator().conectar();
    }
    
    
}
