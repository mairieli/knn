package br.edu.utfpr.knn;

import br.edu.utfpr.bean.Instancia;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class Knn {

    /**
     * Classifica um determinado conjunto de instancias.
     *
     * @param treino Conjunto de instancias para treino.
     * @param teste Conjunto de instancias para teste.
     * @param k Valor de k vizinhos.
     */
    public void classifica(List<Instancia> treino, List<Instancia> teste, Integer k) {
        this.normalizaZScore(treino, teste);
        System.out.println("____________________VALOR K: " + k + "______");

        List<Instancia> treino2 = this.selecionaPorcentagem(treino, 50);
        List<Instancia> treino3 = this.selecionaPorcentagem(treino, 25);

        System.out.println("100% Instancias de Treino - Matriz de Confusão: ");
        this.matrizConfusao(treino, teste, k);
        System.out.println("____________________________");
        System.out.println("50% Instancias de Treino- Matriz de Confusão: ");
        this.matrizConfusao(treino2, teste, k);
        System.out.println("____________________________");
        System.out.println("25% Instancias de Treino- Matriz de Confusão: ");
        this.matrizConfusao(treino3, teste, k);
        System.out.println("_________________________________________________________________________________________________________________________");
    }

    /**
     * Seleciona uma porcentagem aleatoria de um conjunto de instancias.
     *
     * @param instancias Conjunto de instancias.
     * @param porcentagem Porcentagem a ser selecionada.
     * @return Conjunto com a porcentagem de instancias escolhida.
     */
    private List<Instancia> selecionaPorcentagem(List<Instancia> instancias, Integer porcentagem) {
        int quantidadeInstancias = (instancias.size() / 100) * porcentagem;
        List<Instancia> selecionadas = new LinkedList<>();
        Random gerador = new Random();
        while (selecionadas.size() < quantidadeInstancias) {
            int aleatorio = gerador.nextInt(instancias.size());
            if (!selecionadas.contains(instancias.get(aleatorio))) {
                selecionadas.add(instancias.get(aleatorio));
            }
        }

        return selecionadas;
    }

    private void matrizConfusao(List<Instancia> treino, List<Instancia> teste, Integer k) {
        List<String> classes = this.classes(teste);
        Integer quantidadeClasses = classes.size();
        int[][] matriz = new int[quantidadeClasses][quantidadeClasses];

        // Para cada instância de teste:
        teste.stream().forEach((t) -> {
            //Calcula a distâncias euclidiana do conjunto de treino.
            List<Double> distancias = new LinkedList<>();
            treino.stream().forEach((a) -> {
                distancias.add(this.distanciaEuclidiana(a, t));
            });

            //Escolhe os k vizinhos mais proximos.
            List<Instancia> kVizinhos = new LinkedList<>();
            List<Instancia> vizinhos = new LinkedList<>();
            vizinhos.addAll(treino);
            for (int i = 0; i < k; i++) {
                Double menorDistancia = distancias.get(0);
                int indiceMenor = 0;

                for (int j = 0; j < vizinhos.size(); j++) {
                    if (distancias.get(j) < menorDistancia) {
                        menorDistancia = distancias.get(j);
                        indiceMenor = j;
                    }
                }

                kVizinhos.add(vizinhos.get(indiceMenor));

                vizinhos.remove(indiceMenor);
                distancias.remove(indiceMenor);
            }

            //Conta os votos de acordo com os k vizinhos.
            int[] votos = new int[quantidadeClasses];
            kVizinhos.stream().forEach((vizinho) -> {
                votos[classes.indexOf(vizinho.getClasse())]++;
            });

            //Verifica o maior voto.
            int classeMaiorVoto = 0;
            int maiorVoto = 0;
            for (int i = 0; i < quantidadeClasses; i++) {
                if (votos[i] > maiorVoto) {
                    classeMaiorVoto = i;
                    maiorVoto = votos[i];
                }
            }

            //Incrementa a escolha na matriz de confusão.
            matriz[classes.indexOf(t.getClasse())][classeMaiorVoto]++;
        });

        //Imprime a matriz de confusão e a taxa.
        //Taxa = soma(diagonal_principal) / soma(total).
        double soma = 0;
        double diagonal = 0;
        for (int i = 0; i < quantidadeClasses; i++) {
            for (int j = 0; j < quantidadeClasses; j++) {
                System.out.print(matriz[i][j] + " ");
                soma += matriz[i][j];
                if (i == j) {
                    diagonal += matriz[i][j];
                }
            }
            System.out.println();
        }
        Double taxa = (diagonal / soma);
        System.out.println("_______");
        System.out.printf("Taxa: %.4f\n", taxa);
    }

    private double distanciaEuclidiana(Instancia treino, Instancia teste) {
        Double distancia = 0.0;
        for (int i = 0; i < treino.getCaracteristicas().size(); i++) {
            distancia += Math.pow((teste.getCaracteristicas().get(i) - treino.getCaracteristicas().get(i)), 2);
        }
        return Math.sqrt(distancia);
    }

    /**
     * Captura as classes disponiveis em um conjunto de instancias.
     *
     * @param instancias
     * @return Lista com as classes.
     */
    private List<String> classes(List<Instancia> instancias) {
        List<String> classes = new LinkedList<>();
        instancias.stream().filter((instancia) -> (!classes.contains(instancia.getClasse()))).forEach((instancia) -> {
            classes.add(instancia.getClasse());
        });
        return classes;
    }

    private void normalizaZScore(List<Instancia> treino, List<Instancia> teste) {
        int quantidadeCaracteristicas = treino.get(0).getCaracteristicas().size();
        Double[] medias = new Double[quantidadeCaracteristicas];
        Double[] desviosPadrao = new Double[quantidadeCaracteristicas];
        int nInstancias = 0;

        for (int i = 0; i < medias.length; i++) {
            medias[i] = 0.0;
        }

        //Soma cada caracteristica de todas as instancias do conjunto de treino.
        for (Instancia t : treino) {
            for (int i = 0; i < medias.length; i++) {
                medias[i] += t.getCaracteristicas().get(i);
            }
            nInstancias++;
        }

        //Soma cada caracteristica de todas as instancias do conjunto de teste.
        for (Instancia t : teste) {
            for (int i = 0; i < medias.length; i++) {
                medias[i] += t.getCaracteristicas().get(i);
            }
            nInstancias++;
        }

        //Faz a média de cada caracteristica:
        for (int i = 0; i < medias.length; i++) {
            medias[i] = (medias[i] / nInstancias);
        }

        //Calcula o desvio padrão de cada caracteristica.
        for (int i = 0; i < desviosPadrao.length; i++) {
            Double desvioPadrao = 0.0;
            for (Instancia t : treino) {
                desvioPadrao += Math.pow(t.getCaracteristicas().get(i) - medias[i], 2);
            }
            for (Instancia t : teste) {
                desvioPadrao += Math.pow(t.getCaracteristicas().get(i) - medias[i], 2);
            }
            desviosPadrao[i] = Math.sqrt(desvioPadrao / (nInstancias - 1));
        }

        //Normaliza os padrões do Conjunto de Treino.
        treino.stream().forEach((t) -> {
            List<Double> caracterisicaNormalizada = new LinkedList<>();
            for (int i = 0; i < t.getCaracteristicas().size(); i++) {
                Double dadoNormalizado = (t.getCaracteristicas().get(i) - medias[i]) / desviosPadrao[i];
                caracterisicaNormalizada.add(dadoNormalizado);
            }
            t.getCaracteristicas().clear();
            t.getCaracteristicas().addAll(caracterisicaNormalizada);
        });

        //Normaliza os padrões do Conjunto de Teste.
        teste.stream().forEach((t) -> {
            List<Double> caracterisicaNormalizada = new LinkedList<>();
            for (int i = 0; i < t.getCaracteristicas().size(); i++) {
                Double dadoNormalizado = (t.getCaracteristicas().get(i) - medias[i]) / desviosPadrao[i];
                caracterisicaNormalizada.add(dadoNormalizado);
            }
            t.getCaracteristicas().clear();
            t.getCaracteristicas().addAll(caracterisicaNormalizada);
        });
    }
}
