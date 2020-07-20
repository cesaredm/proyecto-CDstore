/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.JOptionPane;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.table.DefaultTableModel;
import modelo.*;
import vista.IMenu;

/**
 *
 * @author CESAR DIAZ MARADIAGA
 */
public class CtrlPagos implements ActionListener, CaretListener {

    IMenu menu;
    PagosCreditos pagos;
    CtrlReportes ctrlR;
    CtrlCreditos ctrlC;
    Creditos creditos;
    Reportes reportes;
    PrintReportes print;
    InfoFactura info;
    DefaultTableModel modelo;
    String id;
    Date fecha;

    public CtrlPagos(IMenu menu, PagosCreditos pagos) {
        this.menu = menu;
        this.pagos = pagos;
        this.reportes = new Reportes();
        this.creditos = new Creditos();
        this.ctrlR = new CtrlReportes(menu, reportes);
        this.ctrlC = new CtrlCreditos(menu, creditos);
        this.modelo = new DefaultTableModel();
        this.print = new PrintReportes();
        this.info = new InfoFactura();
        this.fecha = new Date();
        this.menu.cmbFormaPagoCredito.setModel(pagos.FormasPago());
        this.menu.btnGuardarPago.addActionListener(this);
        this.menu.btnActualizarPago.addActionListener(this);
        this.menu.btnNuevoPago.addActionListener(this);
        this.menu.EditarPago.addActionListener(this);
        this.menu.BorrarPago.addActionListener(this);
        this.menu.txtBuscarPago.addCaretListener(this);
        this.menu.btnMostrarPagosRegistrados.addActionListener(this);
        MostrarPagos("");
        DeshabilitarPagos();
        this.menu.jcFechaPago.setDate(fecha);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        if (e.getSource() == menu.btnGuardarPago) {
            guardarPago();
        }
        if (e.getSource() == menu.btnActualizarPago) {
            int c;
            float montoPago;
            String credito = menu.txtCreditoPago.getText(), monto = menu.txtMontoPago.getText(), formaPago = menu.cmbFormaPagoCredito.getSelectedItem().toString();
            Date f = menu.jcFechaPago.getDate();
            int idFormaPago = Integer.parseInt(pagos.ObtenerFormaPago(formaPago));
            long fecha = f.getTime();
            java.sql.Date fechaPago = new java.sql.Date(fecha);
            if (!credito.equals("") && !monto.equals("")) {
                if (isNumeric(credito) && isNumeric(monto)) {
                    c = Integer.parseInt(credito);
                    montoPago = Float.parseFloat(monto);
                    pagos.Actualizar(this.id, c, montoPago, fechaPago, idFormaPago);
                    MostrarPagos("");
                    ctrlC.MostrarCreditos("");
                    ctrlC.ActualizarEstadoCreditoApendiente();
                    ctrlC.ActualizarEstadoCreditoAabierto();
                    LimpiarPago();
                    ctrlR.reportesDiarios(this.fecha);
                    ctrlR.MostrarReportesDario(this.fecha);
                    ctrlR.ReporteGlobal();
                    ctrlR.SumaTotalFiltroReporte(this.fecha, this.fecha);
                    ctrlC.MostrarCreditosCreados("");
                    menu.btnGuardarPago.setEnabled(true);
                    menu.btnActualizarPago.setEnabled(false);
                }
            } else {

            }
        }
        if (e.getSource() == menu.btnNuevoPago) {
            HabilitarPago();
            LimpiarPago();
            menu.txtMontoPago.requestFocus();
        }
        if (e.getSource() == menu.EditarPago) {
            int filaseleccionada = menu.tblPagos.getSelectedRow();
            String id, monto, credito, formaPago;
            Date fecha;
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            try {
                if (filaseleccionada == -1) {

                } else {
                    this.modelo = (DefaultTableModel) menu.tblPagos.getModel();
                    id = (String) this.modelo.getValueAt(filaseleccionada, 0);
                    monto = (String) this.modelo.getValueAt(filaseleccionada, 1);
                    credito = (String) this.modelo.getValueAt(filaseleccionada, 2);
                    fecha = sdf.parse(this.modelo.getValueAt(filaseleccionada, 3).toString());
                    formaPago = (String) this.modelo.getValueAt(filaseleccionada, 6).toString();
                    this.id = id;

                    HabilitarPago();
                    menu.txtMontoPago.setText(monto);
                    menu.txtCreditoPago.setText(credito);
                    menu.jcFechaPago.setDate(fecha);
                    menu.cmbFormaPagoCredito.setSelectedItem(formaPago);
                    menu.btnGuardarPago.setEnabled(false);
                    menu.btnActualizarPago.setEnabled(true);
                }
            } catch (Exception err) {
            }
        }
        if (e.getSource() == menu.BorrarPago) {
            int filaseleccionada = menu.tblPagos.getSelectedRow(), id = 0;
            try {
                if (filaseleccionada == -1) {

                } else {
                    this.modelo = (DefaultTableModel) menu.tblPagos.getModel();
                    id = Integer.parseInt(this.modelo.getValueAt(filaseleccionada, 0).toString());
                    this.pagos.Eliminar(id);
                    MostrarPagos("");
                    this.ctrlC.MostrarCreditos("");
                    ctrlR.reportesDiarios(this.fecha);
                    ctrlR.MostrarReportesDario(this.fecha);
                    ctrlR.ReporteGlobal();
                    this.ctrlR.SumaTotalFiltroReporte(this.fecha, this.fecha);
                    ctrlC.ActualizarEstadoCreditoApendiente();
                    ctrlC.ActualizarEstadoCreditoAabierto();
                }
            } catch (Exception err) {
                JOptionPane.showMessageDialog(null, e + " en la funcion Borrar Pago", "Error", JOptionPane.ERROR_MESSAGE);
            }

        }
        if (e.getSource() == menu.btnMostrarPagosRegistrados) {
            menu.pagosAcreditos.setSize(860, 400);
            menu.pagosAcreditos.setVisible(true);
            menu.pagosAcreditos.setLocationRelativeTo(null);
        }
    }

    public void MostrarPagos(String buscar) {
        menu.jcFechaPago.setDate(this.fecha);
        menu.tblPagos.getTableHeader().setFont(new Font("Sugoe UI", Font.PLAIN, 14));
        menu.tblPagos.getTableHeader().setOpaque(false);
        menu.tblPagos.getTableHeader().setBackground(new Color(100, 100, 100));
        menu.tblPagos.getTableHeader().setForeground(new Color(255, 255, 255));
        menu.tblPagos.setModel(this.pagos.Mostrar(buscar));
    }

    public boolean isNumeric(String cadena) {//metodo para la validacion de campos numericos
        try {
            Float.parseFloat(cadena);
            return true;
        } catch (NumberFormatException nfe) {
            return false;
        }
    }

    //metodo para limpiar el formulario Pagos
    public void LimpiarPago() {
        menu.txtMontoPago.setText("");
        menu.cmbFormaPagoCredito.setSelectedItem("Efectivo");
    }

    public void HabilitarPago() {
        menu.btnActualizarPago.setEnabled(false);
        menu.btnGuardarPago.setEnabled(true);
    }
    //funcion para deshabilitar componentes de el formulario de pago

    public void DeshabilitarPagos() {
        menu.btnActualizarPago.setEnabled(false);
        menu.btnGuardarPago.setEnabled(false);
    }//lbls para hacer visible el pago en la ventana pago


    public void guardarPago() {
        int c;
        float montoPago, saldo = 0, saldoActual = 0;
        String fechaString = "", credito = menu.txtCreditoPago.getText(), monto = menu.txtMontoPago.getText(), formaPago = menu.cmbFormaPagoCredito.getSelectedItem().toString();
        int idFormaPago = Integer.parseInt(pagos.ObtenerFormaPago(formaPago));
        Date f = menu.jcFechaPago.getDate();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-YYYY");
        long fecha = f.getTime();
        java.sql.Date fechaPago = new java.sql.Date(fecha);
        fechaString = sdf.format(fecha);
        if (!credito.equals("") && !monto.equals("")) {
            if (isNumeric(credito) && isNumeric(monto)) {
                try {
                    c = Integer.parseInt(credito);
                    montoPago = Float.parseFloat(monto);
                    saldoActual = pagos.deuda(credito) - pagos.PagosSegunCredito(credito);
                    pagos.Guardar(c, montoPago, fechaPago, idFormaPago);
                    saldo = saldoActual - montoPago;
                    info.obtenerInfoFactura();
                    print.llenarTicketPago(info.getNombre(), fechaString, this.pagos.cliente(credito), credito, monto, String.valueOf(saldo));
                    MostrarPagos("");
                    LimpiarPago();
                    ctrlC.MostrarCreditos("");
                    ctrlC.ActualizarEstadoCreditoApendiente();
                    ctrlC.ActualizarEstadoCreditoAabierto();
                    ctrlC.MostrarCreditos("");
                    ctrlR.reportesDiarios(this.fecha);
                    ctrlR.MostrarReportesDario(this.fecha);
                    ctrlR.ReporteGlobal();
                    ctrlR.SumaTotalFiltroReporte(this.fecha, this.fecha);
                    MostrarPagos("");
                    ctrlC.MostrarCreditosCreados("");
                    ctrlC.MostrarCreditosAddFactura("");
                    menu.btnGuardarPago.setEnabled(true);
                    menu.btnActualizarPago.setEnabled(false);
                    //el try catch es para el metodo de imprimir
                    print.print("Pago");
                } catch (Exception e) {

                }
            }
        } else {

        }
    }

    @Override
    public void caretUpdate(CaretEvent e) {
        if (e.getSource() == menu.txtBuscarPago) {
            String valor = menu.txtBuscarPago.getText();
            MostrarPagos(valor);
        }
    }
}
