package escalonador;

import processo.BlocoControle;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

public class ListaProntos{
    List<BlocoControle> prontos; // lista de prontos
    TabelaProcessos tabela; // tabela de processos

    public ListaProntos(TabelaProcessos tabela){
        this.tabela = tabela;
        this.prontos= new ArrayList<>();
        iniciaFilaDeProntos();
        // printaLista();
    }

    public void iniciaFilaDeProntos() {//metodo de ordenamento
        // Ordena a fila de pronto pelo tamanho dos creditos (do maior para o menor)
        prontos = tabela.getTabela();
        prontos.sort(Comparator.comparing(BlocoControle::getCreditos).reversed());
        for (BlocoControle bloco : prontos) {
            System.out.println("Carregando " + bloco.getNomePrograma());
        }
    }

    public void ordenaFilaDeProntos() {//metodo de ordenamento
        // Ordena a fila de pronto pelo tamanho dos creditos (do maior para o menor)
        prontos.sort(Comparator.comparing(BlocoControle::getCreditos).reversed());
    }

    public BlocoControle getPrimeiroDaLista() {
        BlocoControle primeiro = prontos.get(0);
        removeBloco(prontos.get(0));
        return primeiro;
    }

    public void adicionaBloco(BlocoControle bloco) {
        this.prontos.add(bloco);
        ordenaFilaDeProntos();
    }
    public void removeBloco(BlocoControle bloco) {
        this.prontos.remove(bloco);
    }

    public void printaLista(){ // propositos de debugar
        System.out.println(prontos.size());
        Iterator it= prontos.iterator();

        while (it.hasNext()) {
            BlocoControle c = (BlocoControle) it.next();
            System.out.println("Credito: " + c.getCreditos());
        }
    }

    public List<BlocoControle> getProntos() {
        return prontos;
    }

}
