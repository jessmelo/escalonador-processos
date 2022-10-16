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





    public static void executaProcessos(ListaProntos prontos) {
        // Método que executa os processos
        for (BlocoControle bloco: prontos.getProntos()) {
            System.out.println("Executando " + bloco.getNomePrograma());
            // Decrementa o número de créditos quando comeca a execucao
            bloco.setCreditos(bloco.getCreditos()-1);
            int instrucoes = 0;
            for (int i = 0; i < bloco.getMemoriaRef().size(); i++) {
                if (instrucoes == quantum){
                    System.out.println("Interrompendo " + bloco.getNomePrograma() + " após " + i + " instrucoes");
                    // Reposiciona a fila de prontos apos a execucao
                    prontos.ordenaFilaDeProntos();
                    break;
                }
                System.out.println("instrucao");
                if (bloco.getMemoriaRef().get(i).equals("E/S")){
                    System.out.println("E/S iniciada em " + bloco.getNomePrograma());
                    System.out.println("Interrompendo " + bloco.getNomePrograma() + " após " + i + " instrucoes");
                    break;
                }
                instrucoes++;
            }
        }
    }
    public static void exportaLog() {

    }

    public static void main(String[] args) throws IOException {
        lerProgramas();
        ListaProntos prontos = new ListaProntos(tabela);
        distribuiCreditos();
        setQuantum();
        executaProcessos(prontos);
    }
}


