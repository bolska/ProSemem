package Classes;

import BancoDeDados.DaoProtocolo;
import java.sql.Date;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Sessao 
{
    private SimpleStringProperty id;
    private SimpleStringProperty estado;
    private SimpleStringProperty protocoloDescricao;
    private SimpleStringProperty cor;
    private SimpleIntegerProperty protocoloId;
    private SimpleIntegerProperty fazendaId;

    private Date dataAbertura;
    private Date dataEncerramento;

    public String getId() {
        return id.getValue();
    }

    public void setId(String id) {
        this.id = new SimpleStringProperty(id);
    }

    public String getEstado() {
        return estado.getValue();
    }

    public void setEstado(String estado) {
        this.estado = new SimpleStringProperty(estado);
    }

    public int getFazendaId() {
        return fazendaId.getValue();
    }

    public void setFazendaId(int fazendaId) {
        this.fazendaId = new SimpleIntegerProperty(fazendaId);
    }
    
    public int getProtocoloId() {
        return protocoloId.getValue();
    }

    public void setProtocoloId(int protocoloId) {
        this.protocoloId = new SimpleIntegerProperty(protocoloId);
    }

    public Date getDataAbertura() {
        return dataAbertura;
    }

    public void setDataAbertura(Date dataAbertura) {
        this.dataAbertura = dataAbertura;
    }

    public Date getDataEncerramento() {
        return dataEncerramento;
    }

    public void setDataEncerramento(Date dataEncerramento) {
        this.dataEncerramento = dataEncerramento;
    }

    public String getCor() {
        return cor.getValue();
    }

    public void setCor(String cor) {
        this.cor = new SimpleStringProperty(cor);
    }
    
    public String getProtocoloDescricao(){
        DaoProtocolo daoProtocolo = new DaoProtocolo();
        Protocolo protocolo = daoProtocolo.getProtocoloById(getProtocoloId());
        return protocolo.getDescricao();
    }
}
