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

public class VentanaPrincipal extends JFrame {
    private final SistemaArchivos sistema;
    private final JournalManager journal;
    private final GestorLocks gestorLocks;

    private final ListaEnlazada<Proceso> procesosSistema;
    private final ListaEnlazada<SolicitudIO> historialSolicitudes;

    private int siguientePid = 1;
    private int siguienteSolicitudId = 1;
    private int cabezaActual = 0;

    private JTree arbol;
    private DefaultTreeModel modeloArbol;

    private JTable tablaAsignacion;
    private DefaultTableModel modeloTablaAsignacion;

    private PanelDisco panelDisco;
    private PanelLogs panelLogs;
    private PanelJournal panelJournal;
    private PanelProcesos panelProcesos;
    private PanelPropiedadesNodo panelPropiedadesNodo;
    private PanelLocksActivos panelLocksActivos;
    private PanelSolicitudesIO panelSolicitudesIO;
    private PanelBarraCabezal panelBarraCabezal;

    private JComboBox<String> comboScheduler;

    private JLabel lblBloquesLibres;
    private JLabel lblBloquesOcupados;
    private JLabel lblSchedulerActivo;
    private JLabel lblCabezal;
    private JLabel lblSolicitudes;

    private JRadioButton rbAdmin;
    private JRadioButton rbUsuario;

    public VentanaPrincipal(SistemaArchivos sistema, JournalManager journal) {
        this.sistema = sistema;
        this.journal = journal;
        this.gestorLocks = new GestorLocks();

        this.procesosSistema = new ListaEnlazada<>();
        this.historialSolicitudes = new ListaEnlazada<>();

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
        JPanel header = new JPanel(new BorderLayout(10, 10));
        header.setBackground(new Color(7, 16, 32));
        header.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(TemaUI.BORDE_SUAVE, 1),
                BorderFactory.createEmptyBorder(12, 16, 12, 16)
        ));

        JLabel titulo = new JLabel("Simulador de Sistema de Archivos");
        titulo.setForeground(new Color(240, 248, 255));
        titulo.setFont(TemaUI.FUENTE_TITULO);

        JPanel izquierda = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 0));
        izquierda.setOpaque(false);
        izquierda.add(titulo);

        JPanel derecha = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        derecha.setOpaque(false);

        comboScheduler = new JComboBox<>(new String[]{"FIFO", "SSTF", "SCAN", "C-SCAN"});
        estilizarCombo(comboScheduler);

        JButton btnCrearArchivo = crearBoton("Crear Archivo");
        JButton btnCrearDirectorio = crearBoton("Crear Directorio");
        JButton btnLeer = crearBoton("Leer");
        JButton btnRenombrar = crearBoton("Renombrar");
        JButton btnEliminar = crearBoton("Eliminar");
        JButton btnSimularFallo = crearBotonPeligro("Simular Fallo");

        btnCrearArchivo.setBackground(TemaUI.BOTON_AZUL);
        btnCrearDirectorio.setBackground(TemaUI.BOTON_VERDE);
        btnLeer.setBackground(new Color(8, 145, 178));
        btnRenombrar.setBackground(TemaUI.BOTON_MORADO);
        btnEliminar.setBackground(new Color(153, 27, 27));

        btnCrearArchivo.addActionListener(e -> crearArchivoDesdeGUI());
        btnCrearDirectorio.addActionListener(e -> crearDirectorioDesdeGUI());
        btnLeer.addActionListener(e -> leerNodoDesdeGUI());
        btnRenombrar.addActionListener(e -> renombrarNodoDesdeGUI());
        btnEliminar.addActionListener(e -> eliminarNodoDesdeGUI());
        btnSimularFallo.addActionListener(e -> simularFallo());

        JLabel lblScheduler = new JLabel("Scheduler:");
        lblScheduler.setForeground(TemaUI.TEXTO);
        lblScheduler.setFont(TemaUI.FUENTE_SUBTITULO);

        derecha.add(lblScheduler);
        derecha.add(comboScheduler);
        derecha.add(btnCrearArchivo);
        derecha.add(btnCrearDirectorio);
        derecha.add(btnLeer);
        derecha.add(btnRenombrar);
        derecha.add(btnEliminar);
        derecha.add(btnSimularFallo);

        header.add(izquierda, BorderLayout.WEST);
        header.add(derecha, BorderLayout.EAST);

        return header;
    }

    private JSplitPane crearCentro() {
        JSplitPane splitPrincipal = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPrincipal.setBorder(null);
        splitPrincipal.setDividerSize(6);
        splitPrincipal.setContinuousLayout(true);
        splitPrincipal.setLeftComponent(crearSidebarIzquierda());
        splitPrincipal.setRightComponent(crearZonaDerechaGrande());
        splitPrincipal.setResizeWeight(0.25);
        return splitPrincipal;
    }

    private JPanel crearSidebarIzquierda() {
        JPanel sidebar = new JPanel(new BorderLayout(10, 10));
        sidebar.setBackground(TemaUI.FONDO_APP);

        JPanel superior = new JPanel(new GridLayout(3, 1, 10, 10));
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
        DefaultMutableTreeNode raizVisual = new DefaultMutableTreeNode(sistema.getRoot());
        modeloArbol = new DefaultTreeModel(raizVisual);
        arbol = new JTree(modeloArbol);
        estilizarTree(arbol);
        arbol.addTreeSelectionListener(this::alSeleccionarNodo);

        JScrollPane scrollArbol = new JScrollPane(arbol);
        estilizarScroll(scrollArbol);
        cardArbol.getContenido().add(scrollArbol, BorderLayout.CENTER);

        PanelCard cardPropiedades = new PanelCard("Propiedades del Nodo");
        panelPropiedadesNodo = new PanelPropiedadesNodo();
        cardPropiedades.getContenido().add(panelPropiedadesNodo, BorderLayout.CENTER);

        superior.add(cardControles);
        superior.add(cardArbol);
        superior.add(cardPropiedades);

        sidebar.add(superior, BorderLayout.CENTER);
        return sidebar;
    }

    private JSplitPane crearZonaDerechaGrande() {
        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        split.setBorder(null);
        split.setDividerSize(6);
        split.setContinuousLayout(true);
        split.setLeftComponent(crearZonaCentral());
        split.setRightComponent(crearSidebarDerecha());
        split.setResizeWeight(0.74);
        return split;
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

        JTabbedPane tabs = new JTabbedPane();
        estilizarTabs(tabs);

        JPanel panelDiscoCompleto = new JPanel(new BorderLayout(10, 10));
        panelDiscoCompleto.setOpaque(false);

        panelBarraCabezal = new PanelBarraCabezal();
        panelBarraCabezal.setPreferredSize(new Dimension(100, 50));

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

        tabs.addTab("Disco", panelDiscoCompleto);
        tabs.addTab("Asignación", cardTabla);
        tabs.addTab("Solicitudes", cardSolicitudes);

        zona.add(cardEstadisticas, BorderLayout.NORTH);
        zona.add(tabs, BorderLayout.CENTER);

        return zona;
    }

    private JPanel crearSidebarDerecha() {
        JPanel sidebar = new JPanel(new GridLayout(3, 1, 10, 10));
        sidebar.setBackground(TemaUI.FONDO_APP);

        PanelCard cardJournal = new PanelCard("Journal");
        panelJournal = new PanelJournal();
        cardJournal.getContenido().add(panelJournal, BorderLayout.CENTER);

        PanelCard cardLocks = new PanelCard("Locks Activos");
        panelLocksActivos = new PanelLocksActivos();
        cardLocks.getContenido().add(panelLocksActivos, BorderLayout.CENTER);

        PanelCard cardProcesos = new PanelCard("Cola de Procesos");
        panelProcesos = new PanelProcesos();
        cardProcesos.getContenido().add(panelProcesos, BorderLayout.CENTER);

        sidebar.add(cardJournal);
        sidebar.add(cardLocks);
        sidebar.add(cardProcesos);

        return sidebar;
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
        radio.setBackground(TemaUI.CARD_2);
        radio.setForeground(TemaUI.TEXTO);
        radio.setFont(TemaUI.FUENTE_NORMAL);
        radio.setBorder(BorderFactory.createEmptyBorder(8, 10, 8, 10));
        radio.setFocusPainted(false);
    }

    private void estilizarTree(JTree tree) {
        tree.setBackground(TemaUI.CARD_2);
        tree.setForeground(TemaUI.TEXTO);
        tree.setFont(new java.awt.Font("Segoe UI", java.awt.Font.PLAIN, 14));
        tree.setRowHeight(30);
        tree.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        tree.setShowsRootHandles(true);
        tree.setRootVisible(true);

        DefaultTreeCellRenderer renderer = new DefaultTreeCellRenderer() {
            @Override
            public Component getTreeCellRendererComponent(
                    JTree tree, Object value, boolean sel, boolean expanded,
                    boolean leaf, int row, boolean hasFocus) {

                super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);

                setBackgroundSelectionColor(TemaUI.ACENTO);
                setTextSelectionColor(Color.WHITE);
                setBackgroundNonSelectionColor(TemaUI.CARD_2);
                setTextNonSelectionColor(TemaUI.TEXTO);

                setOpenIcon(null);
                setClosedIcon(null);
                setLeafIcon(null);

                DefaultMutableTreeNode nodo = (DefaultMutableTreeNode) value;
                Object obj = nodo.getUserObject();

                if (obj instanceof NodoFS) {
                    NodoFS n = (NodoFS) obj;

                    if (n.getPadre() == null) {
                        setText("<html><span style='color:#7dd3fc;'>📁 /</span></html>");
                    } else if (n.esDirectorio()) {
                        setText("<html><span style='color:#86efac;'>📁 " + n.getNombre() + "</span></html>");
                    } else {
                        Archivo archivo = (Archivo) n;
                        setText("<html><span style='color:#e9f5ff;'>📄 " + archivo.getNombre() +
                                "</span> <span style='color:#86efac;'>: " +
                                archivo.getTamanoEnBloques() + " bloques</span></html>");
                    }
                } else {
                    setText(String.valueOf(obj));
                    setForeground(TemaUI.TEXTO);
                }

                return this;
            }
        };

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
        panelJournal.marcarFalloSimulado();

        journal.recuperarOperacionesPendientes(sistema.getDisco());

        panelLogs.agregarLog("[RECOVERY] Recuperación ejecutada desde journal.");
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
        panelProcesos.refrescar(procesosSistema);
        panelLocksActivos.refrescar(gestorLocks);
        panelSolicitudesIO.refrescar(historialSolicitudes);
        panelBarraCabezal.actualizar(cabezaActual, sistema.getDisco().getCantidadBloques(), comboScheduler.getSelectedItem().toString());
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
}