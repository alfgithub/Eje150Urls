package ejercicio15;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultListModel;

/**
 *
 * @author Alfredo
 */
public final class InterfaceEje15 extends javax.swing.JFrame {

    static DefaultListModel modeloLista = new DefaultListModel();
    static Connection con;
    static int opcionUsuario = 1;
    static Usuario usuarioWebs = null;

    public static void establecerConexion() {

        String servidor = "jdbc:mysql://localhost:3333/urls";
        String user = "root";
        String paword = "1234";

        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            con = DriverManager.getConnection(servidor, user, paword);
            System.out.println("Conectado a Base de Datos");

        } catch (ClassNotFoundException ex) {
            Logger.getLogger(InterfaceEje15.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            Logger.getLogger(InterfaceEje15.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(InterfaceEje15.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(InterfaceEje15.class.getName()).log(Level.SEVERE, null, ex);
        }

    } //establecerConexion

    public void checkUser(String usu, String key) {
        PreparedStatement pstCheck = null;
        ResultSet rsCheck = null;

        try {
            pstCheck = con.prepareStatement("SELECT usuarios.idUsuario, usuarios.nombreUsuario, usuarios.claveUsuario "
                    + " From usuarios"
                    + " WHERE usuarios.nombreUsuario = ?"
                    + " and  usuarios.claveUsuario = ?");

            pstCheck.setString(1, usu);
            pstCheck.setString(2, key);
            rsCheck = pstCheck.executeQuery();
        } catch (SQLException ex) {
            Logger.getLogger(InterfaceEje15.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            if (!rsCheck.next()) {
                textoConectadoComo.setText(" ERROR DE LOGIN O PASS");
                usuarioWebs = new Usuario(1, "Invitado", "nopass");
            } else {
                usuarioWebs = new Usuario(((int) rsCheck.getInt(1)), rsCheck.getString(2), rsCheck.getString(3));
            }
        } catch (SQLException ex) {
            Logger.getLogger(InterfaceEje15.class.getName()).log(Level.SEVERE, null, ex);
        }
        cargarUsuario(usuarioWebs.getId());
        cargarEtiquetas();
    }//checkUser

    public void cargarUsuario(int idUsuario) {
        PreparedStatement pst = null;
        ResultSet rs = null;
        Url u;

        try {
            pst = con.prepareStatement("SELECT "
                    + "usuarios.idUsuario, "
                    + "usuarios.nombreUsuario,  "
                    + "usuarios.claveUsuario, "
                    + "urls.idUrl, "
                    + "urls.direccion, "
                    + "etiquetas.idEtiqueta, "
                    + "etiquetas.textoEtiqueta, "
                    + "comentarios.idComentario, "
                    + "comentarios.textoComentario "
                    + " FROM usuarios "
                    + " LEFT JOIN usuurl ON usuarios.idUsuario = usuurl.idUsuario "
                    + " INNER JOIN urls ON usuurl.idUrl = urls.idUrl "
                    + " LEFT JOIN urletq ON urls.idUrl = urletq.idUrl "
                    + " INNER JOIN etiquetas ON urletq.idEtiqueta = etiquetas.idEtiqueta "
                    + " LEFT JOIN urlcoment ON urls.idUrl = urlcoment.idUrl "
                    + " INNER JOIN comentarios ON urlcoment.idComentario = comentarios.idComentario "
                    + " WHERE usuarios.idUsuario = ?"
                    + " ORDER BY urls.idUrl, etiquetas.idEtiqueta, comentarios.idComentario");

            pst.setInt(1, idUsuario);
            rs = pst.executeQuery();

        } catch (SQLException ex) {
            Logger.getLogger(InterfaceEje15.class.getName()).log(Level.SEVERE, null, ex);
        }

        try {
            while (rs.next()) {
                if (usuarioWebs == null) {
                    usuarioWebs = new Usuario(rs.getInt(1), rs.getString(2), rs.getString(3));
                    u = new Url(rs.getInt(4), rs.getString(5));
                    Etiqueta etiqueta = new Etiqueta(rs.getInt(6), rs.getString(7));
                    u.getEtiquetas().put(rs.getInt(6), etiqueta);
                    u.getComentarios().put(rs.getInt(8), rs.getString(9));
                    usuarioWebs.getUrls().put(u.getId(), u);
                } else {
                    u = new Url(rs.getInt(4), rs.getString(5));
                    Etiqueta etiqueta = new Etiqueta(rs.getInt(6), rs.getString(7));
                    usuarioWebs.getUrls().put(rs.getInt(4), u);

                    if (!usuarioWebs.getUrls().get(rs.getInt(4)).getEtiquetas().containsKey(rs.getInt(6))) {
                        usuarioWebs.getUrls().get(rs.getInt(4)).getEtiquetas().put(rs.getInt(6), etiqueta);
                    }

                    if (!usuarioWebs.getUrls().get(rs.getInt(4)).getComentarios().containsKey(rs.getInt(8))) {
                        usuarioWebs.getUrls().get(rs.getInt(4)).getComentarios().put(rs.getInt(8), rs.getString(9));
                    }


                }//IF_ELSE     
            }//WHILE
        } catch (SQLException ex) {
            Logger.getLogger(InterfaceEje15.class.getName()).log(Level.SEVERE, null, ex);
        }//TRY_CATCH


        for (Url url : usuarioWebs.getUrls().values()) {
            modeloLista.addElement(url);
        }
        ListaUrls.setModel(modeloLista);
        cargarEtiquetas();
        textoConectadoComo.setText(usuarioWebs.getLoguin());
    }//cargarUsuario

    public void cargarEtiquetas() {
        Etiqueta todos = new Etiqueta(0, "Todas");
        jComboBox1Etiquetas.addItem(todos);
        for (Url url : usuarioWebs.getUrls().values()) {
            for (Etiqueta e : url.getEtiquetas().values()) {

                jComboBox1Etiquetas.addItem(e);
            }
        }
    }

    public void buscarPorEtiqueta(Integer idEtiqueta) {
        modeloLista.removeAllElements();
        if (idEtiqueta == 0) {
            for (Url url : usuarioWebs.getUrls().values()) {
                modeloLista.addElement(url);
            }
        } else {
            for (Url url : usuarioWebs.getUrls().values()) {
                if (url.getEtiquetas().containsKey(idEtiqueta)) {
                    modeloLista.addElement(url);
                }//IF
            }//FOR
        }//IF_ELSE

        ListaUrls.repaint();
    }

    public InterfaceEje15() {
        initComponents();
        cargarUsuario(1);

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        ListaUrls = new javax.swing.JList();
        jLabel1 = new javax.swing.JLabel();
        textoConectadoComo = new javax.swing.JTextField();
        jComboBox1Etiquetas = new javax.swing.JComboBox();
        jLabel2 = new javax.swing.JLabel();
        jTextField1Login = new javax.swing.JTextField();
        jPasswordField1Pass = new javax.swing.JPasswordField();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jButton1Sesion = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jScrollPane1.setViewportView(ListaUrls);

        jLabel1.setText("Conectado como");

        textoConectadoComo.setEditable(false);
        textoConectadoComo.setText("...");

        jComboBox1Etiquetas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox1EtiquetasActionPerformed(evt);
            }
        });

        jLabel2.setText("  Buscar por Etiqueta");

        jLabel3.setText("Nombre");

        jLabel4.setText("Clave");

        jButton1Sesion.setText("Iniciar Sesión");
        jButton1Sesion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1SesionActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addGap(29, 29, 29)
                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jTextField1Login, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(43, 43, 43)
                        .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPasswordField1Pass, javax.swing.GroupLayout.PREFERRED_SIZE, 169, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jButton1Sesion, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 676, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel1)
                        .addGap(28, 28, 28))
                    .addGroup(layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jComboBox1Etiquetas, 0, 129, Short.MAX_VALUE)
                            .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(textoConectadoComo))
                        .addContainerGap())))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(29, 29, 29)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField1Login, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jPasswordField1Pass, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4)
                    .addComponent(jLabel3)
                    .addComponent(jButton1Sesion))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jComboBox1Etiquetas, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel1)
                        .addGap(13, 13, 13)
                        .addComponent(textoConectadoComo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 404, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jComboBox1EtiquetasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox1EtiquetasActionPerformed
        buscarPorEtiqueta(((Etiqueta) jComboBox1Etiquetas.getSelectedItem()).getId());
    }//GEN-LAST:event_jComboBox1EtiquetasActionPerformed

    private void jButton1SesionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1SesionActionPerformed
        String contraseña = "";

        char[] c = jPasswordField1Pass.getPassword();
        for (char x : c) {
            contraseña += x;
        }
        checkUser(jTextField1Login.getText(), contraseña);
        opcionUsuario = usuarioWebs.getId();
        cargarUsuario(opcionUsuario);
    }//GEN-LAST:event_jButton1SesionActionPerformed

    public static void main(String args[]) {
        establecerConexion();





        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(InterfaceEje15.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(InterfaceEje15.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(InterfaceEje15.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(InterfaceEje15.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                new InterfaceEje15().setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JList ListaUrls;
    private javax.swing.JButton jButton1Sesion;
    private javax.swing.JComboBox jComboBox1Etiquetas;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPasswordField jPasswordField1Pass;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField jTextField1Login;
    private javax.swing.JTextField textoConectadoComo;
    // End of variables declaration//GEN-END:variables
}
//Preguntas.
//validacion es automatica. HashMap sobrescribir hascode y equals de Urls
// El Jlist no respeta los saltos de linea del tostring de Urls.