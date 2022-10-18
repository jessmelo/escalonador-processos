package escalonador;

import processo.BlocoControle;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ListaBloqueados {

    List<BlocoControle> bloqueados; // lista de bloqueados

    public ListaBloqueados(){
        this.bloqueados= new ArrayList<>();
    }

    public void adicionaBloco(BlocoControle bloco) {
        this.bloqueados.add(bloco);
    }

    public void removeBloco(BlocoControle bloco) {
        this.bloqueados.remove(bloco);
    }

    public BlocoControle getPrimeiroDaLista() {
        BlocoControle primeiro = bloqueados.get(0);
        return primeiro;
    }

    public void printaLista(){ // propositos de debugar
        System.out.println(bloqueados.size());
        Iterator it= bloqueados.iterator();

        while (it.hasNext()) {
            BlocoControle c = (BlocoControle) it.next();
            System.out.println("Credito: " + c.getCreditos());
        }
    }

    public List<BlocoControle> getBloqueados() {
        return bloqueados;
    }

    public void decrementaTempoEspera() {
        for (BlocoControle bloco: bloqueados) {
            bloco.decrementaTempoEspera();
        }
    }
}
