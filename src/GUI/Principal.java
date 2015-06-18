package GUI;

import Clases.Atributo;
import Clases.Relacion;
import Clases.RelacionRelacionada;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyVetoException;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Bases2
 */
public class Principal extends JFrame {

    PanelEscritorio panelPrincipal;
    JMenuBar menu;
    JMenu menuArchivo, menuEditar;
    JMenuItem menuArchivoImportar, menuArchivoExportar, menuArchivoNuevaVentana, menuArchivoSalir;
    JMenuItem menuEditarLimpiar, menuEditarAgregarTabla, menuEditarEliminarTabla, menuEditarCambiarNombreTabla;
    JMenuItem clicmenuAgregarTabla;
    Graphics gra;
    String Script;
    File direccionScript;
    LinkedList<VentanaInterna> tablas = new LinkedList<>();
    LinkedList<RelacionRelacionada> relacionesEntreRelaciones = new LinkedList<>();
    LinkedList<RelacionRelacionada> relaciones1A1 = new LinkedList<>();
    int numeroTablas = 0;
    LinkedList<Integer> puntos = new LinkedList<>();
    LinkedList<Integer> puntos1A1 = new LinkedList<>();
    JPopupMenu clicmenu;
    int xNV = 10, yNV = 30;
    Color colorFondo = Color.LIGHT_GRAY;
    Color colorLineas = Color.DARK_GRAY;

    class PanelEscritorio extends JDesktopPane {

        @Override
        public void paint(Graphics g) {
            super.paintComponent(g);
            setBackground(colorFondo);

            gra = getGraphics();
        }
    }
    
    public class VentanaInterna extends JInternalFrame implements ComponentListener, KeyListener {

        private JTable tabla;
        private DefaultTableModel modelo;
        private Relacion tab;
        private int cant = 0;
        private int Rn = 0;
        private int R1 = 0;
        private LinkedList<Integer> puntosRn = new LinkedList<>();
        private LinkedList<Integer> puntosR1 = new LinkedList<>();
        
        public VentanaInterna(String title, Relacion tab) {
            super(title);
            Object[][] data = {};
            String[] col = {"Nombre"};//, "Apellido"};
            tabla = new JTable();
            modelo = new DefaultTableModel(data, col);
            tabla.setModel(modelo);
            add(tabla);
            addComponentListener(this);
//            addKeyListener(this);
            setVisible(true);
            setResizable(true);
//            tabla.set
//            setClosable(true);
            tabla.addKeyListener(this);
            
            this.tab = tab;
            ver();
            
        }
        
        private void ver() {
            vaciar();
            LinkedList<Atributo> atribs = tab.getAtributos();
            for (Atributo atr : atribs) {
                modelo.insertRow(cant, new Object[]{});
                modelo.setValueAt(atr.getNombre()+ " " + atr.getLlaves() , cant++, 0);
                //modelo.setValueAt(, cant++, 1);
                
            }
            modelo.insertRow(cant, new Object[]{});
            modelo.setValueAt( " " , cant++, 0);
            
            int alto = cant > 2 ? (cant * 20) + 20 : 70;
            setSize(160, alto);
        }
        
        public void RefrescarTabla(){
            cant = 0;
            ver();            
        }

        public void setTab(Relacion tab) {
            this.tab = tab;
        }
        
        public Relacion getTab() {
            return tab;
        }

        public void vaciar() {
            int rows = modelo.getRowCount();
            for (int i = 0; i < rows; i++) {
                modelo.removeRow(0);
            }
        }

        public DefaultTableModel getModelo() {
            return modelo;
        }

        @Override
        public void componentResized(ComponentEvent e) {
            
        }

        @Override
        public void componentMoved(ComponentEvent e) {
            Repintar();
        }

        @Override
        public void componentShown(ComponentEvent e) {

        }

        @Override
        public void componentHidden(ComponentEvent e) {

        }

        public int getPuntosR1(int index) {
            return puntosR1.get(index);
        }

        public int getPuntosRn(int index) {
            return puntosRn.get(index);
        }
        
        public int getR1() {
            return R1;
        }

        public int getRn() {
            return Rn;
        }
        
        public void addRn(){
            Rn++;
            puntosRn.add(0);
        }
        
        public void addR1(){
            R1++;
            puntosR1.add(0);
        }

        public void addRn(int n){
            Rn += n;
            for (int i = 0; i < n; i++) {
                puntosRn.add(0);
            }
        }
        
        public void addR1(int n){
            R1 += n;
            for (int i = 0; i < n; i++) {
                puntosR1.add(0);
            }
        }
        
        public void removeRn(){
            Rn--;
            puntosRn.removeLast();
        }
        
        public void removeR1(){
            R1--;
            puntosR1.removeLast();
        }
        
        public void removeRn(int n){
            Rn -= n;
            puntosRn.remove(n);
        }
        
        public void removeR1(int n){
            R1 -= n;
            puntosR1.remove(n);
        }

        @Override
        public void keyTyped(KeyEvent e) {
            int key = e.getKeyChar();
            if (key == 10) {
                LinkedList <Atributo> listaux = new LinkedList<>();
                int posicionTabla = tabla.getSelectedRow();
                posicionTabla = posicionTabla == 0 ? tabla.getRowCount() -1 : posicionTabla -1 ;
                for (int i = 0; i < modelo.getRowCount(); i++) {
                    String valor = modelo.getValueAt(i, 0).toString().trim().toUpperCase();
                    boolean esUltima = i == (tabla.getRowCount() -1);
                    Atributo aAux = null;                    
                    String tipo =  "VARCHAR";
                    
                    
                    if (!esUltima) {
                        aAux = tab.getAtributos().get(i);
                        if (!aAux.getTipo().isEmpty()) {
                            tipo =  aAux.getTipo();
                        }
                    }
                    if (valor.isEmpty()) {
                        if (!esUltima) {
                            if (aAux.getLlaves().contains("(FK)")) {
                                removerRelacion(title, aAux.getNombre());
                            }else{
                                if (existeRelacion(title, aAux.getNombre())) {
                                    JOptionPane.showMessageDialog(rootPane, "no puede eliminarse el atributo "
                                            + aAux.getNombre() + " mientras otra tabla haga referencia a este");
                                    RefrescarTabla();
                                    return ;
                                }
                            }
                            
                        }
                        continue;
                    }
                    
                    String llave = "";
                     if (valor.contains(":")) {
                        tipo = valor.substring(valor.indexOf(":") +1).trim().split("(PK)")[0].split("(FK)")[0].split("=")[0].split("(NN)")[0].split("(UQ)")[0];
                        int posic = valor.indexOf(tipo);
                        int tam = tipo.length();
                        String aux = valor.substring(posic + tam  );
                        
                         if (tipo.contains("(")) {
                             tipo = tipo.substring(0, tipo.indexOf("("));
                         }
                        
                         if (!aux.isEmpty()) {
                             aux  = "(" + aux;
                         }
                        
                        valor = valor.substring(0, valor.indexOf(":")) + " " + aux ;
                        //valor = valor.substring(0, valor.indexOf(":"));
                    }                   
                     
                    if (valor.contains("(PK)")) {
                        llave += "(PK) ";
                        int posPk = valor.indexOf("(PK)");
                        valor = valor.substring(0, posPk) + " " + valor.substring(posPk +4);
                    }
                    
                    if (valor.contains("(UQ)")) {
                        llave += "(UQ) ";
                        int posUQ = valor.indexOf("(UQ)");
                        valor = valor.substring(0, posUQ) + " " + valor.substring(posUQ +4);
                    }
                    
                    if (valor.contains("(NN)")) {
                        llave += "(NN) ";
                        int posNN = valor.indexOf("(NN)");
                        valor = valor.substring(0, posNN) + " " + valor.substring(posNN +4);
                    }
                    
                    if (valor.contains("(FK)")) {
                        String campo1 = valor.substring(0, valor.indexOf("(")).trim();
                        boolean s = true;
                        if (!valor.contains("=") || valor.substring(valor.indexOf("=") +1).trim().isEmpty()) {
                           
                            if (posicionTabla == i && !existeAlgunaRelacion(buscarTab(getTitle()))
                                    && !existeAlgunaRelacion1A1(buscarTab(getTitle())) ) {
                                
                                JOptionPane.showMessageDialog(rootPane, "Llave foranea " + campo1 
                                + " debe hacer referencia a algún valor ");
                                return ;
                            }
                            else{
                                s = false;
                            }
                            
                        }
                        if (s) {
                            String campo2 = valor.substring(valor.lastIndexOf("(") +1, valor.lastIndexOf(")")).trim();
                            String tab2 = (valor.substring(valor.indexOf("=") +1, valor.lastIndexOf("("))).trim();
                            VentanaInterna rela2 = buscarTab(tab2);
                            if (rela2 == null) {
                                JOptionPane.showMessageDialog(rootPane, "Tabla a la que hace refencia el campo " + campo1 + " no encontrada");
                                return ;
                            }
                            if (rela2.tab.buscarAtributo(campo2) == null) {
                                JOptionPane.showMessageDialog(rootPane, "Atributo al que hace refencia el campo " + campo1 + " no encontrado");
                                return ;
                            }
                            
                            Atributo jn = rela2.tab.buscarAtributo(campo2) ;
                            System.out.println("ya casi");
                            System.out.println("tipo " + tipo);
                            if (tipo.trim().compareToIgnoreCase(jn.getTipo()) != 0) {
                                System.out.println("entro");
                                JOptionPane.showMessageDialog(rootPane, "el tipo de dato del campo al "
                                        + "que hace referencia el campo " + campo1 + " debe ser el mismo");
                                return ;
                            }
                            System.out.println("salio");
                            
                            RelacionRelacionada re = bucarRelacion(tab2, getTitle());
                            if (re != null) {
                                relacionesEntreRelaciones.remove(re);
                                RelacionRelacionada nuevaLinea = new RelacionRelacionada(this, campo1, rela2, campo2);
                                relaciones1A1.add(nuevaLinea);
                                puntos1A1.add(0);
                                puntos1A1.add(0);
                                puntos1A1.add(0);
                                puntos1A1.add(0);
                            }else{
//                                tipo;
                                RelacionRelacionada nuevaLinea = new RelacionRelacionada(this, campo1, rela2, campo2);
                                relacionesEntreRelaciones.add(nuevaLinea);
                                puntos.add(0);
                                puntos.add(0);
                                puntos.add(0);
                                puntos.add(0);
                            }
                        }
                        
                        
                        
                        llave += "(FK) ";
                    }
                    
                    if(valor.contains("(")) {    
                        valor = valor.substring(0, valor.indexOf("("));
                    }
                    valor = valor.trim();
                    if (posicionTabla == i && tab.buscarAtributo(valor) != null && tab.posicionAtributo(valor) != posicionTabla) {
                        JOptionPane.showMessageDialog(rootPane, "El campo " + valor + " ya existe en la tabla " + getTitle());
                        return ;
                    }
                    
                    listaux.add(new Atributo(valor));
                    listaux.getLast().addLlaves(llave);
                    listaux.getLast().setTipo(tipo);
                }
                tab.removeAllAtributo();
                tab.setAtributos(listaux);
                
                RefrescarTabla();
            }
            
        }

        @Override
        public void keyPressed(KeyEvent e) {
            
        }

        @Override
        public void keyReleased(KeyEvent e) {
            
        }

            
        }

    public Principal(String title) throws HeadlessException {
        super(title);
        Componentes();
    }

    public static void main(String[] args) {
        Principal p = new Principal("Ventana");
    }

    private void Componentes() {
        menu = new JMenuBar();
        add(menu, "North");
        menuArchivo = new JMenu("Archivo");
        menu.add(menuArchivo);
        menuArchivoImportar = new JMenuItem("Importar Script");
        menuArchivo.add(menuArchivoImportar);
        menuArchivoImportar.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    importarSQL();
                } catch (IOException ex) {
                    Logger.getLogger(Principal.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        menuArchivoExportar = new JMenuItem("Exportat Script");
        menuArchivo.add(menuArchivoExportar);//Aun no hace nada
        menuArchivoExportar.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                exportarSQL();
            }
        });
        menuArchivo.add(new JSeparator());
        menuArchivoNuevaVentana = new JMenuItem("Nueva Ventana");
        menuArchivo.add(menuArchivoNuevaVentana);
        menuArchivoNuevaVentana.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                Principal p = new Principal("Ventana");
            }
        });
        menuArchivo.add(new JSeparator());
        menuArchivoSalir = new JMenuItem("Salir");
        menuArchivo.add(menuArchivoSalir);
        menuArchivoSalir.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        menuEditar = new JMenu("Editar");
        menu.add(menuEditar);
        
        menuEditarAgregarTabla =new JMenuItem("Nueva tabla");
        menuEditar.add(menuEditarAgregarTabla);
        
        menuEditarAgregarTabla.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                nuevaTablaVacia(10, 30);
            }
        });
        
        menuEditar.add(new JSeparator());
        
        menuEditarCambiarNombreTabla = new JMenuItem("Cambiar nombre de una tabla");
        menuEditar.add(menuEditarCambiarNombreTabla);
        menuEditarCambiarNombreTabla.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                String ta = JOptionPane.showInputDialog("Tabla a la que desea cambiarle el nombre");
                VentanaInterna ven = buscarTab(ta);
                if (ven == null) {
                    JOptionPane.showMessageDialog(rootPane, "La tabla " + ta + " no existe");
                    return ;
                }
                String nnta = JOptionPane.showInputDialog("Nuevo nombre de la tabla " + ta );
                if (buscarTab(nnta) != null) {
                    JOptionPane.showMessageDialog(rootPane, "Ya existe una tabla con el nombre " + nnta);
                    return ;
                }
                ven.setTitle(nnta);
            }
        });
        
        menuEditar.add(new JSeparator());
        
        menuEditarEliminarTabla = new JMenuItem("Eliminar Tabla");
        menuEditar.add(menuEditarEliminarTabla);
        menuEditarEliminarTabla.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                eliminarTabla();
            }
        });
        
        menuEditar.add(new JSeparator());
        menuEditarLimpiar = new JMenuItem("Limpiar pantalla");
        menuEditar.add(menuEditarLimpiar);
        menuEditarLimpiar.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                limpiarVentana();
            }
        });
        

        panelPrincipal = new PanelEscritorio();
        add(panelPrincipal);
        
        clicmenu = new JPopupMenu();
        panelPrincipal.add(clicmenu);
        
        clicmenuAgregarTabla =new JMenuItem("Nueva tabla");
        clicmenu.add(clicmenuAgregarTabla);
        
        clicmenuAgregarTabla.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                nuevaTablaVacia(xNV, yNV);
            }
        });
        
        panelPrincipal.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.isMetaDown() && !e.isPopupTrigger()) {
                    xNV = e.getX();
                    yNV = e.getY();
                    clicmenu.show(e.getComponent(), xNV, yNV);
                }
            }
        });
        
        panelPrincipal.repaint();
        
        
        addWindowListener(new WindowAdapter() {

            @Override
            public void windowClosing(WindowEvent e) {
                if (tablas.size() == 0 || JOptionPane.showConfirmDialog(rootPane, "¿Desea realmente cerrar la ventana?", "Cerrar", 
                        JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                    dispose();
                }
            }
        });
        
        setSize(900, 600);
        setVisible(true);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
    }
    
    private void nuevaTablaVacia(int x, int y){
        Relacion re = new Relacion();
        String titulo = JOptionPane.showInputDialog("Nombre tabla");
        
        if (titulo == null) {
            return ;
        }
        
        titulo = titulo.trim().toUpperCase();
        
        if (titulo.isEmpty()) {
            return ;
        }
        
        if (buscarTab(titulo) != null) {
            JOptionPane.showMessageDialog(rootPane, "Ya existe una tabla con el nombre " + titulo);
            return;
        }
        
        VentanaInterna ven = new VentanaInterna(titulo, re);
        tablas.add(ven);
        remove(panelPrincipal);
        add(ven);
        add(panelPrincipal);
        
//        int x = (numeroTablas != 0) ? (80*numeroTablas) : 20; 
//        int y = 30;
        ven.setLocation(x, y);
        numeroTablas++;
    }
    
    private void limpiarVentana(){
        if (JOptionPane.showConfirmDialog(rootPane, "¿seguro?", "Limpiar", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.NO_OPTION) {
            return;
        }
        Script = "";
        for (VentanaInterna tabla : tablas) {
            try {
                tabla.setClosed(true);
            } catch (PropertyVetoException ex) {
                Logger.getLogger(Principal.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        tablas = new LinkedList<>();
        relacionesEntreRelaciones = new LinkedList<>();
        puntos = new LinkedList<>();
        numeroTablas = 0;
        panelPrincipal.repaint();
    }

    private void importarSQL() throws FileNotFoundException, IOException {
        Script = "";
        JFileChooser examinar = new JFileChooser();
        examinar.setFileFilter(new FileNameExtensionFilter("Script", "txt", "sql"));
        int opcion = examinar.showOpenDialog(this);
        //new File("D:\\Desktop\\jexcelapi");
        if (opcion == JFileChooser.APPROVE_OPTION) {
            String s1 = examinar.getSelectedFile().getAbsolutePath();
            if (!s1.contains(".")) {
                    s1 += ".sql";
                }
            direccionScript = new File(s1);
            FileReader fr = new FileReader(direccionScript);
            BufferedReader bf = new BufferedReader(fr);
            String s;
            while ((s = bf.readLine()) != null) {
                s = s.toUpperCase();
                if (s.contains("--")) {
                    s = s.substring(0, s.indexOf("--"));
                }
                
                if (s.contains(",")) {
                    if (s.contains("PRIMARY") ){//|| s.contains("ALTER TABLE")) {
                        s = s.substring(s.lastIndexOf(",")).indexOf(")") > 0 ? s : s.substring(0, s.lastIndexOf(","));
                        if (s.contains(",")) {
                            s = s.replaceAll(",", "#");
                        }
                        s += ",";
                    }
                    /*else{
                        s = s.split(",")[0] + ",";
                    }*/
                }
                Script += s.trim() + " \n";
                
            }
//            direccionScript = new File(direccionScript.toString().substring(0, direccionScript.toString().lastIndexOf("\\")));
            Script = Script.toUpperCase();
            while (Script.contains("/*")) {
                Script = Script.substring(0, Script.indexOf("/*")) + (Script.contains("*/") ? Script.substring(Script.indexOf("*/") + 2, Script.length()) : "");
            }
//            System.out.println(Script);
            crearTabla();
        }
    }

    public void exportarSQL(){
//        if (tablas.isEmpty()) {
//            JOptionPane.showMessageDialog(rootPane, "No hay tablas creadas");
//            return;
//        }
        javax.swing.JFileChooser jF1= new javax.swing.JFileChooser();
        try{
            if(jF1.showSaveDialog(null) == JFileChooser.APPROVE_OPTION){
                String ruta = jF1.getSelectedFile().getAbsolutePath();
                if (!ruta.contains(".")) {
                    ruta += ".sql";
                }
                File archivo = new File(ruta);
                BufferedWriter bw;
//                System.out.println(ruta);
                if(archivo.exists() && JOptionPane.showConfirmDialog(rootPane,
                        "El archivo ya existe\n¿Desea remplazarlo?") > 0) {
                    return;
                }
                bw = new BufferedWriter(new FileWriter(archivo));
                String scr = CrearScript();
                bw.write(scr);
                bw.close();
            }
        }catch (HeadlessException | IOException ex){
        } 
    }
    
    public String CrearScript(){
        String sR = "";
        for (VentanaInterna tabla : tablas) {
            sR += "\n\n--CREAR TABLA "  + tabla.getTitle() + "\n";
            sR += "CREATE TABLE \"" + tabla.getTitle();
            sR += "\" (\n";
            String ll = "PRIMARY KEY (";
            for (Atributo atrib : tabla.tab.getAtributos()) {
                sR += "\t\"" + atrib.getNombre() + "\" " + atrib.getTipo() ;
                if (atrib.getLlaves().contains("PK")) {
                    sR += " UNIQUE";
                    ll += "\"" + atrib.getNombre() + "\",";
                }
                sR += ",\n";
            }
            ll = ll.substring(0, ll.trim().lastIndexOf(",")  == ll.trim().length()-1 ? ll.length() -1 : ll.length());
            ll += ")";
            sR = sR.substring(0, sR.trim().lastIndexOf(",")  == sR.trim().length()-1 ? sR.length() -2 : sR.length());
            if (ll.indexOf(")") != 13) {
                sR += ",\n\t" + ll;
            }
            sR += "\n);\n";
        }
        sR += "\n\n";
        for (RelacionRelacionada rela : relacionesEntreRelaciones) {
            sR += "ALTER TABLE \"" + rela.getRelacion1().getTitle().trim() + "\"";
            sR += " ADD FOREIGN KEY (\"" + rela.getCampo1() + "\"";
            sR += ") REFERENCES \"" + rela.getRelacion2().getTitle().trim() + "\"";
            sR += " (\"" + rela.getCampo2() + "\");\n";
        }
        
        return sR;
    }
    
    private void crearTabla() {
        if (!Script.contains("CREATE TABLE")) {
            JOptionPane.showMessageDialog(rootPane, "No se creo nada");
            return;
        }
        String[] pp = Script.split("CREATE TABLE");
        boolean primera =true;
        for (String s : pp) {
            if (primera) {
                primera = false;
                continue;
            }
            s = s.split(";")[0];
            s = s.split("INSERT")[0];
            s = s.split("VALUES")[0];
//            System.out.println(s);
//            s = s.replace("(", "\n");
//            s = s.replace(")", "\n");
//            System.out.println(" s   es lo siquiente  :: " + s);
//            System.out.println("  index es   ::  " + s.indexOf("\n"));
            String nombreTabla = s.substring(0,s.indexOf("(")).trim();
            String tabla2 = s.substring(s.indexOf("(") + 1, s.lastIndexOf(")")).trim();
            
            
//            tabla2 = tabla2.substring(0, tabla2.indexOf(")")).trim();
            String[] atributos = tabla2.split(",");
            Relacion rela = new Relacion();
            Atributo aa[] = new Atributo[atributos.length];
            int i = 0;
            boolean crearven = false;
            for (String atr : atributos) {
                if (atr.contains("KEY")) {
                    if (!crearven) {
                        VentanaInterna ven = new VentanaInterna(nombreTabla, rela);
                        tablas.add(ven);
                        remove(panelPrincipal);
                        add(ven);
                        add(panelPrincipal);
                        
                        int x = (numeroTablas != 0) ? (80*numeroTablas) : 20; 
                        int y = 30;
                        ven.setLocation(x, y);
                        crearven = true;
                        numeroTablas++;
                    }
                    if (AplicarLlavesPrimarias_y_Foraneas(atr, tablas.getLast())) {
                        continue;
                    }
                    
                }
                String nombre;
                String tipo = "";
                atr = atr.trim();
                if (atr.contains(" ")) {
                    nombre = atr.substring(0, atr.indexOf(" ")).trim();
                    atr = atr.substring(atr.indexOf(" ")).trim();
                    tipo = atr.contains(" ") ? atr.substring(0, atr.indexOf(" ")).trim() : atr;
                    
                }else{
                    nombre = atr.trim();
                }
                aa[i] = new Atributo(nombre, tipo);
                
                if (atr.contains("UNIQUE")) {
                        aa[i].addLlaves("(UQ)");
                }
                
                if (atr.contains("NOT NULL")) {
                        aa[i].addLlaves("(NN)");
                }
                
                rela.addAtributo(aa[i]);
            }
            
            if (!crearven) {
                VentanaInterna ven = new VentanaInterna(nombreTabla, rela);
                tablas.add(ven);
                remove(panelPrincipal);
                add(ven);
                add(panelPrincipal);

                int x =30; 
                int y = 30;
                ven.setLocation(x, y);
                crearven = true;
            }
            
        }
        
        String[] constr = Script.split("ALTER TABLE");
        for (int i = 1; i < constr.length; i++) {
            String aux = constr[i].split(";")[0];
//            System.out.println(aux);
            String[] aux2 = aux.split("ADD");
            VentanaInterna v_aux = buscarTab(aux2[0].trim());
            AplicarLlavesPrimarias_y_Foraneas(aux2[1], v_aux);
        }
        
        Repintar();
    }
    
    private void eliminarTabla(){
        String tablaA_Elimminar = JOptionPane.showInputDialog("Tabla que desea eliminar");
        
        if (tablaA_Elimminar == null ) {
            return ;
        }
        
        tablaA_Elimminar = tablaA_Elimminar.trim().toUpperCase();
        
        if (tablaA_Elimminar.isEmpty()) {
            return ;
        }
        
        VentanaInterna ta = buscarTab(tablaA_Elimminar);
        
        if (ta == null) {
            return ;
        }
        
        if (existeAlgunaRelacion(ta)) {
            JOptionPane.showMessageDialog(rootPane, "no puede eliminarse la tabla "
                    + tablaA_Elimminar + " ya que tiene uno o más campos que son llaves foraneas en una o más tablas");
            return ;
        }
        
        removerCualquierRelacion(ta);
        ta.setVisible(false);
        remove(ta);
        tablas.remove(ta);
        Repintar();
    }
    
    private void removerCualquierRelacion(VentanaInterna tabla){
        for (Atributo atr : tabla.getTab().getAtributos()) {
            if (atr.getLlaves().contains("(FK)")) {
                removerRelacion(tabla.getTitle(), atr.getNombre());
            }
        }
    }
    
    private void removerRelacion(String tabla, String atr){
        int i = 0;
        for (RelacionRelacionada re : relacionesEntreRelaciones) {
            if (re.getRelacion1().getTitle().compareToIgnoreCase(tabla.trim()) == 0
                    && re.getCampo1().compareToIgnoreCase(atr) == 0) {
                relacionesEntreRelaciones.remove(re);
                puntos.removeLast();
                puntos.removeLast();
                puntos.removeLast();
                puntos.removeLast();
            }
            i += 4;
        }
    }
    
    private boolean existeAlgunaRelacion(VentanaInterna tabla){
        for (Atributo atr : tabla.getTab().getAtributos()) {
            if (existeRelacion(tabla.getTitle(), atr.getNombre())) {
                return true;
            }
        }
        return false;
    }
    
    private boolean existeAlgunaRelacion1A1(VentanaInterna tabla){
        for (Atributo atr : tabla.getTab().getAtributos()) {
            if (existeRelacion1A1(tabla.getTitle(), atr.getNombre())) {
                return true;
            }
        }
        return false;
    }
    
    private boolean existeRelacion(String tabla, String campo){
        for (RelacionRelacionada re : relacionesEntreRelaciones) {
            if (re.getRelacion2().getTitle().compareToIgnoreCase(tabla.trim()) == 0 
                    && re.getCampo2().compareToIgnoreCase(campo) == 0) {
                return true;
            }
        }
        return false;
    }
    
    private boolean existeRelacion1A1(String tabla, String campo){
        for (RelacionRelacionada re : relaciones1A1) {
            if (re.getRelacion2().getTitle().compareToIgnoreCase(tabla.trim()) == 0 
                    && re.getCampo2().compareToIgnoreCase(campo) == 0) {
                return true;
            }
        }
        return false;
    }
    
    private boolean AplicarLlavesPrimarias_y_Foraneas(String atr, VentanaInterna tabla){
        atr = atr.replaceAll("KEY", " ");
//                    String atributo = atr.substring(atr.indexOf("("), atr.indexOf(")")).trim();
        if (atr.contains("FOREIGN")) {
            atr = atr.replaceAll("FOREIGN", " ").trim();
            String reE = atr.split("REFERENCES")[1];
            reE = reE.substring(0, reE.indexOf("(")).trim();
            VentanaInterna reExte = buscarTab(reE);
            puntos.add(0);
            puntos.add(0);
            puntos.add(0);
            puntos.add(0);
            String [] atrAux = atr.split("REFERENCES");
            String atributo1 = atrAux[0];
            String atributo2 = atrAux[1];
            atributo1 = atributo1.substring(atributo1.indexOf("(") + 1, atributo1.indexOf(")")).trim();
            atributo2 = atributo2.substring(atributo2.indexOf("(") + 1, atributo2.indexOf(")")).trim();
            LinkedList<Atributo> atrs = tabla.tab.getAtributos();
            for (Atributo aa1 : atrs) {
                if (aa1.getNombre().compareToIgnoreCase(atributo1) == 0) {
                    aa1.addLlaves("(FK)");

                    break;
                }
            }
            RelacionRelacionada ra = new RelacionRelacionada(tabla, atributo1, reExte, atributo2);
            relacionesEntreRelaciones.add(ra);
            tabla.RefrescarTabla();
            return true;
        }
        if (atr.contains("PRIMARY")) {
            atr = atr.substring(atr.indexOf("(") + 1, atr.indexOf(")")).trim();
            LinkedList<Atributo> atrs = tabla.tab.getAtributos();
            String[] aux = atr.split("#");
            for (String atr1 : aux) {
                for (Atributo aa1 : atrs) {
                    if (aa1.getNombre().compareToIgnoreCase(atr1.trim()) == 0) {
                        aa1.addLlaves("(PK)");

                        break;
                    }
                }
            }
            tabla.RefrescarTabla();
            return true;
        }
        return false;
    }

    private VentanaInterna buscarTab (String reE){
        for (VentanaInterna r : tablas) {
                if (r.getTitle().compareToIgnoreCase(reE) == 0) {
                    return r;
                }
            }
        return null;
    }
    
    private RelacionRelacionada bucarRelacion(String t1, String t2){
        for (RelacionRelacionada re : relacionesEntreRelaciones) {
            if (re.getRelacion1().getTitle().compareToIgnoreCase(t1) == 0 && re.getRelacion2().getTitle().compareToIgnoreCase(t2) == 0) {
                return re;
            }
        }
        return null;
    }
    
    private void RomoveTab (String reE){
        VentanaInterna d = buscarTab(reE);
        tablas.remove(d);
        
    }
    
    public void Repintar() {
        gra.setColor(colorFondo);
        for (int i = 0; i < puntos.size(); i+=4) {
            int x1,x2,y1,y2;
            x1 = puntos.get(i);
            y1 = puntos.get(i+1);
            x2 = puntos.get(i+2);
            y2 = puntos.get(i+3);
            gra.drawLine(x1, y1, x2, y2);
            if (x1 > x2) {
//                gra.drawRect(x1, y1 -2, 3, 3);
//                gra.drawRect(x2, y2 -2, 3, 3);
                x1 = x1 -9;
                x2 = x2 +6;
            }
            else{
//                gra.drawRect(x1, y1 -2, 3, 3);
//                gra.drawRect(x2, y2 -2, 3, 3);
                x1 = x1 +6;
                x2 = x2 -9;
            }
            
            gra.drawString("1", x2, y2);
            gra.drawString("n", x1, y1);
        }
        
        for (int i = 0; i < puntos1A1.size(); i+=4) {
            int x1,x2,y1,y2;
            x1 = puntos1A1.get(i);
            y1 = puntos1A1.get(i+1);
            x2 = puntos1A1.get(i+2);
            y2 = puntos1A1.get(i+3);
            gra.drawLine(x1, y1, x2, y2);
            if (x1 > x2) {
                x1 = x1 -9;
                x2 = x2 +6;
            }
            else{
                x1 = x1 +6;
                x2 = x2 -9;
            }
            
            gra.drawString("1", x2, y2);
            gra.drawString("1", x1, y1);
        }
        
        int i = 0;
        gra.setColor(colorLineas);
        
        for (RelacionRelacionada rer : relacionesEntreRelaciones) {
            VentanaInterna v1 = rer.getRelacion1();
            VentanaInterna v2 = rer.getRelacion2();
            int x1,x2,y1,y2;
            x1 = v1.getX();
            y1 = v1.getY() + (v1.getHeight() / 2);
            x2 = v2.getX();
            y2 = v2.getY() + (v2.getHeight() / 2);
            
            int xn = 0, x_1 = 0;
            if (x1 > x2) {
                x2 += v2.getWidth();
                xn = x1 -9;
                x_1 = x2 +6;
            }
            else{
                x1 += v1.getWidth();
                xn = x1 +6;
                x_1 = x2 -9;
            }
            gra.drawString("1", x_1, y2);
            gra.drawString("n", xn, y1);
//            if (x1 > x2) {
//                
//                gra.drawString("1", x_1, y2);
//                gra.drawString("n", x1-9, y1);
//            }
//            else{
//                x1 += v1.getWidth();
//                gra.drawString("1", x2-9, y2);
//                gra.drawString("n", x1+6, y1);
//            }

            gra.drawLine(x1, y1, x2, y2);
            puntos.set(i++, x1);
            puntos.set(i++, y1);
            puntos.set(i++, x2);
            puntos.set(i++, y2);

        }
        
        for (RelacionRelacionada rer : relaciones1A1) {
            VentanaInterna v1 = rer.getRelacion1();
            VentanaInterna v2 = rer.getRelacion2();
            int x1,x2,y1,y2;
            x1 = v1.getX();
            y1 = v1.getY() + (v1.getHeight() / 2);
            x2 = v2.getX();
            y2 = v2.getY() + (v2.getHeight() / 2);
            
            int xn = 0, x_1 = 0;
            if (x1 > x2) {
                x2 += v2.getWidth();
                xn = x1 -9;
                x_1 = x2 +6;
            }
            else{
                x1 += v1.getWidth();
                xn = x1 +6;
                x_1 = x2 -9;
            }
            gra.drawString("1", x_1, y2);
            gra.drawString("1", xn, y1);

            gra.drawLine(x1, y1, x2, y2);
            puntos1A1.set(i++, x1);
            puntos1A1.set(i++, y1);
            puntos1A1.set(i++, x2);
            puntos1A1.set(i++, y2);

        }
        
//        tablas.getFirst().componentResized(null);
    }
}
