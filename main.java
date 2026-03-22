import javax.swing.SwingUtilities;

/**ARRANQUE: Esta es la clase principal que ejecuta Java.*/
public class Main {
    public static void main(String[] args) {
        // Uso invokeLater porque Swing no es seguro con los hilos. 
        // Esto mete la creación de la ventana en la "cola de tareas visuales" para que no pete.
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                VentanaJuego ventana = new VentanaJuego();
                ventana.setVisible(true); // esto es para que salga la ventana sino no se ve 
            }
        });
    }
}