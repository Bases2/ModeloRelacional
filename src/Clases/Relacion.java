/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Clases;

import java.util.LinkedList;

public class Relacion {

    private LinkedList<Atributo> atributos = new LinkedList<>();
    
    public void addAtributo(Atributo atributo){
        atributos.add(atributo);
    }
    
    public void addAtributo(String atributo) {
        addAtributo(atributo, "");
    }
    
    public void addAtributo(String atributo, String llave) {
        atributos.add(new Atributo(atributo, llave));
    }

    public void addAtributo(String atributo, String llave, String tipo) {
        atributos.add(new Atributo(atributo, llave, tipo));
    }
    
    public void removeAtributo(int index) {
        atributos.remove(index);
    }

    public void removeAtributo(Atributo atributo) {
        atributos.remove(atributo);
    }

    public LinkedList<Atributo> getAtributos() {
        return atributos;
    }

}
