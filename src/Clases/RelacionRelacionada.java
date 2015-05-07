/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Clases;

import GUI.Principal;

/**
 *
 * @author Koaski
 */
public class RelacionRelacionada {
    Principal.VentanaInterna relacion1;
    Principal.VentanaInterna relacion2;
    String campo1;
    String campo2;

    public RelacionRelacionada(Principal.VentanaInterna relacion1, String campo1, Principal.VentanaInterna relacion2, String campo2) {
        this.relacion1 = relacion1;
        this.relacion2 = relacion2;
        this.campo1 = campo1;
        this.campo2 = campo2;
    }

    public String getCampo1() {
        return campo1;
    }

    public void setCampo1(String campo1) {
        this.campo1 = campo1;
    }

    public String getCampo2() {
        return campo2;
    }

    public void setCampo2(String campo2) {
        this.campo2 = campo2;
    }

    public Principal.VentanaInterna getRelacion1() {
        return relacion1;
    }

    public Principal.VentanaInterna getRelacion2() {
        return relacion2;
    }

    public void setRelacion1(Principal.VentanaInterna relacion1) {
        this.relacion1 = relacion1;
    }

    public void setRelacion2(Principal.VentanaInterna relacion2) {
        this.relacion2 = relacion2;
    }
}

