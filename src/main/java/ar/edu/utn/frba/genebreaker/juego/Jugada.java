package ar.edu.utn.frba.genebreaker.juego;

import java.util.ArrayList;
import java.util.List;

import org.jenetics.Chromosome;
import org.jenetics.Gene;

public class Jugada {
	public List<Integer> codigo = new ArrayList<Integer>();
	public int aciertos_en_pos = 0;
	public int aciertos_no_en_pos = 0;
	
	@Override
	public String toString() {
		StringBuilder s = new StringBuilder();
		
		s.append("Codigo: ");
		s.append(codigo.toString());
		s.append("\n");
		s.append("Aciertos en posicion correcta: ");
		s.append(aciertos_en_pos);
		s.append("\n");
		s.append("Aciertos en posicion incorrecta: ");
		s.append(aciertos_no_en_pos);
		s.append("\n");
		
		return s.toString();
	}

	/**
	 * Convierte un cromosoma a jugada.
	 * @param chromosome
	 * @return
	 */
	public static Jugada desdeCromosoma(Chromosome<?> chromosome) {
		Jugada jugada = new Jugada();
		
		for (Gene<?, ?> gen : chromosome) {
			Integer color = (Integer) gen.getAllele();
			jugada.codigo.add(color);
		}
		
		return jugada;
	}
}
