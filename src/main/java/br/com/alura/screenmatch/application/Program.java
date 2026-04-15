package br.com.alura.screenmatch.application;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

import br.com.alura.screenmatch.model.DadosEpisodio;
import br.com.alura.screenmatch.model.DadosSerie;
import br.com.alura.screenmatch.model.DadosTemporada;
import br.com.alura.screenmatch.model.Episodio;
import br.com.alura.screenmatch.service.ConsumoApi;
import br.com.alura.screenmatch.service.ConverteDados;

public class Program {

    private Scanner sc  = new Scanner(System.in);
    private ConverteDados conversor = new ConverteDados();
    private ConsumoApi consumo = new ConsumoApi();
    
    private final String ENDERECO = "http://www.omdbapi.com/?t=";
    private final String API_KEY = "&apikey=ce4859c8";

    public void exibeMenu(){
        System.out.println("Digite o nome da série para a busca:\n>>");
        var nomeSerie = sc.nextLine();
    
        var json = consumo.obterDados(ENDERECO + nomeSerie.replaceAll(" ", "+")+ API_KEY);
        
		DadosSerie dados = conversor.obterDados(json, DadosSerie.class);
        System.out.println(dados);
    
        //Armazenar dados das temporadas:
        List<DadosTemporada> temporadas = new ArrayList<>(); 

        for(int i = 1; i<=dados.totalTemporadas(); i++){
		json = consumo.obterDados(ENDERECO + nomeSerie.replaceAll(" ", "+")+ "&season=" + i+ API_KEY);
		DadosTemporada dadosTemporada = conversor.obterDados(json, DadosTemporada.class);

			temporadas.add(dadosTemporada);
		}

        //Recurso forEach
        temporadas.forEach(t -> t.dadosEpisodio().forEach(e ->System.out.println(e.titulo())));
        

        //Caso fosse fazer comum for dentro do outro
        //Mostrar titulos dos episódios das temporadas
        // for(int i = 0; i<dados.totalTemporadas(); i++){
        //      List<DadosEpisodio> episodiosTemporada = temporadas.get(i).dadosEpisodio();
        //      for (int j = 0; j < episodiosTemporada.size(); j++) {
        //         System.out.println(episodiosTemporada.get(j).titulo());
        //      }
        // }

        List<DadosEpisodio> dadosEpisodios = temporadas.stream()
                                        .flatMap(t -> t.dadosEpisodio().stream())
                                        .collect(Collectors.toList());

        dadosEpisodios.stream().filter(e -> !e.avaliacao().equalsIgnoreCase("N/A")).sorted(Comparator.comparing(DadosEpisodio::avaliacao).reversed())
                          .limit(5)
                          .forEach(System.out::println);
        
        List<Episodio> episodios = temporadas.stream().flatMap(t -> t.dadosEpisodio().stream().map(d -> new Episodio(t.temporada(), d))).collect(Collectors.toList());

        episodios.forEach(System.out::println);
        System.out.print("A partir de que ano você deseja assistir:\n>> ");
        var ano = sc.nextInt();
        sc.nextLine();

        LocalDate dataBusca = LocalDate.of(ano, 1, 1);
        DateTimeFormatter df = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        episodios.stream().filter(e -> e.getDataDeLancamento() !=null && e.getDataDeLancamento().isAfter(dataBusca))
        .forEach(e -> System.out.println("Temporada: " + e.getTemporada() + 
                                            "\nEpisódio: " + e.getNumeroEpisodio() + 
                                                "\nData de lançamento: " + df.format(e.getDataDeLancamento())));
    }
}
