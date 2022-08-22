/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proyecto_producto;

import controlador.ControllerLogin;
import modelo.UsuarioJpaController;
import vista.ViewAdmin;
import vista.ViewLogin;

/**
 *
 * @author Yudit
 */
public class Proyecto_producto {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        ManagerFactory manager = new ManagerFactory();
        ViewLogin vist = new ViewLogin();
        ViewAdmin vista = new ViewAdmin();
        UsuarioJpaController model = new UsuarioJpaController(manager.getEnf());
        ControllerLogin ctrl = new ControllerLogin(manager, vist, vista, model);
    }

}
