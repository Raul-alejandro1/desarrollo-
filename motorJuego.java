import java.util.ArrayList;
import java.util.List;

public class MotorJuego {
    private int tiempoRestante;
    private int tiempoInicial;
    private List<Nivel> nivelesJuego;
    private int indiceNivelActual;

    public MotorJuego() {
        nivelesJuego = new ArrayList<>();
        
        // configuracion para el nivel uno
        Nivel n1 = new Nivel("imagen1.jpg");
        // Usamos tus coordenadas exactas: [X, Y, Radio de acierto]
        n1.agregarObjetivo(new Objetivo("Señor Plátano", 190, 170, 50));
        n1.agregarObjetivo(new Objetivo("Fresa Agresiva", 600, 400, 50));
        nivelesJuego.add(n1);

        // configuracion para el nivel dos
        Nivel n2 = new Nivel("imagen2.jpg");
        n2.agregarObjetivo(new Objetivo("Señor Plátano", 80, 420, 50));
        n2.agregarObjetivo(new Objetivo("Fresa Agresiva", 730, 30, 50));
        nivelesJuego.add(n2);
    }

    public void iniciarJuego(int tiempo) {
        this.tiempoInicial = tiempo;
        this.tiempoRestante = tiempo;
        this.indiceNivelActual = 0;
    }

    public Nivel getNivelActual() { return nivelesJuego.get(indiceNivelActual); }
    public void restarSegundo() { this.tiempoRestante--; }
    public int getTiempoRestante() { return tiempoRestante; }
    public boolean isTiempoAgotado() { return tiempoRestante <= 0; }

    public Objetivo comprobarClic(int x, int y) {
        return getNivelActual().procesarClic(x, y);
    }

    public boolean isNivelSuperado() {
        return getNivelActual().isCompletado();
    }

    public boolean avanzarSiguienteNivel() {
        indiceNivelActual++;
        if (indiceNivelActual < nivelesJuego.size()) {
            this.tiempoRestante = tiempoInicial; // Reset de tiempo para el nuevo nivel
            return true;
        }
        return false; 
    }
}