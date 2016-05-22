package br.edu.utfpr.bean;

import java.util.List;

/**
 *
 * @author mairieli
 */
public class Instancia {

    String classe;
    List<Double> caracteristicas;

    public Instancia(List<Double> caracteristicas, String classe) {
        this.caracteristicas = caracteristicas;
        this.classe = classe;
    }

    public List<Double> getCaracteristicas() {
        return caracteristicas;
    }

    public void setCaracteristicas(List<Double> caracteristicas) {
        this.caracteristicas = caracteristicas;
    }

    public String getClasse() {
        return classe;
    }

    public void setClasse(String classe) {
        this.classe = classe;
    }

}
