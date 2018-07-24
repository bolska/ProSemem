/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Dia;

import javafx.beans.property.SimpleStringProperty;

/**
 *
 * @author gabrielgarcia
 */

//Foi necessário criar essa classe em especial para fazer a tabela funcionar (é a desvantagem de usar tabela)
public class DiaConstructor {
    
    private SimpleStringProperty colHorario;
    private SimpleStringProperty colCompromissos;

    public DiaConstructor(String colHorario, String colCompromissos) {
        this.colHorario = new SimpleStringProperty(colHorario);
        this.colCompromissos = new SimpleStringProperty (colCompromissos);
    }

    public String getColHorario() {
        return colHorario.get();
    }

    public String getColCompromissos() {
        return colCompromissos.get();
    }
    
    public void setColHorario(String colHorario){
        this.colHorario = new SimpleStringProperty (colHorario);
    }
   
}