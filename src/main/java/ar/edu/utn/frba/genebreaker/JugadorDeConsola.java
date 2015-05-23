package ar.edu.utn.frba.genebreaker;

import java.util.Scanner;

import ar.edu.utn.frba.genebreaker.ui.JugadorAGDeConsola;
import ar.edu.utn.frba.genebreaker.ui.JugadorHumanoDeConsola;

public class JugadorDeConsola {

	public static void main(String[] args) {
		@SuppressWarnings("resource")
		Scanner in = new Scanner(System.in);
		
		System.out.println("¿Quién va a jugar? (1: Humano, 2: AG) : ");
		Integer seleccion = in.nextInt();
		
		switch (seleccion) {
		case 1:
			new JugadorHumanoDeConsola().jugar();
			break;
			
		case 2:
			new JugadorAGDeConsola().jugar();
			break;

		default:
			break;
		}
	}

}
