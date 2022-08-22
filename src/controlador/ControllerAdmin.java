/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador;

import modelo.PersonaJpaController;
import modelo.ProductoJpaController;
import modelo.UsuarioJpaController;
import proyecto_producto.ManagerFactory;
import vista.interna.ViewPersonas;
import vista.ViewAdmin;
import vista.interna.ViewProductos;
import vista.interna.ViewUsuarios;

/**
 *
 * @author Yudit
 */
public class ControllerAdmin extends javax.swing.JFrame {

    ViewAdmin vista;
    ManagerFactory manager;
    public static ViewPersonas vp;
    public static ViewProductos vd;
    public static ViewUsuarios vu;

    public ControllerAdmin(ViewAdmin vista, ManagerFactory manager) {
        this.vista = vista;
        this.manager = manager;
        this.vista.setExtendedState(MAXIMIZED_BOTH);
        controlEventos();
    }

    public void controlEventos() {
        this.vista.getMitemPer().addActionListener(l -> cargarVistPer());
        this.vista.getMitemProd().addActionListener(l -> cargarVistProd());
        this.vista.getMitemUser().addActionListener(l -> cargarVistaUser());
    }

    public void cargarVistPer() {
        new ControllerPer(vp, manager, new PersonaJpaController(manager.getEnf()), vista.getjDesktopPane1());
        Resouces.success("ATENCIÓN!!!", "Accesso existoso a Personas :>");
    }

    public void cargarVistProd() {
        new ControllerProd(vd, manager, new ProductoJpaController(manager.getEnf()), vista.getjDesktopPane1());
        Resouces.success("ATENCIÓN!!!", "Accesso existoso a Productos :>");
    }

    public void cargarVistaUser() {
        new ControllerUser(vu, manager, new UsuarioJpaController(manager.getEnf()), vista.getjDesktopPane1());
        Resouces.success("ATENCIÓN!!!", "Accesso existoso a Usuarios:>");
    }
}
