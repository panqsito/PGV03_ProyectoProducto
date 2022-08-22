/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador;

import java.awt.Dimension;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JDesktopPane;
import javax.swing.JOptionPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import modelo.Persona;
import modelo.PersonaJpaController;
import modelo.Usuario;
import modelo.UsuarioJpaController;
import modelo.exceptions.NonexistentEntityException;
import proyecto_producto.ManagerFactory;
import vista.interna.ViewUsuarios;

/**
 *
 * @author Yudit
 */
public class ControllerUser {

    ViewUsuarios vistus;
    ManagerFactory manager;
    UsuarioJpaController modelUser;
    Usuario usuario;
    JDesktopPane panelEscritorio;
    ModeloTablaUsuario modeloTablaUser;
    ListSelectionModel listaUsuarioModel;

    public ControllerUser(ViewUsuarios vistus, ManagerFactory manager, UsuarioJpaController modelUser, JDesktopPane panelEscritorio) {
        this.vistus = vistus;
        this.manager = manager;
        this.modelUser = modelUser;
        this.panelEscritorio = panelEscritorio;
        //inicializar la tabla
        this.modeloTablaUser = new ModeloTablaUsuario();
        // devuelve la lista de personas
        this.modeloTablaUser.setFilas(modelUser.findUsuarioEntities());

        if (ControllerAdmin.vu == null) {
            ControllerAdmin.vu = new ViewUsuarios();
            this.vistus = ControllerAdmin.vu;
            this.panelEscritorio.add(this.vistus);
            Dimension desktopSize = this.panelEscritorio.getSize();
            Dimension FrameSize = this.vistus.getSize();
            this.vistus.setLocation((desktopSize.width - FrameSize.width) / 2, (desktopSize.height - FrameSize.height) / 2);

            this.vistus.show();
            iniciarControlProd();
            cargarComboBox();

            // agregar tabla usuario
            this.vistus.getTblUsers().setModel(modeloTablaUser);
        } else {
            ControllerAdmin.vu.show();
        }
    }

    // Inicniar control usuario 
    public void iniciarControlProd() {
        this.vistus.getBtnguardarUser().addActionListener(l -> guardarUsuario());
        this.vistus.getBtneditarUser().addActionListener(l -> editarUsuario());
        this.vistus.getBtneliminarUser().addActionListener(l -> eliminarUsuario());
        this.vistus.getBtnlimpiarUser().addActionListener(l -> limpiarUser());
        this.vistus.getBtnlimpiarUserbsq().addActionListener(l -> limpiarBuscadorProd());
        this.vistus.getBtnbuscarUser().addActionListener(l -> buscarUsuario());
        this.vistus.getChekBsqUser().addActionListener(l -> buscarUsuario());

        // eventos tabla
        this.vistus.getTblUsers().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        this.listaUsuarioModel = this.vistus.getTblUsers().getSelectionModel();
        listaUsuarioModel.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    usuarioSeleccionado();
                }
            }
        });
        // control de botones inicio
        this.vistus.getBtneditarUser().setEnabled(false);
        this.vistus.getBtneliminarUser().setEnabled(false);
    }

    public void cargarComboBox() {
        try {
            Vector v = new Vector();
            v.addAll(new PersonaJpaController(manager.getEnf()).findPersonaEntities());
            this.vistus.getComboUserper().setModel(new DefaultComboBoxModel(v));
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("Capturando errores cargando combobox");
        }
    }

    private void usuarioSeleccionado() {
        if (this.vistus.getTblUsers().getSelectedRow() != -1) {
            usuario = modeloTablaUser.getFilas().get(this.vistus.getTblUsers().getSelectedRow());
            //  cargar los datos a la vista
            this.vistus.getComboUserper().setSelectedItem(usuario.getIdpersonau());
            this.vistus.getTxtnombreUser().setText(usuario.getUsuario());
            this.vistus.getTxtpwsclaveUser().setText(usuario.getClave());

            // control de botones seleccionados
            this.vistus.getBtneditarUser().setEnabled(true);
            this.vistus.getBtneliminarUser().setEnabled(true);
            this.vistus.getBtnguardarUser().setEnabled(false);

        }
    }

    // metodos
    public void guardarUsuario() {
        if (validarCampos() != true) {
            Resouces.warning("ATENCIÓN!!!", "Debe llenar todos los campos ¬¬");
        } else {
            usuario = new Usuario();
            usuario.setIdpersonau((Persona) this.vistus.getComboUserper().getSelectedItem());
            usuario.setUsuario(this.vistus.getTxtnombreUser().getText());
            String clave = new String(this.vistus.getTxtpwsclaveUser().getPassword());
            usuario.setClave(clave);
            modelUser.create(usuario);
            modeloTablaUser.agregar(usuario);
            Resouces.success(" ATENCIÓN!!!", "Usuario creado correctamente :>!");
            limpiarUser();
        }

    }

    public void editarUsuario() {
        if (usuario != null) {
            usuario.setIdpersonau((Persona) this.vistus.getComboUserper().getSelectedItem());
            usuario.setUsuario(this.vistus.getTxtnombreUser().getText());
            String clave = new String(this.vistus.getTxtpwsclaveUser().getPassword());
            usuario.setClave(clave);
            try {
                modelUser.edit(usuario);
                modeloTablaUser.eliminar(usuario);
                modeloTablaUser.actualizar(usuario);
            } catch (Exception ex) {
                Logger.getLogger(ControllerPer.class.getName()).log(Level.SEVERE, null, ex);
            }
            Resouces.success(" ATENCIÓN!!!", "Usuario editado correctamente :>!");
            limpiarUser();
        } else {
            Resouces.error(" ATENCIÓN!!!",  "No se pudo editar el usuario :<!");
            limpiarUser();
        }
    }

    public void eliminarUsuario() {
        if (usuario != null) {
            try {
                modelUser.destroy(usuario.getIdusuario());
                modeloTablaUser.eliminar(usuario);
            } catch (NonexistentEntityException ex) {
                Logger.getLogger(ControllerPer.class.getName()).log(Level.SEVERE, null, ex);
            }
            Resouces.success(" ATENCIÓN!!!", "Usuario elminado correctamente :>!");
            limpiarUser();
        }
    }

    public void buscarUsuario() {

        if (this.vistus.getChekBsqUser().isSelected()) {
            modeloTablaUser.setFilas(modelUser.findUsuarioEntities());
            modeloTablaUser.fireTableDataChanged();
        } else {
            if (this.vistus.getTxtBsqUser().getText().isEmpty()) {
                Resouces.warning("ATENCIÓN!!!", "Llene el campo de busqueda :P");
                limpiarBuscadorProd();
            } else {
                modeloTablaUser.setFilas(modelUser.buscarUsers(this.vistus.getTxtBsqUser().getText()));
                modeloTablaUser.fireTableDataChanged();
            }
        }

    }

    //limipiar y validar
    public void limpiarUser() {
        this.vistus.getTxtnombreUser().setText("");
        this.vistus.getTxtpwsclaveUser().setText("");
        this.vistus.getComboUserper().setSelectedIndex(0);
        
        //control de botones
        this.vistus.getBtneditarUser().setEnabled(false);
        this.vistus.getBtneliminarUser().setEnabled(false);
        this.vistus.getBtnguardarUser().setEnabled(true);
    }

    public void limpiarBuscadorProd() {
        this.vistus.getTxtBsqUser().setText("");
        modeloTablaUser.setFilas(modelUser.findUsuarioEntities());
        modeloTablaUser.fireTableDataChanged();
    }

    public boolean validarCampos() {
        boolean valid = true;

        if (vistus.getTxtnombreUser().getText().isEmpty()) {
            Resouces.warning("ATENCIÓN!!!", "El campo usuario esta vacio");
            valid = false;
        }

        if (vistus.getTxtpwsclaveUser().getText().isEmpty()) {
            Resouces.warning("ATENCIÓN!!!", "El campo clave esta vacio");
            valid = false;
        }

        //if(vistus.getComboUserper().getSelectedItem().)
        return valid;
    }

}
