package processo;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class BlocoControle {
    String nomePrograma;
    List<String> memoriaRef = new ArrayList<>();
    Estado estadoProcesso;
    int contadorPrograma = 0;
    int prioridade;
    int creditos;
    int registradorX;
    int registradorY;

    public BlocoControle(String programaTxt) throws IOException {
        FileReader programa = new FileReader(programaTxt);
        BufferedReader reader = new BufferedReader(programa);
        String line = reader.readLine();
        nomePrograma  = line;
        line = null;
        while ((line = reader.readLine()) != null) {
            memoriaRef.add(line);
        }
    }

    @Override
    public boolean equals(Object o){
        if(this.nomePrograma==((BlocoControle)o).nomePrograma && this.prioridade == ((BlocoControle)o).prioridade) return true;
        return false;
    }

    public Estado getEstadoProcesso() {
        return estadoProcesso;
    }

    public void setEstadoProcesso(Estado estadoProcesso) {
        this.estadoProcesso = estadoProcesso;
    }

    public void setPrioridade(int prioridade) {
        this.prioridade = prioridade;
    }

    public int getPrioridade() {
        return prioridade;
    }

    public void setCreditos(int creditos) {
        this.creditos = creditos;
    }

    public int getCreditos() {
        return creditos;
    }

    public void setRegistradorX(int registradorX) {
        this.registradorX = registradorX;
    }

    public int getRegistradorX() {
        return registradorX;
    }

    public void setRegistradorY(int registradorY) {
        this.registradorY = registradorY;
    }

    public int getRegistradorY() {
        return registradorY;
    }

    public void incrementaContador() {
        this.contadorPrograma++;
    }

    public int getContadorPrograma() {
        return contadorPrograma;
    }

    public String getNomePrograma() {
        return nomePrograma;
    }

    public List<String> getMemoriaRef() {
        return memoriaRef;
    }

    public static class BlocoControleComparator implements Comparator<BlocoControle> {
        @Override
        public int compare(BlocoControle o1, BlocoControle o2) {
            return o1.getCreditos() - o2.getCreditos();
        }
    }
}

