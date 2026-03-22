import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class VentanaJuego extends JFrame implements ActionListener, MouseListener {

    private JComboBox<Integer> comboTiempo;
    private JLabel lblCronometro;
    private JButton btnIniciarPartida;
    
    // 1 Un panel personalizado para poder dibujar círculos sobre la foto
    private PanelConImagen panelCentral; 
    private JPanel panelSur; 

    private Timer temporizador;
    private MotorJuego motor;

    public VentanaJuego() {
        this.motor = new MotorJuego();
        
        this.setTitle("MTPA - Encuéntralo");
        this.setSize(800, 600);
        this.setResizable(false); 
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);
        this.setLayout(new BorderLayout(10, 10));

        configurarPanelSuperior();
        
        // Inicializamos nuestro panel dibujable
        panelCentral = new PanelConImagen();
        panelCentral.addMouseListener(this);
        panelCentral.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        this.add(panelCentral, BorderLayout.CENTER);
        
        panelSur = new JPanel();
        this.add(panelSur, BorderLayout.SOUTH);

        configurarEventos();
    }

    private void configurarPanelSuperior() {
        JPanel panelSuperior = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panelSuperior.add(new JLabel("Tiempo:"));

        Integer[] opciones = {30, 45, 60}; // Tiempos exactos del enunciado
        comboTiempo = new JComboBox<>(opciones);
        panelSuperior.add(comboTiempo);

        lblCronometro = new JLabel(" ⏱ 0s "); // Icono de reloj del boceto
        lblCronometro.setFont(new Font("Arial", Font.BOLD, 16));
        panelSuperior.add(lblCronometro);

        btnIniciarPartida = new JButton("Iniciar Partida");
        panelSuperior.add(btnIniciarPartida);

        this.add(panelSuperior, BorderLayout.NORTH);
    }

    private void configurarEventos() {
        btnIniciarPartida.addActionListener(this);

        temporizador = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                motor.restarSegundo();
                lblCronometro.setText(" ⏱ " + motor.getTiempoRestante() + "s ");
                
                if (motor.isTiempoAgotado()) { // Si no encuentra los detalles a tiempo, pierde
                    temporizador.stop();
                    JOptionPane.showMessageDialog(VentanaJuego.this, "¡Tiempo agotado! Has perdido.", "Fin del Juego", JOptionPane.ERROR_MESSAGE);
                    btnIniciarPartida.setEnabled(true);
                }
            }
        });
    }

    private void actualizarPanelInferior() {
        panelSur.removeAll(); 
        List<Objetivo> objetivos = motor.getNivelActual().getObjetivos();
        
        panelSur.setLayout(new GridLayout(1, objetivos.size(), 10, 10));
        panelSur.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        for (Objetivo obj : objetivos) {
            JLabel etiqueta = new JLabel(obj.getNombre(), SwingConstants.CENTER);
            etiqueta.setName(obj.getNombre()); 
            etiqueta.setBorder(BorderFactory.createLineBorder(Color.GRAY));
            panelSur.add(etiqueta);
        }
        panelSur.revalidate();
        panelSur.repaint();
    }

    private void cargarNivelVisual() {
        Nivel nivelActual = motor.getNivelActual();
        try {
            Image img = new ImageIcon(nivelActual.getRutaImagen()).getImage();
            panelCentral.setImagen(img, motor.getNivelActual().getObjetivos());
        } catch (Exception ex) {
            System.out.println("Error cargando imagen");
        }
        actualizarPanelInferior();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnIniciarPartida) {
            int tiempo = (Integer) comboTiempo.getSelectedItem();
            
            motor.iniciarJuego(tiempo); // Carga la primera imagen
            lblCronometro.setText(" ⏱ " + tiempo + "s ");
            
            cargarNivelVisual();
            
            btnIniciarPartida.setEnabled(false);
            temporizador.start();
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (temporizador != null && temporizador.isRunning()) {
            Objetivo obj = motor.comprobarClic(e.getX(), e.getY());
            
            if (obj != null) { 
                // 1. Marcamos el detalle inferior en verde
                for (Component comp : panelSur.getComponents()) {
                    JLabel etiqueta = (JLabel) comp;
                    if (etiqueta.getName().equals(obj.getNombre())) {
                        etiqueta.setForeground(Color.GREEN);
                        etiqueta.setText(obj.getNombre() + " [OK]");
                    }
                }
                
                // 2. Le decimos al panel que redibuje para que aparezca el círculo negro en la foto
                panelCentral.repaint(); 
                
                // 3. Comprobamos si completamos la foto
                if (motor.isNivelSuperado()) {
                    temporizador.stop();
                    JOptionPane.showMessageDialog(this, "¡Imagen completada! Pasando a la siguiente...");
                    
                    if (motor.avanzarSiguienteNivel()) {
                        cargarNivelVisual();
                        temporizador.start(); // Reiniciamos para la nueva imagen
                    } else {
                        JOptionPane.showMessageDialog(this, "¡HAS GANADO EL JUEGO COMPLETO!");
                        btnIniciarPartida.setEnabled(true);
                    }
                }
            }
        }
    }

    @Override public void mousePressed(MouseEvent e) {}
    @Override public void mouseReleased(MouseEvent e) {}
    @Override public void mouseEntered(MouseEvent e) {}
    @Override public void mouseExited(MouseEvent e) {}

    // --- CLASE INTERNA PRIVADA PARA PINTAR CÍRCULOS SOBRE LA FOTO
    private class PanelConImagen extends JPanel {
        private Image imagen;
        private List<Objetivo> listaObjetivos;

        public void setImagen(Image img, List<Objetivo> objetivos) {
            this.imagen = img;
            this.listaObjetivos = objetivos;
            this.repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (imagen != null) {
                // Dibujamos la foto escalada
                g.drawImage(imagen, 0, 0, getWidth(), getHeight(), this);
                
                // Dibujamos un círculo grueso negro por cada objetivo encontrado (como en el PDF)
                if (listaObjetivos != null) {
                    Graphics2D g2d = (Graphics2D) g;
                    g2d.setColor(Color.BLACK);
                    g2d.setStroke(new BasicStroke(4)); // Grosor del rotulador
                    
                    for (Objetivo obj : listaObjetivos) {
                        if (obj.isEncontrado()) {
                            int r = obj.getRadioColision();
                            g2d.drawOval(obj.getCentroX() - r, obj.getCentroY() - r, r * 2, r * 2);
                        }
                    }
                }
            }
        }
    }
}
