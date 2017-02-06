/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tracker.Controller.Service;

import tracker.facade.Business;

/**
 *
 * @author Diseño
 */
public class ServiceLocator {
    private static ServiceLocator instance;
    private Business businessFacade;
    
    private ServiceLocator() {
        // Se obtiene por instanciacion directa.
        // Pero en otros casos podria incluir invocacion remota
        // o llamado a un servicio web.
        businessFacade = new Business();
    }
    private ServiceLocator(String [] param) {
        // Se obtiene por instanciacion directa.
        // Pero en otros casos podria incluir invocacion remota
        // o llamado a un servicio web.
        businessFacade = new Business(param);
    }
    
    private ServiceLocator(String [] param, int n){
        businessFacade = new Business(param,n);
    }

    public static ServiceLocator getInstance() {
        if (instance == null) {
            instance = new ServiceLocator();
        }
        return instance;
    }
    public static ServiceLocator getInstance(String[] param) {
        if (instance == null) {
            instance = new ServiceLocator(param);
        }
        return instance;
    }
    
    public static ServiceLocator getInstance(String[] param,int n) {
        if (instance == null) {
            instance = new ServiceLocator(param,n);
        }
        return instance;
    }
    public Business getBusinessFacadeInstance() {
        return businessFacade;
    }
}
