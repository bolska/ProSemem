package Classes;

import BancoDeDados.DaoFazenda;
import BancoDeDados.DaoProtocolo;
import java.sql.Date;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

public class Sessao 
{
    private SimpleStringProperty id;
    private SimpleStringProperty estado;
    private SimpleStringProperty cor;
    private SimpleIntegerProperty protocoloId;
    private SimpleStringProperty protocoloDescricao;
    private SimpleIntegerProperty fazendaId;
    private SimpleStringProperty fazendaNome;
    private ObjectProperty<Date> dataAbertura;
    private ObjectProperty<Date> dataEncerramento;

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
        return dataAbertura.getValue();
    }

    public void setDataAbertura(Date dataAbertura) {
        this.dataAbertura = new SimpleObjectProperty<Date>(dataAbertura);
    }

    public ObjectProperty dataAberturaProperty(){
        return dataAbertura;
    } 
    
    public Date getDataEncerramento() {
        return dataEncerramento.getValue();
    }

    public void setDataEncerramento(Date dataEncerramento) {
        this.dataEncerramento = new SimpleObjectProperty<Date>(dataEncerramento);
    }
    
    public ObjectProperty dataEncerramentoProperty(){
        return dataEncerramento;
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
    
    public String getFazendaNome(){
        DaoFazenda daoFazenda = new DaoFazenda();
        Fazenda fazenda = daoFazenda.getFazendaById(getFazendaId());
        return fazenda.getNome();
    }
}
