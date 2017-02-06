/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tracker.factory;

import java.sql.ResultSet;
import tracker.util.Conexion;

/**
 *
 * @author Diseño
 */
public  class DAOFactoryConexion {
    
    public static final int ORACLE = 1;
    public static final int DERBY = 2;
    public static final int MYSQL= 3;
    public static final int POSTGRES= 4;
    private Conexion cn;

    public DAOFactoryConexion() {
        cn=new Conexion();
    }
    public  DAOFactoryConexion(String [] param) {
        cn=new Conexion(param);
    }
    
    
    
    public void  getDAOFactory(int whichFactory) {
        
        switch (whichFactory) {
            case MYSQL:{
                cn.setMotor("jdbc:mysql://");
                cn.setControlador("com.mysql.jdbc.Driver");
                cn.construirURL();
            }break;
            case POSTGRES:{
                cn.setMotor("jdbc:postgresql://");
                cn.setControlador("org.postgresql.Driver");
                cn.construirURL();
            }break;
                
        }
    }
    
    public Conexion getConexion(){
        return cn;
    }
    
    
    
    
    
    
}
