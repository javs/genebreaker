package genebreaker.juego;

import java.util.ArrayList;
import java.util.List;

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
}
