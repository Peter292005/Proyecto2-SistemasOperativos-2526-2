/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package GUI;

/**
 *
 * @author Peter
 */
import Concurrencia.GestorLocks;
import Estructuras.ListaEnlazada;
import Filesystem.Archivo;
import Filesystem.Directorio;
import Filesystem.NodoFS;
import Filesystem.SistemaArchivos;
import Journal.EntradaJournal;
import Journal.JournalManager;
import Procesos.EstadoProceso;
import Procesos.Proceso;
import Procesos.SolicitudIO;
import Procesos.TipoOperacionIO;
import Scheduler.PlanificadorCSCAN;
import Scheduler.PlanificadorFIFO;
import Scheduler.PlanificadorSCAN;
import Scheduler.PlanificadorSSTF;
import Pruebas.AplicadorCasoPrueba;
import Pruebas.CargadorCasoJSON;
import Pruebas.CasoPrueba;
import Pruebas.ContextoCasoPrueba;
import Pruebas.SimuladorPoliticas;
import javax.swing.ScrollPaneConstants;
import javax.swing.Timer;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.SwingUtilities;

import java.awt.GridBagLayout;
import java.awt.Graphics;
import java.awt.RenderingHints;
import javax.swing.JPopupMenu;
import javax.swing.JMenuItem;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

import javax.swing.SwingUtilities;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTree;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.JRadioButton;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import javax.swing.Box;
import javax.swing.Icon;
import java.awt.Graphics2D;
import java.awt.BasicStroke;


public class VentanaPrincipal extends JFrame {
    private final SistemaArchivos sistema;
    private final JournalManager journal;
    private final GestorLocks gestorLocks;
    private VentanaProcesos ventanaProcesos;

    
    private final ListaEnlazada<SolicitudIO> solicitudesActualesCaso;

    private final ListaEnlazada<Proceso> procesosSistema;
    private final ListaEnlazada<SolicitudIO> historialSolicitudes;

    private int siguientePid = 1;
    private int siguienteSolicitudId = 1;
    private int cabezaActual = 0;

    private JTree arbol;
    private DefaultTreeModel modeloArbol;

    private JTable tablaAsignacion;
    private DefaultTableModel modeloTablaAsignacion;
    private Timer timerSimulacion;
private ListaEnlazada<SolicitudIO> ordenSimulacionActual;
private int indiceSimulacion = 0;
private SolicitudIO solicitudActiva = null;
private boolean simulacionPreparada = false;
private boolean simulacionEnCurso = false;
private JButton btnCargarCaso;
private JButton btnPaso;


private JComboBox<String> comboVelocidad;
    
    private CasoPrueba casoPruebaActual;
private ContextoCasoPrueba contextoCasoActual;
private final SimuladorPoliticas simuladorPoliticas = new SimuladorPoliticas();

    private PanelDisco panelDisco;
    private PanelLogs panelLogs;
    private PanelJournal panelJournal;
    private PanelProcesos panelProcesos;
    private PanelPropiedadesNodo panelPropiedadesNodo;
    private PanelLocksActivos panelLocksActivos;
    private PanelSolicitudesIO panelSolicitudesIO;
    private PanelBarraCabezal panelBarraCabezal;

    private JComboBox<String> comboScheduler;
    private JButton btnReanudar;
private JButton btnInstantaneo;
private JButton btnPausar;
private JButton btnEjecutarCaso;

    private JLabel lblBloquesLibres;
    private JLabel lblBloquesOcupados;
    private JLabel lblSchedulerActivo;
    private JLabel lblCabezal;
    private JLabel lblSolicitudes;
    
    private JTabbedPane tabsCentro;
private PanelCard cardJournalCentro;
private PanelCard cardLocksCentro;
private PanelCard cardProcesosCentro;

    private JRadioButton rbAdmin;
    private JRadioButton rbUsuario;

    public VentanaPrincipal(SistemaArchivos sistema, JournalManager journal) {
        this.sistema = sistema;
        this.journal = journal;
        this.gestorLocks = new GestorLocks();
        this.procesosSistema = new ListaEnlazada<>();
this.historialSolicitudes = new ListaEnlazada<>();
this.solicitudesActualesCaso = new ListaEnlazada<>();
this.ordenSimulacionActual = new ListaEnlazada<>();
        
        

        

        configurarLookAndFeelBasico();

        setTitle("Simulador de Sistema de Archivos - Proyecto 2 SO");
        setSize(1520, 900);
        setMinimumSize(new Dimension(1350, 800));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        getContentPane().setBackground(TemaUI.FONDO_APP);
        setLayout(new BorderLayout(10, 10));

        JPanel contenedor = new JPanel(new BorderLayout(10, 10));
        contenedor.setBackground(TemaUI.FONDO_APP);
        contenedor.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        setContentPane(contenedor);

        add(crearHeader(), BorderLayout.NORTH);
        add(crearCentro(), BorderLayout.CENTER);
        add(crearZonaLogs(), BorderLayout.SOUTH);

        refrescarTodo();
    }

    private void configurarLookAndFeelBasico() {
        UIManager.put("ToolTip.background", TemaUI.CARD);
        UIManager.put("ToolTip.foreground", TemaUI.TEXTO);
        UIManager.put("ToolTip.border", BorderFactory.createLineBorder(TemaUI.BORDE));
    }

  private JPanel crearHeader() {
    JPanel header = new JPanel(new BorderLayout(12, 12));
    header.setBackground(new Color(7, 16, 32));
    header.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(TemaUI.BORDE_SUAVE, 1),
            BorderFactory.createEmptyBorder(14, 18, 14, 18)
    ));

    JLabel titulo = new JLabel("Simulador de Sistema de Archivos");
    titulo.setForeground(new Color(240, 248, 255));
    titulo.setFont(TemaUI.FUENTE_TITULO);

    JPanel filaSuperior = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
    filaSuperior.setOpaque(false);
    filaSuperior.add(titulo);

    JPanel filaInferior = new JPanel(new BorderLayout(12, 0));
    filaInferior.setOpaque(false);

    JPanel grupoPrincipal = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
    grupoPrincipal.setOpaque(false);

    comboScheduler = new JComboBox<>(new String[]{"FIFO", "SSTF", "SCAN", "C-SCAN"});
    estilizarCombo(comboScheduler);

    comboVelocidad = new JComboBox<>(new String[]{"Lenta", "Media", "Rápida"});
    estilizarCombo(comboVelocidad);
    comboVelocidad.setSelectedItem("Media");
    comboVelocidad.addActionListener(e -> actualizarVelocidadEnCaliente());

    btnCargarCaso = crearBoton("Cargar");
btnEjecutarCaso = crearBoton("Ejecutar");
btnPausar = crearBoton("Pausar");
btnReanudar = crearBoton("Reanudar");
btnPaso = crearBoton("Paso a paso");
btnInstantaneo = crearBoton("Instantánea");

    btnCargarCaso.setBackground(new Color(30, 64, 175));
    btnEjecutarCaso.setBackground(new Color(22, 163, 74));
    btnPausar.setBackground(new Color(147, 51, 234));
    btnReanudar.setBackground(new Color(79, 70, 229));
    btnPaso.setBackground(new Color(8, 145, 178));
    btnInstantaneo.setBackground(new Color(217, 119, 6));

    btnCargarCaso.addActionListener(e -> cargarCasoDesdeJSON());
    btnEjecutarCaso.addActionListener(e -> ejecutarCasoActual());
    btnPausar.addActionListener(e -> pausarSimulacion());
    btnReanudar.addActionListener(e -> reanudarSimulacion());
    btnPaso.addActionListener(e -> ejecutarPasoManual());
    btnInstantaneo.addActionListener(e -> ejecutarCasoInstantaneo());

    JLabel lblScheduler = new JLabel("Scheduler:");
    lblScheduler.setForeground(TemaUI.TEXTO);
    lblScheduler.setFont(TemaUI.FUENTE_SUBTITULO);

    JLabel lblVelocidad = new JLabel("Velocidad:");
    lblVelocidad.setForeground(TemaUI.TEXTO);
    lblVelocidad.setFont(TemaUI.FUENTE_SUBTITULO);

    grupoPrincipal.add(lblScheduler);
    grupoPrincipal.add(comboScheduler);
    grupoPrincipal.add(Box.createHorizontalStrut(8));
    grupoPrincipal.add(lblVelocidad);
    grupoPrincipal.add(comboVelocidad);
    grupoPrincipal.add(Box.createHorizontalStrut(14));
    grupoPrincipal.add(btnCargarCaso);
    grupoPrincipal.add(btnEjecutarCaso);
    grupoPrincipal.add(btnPausar);
    grupoPrincipal.add(btnReanudar);
    grupoPrincipal.add(btnPaso);
    grupoPrincipal.add(btnInstantaneo);

   JButton btnMenu = crearBoton("☰ Menú");
btnMenu.setBackground(new Color(51, 65, 85));

JPopupMenu menu = new JPopupMenu();
estilizarPopupMenu(menu);

JMenuItem tituloSim = crearMenuTitulo("Simulación");
JMenuItem itemLimpiar = crearMenuItem("Limpiar Disco", e -> limpiarDiscoCompleto());
JMenuItem itemJ1 = crearMenuItem("Crear archivo fallo", e -> probarJ1Real());

JMenuItem tituloArch = crearMenuTitulo("Operaciones de archivos");
JMenuItem itemCrearArchivo = crearMenuItem("Crear Archivo", e -> crearArchivoDesdeGUI());
JMenuItem itemCrearDirectorio = crearMenuItem("Crear Directorio", e -> crearDirectorioDesdeGUI());
JMenuItem itemLeer = crearMenuItem("Leer", e -> leerNodoDesdeGUI());
JMenuItem itemRenombrar = crearMenuItem("Renombrar", e -> renombrarNodoDesdeGUI());
JMenuItem itemEliminar = crearMenuItem("Eliminar", e -> eliminarNodoDesdeGUI());

JMenuItem tituloRec = crearMenuTitulo("Recuperación");
JMenuItem itemFallo = crearMenuItem("Fallo/Recuperar", e -> simularFallo());

menu.add(tituloSim);
menu.add(itemLimpiar);
menu.add(itemJ1);
menu.addSeparator();

menu.add(tituloArch);
menu.add(itemCrearArchivo);
menu.add(itemCrearDirectorio);
menu.add(itemLeer);
menu.add(itemRenombrar);
menu.add(itemEliminar);
menu.addSeparator();

menu.add(tituloRec);
menu.add(itemFallo);

btnMenu.addActionListener(e -> {
    boolean esAdmin = rbAdmin.isSelected();

    itemLimpiar.setEnabled(esAdmin);
    itemJ1.setEnabled(esAdmin);
    itemCrearArchivo.setEnabled(esAdmin);
    itemCrearDirectorio.setEnabled(esAdmin);
    itemLeer.setEnabled(esAdmin);
    itemRenombrar.setEnabled(esAdmin);
    itemEliminar.setEnabled(esAdmin);
    itemFallo.setEnabled(esAdmin);

    menu.show(btnMenu, 0, btnMenu.getHeight());
});

    JPanel contenedorIzq = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
    contenedorIzq.setOpaque(false);
    contenedorIzq.add(grupoPrincipal);

    JPanel contenedorDer = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
    contenedorDer.setOpaque(false);
    contenedorDer.add(btnMenu);

    filaInferior.add(contenedorIzq, BorderLayout.WEST);
    filaInferior.add(contenedorDer, BorderLayout.EAST);

    header.add(filaSuperior, BorderLayout.NORTH);
    header.add(filaInferior, BorderLayout.CENTER);

    return header;
}
  
    private JSplitPane crearCentro() {
    JSplitPane splitPrincipal = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
    splitPrincipal.setBorder(null);
    splitPrincipal.setDividerSize(6);
    splitPrincipal.setContinuousLayout(true);
    splitPrincipal.setLeftComponent(crearSidebarIzquierda());
    splitPrincipal.setRightComponent(crearZonaCentroConDerecha());
    splitPrincipal.setResizeWeight(0.19);
    return splitPrincipal;
}
    private JSplitPane crearZonaCentroConDerecha() {
    JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
    split.setBorder(null);
    split.setDividerSize(6);
    split.setContinuousLayout(true);
    split.setLeftComponent(crearZonaCentral());
    split.setRightComponent(crearSidebarDerechaPropiedades());
    split.setResizeWeight(0.87);
    return split;
}
    private JPanel crearSidebarDerechaPropiedades() {
    JPanel sidebar = new JPanel(new BorderLayout(10, 10));
    sidebar.setBackground(TemaUI.FONDO_APP);

    PanelCard cardPropiedades = new PanelCard("Propiedades del Nodo");
    panelPropiedadesNodo = new PanelPropiedadesNodo();
    cardPropiedades.getContenido().add(panelPropiedadesNodo, BorderLayout.CENTER);

    sidebar.add(cardPropiedades, BorderLayout.CENTER);
    return sidebar;
}

    private JPanel crearSidebarIzquierda() {
    JPanel sidebar = new JPanel(new BorderLayout(10, 10));
    sidebar.setBackground(TemaUI.FONDO_APP);

    JPanel superior = new JPanel(new GridLayout(2, 1, 10, 10));
    superior.setOpaque(false);

    PanelCard cardControles = new PanelCard("Controles");
    JPanel pControles = new JPanel(new GridLayout(5, 1, 8, 8));
    pControles.setOpaque(false);
   

    rbAdmin = new JRadioButton("Administrador");
rbUsuario = new JRadioButton("Usuario");
estilizarRadio(rbAdmin);
estilizarRadio(rbUsuario);
rbAdmin.setSelected(true);

    ButtonGroup grupo = new ButtonGroup();
    grupo.add(rbAdmin);
    grupo.add(rbUsuario);
    rbAdmin.addActionListener(e -> actualizarPermisosPorRol());
rbUsuario.addActionListener(e -> actualizarPermisosPorRol());

    lblSchedulerActivo = crearBadge("Política: FIFO");
    lblBloquesLibres = crearBadge("Bloques libres: 0");
    lblBloquesOcupados = crearBadge("Bloques ocupados: 0");

    pControles.add(rbAdmin);
    pControles.add(rbUsuario);
    pControles.add(lblSchedulerActivo);
    pControles.add(lblBloquesLibres);
    pControles.add(lblBloquesOcupados);
    cardControles.getContenido().add(pControles, BorderLayout.CENTER);

    PanelCard cardArbol = new PanelCard("Sistema de Archivos");
cardArbol.setPreferredSize(new Dimension(260, 420));
    DefaultMutableTreeNode raizVisual = new DefaultMutableTreeNode(sistema.getRoot());
    modeloArbol = new DefaultTreeModel(raizVisual);
    arbol = new JTree(modeloArbol);
    estilizarTree(arbol);
    arbol.addTreeSelectionListener(this::alSeleccionarNodo);

    JScrollPane scrollArbol = new JScrollPane(arbol);
estilizarScrollArbol(scrollArbol);
cardArbol.getContenido().add(scrollArbol, BorderLayout.CENTER);

    superior.add(cardControles);
    superior.add(cardArbol);

    sidebar.add(superior, BorderLayout.CENTER);
    SwingUtilities.invokeLater(this::actualizarPermisosPorRol);
    return sidebar;
}

  private void estilizarScrollArbol(JScrollPane scroll) {
    scroll.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(37, 99, 235, 120), 1),
            BorderFactory.createEmptyBorder(2, 2, 2, 2)
    ));
    scroll.getViewport().setBackground(new Color(10, 24, 44));
    scroll.getVerticalScrollBar().setUnitIncrement(16);
    scroll.getHorizontalScrollBar().setUnitIncrement(16);
    scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
}

   private JPanel crearZonaCentral() {
    JPanel zona = new JPanel(new BorderLayout(10, 10));
    zona.setBackground(TemaUI.FONDO_APP);

    PanelCard cardEstadisticas = new PanelCard("Estadísticas");
    JPanel stats = new JPanel(new GridLayout(1, 5, 10, 10));
    stats.setOpaque(false);

    lblCabezal = crearStatPanel("Cabezal", "0");
    lblSolicitudes = crearStatPanel("Solicitudes", "0");
    JLabel lblA = crearStatPanel("Scheduler", "Activo");
    JLabel lblB = crearStatPanel("Estado", "Operativo");
    JLabel lblC = crearStatPanel("Modo", "Interactivo");

    stats.add(lblA);
    stats.add(lblB);
    stats.add(lblC);
    stats.add(lblCabezal);
    stats.add(lblSolicitudes);

    cardEstadisticas.getContenido().add(stats, BorderLayout.CENTER);

    tabsCentro = new JTabbedPane();
    estilizarTabs(tabsCentro);

    JPanel panelDiscoCompleto = new JPanel(new BorderLayout(10, 10));
    panelDiscoCompleto.setOpaque(false);

    panelBarraCabezal = new PanelBarraCabezal();
    panelBarraCabezal.setPreferredSize(new Dimension(100, 72));

    panelDisco = new PanelDisco(sistema.getDisco());

    JScrollPane scrollDisco = new JScrollPane(panelDisco);
    scrollDisco.setBorder(BorderFactory.createLineBorder(TemaUI.BORDE, 1));
    scrollDisco.getViewport().setBackground(TemaUI.CARD_2);
    scrollDisco.getVerticalScrollBar().setUnitIncrement(16);
    scrollDisco.getHorizontalScrollBar().setUnitIncrement(16);

    PanelCard cardDiscoInterno = new PanelCard("Disk Visualization");
    cardDiscoInterno.getContenido().add(scrollDisco, BorderLayout.CENTER);

    panelDiscoCompleto.add(panelBarraCabezal, BorderLayout.NORTH);
    panelDiscoCompleto.add(cardDiscoInterno, BorderLayout.CENTER);

    modeloTablaAsignacion = new DefaultTableModel(
            new Object[]{"Nombre", "Propietario", "Bloques", "Primer bloque", "Color"}, 0) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };
    tablaAsignacion = new JTable(modeloTablaAsignacion);
    estilizarTabla(tablaAsignacion);
    JScrollPane scrollTabla = new JScrollPane(tablaAsignacion);
    estilizarScroll(scrollTabla);
    PanelCard cardTabla = new PanelCard("Tabla de Asignación");
    cardTabla.getContenido().add(scrollTabla, BorderLayout.CENTER);

    panelSolicitudesIO = new PanelSolicitudesIO();
    PanelCard cardSolicitudes = new PanelCard("Solicitudes de E/S");
    cardSolicitudes.getContenido().add(panelSolicitudesIO, BorderLayout.CENTER);

    cardJournalCentro = new PanelCard("Journal");
    panelJournal = new PanelJournal();
    cardJournalCentro.getContenido().add(panelJournal, BorderLayout.CENTER);

    cardLocksCentro = new PanelCard("Locks Activos");
    panelLocksActivos = new PanelLocksActivos();
    cardLocksCentro.getContenido().add(panelLocksActivos, BorderLayout.CENTER);

cardProcesosCentro = new PanelCard("Cola de Procesos");
panelProcesos = new PanelProcesos();

JButton btnVerProcesosCompleto = crearBoton("Ver completo");
btnVerProcesosCompleto.setBackground(new Color(37, 99, 235));
btnVerProcesosCompleto.addActionListener(e -> abrirVentanaProcesos());

JPanel barraProcesos = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
barraProcesos.setOpaque(false);
barraProcesos.add(btnVerProcesosCompleto);

cardProcesosCentro.getContenido().add(barraProcesos, BorderLayout.NORTH);
cardProcesosCentro.getContenido().add(panelProcesos, BorderLayout.CENTER);

    tabsCentro.addTab("Disco", panelDiscoCompleto);
    tabsCentro.addTab("Asignación", cardTabla);
    tabsCentro.addTab("Solicitudes", cardSolicitudes);
    tabsCentro.addTab("Journal", cardJournalCentro);
    tabsCentro.addTab("Locks", cardLocksCentro);
    tabsCentro.addTab("Procesos", cardProcesosCentro);

    zona.add(cardEstadisticas, BorderLayout.NORTH);
    zona.add(tabsCentro, BorderLayout.CENTER);

    return zona;
}

  
    private PanelCard crearZonaLogs() {
        PanelCard card = new PanelCard("Log de Eventos");
        panelLogs = new PanelLogs();
        card.getContenido().add(panelLogs, BorderLayout.CENTER);
        card.setPreferredSize(new Dimension(100, 180));
        return card;
    }

    private JLabel crearStatPanel(String titulo, String valor) {
        JLabel panel = new JLabel("<html><div style='padding:6px'><span style='color:#38bdf8;'>" +
                titulo + "</span><br><span style='font-size:22px; color:#ebf1fa; font-weight:bold;'>" +
                valor + "</span></div></html>");
        panel.setOpaque(true);
        panel.setBackground(TemaUI.CARD_3);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(TemaUI.BORDE_SUAVE, 1),
                BorderFactory.createEmptyBorder(10, 12, 10, 12)
        ));
        return panel;
    }

    private JLabel crearBadge(String texto) {
        JLabel lbl = new JLabel(texto);
        lbl.setOpaque(true);
        lbl.setBackground(TemaUI.CARD_3);
        lbl.setForeground(TemaUI.TEXTO);
        lbl.setHorizontalAlignment(SwingConstants.CENTER);
        lbl.setFont(TemaUI.FUENTE_NORMAL);
        lbl.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(TemaUI.BORDE_SUAVE, 1),
                BorderFactory.createEmptyBorder(9, 12, 9, 12)
        ));
        return lbl;
    }
    
    private void reanudarSimulacion() {
    if (!simulacionPreparada && !simulacionEnCurso) {
        mostrarError("No hay una simulación pausada o preparada para reanudar.");
        return;
    }

    if (timerSimulacion != null && timerSimulacion.isRunning()) {
        return;
    }

    iniciarSimulacion();
}

private void actualizarVelocidadEnCaliente() {
    if (timerSimulacion != null && timerSimulacion.isRunning()) {
        timerSimulacion.setDelay(obtenerDelaySimulacion());
        timerSimulacion.setInitialDelay(obtenerDelaySimulacion());
        panelLogs.agregarLog("[SIM] Velocidad cambiada a " + comboVelocidad.getSelectedItem() + ".");
    }
}

private void ejecutarCasoInstantaneo() {
    if (contextoCasoActual == null) {
        mostrarError("Primero debes cargar un caso JSON.");
        return;
    }

    try {
        if (timerSimulacion != null) {
            timerSimulacion.stop();
        }

        simulacionEnCurso = false;
        simulacionPreparada = false;
        solicitudActiva = null;
        indiceSimulacion = 0;

        String politica = comboScheduler.getSelectedItem().toString();
        ListaEnlazada<SolicitudIO> orden = simuladorPoliticas.planificar(contextoCasoActual, politica);

        vaciarLista(ordenSimulacionActual);
        for (int i = 0; i < orden.tamano(); i++) {
            ordenSimulacionActual.agregar(orden.obtener(i));
        }

        vaciarLista(procesosSistema);
        for (int i = 0; i < contextoCasoActual.getSolicitudes().tamano(); i++) {
            SolicitudIO s = contextoCasoActual.getSolicitudes().obtener(i);
            s.getProceso().setEstado(EstadoProceso.TERMINADO);
            procesosSistema.agregar(s.getProceso());
        }

        if (!orden.estaVacia()) {
            cabezaActual = orden.obtener(orden.tamano() - 1).getPosicionDisco();
        } else {
            cabezaActual = contextoCasoActual.getCabezaInicial();
        }

        int movimientoTotal = simuladorPoliticas.calcularMovimientoTotal(
                contextoCasoActual.getCabezaInicial(),
                orden
        );

        panelLogs.agregarLog("[SIM] Ejecución instantánea.");
        panelLogs.agregarLog("[SIM] Política: " + politica);
        panelLogs.agregarLog("[SIM] Orden resultante: " + simuladorPoliticas.formatearOrdenPosiciones(orden));
        panelLogs.agregarLog("[SIM] Movimiento total: " + movimientoTotal);

        refrescarTodo();

    } catch (Exception ex) {
        mostrarError("Error en ejecución instantánea: " + ex.getMessage());
    }
}
    
  private void abrirVentanaProcesos() {
    if (ventanaProcesos == null || !ventanaProcesos.isDisplayable()) {
        ventanaProcesos = new VentanaProcesos(procesosSistema);
    }

    ventanaProcesos.refrescar(
            procesosSistema,
            solicitudActiva != null ? solicitudActiva.getProceso() : null
    );
    ventanaProcesos.setVisible(true);
    ventanaProcesos.toFront();
    ventanaProcesos.repaint();
}
    private JButton crearBoton(String texto) {
        JButton btn = new JButton(texto);
        btn.setFocusPainted(false);
        btn.setBackground(TemaUI.BOTON_GRIS);
        btn.setForeground(TemaUI.TEXTO);
        btn.setFont(TemaUI.FUENTE_BOTON);
        btn.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(TemaUI.BORDE, 1),
                BorderFactory.createEmptyBorder(8, 14, 8, 14)
        ));
        return btn;
    }
    
    private void estilizarPopupMenu(JPopupMenu menu) {
    menu.setBackground(new Color(15, 23, 42));
    menu.setBorder(BorderFactory.createLineBorder(TemaUI.BORDE_SUAVE, 1));
}

private JMenuItem crearMenuItem(String texto, java.awt.event.ActionListener accion) {
    JMenuItem item = new JMenuItem(texto);
    item.setFont(TemaUI.FUENTE_NORMAL);
    item.setBackground(new Color(15, 23, 42));
    item.setForeground(TemaUI.TEXTO);
    item.setFocusPainted(false);
    item.setBorder(BorderFactory.createEmptyBorder(10, 14, 10, 14));
    item.addActionListener(accion);
    return item;
}

    private JButton crearBotonPeligro(String texto) {
        JButton btn = crearBoton(texto);
        btn.setBackground(TemaUI.BOTON_ROJO);
        btn.setForeground(Color.WHITE);
        return btn;
    }

    private void estilizarCombo(JComboBox<String> combo) {
        combo.setBackground(TemaUI.CARD_2);
        combo.setForeground(TemaUI.TEXTO);
        combo.setFont(TemaUI.FUENTE_NORMAL);
    }

    private void estilizarRadio(JRadioButton radio) {
    radio.setOpaque(true);
    radio.setFocusPainted(false);
    radio.setBackground(new Color(11, 25, 45));
    radio.setForeground(TemaUI.TEXTO);
    radio.setFont(new Font("Segoe UI", Font.PLAIN, 14));
    radio.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(30, 41, 59), 1),
            BorderFactory.createEmptyBorder(10, 12, 10, 12)
    ));
}
    
    private void probarJ1Real() {
    if (rbUsuario.isSelected()) {
        mostrarError("En modo Usuario no se permite ejecutar J1 real.");
        return;
    }

    String nombre = pedirTexto("Nombre del archivo para J1:");
    if (nombre == null) return;

    if (sistema.getRoot().buscarHijoPorNombre(nombre) != null) {
        mostrarError("Ya existe un nodo con ese nombre en el directorio raíz.");
        return;
    }

    String propietario = pedirTexto("Propietario:");
    if (propietario == null) return;

    Integer bloques = pedirEnteroPositivo("Cantidad de bloques:");
    if (bloques == null) return;

    Archivo archivo = new Archivo(nombre, propietario, bloques);

    // 1) Registrar CREATE como PENDIENTE en journal
    EntradaJournal entrada = journal.registrarOperacionPendiente(TipoOperacionIO.CREATE, archivo);

    // 2) Asignar bloques en disco
    boolean asignado = sistema.getDisco().asignarBloquesAArchivo(archivo);
    if (!asignado) {
        panelLogs.agregarLog("[J1] No se pudo asignar espacio para '" + nombre + "'.");
        refrescarTodo();
        return;
    }

    // Ahora sí conocemos el primer bloque real
    entrada.setPrimerBloque(archivo.getPrimerBloque());

    panelLogs.agregarLog("[J1] CREATE registrado como PENDIENTE para '" + nombre + "'.");
    panelLogs.agregarLog("[J1] Bloques asignados temporalmente. Primer bloque: " + archivo.getPrimerBloque());
    panelJournal.agregarEvento("[EVENTO] J1 CREATE PENDIENTE: " + nombre);

    // 3) Simular fallo ANTES del commit y ANTES de agregar al árbol
    panelLogs.agregarLog("[J1] Se simula fallo antes del commit.");
    panelJournal.agregarEvento("[EVENTO] FALLO J1 ANTES DE COMMIT");
    panelJournal.marcarFalloSimulado();

    // 4) Recovery hace UNDO de la entrada pendiente
    journal.recuperarOperacionesPendientes(sistema.getDisco());

    panelLogs.agregarLog("[J1] Recovery ejecutó UNDO sobre '" + nombre + "'.");
    panelJournal.agregarEvento("[EVENTO] UNDO J1 APLICADO: " + nombre);
    panelJournal.marcarSistemaNormal();

    // 5) El archivo NO se agrega al árbol porque el commit nunca ocurrió
    refrescarTodo();

    JOptionPane.showMessageDialog(
            this,
            "J1 ejecutado.\nCREATE quedó PENDIENTE, ocurrió el fallo y recovery aplicó UNDO.",
            "J1 Real",
            JOptionPane.INFORMATION_MESSAGE
    );
}
    
private static class FolderIcon implements Icon {
    private final Color cuerpo;
    private final Color pestaña;

    public FolderIcon(Color cuerpo, Color pestaña) {
        this.cuerpo = cuerpo;
        this.pestaña = pestaña;
    }

    @Override
    public int getIconWidth() {
        return 18;
    }

    @Override
    public int getIconHeight() {
        return 16;
    }

    @Override
    public void paintIcon(Component c, Graphics g, int x, int y) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2.setColor(pestaña);
        g2.fillRoundRect(x + 2, y + 1, 7, 5, 4, 4);

        g2.setColor(cuerpo);
        g2.fillRoundRect(x + 1, y + 4, 15, 10, 4, 4);

        g2.setColor(new Color(255, 255, 255, 70));
        g2.drawRoundRect(x + 1, y + 4, 15, 10, 4, 4);

        g2.dispose();
    }
}

private static class FileIcon implements Icon {
    private final Color hoja;
    private final Color detalle;

    public FileIcon(Color hoja, Color detalle) {
        this.hoja = hoja;
        this.detalle = detalle;
    }

    @Override
    public int getIconWidth() {
        return 16;
    }

    @Override
    public int getIconHeight() {
        return 18;
    }

    @Override
    public void paintIcon(Component c, Graphics g, int x, int y) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2.setColor(hoja);
        g2.fillRoundRect(x + 2, y + 1, 11, 15, 3, 3);

        g2.setColor(new Color(203, 213, 225));
        int[] px = {x + 9, x + 13, x + 13};
        int[] py = {y + 1, y + 1, y + 5};
        g2.fillPolygon(px, py, 3);

        g2.setColor(detalle);
        g2.setStroke(new BasicStroke(1.3f));
        g2.drawLine(x + 4, y + 8, x + 11, y + 8);
        g2.drawLine(x + 4, y + 11, x + 10, y + 11);

        g2.setColor(new Color(255, 255, 255, 70));
        g2.drawRoundRect(x + 2, y + 1, 11, 15, 3, 3);

        g2.dispose();
    }
}

private void estilizarTree(JTree tree) {
    tree.setBackground(new Color(10, 24, 44));
    tree.setForeground(TemaUI.TEXTO);
    tree.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 12));
    tree.setRowHeight(24);
    tree.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8));
    tree.setShowsRootHandles(true);
    tree.setRootVisible(true);
    tree.putClientProperty("JTree.lineStyle", "None");
    tree.setOpaque(true);
    tree.setToggleClickCount(1);

    final Icon iconoCarpeta = new FolderIcon(new Color(34, 197, 94), new Color(187, 247, 208));
    final Icon iconoCarpetaSistema = new FolderIcon(new Color(56, 189, 248), new Color(186, 230, 253));
    final Icon iconoArchivo = new FileIcon(new Color(226, 232, 240), new Color(56, 189, 248));

    DefaultTreeCellRenderer renderer = new DefaultTreeCellRenderer() {
        @Override
        public Component getTreeCellRendererComponent(
                JTree tree, Object value, boolean sel, boolean expanded,
                boolean leaf, int row, boolean hasFocus) {

            super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);

            setBorder(BorderFactory.createEmptyBorder(2, 4, 2, 4));

            Color fondoNormal = new Color(10, 24, 44);
            Color fondoSeleccion = new Color(37, 99, 235);
            Color bordeSeleccion = new Color(96, 165, 250);

            if (sel) {
                setOpaque(true);
                setBackground(fondoSeleccion);
                setForeground(Color.WHITE);
                setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(bordeSeleccion, 1),
                        BorderFactory.createEmptyBorder(2, 4, 2, 4)
                ));
            } else {
                setOpaque(true);
                setBackground(fondoNormal);
                setForeground(TemaUI.TEXTO);
                setBorder(BorderFactory.createEmptyBorder(2, 4, 2, 4));
            }

            DefaultMutableTreeNode nodo = (DefaultMutableTreeNode) value;
            Object obj = nodo.getUserObject();

            if (obj instanceof NodoFS) {
                NodoFS n = (NodoFS) obj;

                if (n.getPadre() == null) {
                    setIcon(iconoCarpetaSistema);
                    setText("<html><span style='color:#7dd3fc; font-weight:700;'>/</span></html>");
                } else if (n.esDirectorio()) {
                    Directorio dir = (Directorio) n;
                    int hijos = dir.getHijos().tamano();

                    boolean esSistema = "system_files".equalsIgnoreCase(dir.getNombre());
                    setIcon(esSistema ? iconoCarpetaSistema : iconoCarpeta);

                    String colorNombre = sel ? "#ffffff" : (esSistema ? "#7dd3fc" : "#86efac");
                    String colorMeta = sel ? "#dbeafe" : "#93c5fd";

                    setText("<html><span style='color:" + colorNombre + "; font-weight:700;'>" +
                            dir.getNombre() +
                            "</span> <span style='color:" + colorMeta + "; font-size:10px;'>[" +
                            hijos + "]</span></html>");
                } else {
                    Archivo archivo = (Archivo) n;
                    setIcon(iconoArchivo);

                    String colorNombre = sel ? "#ffffff" : "#e2e8f0";
                    String colorMeta = sel ? "#dbeafe" : "#38bdf8";

                    setText("<html><span style='color:" + colorNombre + "; font-weight:600;'>" +
                            archivo.getNombre() +
                            "</span> <span style='color:" + colorMeta + "; font-size:10px;'>[" +
                            archivo.getTamanoEnBloques() + "]</span></html>");
                }
            } else {
                setIcon(null);
                setText(String.valueOf(obj));
            }

            return this;
        }
    };

    renderer.setBackgroundNonSelectionColor(new Color(10, 24, 44));
    renderer.setBackgroundSelectionColor(new Color(37, 99, 235));
    renderer.setTextNonSelectionColor(TemaUI.TEXTO);
    renderer.setTextSelectionColor(Color.WHITE);

    tree.setCellRenderer(renderer);
}



    private void estilizarScroll(JScrollPane scroll) {
        scroll.setBorder(BorderFactory.createLineBorder(TemaUI.BORDE, 1));
        scroll.getViewport().setBackground(TemaUI.CARD_2);
    }

    private void estilizarTabs(JTabbedPane tabs) {
        tabs.setBackground(TemaUI.CARD);
        tabs.setForeground(TemaUI.TEXTO);
        tabs.setFont(TemaUI.FUENTE_NORMAL);
        tabs.setBorder(BorderFactory.createLineBorder(TemaUI.BORDE, 1));
    }

    private void estilizarTabla(JTable tabla) {
        tabla.setBackground(TemaUI.CARD_2);
        tabla.setForeground(TemaUI.TEXTO);
        tabla.setGridColor(TemaUI.BORDE_SUAVE);
        tabla.setRowHeight(28);
        tabla.setFont(TemaUI.FUENTE_NORMAL);
        tabla.setSelectionBackground(TemaUI.ACENTO);
        tabla.setSelectionForeground(Color.WHITE);
        tabla.setFillsViewportHeight(true);
        tabla.setShowVerticalLines(false);

        tabla.getTableHeader().setBackground(TemaUI.CARD_3);
        tabla.getTableHeader().setForeground(TemaUI.TEXTO);
        tabla.getTableHeader().setFont(TemaUI.FUENTE_SUBTITULO);

        DefaultTableCellRenderer center = new DefaultTableCellRenderer();
        center.setHorizontalAlignment(SwingConstants.CENTER);
        center.setBackground(TemaUI.CARD_2);
        center.setForeground(TemaUI.TEXTO);

        for (int i = 0; i < tabla.getColumnModel().getColumnCount(); i++) {
            tabla.getColumnModel().getColumn(i).setCellRenderer(center);
        }
    }

    private void alSeleccionarNodo(TreeSelectionEvent e) {
        DefaultMutableTreeNode nodoSeleccionado = (DefaultMutableTreeNode) arbol.getLastSelectedPathComponent();

        if (nodoSeleccionado == null) {
            panelPropiedadesNodo.mostrarNodo(null);
            return;
        }

        Object obj = nodoSeleccionado.getUserObject();

        if (obj instanceof NodoFS) {
            panelPropiedadesNodo.mostrarNodo((NodoFS) obj);
        } else {
            panelPropiedadesNodo.mostrarNodo(null);
        }
    }

    private void crearArchivoDesdeGUI() {
        if (rbUsuario.isSelected()) {
            mostrarError("En modo Usuario no se permite crear archivos.");
            return;
        }

        String nombre = pedirTexto("Nombre del archivo:");
        if (nombre == null) return;

        Directorio directorioDestino = obtenerDirectorioDestinoDesdeSeleccion();
        if (buscarHijoPorNombreEnDirectorio(directorioDestino, nombre) != null) {
            mostrarError("Ya existe un nodo con ese nombre en el directorio seleccionado.");
            return;
        }

        String propietario = pedirTexto("Propietario:");
        if (propietario == null) return;

        Integer bloques = pedirEnteroPositivo("Cantidad de bloques:");
        if (bloques == null) return;

        Proceso proceso = crearProceso("PROC_CREATE_" + nombre);
        int posicion = encontrarPrimerBloqueLibre();
        SolicitudIO solicitud = registrarSolicitud(proceso, TipoOperacionIO.CREATE, nombre, posicion);

        proceso.setEstado(EstadoProceso.EJECUTANDO);
        gestorLocks.solicitarLock(solicitud);

        Archivo archivo = new Archivo(nombre, propietario, bloques);
        boolean asignado = sistema.getDisco().asignarBloquesAArchivo(archivo);

        if (!asignado) {
            proceso.setEstado(EstadoProceso.BLOQUEADO);
            panelLogs.agregarLog("[ERROR] Espacio insuficiente para crear archivo '" + nombre + "'.");
            refrescarTodo();
            return;
        }

        directorioDestino.agregarHijo(archivo);

        EntradaJournal entrada = journal.registrarOperacionPendiente(TipoOperacionIO.CREATE, archivo);
        entrada.setPrimerBloque(archivo.getPrimerBloque());
        journal.confirmarOperacion(entrada.getIdEntrada());

        cabezaActual = archivo.getPrimerBloque();

        gestorLocks.liberarLock(solicitud);
        proceso.setEstado(EstadoProceso.TERMINADO);

        panelLogs.agregarLog("[CREATE] Archivo '" + nombre + "' creado en '" +
                obtenerRutaVisible(directorioDestino) + "'. Propietario: " + propietario +
                " | Bloques: " + bloques + " | Primer bloque: " + archivo.getPrimerBloque());

        refrescarTodo();
    }

    private void crearDirectorioDesdeGUI() {
        if (rbUsuario.isSelected()) {
            mostrarError("En modo Usuario no se permite crear directorios.");
            return;
        }

        String nombre = pedirTexto("Nombre del directorio:");
        if (nombre == null) return;

        Directorio directorioDestino = obtenerDirectorioDestinoDesdeSeleccion();
        if (buscarHijoPorNombreEnDirectorio(directorioDestino, nombre) != null) {
            mostrarError("Ya existe un nodo con ese nombre en el directorio seleccionado.");
            return;
        }

        String propietario = pedirTexto("Propietario:");
        if (propietario == null) return;

        Proceso proceso = crearProceso("PROC_MKDIR_" + nombre);
        registrarSolicitud(proceso, TipoOperacionIO.CREATE, nombre, cabezaActual);

        proceso.setEstado(EstadoProceso.EJECUTANDO);

        Directorio dir = new Directorio(nombre, propietario);
        directorioDestino.agregarHijo(dir);

        proceso.setEstado(EstadoProceso.TERMINADO);

        panelLogs.agregarLog("[MKDIR] Directorio '" + nombre + "' creado en '" +
                obtenerRutaVisible(directorioDestino) + "'.");

        refrescarTodo();
    }

    private void leerNodoDesdeGUI() {
        NodoFS nodo = obtenerNodoSeleccionado();
        if (nodo == null) {
            mostrarError("Debes seleccionar un archivo o directorio.");
            return;
        }

        Proceso proceso = crearProceso("PROC_READ_" + nodo.getNombre());
        int posicion = (nodo instanceof Archivo) ? ((Archivo) nodo).getPrimerBloque() : cabezaActual;
        SolicitudIO solicitud = registrarSolicitud(proceso, TipoOperacionIO.READ, nodo.getNombre(), posicion);

        proceso.setEstado(EstadoProceso.EJECUTANDO);
        gestorLocks.solicitarLock(solicitud);

        panelLogs.agregarLog("[READ] Se leyó '" + nodo.getNombre() + "' desde '" +
                (nodo.getPadre() == null ? "/" : obtenerRutaVisible(nodo.getPadre())) + "'.");

        gestorLocks.liberarLock(solicitud);
        proceso.setEstado(EstadoProceso.TERMINADO);

        refrescarTodo();
    }

    private void renombrarNodoDesdeGUI() {
        if (rbUsuario.isSelected()) {
            mostrarError("En modo Usuario no se permite renombrar.");
            return;
        }

        NodoFS nodo = obtenerNodoSeleccionado();
        if (nodo == null || nodo.getPadre() == null) {
            mostrarError("Debes seleccionar un archivo o directorio válido para renombrar.");
            return;
        }

        String actual = nodo.getNombre();
        String nuevo = pedirTexto("Nuevo nombre:");
        if (nuevo == null) return;

        if (buscarHijoPorNombreEnDirectorio(nodo.getPadre(), nuevo) != null) {
            mostrarError("Ya existe otro nodo con ese nombre en el mismo directorio.");
            return;
        }

        Proceso proceso = crearProceso("PROC_RENAME_" + actual);
        registrarSolicitud(proceso, TipoOperacionIO.WRITE, actual, cabezaActual);

        proceso.setEstado(EstadoProceso.EJECUTANDO);
        nodo.setNombre(nuevo);
        proceso.setEstado(EstadoProceso.TERMINADO);

        panelLogs.agregarLog("[RENAME] '" + actual + "' renombrado a '" + nuevo + "'.");
        refrescarTodo();
    }
    
    private void actualizarPermisosPorRol() {
    boolean esAdmin = rbAdmin.isSelected();

    btnCargarCaso.setEnabled(esAdmin);
    btnEjecutarCaso.setEnabled(esAdmin);
    btnPausar.setEnabled(esAdmin);
    btnReanudar.setEnabled(esAdmin);
    btnPaso.setEnabled(esAdmin);
    btnInstantaneo.setEnabled(esAdmin);
    comboScheduler.setEnabled(esAdmin);
    comboVelocidad.setEnabled(esAdmin);

    refrescarEstiloRol();
}
    
    private void refrescarEstiloRol() {
    aplicarEstiloRadioRol(rbAdmin, rbAdmin.isSelected(), new Color(37, 99, 235));
    aplicarEstiloRadioRol(rbUsuario, rbUsuario.isSelected(), new Color(16, 185, 129));
}
    
    private void aplicarEstiloRadioRol(JRadioButton radio, boolean seleccionado, Color colorActivo) {
    if (seleccionado) {
        radio.setBackground(colorActivo);
        radio.setForeground(Color.WHITE);
        radio.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(colorActivo.brighter(), 1),
                BorderFactory.createEmptyBorder(10, 12, 10, 12)
        ));
        radio.setFont(new Font("Segoe UI", Font.BOLD, 15));
    } else {
        radio.setBackground(new Color(11, 25, 45));
        radio.setForeground(TemaUI.TEXTO);
        radio.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(30, 41, 59), 1),
                BorderFactory.createEmptyBorder(10, 12, 10, 12)
        ));
        radio.setFont(new Font("Segoe UI", Font.PLAIN, 14));
    }
}

    private void eliminarNodoDesdeGUI() {
        if (rbUsuario.isSelected()) {
            mostrarError("En modo Usuario no se permite eliminar.");
            return;
        }

        NodoFS nodo = obtenerNodoSeleccionado();
        if (nodo == null || nodo.getPadre() == null) {
            mostrarError("Debes seleccionar un archivo o directorio válido para eliminar.");
            return;
        }

        String nombre = nodo.getNombre();

        Proceso proceso = crearProceso("PROC_DELETE_" + nombre);
        int posicion = (nodo.esDirectorio() || !(nodo instanceof Archivo))
                ? cabezaActual
                : ((Archivo) nodo).getPrimerBloque();

        SolicitudIO solicitud = registrarSolicitud(proceso, TipoOperacionIO.DELETE, nombre, posicion);

        proceso.setEstado(EstadoProceso.EJECUTANDO);
        gestorLocks.solicitarLock(solicitud);

        Directorio padre = nodo.getPadre();

        if (nodo.esDirectorio()) {
            padre.eliminarHijoPorNombre(nombre);
            panelLogs.agregarLog("[DELETE] Directorio '" + nombre + "' eliminado.");
        } else {
            Archivo archivo = (Archivo) nodo;

            EntradaJournal entrada = journal.registrarOperacionPendiente(TipoOperacionIO.DELETE, archivo);
            entrada.setPrimerBloque(archivo.getPrimerBloque());

            cabezaActual = archivo.getPrimerBloque();
            sistema.getDisco().liberarBloquesDeArchivo(archivo);
            padre.eliminarHijoPorNombre(nombre);

            journal.confirmarOperacion(entrada.getIdEntrada());
            panelLogs.agregarLog("[DELETE] Archivo '" + nombre + "' eliminado.");
        }

        gestorLocks.liberarLock(solicitud);
        proceso.setEstado(EstadoProceso.TERMINADO);
        refrescarTodo();
    }

    private void simularFallo() {
    panelLogs.agregarLog("[FALLO] Se simuló una falla del sistema.");
    panelJournal.agregarEvento("[EVENTO] FALLO SIMULADO");
    panelJournal.marcarFalloSimulado();

    journal.recuperarOperacionesPendientes(sistema.getDisco());

    panelLogs.agregarLog("[RECOVERY] Recuperación ejecutada desde journal.");
    panelJournal.agregarEvento("[EVENTO] RECOVERY EJECUTADA");
    panelJournal.marcarSistemaNormal();

    refrescarTodo();

    JOptionPane.showMessageDialog(this,
            "Fallo simulado y recuperación ejecutada desde el journal.",
            "Simulación",
            JOptionPane.INFORMATION_MESSAGE);
}

    private Proceso crearProceso(String nombre) {
        Proceso proceso = new Proceso(siguientePid++, nombre);
        proceso.setEstado(EstadoProceso.LISTO);
        procesosSistema.agregar(proceso);
        return proceso;
    }

    private SolicitudIO registrarSolicitud(Proceso proceso, TipoOperacionIO tipo, String nombreArchivo, int posicionDisco) {
        if (posicionDisco < 0) {
            posicionDisco = cabezaActual;
        }

        SolicitudIO solicitud = new SolicitudIO(
                siguienteSolicitudId++,
                proceso,
                tipo,
                nombreArchivo,
                posicionDisco
        );

        historialSolicitudes.agregar(solicitud);
        actualizarCabezalSegunScheduler();
        return solicitud;
    }

    private void actualizarCabezalSegunScheduler() {
        if (historialSolicitudes.estaVacia()) {
            return;
        }

        ListaEnlazada<SolicitudIO> orden;
        String politica = comboScheduler.getSelectedItem().toString();

        switch (politica) {
            case "SSTF":
                orden = new PlanificadorSSTF(cabezaActual).planificar(historialSolicitudes);
                break;
            case "SCAN":
                orden = new PlanificadorSCAN(cabezaActual, true).planificar(historialSolicitudes);
                break;
            case "C-SCAN":
                orden = new PlanificadorCSCAN(cabezaActual, true).planificar(historialSolicitudes);
                break;
            default:
                orden = new PlanificadorFIFO(cabezaActual).planificar(historialSolicitudes);
                break;
        }

        if (!orden.estaVacia()) {
            cabezaActual = orden.obtener(orden.tamano() - 1).getPosicionDisco();
        }
    }

    private int encontrarPrimerBloqueLibre() {
        for (int i = 0; i < sistema.getDisco().getCantidadBloques(); i++) {
            if (sistema.getDisco().obtenerBloque(i).estaLibre()) {
                return i;
            }
        }
        return -1;
    }

    private NodoFS obtenerNodoSeleccionado() {
        DefaultMutableTreeNode nodoSeleccionado = (DefaultMutableTreeNode) arbol.getLastSelectedPathComponent();
        if (nodoSeleccionado == null) {
            return sistema.getRoot();
        }

        Object obj = nodoSeleccionado.getUserObject();
        if (obj instanceof NodoFS) {
            return (NodoFS) obj;
        }

        return sistema.getRoot();
    }

    private Directorio obtenerDirectorioDestinoDesdeSeleccion() {
        NodoFS seleccionado = obtenerNodoSeleccionado();

        if (seleccionado == null) {
            return sistema.getRoot();
        }

        if (seleccionado.esDirectorio()) {
            return (Directorio) seleccionado;
        }

        if (seleccionado.getPadre() != null) {
            return seleccionado.getPadre();
        }

        return sistema.getRoot();
    }

    private NodoFS buscarHijoPorNombreEnDirectorio(Directorio dir, String nombre) {
        for (int i = 0; i < dir.getHijos().tamano(); i++) {
            NodoFS hijo = dir.getHijos().obtener(i);
            if (hijo.getNombre().equals(nombre)) {
                return hijo;
            }
        }
        return null;
    }

    private String obtenerRutaVisible(Directorio dir) {
        if (dir == null || dir.getPadre() == null) {
            return "/";
        }

        String ruta = dir.getRutaCompleta();
        if (ruta.startsWith("/root")) {
            ruta = ruta.substring(5);
        }
        return ruta.isEmpty() ? "/" : ruta;
    }

    private String pedirTexto(String mensaje) {
        String valor = JOptionPane.showInputDialog(this, mensaje);
        if (valor == null) return null;

        valor = valor.trim();
        if (valor.isEmpty()) {
            mostrarError("El valor no puede estar vacío.");
            return null;
        }

        return valor;
    }

    private Integer pedirEnteroPositivo(String mensaje) {
        String valor = JOptionPane.showInputDialog(this, mensaje);
        if (valor == null) return null;

        valor = valor.trim();
        if (valor.isEmpty()) {
            mostrarError("Debes introducir un número.");
            return null;
        }

        try {
            int numero = Integer.parseInt(valor);
            if (numero <= 0) {
                mostrarError("El número debe ser mayor que cero.");
                return null;
            }
            return numero;
        } catch (NumberFormatException ex) {
            mostrarError("Debes introducir un número válido.");
            return null;
        }
    }

    private void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(this, mensaje, "Error", JOptionPane.ERROR_MESSAGE);
    }

    private void refrescarTodo() {
        actualizarArbol();
        actualizarTablaAsignacion();
        actualizarEstadisticas();
        panelDisco.repaint();
        panelJournal.refrescar(journal);
        panelProcesos.refrescar(procesosSistema, solicitudActiva != null ? solicitudActiva.getProceso() : null);
        panelLocksActivos.refrescar(gestorLocks);
        panelSolicitudesIO.refrescar(historialSolicitudes);
        panelBarraCabezal.actualizar(cabezaActual, sistema.getDisco().getCantidadBloques(), comboScheduler.getSelectedItem().toString());
        if (ventanaProcesos != null && ventanaProcesos.isDisplayable()) {
    SwingUtilities.invokeLater(() -> {
        ventanaProcesos.refrescar(
                procesosSistema,
                solicitudActiva != null ? solicitudActiva.getProceso() : null
        );
        ventanaProcesos.revalidate();
        ventanaProcesos.repaint();
    });

}
    }

    private void actualizarArbol() {
        DefaultMutableTreeNode raizVisual = construirNodoVisual(sistema.getRoot());
        modeloArbol.setRoot(raizVisual);
        modeloArbol.reload();

        for (int i = 0; i < arbol.getRowCount(); i++) {
            arbol.expandRow(i);
        }
    }

    private DefaultMutableTreeNode construirNodoVisual(NodoFS nodo) {
        DefaultMutableTreeNode visual = new DefaultMutableTreeNode(nodo);

        if (nodo.esDirectorio()) {
            Directorio dir = (Directorio) nodo;
            for (int i = 0; i < dir.getHijos().tamano(); i++) {
                visual.add(construirNodoVisual(dir.getHijos().obtener(i)));
            }
        }

        return visual;
    }

    private void actualizarTablaAsignacion() {
        modeloTablaAsignacion.setRowCount(0);
        cargarArchivosDesdeDirectorio(sistema.getRoot());

        if (tablaAsignacion.getColumnModel().getColumnCount() > 4) {
            tablaAsignacion.getColumnModel().getColumn(4).setCellRenderer(new DefaultTableCellRenderer() {
                @Override
                public Component getTableCellRendererComponent(
                        JTable table, Object value, boolean isSelected,
                        boolean hasFocus, int row, int column) {

                    super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                    setHorizontalAlignment(CENTER);

                    String hex = String.valueOf(value);
                    setText(hex);

                    if (!isSelected) {
                        try {
                            setBackground(Color.decode(hex));
                            setForeground(Color.WHITE);
                        } catch (Exception e) {
                            setBackground(TemaUI.CARD_2);
                            setForeground(TemaUI.TEXTO);
                        }
                    }

                    return this;
                }
            });
        }
    }

    private void cargarArchivosDesdeDirectorio(Directorio dir) {
        for (int i = 0; i < dir.getHijos().tamano(); i++) {
            NodoFS hijo = dir.getHijos().obtener(i);

            if (hijo.esDirectorio()) {
                cargarArchivosDesdeDirectorio((Directorio) hijo);
            } else {
                Archivo archivo = (Archivo) hijo;

                modeloTablaAsignacion.addRow(new Object[]{
                        archivo.getNombre(),
                        archivo.getPropietario(),
                        archivo.getTamanoEnBloques(),
                        archivo.getPrimerBloque(),
                        colorHexPorArchivo(archivo.getNombre())
                });
            }
        }
    }

    private String colorHexPorArchivo(String nombre) {
        Color c = colorPorArchivo(nombre);
        return String.format("#%02X%02X%02X", c.getRed(), c.getGreen(), c.getBlue());
    }

    private Color colorPorArchivo(String nombreArchivo) {
        if (nombreArchivo == null) {
            return TemaUI.BLOQUE_LIBRE;
        }

        int hash = Math.abs(nombreArchivo.hashCode());
        Color[] paleta = new Color[]{
                new Color(239, 68, 68),
                new Color(249, 115, 22),
                new Color(245, 158, 11),
                new Color(34, 197, 94),
                new Color(20, 184, 166),
                new Color(59, 130, 246),
                new Color(99, 102, 241),
                new Color(168, 85, 247),
                new Color(236, 72, 153)
        };
        return paleta[hash % paleta.length];
    }

    private void actualizarEstadisticas() {
        int libres = sistema.getDisco().contarBloquesLibres();
        int total = sistema.getDisco().getCantidadBloques();
        int ocupados = total - libres;

        lblBloquesLibres.setText("Bloques libres: " + libres);
        lblBloquesLibres.setBackground(new Color(8, 64, 48));

        lblBloquesOcupados.setText("Bloques ocupados: " + ocupados);
        lblBloquesOcupados.setBackground(new Color(78, 33, 33));

        lblSchedulerActivo.setText("Política: " + comboScheduler.getSelectedItem());
        lblSchedulerActivo.setBackground(new Color(29, 43, 68));

        lblCabezal.setText("<html><div style='padding:6px'><span style='color:#38bdf8;'>Cabezal</span><br><span style='font-size:22px; color:#ebf1fa; font-weight:bold;'>"
                + cabezaActual + "</span></div></html>");

        lblSolicitudes.setText("<html><div style='padding:6px'><span style='color:#38bdf8;'>Solicitudes</span><br><span style='font-size:22px; color:#ebf1fa; font-weight:bold;'>"
                + historialSolicitudes.tamano() + "</span></div></html>");
    }
    
   private void cargarCasoDesdeJSON() {
    JFileChooser chooser = new JFileChooser();
    chooser.setDialogTitle("Seleccionar caso de prueba JSON");
    chooser.setFileFilter(new FileNameExtensionFilter("Archivos JSON", "json"));

    int resultado = chooser.showOpenDialog(this);
    if (resultado != JFileChooser.APPROVE_OPTION) {
        return;
    }

    try {
        String ruta = chooser.getSelectedFile().getAbsolutePath();

        CargadorCasoJSON cargador = new CargadorCasoJSON();
        CasoPrueba caso = cargador.cargarDesdeArchivo(ruta);

        AplicadorCasoPrueba aplicador = new AplicadorCasoPrueba(sistema);
        ContextoCasoPrueba contexto = aplicador.aplicar(caso);

        this.casoPruebaActual = caso;
        this.contextoCasoActual = contexto;
        this.cabezaActual = contexto.getCabezaInicial();

        vaciarLista(solicitudesActualesCaso);
        vaciarLista(procesosSistema);

        for (int i = 0; i < contexto.getSolicitudes().tamano(); i++) {
            SolicitudIO solicitud = contexto.getSolicitudes().obtener(i);

            solicitudesActualesCaso.agregar(solicitud);
            historialSolicitudes.agregar(solicitud);
            procesosSistema.agregar(solicitud.getProceso());
        }

       panelLogs.agregarLog("[CASO] Caso cargado: " + caso.getTestId());
panelLogs.agregarLog("[CASO] Cabezal inicial: " + contexto.getCabezaInicial());
panelLogs.agregarLog("[CASO] Dirección: " + (contexto.isHaciaArriba() ? "UP" : "DOWN"));
panelLogs.agregarLog("[CASO] Solicitudes cargadas: " + contexto.getSolicitudes().tamano());

panelJournal.agregarEvento("[EVENTO] CASO CARGADO: " + caso.getTestId());

        refrescarTodo();

    } catch (Exception ex) {
        mostrarError("Error al cargar el caso JSON: " + ex.getMessage());
    }
}

private void ejecutarCasoActual() {
    if (contextoCasoActual == null) {
        mostrarError("Primero debes cargar un caso JSON.");
        return;
    }

    try {
        if (!simulacionPreparada && !simulacionEnCurso) {
            prepararSimulacionPasoAPaso();
        }
        iniciarSimulacion();

    } catch (Exception ex) {
        mostrarError("Error al ejecutar el caso: " + ex.getMessage());
    }
}

private void prepararSimulacionPasoAPaso() {
    String politica = comboScheduler.getSelectedItem().toString();

    vaciarLista(ordenSimulacionActual);

    ListaEnlazada<SolicitudIO> orden = simuladorPoliticas.planificar(contextoCasoActual, politica);
    for (int i = 0; i < orden.tamano(); i++) {
        ordenSimulacionActual.agregar(orden.obtener(i));
    }

    indiceSimulacion = 0;
    solicitudActiva = null;
    simulacionPreparada = true;
    simulacionEnCurso = false;

    cabezaActual = contextoCasoActual.getCabezaInicial();

    vaciarLista(procesosSistema);
    for (int i = 0; i < contextoCasoActual.getSolicitudes().tamano(); i++) {
        SolicitudIO s = contextoCasoActual.getSolicitudes().obtener(i);
        s.getProceso().setEstado(EstadoProceso.LISTO);
        procesosSistema.agregar(s.getProceso());
    }

    panelLogs.agregarLog("[SIM] Simulación preparada.");
    panelLogs.agregarLog("[SIM] Política: " + politica);
    panelLogs.agregarLog("[SIM] Orden planificado: " + simuladorPoliticas.formatearOrdenPosiciones(ordenSimulacionActual));

    refrescarTodo();
}


private void iniciarSimulacion() {
    if (!simulacionPreparada) {
        mostrarError("Primero debes preparar o cargar un caso.");
        return;
    }

    if (simulacionEnCurso) {
        return;
    }

    int delay = obtenerDelaySimulacion();

    timerSimulacion = new Timer(delay, new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            ejecutarTickSimulacion();
        }
    });

    simulacionEnCurso = true;
    timerSimulacion.start();
    panelLogs.agregarLog("[SIM] Simulación iniciada/reanudada a velocidad " + comboVelocidad.getSelectedItem() + ".");
}

private void pausarSimulacion() {
    if (timerSimulacion != null && timerSimulacion.isRunning()) {
        timerSimulacion.stop();
        simulacionEnCurso = false;
        panelLogs.agregarLog("[SIM] Simulación pausada.");
    }
}



private void ejecutarPasoManual() {
    if (!simulacionPreparada) {
        if (contextoCasoActual == null) {
            mostrarError("Primero debes cargar un caso JSON.");
            return;
        }
        prepararSimulacionPasoAPaso();
    }

    if (timerSimulacion != null && timerSimulacion.isRunning()) {
        timerSimulacion.stop();
        simulacionEnCurso = false;
    }

    ejecutarTickSimulacion();
}

private void ejecutarTickSimulacion() {
    if (!simulacionPreparada) {
        return;
    }

    if (solicitudActiva == null) {
        if (indiceSimulacion >= ordenSimulacionActual.tamano()) {
            finalizarSimulacionPasoAPaso();
            return;
        }

        solicitudActiva = ordenSimulacionActual.obtener(indiceSimulacion);

        Proceso proceso = solicitudActiva.getProceso();
        proceso.setEstado(EstadoProceso.EJECUTANDO);

        gestorLocks.solicitarLock(solicitudActiva);

        panelLogs.agregarLog("[SIM] Atendiendo solicitud #" + solicitudActiva.getIdSolicitud()
                + " | " + solicitudActiva.getTipoOperacion()
                + " | Archivo: " + solicitudActiva.getNombreArchivo()
                + " | Posición: " + solicitudActiva.getPosicionDisco());

        panelJournal.agregarEvento("[EVENTO] ACCESO: " + solicitudActiva.getNombreArchivo()
                + " @ " + solicitudActiva.getPosicionDisco());

        refrescarTodo();
        return;
    }

    cabezaActual = solicitudActiva.getPosicionDisco();

    gestorLocks.liberarLock(solicitudActiva);

    Proceso proceso = solicitudActiva.getProceso();
    proceso.setEstado(EstadoProceso.TERMINADO);

    panelLogs.agregarLog("[SIM] Solicitud completada. Cabezal movido a " + cabezaActual + ".");

    solicitudActiva = null;
    indiceSimulacion++;

    refrescarTodo();

    if (indiceSimulacion >= ordenSimulacionActual.tamano()) {
        finalizarSimulacionPasoAPaso();
    }
}

private void finalizarSimulacionPasoAPaso() {
    if (timerSimulacion != null) {
        timerSimulacion.stop();
    }

    simulacionEnCurso = false;
    simulacionPreparada = false;

    int movimientoTotal = simuladorPoliticas.calcularMovimientoTotal(
            contextoCasoActual.getCabezaInicial(),
            ordenSimulacionActual
    );

    panelLogs.agregarLog("[SIM] Simulación finalizada.");
    panelLogs.agregarLog("[SIM] Movimiento total: " + movimientoTotal);

    refrescarTodo();
}

private int obtenerDelaySimulacion() {
    String velocidad = comboVelocidad.getSelectedItem().toString();

    switch (velocidad) {
        case "Lenta":
            return 1500;
        case "Rápida":
            return 450;
        case "Media":
        default:
            return 850;
    }
}

private <T> void vaciarLista(ListaEnlazada<T> lista) {
    while (lista.tamano() > 0) {
        lista.eliminar(0);
    }
}
private void limpiarDiscoCompleto() {
    try {
        if (timerSimulacion != null) {
            timerSimulacion.stop();
        }

        simulacionEnCurso = false;
        simulacionPreparada = false;
        solicitudActiva = null;
        indiceSimulacion = 0;

        sistema.getDisco().limpiarDisco();

        vaciarLista(historialSolicitudes);
        vaciarLista(solicitudesActualesCaso);
        vaciarLista(ordenSimulacionActual);
        vaciarLista(procesosSistema);

        while (sistema.getRoot().getHijos().tamano() > 0) {
            NodoFS hijo = sistema.getRoot().getHijos().obtener(0);
            sistema.getRoot().eliminarHijoPorNombre(hijo.getNombre());
        }

        this.casoPruebaActual = null;
        this.contextoCasoActual = null;
        this.cabezaActual = 0;

        panelLogs.limpiar();
        panelJournal.limpiarEventos();
        panelJournal.marcarSistemaNormal();

        panelLogs.agregarLog("[RESET] Disco, árbol, solicitudes y procesos reiniciados.");
        refrescarTodo();

    } catch (Exception ex) {
        mostrarError("Error al limpiar el disco: " + ex.getMessage());
    }
}


private JPanel crearPanelBotonCentral(JButton boton) {
    JPanel panel = new JPanel(new GridBagLayout());
    panel.setOpaque(false);
    panel.add(boton);
    return panel;
}

private void abrirPestanaCentro(String nombrePestana) {
    if (tabsCentro == null) {
        return;
    }

    for (int i = 0; i < tabsCentro.getTabCount(); i++) {
        if (tabsCentro.getTitleAt(i).equalsIgnoreCase(nombrePestana)) {
            tabsCentro.setSelectedIndex(i);
            return;
        }
    }
}
private JMenuItem crearMenuTitulo(String texto) {
    JMenuItem item = new JMenuItem(texto);
    item.setEnabled(false);
    item.setFont(new Font("Segoe UI", Font.BOLD, 13));
    item.setBackground(new Color(15, 23, 42));
    item.setForeground(new Color(125, 211, 252));
    item.setBorder(BorderFactory.createEmptyBorder(8, 12, 6, 12));
    return item;
}
}