/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tracker.protocolo.funciones;

import java.math.BigInteger;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import tracker.util.Conexion;
import tracker.util.Fecha;
import tracker.util.UtilProtocolo;

/**
 *
 * @author Diseño
 */
public class Gt06ProtocoloFunciones {
    private static Statement st=null;
    private static ResultSet rs=null;
    private static  String imei="0123456";
    private static  int id_imei=666;
    
    private Fecha date;
    
    private UtilProtocolo uPro;
    

    public Gt06ProtocoloFunciones() {
        date=new Fecha();
        uPro=new UtilProtocolo("0123456789ABCDEF".toCharArray());
    }
    
    public int data_save(byte dato[], int imeix) throws SQLException{
        id_imei=imeix;
       
       
       
        int long_datas  = dato[2]+5;
        int n_datos_a_escupir=dato.length/long_datas;
        System.out.println("dato a escupir: "+n_datos_a_escupir);
        System.out.println("long datas: "+long_datas);
        System.out.println("dato length: "+dato.length);
        System.out.println("imei: "+id_imei+" *******************************************/////////////////////*************"+imei);
        if(dato[0]==0x78&&dato[1]==0x78){ System.out.println("GT06");}
        if(dato[3]==0x01){System.out.println("MENSAJE DE LOGIN");  return  login_mensaje_save(dato);    }
        if(dato[3]==0x12){
            System.out.println("MENSAJE DE LOCALIZACION");
        
            if(n_datos_a_escupir>1){

                byte [] dato_save=new byte[long_datas];
                int z=n_datos_a_escupir;
                for (int ii=0;ii<z;ii++){
                    int yx= 36*ii;
                    for (int i=0;i<long_datas;i++){dato_save[i]=dato[yx+i];       }
                    System.out.println(this.uPro.bytesToHex(dato_save));
                    location_data_save(dato_save);                               
                }
            }else{location_data_save(dato);}
        }

        if(dato[3]==0x13){System.out.println("MENSAJE DE INFORMACION DE ESTADO"); status_information_save(dato);}
        if(dato[3]==0x15){System.out.println("MENSAJE DE CADENA DE INFORMACION"); string_informatin_save(dato);}
        if(dato[3]==0x16){System.out.println("MENSAJE DE DATOS DE ALARMA");       alarm_data_save(dato);}
        if(dato[3]==0x1A){System.out.println("MENSAJE DE GPS, PETICION DE DIRECCION POR NUMERO DE TELEFONO");      }
        if(dato[3]==0x80){System.out.println("MENSAJE COMANDO DE INFORMACION ENVIADA POR EL SERVIDOR AL TERMINAL");}
    
        return 0;
    }


    private int login_mensaje_save(byte[] dato) throws SQLException {
        int ii=0;
        byte    imei1 [] = new byte[8];
        for(int i=4;i<=11;i++){
            imei1[ii]=dato[i];
            ii++;
        }
       
        System.out.println("IMEI por byte "+this.uPro.bytesToHex(imei1));
       
        imei = this.uPro.bytesToHex(imei1);
        rs=Conexion.ejecutarSQL("Select id from dispositivos where imei ='"+imei+"'");
        System.out.println("dfgsdgdfgdfshfgghjhfghjhdjh= "+rs);
        if(rs.next()){// UN ERROR
                id_imei= (int) rs.getInt(1);       //nombre           
            
        }else{
            rs=Conexion.ejecutarSQL("SELECT (MAX(id)) FROM dispositivos");
            rs.next();
            int n=rs.getInt(1)+1;
            String comando="INSERT INTO dispositivos (id,nombre, imei, celular, id_empresa, ultima_posicion_id, borrado, created_at, last_update, id_usuario) VALUES ("+n+",'no se encuentra reguistrado', '"+imei+"', '000000000', '1', '0', '0', '2017-01-19 00:00:00', CURRENT_TIMESTAMP, '1')";
            
            Conexion.ejecutarActualizacionSQL(comando);

            rs=Conexion.ejecutarSQL("Select id from dispositivos where imei = '"+imei+"'");
            
            while (rs.next()) {
                id_imei= (int) rs.getInt(1);       //nombre           
            }
        }    
        return id_imei;

    }

    private void location_data_save(byte[] dato) throws SQLException {
       
        int ii=0;
        String fecha = this.fecha(dato);
        System.out.println("fecha "+fecha);// corregir error de minuto y hora <10
      
        int ctdsatgps= dato[10];
        System.out.println("dato 10="+ctdsatgps);
        String xxx=    Integer.toBinaryString(ctdsatgps);
        System.out.println("Pasado a bynario="+xxx);
        int lgt= xxx.length();
        ctdsatgps=Integer.parseInt(xxx.substring(lgt-4, lgt),2);
        int longInfor=Integer.parseInt(xxx.substring(lgt-8,lgt-4),2);
        
        System.out.println("Numero de satelites="+ctdsatgps+" log="+longInfor);
        
        ii=0;
        byte [] latitud = new byte [4] ;
        for(int i=11;i<=14;i++){
            latitud[ii]=dato[i];
            ii++;
        }
        
        double latitud_decimal= this.parseCordenada(latitud);
        
        
        ii=0;
        byte []  longitud  = new byte[4];
        for(int i=15;i<=18;i++){
            longitud[ii]=dato[i];
            ii++;
        }
        
        double longitud_decimal= this.parseCordenada(longitud);

     
        int velocidad=dato[19];

       
        int course_status1=0, course_status2=0;
        course_status1=dato[20];
        course_status2=dato[21];
       
        
        String course_status_str1=  Integer.toBinaryString(dato[20]);
        String course_status_str2=  Integer.toBinaryString(course_status2);
        
       
        int lgt_css1= 8;
        int lgt_css2= 8;
        
        course_status_str1=this.uPro.convertirBits(course_status_str1);
        course_status_str2=this.uPro.convertirBits(course_status_str2);
        
        
       
        course_status_str1 =course_status_str1.substring(lgt_css1-8, lgt_css1)+  course_status_str2.substring(lgt_css2-8, lgt_css2);
        
        lgt_css1= course_status_str1.length();
        System.out.println("css: "+course_status_str1);
        int course=Integer.parseInt(course_status_str1.substring(lgt_css1-10, lgt_css1),2);
        int lat_course=Integer.parseInt(course_status_str1.substring(lgt_css1-11, lgt_css1-10),2);
        int log_course=Integer.parseInt(course_status_str1.substring(lgt_css1-12, lgt_css1-11),2);
        int gps_has_positioned=Integer.parseInt(course_status_str1.substring(lgt_css1-13, lgt_css1-12),2);
        int real_timegps=Integer.parseInt(course_status_str1.substring(lgt_css1-14, lgt_css1-13),2);
        int acc0=Integer.parseInt(course_status_str1.substring(lgt_css1-15, lgt_css1-14),2);
        int acc1=Integer.parseInt(course_status_str1.substring(lgt_css1-16, lgt_css1-15),2);
        System.out.println("lat_course: "+lat_course+" -log_course: "+log_course+" gps_has_positioned: "+gps_has_positioned+
               "real_timegps: "+real_timegps +" acc0: "+acc0  + " acc1"+acc1);
       
        if (lat_course==0){ latitud_decimal*=-1; }
        if (log_course==1){ longitud_decimal*=-1;}
        
        
        
        byte [] mcc= new byte[2];
               mcc[0]= dato[22];
               mcc[1]=dato[23];
               
        Integer mccx = Integer.parseInt(this.uPro.bytesToHex(mcc),16);  
 
               
        int  mnc=dato[24];
      
        byte []  lac= new byte[2];
                lac[0]= dato[25];
                lac[1]= dato[26];
                
        Integer lacx = Integer.parseInt(this.uPro.bytesToHex(lac),16);
                
                
        ii=0;
        byte [] cellid = new byte[3];
        for(int i=27;i<=29;i++){
            cellid[ii]=dato[i];
            ii++;
        }
        
        Integer cellidx = Integer.parseInt( this.uPro.bytesToHex(cellid) ,16);
       
       
       
        rs = Conexion.ejecutarSQL("Select * from sistema");
        String  nombre_trama_conexion="tramaemergencial";
	           
        while (rs.next()) {
            nombre_trama_conexion= (String) rs.getString(2);       //nombre           
        }
       
        String comando; 
        
        String fechax=date.getFecha();
        
       
        comando = "INSERT INTO "+nombre_trama_conexion+" (id_dispositivo, date, cantidadgps, latitud, longitud, velocidad, gps_has_positioned,real_timegps,acc, direccion, mcc, mnc, lac, cellid,f_insert )"
                        + " VALUES (   '" + id_imei + "', '" + fecha + "', '" + ctdsatgps + "','" + latitud_decimal +"','" + longitud_decimal + "','" + velocidad + "','"+ gps_has_positioned + "','"+real_timegps+  "','"+acc1+ "','" + course + "','" + mccx + "', '" + mnc + "','" + lacx + "','" + cellidx +  "','"+fechax + "')";    
          
        
        Conexion.ejecutarActualizacionSQL(comando);
       
        comando="UPDATE DISPOSITIVOS SET ULTIMA_POSICION_ID  = (SELECT ID FROM "+nombre_trama_conexion+"  WHERE id_dispositivo='" + id_imei 
                + "'  order by id DESC  LIMIT 1)  WHERE DISPOSITIVOS.id='" + id_imei + "'";
        
        Conexion.ejecutarActualizacionSQL(comando);
       
    }
    
    public void estado (int imei) throws SQLException{
      
     
        rs = Conexion.ejecutarSQL("Select * from sistema");
        String  nombre_trama_conexion="tramaemergencia";
	           
        while (rs.next()) {
            nombre_trama_conexion= (String) rs.getString(3);       //nombre           
        }
       
        String comando;
       
        
        String fechax=date.getFecha();
        
        
        
        System.out.println("nombre trama: "+nombre_trama_conexion+ " id: "+imei+" fechax: "+fechax);
        comando = "INSERT INTO "+nombre_trama_conexion+" (id_dispositivo, estado, f_conexion )"
                        + " VALUES (   '" + imei  + "', 0,'"  +fechax+  "')";   
                   //   + " VALUES (   '" + id_imei + "', '" + rele1 + "', '" + gpstracking1 + "','" + evento1 +"','" + cargando1 + "','" + acc1 + "','" + defense1 + "','" + bytesToHex(voltajelvl) + "', '" + bytesToHex(gsmsignal) + "','" + bytesToHex(alarma) + "','" + bytesToHex(language)+ "', 1,'"  +fechax+  "')";   
            
        Conexion.ejecutarActualizacionSQL(comando);
 
        
    } 

    private void status_information_save(byte[] dato) throws SQLException {

        byte[]   terminalinfo  = new byte[1];
        terminalinfo[0]=dato[4];

        byte    voltajelvl [] = new byte[1];
        voltajelvl[0]=dato[5];
       
        byte    gsmsignal [] = new byte[1];
        gsmsignal[0]=dato[6];
       
        byte    alarma [] = new byte[1];
        alarma[0]=dato[7];
       
        byte    language [] = new byte[1];
        language[0]=dato[8];
        
        String b=  new BigInteger(this.uPro.bytesToHex(terminalinfo), 16).toString(2);
        System.out.println("Binario info: "+b);
        
        b=this.uPro.convertirBits(b);
        
      
        int rele1= Integer.parseInt(b.substring(0, 1));
        int gpstracking1= Integer.parseInt(b.substring(1, 2));
        int evento1= Integer.parseInt(b.substring(2, 5));
        int cargando1= Integer.parseInt(b.substring(5, 6));
        int acc1= Integer.parseInt(b.substring(6, 7));
        int defense1= Integer.parseInt(b.substring(7, 8));
      
        rs = Conexion.ejecutarSQL("Select * from sistema");
      
        String  nombre_trama_conexion="tramaemergencia";
	           
        while (rs.next()) {
            nombre_trama_conexion= (String) rs.getString(3);       //nombre           
        }
       
        String comando;
        
        
        String fechax=date.getFecha();
       
        System.out.println("fecha="+fechax);
       
        comando = "INSERT INTO "+nombre_trama_conexion+" (id_dispositivo, rele, gpstracking, evento, cargando, acc, defense, voltage, gsm_signal_strength, alarma, language, estado, f_conexion )"
                        + " VALUES (   '" + id_imei + "', '" + rele1 + "', '" + gpstracking1 + "','" + evento1 +"','" + cargando1 + "','" + acc1 + "','"
                        + defense1 + "','" + this.uPro.bytesToHex(voltajelvl) + "', '" + this.uPro.bytesToHex(gsmsignal) + "','" + this.uPro.bytesToHex(alarma) 
                        + "','" + this.uPro.bytesToHex(language)+ "','1','"  +fechax+  "')";   
        
                    
        Conexion.ejecutarActualizacionSQL(comando);    
       
    }
    

    private void string_informatin_save(byte[] dato) {

    }

    private void alarm_data_save(byte[] dato) throws SQLException {
    
        int ii=0;
        
        String fecha = this.fecha(dato);
        System.out.println("fecha "+fecha);// corregir error de minuto y hora <10
        
        
        
        
        int ctdsatgps= dato[10];
        String xxx=    Integer.toBinaryString(ctdsatgps);
        int lgt= xxx.length();
        ctdsatgps=    Integer.parseInt(xxx.substring(lgt-4, lgt),2);
  
        ii=0;
        byte [] latitud = new byte [4] ;
        for(int i=11;i<=14;i++){
            latitud[ii]=dato[i];
            ii++;
        }
        
        double latitud_decimal= this.parseCordenada(latitud);
       
       
        ii=0;
        byte []  longitud  = new byte[4];
        for(int i=15;i<=18;i++){
            longitud[ii]=dato[i];
            ii++;
        }
        
        double longitud_decimal= this.parseCordenada(longitud);


        int velocidad=dato[19];

       
        int course_status1=0, course_status2=0;
        course_status1=dato[20];
        course_status2=dato[21];
        String course_status_str1=  Integer.toBinaryString(course_status1);
        String course_status_str2=  Integer.toBinaryString(course_status2);
       
        int lgt_css1= 8;
        int lgt_css2= 8;
     
        course_status_str1=this.uPro.convertirBits(course_status_str1);
        course_status_str2=this.uPro.convertirBits(course_status_str2);
        
        course_status_str1 =course_status_str1.substring(0, 8)+  course_status_str2.substring(0, 8);
       
        lgt_css1= course_status_str1.length();
        System.out.println("css: "+course_status_str1);
        int course=Integer.parseInt(course_status_str1.substring(lgt_css1-10, lgt_css1),2);
        int lat_course=Integer.parseInt(course_status_str1.substring(lgt_css1-11, lgt_css1-10),2);
        int log_course=Integer.parseInt(course_status_str1.substring(lgt_css1-12, lgt_css1-11),2);
        int gps_has_positioned=Integer.parseInt(course_status_str1.substring(lgt_css1-13, lgt_css1-12),2);
        int real_timegps=Integer.parseInt(course_status_str1.substring(lgt_css1-14, lgt_css1-13),2);
        int acc0=Integer.parseInt(course_status_str1.substring(lgt_css1-15, lgt_css1-14),2);
        int acc1=Integer.parseInt(course_status_str1.substring(lgt_css1-16, lgt_css1-15),2);
      
       
        if (lat_course==0){ latitud_decimal*=-1; }
        if (log_course==1){ longitud_decimal*=-1;}
       

        int lbslength=dato[22];
       
       
        byte [] mcc= new byte[2];
               mcc[0]= dato[23];
               mcc[1]=dato[24];
        
        Integer mccx = Integer.parseInt(this.uPro.bytesToHex(mcc),16);  
       
        int  mnc=dato[25];

        byte []  lac= new byte[2];
                lac[0]= dato[26];
                lac[1]= dato[27];
        
        Integer lacx = Integer.parseInt(this.uPro.bytesToHex(lac),16);

       
       
        ii=0;
        byte [] cellid = new byte[3];
        for(int i=28;i<=30;i++){
            cellid[ii]=dato[i];
            ii++;
        }
        
        Integer cellidx = Integer.parseInt(this.uPro.bytesToHex(cellid),16);
       
       
       
        byte[]   tinfo  = new byte[1];
        tinfo[0]=dato[31];
       
        String b=  new BigInteger(this.uPro.bytesToHex(tinfo), 16).toString(2);
        System.out.println("Binario info: "+b);
      
        b=this.uPro.convertirBits(b);
        
        int rele1= Integer.parseInt(b.substring(0, 1));
        int gpstracking1= Integer.parseInt(b.substring(1, 2));
        int evento1= Integer.parseInt(b.substring(2, 5));
        int cargando1= Integer.parseInt(b.substring(5, 6));
        int accc= Integer.parseInt(b.substring(6, 7));
        int defense1= Integer.parseInt(b.substring(7, 8));
       
       
       
       byte[]   voltagelvl  = new byte[1];
       voltagelvl[0]=dato[32];
       
        byte[]   gsmsignal  = new byte[1];
        gsmsignal[0]=dato[33];
       
    
        int alarm=dato[34];
        int language=dato[35];

       
        rs = Conexion.ejecutarSQL("Select * from sistema");
        String  nombre_trama_conexion="tramaemergencial";
	           
        while (rs.next()) {
            nombre_trama_conexion= (String) rs.getString(2);       //nombre           
        }
       
        String comando;
      
        
        String fechax=date.getFecha();
        
        comando = "INSERT INTO "+nombre_trama_conexion+" (id_dispositivo, date, cantidadgps, latitud, longitud, velocidad, gps_has_positioned,real_timegps,acc, direccion, mcc, mnc, lac, rele, gpstracking, evento, cargando, defense, voltage, gsm_signal_strength, alarma, language  ,cellid,f_insert )"
                        + " VALUES (   '" + id_imei + "', '" + fecha + "', '" + ctdsatgps + "','" + latitud_decimal +"','" + longitud_decimal + "','" + velocidad + "','"+ gps_has_positioned + "','"+real_timegps+  "','"+acc1+ "','" + course + "','" + mccx + "', '" + mnc + "','" + lacx   + "', '" + rele1 + "', '" + gpstracking1 + "','" + evento1 +"','" + cargando1 + "','" + defense1 + "','" + this.uPro.bytesToHex(voltagelvl) + "', '" + this.uPro.bytesToHex(gsmsignal) + "','" +alarm + "','" + language+"','" + cellidx +  "','"+fechax + "')";    
        
        Conexion.ejecutarActualizacionSQL(comando);
      
        comando="UPDATE DISPOSITIVOS SET ULTIMA_POSICION_ID  = (SELECT ID FROM "+nombre_trama_conexion+"  WHERE id_dispositivo='" + id_imei + "'  order by id DESC  LIMIT 1)  WHERE DISPOSITIVOS.id='" + id_imei + "'";
      
        Conexion.ejecutarActualizacionSQL(comando);
      
    }
    
    /***
     * Metodo Pasar fecha posicion 4 a 9 del dato 
     * @param dato en Hexadecimal
     * @return un String con la fecha AAAA-MM-DD HH:MM:SS 
     */
    private String fecha(byte[] dato){
        int ii=0;
        String fecha = "";
        byte    datetime [] = new byte[6];
        for(int i=4;i<=9;i++){
            datetime[ii]=dato[i];
            int value= datetime[ii];
            String value2= Integer.toString(value);
            if (value<10){ value2="0"+value2;}
            fecha+=value2;
            ii++;
        }
        fecha="20"+fecha.charAt(0)+fecha.charAt(1)+"-"+fecha.charAt(2)+fecha.charAt(3)+"-"+
                fecha.charAt(4)+fecha.charAt(5)+" "+fecha.charAt(6)+fecha.charAt(7)+":"+
                fecha.charAt(8)+fecha.charAt(9)+":"+fecha.charAt(10)+fecha.charAt(11);
        return fecha;
    }
    
    /**
     * Metodo Pasar cordenadas
     * @param coordenada en Hexadecimal
     * @return un double con la coordenada ya estructurada
     */
    private double parseCordenada(byte[] coordenada){
        
        String hex = this.uPro.bytesToHex(coordenada);
        float coordenadax = Integer.parseInt(hex,16); 
        int grados = (int) (coordenadax / (500*60*60));             
        double minutos = coordenadax/(500*60)  - grados*60;
        double coode_decimal= (double) grados+ minutos/60;
        return coode_decimal;
    }
    
}
