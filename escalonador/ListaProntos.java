package escalonador;

import processo.BlocoControle;
import escalonador.TabelaProcessos;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

public class ListaProntos{

    public ListaProntos(TabelaProcessos tabela){
        this.tabela = tabela;
        this.prontos= new ArrayList<>();
    }

    List<BlocoControle> prontos; //lista de prontos
    TabelaProcessos tabela; //tabela de processos

    public void ordenaFilaDeProntos() {//metodo de ordenamento
        // Ordena a fila de pronto pelo tamanho dos creditos (do maior para o menor)
        prontos = tabela.getTabela();
        prontos.sort(Comparator.comparing(BlocoControle::getCreditos).reversed());
        for (BlocoControle bloco : prontos) {
            System.out.println("Carregando " + bloco.getNomePrograma());
        }
    }
    public void printaLista(){//propositos de debugar
        System.out.println(prontos.size());
        Iterator it= prontos.iterator();

        while(it.hasNext()){
            BlocoControle c =(BlocoControle) it.next();
            System.out.println("Credito: "+c.getCreditos());
        }
    }

    public List<BlocoControle> getProntos() {
        return prontos;
    }

}
