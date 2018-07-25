
package RegrasNegocio;

import BancoDeDados.DaoAtividade;
import BancoDeDados.DaoCompromisso;
import BancoDeDados.DaoEncarregado;
import BancoDeDados.DaoEvento;
import BancoDeDados.DaoFazenda;
import BancoDeDados.DaoProtocolo;
import BancoDeDados.DaoSessao;
import Classes.Atividade;
import Classes.Compromisso;
import Classes.Encarregado;
import Classes.Evento;
import Classes.Fazenda;
import Classes.Protocolo;
import Classes.Sessao;
import java.sql.Date;
import java.time.DayOfWeek;
import java.time.LocalDate;
import javafx.collections.ObservableList;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

/**
 *
 * @author Bolska
 */
public class Verify {
    
    public boolean verificaDomingo(String labelId){
        if(labelId.equals("domingo")){        
            return true;
        } else {
            return false;
        }
    }
    
    public static boolean protocoloHasPrincipal(Protocolo protocolo){
        DaoAtividade daoAtividade = new DaoAtividade();
        Atividade atvP = daoAtividade.getAtividadePrincipalProtocolo(protocolo.getId());
        if(atvP.getTipo() != null){
            return true;
        }
        else{
            return false;
        }    
    }
    
    public static boolean isCompromissoInsertedOn(VBox vBox, String selected){
        DaoCompromisso daoCompromisso = new DaoCompromisso();
        Compromisso compromisso = daoCompromisso.getCompromissoById(Integer.parseInt(selected));
        
        for(int i = 0; i < vBox.getChildren().size(); i++){
            Label label = (Label) vBox.getChildren().get(i);
            if(label.getText().equals(compromisso.getDescricao())){
                return true;
            }
        }
        return false;
    }
    
    public static boolean hasEqual(Compromisso compromisso){
        DaoCompromisso daoCompromisso = new DaoCompromisso();
        
        if(compromisso.getTipo().equals("V")){
            ObservableList<Compromisso> listCompromisso = daoCompromisso.getListCompromissoVeterinario();

            for(int i = 0; i < listCompromisso.size(); i++){
                if(listCompromisso.get(i).getDescricao().equals(compromisso.getDescricao())){
                    return true;
                }
            }
        }
        else{
            //Tipo A (avulso)
            ObservableList<Compromisso> listCompromisso = daoCompromisso.getListCompromissoAvulso();
            
            for(Compromisso comp : listCompromisso){
                if(comp.getDescricao().equals(compromisso.getDescricao())){
                    return true;
                }
            }
        }
        
        return false;
    }
    
    public static boolean hasEqual(Sessao sessao){
        DaoSessao daoSessao = new DaoSessao();
        ObservableList<Sessao> listSessao = daoSessao.getListSessao();
        
        for(int i = 0; i < listSessao.size(); i++){
            if(listSessao.get(i).getId().equals(sessao.getId())){
                return true;
            }
        }
        return false;
    }
    
    public static boolean hasEqual(Protocolo protocolo){
        DaoProtocolo daoProtocolo = new DaoProtocolo();
        ObservableList<Protocolo> listProtocolo = daoProtocolo.getListProtocolo();
        
        for(int i = 0; i < listProtocolo.size(); i++){
            if(listProtocolo.get(i).getDescricao().equals(protocolo.getDescricao())){
                return true;
            }
        }
        return false;
    }
    
    public static boolean hasEqual(Fazenda fazenda){
        DaoFazenda daoFazenda = new DaoFazenda();
        ObservableList<Fazenda> listFazenda = daoFazenda.getListFazenda();

        if(fazenda.getNome() != null && fazenda.getSigla() != null){
            for(int i = 0; i < listFazenda.size(); i++){
                if(listFazenda.get(i).getNome().equals(fazenda.getNome()) && listFazenda.get(i).getSigla().equals(fazenda.getSigla())){
                    return true;
                }
            }
            return false;
        }
        else if(fazenda.getNome() != null && fazenda.getSigla() == null){
            for(int i = 0; i < listFazenda.size(); i++){
                if(listFazenda.get(i).getSigla().equals(fazenda.getSigla())){
                    return true;
                }
            }
            return false;
        }
        else if(fazenda.getNome() == null && fazenda.getSigla() != null){
            for(int i = 0; i < listFazenda.size(); i++){
                if(listFazenda.get(i).getNome().equals(fazenda.getNome())){
                    return true;
                }
            }
            return false;
        }
        else{
            return false;
        }
    }
    
    public static boolean hasEqual(Encarregado encarregado){
        DaoEncarregado daoEncarregado = new DaoEncarregado();
        ObservableList<Encarregado> listEncarregado = daoEncarregado.getListEncarregado();
        
        for(int i = 0; i < listEncarregado.size(); i++){
            if(listEncarregado.get(i).getNome().equals(encarregado.getNome())){
                return true;
            }
        }
        return false;
    }
    
    public static boolean hasEvent(Protocolo protocolo){
        DaoEvento daoE = new DaoEvento();
        ObservableList<Evento> listEvento = daoE.getListaEventoByProtocoloId(protocolo.getId());
        
        if(listEvento.size() > 0){
            return true;
        }
        
        return false;
    }
    
    public static boolean hasEvent(Atividade atividade){
        DaoEvento daoE = new DaoEvento();
        ObservableList<Evento> listEvento = daoE.getListaEventoByAtividadeId(atividade.getId());
        
        if(listEvento.size() > 0){
            return true;
        }
        
        return false;
    }
    
    public static boolean hasEventOnDate(Date date){
        DaoEvento daoE = new DaoEvento();
        int count = daoE.getCountEventoByDate(date);
        
        if(count >= 1){
            return true;
        }
        return false;
    }
    
    public static boolean isSunday(Date sqlDate){
        LocalDate date = sqlDate.toLocalDate();
        if(date.getDayOfWeek() == DayOfWeek.SUNDAY){
            return true;
        }
        else{
            return false;
        }
    }
}
