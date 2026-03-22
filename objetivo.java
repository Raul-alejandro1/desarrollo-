public class Objetivo {
    private String nombre;
    private int centroX;
    private int centroY;
    private int radioColision;
    private boolean encontrado;

    public Objetivo(String nombre, int centroX, int centroY, int radioColision) {
        this.nombre = nombre;
        this.centroX = centroX;
        this.centroY = centroY;
        this.radioColision = radioColision;
        this.encontrado = false;
    }

    public String getNombre() { return nombre; }
    public boolean isEncontrado() { return encontrado; }
    public void marcarComoEncontrado() { this.encontrado = true; }

    //Solucion al error , metodos para que la ventana pueda leer donde dibujar el circulo
    public int getCentroX() { return centroX; }
    public int getCentroY() { return centroY; }
    public int getRadioColision() { return radioColision; }
    // ----------------------------------------

    public boolean verificarImpacto(int clicX, int clicY) {
        if (encontrado) return false;
        // Formula de la distancia (Teorema de Pitágoras)
        double distancia = Math.sqrt(Math.pow(clicX - centroX, 2) + Math.pow(clicY - centroY, 2));
        return distancia <= radioColision;
    }
}