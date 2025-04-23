package proyectofinal;

import java.util.Scanner;

public class JuegoRompecabezas {
    public static void main(String[] args) {
        Imagen imagen = new Imagen("imagen.jpg");
        Puzzle juego = new Puzzle(imagen);
        juego.desordenar();
        juego.imprimirPorTexto();

        Scanner scanner = new Scanner(System.in);
        while (!juego.estaResuelto()) {
            System.out.print("Turno " + (juego.getTurnos() + 1) + ": ");
            char tecla = scanner.next().charAt(0);
            juego.mover(tecla);
            juego.imprimirPorTexto();
        }

        System.out.println("¡Felicidades! ¡Has resuelto el rompecabezas!");
    }
}
