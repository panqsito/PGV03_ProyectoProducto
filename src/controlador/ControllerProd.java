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
import modelo.Producto;
import modelo.ProductoJpaController;
import modelo.exceptions.NonexistentEntityException;
import proyecto_producto.ManagerFactory;
import vista.interna.ViewProductos;

/**
 *
 * @author Yudit
 */
public class ControllerProd {

    ViewProductos vistpd;
    ManagerFactory manager;
    ProductoJpaController modelPd;
    Producto producto;
    JDesktopPane panelEscritorio;
    ModeloTablaProducto modeloTablaProd;
    ListSelectionModel listaProductoModel;

    public ControllerProd(ViewProductos vistpd, ManagerFactory manager, ProductoJpaController modelPd, JDesktopPane panelEscritorio) {
        this.vistpd = vistpd;
        this.manager = manager;
        this.modelPd = modelPd;
        this.panelEscritorio = panelEscritorio;
        //inicializar la tabla
        this.modeloTablaProd = new ModeloTablaProducto();
        // devuelve la lista de personas
        this.modeloTablaProd.setFilas(modelPd.findProductoEntities());

        if (ControllerAdmin.vd == null) {
            ControllerAdmin.vd = new ViewProductos();
            this.vistpd = ControllerAdmin.vd;
            this.panelEscritorio.add(this.vistpd);
            Dimension desktopSize = this.panelEscritorio.getSize();
            Dimension FrameSize = this.vistpd.getSize();
            this.vistpd.setLocation((desktopSize.width - FrameSize.width) / 2, (desktopSize.height - FrameSize.height) / 2);

            this.vistpd.show();
            iniciarControlProd();

            // agregar tabla producto
            this.vistpd.getTblProductos().setModel(modeloTablaProd);
        } else {
            ControllerAdmin.vd.show();
        }
    }

    // Inicniar control producto 
    public void iniciarControlProd() {
        this.vistpd.getBtnguardarPrd().addActionListener(l -> guardarProducto());
        this.vistpd.getBtneditarProd().addActionListener(l -> editarProducto());
        this.vistpd.getBtneliminarProd().addActionListener(l -> eliminarProd());
        this.vistpd.getBtnlimpiarProd().addActionListener(l -> limpiarProd());
        this.vistpd.getBtnlimpiarProdbsq().addActionListener(l -> limpiarBuscadorProd());
        this.vistpd.getBtnbuscarProd().addActionListener(l -> buscarProducto());
        this.vistpd.getChekBsqProd().addActionListener(l -> buscarProducto());

        // eventos tabla
        this.vistpd.getTblProductos().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        this.listaProductoModel = this.vistpd.getTblProductos().getSelectionModel();
        listaProductoModel.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                if (!e.getValueIsAdjusting()) {
                    productoSeleccionado();
                }
            }
        });
        
        this.vistpd.getBtnReporteGeneral().addActionListener(l -> reporteGeneral());
        this.vistpd.getBtnReporteIndividual().addActionListener(l -> reporteIndividual());
        // control de botones inicio
        this.vistpd.getBtneditarProd().setEnabled(false);
        this.vistpd.getBtneliminarProd().setEnabled(false);
    }

    private void productoSeleccionado() {
        if (this.vistpd.getTblProductos().getSelectedRow() != -1) {
            producto = modeloTablaProd.getFilas().get(this.vistpd.getTblProductos().getSelectedRow());
            //  cargar los datos a la vista
            this.vistpd.getTxtnombreProd().setText(producto.getNombre());
            String precio = String.valueOf(producto.getPrecio());
            this.vistpd.getTxtprecioProd().setText(precio);
            this.vistpd.getSpncantidadProd().setValue(producto.getCantidad());
            // control de botones seleccionados
            this.vistpd.getBtneditarProd().setEnabled(true);
            this.vistpd.getBtneliminarProd().setEnabled(true);
            this.vistpd.getBtnguardarPrd().setEnabled(false);

        }
    }

    // metodos
    public void guardarProducto() {
        if (validarCampos() != true) {
            Resouces.warning("ATENCIÓN!!!", "Debe llenar todos los campos ¬¬");
        } else {
            producto = new Producto();
            producto.setNombre(this.vistpd.getTxtnombreProd().getText());
            long precio = Long.parseLong(this.vistpd.getTxtprecioProd().getText());
            producto.setPrecio(precio);
            producto.setCantidad((Integer) this.vistpd.getSpncantidadProd().getValue());
            modelPd.create(producto);
            modeloTablaProd.agregar(producto);
            Resouces.success(" ATENCIÓN!!!", "Producto creado correctamente :>!");
            limpiarProd();
        }

    }

    public void editarProducto() {
        if (producto != null) {
            producto.setNombre(this.vistpd.getTxtnombreProd().getText());
            long precio = Long.parseLong(this.vistpd.getTxtprecioProd().getText());
            producto.setPrecio(precio);
            producto.setCantidad((Integer) this.vistpd.getSpncantidadProd().getValue());
            try {
                modelPd.edit(producto);
                modeloTablaProd.eliminar(producto);
                modeloTablaProd.actualizar(producto);
            } catch (Exception ex) {
                Logger.getLogger(ControllerPer.class.getName()).log(Level.SEVERE, null, ex);
            }
            Resouces.success(" ATENCIÓN!!!", "Producto editado correctamente :>!");
            limpiarProd();
        } else {
            Resouces.error(" ATENCIÓN!!!",  "No se pudo editar el producto :<!");
            limpiarProd();
        }
    }

    public void eliminarProd() {
        if (producto != null) {
            try {
                modelPd.destroy(producto.getIdproducto());
                modeloTablaProd.eliminar(producto);
            } catch (NonexistentEntityException ex) {
                Logger.getLogger(ControllerPer.class.getName()).log(Level.SEVERE, null, ex);
            }
            Resouces.success(" ATENCIÓN!!!", "Producto elminado correctamente :>!");
            limpiarProd();
        }
    }

    public void buscarProducto() {

        if (this.vistpd.getChekBsqProd().isSelected()) {
            modeloTablaProd.setFilas(modelPd.findProductoEntities());
            modeloTablaProd.fireTableDataChanged();
        } else {
            if (this.vistpd.getTxtBsqProd().getText().isEmpty()) {
                Resouces.warning("Atención!!!", "Llene el campo de busqueda :P");
                limpiarBuscadorProd();
            } else {
                modeloTablaProd.setFilas(modelPd.buscarProducto(this.vistpd.getTxtBsqProd().getText()));
                modeloTablaProd.fireTableDataChanged();
            }
        }

    }
    
    public void reporteGeneral() {
        Resouces.imprimirReeporte(ManagerFactory.getConnection(manager.getEnf().createEntityManager()), "/reporte/Producto.jasper",new HashMap());
    }
    
    public void reporteIndividual() {
        // validar si existe un registro seleccionado 
        if (producto != null) {
            //contruir los parametros de encio al reporte
            Map parameters = new HashMap(); //  los hash maps son clave valor 
            // Asignar parametros al 
            parameters.put("cod", producto.getIdproducto()); // clave seria el id // valor seria el persona.getIdpersona
            // llamamos al metodo del reporte
            Resouces.imprimirReeporte(ManagerFactory.getConnection(manager.getEnf().createEntityManager()), "/reporte/Prindividual.jasper", parameters);
        } else {
            Resouces.warning("ATENCIÓN!!!","Debe seleccionar una persona :P");
        }
    }

    //limipiar y validar
    public void limpiarProd() {
        this.vistpd.getTxtnombreProd().setText("");
        this.vistpd.getTxtprecioProd().setText("");
        this.vistpd.getSpncantidadProd().setValue(0);
        this.vistpd.getBtneditarProd().setEnabled(false);
        this.vistpd.getBtneliminarProd().setEnabled(false);
        this.vistpd.getBtnguardarPrd().setEnabled(true);
    }

    public void limpiarBuscadorProd() {
        this.vistpd.getTxtBsqProd().setText("");
        modeloTablaProd.setFilas(modelPd.findProductoEntities());
        modeloTablaProd.fireTableDataChanged();
    }

    public boolean validarCampos() {
        boolean valid = true;

        if (vistpd.getTxtnombreProd().getText().isEmpty()) {
            Resouces.warning("Atención!!!", "El campo nombre esta vacio");
            valid = false;
        }

        if (vistpd.getTxtprecioProd().getText().isEmpty()) {
            Resouces.warning("Atención!!!", "El campo precio esta vacio");
            valid = false;
        }

        return valid;
    }

}
