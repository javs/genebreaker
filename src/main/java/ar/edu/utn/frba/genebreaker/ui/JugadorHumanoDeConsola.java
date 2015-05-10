package ar.edu.utn.frba.genebreaker.ui;

import java.util.Scanner;

import ar.edu.utn.frba.genebreaker.juego.CodebreakerJuego;
import ar.edu.utn.frba.genebreaker.juego.Jugada;

public class JugadorHumanoDeConsola {

	public static void main(String[] args) {
		CodebreakerJuego juego = new CodebreakerJuego();
		
		System.out.println(juego.getCodigo());
		
		@SuppressWarnings("resource")
		Scanner in = new Scanner(System.in);
		
		for (int i = 0; i < juego.getN_jugadas(); i++) {
			Jugada jugada = new Jugada();
			
			for (int j = 0; j < juego.getN_elecciones(); j++) {
				System.out.print("Elegir un color: ");
				Integer color = in.nextInt();
				jugada.codigo.add(color);
			}
			
			boolean gano = juego.jugar(jugada);
			
			System.out.println("\n" + jugada.toString());
			
			if (gano) {
				System.out.println("Ganaste!");
				return;
			}
			else if (juego.terminado()) {
				System.out.println("Perdiste =(");
				return;
			}
			else
				System.out.println("No acertaste. Jugadas restantes: " + juego.jugadas_restantes());
		}
	}

}
