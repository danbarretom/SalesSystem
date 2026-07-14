package Gerenciadores;
import Interfaces.Armazenavel;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public abstract class GerenciadorBase<T> implements Armazenavel {
    protected ArrayList<T> lista;
    protected String arquivo;

    public GerenciadorBase(String arquivo) {
        this.arquivo = arquivo;
        this.lista = new ArrayList<>();
        carregarDados();
    }

    @Override
    public void carregarDados() {
        File file = new File(arquivo);
        if (!file.exists()) return;

        // O Java agora fecha o Scanner automaticamente ao final do bloco
        try (Scanner leitor = new Scanner(file)) {
            while (leitor.hasNextLine()) {
                String linha = leitor.nextLine();
                if (linha.trim().isEmpty()) continue;
                lista.add(criarObjetoDaLinha(linha));
            }
        } catch (IOException e) {
            System.out.println("Erro ao ler o arquivo " + arquivo + ": " + e.getMessage());
        }
    }

    @Override
    public void salvarDados() {
        // O Java agora fecha o FileWriter automaticamente ao final do bloco
        try (FileWriter escritor = new FileWriter(arquivo)) {
            for (T obj : lista) {
                escritor.write(gerarLinhaDoObjeto(obj) + "\n");
            }
        } catch (IOException e) {
            System.out.println("Erro ao salvar o arquivo " + arquivo + ": " + e.getMessage());
        }
    }

    protected abstract T criarObjetoDaLinha(String linha);
    protected abstract String gerarLinhaDoObjeto(T obj);
}