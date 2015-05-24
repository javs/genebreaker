package ar.edu.utn.frba.genebreaker.juego;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import ar.edu.utn.frba.genebreaker.juego.Jugada;

public class CodebreakerJuego {
	private List<Integer> codigo;
	private int n_jugadas;
	private int n_elecciones;
	private int n_colores;
	private int jugadas_hechas;
	private boolean descubierto;

	public CodebreakerJuego(int n_elecciones, int n_colores, int n_jugadas) {
		reiniciar(n_elecciones, n_colores, n_jugadas);
	}

	public CodebreakerJuego() {
		reiniciar(4, 6, 10);
	}

	public void reiniciar(int n_elecciones, int n_colores, int n_jugadas) {
		this.n_elecciones = n_elecciones;
		this.n_colores = n_colores;
		this.n_jugadas = n_jugadas;
		jugadas_hechas = 0;
		descubierto = false;
		codigo = new ArrayList<Integer>(n_elecciones);

		Random rand = new Random();
		for (int i = 0; i < n_elecciones; i++) {
			codigo.add(rand.nextInt(n_colores));
		}
	}

	public boolean jugar(Jugada jugada) {
		if (terminado())
			return false;

		compararJugada(jugada, codigo);

		jugadas_hechas++;
		descubierto = jugada.aciertos_en_pos == n_elecciones;

		return descubierto;
	}

	public void compararJugada(Jugada jugada, List<Integer> codigo_ganador) {
		if (jugada.codigo.size() != n_elecciones)
			throw new RuntimeException(
					"Jugada con numero incorrecto de elecciones: "
							+ jugada.codigo.toString());
		
		jugada.aciertos_en_pos = 0;
		jugada.aciertos_no_en_pos = 0;
		
		// Copias.
		List<Integer> real = new ArrayList<Integer>(codigo_ganador);
		List<Integer> intento = new ArrayList<Integer>(jugada.codigo);

		// Puntos por colores en posicion.
		for (int i = 0; i < n_elecciones; i++) {
			Integer c_intento = intento.get(i);

			if (c_intento < 0 || n_colores <= c_intento)
				throw new RuntimeException("Jugada con color incorrecto: " + c_intento);

			if (c_intento.equals(real.get(i))) {
				jugada.aciertos_en_pos++;
				real.set(i, null);
				intento.set(i, null);
			}
		}
		
		// Puntos por colores en otra posicion.
		for (int i = 0; i < n_elecciones; i++) {
			Integer c_intento = jugada.codigo.get(i);

			int pos = real.indexOf(c_intento);
			
			if (pos > -1) {
				jugada.aciertos_no_en_pos++;
				real.set(pos, null);
			}
		}
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
