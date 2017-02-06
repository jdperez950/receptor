/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tracker.facade;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import tracker.factory.DAOFactoryConexion;
import tracker.factory.DAOFactoryProtocolo;
import tracker.util.Conexion;

/**
 *
 * @author Diseño
 */
public class Business implements IBusiness {
    private DAOFactoryConexion factoryC;
    private DAOFactoryProtocolo facProtocol;
    
    
    
    public Business(){
        factoryC=new DAOFactoryConexion();
        factoryC.getDAOFactory(DAOFactoryConexion.MYSQL);
    }
    public Business(String [] param){
        factoryC=new DAOFactoryConexion(param);
        factoryC.getDAOFactory(DAOFactoryConexion.MYSQL);
    }
    public Business(String [] param,int n){
        factoryC=new DAOFactoryConexion(param);
        factoryC.getDAOFactory(n);
    }
    public void asignarTipoBD(int tipoBD) throws Exception {
        factoryC.getDAOFactory(tipoBD);
    }
    
    public void conectar(){
        Conexion.conectar();
        if(!Conexion.hayConexion())
            System.out.println("No hay Conexion con Mysql Por SandBox\n"); 
        
    }
    
    public static String sentenciaPrueba(String sql){
        
        try {
            String ntcon="";
            ResultSet rs=Conexion.ejecutarSQL(sql);
            while (rs.next()) {
                ntcon= (String) rs.getString(2);       //nombre
            }
            return ntcon;
        } catch (SQLException ex) {
                System.out.println("traccar.facade.Business.sentenciaPrueba()");
            }
        return null;
    }
    
    public void initServicio(){
        this.facProtocol=new DAOFactoryProtocolo();
    }
    
    
}
