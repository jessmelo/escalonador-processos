package escalonador;


import java.io.*;
import java.util.List;

import processo.BlocoControle;
import processo.Estado;

public class Escalonador {
    static int quantum;
    static int prioridade;

    public static void lerProgramas() throws IOException {
        File folder = new File("programas");
        File[] listOfFiles = folder.listFiles();
        TabelaProcessos tabela = new TabelaProcessos();
        for (File file : listOfFiles) {
            if (file.isFile()) {
                BlocoControle bloco = new BlocoControle(file.getName());
                bloco.setEstadoProcesso(Estado.PRONTO);
                System.out.println("Carregando " + bloco.getNomePrograma());
                tabela.adicionaBloco(bloco);
            }
        }
    }
    public static int setPrioridade(String prioridadeTxt) throws IOException {
        FileReader prioridadesFile = new FileReader(prioridadeTxt);
        BufferedReader reader = new BufferedReader(prioridadesFile);
        String line = reader.readLine();
        while (line != null) {
            prioridade = Integer.parseInt(line);
            System.out.println(prioridade);
            line = reader.readLine();
        }
        return prioridade;
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
        setQuantum("programas/quantum.txt");
        setPrioridade("programas/prioridades.txt");
    }
}


