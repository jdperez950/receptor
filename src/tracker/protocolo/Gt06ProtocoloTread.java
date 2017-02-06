/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tracker.protocolo;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.nio.ByteBuffer;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import tracker.protocolo.funciones.Gt06ProtocoloFunciones;
import tracker.util.Fecha;
import tracker.util.UtilProtocolo;

/**
 *
 * @author Eliam Zapata
 */
public class Gt06ProtocoloTread implements Runnable{

    protected Socket conexion     = null;
    protected String serverText   = null;
    private DataInputStream inp ;
    private DataOutputStream  oos;
    private int length;
    
    private Fecha date;
    
    private UtilProtocolo uPro;
    
    private int nconexion;
    private String imeix="0123456";
    private int id_imeix=666;

    public Gt06ProtocoloTread() {
        uPro=new UtilProtocolo("0123456789ABCDEF".toCharArray());
    }
    
    
    /**
     * Metodo Constructor GT07 Hilo
     * @param clientSocket
     * @param serverText
     * @param n_conexion 
     */
    public Gt06ProtocoloTread(Socket clientSocket, String serverText, int n_conexion) {
        this.conexion =   clientSocket;
        this.serverText = serverText;
        this.nconexion=   n_conexion;
    }
    
    @Override
    public void run() {
        try {
            
            receptor();
            
        } catch (IOException ex) {
            Logger.getLogger(Gt06ProtocoloTread.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Gt06ProtocoloTread.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(Gt06ProtocoloTread.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /***
     * Metodo Receptor 
     * @throws IOException
     * @throws ClassNotFoundException
     * @throws SQLException 
     */
    public void receptor() throws IOException, ClassNotFoundException, SQLException{
        byte [] datoaenviar =new byte[50];
        byte[] bytes1  = new byte[30];
        byte[] bytes2  = new byte[30];
        byte[] bytes3  = new byte[30];
        String v;
        Gt06ProtocoloFunciones f_gt =new Gt06ProtocoloFunciones();
        try {  
            while(true){  
                System.out.println("Conexion numero: "+nconexion+" :"+this.serverText);
                inp = new DataInputStream(conexion.getInputStream());
                conexion.setSoTimeout(25*60*1000);               //                                            time out *
                System.out.println("bytes disponibles: "+inp.available());

                bytes2  = new byte[10];

                System.out.println("esperando entrada de datos "+nconexion+"\n\n"); 

                inp.read(bytes2);  
                System.out.println("bytes disponibles: "+inp.available());


                if (inp.available()>0){
                    System.out.println("bytes disponibles concatenar............");
                    bytes3  = new byte[inp.available()];
                    inp.read(bytes3);
                    this.uPro=new UtilProtocolo();
                    System.out.println("dato entrada2: "+this.uPro.bytesToHex(bytes2));
                    System.out.println("dato entrada3: "+this.uPro.bytesToHex(bytes3));
                    bytes1= new byte[bytes2.length+bytes3.length];
                    bytes1 = ByteBuffer.allocate(bytes2.length+bytes3.length).put(bytes2).put(bytes3).array();
                }else{
                    bytes1= new byte[bytes2.length];
                    bytes1=bytes2;
                }
         
                v=this.uPro.bytesToHex(bytes1);
                length  = bytes1[2]+5;
         
                System.out.println("puerto interno de conexion: "+conexion.getPort());
                System.out.println("longitud: "+length);
                System.out.println("dato entrada total: "+v);
                System.out.println("dato entrada cortado: "+v.substring(0,length*2)+"\n");
                System.out.println("para pasar: "+v.substring(4, 28)); 
         
                if(bytes1[0]==0x78&&bytes1[1]==0x78){
                    date=new Fecha();
                    System.out.println(date.getHora()+":"+date.getMinuto()+":"+date.getSegundo()+" En espera "+nconexion); 
          

                    //funciones f_gt =new funciones();
        
                    int id_imeiy=f_gt.data_save(bytes1,id_imeix);
                    if (id_imeiy!=0){id_imeix=id_imeiy;}

            
                    if(bytes1[3]!=0x01&&bytes1[3]!=0x12&&bytes1[3]!=0x13&&bytes1[3]!=0x15&&bytes1[3]!=0x16&&bytes1[3]!=0x1A&&bytes1[3]!=0x80&&bytes1[3]!=0x8B){
                        break;
                    }

                    if(bytes1[3]==0x12){
                        System.out.println("Paquete de localizacion no enviar nada");
                        oos.flush();
                    }else{
                        datoaenviar = calcularcrc(bytes1);     
                        oos = new DataOutputStream (conexion.getOutputStream());
                        oos.write(datoaenviar);
                        oos.flush();
                // oos.close();
                        System.out.println("escribio dato con longitud de: " +datoaenviar.length+"\n\n******************************************\n");
                    }
  
        
        }else{ System.out.println("error de entrada!!!!!!");
            break;
        }
    }
    } catch (SocketTimeoutException ste) {
        ste.printStackTrace();
        System.out.println("Puerto cerrado: "+conexion.getPort());
        //funciones f_gt =new funciones();
        f_gt.estado( id_imeix);
        oos.flush();
        conexion.close();

      }
     
     System.out.println("Puerto cerrado: "+conexion.getPort());
     
     f_gt.estado(id_imeix);
     System.out.println("salio del while xq no tenia ningun protocolo la trama");
     oos.flush();
     conexion.close();
     
    }
    
    
    public  byte[] calcularcrc(byte bytesinterno[]){
    
        length  = bytesinterno[2]+5;
    
        byte    datopruebaresponsecrc [] =   new byte[4];
                datopruebaresponsecrc [0] =  (byte) 0x05;       
                datopruebaresponsecrc [1] =  bytesinterno[3];              //(byte) 0x01;       
                datopruebaresponsecrc [2] =  bytesinterno[length-6];       //serial info
                datopruebaresponsecrc [3] =  bytesinterno[length-5];       //serial info

        String  cadena=this.uPro.bytesToHex(datopruebaresponsecrc);   //solo info
        System.out.println("*** bytes crcr: "+cadena);          //

        int crcr = GetCrc16( datopruebaresponsecrc, 4 );
        
        System.out.println("*** crcr para responder: "+ Integer.toHexString(crcr)+ " longitud: "+datopruebaresponsecrc.length);  

        return enviarlogin(crcr, bytesinterno);

    }

    public  byte[] enviarlogin(int crcraenviar, byte byteslogin[]){

       String xxx=    Integer.toBinaryString(crcraenviar);
       int longitud_crc= xxx.length();
       length  = byteslogin[2]+5;
       System.out.println("*** longitud!!!!: "+length);  
       
       switch (longitud_crc){
           case 8:   xxx="00000000"+xxx;    break;
           case 9:   xxx="0000000"+xxx;    break;
           case 10:  xxx="000000"+xxx;    break;
           case 11:  xxx="00000"+xxx;    break;
           case 12:  xxx="0000"+xxx;    break;
           case 13:  xxx="000"+xxx;    break;
           case 14:  xxx="00"+xxx;    break;
           case 15:  xxx="0"+xxx;    break;
       }
       System.out.println("xxx="+xxx);
       String crcr2_1=  xxx.substring(xxx.length()-8,  xxx.length());
       String crcr2_2=  xxx.substring(xxx.length()-16, xxx.length()-8);
       
       System.out.println("Cadena 2: " +crcr2_2+" Cadena 1: "+crcr2_1);

       int e0 = Integer.parseInt(crcr2_1, 2);
       int e1 = Integer.parseInt(crcr2_2, 2);


        byte    datosalidacrc [] =     new byte[10];
                datosalidacrc [0] =  (byte) 0x78;       //    (byte) 0x0D;
                datosalidacrc [1] =  (byte) 0x78;
                datosalidacrc [2] =  (byte) 0x05;       //    (byte) 0x0D;
                datosalidacrc [3] =  byteslogin[3];   //(byte) 0x01;       //    (byte) 0x01;
                datosalidacrc [4] =  byteslogin[length-6];       //    (byte) 0x45;
                datosalidacrc [5] =  byteslogin[length-5];       //    (byte) 0x67;
                datosalidacrc [6] = (byte) e1;
                datosalidacrc [7] = (byte) e0;    
                datosalidacrc [8] = (byte) 0x0D;        //    (byte) 0x45;
                datosalidacrc [9] = (byte) 0x0A;  
                
      /* byte    borrame [] =     new byte[1];  
       
                borrame [0] =  byteslogin[byteslogin.length-2];
                borrame [1] =  byteslogin[byteslogin.length-1];

       String  cadenac=bytesToHex(borrame);   //solo info
       System.out.println("*** serial!!!!: "+cadenac);  */
                
        String  datointerno=this.uPro.bytesToHex(datosalidacrc);

        System.out.println("*/enviar login/* dato responder: "+datointerno);

        return datosalidacrc;
    }  
    
    public static short GetCrc16(byte[] pData, int nLength)
    {
           short fcs = (short) 0xffff; // Initialization
           int i;
           for(i=0;i<nLength;i++)
           {
           fcs = (short) (((fcs & 0xFFFF) >>> 8) ^ crctab16[(fcs ^ pData[i]) & 0xff]);
           }
           return (short) ~fcs;
    }
    
    private static short crctab16[] ={
    (short)0x0000, (short)0x1189, (short)0x2312, (short)0x329b, (short)0x4624, (short)0x57ad, (short)0x6536, (short)0x74bf,
    (short)0x8c48, (short)0x9dc1, (short)0xaf5a, (short)0xbed3, (short)0xca6c, (short)0xdbe5, (short)0xe97e, (short)0xf8f7,
    (short)0x1081, (short)0x0108, (short)0x3393, (short)0x221a, (short)0x56a5, (short)0x472c, (short)0x75b7, (short)0x643e,
    (short)0x9cc9, (short)0x8d40, (short)0xbfdb, (short)0xae52, (short)0xdaed, (short)0xcb64, (short)0xf9ff, (short)0xe876,
    (short)0x2102, (short)0x308b, (short)0x0210, (short)0x1399, (short)0x6726, (short)0x76af, (short)0x4434, (short)0x55bd,
    (short)0xad4a, (short)0xbcc3, (short)0x8e58, (short)0x9fd1, (short)0xeb6e, (short)0xfae7, (short)0xc87c, (short)0xd9f5,
    (short)0x3183, (short)0x200a, (short)0x1291, (short)0x0318, (short)0x77a7, (short)0x662e, (short)0x54b5, (short)0x453c, 
    (short)0xbdcb, (short)0xac42, (short)0x9ed9, (short)0x8f50, (short)0xfbef, (short)0xea66, (short)0xd8fd, (short)0xc974,
    (short)0x4204, (short)0x538d, (short)0x6116, (short)0x709f, (short)0x0420, (short)0x15a9, (short)0x2732, (short)0x36bb,
    (short)0xce4c, (short)0xdfc5, (short)0xed5e, (short)0xfcd7, (short)0x8868, (short)0x99e1, (short)0xab7a, (short)0xbaf3,
    (short)0x5285, (short)0x430c, (short)0x7197, (short)0x601e, (short)0x14a1, (short)0x0528, (short)0x37b3, (short)0x263a,
    (short)0xdecd, (short)0xcf44, (short)0xfddf, (short)0xec56, (short)0x98e9, (short)0x8960, (short)0xbbfb, (short)0xaa72,
    (short)0x6306, (short)0x728f, (short)0x4014, (short)0x519d, (short)0x2522, (short)0x34ab, (short)0x0630, (short)0x17b9,
    (short)0xef4e, (short)0xfec7, (short)0xcc5c, (short)0xddd5, (short)0xa96a, (short)0xb8e3, (short)0x8a78, (short)0x9bf1,
    (short)0x7387, (short)0x620e, (short)0x5095, (short)0x411c, (short)0x35a3, (short)0x242a, (short)0x16b1, (short)0x0738,
    (short)0xffcf, (short)0xee46, (short)0xdcdd, (short)0xcd54, (short)0xb9eb, (short)0xa862, (short)0x9af9, (short)0x8b70,
    (short)0x8408, (short)0x9581, (short)0xa71a, (short)0xb693, (short)0xc22c, (short)0xd3a5, (short)0xe13e, (short)0xf0b7,
    (short)0x0840, (short)0x19c9, (short)0x2b52, (short)0x3adb, (short)0x4e64, (short)0x5fed, (short)0x6d76, (short)0x7cff,
    (short)0x9489, (short)0x8500, (short)0xb79b, (short)0xa612, (short)0xd2ad, (short)0xc324, (short)0xf1bf, (short)0xe036,
    (short)0x18c1, (short)0x0948, (short)0x3bd3, (short)0x2a5a, (short)0x5ee5, (short)0x4f6c, (short)0x7df7, (short)0x6c7e,
    (short)0xa50a, (short)0xb483, (short)0x8618, (short)0x9791, (short)0xe32e, (short)0xf2a7, (short)0xc03c, (short)0xd1b5,
    (short)0x2942, (short)0x38cb, (short)0x0a50, (short)0x1bd9, (short)0x6f66, (short)0x7eef, (short)0x4c74, (short)0x5dfd,
    (short)0xb58b, (short)0xa402, (short)0x9699, (short)0x8710, (short)0xf3af, (short)0xe226, (short)0xd0bd, (short)0xc134,
    (short)0x39c3, (short)0x284a, (short)0x1ad1, (short)0x0b58, (short)0x7fe7, (short)0x6e6e, (short)0x5cf5, (short)0x4d7c,
    (short)0xc60c, (short)0xd785, (short)0xe51e, (short)0xf497, (short)0x8028, (short)0x91a1, (short)0xa33a, (short)0xb2b3,
    (short)0x4a44, (short)0x5bcd, (short)0x6956, (short)0x78df, (short)0x0c60, (short)0x1de9, (short)0x2f72, (short)0x3efb,
    (short)0xd68d, (short)0xc704, (short)0xf59f, (short)0xe416, (short)0x90a9, (short)0x8120, (short)0xb3bb, (short)0xa232,
    (short)0x5ac5, (short)0x4b4c, (short)0x79d7, (short)0x685e, (short)0x1ce1, (short)0x0d68, (short)0x3ff3, (short)0x2e7a,
    (short)0xe70e, (short)0xf687, (short)0xc41c, (short)0xd595, (short)0xa12a, (short)0xb0a3, (short)0x8238, (short)0x93b1,
    (short)0x6b46, (short)0x7acf, (short)0x4854, (short)0x59dd, (short)0x2d62, (short)0x3ceb, (short)0x0e70, (short)0x1ff9,
    (short)0xf78f, (short)0xe606, (short)0xd49d, (short)0xc514, (short)0xb1ab, (short)0xa022, (short)0x92b9, (short)0x8330,
    (short)0x7bc7, (short)0x6a4e, (short)0x58d5, (short)0x495c, (short)0x3de3, (short)0x2c6a, (short)0x1ef1, (short)0x0f78
    };
      

    
}
