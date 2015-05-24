package ar.edu.utn.frba.genebreaker.ui;

import java.util.ArrayList;
import java.util.List;

import org.jenetics.Genotype;
import org.jenetics.IntegerChromosome;
import org.jenetics.IntegerGene;
import org.jenetics.Mutator;
import org.jenetics.RouletteWheelSelector;
import org.jenetics.SinglePointCrossover;
import org.jenetics.engine.Engine;
import org.jenetics.engine.EvolutionResult;
import org.jenetics.util.Factory;

import ar.edu.utn.frba.genebreaker.juego.CodebreakerJuego;
import ar.edu.utn.frba.genebreaker.juego.Jugada;
import ar.edu.utn.frba.genebreaker.stats.EstadisticasDeAptitud;

public class JugadorAGDeConsola implements Jugador {
	
	CodebreakerJuego juego;
	
	private final int N_Colores = 6;
	private final int N_Elecciones = 4;
	private final int N_Jugadas = 100;

	private List<Jugada> jugadas = new ArrayList<Jugada>();
	private double[][] puntajes = new double[N_Colores][N_Elecciones];
	
	private void puntuar(Jugada jugada, int n_jugada) {
		// Puntos por acertar colores en posicion correcta: todos los colores
		// elegidos suman puntos en la posicion donde esten.
		if (jugada.aciertos_en_pos > 0)
		{
			for (int i = 0; i < jugada.codigo.size(); i++) {
				puntajes[jugada.codigo.get(i)][i] += jugada.aciertos_en_pos / 2; 
			}
		}
		
		// Puntos por acertar colores en otra posicion: todos los colores elegidos
		// suman puntos en todas las posiciones.
		if (jugada.aciertos_no_en_pos > 0)
		{
			for (int i = 0; i < jugada.codigo.size(); i++) {
				for (int j = 0; j < N_Elecciones; j++) {
					puntajes[jugada.codigo.get(i)][j] += (jugada.aciertos_no_en_pos) / 2;
				} 
			}
		}
	}

	/**
	 * Funcion de aptitud. Individuos reciben puntos en base a coincidencias con
	 * jugadas anteriores.
	 * @param gt Genotipo que contiene solo un cromosoma con la jugada.
	 * @return Resultado de la funcion, a mayor valor, mejor candidato.
	 */
	private Double aptitud(Genotype<IntegerGene> gt) {
		Double puntaje = 0.0;
		
//		Alg C
		Jugada jugada_individuo = Jugada.desdeCromosoma(gt.getChromosome());
		
		// Penalizar jugadas ya hechas.
		for (Jugada jugada_anterior : jugadas)
			if (jugada_individuo.codigo.equals(jugada_anterior.codigo))
				return -1.0;
		
		// Puntuar la jugada del cromosoma basado en puntajes de colores/posiciones anteriores.
		for (int i = 0; i < jugada_individuo.codigo.size(); i++) {
			puntaje += puntajes[jugada_individuo.codigo.get(i)][i];
		}
		
//		Alg B
//		for (Jugada jugada_anterior : jugadas) {
//			if (jugada_individuo.codigo.equals(jugada_anterior.codigo)) {
//				puntaje = -1;
//				break;
//			}
//			
//			List<Integer> codigo_anterior = new ArrayList<Integer>(jugada_anterior.codigo);
//			
//			int puntaje_actual = 0;
//			for (IntegerGene gen : gt.getChromosome()) {
//				Integer color = gen.getAllele();
//				
//				int pos = codigo_anterior.indexOf(color);
//				if (pos > -1)
//				{
//					puntaje_actual++;
//					codigo_anterior.set(pos, null);
//				}
//			}
//			
//			puntaje_actual += puntaje_actual * (jugada_anterior.aciertos_en_pos * 2 + jugada_anterior.aciertos_no_en_pos);
//			puntaje_actual -= 1 * (juego.getN_elecciones() - jugada_anterior.aciertos_en_pos - jugada_anterior.aciertos_no_en_pos);
//			
//			if (puntaje_actual > 0)
//				puntaje += puntaje_actual;
//		}
		
//		Alg. A
//		for (Jugada jugada_anterior : jugadas) {
//			juego.compararJugada(jugada_individuo, jugada_anterior.codigo);
//			puntaje += Math.abs(jugada_individuo.aciertos_en_pos - jugada_anterior.aciertos_en_pos)
//					+ Math.abs(jugada_individuo.aciertos_no_en_pos - jugada_anterior.aciertos_no_en_pos)
//					+ 2 * juego.getN_elecciones() * (juego.getJugadas_hechas() -1);
//			
//			if (jugada_individuo.codigo.equals(jugada_anterior.codigo))
//			{
//				puntaje = -1;
//				break;
//			}
//		}
		
		return puntaje;
	}

	public static void main(String[] args) {
		new JugadorAGDeConsola().jugar();
	}
	
	@Override
	public void jugar()
	{
		juego = new CodebreakerJuego(N_Elecciones, N_Colores, N_Jugadas);

		System.out.println("Codigo secreto: " + juego.getCodigo());
		
		Factory<Genotype<IntegerGene>> gtf = Genotype.of(
				IntegerChromosome.of(0, juego.getN_colores() -1, juego.getN_elecciones()));
		
		EstadisticasDeAptitud<Double> stats;
		
		do {
			Engine<IntegerGene, Double> engine = Engine
					.builder(this::aptitud, gtf)
					.populationSize(50)
					.survivorsSelector(new RouletteWheelSelector<>())
					.alterers(
							new Mutator<>(0.5),
							new SinglePointCrossover<>(0.5)
							)
					.build();
			
			stats = new EstadisticasDeAptitud<Double>();
			
			Genotype<IntegerGene> result = engine
					.stream()
					.peek(stats)
					.limit(15)
					.collect(EvolutionResult.toBestGenotype());
			
			Jugada jugada = Jugada.desdeCromosoma(result.getChromosome());
			
			boolean gano = juego.jugar(jugada);
			
			System.out.println("Jugando: " + jugada.toString());
			
			if (gano) {
				System.out.println("Gano en " + juego.getJugadas_hechas());
				break;
			}
			else if (juego.terminado()) {
				System.out.println("Perdio =(");
				break;
			}
			
			jugadas.add(jugada);
			puntuar(jugada, juego.getJugadas_hechas());
		} while(true);
	}
}
