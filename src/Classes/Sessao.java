package Classes;

import BancoDeDados.DaoFazenda;
import BancoDeDados.DaoProtocolo;
import java.sql.Date;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;

public class Sessao 
{
    private String id;
    private String estado;;
    private String cor;
    private int protocoloId;
    private String protocoloDescricao;;
    private int fazendaId;
    private String fazendaNome;
    private ObjectProperty<Date> dataAbertura;
    private ObjectProperty<Date> dataEncerramento;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public int getFazendaId() {
        return fazendaId;
    }

    public void setFazendaId(int fazendaId) {
        this.fazendaId = fazendaId;
    }
    
    public int getProtocoloId() {
        return protocoloId;
    }

    public void setProtocoloId(int protocoloId) {
        this.protocoloId = protocoloId;
    }

    public Date getDataAbertura() {
        return dataAbertura.getValue();
    }

    public void setDataAbertura(Date dataAbertura) {
        this.dataAbertura = new SimpleObjectProperty<>(dataAbertura);
    }

    public ObjectProperty dataAberturaProperty(){
        return dataAbertura;
    } 
    
    public Date getDataEncerramento() {
        return dataEncerramento.getValue();
    }

    public void setDataEncerramento(Date dataEncerramento) {
        this.dataEncerramento = new SimpleObjectProperty<>(dataEncerramento);
    }
    
    public ObjectProperty dataEncerramentoProperty(){
        return dataEncerramento;
    }

    public String getCor() {
        return cor;
    }

    public void setCor(String cor) {
        this.cor = cor;
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
