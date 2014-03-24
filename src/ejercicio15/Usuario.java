/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ejercicio15;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Alfredo
 */
public class Usuario {
    
    private int id;
    private String loguin;
    private String pass;
    private Map<Integer, Url> urls = new HashMap<Integer, Url>();
    
    public Usuario(String loguin, String pass) {        
        this.loguin = loguin;
        this.pass = pass;
    }
    
    public Usuario(int id, String loguin, String pass) {
        this.id = id;
        this.loguin = loguin;
        this.pass = pass;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLoguin() {
        return loguin;
    }

    public void setLoguin(String loguin) {
        this.loguin = loguin;
    }
    
    public Map<Integer, Url> getUrls() {
        return urls;
    }

    public void setUrls(Map<Integer, Url> urls) {
        this.urls = urls;
    }

    
    
}
