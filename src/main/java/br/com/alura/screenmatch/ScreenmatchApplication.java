package br.com.alura.screenmatch;

import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import br.com.alura.screenmatch.model.DadosEpisodio;
import br.com.alura.screenmatch.model.DadosSerie;
import br.com.alura.screenmatch.model.DadosTemporada;
import br.com.alura.screenmatch.service.ConsumoApi;
import br.com.alura.screenmatch.service.ConverteDados;

@SpringBootApplication
public class ScreenmatchApplication implements CommandLineRunner{

	public static void main(String[] args) {
		SpringApplication.run(ScreenmatchApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		var consumoApi = new ConsumoApi();
		var json = consumoApi.obterDados("http://www.omdbapi.com/?t=gilmore+girls&season=1&3896198&apikey=ce4859c8");
		// System.out.println(json);

		ConverteDados conversor = new ConverteDados();

		DadosSerie dados = conversor.obterDados(json, DadosSerie.class);
		System.out.println(dados);

		List<DadosTemporada> temporadas = new ArrayList<>(); 

		for(int i = 1; i<=dados.totalTemporadas(); i++){
			json = consumoApi.obterDados("http://www.omdbapi.com/?t=gilmore+girls&season=" + i + "&3896198&apikey=ce4859c8");

			DadosTemporada dadosTemporada = conversor.obterDados(json, DadosTemporada.class);


			temporadas.add(dadosTemporada);;
		}

		temporadas.forEach(System.out::println) ;
	}

}
