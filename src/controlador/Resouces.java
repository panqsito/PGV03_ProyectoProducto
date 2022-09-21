/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controlador;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import static javax.swing.WindowConstants.DISPOSE_ON_CLOSE;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.view.JasperViewer;
import rojerusan.RSNotifyAnimated;

/**
 *
 * @author Yudit
 */
public class Resouces {

    public static void success(String actividad, String mensaje) {
        new rojerusan.RSNotifyAnimated(actividad, mensaje,
                5, RSNotifyAnimated.PositionNotify.BottomRight, RSNotifyAnimated.AnimationNotify.RightLeft,
                RSNotifyAnimated.TypeNotify.SUCCESS).setVisible(true);
    }

    public static void warning(String actividad, String mensaje) {
        new rojerusan.RSNotifyAnimated(actividad, mensaje,
                5, RSNotifyAnimated.PositionNotify.BottomRight, RSNotifyAnimated.AnimationNotify.RightLeft,
                RSNotifyAnimated.TypeNotify.WARNING).setVisible(true);
    }

    public static void error(String actividad, String mensaje) {
        new rojerusan.RSNotifyAnimated(actividad, mensaje,
                5, RSNotifyAnimated.PositionNotify.BottomRight, RSNotifyAnimated.AnimationNotify.RightLeft,
                RSNotifyAnimated.TypeNotify.ERROR).setVisible(true);
    }

    public static void imprimirReeporte(Connection con, String url, Map parametros) {
        JasperPrint print;
        try {
            print = JasperFillManager.fillReport(Resouces.class.getResourceAsStream(url), parametros, con);
            JasperViewer view = new JasperViewer(print, false);
            view.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
            view.setVisible(true);
        } catch (JRException ex) {
            Logger.getLogger(Resouces.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
