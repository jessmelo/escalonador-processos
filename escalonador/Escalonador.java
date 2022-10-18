package escalonador;

import java.io.*;
import java.util.*;

import processo.BlocoControle;
import processo.Estado;
import escalonador.ListaProntos;

public class Escalonador {
    static int quantum;
    static TabelaProcessos tabela = new TabelaProcessos();
    ListaProntos prontos;
    ListaProntos bloqueados;

    public static void lerProgramas() throws IOException {
        // Carrega todos os arquivos programas .txt da pasta "programas" em ordem alfabetica
        File folder = new File("programas");
        File[] listOfFiles = folder.listFiles();
        Arrays.sort(listOfFiles);

        // Carrega arquivo de prioridades
        FileReader prioridadesFile = new FileReader("programas/prioridades.txt");
        BufferedReader prioridades = new BufferedReader(prioridadesFile);

        // Cria bloco de controle para cada programa e adiciona na tabela de processos
        for (File file : listOfFiles) {
            if (file.isFile()) {
                if (!file.getName().equals("prioridades.txt") && !file.getName().equals("quantum.txt")) {
                    BlocoControle bloco = new BlocoControle("programas/" + file.getName());
                    bloco.setEstadoProcesso(Estado.PRONTO);
                    bloco.setPrioridade(Integer.parseInt(prioridades.readLine()));
                    tabela.adicionaBloco(bloco);
                }
            }
        }
    }

    public static void distribuiCreditos() {
        // Distribuindo os creditos iniciais de acordo com a prioridade
        for (BlocoControle bloco : tabela.getTabela()) {
            bloco.setCreditos(bloco.getPrioridade());
        }
    }

    public static void setQuantum() throws IOException {
        // Configura quantum dos processos
        FileReader quantumFile = new FileReader("programas/quantum.txt");
        BufferedReader reader = new BufferedReader(quantumFile);
        quantum = Integer.parseInt(reader.readLine());
    }

    public static void executaProcessos(ListaProntos prontos, ListaBloqueados bloqueados) {
        // Método que executa os processos prontos
        while (prontos.getProntos().size() > 0) {
            // Pega o primeiro processo pronto e executa, a lista de prontos é atualizada
            BlocoControle bloco = prontos.getPrimeiroDaLista();
            bloco.setCreditos(bloco.getCreditos() - 1);
            bloco.setEstadoProcesso(Estado.EXECUTANDO);
            System.out.println("Executando " + bloco.getNomePrograma());

            int instrucoes = 0;
            for (int i = 1; i <= quantum; i++) {
                int contador = bloco.getContadorPrograma();
                // Se for registrador X, atualizar registrador do bloco e contagem de instrucoes
                if (bloco.getMemoriaRef().get(contador).startsWith("X=")) {
                    instrucoes++;
                    bloco.setRegistradorX(Integer.parseInt(bloco.getMemoriaRef().get(contador).substring(2)));
                    bloco.incrementaContador();
                }
                // Se for registrador Y, atualizar registrador do bloco e contagem de instrucoes
                if (bloco.getMemoriaRef().get(contador).startsWith("Y=")) {
                    instrucoes++;
                    bloco.setRegistradorY(Integer.parseInt(bloco.getMemoriaRef().get(contador).substring(2)));
                    bloco.incrementaContador();
                }
                // Se for instrucao de comando, incrementa instrucoes e continua
                if (bloco.getMemoriaRef().get(contador).equals("COM")) {
                    instrucoes++;
                    bloco.incrementaContador();
                }
                // Se for instrucao de E/S, interromper o processo
                if (bloco.getMemoriaRef().get(contador).equals("E/S")) {
                    instrucoes++;
                    System.out.println("E/S iniciada em " + bloco.getNomePrograma());
                    System.out.println("Interrompendo " + bloco.getNomePrograma() + " após " + instrucoes + " instrucoes");
                    bloco.setEstadoProcesso(Estado.BLOQUEADO);
                    bloqueados.adicionaBloco(bloco);
                    bloco.incrementaContador();
                    break;
                }
                // Se o comando do processo for SAIDA, finaliza o processo
                if (bloco.getMemoriaRef().get(contador).equals("SAIDA")) {
                    instrucoes++;
                    System.out.println(bloco.getNomePrograma() + " terminado. X= " + bloco.getRegistradorX() + ". Y=" + bloco.getRegistradorY());
                    removeProcesso(bloco, prontos);
                    bloco.incrementaContador();
                    break;
                }
                // Se o numero do instrucoes for igual ao quantum, interromper processo e exibir no log
                if (instrucoes == quantum) {
                    System.out.println("Interrompendo " + bloco.getNomePrograma() + " após " + instrucoes + " instrucoes");
                    // Reposiciona o processo na fila de prontos apos a execucao
                    bloco.setEstadoProcesso(Estado.PRONTO);
                    prontos.adicionaBloco(bloco);
                    break;
                }
            }
        }
    }

    private static void removeProcesso(BlocoControle bloco, ListaProntos prontos) {
        tabela.removeBloco(bloco);
        prontos.removeBloco(bloco);
    }

    private static void finalizaProcesso(BlocoControle bloco) {
    }

    public static void exportaLog() {

    }

    public static void printaBloco() {
        BlocoControle bloco = tabela.getTabela().get(1);
        System.out.println(bloco.getNomePrograma());
        for (String i: bloco.getMemoriaRef()) {
            System.out.println(i);
        }
    }

    public static void main(String[] args) throws IOException {
        lerProgramas();
        distribuiCreditos();
        ListaProntos prontos = new ListaProntos(tabela);
        ListaBloqueados bloqueados = new ListaBloqueados();
        setQuantum();
        executaProcessos(prontos, bloqueados);
        //printaBloco();
    }
}


