package br.edu.utfpr.arquivo;

import br.edu.utfpr.bean.Instancia;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author mairieli
 */
public class ManipuladorArquivo {

    public static List<Instancia> lerArquivo(String caminho) {
        List<Instancia> instancias = new LinkedList<>();
        try {
            File arquivo = new File(caminho);
            BufferedReader br = new BufferedReader(new FileReader(arquivo));
            while (br.ready()) {
                String linha = br.readLine();
                String[] tokens = linha.replaceAll(" ", "").split(",");
                int indiceClasse = tokens.length - 1;
                String classe = tokens[indiceClasse];
                List<String> caracteristicas = new LinkedList<>();
                caracteristicas.addAll(Arrays.asList(tokens));
                caracteristicas.remove(indiceClasse);
                List<Double> parsed = new LinkedList<>();
                for (int i = 0; i < caracteristicas.size(); i++) {
                    parsed.add(Double.valueOf(caracteristicas.get(i)));
                }
                instancias.add(new Instancia(parsed, classe));
            }
            br.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(ManipuladorArquivo.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ManipuladorArquivo.class.getName()).log(Level.SEVERE, null, ex);
        }
        return instancias;
    }
}
