package ar.edu.utn.frba.genebreaker.ui;

import java.util.ArrayList;
import java.util.List;

import org.jenetics.Genotype;
import org.jenetics.IntegerChromosome;
import org.jenetics.IntegerGene;
import org.jenetics.engine.Engine;
import org.jenetics.engine.EvolutionResult;
import org.jenetics.util.Factory;

import ar.edu.utn.frba.genebreaker.juego.CodebreakerJuego;
import ar.edu.utn.frba.genebreaker.juego.Jugada;

public class JugadorAGDeConsola {
	
	CodebreakerJuego juego;

	private List<Jugada> jugadas = new ArrayList<Jugada>();

	private Integer aptitud(Genotype<IntegerGene> gt) {
		Integer puntaje = 0;
		
		Jugada jugada_individuo = new Jugada();
		
		for (IntegerGene gen : gt.getChromosome()) {
			Integer color = gen.getAllele();
			
			jugada_individuo.codigo.add(color);
		}
		
		for (Jugada jugada_anterior : jugadas) {
			juego.compararJugada(jugada_individuo, jugada_anterior.codigo);
			puntaje += jugada_individuo.aciertos_en_pos + jugada_individuo.aciertos_no_en_pos
					- jugada_anterior.aciertos_en_pos - jugada_anterior.aciertos_no_en_pos;
		}
		
		return puntaje;
	}

	public static void main(String[] args) {
		JugadorAGDeConsola jugador = new JugadorAGDeConsola();
		jugador.jugar();
	}
	
	public void jugar()
	{
		juego = new CodebreakerJuego();

		System.out.println("Codigo secreto: " + juego.getCodigo());
		
		Factory<Genotype<IntegerGene>> gtf = Genotype.of(
				IntegerChromosome.of(0, juego.getN_colores() -1, juego.getN_elecciones()));

		do {
			Jugada jugada = new Jugada();

			Engine<IntegerGene, Integer> engine = Engine
					.builder(this::aptitud, gtf)
					.populationSize(100)
					.build();

			Genotype<IntegerGene> result = engine
					.stream()
					.limit(50)
					.collect(EvolutionResult.toBestGenotype());
			
			for (int i = 0; i < juego.getN_elecciones(); i++) {
				jugada.codigo.add(result.getChromosome().getGene(i).getAllele());
			}
			
			boolean gano = juego.jugar(jugada);
			
			System.out.println("Jugando: " + jugada.toString());
			
			if (gano) {
				System.out.println("Gano!");
				return;
			}
			else if (juego.terminado()) {
				System.out.println("Perdio =(");
				return;
			}
			
			jugadas.add(jugada);
		} while(true);
	}
}
