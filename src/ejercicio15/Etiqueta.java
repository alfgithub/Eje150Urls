/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejercicio15;

/**
 *
 * @author Alfredo
 */
public class Etiqueta {

    private int id;
    private String texto;

    public Etiqueta(int id, String texto) {
        this.id = id;
        this.texto = texto;
    }

    public Etiqueta(String texto) {
        this.texto = texto;
    }

    @Override
    public String toString() {
        return this.getTexto();
    }

    @Override
    public int hashCode() {
        return this.id * this.texto.length();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Etiqueta) {
            Etiqueta e = (Etiqueta) obj;
            return this.texto.equals(e.texto);
        } else {
            return false;
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }
}
