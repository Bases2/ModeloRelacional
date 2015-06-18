/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Clases;

import java.util.LinkedList;

public class Relacion {

    private LinkedList<Atributo> atributos = new LinkedList<>();

    public Atributo buscarAtributo(String nombreMateria) {
        for (Atributo aa1 : atributos) {
            if (aa1.getNombre().compareToIgnoreCase(nombreMateria) == 0) {
                return aa1;
            }
        }
        return null;
    }
    
    public int posicionAtributo(String nombreMateria) {
        int i = 0;
        for (Atributo aa1 : atributos) {
            if (aa1.getNombre().compareToIgnoreCase(nombreMateria) == 0) {
                return i;
            }
        }
        return 0;
    }

    public void addAtributo(Atributo atributo) {
        atributos.add(atributo);
    }

    public void addAtributo(String atributo) {
        addAtributo(atributo, "");
    }

    public void addAtributo(String atributo, String tipo) {
        atributos.add(new Atributo(atributo, tipo));
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

    public void removeAllAtributo() {
        int n = atributos.size();
        for (int i = 0; i < n; i++) {
            atributos.remove(0);
        }
    }

    public LinkedList<Atributo> getAtributos() {
        return atributos;
    }

    public void setAtributos(LinkedList<Atributo> atributos) {
        this.atributos = atributos;
    }
}
