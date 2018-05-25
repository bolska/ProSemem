/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Classes;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 *
 * @author Bolska
 */
public class EncarregadoProperty {
    private SimpleIntegerProperty id;
    private SimpleStringProperty nome;
    private Encarregado encarregado;
    
    public EncarregadoProperty(Encarregado encar){
        this.id = new SimpleIntegerProperty(encar.getId());
        this.nome = new SimpleStringProperty(encar.getNome());
        this.encarregado = encar;
    }

    public int getId() {
        return this.id.getValue();
    }

    public String getNome() {
        return this.nome.getValue();
    }
    
    public Encarregado getEncarregado(){
        return this.encarregado;
    }
}
