/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador;

import modelo.Usuario;
import modelo.UsuarioJpaController;
import proyecto_producto.ManagerFactory;
import vista.ViewAdmin;
import vista.ViewLogin;

/**
 *
 * @author Yudit
 */
public class ControllerLogin {

    private ManagerFactory manager;
    private ViewLogin vist;
    private ViewAdmin vista;
    private UsuarioJpaController model;

    public ControllerLogin(ManagerFactory manager, ViewLogin vist, ViewAdmin vista, UsuarioJpaController model) {
        this.manager = manager;
        this.vist = vist;
        this.vista = vista;
        this.model = model;
        this.vista.setLocationRelativeTo(null);
        this.vist.setLocationRelativeTo(null);
        this.vist.setVisible(true);
        iniciarControl();
    }

    public void iniciarControl() {
        vist.getBtnentrar().addActionListener(l -> controlLogin());
        vist.getBtncerrar().addActionListener(l -> cerrar());
        vista.getBtnsalir().addActionListener(l -> salir());
    }

    public void controlLogin() {
        String usuario = vist.getTxtusuario().getText();
        String clave = new String(vist.getTxtpassword().getPassword());

        //inicializamos el objeto modelo
        Usuario user = model.buscarUsuario(usuario, clave);
        // controles
        if (user != null) {
            vista.setVisible(true);
            Resouces.success("ATENCIÓN!!!", "Usuario Correcto :> \nBienvenid@ " + user.getIdpersonau().getNombre());
            new ControllerAdmin(vista, manager);
            limpiarLogin();
            vist.dispose();
        } else {
            Resouces.error("ERROR!!!", "Usuario Incorrecto :<");
        }

    }

    //-------------------------------------------------------------------------
    
    public void salir() {
        Resouces.success("ATENCIÓN!!!", "Saliendo del programa :< \nHasta pronto...");
        vista.dispose();
        vist.setVisible(true);
    }

    public void cerrar() {
        System.exit(0);
    }

    public void limpiarLogin() {
        vist.getTxtpassword().setText("");
        vist.getTxtusuario().setText("");
    }
}
