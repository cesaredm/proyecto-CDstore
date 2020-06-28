package controlador;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.JOptionPane;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.table.DefaultTableModel;
import modelo.Creditos;
import modelo.PagosCreditos;
import modelo.Reportes;
import vista.IMenu;

public class CtrlCreditos implements ActionListener, CaretListener, MouseListener {

    IMenu menu;
    Creditos creditos;
    PagosCreditos pagos;
    Reportes report;
    CtrlReportes ctrlReport;
    Date fecha;
    String id;
    DefaultTableModel modelo;

    public CtrlCreditos(IMenu menu, Creditos creditos) {
        this.menu = menu;
        this.creditos = creditos;
        this.pagos = new PagosCreditos();
        this.report = new Reportes();
        this.ctrlReport = new CtrlReportes(menu, report);
        this.modelo = new DefaultTableModel();
        this.fecha = new Date();
        this.menu.btnCrearCredito.addActionListener(this);
        this.menu.btnCrearCredito.setActionCommand("CREAR-CREDITO");
        this.menu.btnNuevoCredito.addActionListener(this);
        this.menu.btnNuevoCredito.setActionCommand("NUEVO-CREDITO");
        this.menu.btnActualizarCredito.addActionListener(this);
        this.menu.EditarCredito.addActionListener(this);
        this.menu.EliminarCredito.addActionListener(this);
        this.menu.GenerarPago.addActionListener(this);
        this.menu.btnAddClienteCredito.addActionListener(this);
        this.menu.btnAddClienteCredito.setActionCommand("AGREGAR-CLIENTE");
        this.menu.txtBuscarCreditosCreados.addCaretListener(this);
        this.menu.txtBuscarCredito.addCaretListener(this);
        this.menu.txtBuscarCreditoFactura.addCaretListener(this);
        this.menu.tblAddClienteCredito.addMouseListener(this);
        this.menu.tblCreditos.addMouseListener(this);
        this.menu.btnYes.addActionListener(this);
        this.menu.btnCancel.addActionListener(this);
        iniciar();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String comando = e.getActionCommand();
        if (e.getSource() == menu.btnCrearCredito) {
            crearCredito();
        }
        
        if (e.getSource() == menu.EliminarCredito) {
           // eliminarCredito();
        }
        if (e.getSource() == menu.btnYes) {
            confirmarBorarCredito();
        }
        if (e.getSource() == menu.btnCancel) {
            menu.ConfimarEliminarCredito.setVisible(false);
        }
        if (e.getSource() == menu.btnActualizarCredito) {
            actualizarCredito();
        }
        //
        if (e.getSource() == menu.btnNuevoCredito) {
            HabilitarCreditos();
            LimpiarCreditos();
        }
        //
        if (e.getSource() == menu.btnAddClienteCredito) {
            menu.BuscarClienteCredito.setSize(592, 277);
            menu.BuscarClienteCredito.setVisible(true);
            menu.BuscarClienteCredito.setLocationRelativeTo(null);
        }
        //
        if (e.getSource() == menu.EditarCredito) {
            editarCredito();
        }
        //
        if (e.getSource() == menu.GenerarPago) {
            generarPago();
        }
    }

    @Override
    public void caretUpdate(CaretEvent e) {
        if (e.getSource() == menu.txtBuscarCreditosCreados) {
            MostrarCreditosCreados(menu.txtBuscarCreditosCreados.getText());
        }
        if (e.getSource() == menu.txtBuscarCredito) {
            MostrarCreditos(menu.txtBuscarCredito.getText());
        }
        if (e.getSource() == menu.txtBuscarCreditoFactura) {
            MostrarCreditosAddFactura(menu.txtBuscarCreditoFactura.getText());
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getSource() == menu.tblAddClienteCredito) {
            int filaseleccionada = menu.tblAddClienteCredito.getSelectedRow();
            try {
                String id, nombres, apellidos;
                if (filaseleccionada == -1) {

                } else {
                    this.modelo = (DefaultTableModel) menu.tblAddClienteCredito.getModel();
                    nombres = this.modelo.getValueAt(filaseleccionada, 1).toString();
                    apellidos = this.modelo.getValueAt(filaseleccionada, 2).toString();
                    id = this.modelo.getValueAt(filaseleccionada, 0).toString();
                    menu.txtClienteCredito.setText(id);
                }

            } catch (Exception err) {
                JOptionPane.showMessageDialog(null, err);
            }
        }
//        if (e.getSource() == menu.tblCreditos) {
//            int filaseleccionada = menu.tblCreditos.getSelectedRow();
//            String id;
//            if (e.getClickCount() == 2) {
//                try {
//                    if (filaseleccionada == -1) {
//
//                    } else {
//                        this.modelo = (DefaultTableModel) menu.tblCreditos.getModel();
//                        id = (String) this.modelo.getValueAt(filaseleccionada, 0);
//                        MostrarFacturasPorCreditos(id);
//                        menu.FacturasPorCreditos.setSize(830, 308);
//                        menu.FacturasPorCreditos.setVisible(true);
//                        menu.FacturasPorCreditos.setLocationRelativeTo(null);
//
//                    }
//                } catch (Exception err) {
//                    JOptionPane.showMessageDialog(null, err + "mostrar facturasporcreditos");
//                }
//            }
//        }

    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    public void iniciar() {
        menu.jcFechaCredito.setDate(fecha);
        MostrarCreditosAddFactura("");
        MostrarCreditosCreados("");
        MostrarCreditos("");
        DeshabilitarCreditos();
        MostrarFacturasPorCreditos("");
        menu.jcFechaPago.setDate(fecha);

    }

//deshabilitar los elementos del form creditos
    public void DeshabilitarCreditos() {
        menu.btnActualizarCredito.setEnabled(false);
        menu.btnCrearCredito.setEnabled(false);
        menu.btnAddClienteCredito.setEnabled(false);
    }

    public boolean isNumeric(String cadena) {//metodo para la validacion de campos numericos
        try {
            Float.parseFloat(cadena);
            return true;
        } catch (NumberFormatException nfe) {
            return false;
        }
    }

    public void MostrarCreditosCreados(String buscar) {
        menu.tblCreditosCreados.getTableHeader().setFont(new Font("Sugoe UI", Font.PLAIN, 14));
        menu.tblCreditosCreados.getTableHeader().setOpaque(false);
        menu.tblCreditosCreados.getTableHeader().setBackground(new Color(100, 100, 100));
        menu.tblCreditosCreados.getTableHeader().setForeground(new Color(255, 255, 255));
        menu.jcFechaCredito.setDate(this.fecha);
        menu.tblCreditosCreados.setModel(creditos.MostrarCreditosCreados(buscar));
    }

    public void MostrarCreditosAddFactura(String buscar) {
        menu.tblAddCreditoFactura.getTableHeader().setFont(new Font("Sugoe UI", Font.PLAIN, 14));
        menu.tblAddCreditoFactura.getTableHeader().setOpaque(false);
        menu.tblAddCreditoFactura.getTableHeader().setBackground(new Color(100, 100, 100));
        menu.tblAddCreditoFactura.getTableHeader().setForeground(new Color(255, 255, 255));
        menu.jcFechaCredito.setDate(this.fecha);
        menu.tblAddCreditoFactura.setModel(creditos.MostrarCreditosAddFactura(buscar));
    }

    public void HabilitarCreditos() {
        menu.btnActualizarCredito.setEnabled(false);
        menu.btnCrearCredito.setEnabled(true);
        menu.btnAddClienteCredito.setEnabled(true);
    }

    public void LimpiarCreditos() {
        menu.txtClienteCredito.setText("");
        menu.cmbEstadoCredito.setSelectedItem("Abierto");
    }

    public void MostrarCreditos(String buscar) {
        menu.tblCreditos.getTableHeader().setFont(new Font("Sugoe UI", Font.PLAIN, 14));
        menu.tblCreditos.getTableHeader().setOpaque(false);
        menu.tblCreditos.getTableHeader().setBackground(new Color(100, 100, 100));
        menu.tblCreditos.getTableHeader().setForeground(new Color(255, 255, 255));
        menu.tblCreditos.setModel(this.creditos.Mostrar(buscar));
    }

    //funcion para cambiar el estado del credito cerrado a Abierto
    public void ActualizarEstadoCreditoAabierto() {
        this.modelo = (DefaultTableModel) menu.tblCreditos.getModel();
        //variable para almacenar total de credito de cliete
        float credito;
        //variable para almacenar el id de credito
        int idCredito = 0;
        //variable para almacenar el id de cliente
        String idCliente;
        //contar la filas de la tabla
        int filas = this.modelo.getRowCount();
        for (int i = 0; i < filas; i++) {
            //id de credito
            idCredito = Integer.parseInt(this.modelo.getValueAt(i, 0).toString());
            //total de credito de cliente
            credito = Float.parseFloat(this.modelo.getValueAt(i, 1).toString());
            //id de cliente
            idCliente = (String) (this.modelo.getValueAt(i, 2).toString());
            //condicion para saber si el saldo esta en 0.0 o menor de 0.0
            if (credito == 0.0 || credito < 0) {
                ctrlReport.setEstadoC(true);
                ctrlReport.idCliente = idCliente;
                creditos.ActualizarEstadoCredito(idCredito, "Abierto");
            } else {
                ctrlReport.setEstadoC(false);
                ctrlReport.idCliente = "0";
            }
        }
    }
    //funcion para cambiar el estado del credito a pendiente

    public void ActualizarEstadoCreditoApendiente() {
        float pagos = 0, credito = 0, saldo = 0;
        String cliente;
        int idCredito;
        this.modelo = (DefaultTableModel) menu.tblCreditosCreados.getModel();
        int filas = this.modelo.getRowCount();
        for (int i = 0; i < filas; i++) {
            idCredito = Integer.parseInt(this.modelo.getValueAt(i, 0).toString());
            cliente = (String) this.modelo.getValueAt(i, 1);
            pagos = this.pagos.PagosCliente(cliente);
            credito = this.creditos.TotalCreditoCliente(Integer.parseInt(cliente));
            saldo = credito - pagos;
            if (saldo > 0.0) {
                creditos.ActualizarEstadoCredito(idCredito, "Pendiente");
            }
        }
    }

    public void MostrarFacturasPorCreditos(String id) {
        if (!id.equals("")) {
            int idC = Integer.parseInt(id);
            menu.tblFacturasCreditos.getTableHeader().setFont(new Font("Sugoe UI", Font.PLAIN, 14));
            menu.tblFacturasCreditos.getTableHeader().setOpaque(false);
            menu.tblFacturasCreditos.getTableHeader().setBackground(new Color(100, 100, 100));
            menu.tblFacturasCreditos.getTableHeader().setForeground(new Color(255, 255, 255));
            menu.tblFacturasCreditos.setModel(creditos.MostrarFacturasPorCreditdos(idC));
        }
    }

    public void crearCredito() {
        Date fechaCredito = menu.jcFechaCredito.getDate();
        int c;
        String Cliente = menu.txtClienteCredito.getText();
        String estado = menu.cmbEstadoCredito.getSelectedItem().toString();
        long f = fechaCredito.getTime();
        java.sql.Date fCredito = new java.sql.Date(f);
        if (!Cliente.equals("")) {
            if (!creditos.VerificarExistenciaDeCredito(Cliente)) {
                if (isNumeric(Cliente)) {
                    c = Integer.parseInt(menu.txtClienteCredito.getText());
                    creditos.GuardarCredito(c, fCredito, estado);
                    MostrarCreditosCreados("");
                    MostrarCreditosAddFactura("");
                    HabilitarCreditos();
                    LimpiarCreditos();
                    menu.btnActualizarCredito.setEnabled(false);
                    menu.btnCrearCredito.setEnabled(true);
                }
            } else {
                JOptionPane.showMessageDialog(null, "Ya existe un credito para el cliente " + Cliente);
            }
        } else {

        }
    }

    public void eliminarCredito() {
        int filaseleccionada = 0, id = 0, confirmacion = 0;

        try {
            filaseleccionada = menu.tblCreditosCreados.getSelectedRow();
            if (filaseleccionada == -1) {

            } else {
                /*confirmacion = JOptionPane.showConfirmDialog(null, "Seguro que Quieres Borrar Este Credito", "Advertencia", JOptionPane.OK_CANCEL_OPTION);
                    if(confirmacion == JOptionPane.YES_OPTION)
                    {
                    modelo = (DefaultTableModel) menu.tblCreditosCreados.getModel();
                    id = Integer.parseInt(modelo.getValueAt(filaseleccionada, 0).toString());
                    creditos.Eliminar(id);
                    MostrarCreditosCreados("");
                    }*/
                menu.ConfimarEliminarCredito.setSize(272, 98);
                menu.ConfimarEliminarCredito.setVisible(true);
                menu.ConfimarEliminarCredito.setLocationRelativeTo(null);
            }
        } catch (Exception err) {
            JOptionPane.showMessageDialog(null, err + "en la funcion eliminar Credito");
        }
    }

    public void confirmarBorarCredito() {
        int filaseleccionada = 0, id = 0, confirmacion = 0;
        try {
            filaseleccionada = menu.tblCreditosCreados.getSelectedRow();
            if (filaseleccionada == -1) {

            } else {
                //confirmacion = JOptionPane.showConfirmDialog(null, "Seguro que Quieres Borrar Este Credito", "Advertencia", JOptionPane.OK_CANCEL_OPTION);
                //if(confirmacion == JOptionPane.YES_OPTION)
                //{
                modelo = (DefaultTableModel) menu.tblCreditosCreados.getModel();
                id = Integer.parseInt(modelo.getValueAt(filaseleccionada, 0).toString());
                String estado = (String) modelo.getValueAt(filaseleccionada, 5);
                if (estado.equals("Abierto") || estado.equals("Cancelado")) {
                    creditos.Eliminar(id);
                    MostrarCreditosCreados("");
                    menu.ConfimarEliminarCredito.setVisible(false);
                } else {
                    JOptionPane.showMessageDialog(null, "El crédito no se puede eliminar por que esta pendiente", "Advertencia", JOptionPane.WARNING_MESSAGE);
                }
                //}
            }
        } catch (Exception err) {
            JOptionPane.showMessageDialog(null, err + "en la funcion eliminar Credito");
        }
    }

    public void actualizarCredito() {
        Date fechaCredito = menu.jcFechaCredito.getDate();
        int c, id = Integer.parseInt(this.id);
        String Cliente = menu.txtClienteCredito.getText();
        String estado = menu.cmbEstadoCredito.getSelectedItem().toString();
        long f = fechaCredito.getTime();
        java.sql.Date fCredito = new java.sql.Date(f);
        if (!Cliente.equals("")) {
            if (isNumeric(Cliente)) {
                c = Integer.parseInt(menu.txtClienteCredito.getText());
                this.creditos.Actualizar(id, c, fCredito, estado);
                MostrarCreditosCreados("");
                MostrarCreditosAddFactura("");
                LimpiarCreditos();
                menu.btnActualizarCredito.setEnabled(false);
                menu.btnCrearCredito.setEnabled(true);
            }
        } else {

        }
    }

    public void editarCredito() {
        int filaseleccionada = menu.tblCreditosCreados.getSelectedRow();
        String id, estado, cliente;
        Date fecha;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            if (filaseleccionada == -1) {

            } else {
                this.modelo = (DefaultTableModel) menu.tblCreditosCreados.getModel();
                id = (String) this.modelo.getValueAt(filaseleccionada, 0);
                cliente = (String) this.modelo.getValueAt(filaseleccionada, 1);
                estado = (String) this.modelo.getValueAt(filaseleccionada, 5);
                fecha = sdf.parse(this.modelo.getValueAt(filaseleccionada, 4).toString());
                if (estado.equals("Abierto") || estado.equals("Cancelado")) {
                    HabilitarCreditos();
                    menu.txtClienteCredito.setText(cliente);
                    menu.jcFechaCredito.setDate(fecha);
                    menu.cmbEstadoCredito.setSelectedItem(estado);
                    this.id = id;
                    menu.btnActualizarCredito.setEnabled(true);
                    menu.btnCrearCredito.setEnabled(false);
                } else {
                    JOptionPane.showMessageDialog(null, "El crédito no se puede editar porque esta pendiente");
                }
            }
        } catch (Exception err) {
            JOptionPane.showMessageDialog(null, err + "en la funcion editar Credito");
        }
    }

    public void generarPago() {
        int filaseleccionada = menu.tblCreditos.getSelectedRow();
        String credito, totalCredito;

        try {
            if (filaseleccionada == -1) {

            } else {
                this.modelo = (DefaultTableModel) menu.tblCreditos.getModel();
                credito = (String) this.modelo.getValueAt(filaseleccionada, 0);
                totalCredito = (String) this.modelo.getValueAt(filaseleccionada, 1);
                menu.txtCreditoPago.setText(credito);
                menu.txtMontoPago.setText("0.0");
                menu.txtMontoPago.requestFocus();
                menu.lblCredito.setText(totalCredito);
                menu.pagosAcreditos.setSize(860, 400);
                menu.pagosAcreditos.setVisible(true);
                menu.pagosAcreditos.setLocationRelativeTo(null);

            }
        } catch (Exception err) {
            JOptionPane.showMessageDialog(null, err + "BTN GENERAR PAGO");
        }
    }
}
