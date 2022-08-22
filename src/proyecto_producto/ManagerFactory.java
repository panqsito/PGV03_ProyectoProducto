/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proyecto_producto;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author Yudit
 */
public class ManagerFactory {

    private EntityManagerFactory enf = null;

    public EntityManagerFactory getEnf() {
        return enf = Persistence.createEntityManagerFactory("Proyecto_productoPU");
    }
}
