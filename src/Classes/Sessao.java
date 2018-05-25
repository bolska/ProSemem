package Classes;

import java.sql.Date;

/**
 *
 * @author user
 */
public class Sessao {
    private String id;
    private String estado;
    private int protocoloId;
    private int fazendaId;
    private Date dataAbertura;
    private Date dataEncerramento;
    private String cor;

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
        return cor;
    }

    public void setCor(String cor) {
        this.cor = cor;
    }
}
