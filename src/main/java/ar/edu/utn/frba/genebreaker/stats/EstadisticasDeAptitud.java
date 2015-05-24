package ar.edu.utn.frba.genebreaker.stats;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.function.Consumer;

import org.jenetics.engine.EvolutionResult;

import ar.edu.utn.frba.genebreaker.juego.Jugada;

/**
 * Estadisticas orientadas a la función de aptitud.
 *
 * @param <C>
 */
public class EstadisticasDeAptitud<C extends Comparable<? super C>>
	implements Consumer<EvolutionResult<?, C>> {
	
	List<ResultadoDeIteracion> stats = new ArrayList<ResultadoDeIteracion>();

	@Override
	public void accept(EvolutionResult<?, C> result) {
		Jugada mejor_jugada = Jugada.desdeCromosoma( 
				result.getBestPhenotype().getGenotype().getChromosome());
		
		ResultadoDeIteracion iteracion = new ResultadoDeIteracion();
		
		iteracion.aptitud = (Double) result.getBestPhenotype().getFitness();
		iteracion.codigo = mejor_jugada.codigo;
		
		stats.add(iteracion);
	}
	
	public void escribirCSV() {
		PrintWriter pw;
		
		try {
			pw = new PrintWriter("stats.csv", "UTF-8");
		} catch (FileNotFoundException | UnsupportedEncodingException e) {
			e.printStackTrace();
			return;
		}
		
		pw.println("Iteración,Aptitud,Código");
		
		for (final ListIterator<ResultadoDeIteracion> it = stats.listIterator(); it.hasNext();) {
		    final ResultadoDeIteracion r = it.next();
		    pw.println(it.previousIndex() + "," + r.aptitud + ",\"" + r.codigo + "\"");
		}
		
		pw.close();
	}
}
