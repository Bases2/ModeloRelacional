package GUI;

import Clases.Atributo;
import Clases.Relacion;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyVetoException;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
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
    JMenuItem menuEditarLimpiar;
    JMenuItem clicmenuAgregarTabla;
    Graphics gra;
    String Script;
    File direccionScript;
    LinkedList<VentanaInterna> tablas = new LinkedList<>();
    LinkedList<RelacionRelacionada> relacionesEntreRelaciones = new LinkedList<>();
    int numeroTablas = 0;
    LinkedList<Integer> puntos = new LinkedList<>();
    JPopupMenu clicmenu;

    class PanelEscritorio extends JDesktopPane {

        @Override
        public void paint(Graphics g) {
            super.paintComponent(g);
            setBackground(Color.lightGray);

            gra = getGraphics();
        }
    }

    
    class RelacionRelacionada {
        VentanaInterna relacion1;
        VentanaInterna relacion2;

        public RelacionRelacionada(VentanaInterna relacion1, VentanaInterna relacion2) {
            this.relacion1 = relacion1;
            this.relacion2 = relacion2;
        }

        public VentanaInterna getRelacion1() {
            return relacion1;
        }

        public VentanaInterna getRelacion2() {
            return relacion2;
        }

        public void setRelacion1(VentanaInterna relacion1) {
            this.relacion1 = relacion1;
        }

        public void setRelacion2(VentanaInterna relacion2) {
            this.relacion2 = relacion2;
        }
    }

    
    class VentanaInterna extends JInternalFrame implements ComponentListener {

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
            setVisible(true);
            setResizable(true);
//            tabla.set

            this.tab = tab;
            ver();
            
            int alto = cant > 2 ? (cant * 20) + 20 : 70;
            setSize(160, alto);
        }
        
        private void ver() {
            vaciar();
            LinkedList<Atributo> atribs = tab.getAtributos();
            for (Atributo atr : atribs) {
                modelo.insertRow(cant, new Object[]{});
                modelo.setValueAt(atr.getNombre()+ " " + atr.getLlaves() , cant++, 0);
                //modelo.setValueAt(, cant++, 1);
                
            }
//            modelo.setValueAt( " " , cant++, 0);

        }
        
        public void RefrescarTabla(){
            vaciar();
            LinkedList<Atributo> atribs = tab.getAtributos();
            cant = 0;
            for (Atributo atr : atribs) {
                modelo.insertRow(cant, new Object[]{});
                modelo.setValueAt(atr.getNombre()+ "  " + atr.getLlaves() , cant++, 0);
                //modelo.setValueAt(, i++, 1);
                
            }
//            modelo.setValueAt( " " , cant++, 0);
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
//        menuArchivo.add(menuArchivoExportar);//Aun no hace nada
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
                dispose();
            }
        });
        menuEditar = new JMenu("Editar");
        menu.add(menuEditar);
        menuEditarLimpiar = new JMenuItem("Limpiar");
        menuEditar.add(menuEditarLimpiar);
        menuEditarLimpiar.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                if (JOptionPane.showConfirmDialog(rootPane, "¿seguro?") != 0) {
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
                                
            }
        });
        
        panelPrincipal.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.isMetaDown() && !e.isPopupTrigger()) {
                    clicmenu.show(e.getComponent(), e.getX(), e.getY());
                }
            }
            
});
        
        panelPrincipal.repaint();
        
        setSize(900, 600);
        setVisible(true);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    }

    private void importarSQL() throws FileNotFoundException, IOException {
        Script = "";
        JFileChooser examinar = new JFileChooser();
        examinar.setFileFilter(new FileNameExtensionFilter("Script", "txt", "sql"));
        int opcion = examinar.showOpenDialog(this);
        //new File("D:\\Desktop\\jexcelapi");
        if (opcion == JFileChooser.APPROVE_OPTION) {
            direccionScript = examinar.getSelectedFile().getAbsoluteFile();
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
            System.out.println(Script);
            crearTabla();
        }
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
                    if (relacionar(atr, tablas.getLast())) {
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
            System.out.println(aux);
            String[] aux2 = aux.split("ADD");
            VentanaInterna v_aux = buscarTab(aux2[0].trim());
            relacionar(aux2[1], v_aux);
        }
        
        Repintar();
    }
    
    boolean relacionar(String atr, VentanaInterna tabla){
        atr = atr.replaceAll("KEY", " ");
//                    String atributo = atr.substring(atr.indexOf("("), atr.indexOf(")")).trim();
        if (atr.contains("FOREIGN")) {
            atr = atr.replaceAll("FOREIGN", " ").trim();
            String reE = atr.split("REFERENCES")[1];
            reE = reE.substring(0, reE.indexOf("(")).trim();
            VentanaInterna reExte = buscarTab(reE);
            RelacionRelacionada ra = new RelacionRelacionada(tabla,reExte);
            relacionesEntreRelaciones.add(ra);
            puntos.add(0);
            puntos.add(0);
            puntos.add(0);
            puntos.add(0);
            String atributo = atr.split("REFERENCES")[0];
            atributo = atributo.substring(atributo.indexOf("(") + 1, atributo.indexOf(")")).trim();
            LinkedList<Atributo> atrs = tabla.tab.getAtributos();
            for (Atributo aa1 : atrs) {
                if (aa1.getNombre().compareToIgnoreCase(atributo) == 0) {
                    aa1.addLlaves("(FK)");

                    break;
                }
            }
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

    VentanaInterna buscarTab (String reE){
        for (VentanaInterna r : tablas) {
                if (r.getTitle().compareToIgnoreCase(reE) == 0) {
                    return r;
                }
            }
        return null;
    }
    
    
    public void Repintar() {
        gra.setColor(Color.lightGray);
        for (int i = 0; i < puntos.size(); i+=4) {
            int x1,x2,y1,y2;
            x1 = puntos.get(i);
            y1 = puntos.get(i+1);
            x2 = puntos.get(i+2);
            y2 = puntos.get(i+3);
            gra.drawLine(x1, y1, x2, y2);
            if (x1 > x2) {
                gra.drawString("1", x2+6, y2);
                gra.drawString("n", x1-9, y1);
            }
            else{
                gra.drawString("1", x2-9, y2);
                gra.drawString("n", x1+6, y1);
            }
        }
        int i = 0;
        gra.setColor(Color.darkGray);
        
        for (RelacionRelacionada rer : relacionesEntreRelaciones) {
            VentanaInterna v1 = rer.relacion1;
            VentanaInterna v2 = rer.relacion2;
            int x1,x2,y1,y2;
            x1 = v1.getX();
            y1 = v1.getY() + (v1.getHeight() / 2);
            x2 = v2.getX();
            y2 = v2.getY() + (v2.getHeight() / 2);

            if (x1 > x2) {
                x2 += v2.getWidth();
                gra.drawString("1", x2+6, y2);
                gra.drawString("n", x1-9, y1);
            }
            else{
                x1 += v1.getWidth();
                gra.drawString("1", x2-9, y2);
                gra.drawString("n", x1+6, y1);
            }

            gra.drawLine(x1, y1, x2, y2);
            puntos.set(i++, x1);
            puntos.set(i++, y1);
            puntos.set(i++, x2);
            puntos.set(i++, y2);

        }
//        tablas.getFirst().componentResized(null);
    }
}
