/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tracker.util;

/**
 *
 * @author Diseño
 */
public class UtilProtocolo {
    
    protected  char[] hexArray = "0123456789ABCDEF".toCharArray();
    
    
    
    public UtilProtocolo() {
    }

    public UtilProtocolo(char[] hexArray) {
        this.hexArray=hexArray;
    }
    /**
     * Metodo bytesToHex bytes a Hexa
     * @param bytes
     * @return 
     */
    public String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for ( int j = 0; j < bytes.length; j++ ) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }
    
    /**
     * Convertir dato que llega de Bits
     * @param b
     * @return 
     */
    public String convertirBits(String b){
        
        switch (b.length()){
           case 0:  b="00000000"+b; break;
           case 1:  b="0000000"+b;  break;
           case 2:  b="000000"+b;   break;
           case 3:  b="00000"+b;    break;
           case 4:  b="0000"+b;     break;
           case 5:  b="000"+b;      break;
           case 6:  b="00"+b;       break;
           case 7:  b="0"+b;        
        }
        if(b.length()>8)
            b=b.substring(b.length()-8);
        return b;
    }
}
