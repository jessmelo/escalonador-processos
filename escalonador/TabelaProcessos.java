package escalonador;

import processo.BlocoControle;

import java.util.List;

public class TabelaProcessos {
    List<BlocoControle> tabela;

    public void adicionaBloco(BlocoControle bloco) {
        tabela.add(bloco);
    }
}
