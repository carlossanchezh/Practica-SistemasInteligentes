package es.upm.ssii.practica.prediccionClima.ui;

import es.upm.ssii.practica.prediccionClima.models.PredictionResult;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.geom.RoundRectangle2D;

/**
 * Ventana de resultados de la predicción meteorológica.
 * Muestra temperatura máx/mín/media, nubosidad, probabilidad de lluvia
 * y recomendación de ropa recibidas desde el agente ML.
 */
public class JFrameResultado extends JFrame {

    //Colores
    private static final Color BACKGROUND     = new Color(13,  18,  33);
    private static final Color ETIQUETAS_BACKGROUNDS     = new Color(22,  30,  70);
    private static final Color ROJO_MAX  = new Color(255, 0,  0);
    private static final Color AZUL_MIN = new Color( 80, 220, 200);
    private static final Color BLANCO   = new Color(255, 255, 255);
    private static final Color TEXTO_SUBELEM  = new Color(255, 255, 50);
    private static final Color RAIN_COLOR  = new Color( 80, 155, 245);

    // Etiquetas
    private JLabel etiquetaTempMedia;
    private JLabel etiquetaTempMaxima;
    private JLabel etiquetaTempMinima;
    private JLabel etiquetaNubes;
    private JLabel etiquetaViento;
    private JLabel etiquetaLluvia;
    private JLabel etiquetaRopa;

    public JFrameResultado(PredictionResult p) {
        inicializarUI();
        if (p != null) rellenarDatos(p);
    }

    //Configuración principal de la interfaz

    private void inicializarUI() {
        setTitle("Predicción del Clima · Sistema JADE");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(460, 650);
        setMinimumSize(new Dimension(460, 650));
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel root = new JPanel(new BorderLayout(0, 0));
        root.setBackground(BACKGROUND);
        root.setBorder(new EmptyBorder(22, 22, 22, 22));
        setContentPane(root);

        //Cabecera
        JPanel cabecera = new JPanel(new BorderLayout(0, 4));
        cabecera.setOpaque(false);
        cabecera.setBorder(new EmptyBorder(0, 0, 16, 0));

        JLabel titulo = new JLabel("Predicción Meteorológica");
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 19));
        titulo.setForeground(BLANCO);


        cabecera.add(titulo,BorderLayout.NORTH);
        root.add(cabecera, BorderLayout.NORTH);

        //Panel central
        JPanel centro = new JPanel();
        centro.setLayout(new BoxLayout(centro, BoxLayout.Y_AXIS));
        centro.setOpaque(false);

        //Pane Temperatura
        centro.add(crearTarjetaTemperatura());
        centro.add(Box.createVerticalStrut(12));

        //Panel Nubosidad + lluvia
        centro.add(crearTarjetaMetricas());
        centro.add(Box.createVerticalStrut(12));

        //Panel Ropa
        centro.add(crearTarjetaRopa());

        root.add(centro, BorderLayout.CENTER);

        //Boton de cierre
        JPanel footer = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
        footer.setOpaque(false);
        footer.setBorder(new EmptyBorder(14, 0, 0, 0));

        JButton btnCerrar = new JButton("Cerrar");
        estilizarBoton(btnCerrar);
        btnCerrar.addActionListener((ActionEvent e) -> dispose());
        footer.add(btnCerrar);
        root.add(footer, BorderLayout.SOUTH);
    }

    private JPanel crearTarjetaTemperatura() {
        JPanel panelPrincipal = crearpanelPrincipal();
        panelPrincipal.setLayout(new BorderLayout(0, 0));

        // Temperatura media
        JPanel rightPanel = new JPanel(new GridLayout(3, 1, 0, 2));
        rightPanel.setOpaque(false);

        etiquetaTempMedia = new JLabel("──°C", SwingConstants.LEFT);
        etiquetaTempMedia.setFont(new Font("Segoe UI Light", Font.BOLD, 46));
        etiquetaTempMedia.setForeground(BLANCO);

        JLabel lblMediaLabel = new JLabel("Temperatura media", SwingConstants.LEFT);
        lblMediaLabel.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        lblMediaLabel.setForeground(TEXTO_SUBELEM);

        // Max / Min
        JPanel maxMinPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        maxMinPanel.setOpaque(false);

        maxMinPanel.setBorder(new EmptyBorder(10, 0, 0, 0));

        etiquetaTempMaxima = new JLabel("↑ ──°C");
        etiquetaTempMaxima.setFont(new Font("Segoe UI", Font.BOLD, 13));
        etiquetaTempMaxima.setForeground(ROJO_MAX);
        etiquetaTempMaxima.setBorder(new EmptyBorder(0, 0, 0, 14));

        etiquetaTempMinima = new JLabel("↓ ──°C");
        etiquetaTempMinima.setFont(new Font("Segoe UI", Font.BOLD, 13));
        etiquetaTempMinima.setForeground(AZUL_MIN);

        maxMinPanel.add(etiquetaTempMaxima);
        maxMinPanel.add(etiquetaTempMinima);

        rightPanel.add(lblMediaLabel);
        rightPanel.add(etiquetaTempMedia);
        rightPanel.add(maxMinPanel);

        panelPrincipal.add(rightPanel, BorderLayout.CENTER);
        return panelPrincipal;
    }

    private JPanel crearTarjetaMetricas() {
        JPanel panelPrincipal = crearpanelPrincipal();
        panelPrincipal.setLayout(new GridLayout(3, 1, 0, 10));

        // Fila nubosidad
        JPanel filaNub = new JPanel(new BorderLayout());
        filaNub.setOpaque(false);
        JLabel nubLabel = new JLabel("-> Nubosidad");
        nubLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        nubLabel.setForeground(TEXTO_SUBELEM);
        etiquetaNubes = new JLabel("──%", SwingConstants.RIGHT);
        etiquetaNubes.setFont(new Font("Segoe UI", Font.BOLD, 13));
        etiquetaNubes.setForeground(BLANCO);
        filaNub.add(nubLabel, BorderLayout.WEST);
        filaNub.add(etiquetaNubes, BorderLayout.EAST);

        // Fila viento
        JPanel filaViento = new JPanel(new BorderLayout());
        filaViento.setOpaque(false);
        JLabel vientoLabel = new JLabel("-> Velocidad del viento");
        vientoLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        vientoLabel.setForeground(TEXTO_SUBELEM);
        etiquetaViento = new JLabel("── km/h", SwingConstants.RIGHT);
        etiquetaViento.setFont(new Font("Segoe UI", Font.BOLD, 13));
        etiquetaViento.setForeground(BLANCO);
        filaViento.add(vientoLabel, BorderLayout.WEST);
        filaViento.add(etiquetaViento, BorderLayout.EAST);

        // Fila lluvia
        JPanel filaLluvia = new JPanel(new BorderLayout());
        filaLluvia.setOpaque(false);
        JLabel lluviaLabel = new JLabel("-> Probabilidad de lluvia");
        lluviaLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lluviaLabel.setForeground(TEXTO_SUBELEM);
        etiquetaLluvia = new JLabel("──%", SwingConstants.RIGHT);
        etiquetaLluvia.setFont(new Font("Segoe UI", Font.BOLD, 13));
        etiquetaLluvia.setForeground(BLANCO);
        filaLluvia.add(lluviaLabel, BorderLayout.WEST);
        filaLluvia.add(etiquetaLluvia, BorderLayout.EAST);

        panelPrincipal.add(filaNub);
        panelPrincipal.add(filaViento);
        panelPrincipal.add(filaLluvia);
        return panelPrincipal;
    }

    private JPanel crearTarjetaRopa() {
        JPanel panelPrincipal = crearpanelPrincipal();
        panelPrincipal.setLayout(new BorderLayout(0, 8));
        panelPrincipal.setBackground(new Color(28, 38, 65));

        JLabel titulo = new JLabel("-> Recomendación de ropa");
        titulo.setFont(new Font("Segoe UI", Font.BOLD, 12));
        titulo.setForeground(TEXTO_SUBELEM);

        etiquetaRopa = new JLabel("Calculando...");
        etiquetaRopa.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        etiquetaRopa.setForeground(BLANCO);

        panelPrincipal.add(titulo, BorderLayout.NORTH);
        panelPrincipal.add(etiquetaRopa, BorderLayout.CENTER);
        return panelPrincipal;
    }

    // Manejo de los datos

    private void rellenarDatos(PredictionResult p) {
        etiquetaTempMedia.setText(String.format("%.1f°C", p.getTemperaturaMedia()));
        etiquetaTempMaxima.setText(String.format("Temp. Max: ↑ %.1f°C", p.getTemperaturaMax()));
        etiquetaTempMinima.setText(String.format("Temp. Min: ↓ %.1f°C", p.getTemperaturaMin()));

        int pctNub = (int) Math.round(p.getNubosidad());
        etiquetaNubes.setText(pctNub + "/10");

        double viento = p.getVelocidadViento();
        etiquetaViento.setText(String.format("%.1f km/h", viento));

        int pctLluvia = (int) Math.round(p.getProbabilidadLluvia() * 100);
        etiquetaLluvia.setText(pctLluvia + "%");
        etiquetaLluvia.setForeground(pctLluvia > 50 ? RAIN_COLOR : BLANCO);

        String rec = p.getRecomendacion() != null ? p.getRecomendacion() : "Sin información";
        etiquetaRopa.setText(rec);

        // Color de temperatura media según temperatura
        etiquetaTempMedia.setForeground(colorSegunTemp(p.getTemperaturaMedia()));

        repaint();
    }

    // Auxiliares

    /** Tarjeta con fondo redondeado y padding */
    private JPanel crearpanelPrincipal() {
        JPanel panelPrincipal = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(ETIQUETAS_BACKGROUNDS);
                g2.fill(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 20, 20));
                g2.dispose();
            }
        };
        panelPrincipal.setOpaque(false);
        panelPrincipal.setBorder(new EmptyBorder(18, 20, 18, 20));
        panelPrincipal.setMaximumSize(new Dimension(Integer.MAX_VALUE, 160));
        panelPrincipal.setAlignmentX(Component.LEFT_ALIGNMENT);
        return panelPrincipal;
    }

    private void estilizarBoton(JButton btn) {
        btn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btn.setForeground(BLANCO);
        btn.setBackground(new Color(45, 55, 88));
        btn.setBorder(new EmptyBorder(8, 28, 8, 28));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setOpaque(true);
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override public void mouseEntered(java.awt.event.MouseEvent e) { btn.setBackground(new Color(60, 80, 130)); }
            @Override public void mouseExited (java.awt.event.MouseEvent e) { btn.setBackground(new Color(45, 55, 88)); }
        });
    }


    private Color colorSegunTemp(double t) {
        if (t < 15)  return AZUL_MIN;
        if (t < 25) return new Color(255, 140, 0);
        if (t > 40) return new Color(50, 50, 50);
        return ROJO_MAX;
    }

}
