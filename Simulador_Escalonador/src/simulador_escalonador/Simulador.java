/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package simulador_escalonador;

import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

/**
 *
 * @author JUNIOR
 */
public class Simulador {

    private static final Gerenciador gerenciador = new Gerenciador();
    private static int qtd_processos;
    private static int quantum;
    private static int escalonador;
    private static boolean controla_thread;
    private static ArrayList<Processo> fila = new ArrayList<>();
    private static ArrayList<Processo> executa = new ArrayList<>();
    private static int geraPID = 0;
    
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
    
    public static void getEscalonador()
    {
        Scanner leitura = new Scanner(System.in);
        
        System.out.println("\t___________________________________");
        System.out.println("\t|Selecione o escalonador desejado: |");
        System.out.println("\t|1 - SJF (Shortest-Job_First)      |");
        System.out.println("\t|2 - Prioridade não-preemptiva     |");
        System.out.println("\t|3 - Round Robin                   |");
        System.out.println("\t|4 - LCFS (Last-Come-First-Server) |");
        System.out.println("\t|5 - Nº Servidores                 |");
        System.out.println("\t|Selecione o escalonador desejado: |");
        System.out.println("\t|__________________________________|");
        System.out.print("\tEscalonador: ");
        escalonador = leitura.nextInt();
        leitura.reset();
    }
    
    public static void getQtd_processos()
    {
        Scanner leitura = new Scanner(System.in);
        
        System.out.print("\n\tInforme a quantidade de processos a simular: ");
        qtd_processos = leitura.nextInt();
    }
    
    public static void getQuantum()
    {
        Scanner leitura = new Scanner(System.in);
        boolean quantum_valido = false;
        
        while(!quantum_valido)
        {
            quantum = leitura.nextInt();
            if ( ( quantum >= 2 ) && ( quantum <= 4 ) )
            {
                quantum_valido = true;
            }
            else
            {
                System.out.println("O quantum deve ser de 2 a 4, informe novamente: ");
            }
        }
        
    }
    
    public static int geraPID()
    {
        return geraPID++;
    }
    
    public static void simula(int escalonador)
    {
        
    }
    
    
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
    
    public static void round_robin()
    {
        Random numero_aleatorio = new Random();
        int aux, i;
        boolean troca;
        
        for ( i = 0 ; i < qtd_processos ; i++ )
        {
            fila.add(new Processo(geraPID(), numero_aleatorio.nextInt(26), numero_aleatorio.nextInt(16)));
            fila.get(i).setTempo_restante(fila.get(i).getTempo_execucao());
            fila.get(i).setTempo_execucao(quantum);
        }
        
        executa.add(fila.get(0));
        executa.get(0).setTempo_fila(0);
        executa.get(0).setTempo_entrada_atend(executa.get(0).getTempo_criacao());
        executa.get(0).setTempo_saida(executa.get(0).getTempo_entrada_atend() + executa.get(0).getTempo_execucao());
        executa.get(0).diminui_tempo_restante(executa.get(0).getTempo_execucao());
        if ( executa.get(0).getTempo_restante() <= 0 )
        {
            
        }
        
        //aqui
        
    }
    
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
    
    public static void atribuirValores()
    {
        int i;
        
        fila.get(0).setTempo_fila(0);
        fila.get(0).setTempo_entrada_atend(0);
        fila.get(0).setTempo_saida(fila.get(0).getTempo_entrada_atend() + fila.get(0).getTempo_execucao());
        for ( i = 1 ; i < fila.size() ; i++ )
        {
            if ( fila.get(i).getTempo_criacao() >= fila.get(i-1).getTempo_saida() )
            {
                fila.get(i).setTempo_fila(0);
                fila.get(i).setTempo_entrada_atend(fila.get(i).getTempo_criacao());
            }
            else
            {
                fila.get(i).setTempo_fila(fila.get(i-1).getTempo_saida() - fila.get(i).getTempo_criacao());
                fila.get(i).setTempo_entrada_atend(fila.get(i-1).getTempo_saida());
            }
            fila.get(i).setTempo_saida(fila.get(i).getTempo_entrada_atend() + fila.get(i).getTempo_execucao());
        }
    }
    
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
    
    public static void criarProcessos()
    {
        Random numero_aleatorio = new Random();
        int i, aux;
        boolean troca;
        
        for ( i = 0 ; i < qtd_processos ; i++ )
        {
            if ( fila.isEmpty() )
            {
                fila.add(new Processo(geraPID(), numero_aleatorio.nextInt(4), numero_aleatorio.nextInt(16)));
            }
            else
            {
                fila.add(new Processo(geraPID(),(fila.get(0).getTempo_criacao()+numero_aleatorio.nextInt(21)), numero_aleatorio.nextInt(16)));
            }
        }
        
        troca = true;
        while (troca) 
        {
            troca = false;
            for ( i = 1; i < fila.size()-1 ; i++) 
            {
                if ( fila.get(i).getTempo_execucao() > fila.get(i+1).getTempo_execucao() ) 
                {
                    aux = fila.get(i).getTempo_execucao();
                    fila.get(i).setTempo_execucao(fila.get(i+1).getTempo_execucao());
                    fila.get(i+1).setTempo_execucao(aux);
                    
                    aux = fila.get(i).getPID();
                    fila.get(i).setPID(fila.get(i+1).getPID());
                    fila.get(i+1).setPID(aux);
                    
                    troca = true;
                }   
            }    
        } 
    
    }
    
    
}


