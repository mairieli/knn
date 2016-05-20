package br.edu.utfpr.knn;

import br.edu.utfpr.arquivo.ManipuladorArquivo;
import br.edu.utfpr.bean.Instancia;
import java.util.List;

/**
 *
 * @author mairieli
 */
public class Main {

    public static void main(String[] args) {
        List<Instancia> instancias = ManipuladorArquivo.lerArquivo("treino-teste/teste.data");
        for (Instancia i : instancias) {
            System.out.println("Classe: " + i.getClasse());
            System.out.println("Caracteristicas: " + i.getCaracteristicas());
        }
    }

}
