package br.com.alura.screenmatch.application;

import java.util.Scanner;

public class Principal {
    private Scanner sc  = new Scanner(System.in);
    private final String ENDERECO = "http://www.omdbapi.com/?t=";
    private final String API_KEY = "&apikey=ce4859c8";
    public void exibeMenu(){
        System.out.println("Digite o nome da série para a busca:\n>>");
        var nomeSerie = sc.nextLine();


    }
}
