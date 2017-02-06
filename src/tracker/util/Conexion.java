/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tracker.util;

import java.sql.ResultSet;

/**
 *
 * @author Diseño
 */
public class Conexion {
    
    private static String bd = "prueba";
    private static  String login = "prueba";
    private static  String password = "ufps";
    private String puerto="";
    private String urlDB="sandbox2.ufps.edu.co";
    private static  String url = "jdbc:mysql://sandbox2.ufps.edu.co/"+bd;
    private static  String controlador="com.mysql.jdbc.Driver";
    private static String motor="jdbc:mysql://";
    private static   JDBCMiddler jdbc;
    
    public Conexion() {
    }
    
    public Conexion(String[] param) {
        int n=param.length;
        if(n>0) this.bd=param[0];
        if(n>1) this.login=param[1];
        if(n>2) this.password=param[2];
        if(n>3) this.puerto=param[3];
        if(n>4){
            this.urlDB=param[4];  
        }
        this.construirURL();
        
    }

    public static String getControlador() {
        return controlador;
    }

    public static void setControlador(String controlador) {
        Conexion.controlador = controlador;
    }

    public static String getMotor() {
        return motor;
    }

    public static void setMotor(String motor) {
        Conexion.motor = motor;
    }
    
    public void construirURL(){
        url=motor+urlDB;
        if(puerto!=""){
            url+=":"+puerto;
        }
        url+="/"+bd;
    }
    
    /**
     * Comprueba si hay o no hay conexion
     * @return un boolean, true si hay conexion y false si no
     */
    public static boolean hayConexion() {
        
        return (jdbc!=null && jdbc.hayconexion());
        
    }

    /**
     * Método que realiza la conexion con administrador de la base de datos
     */
    public static  void conectar() {
        jdbc=new tracker.util.JDBCMiddler(controlador,url,login,password);
        
        try{
            
            jdbc.conectar();
            
        }catch(Exception e) {
            
            System.err.println(e.getMessage());
        }
    }
    
    /**
     * Método que ejecuta una actualizacion en una bae de datos
     * @param comandoSQL es de tipo Sting y contiene el comando SQL para ejecutar la actualizacion
     * @return un tipo boolean, true si ejecuta la actualiza y false sino.
     */
    public  static boolean ejecutarActualizacionSQL(String comandoSQL) {
        
        //System.err.println(comandoSQL);
        try {
            
            return (jdbc.ejecutarActualizacionSQL(comandoSQL));
            
        }catch(Exception e) {
            
            System.err.println("SQL Error:"+e.getMessage());
            return (false);
            
        }
        
    }
    
    /**
     * Método que desconecta la aplicacion de la base de datos
     */
    public  static void desconectar() {
        
        try{
            
            jdbc.desconectar();
            
        }catch(Exception e) {
            
            System.err.println("SQL Error:"+e.getMessage());
            
        }
        
    }
    
    /**
     * Método que retorna una consulta en formato de tabla HTML
     * @param sql contiene la consulta en sql
     * @return un Stirng con la informacion en formato de tabla HTML
     */
    public  static String getTablaHTML(String sql) {
        
        try{
            
            return (jdbc.getHTML(sql));
            
        }catch(Exception e) {
            
            System.err.println("SQL Error:"+e.getMessage());
            return ("No se pudo crear la tabla");
            
        }
        
    }
    
    /**
     * Método que ejecuta una instruccion SQL
     * @param consultaSQL es de tipo Stringy contiene la consulta en SQL
     * @return un tipo ResultSet que contiene la informacion sobre la consulta.
     */
    
    public  static ResultSet ejecutarSQL(String consultaSQL) {
        
        //System.err.println(consultaSQL);
        try{
            
            return (jdbc.ejecutarSQL(consultaSQL));
            
        }catch(Exception e) {
            
            System.err.println("SQL Error:"+e.getMessage());
            return (null);
            
        }
        
    }
    
    /**
     * Método que ejecuta una consulta en SQL
     * @param sql es de tipo String y contiene la consulta en sql
     * @return un tipo ArrayList y contiene la informacion que retorna la consulta
     */
    public static java.util.ArrayList<String>  getConsultaSQL(String sql) {
        
        try{
            
            return (jdbc.getSQL(sql));
            
        }catch(Exception e) {
            
            System.err.println("SQL Error:"+e.getMessage());
            return (null);
            
        }
        
    }

}
