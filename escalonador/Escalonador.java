package escalonador;

import java.io.*;
import java.util.*;

import processo.BlocoControle;
import processo.Estado;

public class Escalonador {
    static int quantum;
    static TabelaProcessos tabela = new TabelaProcessos();
    ListaProntos prontos;
    ListaProntos bloqueados;
    static int tempoEspera = 2;
    static int instrucoesRodadas;
    static ArrayList<Integer> trocasProcessos = new ArrayList<>();
    static ArrayList<Integer> instrucoesQuantum = new ArrayList<>();

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
        while (prontos.getProntos().size() > 0 || bloqueados.getBloqueados().size() > 0) {

            if (prontos.getProntos().size() == 0 && bloqueados.getBloqueados().size() > 0) {
                bloqueados.decrementaTempoEspera();
                if (bloqueados.getPrimeiroDaLista().getTempoEspera() == 0) {
                    BlocoControle primeiroBloqueado = bloqueados.getPrimeiroDaLista();
                    bloqueados.removeBloco(primeiroBloqueado);
                    primeiroBloqueado.setEstadoProcesso(Estado.PRONTO);
                    prontos.adicionaBloco(primeiroBloqueado);
                }
            }

            if (prontos.getProntos().size() > 0 && prontos.getPrimeiroDaLista().getCreditos() > 0) {
                // Pega o primeiro processo pronto e executa, a lista de prontos é atualizada
                BlocoControle bloco = prontos.getPrimeiroDaLista();
                prontos.removeBloco(bloco);
                bloco.setCreditos(bloco.getCreditos() - 1);
                bloco.setEstadoProcesso(Estado.EXECUTANDO);
                System.out.println("Executando " + bloco.getNomePrograma());

                int instrucoes = 0;
                for (int i = 1; i <= quantum; i++) {
                    int contador = bloco.getContadorPrograma();
                    String instrucao = bloco.getMemoriaRef().get(contador);
                    // Se for registrador X, atualizar registrador do bloco e contagem de instrucoes
                    if (instrucao.startsWith("X=")) {
                        instrucoes++;
                        bloco.setRegistradorX(Integer.parseInt(bloco.getMemoriaRef().get(contador).substring(2)));
                        bloco.incrementaContador();
                    }
                    // Se for registrador Y, atualizar registrador do bloco e contagem de instrucoes
                    if (instrucao.startsWith("Y=")) {
                        instrucoes++;
                        bloco.setRegistradorY(Integer.parseInt(bloco.getMemoriaRef().get(contador).substring(2)));
                        bloco.incrementaContador();
                    }
                    // Se for instrucao de comando, incrementa instrucoes e continua
                    if (instrucao.equals("COM")) {
                        instrucoes++;
                        bloco.incrementaContador();
                    }
                    // Se for instrucao de E/S, interromper o processo
                    if (instrucao.equals("E/S")) {
                        instrucoes++;
                        instrucoesQuantum(instrucoes);
                        System.out.println("E/S iniciada em " + bloco.getNomePrograma());
                        System.out.println("Interrompendo " + bloco.getNomePrograma() + " após " + instrucoes + " instrucoes");
                        bloco.trocas++;
                        bloco.incrementaContador();
                        bloco.setEstadoProcesso(Estado.BLOQUEADO);
                        bloco.setTempoEspera(tempoEspera);
                        bloqueados.adicionaBloco(bloco);
                        break;
                    }
                    // Se o comando do processo for SAIDA, finaliza o processo
                    if (instrucao.equals("SAIDA")) {
                        instrucoes++;
                        bloco.trocas++;
                        instrucoesQuantum(instrucoes);
                        trocasProcessos(bloco.trocas);
                        System.out.println(bloco.getNomePrograma() + " terminado. X= " + bloco.getRegistradorX() + ". Y=" + bloco.getRegistradorY());
                        removeProcesso(bloco, prontos);
                        break;
                    }
                    // Se o numero do instrucoes for igual ao quantum, interromper processo e exibir no log
                    if (instrucoes == quantum) {
                        bloco.trocas++;
                        instrucoesQuantum(instrucoes);
                        System.out.println("Interrompendo " + bloco.getNomePrograma() + " após " + instrucoes + " instrucoes");
                        // Reposiciona o processo na fila de prontos apos a execucao
                        bloco.setEstadoProcesso(Estado.PRONTO);
                        prontos.adicionaBloco(bloco);
                        break;
                    }
                }

                if (bloqueados.getBloqueados().size() > 0) {
                    // System.out.println(bloqueados.getPrimeiroDaLista().getTempoEspera());
                    bloqueados.decrementaTempoEspera();
                    if (bloqueados.getPrimeiroDaLista().getTempoEspera() == 0) {
                        BlocoControle primeiroBloqueado = bloqueados.getPrimeiroDaLista();
                        bloqueados.removeBloco(primeiroBloqueado);
                        primeiroBloqueado.setEstadoProcesso(Estado.PRONTO);
                        prontos.adicionaBloco(primeiroBloqueado);
                    }
                }
            }

            // Se todos os creditos estiverem zerados, reordenar lista de prontos de acordo com a prioridade
            if (todosZero(tabela)) {
                distribuiCreditos();
            }
        }
    }

    private static boolean todosZero(TabelaProcessos tabela) {
        for (BlocoControle bloco: tabela.getTabela()) {
            if (bloco.getCreditos() != 0) {
                return false;
            }
        }
        return true;
    }

    private static void removeProcesso(BlocoControle bloco, ListaProntos prontos) {
        tabela.removeBloco(bloco);
        prontos.removeBloco(bloco);
    }

    public static void trocasProcessos(int trocas) {
        trocasProcessos.add(trocas);
    }
    public static void mediaTrocas() {
        Double sum = 0.0;
        if(!trocasProcessos.isEmpty()) {
            for (Integer trocas : trocasProcessos) {
                sum += trocas;
            }
            sum = sum.doubleValue() / trocasProcessos.size();
        }
        System.out.println("MEDIA DE TROCAS:  " + sum);
    }
    public static void instrucoesQuantum(int instrucoes) {
        instrucoesQuantum.add(instrucoes);
    }

    public static void mediaInstrucoesQuantum() {
        Double sum = 0.0;
        if(!instrucoesQuantum.isEmpty()) {
            for (Integer instrucoes : instrucoesQuantum) {
                sum += instrucoes;
            }
            sum = sum.doubleValue() / instrucoesQuantum.size();
        }
        System.out.println("MEDIA DE INSTRUCOES:  " + sum);
    }

    public static void printQuantum() {
        System.out.println("QUANTUM:  " + quantum);
    }
    public static void exportaLog() throws FileNotFoundException {
        PrintStream fileStream = new PrintStream("log" + String.format("%02d", quantum) + ".txt");
        System.setOut(fileStream);
    }

    public static void main(String[] args) throws IOException {
        lerProgramas();
        setQuantum();
        exportaLog();
        distribuiCreditos();
        ListaProntos prontos = new ListaProntos(tabela);
        ListaBloqueados bloqueados = new ListaBloqueados();
        executaProcessos(prontos, bloqueados);
        mediaTrocas();
        mediaInstrucoesQuantum();
        printQuantum();
    }
}


