package escalonador;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import processo.BlocoControle;
import processo.Estado;

public class Escalonador {
    static int quantum;
    static TabelaProcessos tabela = new TabelaProcessos();
    static List<BlocoControle> prontos = new ArrayList<>();

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
                    System.out.println("Carregando " + bloco.getNomePrograma());
                    tabela.adicionaBloco(bloco);
                }
            }
        }
    }

    public static void executaProcessos() {
        for (BlocoControle bloco : tabela.getTabela()) {
            bloco.setCreditos(bloco.getPrioridade());
        }
    }
    public static int setQuantum(String quantumTxt) throws IOException {
        FileReader quantumFile = new FileReader(quantumTxt);
        BufferedReader reader = new BufferedReader(quantumFile);
        quantum = Integer.parseInt(reader.readLine());
        System.out.println(quantum);
        return quantum;
    }

    public static void ordenaFilaDeProntos(List prontos, BlocoControle c){
        int i;
        int tamanho = prontos.size();
        if(prontos.isEmpty()) prontos.add(c);
        else{
            List<BlocoControle> aux;
            for(i=0;i<prontos.size();i++){
                BlocoControle a = (BlocoControle) prontos.get(i);

                if(a.getPrioridade() < c.getPrioridade()){//tem que arrumar
                    aux = prontos.subList(i, prontos.size()-1);
                    prontos.removeAll(aux);
                    prontos.set(i,c);
                    prontos.addAll(aux);
                }

            }
            if(tamanho==prontos.size() && i==prontos.size())prontos.add(c);//fila não alterada, portanto ultimo da fila
        }
    }

    public static void writeLog() {

    }

    public static void main(String[] args) throws IOException {
        System.out.println("Hello");
        lerProgramas();
        executaProcessos();
    }
}


