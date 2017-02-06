/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tracker.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 *
 * @author Eliam Zapata
 */
public class Fecha {
    private String dia;
    private String mes;
    private String año;
    private String hora;
    private String minuto;
    private String segundo;
    
    public Fecha(){
        Calendar c = Calendar.getInstance();
	SimpleDateFormat df1 = new SimpleDateFormat("dd");
        this.dia = df1.format(c.getTime());
	SimpleDateFormat df2 = new SimpleDateFormat("MM");
        this.mes = df2.format(c.getTime());
	SimpleDateFormat df3 = new SimpleDateFormat("yyyy");
	this.año = df3.format(c.getTime());
	SimpleDateFormat df4 = new SimpleDateFormat("HH");
	this.hora = df4.format(c.getTime());
	SimpleDateFormat df5 = new SimpleDateFormat("mm");
	this.minuto = df5.format(c.getTime());
	SimpleDateFormat df6 = new SimpleDateFormat("ss");
	this.segundo = df6.format(c.getTime());
    }
    
    public String getFecha(){
        Calendar c = Calendar.getInstance();
        
        SimpleDateFormat df1 = new SimpleDateFormat("dd");
        this.dia = df1.format(c.getTime());
	SimpleDateFormat df2 = new SimpleDateFormat("MM");
        this.mes = df2.format(c.getTime());
	SimpleDateFormat df3 = new SimpleDateFormat("yyyy");
	this.año = df3.format(c.getTime());
        SimpleDateFormat df4 = new SimpleDateFormat("HH");
        this.hora = df4.format(c.getTime());
	SimpleDateFormat df5 = new SimpleDateFormat("mm");
	this.minuto = df5.format(c.getTime());
	SimpleDateFormat df6 = new SimpleDateFormat("ss");
	this.segundo = df6.format(c.getTime());
        
        return this.getAño()+"-"+this.getMes()+"-"+this.getDia()+" "+this.getHora()+":"+this.getMinuto()+":"+this.getSegundo();
    }
    public String getDia() {
        return dia;
    }

    public void setDia(String dia) {
        this.dia = dia;
    }

    public String getMes() {
        return mes;
    }

    public void setMes(String mes) {
        this.mes = mes;
    }

    public String getAño() {
        return año;
    }

    public void setAño(String año) {
        this.año = año;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public String getMinuto() {
        return minuto;
    }

    public void setMinuto(String minuto) {
        this.minuto = minuto;
    }

    public String getSegundo() {
        return segundo;
    }

    public void setSegundo(String segundo) {
        this.segundo = segundo;
    }
    
    
}
