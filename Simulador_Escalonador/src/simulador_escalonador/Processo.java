package simulador_escalonador;

import java.util.Date;

/**
 *
 * @author JUNIOR
 */
public class Processo {
    private int PID;
    private int tempo_criacao;
    private int tempo_fila;
    private int tempo_entrada_atend;
    private int tempo_execucao;
    private int tempo_saida;
    private int tempo_restante_atend;
    private int prioridade;

    public Processo(int PID, int tempo_criacao, int tempo_execucao) {
        this.PID = PID;
        this.tempo_criacao = tempo_criacao;
        this.tempo_execucao = tempo_execucao;
    }

    public void setTempo_fila(int tempo_fila) {
        this.tempo_fila = tempo_fila;
    }

    public void setTempo_entrada_atend(int tempo_entrada_atend) {
        this.tempo_entrada_atend = tempo_entrada_atend;
    }

    public void setTempo_execucao(int tempo_execucao) {
        this.tempo_execucao = tempo_execucao;
    }

    public void setTempo_saida(int tempo_saida) {
        this.tempo_saida = tempo_saida;
    }

    public void setTempo_criacao(int tempo_criacao)
    {
        this.tempo_criacao = tempo_criacao;
    }
    
    public void setPID(int pid)
    {
        this.PID = pid;
    }
    
    public void setTempo_restante(int tempo_restante)
    {
        this.tempo_restante_atend = tempo_restante;
    }
    
    public void diminui_tempo_restante(int tempo_restante)
    {
        this.tempo_restante_atend -= tempo_restante;
    }

    public void setPrioridade(int prioridade) {
        this.prioridade = prioridade;
    }
    
    
    
    public int getPID() {
        return PID;
    }

    public int getTempo_criacao() {
        return tempo_criacao;
    }

    public int getTempo_fila() {
        return tempo_fila;
    }

    public int getTempo_entrada_atend() {
        return tempo_entrada_atend;
    }

    public int getTempo_execucao() {
        return tempo_execucao;
    }

    public int getTempo_saida() {
        return tempo_saida;
    }
    
    public int getTempo_restante()
    {
        return tempo_restante_atend;
    }

    public int getPrioridade() {
        return prioridade;
    }
    
    
}
