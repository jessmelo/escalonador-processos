package escalonador;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
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

    public static void ordenaFilaDeProntos(){
        // Ordena a fila de pronto pela ordem de tamanho dos creditos
        int i;
        int tamanho = prontos.size();

        List<BlocoControle> aux = new ArrayList<>();

        Iterator it = tabela.getTabela().iterator();

        while(it.hasNext()){
            BlocoControle c =(BlocoControle) it.next();
            if (prontos.isEmpty()) prontos.add(c);
            if (!prontos.isEmpty()) {
                Iterator iter = prontos.iterator();
                //for (i = 0; i < prontos.size(); i++)
                while(iter.hasNext()){

                    BlocoControle a = (BlocoControle) iter.next();


                    if (a.getCreditos() < c.getCreditos()) {//tem que arrumar
                        aux.addAll(prontos.subList(prontos.indexOf(a), prontos.size()));
                        prontos.removeAll(aux);
                        prontos.add(c);
                        prontos.addAll(aux);
                    }

                }
                //if (tamanho == prontos.size() && i == prontos.size())
                  //  prontos.add(c);//fila não alterada, portanto ultimo da fila
            }
        }
    }

    public static void printaLista(){
        System.out.println(prontos.size());
        Iterator it= prontos.iterator();

        while(it.hasNext()){
            BlocoControle c =(BlocoControle) it.next();
            System.out.println("Credito:"+c.getCreditos());
        }
    }
    public static void executaProcessos() {

    }
    public static void exportaLog() {

    }

    public static void main(String[] args) throws IOException {
        lerProgramas();
        distribuiCreditos();
        setQuantum();
        ordenaFilaDeProntos();
        printaLista();
    }
}


