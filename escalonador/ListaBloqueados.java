package escalonador;

import processo.BlocoControle;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

public class ListaBloqueados {

    List<BlocoControle> bloqueados; // lista de prontos
    int tempoEspera;

    public ListaBloqueados(){
        this.bloqueados= new ArrayList<>();
    }

    public void ordenaFilaDeProntos() {//metodo de ordenamento
        // Ordena a fila de pronto pelo tamanho dos creditos (do maior para o menor)
        bloqueados.sort(Comparator.comparing(BlocoControle::getCreditos).reversed());
        for (BlocoControle bloco : bloqueados) {
            System.out.println("Carregando " + bloco.getNomePrograma());
        }
    }

    public void adicionaBloco(BlocoControle bloco) {
        this.bloqueados.add(bloco);
    }

    public void removeBloco(BlocoControle bloco) {
        this.bloqueados.remove(bloco);
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

}
