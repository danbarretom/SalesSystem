package Gerenciadores;
import BasicClasses.*;
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
        try {
            File file = new File(arquivo);
            if (!file.exists()) return;

            Scanner leitor = new Scanner(file);
            while (leitor.hasNextLine()) {
                String linha = leitor.nextLine();
                if (linha.trim().isEmpty()) continue;
                lista.add(criarObjetoDaLinha(linha));
            }
            leitor.close();
        } catch (IOException e) {
            System.out.println("Erro ao ler o arquivo " + arquivo + ": " + e.getMessage());
        }
    }

    @Override
    public void salvarDados() {
        try {
            FileWriter escritor = new FileWriter(arquivo);
            for (T obj : lista) {
                escritor.write(gerarLinhaDoObjeto(obj) + "\n");
            }
            escritor.close();
        } catch (IOException e) {
            System.out.println("Erro ao salvar o arquivo " + arquivo + ": " + e.getMessage());
        }
    }

    protected abstract T criarObjetoDaLinha(String linha);
    protected abstract String gerarLinhaDoObjeto(T obj);
}