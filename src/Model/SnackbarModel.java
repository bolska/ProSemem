package Model;

import com.jfoenix.controls.JFXSnackbar;
import javafx.scene.layout.Pane;

public class SnackbarModel {
    
    private final static long DURATION = 2500;
    
    public static Pane pane;
    
    private final static String COMPROMISSO = "Compromisso ";
    public final static String COMPROMISSO_ADD = COMPROMISSO + "Adicionado";
    public final static String COMPROMISSO_DEL = COMPROMISSO + "Removido";
    public final static String COMPROMISSO_UPD = COMPROMISSO + "Atualizado";
    
    private final static String EVENTO = "Evento ";
    public final static String EVENTO_ADD = EVENTO + "Adicionado";
    public final static String EVENTO_DEL = EVENTO + "Removido";
    public final static String EVENTO_UPD = EVENTO + "Atualizado";
    
    private final static String PROTOCOLO = "Protocolo ";
    public final static String PROTOCOLO_ADD = PROTOCOLO + "Adicionado";
    public final static String PROTOCOLO_DEL = PROTOCOLO + "Removido";
    public final static String PROTOCOLO_UPD = PROTOCOLO + "Atualizado";
    
    private final static String ATIVIDADE = "Atividade ";
    public final static String ATIVIDADE_ADD = ATIVIDADE + "Adicionada";
    public final static String ATIVIDADE_DEL = ATIVIDADE + "Removida";
    public final static String ATIVIDADE_UPD = ATIVIDADE + "Atualizada";
    
    private final static String SESSAO = "Sessão ";
    public final static String SESSAO_ADD = SESSAO + "Adicionada";
    public final static String SESSAO_DEL = SESSAO + "Removida";
    public final static String SESSAO_UPD = SESSAO + "Atualizada";
    
    private final static String FAZENDA = "Fazenda ";
    public final static String FAZENDA_ADD = FAZENDA + "Adicionada";
    public final static String FAZENDA_DEL = FAZENDA + "Removida";
    public final static String FAZENDA_UPD = FAZENDA + "Atualizada";
    
    private final static String ENCARREGADO = "Encarregado ";
    public final static String ENCARREGADO_DEL = ENCARREGADO + "Removido";
    public final static String ENCARREGADO_UPD = ENCARREGADO + "Atualizado";
    
    
    public static void showSnackbar(String text){
        JFXSnackbar snackbar = new JFXSnackbar(pane);
        snackbar.show(text, DURATION);
    }
}
