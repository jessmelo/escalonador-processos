package processo;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class BlocoControle {
    String nomePrograma;
    List<String> memoriaRef = new ArrayList<>();
    Estado estadoProcesso;
    int contadorPrograma;
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
            line = reader.readLine();
        }
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

    public String getNomePrograma() {
        return nomePrograma;
    }
}
