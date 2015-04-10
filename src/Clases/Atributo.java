/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Clases;

public class Atributo {

    private String Nombre;
    private String Llaves;
    private String Tipo;

    
    public Atributo(String Nombre, String Llaves, String Tipo) {
        this.Nombre = Nombre;
        this.Llaves = Llaves;
        this.Tipo = Tipo;
    }

    public Atributo(String Nombre, String Tipo) {
        this.Nombre = Nombre;
        this.Tipo = Tipo;
        this.Llaves = "";
    }
        

    public void setTipo(String Tipo) {
        this.Tipo = Tipo;
    }

    public String getTipo() {
        return Tipo;
    }

    public Atributo(String Nombre) {
        this.Nombre = Nombre;
        this.Llaves = "";
    }

    public Atributo() {
        this.Llaves = "";
    }

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String Nombre) {
        this.Nombre = Nombre;
    }

    public String getLlaves() {
        return Llaves;
    }

    public void setLlaves(String... llaves) {
        this.Llaves = "";
        addLlaves(llaves);
    }

    public void addLlaves(String... llaves) {
        for (String llv : llaves) {
            this.Llaves += llv;
        }
    }

}
