import java.util.ArrayList;
import java.util.List;

/**
 * MODELO: Esta clase me sirve para agrupar todo lo que pertenece a una misma ronda.
 * Es decir, la imagen de fondo y la lista de todas las frutas/objetos escondidos en ella.
 */
public class Nivel {
    private String rutaImagen; // La ruta del archivo de la foto
    private List<Objetivo> objetivos; // Una lista dinámica con los objetos a buscar

    public Nivel(String rutaImagen) {
        this.rutaImagen = rutaImagen;
        this.objetivos = new ArrayList<>(); // Inicializo la lista vacía
    }

    // Método para ir metiendo las frutas en la lista de este nivel
    public void agregarObjetivo(Objetivo objetivo) {
        this.objetivos.add(objetivo);
    }

    public String getRutaImagen() { return rutaImagen; }
    public List<Objetivo> getObjetivos() { return objetivos; }

    /**
     * Cuando el usuario hace clic, le paso las coordenadas a este método.
     * Recorro mi lista de objetos para ver si ha tocado alguno.
     */
    public Objetivo procesarClic(int x, int y) {
        for (Objetivo obj : objetivos) {
            // Le pregunto a cada objeto si el clic le ha dado
            if (obj.verificarImpacto(x, y)) {
                obj.marcarComoEncontrado(); // Lo marco para no volver a buscarlo
                return obj; // Devuelvo el objeto al que le he dado
            }
        }
        return null; // Si termina el bucle y no le he dado a nada, devuelvo nulo
    }

    /**
     * Método para comprobar si ya he ganado la partida.
     * Recorro la lista y si encuentro UNO solo que falte, devuelvo false.
     */
    public boolean isCompletado() {
        for (Objetivo obj : objetivos) {
            if (!obj.isEncontrado()) {
                return false;
            }
        }
        return true; // Si el bucle termina, es que todos están encontrados
    }
}