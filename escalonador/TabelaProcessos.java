package escalonador;

import processo.BlocoControle;

import java.util.ArrayList;
import java.util.List;

public class TabelaProcessos {
    List<BlocoControle> tabela = new ArrayList<>();

    public void adicionaBloco(BlocoControle bloco) {
        tabela.add(bloco);
    }

    public List<BlocoControle> getTabela() {
        return tabela;
    }
}
