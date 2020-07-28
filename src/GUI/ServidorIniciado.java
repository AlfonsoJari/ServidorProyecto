package GUI;

import Clases.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;
import java.util.Observable;
import java.util.Observer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.table.DefaultTableModel;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class ServidorIniciado extends javax.swing.JInternalFrame implements Observer {

    DAO_Empleado dao = new DAO_Empleado();
    Empleados emp = new Empleados();
    private Socket socket;
    private ServerSocket serverSocket;
    private DataInputStream bufferDeEntrada = null;
    private DataOutputStream bufferDeSalida = null;

    public ServidorIniciado() {
        initComponents();
        dao.addObserver(this);
    }

    public void levantarConexion() {
        try {
            serverSocket = new ServerSocket(5000);
            socket = serverSocket.accept();
            jTextArea2.append("Conexión establecida.\n");
        } catch (Exception e) {
            System.exit(0);
        }
    }

    public void flujos() {
        try {
            bufferDeEntrada = new DataInputStream(socket.getInputStream());
            bufferDeSalida = new DataOutputStream(socket.getOutputStream());
            bufferDeSalida.flush();
        } catch (IOException e) {
        }
    }

    public void recibirDatos() {
        String st = "";
        try {
            do {
                Session session = HibernateUtil.getSessionFactory().openSession();
                Transaction transaction = session.beginTransaction();
                st = (String) bufferDeEntrada.readUTF();
                String[] parts = st.split("/");

                if ("anadir".equals(parts[0])) {
                    emp.setClave(parts[1]);
                    emp.setNombre(parts[2]);
                    emp.setDireccion(parts[3]);
                    emp.setTelefono(Integer.parseInt(parts[4]));
                    session.save(emp);
                    jTextArea2.append("Se ha añadido un empleado remotamente. ID: " + parts[1]+"\n");
                }

                if ("cons".equals(parts[0])) {
                    Empleados consulta = (Empleados) session.get(Empleados.class, parts[1]);
                    enviar("tabla" + "/" + consulta.getClave() + "/" + consulta.getNombre() + "/" + consulta.getDireccion() + "/" + consulta.getTelefono());
                    jTextArea2.append("Se ha consultado un empleado remotamente. ID: " + consulta.getClave()+"\n");
                }

                if ("elim".equals(parts[0])) {
                    Empleados consulta = (Empleados) session.get(Empleados.class, parts[1]);
                    session.delete(consulta);
                    jTextArea2.append("Se ha eliminado un empleado remotamente. ID: " + parts[1]+"\n");
                }

                if ("edit".equals(parts[0])) {
                    emp.setClave(parts[1]);
                    emp.setNombre(parts[2]);
                    emp.setDireccion(parts[3]);
                    emp.setTelefono(Integer.parseInt(parts[4]));
                    session.update(emp);
                    jTextArea2.append("Se ha editado un empleado remotamente. ID: " + parts[1]+"\n");
                }

                transaction.commit();
                session.close();
            } while (true);
        } catch (IOException e) {
        }
    }

    public void enviar(String s) {
        try {
            bufferDeSalida.writeUTF(s);
            bufferDeSalida.flush();
        } catch (IOException e) {
        }
    }

    public void cerrarConexion() {
        try {
            bufferDeEntrada.close();
            bufferDeSalida.close();
            socket.close();
        } catch (IOException e) {
        } finally {
            System.exit(0);
        }
    }

    public void ejecutarConexion() {
        Thread hilo = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        levantarConexion();
                        flujos();
                        recibirDatos();
                    } finally {
                        cerrarConexion();
                    }
                }
            }
        });
        hilo.start();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jButton1 = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTextArea2 = new javax.swing.JTextArea();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        clavetxt = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        nombretxt = new javax.swing.JTextField();
        direcciontxt = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        telefonotxt = new javax.swing.JTextField();
        ConsultarTodos = new javax.swing.JButton();
        Consulta = new javax.swing.JButton();
        Anadir = new javax.swing.JButton();
        Eliminar = new javax.swing.JButton();
        Editar = new javax.swing.JButton();

        setMinimumSize(new java.awt.Dimension(485, 545));
        setOpaque(false);

        jButton1.setText("Escuchar");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jTextArea2.setColumns(20);
        jTextArea2.setRows(5);
        jScrollPane2.setViewportView(jTextArea2);

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Clave", "Nombre", "Direccion", "Telefono"
            }
        ));
        jScrollPane1.setViewportView(jTable1);

        jLabel1.setText("Clave(5):");

        jLabel2.setText("Nombre:");

        jLabel3.setText("Direccion:");

        jLabel4.setText("Telefono:");

        ConsultarTodos.setText("Consultar Todos");
        ConsultarTodos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ConsultarTodosActionPerformed(evt);
            }
        });

        Consulta.setText("Consulta");
        Consulta.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ConsultaActionPerformed(evt);
            }
        });

        Anadir.setText("Anadir");
        Anadir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                AnadirActionPerformed(evt);
            }
        });

        Eliminar.setText("Eliminar");
        Eliminar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                EliminarActionPerformed(evt);
            }
        });

        Editar.setText("Editar");
        Editar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                EditarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel1)
                                .addGap(18, 18, 18)
                                .addComponent(clavetxt, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel2)
                                .addGap(18, 18, 18)
                                .addComponent(nombretxt, javax.swing.GroupLayout.PREFERRED_SIZE, 168, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel3)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(direcciontxt, javax.swing.GroupLayout.PREFERRED_SIZE, 185, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel4)
                                .addGap(18, 18, 18)
                                .addComponent(telefonotxt, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(ConsultarTodos)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(Consulta)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(Anadir)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(Eliminar)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(Editar)))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
            .addGroup(layout.createSequentialGroup()
                .addGap(189, 189, 189)
                .addComponent(jButton1)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(clavetxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(nombretxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(24, 24, 24)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(direcciontxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(telefonotxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 9, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(ConsultarTodos)
                    .addComponent(Anadir)
                    .addComponent(Eliminar)
                    .addComponent(Editar)
                    .addComponent(Consulta))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        jTextArea2.append("Esperando una conexión:\n");
        ejecutarConexion();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void ConsultarTodosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ConsultarTodosActionPerformed
        try {
            dao.obtenerEmpleados((DefaultTableModel) jTable1.getModel());
        } catch (SQLException ex) {
            Logger.getLogger(ServidorIniciado.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_ConsultarTodosActionPerformed

    private void ConsultaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ConsultaActionPerformed
        try {
            dao.obtenerEmpleado((DefaultTableModel) jTable1.getModel(), clavetxt.getText());
        } catch (SQLException ex) {
            Logger.getLogger(ServidorIniciado.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_ConsultaActionPerformed

    private void AnadirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_AnadirActionPerformed
        try {
            dao.anadirEmpleado(clavetxt.getText(), nombretxt.getText(), direcciontxt.getText(), telefonotxt.getText());
        } catch (SQLException ex) {
            Logger.getLogger(ServidorIniciado.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_AnadirActionPerformed

    private void EliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_EliminarActionPerformed
        try {
            dao.eliminarCliente(clavetxt.getText());
        } catch (SQLException ex) {
            Logger.getLogger(ServidorIniciado.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_EliminarActionPerformed

    private void EditarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_EditarActionPerformed
        try {
            dao.actualizarEmpleado(clavetxt.getText(), nombretxt.getText(), direcciontxt.getText(), telefonotxt.getText());
        } catch (SQLException ex) {
            Logger.getLogger(ServidorIniciado.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_EditarActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton Anadir;
    private javax.swing.JButton Consulta;
    private javax.swing.JButton ConsultarTodos;
    private javax.swing.JButton Editar;
    private javax.swing.JButton Eliminar;
    private javax.swing.JTextField clavetxt;
    private javax.swing.JTextField direcciontxt;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextArea jTextArea2;
    private javax.swing.JTextField nombretxt;
    private javax.swing.JTextField telefonotxt;
    // End of variables declaration//GEN-END:variables

    @Override
    public void update(Observable o, Object obj) {
        jTextArea2.append(((String) obj)+"\n");
    }

}
