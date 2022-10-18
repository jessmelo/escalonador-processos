package escalonador;

import processo.BlocoControle;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class TabelaProcessos {
    List<BlocoControle> tabela = new ArrayList<>();

    public void adicionaBloco(BlocoControle bloco) {
        tabela.add(bloco);
    }

    public void removeBloco(BlocoControle bloco) { tabela.remove(bloco); };
    public List<BlocoControle> getTabela() {
        return tabela;
    }

    public void printaLista(){ // propositos de debugar
        System.out.println(tabela.size());
        Iterator it= tabela.iterator();

        while (it.hasNext()) {
            BlocoControle c = (BlocoControle) it.next();
            System.out.println("Credito: " + c.getPrioridade());
        }
    }

}
