package Classes;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Encarregado {
    private SimpleIntegerProperty id;
    private SimpleStringProperty nome;

    public int getId() {
        return id.getValue();
    }

    public void setId(int id) {
        this.id = new SimpleIntegerProperty(id);
    }

    public String getNome() {
        return nome.getValue();
    }

    public void setNome(String nome) {
        this.nome = new SimpleStringProperty(nome);
    }
    
}
