/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador;

import java.awt.Dimension;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JDesktopPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import modelo.Persona;
import modelo.PersonaJpaController;
import modelo.exceptions.NonexistentEntityException;
import proyecto_producto.ManagerFactory;
import vista.interna.ViewPersonas;

/**
 *
 * @author Yudit
 */
public class ControllerPer {

    ViewPersonas vistpr;
    ManagerFactory manager;
    PersonaJpaController modelPr;
    Persona persona;
    JDesktopPane panelEscritorio;
    ModeloTablaPersona modeloTablaPer;
    ListSelectionModel listaPersonaModel;

    public ControllerPer(ViewPersonas vistpr, ManagerFactory manager, PersonaJpaController modelPr, JDesktopPane panelEscritorio) {

        this.manager = manager;
        this.modelPr = modelPr;
        this.panelEscritorio = panelEscritorio;
        //inicializar la tabla
        this.modeloTablaPer = new ModeloTablaPersona();
        // devuelve la lista de personas
        this.modeloTablaPer.setFilas(modelPr.findPersonaEntities());

        if (ControllerAdmin.vp == null) {
            ControllerAdmin.vp = new ViewPersonas();
            this.vistpr = ControllerAdmin.vp;
            this.panelEscritorio.add(this.vistpr);
            Dimension desktopSize = this.panelEscritorio.getSize();
            Dimension FrameSize = this.vistpr.getSize();
            this.vistpr.setLocation((desktopSize.width - FrameSize.width) / 2, (desktopSize.height - FrameSize.height) / 2);
            this.vistpr.show();
            iniciarControlPer();

            //agregar tabla persona
            this.vistpr.getTblPersonas().setModel(modeloTablaPer);
        } else {
            ControllerAdmin.vp.show();
        }
    }

    // Inicniar control persona 
    public void iniciarControlPer() {
        this.vistpr.getBtnguardarPer().addActionListener(l -> guardarPersona());
        this.vistpr.getBtneditarPer().addActionListener(l -> editarPersona());
        this.vistpr.getBtneliminarPer().addActionListener(l -> eliminarPer());
        this.vistpr.getBtnlimpiarPer().addActionListener(l -> limpiarPer());
        this.vistpr.getBtnlimpiarPerbsq().addActionListener(l -> limpiarBuscadorPer());
        this.vistpr.getBtnbuscarPer().addActionListener(l -> buscarPersona());
        this.vistpr.getChekBsqPer().addActionListener(l -> buscarPersona());

        // eventos tabla
        this.vistpr.getTblPersonas().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        this.listaPersonaModel = this.vistpr.getTblPersonas().getSelectionModel();
        listaPersonaModel.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    personaSeleccionada();
                }
            }
        });
        // control de botones inicio
        this.vistpr.getBtnReporteGeneral().addActionListener(l -> reporteGeneral());
        this.vistpr.getBtnReporteIndividual().addActionListener(l -> reporteIndividual());
        this.vistpr.getBtneditarPer().setEnabled(false);
        this.vistpr.getBtneliminarPer().setEnabled(false);

    }

    // asignar lo seleccionado al objeto
    private void personaSeleccionada() {
        if (this.vistpr.getTblPersonas().getSelectedRow() != -1) {
            persona = modeloTablaPer.getFilas().get(this.vistpr.getTblPersonas().getSelectedRow());
            //  cargar los datos a la vista
            this.vistpr.getTxtnombrePer().setText(persona.getNombre());
            this.vistpr.getTxtapellidoPer().setText(persona.getApellido());
            this.vistpr.getTxtcedulaPer().setText(persona.getCedula());
            this.vistpr.getTxtcelularPer().setText(persona.getCelular());
            this.vistpr.getTxtcorreoPer().setText(persona.getCedula());
            this.vistpr.getTxtdireccionPer().setText(persona.getDireccion());
            // control de botones seleccionados
            this.vistpr.getBtneditarPer().setEnabled(true);
            this.vistpr.getBtneliminarPer().setEnabled(true);
            this.vistpr.getBtnguardarPer().setEnabled(false);

        }
    }

    // metodos
    public void guardarPersona() {
        if (validarCampos() != true) {
            Resouces.warning("ATENCIÓN!!!", "Debe llenar todos los campos ¬¬");
        } else {
            persona = new Persona();
            persona.setNombre(this.vistpr.getTxtnombrePer().getText());
            persona.setApellido(this.vistpr.getTxtapellidoPer().getText());
            persona.setCedula(this.vistpr.getTxtcedulaPer().getText());
            persona.setCelular(this.vistpr.getTxtcelularPer().getText());
            persona.setCorreo(this.vistpr.getTxtcorreoPer().getText());
            persona.setDireccion(this.vistpr.getTxtdireccionPer().getText());
            modelPr.create(persona);
            modeloTablaPer.agregar(persona);
            Resouces.success(" ATENCIÓN!!!", "Persona creada correctamente :>!");
            limpiarPer();
        }

    }

    public void editarPersona() {
        if (persona != null) {
            persona.setNombre(this.vistpr.getTxtnombrePer().getText());
            persona.setApellido(this.vistpr.getTxtapellidoPer().getText());
            persona.setCedula(this.vistpr.getTxtcedulaPer().getText());
            persona.setCelular(this.vistpr.getTxtcelularPer().getText());
            persona.setCorreo(this.vistpr.getTxtcorreoPer().getText());
            persona.setDireccion(this.vistpr.getTxtdireccionPer().getText());
            try {
                modelPr.edit(persona);
                modeloTablaPer.eliminar(persona);
                modeloTablaPer.actualizar(persona);
            } catch (Exception ex) {
                Logger.getLogger(ControllerPer.class.getName()).log(Level.SEVERE, null, ex);
            }
            Resouces.success(" ATENCIÓN!!!", "Persona editada correctamente :>!");
            limpiarPer();
        } else {
            Resouces.error(" ATENCIÓN!!!", "No se pudo editar la persona :<!");
            limpiarPer();
        }
    }

    public void eliminarPer() {
        if (persona != null) {
            try {
                modelPr.destroy(persona.getIdpersona());
                modeloTablaPer.eliminar(persona);
            } catch (NonexistentEntityException ex) {
                Logger.getLogger(ControllerPer.class.getName()).log(Level.SEVERE, null, ex);
            }
            Resouces.success(" ATENCIÓN!!!", "Persona elminada correctamente :>!");
            limpiarPer();
        }
    }

    public void buscarPersona() {

        if (this.vistpr.getChekBsqPer().isSelected()) {
            modeloTablaPer.setFilas(modelPr.findPersonaEntities());
            modeloTablaPer.fireTableDataChanged();
        } else {
            if (this.vistpr.getTxtBsq().getText().isEmpty()) {
                Resouces.warning("ATENCIÓN!!!", "Llene el campo de busqueda :P");
                limpiarBuscadorPer();
            } else {
                modeloTablaPer.setFilas(modelPr.buscarPersona(this.vistpr.getTxtBsq().getText()));
                modeloTablaPer.fireTableDataChanged();
            }
        }

    }

    public void reporteGeneral() {
        Resouces.imprimirReeporte(ManagerFactory.getConnection(manager.getEnf().createEntityManager()), "/reporte/Coffee_Landscape_1.jasper", new HashMap());
    }

    public void reporteIndividual() {
        // validar si existe un registro seleccionado 
        if (persona != null) {
            //contruir los parametros de encio al reporte
            Map parameters = new HashMap(); //  los hash maps son clave valor 
            // Asignar parametros al 
            parameters.put("id", persona.getIdpersona()); // clave seria el id // valor seria el persona.getIdpersona
            // llamamos al metodo del reporte
            Resouces.imprimirReeporte(ManagerFactory.getConnection(manager.getEnf().createEntityManager()), "/reporte/Pindividual.jasper", parameters);
        } else {
            Resouces.warning("ATENCIÓN!!!","Debe seleccionar una persona :P");
        }
    }

    //limipiar y validar
    public void limpiarPer() {
        this.vistpr.getTxtnombrePer().setText("");
        this.vistpr.getTxtapellidoPer().setText("");
        this.vistpr.getTxtcedulaPer().setText("");
        this.vistpr.getTxtcelularPer().setText("");
        this.vistpr.getTxtcorreoPer().setText("");
        this.vistpr.getTxtdireccionPer().setText("");
        this.vistpr.getTblPersonas().getSelectionModel().clearSelection();

        //control de botones
        this.vistpr.getBtneditarPer().setEnabled(false);
        this.vistpr.getBtneliminarPer().setEnabled(false);
        this.vistpr.getBtnguardarPer().setEnabled(true);
    }

    public void limpiarBuscadorPer() {
        this.vistpr.getTxtBsq().setText("");
        modeloTablaPer.setFilas(modelPr.findPersonaEntities());
        modeloTablaPer.fireTableDataChanged();
    }

    public boolean validarCampos() {
        boolean valid = true;

        if (vistpr.getTxtnombrePer().getText().isEmpty()) {
            Resouces.warning("Atención!!!", "El campo nombre esta vacio");
            valid = false;
        }

        if (vistpr.getTxtapellidoPer().getText().isEmpty()) {
            Resouces.warning("Atención!!!", "El campo apellido esta vacio");
            valid = false;
        }

        if (vistpr.getTxtcedulaPer().getText().isEmpty()) {
            Resouces.warning("Atención!!!", "El campo cédula esta vacio");
            valid = false;
        }

        if (vistpr.getTxtcelularPer().getText().isEmpty()) {
            Resouces.warning("Atención!!!", "El campo celular esta vacio");
            valid = false;
        }

        if (vistpr.getTxtcorreoPer().getText().isEmpty()) {
            Resouces.warning("Atención!!!", "El campo correo esta vacio");
            valid = false;
        }

        if (vistpr.getTxtdireccionPer().getText().isEmpty()) {
            Resouces.warning("Atención!!!", "El campo dirección esta vacio");
            valid = false;
        }
        return valid;
    }
}
