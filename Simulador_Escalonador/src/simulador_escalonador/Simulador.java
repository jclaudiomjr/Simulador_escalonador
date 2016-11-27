package simulador_escalonador;

import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

/**
 * Programa simulador de escalonadores de processos.
 * Escalonadores simulados:
 * - SJF (Shortest-Job_First)
 * - Prioridade não-preemptiva
 * - Round Robin
 * - LCFS (Last-Come-First-Server)
 * - FIFO com Nº de servidores (First-In-First-Out)
 * 
 * @author JUNIOR
 */
public class Simulador 
{
    private static int qtd_processos;       /** Armazena a quantidade de processos a simular. */
    private static int quantum;     /** Armazena o quantum de tempo (este deve ser um numero entre 2 e 4, inclusive. */
    private static int escalonador;     /** Armazena a escolha do usuário de qual escalonador o programa irá simular. */
    private static ArrayList<Processo> fila = new ArrayList<>();        /** Fila de processos criados. */
    private static ArrayList<Processo> executa = new ArrayList<>();     /** ArrayList que será mostrado na tabela. */
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        getEscalonador();
        getQtd_processos();
        fila.clear();
        executa.clear();
        
        switch(escalonador)
        {
            case 1:
                sjf();
                break;
            case 2:
                prioridadeNaoPreemptiva();
                break;
            case 3:
                getQuantum();
                round_robin();
                break;
            case 4:
                lcfs();
                break;
            default:
                break;
        }
        
        
        
        
    }
    
    /**
     * Mostra as opções de escalonador e recebe o escalonador desejado.
     */
    public static void getEscalonador()
    {
        Scanner leitura = new Scanner(System.in);
        
        System.out.println("\t____________________________________");
        System.out.println("\t|Selecione o escalonador desejado: |");
        System.out.println("\t|1 - SJF (Shortest-Job_First)      |");
        System.out.println("\t|2 - Prioridade não-preemptiva     |");
        System.out.println("\t|3 - Round Robin                   |");
        System.out.println("\t|4 - LCFS (Last-Come-First-Server) |");
        System.out.println("\t|__________________________________|");
        System.out.print("\tEscalonador: ");
        escalonador = leitura.nextInt();
        leitura.reset();
    }
    
    /**
     * Recebe a quantidade de processos que deseja simular.
     */
    public static void getQtd_processos()
    {
        Scanner leitura = new Scanner(System.in);
        System.out.print("\n\tInforme a quantidade de processos a simular: ");
        qtd_processos = leitura.nextInt();
        leitura.reset();
    }
    
    /**
     * Recebe a quantidade de quantum de tempo.
     */
    public static void getQuantum()
    {
        Scanner leitura = new Scanner(System.in);
        boolean quantum_valido = false;
        
        System.out.print("\tInforme o quantum de tempo: ");
        while(!quantum_valido)
        {
            quantum = leitura.nextInt();
            leitura.reset();
            if ( ( quantum >= 2 ) && ( quantum <= 4 ) )
            {
                quantum_valido = true;
            }
            else
            {
                System.out.println("\tO quantum deve ser de 2 a 4, informe novamente: ");
            }
        } 
    }
    
    /**
     * Gera o PID dos processos, de forma sequencial.
     * @return PID - Pid de processo.
     */
    public static int geraPID()
    {
        int PID = 1;
        
        if ( fila.isEmpty() )
            return PID;
        else
            return fila.get(fila.size()-1).getPID()+1;
    }
    
    /**
     * Aplica o escalonador LCFS (Last-Come-First-Server).
     */
    public static void lcfs()
    {
        Random numero_aleatorio = new Random();
        int aux, i, j, marca_indice = 0;
        
        for ( i = 0 ; i < qtd_processos ; i++ )
        {
            if ( fila.isEmpty() )
            {
                fila.add(new Processo(geraPID(), numero_aleatorio.nextInt(4), (1+ numero_aleatorio.nextInt(11))));
            }
            else
            {
                fila.add(new Processo(geraPID(),(fila.get(i-1).getTempo_criacao()+(1+numero_aleatorio.nextInt(4))), (1+ numero_aleatorio.nextInt(11))));
            }
        }
        executa.add(fila.get(0));
        executa.get(0).setTempo_fila(0);
        executa.get(0).setTempo_entrada_atend(0);
        executa.get(0).setTempo_saida(fila.get(0).getTempo_entrada_atend() + fila.get(0).getTempo_execucao());
        fila.remove(0);
        
        for ( i = 0 ; i < executa.size() ; i++ )
        {
            if ( ! fila.isEmpty() )
            {    
                for ( j = 0 ; j < fila.size() ; j++ )
                {
                    if ( fila.get(j).getTempo_criacao() <= executa.get(i).getTempo_saida() )
                    {
                        marca_indice = j;
                    }
                    else
                    {
                        j = fila.size();
                    }
                }
            }
            else
            {
                break;
            }
            
            aux = executa.get(i).getTempo_saida() - fila.get(marca_indice).getTempo_criacao();
            if ( aux < 0 )
            {
                aux = 0;
            }
            fila.get(marca_indice).setTempo_fila(aux);
            if ( aux == 0 )
            {
                fila.get(marca_indice).setTempo_entrada_atend(fila.get(marca_indice).getTempo_criacao());
            }
            else
            {
                fila.get(marca_indice).setTempo_entrada_atend(executa.get(i).getTempo_saida());
            }
            fila.get(marca_indice).setTempo_saida(fila.get(marca_indice).getTempo_entrada_atend() + fila.get(marca_indice).getTempo_execucao());
            executa.add(fila.get(marca_indice));
            fila.remove(marca_indice); 
        }
        mostrarTabela(); 
    }
    
    /**
     * Aplica o escalonador Prioridade não-preemptiva.
     */
    public static void prioridadeNaoPreemptiva()
    {
        Random numero_aleatorio = new Random();
        int aux, i, j, marca_indice = 0, marca_indice2 = 0, menor_prioridade = 100;
        
        for ( i = 0 ; i < qtd_processos ; i++ )
        {
            if ( fila.isEmpty() )
            {
                fila.add(new Processo(geraPID(), numero_aleatorio.nextInt(4), (1+ numero_aleatorio.nextInt(11))));
                fila.get(i).setPrioridade(1 + numero_aleatorio.nextInt(3));
            }
            else
            {
                fila.add(new Processo(geraPID(),(fila.get(i-1).getTempo_criacao()+(1+numero_aleatorio.nextInt(4))), (1+ numero_aleatorio.nextInt(11))));
                fila.get(i).setPrioridade(1 + numero_aleatorio.nextInt(3));
            }
            System.out.println("Criei!");
            System.out.println("\t"+fila.get(i).getTempo_criacao()+"\t"+fila.get(i).getTempo_execucao());
        }
        executa.add(fila.get(0));
        executa.get(0).setTempo_fila(0);
        executa.get(0).setTempo_entrada_atend(0);
        executa.get(0).setTempo_saida(fila.get(0).getTempo_entrada_atend() + fila.get(0).getTempo_execucao());
        fila.remove(0);   
        
        for ( i = 0 ; i < executa.size() ; i++ )
        {
            if ( ! fila.isEmpty() )
            {    
                for ( j = 0 ; j < fila.size() ; j++ )
                {
                    if ( fila.get(j).getTempo_criacao() <= executa.get(i).getTempo_saida() )
                    {
                        marca_indice = j;
                    }
                    else
                    {
                        j = fila.size();
                    }
                }
                for ( j = 0 ; j <= marca_indice ; j++ )
                {
                    if ( fila.get(j).getPrioridade() < menor_prioridade )
                    {
                        menor_prioridade = fila.get(j).getPrioridade();
                        marca_indice2 = j;
                    }
                }
                
            }
            else
            {
                break;
            }
            
            aux = executa.get(i).getTempo_saida() - fila.get(marca_indice2).getTempo_criacao();
            if ( aux < 0 )
            {
                aux = 0;
            }
            fila.get(marca_indice2).setTempo_fila(aux);
            if ( aux == 0 )
            {
                fila.get(marca_indice2).setTempo_entrada_atend(fila.get(marca_indice2).getTempo_criacao());
            }
            else
            {
                fila.get(marca_indice2).setTempo_entrada_atend(executa.get(i).getTempo_saida());
            }
            fila.get(marca_indice2).setTempo_saida(fila.get(marca_indice2).getTempo_entrada_atend() + fila.get(marca_indice2).getTempo_execucao());
            executa.add(fila.get(marca_indice2));
            fila.remove(marca_indice2);
            menor_prioridade = 100;
        }
        mostrarTabela();
    }
    
    /**
     * Aplica o escalonador Round Robin.
     */
    public static void round_robin()
    {
        ArrayList<Processo> fila2 = new ArrayList<>();
        Random numero_aleatorio = new Random();
        int i;
        
         for ( i = 0 ; i < qtd_processos ; i++ )
        {
            if ( fila.isEmpty() )
            {
                fila.add(new Processo(geraPID(), numero_aleatorio.nextInt(4), (1+ numero_aleatorio.nextInt(11))));
                fila.get(i).setTempo_restante(fila.get(i).getTempo_execucao());
                if ( fila.get(i).getTempo_restante() < quantum)
                    fila.get(i).setTempo_execucao(fila.get(i).getTempo_restante());
                else
                    fila.get(i).setTempo_execucao(quantum);
                System.out.println("\nCriei   "+fila.get(i).getTempo_criacao()+"  "+ fila.get(i).getTempo_execucao()+"  "+fila.get(i).getTempo_restante());
            }
            else
            {
                fila.add(new Processo(geraPID(),(fila.get(i-1).getTempo_criacao()+(1+numero_aleatorio.nextInt(4))), (1+ numero_aleatorio.nextInt(11))));
                fila.get(i).setTempo_restante(fila.get(i).getTempo_execucao());
                if ( fila.get(i).getTempo_restante() < quantum)
                    fila.get(i).setTempo_execucao(fila.get(i).getTempo_restante());
                else 
                    fila.get(i).setTempo_execucao(quantum);
                System.out.println("\nCriei   "+fila.get(i).getTempo_criacao()+"  "+ fila.get(i).getTempo_execucao()+"  "+fila.get(i).getTempo_restante());
            }
        }
        
        fila.get(0).setTempo_fila(0);
        fila.get(0).setTempo_entrada_atend(fila.get(0).getTempo_criacao());
        fila.get(0).setTempo_saida(fila.get(0).getTempo_entrada_atend() + fila.get(0).getTempo_execucao());
        fila.get(0).diminui_tempo_restante(fila.get(0).getTempo_execucao());
        executa.add(fila.get(0));
        if ( fila.get(0).getTempo_restante() > 0 )
            fila2.add(fila.get(0));
        fila.remove(0);
        
        for ( i = 0 ; i < executa.size() ;  )
        {
            if ( ! fila.isEmpty() )
            {
                while (  ! fila.isEmpty() )
                {
                    if ( ! fila2.isEmpty() )
                    {
                        if ( fila.get(0).getTempo_criacao() <= fila2.get(0).getTempo_saida() )
                        {
                            fila.get(0).setTempo_fila(executa.get(i).getTempo_saida() - fila.get(0).getTempo_criacao());
                            fila.get(0).setTempo_entrada_atend(executa.get(i).getTempo_saida());
                            fila.get(0).setTempo_saida(fila.get(0).getTempo_entrada_atend() + fila.get(0).getTempo_execucao() );
                            fila.get(0).diminui_tempo_restante(fila.get(0).getTempo_execucao());
                            executa.add(fila.get(0));
                            i++;
                            if ( fila.get(0).getTempo_restante() > 0 )
                                fila2.add(fila.get(0));
                            fila.remove(0);
                        }
                        else
                        {
                            fila2.get(0).setTempo_criacao(fila2.get(0).getTempo_saida());
                            fila2.get(0).setTempo_fila(executa.get(i).getTempo_saida() - fila2.get(0).getTempo_criacao());
                            fila2.get(0).setTempo_entrada_atend(executa.get(i).getTempo_saida());
                            if ( fila.get(0).getTempo_restante() < quantum )
                                fila2.get(0).setTempo_execucao(fila2.get(0).getTempo_restante());
                            fila2.get(0).setTempo_saida(fila2.get(0).getTempo_entrada_atend() + fila2.get(0).getTempo_execucao());
                            fila2.get(0).diminui_tempo_restante(fila2.get(0).getTempo_execucao());
                            executa.add(fila2.get(0));
                            i++;
                            if ( fila2.get(0).getTempo_restante() > 0 )
                                fila2.add(fila2.get(0));
                            fila2.remove(0);
                        }
                    }
                    else
                    {
                        if ( fila.get(0).getTempo_criacao() <= executa.get(i).getTempo_saida() )
                        {
                            fila.get(0).setTempo_fila(executa.get(i).getTempo_saida() - fila.get(0).getTempo_criacao());
                            fila.get(0).setTempo_entrada_atend(executa.get(i).getTempo_saida());
                            fila.get(0).setTempo_saida(fila.get(0).getTempo_entrada_atend() + fila.get(0).getTempo_execucao() );
                            fila.get(0).diminui_tempo_restante(fila.get(0).getTempo_execucao());
                            executa.add(fila.get(0));
                            i++;
                            if ( fila.get(0).getTempo_restante() > 0 )
                                fila2.add(fila.get(0));
                            fila.remove(0);
                        }
                        else
                        {
                            fila.get(0).setTempo_fila(0);
                            fila.get(0).setTempo_entrada_atend(fila.get(0).getTempo_criacao());
                            if ( fila.get(0).getTempo_restante() < quantum )
                                fila.get(0).setTempo_execucao(fila.get(0).getTempo_restante());
                            fila.get(0).setTempo_saida(fila.get(0).getTempo_entrada_atend() + fila.get(0).getTempo_execucao());
                            fila.get(0).diminui_tempo_restante(fila.get(0).getTempo_execucao());
                            executa.add(fila.get(0));
                            i++;
                            if ( fila.get(0).getTempo_restante() > 0 )
                                fila2.add(fila.get(0));
                            fila.remove(0);
                        }
                    }
                } 
            }
            
            if ( ! fila2.isEmpty() )
            {
                while( ! fila2.isEmpty() )
                {
                    fila2.get(0).setTempo_criacao(fila2.get(0).getTempo_saida());
                    fila2.get(0).setTempo_fila(executa.get(i).getTempo_saida() - fila2.get(0).getTempo_criacao());
                    fila2.get(0).setTempo_entrada_atend(executa.get(i).getTempo_saida());
                    if ( fila2.get(0).getTempo_restante() < quantum )
                        fila2.get(0).setTempo_execucao(fila2.get(0).getTempo_restante());
                    fila2.get(0).setTempo_saida(fila2.get(0).getTempo_entrada_atend() + fila2.get(0).getTempo_execucao());
                    fila2.get(0).diminui_tempo_restante(fila2.get(0).getTempo_execucao());
                    executa.add(fila2.get(0));
                    i++;
                    if ( fila2.get(0).getTempo_restante() > 0 )
                        fila2.add(fila2.get(0));
                    fila2.remove(0);
                }
            }
            break;
        }
        mostrarTabela();
    }
    
    /**
     * Aplica o escalonador SJF (Shortest-Job-First).
     */
    public static void sjf()
    {
        Random numero_aleatorio = new Random();
        int aux, i, j, marca_indice = 0, marca_indice2 = 0, menor_job = 100;
        
        for ( i = 0 ; i < qtd_processos ; i++ )
        {
            if ( fila.isEmpty() )
            {
                fila.add(new Processo(geraPID(), numero_aleatorio.nextInt(4), (1+ numero_aleatorio.nextInt(11))));
            }
            else
            {
                fila.add(new Processo(geraPID(),(fila.get(i-1).getTempo_criacao()+(1+numero_aleatorio.nextInt(4))), (1+ numero_aleatorio.nextInt(11))));
            }
        }
        executa.add(fila.get(0));
        executa.get(0).setTempo_fila(0);
        executa.get(0).setTempo_entrada_atend(0);
        executa.get(0).setTempo_saida(fila.get(0).getTempo_entrada_atend() + fila.get(0).getTempo_execucao());
        fila.remove(0);   
        
        for ( i = 0 ; i < executa.size() ; i++ )
        {
            if ( ! fila.isEmpty() )
            {    
                for ( j = 0 ; j < fila.size() ; j++ )
                {
                    if ( fila.get(j).getTempo_criacao() <= executa.get(i).getTempo_saida() )
                    {
                        marca_indice = j;
                    }
                    else
                    {
                        j = fila.size();
                    }
                }
                for ( j = 0 ; j <= marca_indice ; j++ )
                {
                    if ( fila.get(j).getTempo_execucao() < menor_job )
                    {
                        menor_job = fila.get(j).getTempo_execucao();
                        marca_indice2 = j;
                    }
                }
                
            }
            else
            {
                break;
            }
            
            aux = executa.get(i).getTempo_saida() - fila.get(marca_indice2).getTempo_criacao();
            if ( aux < 0 )
            {
                aux = 0;
            }
            fila.get(marca_indice2).setTempo_fila(aux);
            if ( aux == 0 )
            {
                fila.get(marca_indice2).setTempo_entrada_atend(fila.get(marca_indice2).getTempo_criacao());
            }
            else
            {
                fila.get(marca_indice2).setTempo_entrada_atend(executa.get(i).getTempo_saida());
            }
            fila.get(marca_indice2).setTempo_saida(fila.get(marca_indice2).getTempo_entrada_atend() + fila.get(marca_indice2).getTempo_execucao());
            executa.add(fila.get(marca_indice2));
            fila.remove(marca_indice2);
            menor_job = 100;
        }
        mostrarTabela(); 
        
        
    }
    
    /**
     * Mostra a tabela (ArrayList executa) pronta.
     */
    public static void mostrarTabela()
    {
        int i;
        
        switch(escalonador)
        {
            case 1:
                System.out.println("\n\n\t|PID\t|TC\t|TF\t|TE\t|TA\t|TS\t");
                for ( i = 0 ; i < executa.size() ; i++ )
                {
                    System.out.println("\t|"+executa.get(i).getPID()+"\t|"+executa.get(i).getTempo_criacao()+"\t|"+executa.get(i).getTempo_fila()+"\t|"
                        +executa.get(i).getTempo_entrada_atend()+"\t|"+executa.get(i).getTempo_execucao()+"\t|"+executa.get(i).getTempo_saida());
                }
                break;
            case 2:
                System.out.println("\n\n\t|PID\t|TC\t|TF\t|TE\t|TA\t|TS\t|Pr\t");
                for ( i = 0 ; i < executa.size() ; i++ )
                {
                    System.out.println("\t|"+executa.get(i).getPID()+"\t|"+executa.get(i).getTempo_criacao()+"\t|"+executa.get(i).getTempo_fila()+"\t|"
                        +executa.get(i).getTempo_entrada_atend()+"\t|"+executa.get(i).getTempo_execucao()+"\t|"+executa.get(i).getTempo_saida()+"\t|"
                        +executa.get(i).getPrioridade());
                }
                break;
            case 3:
                System.out.println("\n\n\t|PID\t|TC\t|TF\t|TE\t|TA\t|TS\t|TRA\t");
                for ( i = 0 ; i < executa.size() ; i++ )
                {
                    System.out.println("\t|"+executa.get(i).getPID()+"\t|"+executa.get(i).getTempo_criacao()+"\t|"+executa.get(i).getTempo_fila()+"\t|"
                        +executa.get(i).getTempo_entrada_atend()+"\t|"+executa.get(i).getTempo_execucao()+"\t|"+executa.get(i).getTempo_saida()+"\t|"
                        +executa.get(i).getTempo_restante());
                }
                break;
            case 4:
                System.out.println("\n\n\t|PID\t|TC\t|TF\t|TE\t|TA\t|TS\t");
                for ( i = 0 ; i < executa.size() ; i++ )
                {
                    System.out.println("\t|"+executa.get(i).getPID()+"\t|"+executa.get(i).getTempo_criacao()+"\t|"+executa.get(i).getTempo_fila()+"\t|"
                        +executa.get(i).getTempo_entrada_atend()+"\t|"+executa.get(i).getTempo_execucao()+"\t|"+executa.get(i).getTempo_saida());
                }
                break;
            default:
                break;
        }
        
             
    }
      
}


