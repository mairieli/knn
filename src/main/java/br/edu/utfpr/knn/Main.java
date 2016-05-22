package br.edu.utfpr.knn;

import br.edu.utfpr.arquivo.ManipuladorArquivo;
import br.edu.utfpr.bean.Instancia;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        List<Instancia> treino = ManipuladorArquivo.lerArquivo("treino-teste/treino.data");
        List<Instancia> teste = ManipuladorArquivo.lerArquivo("treino-teste/teste.data");

        Knn knn = new Knn();
        knn.classifica(treino, teste, 1);
        knn.classifica(treino, teste, 3);
        knn.classifica(treino, teste, 5);
        knn.classifica(treino, teste, 7);
        knn.classifica(treino, teste, 9);
    }

}
