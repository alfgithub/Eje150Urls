/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ejercicio15;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 *
 * @author Alfredo
 */
public class Url {
    
    private int id;
    private String direccion;    
    private Map<Integer, Etiqueta> etiquetas = new HashMap<Integer, Etiqueta>();
    private Map<Integer, String> comentarios = new HashMap<Integer, String>();
    
    public Url(String direccion) {
        this.direccion = direccion;
    }
    
    public Url(int id, String direccion) {
        this.id = id;
        this.direccion = direccion;
    }

    @Override
    public String toString() {        
        String et = "";
        Iterator iter = comentarios.entrySet().iterator();
        String com = "";

        for(Etiqueta e: etiquetas.values()){
            et += e.getTexto() + " ";
        }
    
        
        while (iter.hasNext()) {
            Map.Entry e = (Map.Entry)iter.next();
            com += " - " + e.getValue() + "\n ";            
        }
              System.out.println(this.direccion + "\n" + et + "\n" + com);
        
        return this.direccion + "\n" + et + "\n" + com;
    }//toString

    

    @Override
    public boolean equals(Object o) {
         if (o instanceof Url) {
            Url u = (Url)o;
            return this.direccion.equals(u.direccion);
        } else {
                return false;
            }
    }

    @Override
    public int hashCode() {
        return this.id * this.direccion.length();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public Map<Integer, Etiqueta> getEtiquetas() {
        return etiquetas;
    }

    public void setEtiquetas(Map<Integer, Etiqueta> etiquetas) {
        this.etiquetas = etiquetas;
    }

    public Map<Integer, String> getComentarios() {
        return comentarios;
    }

    public void setComentarios(Map<Integer, String> comentarios) {
        this.comentarios = comentarios;
    }
    
}//Url


//--http://lineadecodigo.com/java/usando-las-clases-hashset-y-hashmap/ 