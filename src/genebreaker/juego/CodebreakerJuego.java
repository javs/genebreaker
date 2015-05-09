package genebreaker.juego;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CodebreakerJuego {
	private List<Integer> codigo;
	private int n_jugadas;
	private int n_elecciones;
	private int n_colores;
	private int jugadas_hechas;
	private boolean descubierto;
	
	public CodebreakerJuego(int n_elecciones, int n_colores) {
		reiniciar(n_elecciones, n_colores);
	}
	
	public CodebreakerJuego() {
		reiniciar(4, 6);
	}

	public void reiniciar(int n_elecciones, int n_colores) {
		this.n_elecciones = n_elecciones;
		this.n_colores = n_colores;
		n_jugadas = 10;
		jugadas_hechas = 0;
		descubierto = false;
		codigo = new ArrayList<Integer>(n_elecciones);
		
		Random rand = new Random();
		for (int i = 0; i < n_elecciones; i++) {
			codigo.add(rand.nextInt(n_colores));
		}
	}
	
	public boolean jugar(Jugada jugada) {
		if (jugada.codigo.size() != n_elecciones)
			throw new RuntimeException("Jugada con numero incorrecto de elecciones.");
		
		if (terminado())
			return false;
		
		jugada.aciertos_en_pos = 0;
		jugada.aciertos_no_en_pos = 0;
		
		for (int i = 0; i < n_elecciones; i++) {
			Integer color = jugada.codigo.get(i);
			
			if (color < 0 || n_colores <= color)
				throw new RuntimeException("Jugada con color incorrecto");
			
			if (color.equals(codigo.get(i)))
				jugada.aciertos_en_pos++;
			else if (codigo.contains(color)) {
				jugada.aciertos_no_en_pos++;
			}
		}
		
		jugadas_hechas++;
		descubierto = jugada.aciertos_en_pos == n_elecciones;
		
		return descubierto;
	}
	
	public boolean terminado() {
		return descubierto || jugadas_hechas >= n_jugadas;
	}

	public List<Integer> getCodigo() {
		return codigo;
	}

	public int getN_jugadas() {
		return n_jugadas;
	}

	public int getN_elecciones() {
		return n_elecciones;
	}

	public int getN_colores() {
		return n_colores;
	}

	public int getJugadas_hechas() {
		return jugadas_hechas;
	}

	public boolean ganado() {
		return descubierto;
	}

	public int jugadas_restantes() {
		return n_jugadas - jugadas_hechas;
	}
	
}
