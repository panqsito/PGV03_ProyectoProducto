/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proyecto_producto;

import java.sql.Connection;
import javax.persistence.EntityManager;
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

    public static final Connection getConnection(final EntityManager entityManager) {
        entityManager.getTransaction().begin();
        Connection connection = entityManager.unwrap(java.sql.Connection.class);
        return connection;

    }
   
}
