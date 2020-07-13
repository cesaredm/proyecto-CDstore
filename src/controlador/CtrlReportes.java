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
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.JOptionPane;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import modelo.Reportes;
import vista.IMenu;
import javax.swing.table.DefaultTableModel;
import modelo.PagosCreditos;
import modelo.AperturasYcierres;

/**
 *
 * @author CESAR DIAZ MARADIAGA
 */
public class CtrlReportes implements ActionListener, MouseListener, KeyListener {

    IMenu menu;
    Reportes reportes;
    PagosCreditos pagosC;
    AperturasYcierres aperturas;
    SpinnerNumberModel sModel;
    JSpinner spiner;
    Date fecha;
    DefaultTableModel modelo;
    String idCliente = "";
    private boolean estadoC = true;

    public CtrlReportes(IMenu menu, Reportes reportes) {
        this.menu = menu;
        this.reportes = reportes;
        this.pagosC = new PagosCreditos();
        this.aperturas = new AperturasYcierres();
        this.fecha = new Date();
        this.menu.btnReporteDiario.addActionListener(this);
        this.menu.btnReporteDiario.setActionCommand("REPORTE-DIARIO");
        this.menu.btnMostraTotalFacturado.addActionListener(this);
        this.menu.btnMostraTotalFacturado.setActionCommand("MOSTRAR-FACTURADO");
        this.menu.btnMasReportes.addActionListener(this);
        this.menu.btnMasReportes.setActionCommand("MAS-REPORTES");
        this.menu.btnAperturaCaja.addActionListener(this);
        this.menu.btnAperturaCaja.setActionCommand("APERTURA-CAJA");
        this.menu.btnGuardarApertura.addActionListener(this);
        this.menu.btnGuardarApertura.addKeyListener(this);
        this.menu.txtEfectivoApertura.addKeyListener(this);
        this.menu.btnGuardarApertura.setActionCommand("GUARDAR-APERTURA");
        this.menu.btnAddTotalV.addActionListener(this);
        this.menu.btnAddTotalV.setActionCommand("ADD-TOTALV");
        this.menu.tblReporte.addMouseListener(this);
        this.menu.btnMostrarInversion.addActionListener(this);
        this.menu.btnMostrarInversion.setActionCommand("MOSTRAR-INVERSION");
        this.menu.btnProductosMasVendidos.addActionListener(this);
        this.menu.btnProductosMasVendidos.setActionCommand("PRODUCTOS-MAS-VENDIDOS");
        this.menu.btnMostrarPmasV.addActionListener(this);
        this.menu.btnMostrarPmasV.setActionCommand("MOSTRARPMASV");
        this.menu.btnImprimirPmasV.addActionListener(this);
        this.menu.btnImprimirPmasV.setActionCommand("IMPRIMIRPMASV");
        this.menu.btnMostrarFacturasEmitidas.addActionListener(this);
        this.menu.btnMostrarFacturasEmitidas.setActionCommand("MOSTRARFACTURASEMITIDAS");
        this.menu.btnDevolverProducto.addActionListener(this);
        this.menu.btnDevolverProducto.setActionCommand("DEVOLVER-PRODUCTO");
        this.menu.btnBuscarFiltroReporte.addActionListener(this);
        this.menu.txtBuscarFactura.addKeyListener(this);
        EstiloTablaTotalV();
        iniciar();
    }

    public void iniciar() {
        reportesDiarios(this.fecha);
        MostrarReportesDario(this.fecha);
        mostrarProductosMasVendidios(this.fecha, this.fecha);
        ReporteGlobal();
        this.menu.jcFechaReporteDario.setDate(this.fecha);
        this.menu.jcFecha1.setDate(this.fecha);
        this.menu.jcFecha2.setDate(this.fecha);
        this.menu.jc1.setDate(this.fecha);
        this.menu.jc2.setDate(this.fecha);
        this.menu.jcFechaReport1.setDate(this.fecha);
        this.menu.jcFechaReport2.setDate(this.fecha);
        this.menu.jcFacturasEmitidas.setDate(this.fecha);
        SumaTotalFiltroReporte(this.fecha, this.fecha);
        inversion();
    }

    public boolean getEstadoC() {
        return estadoC;
    }

    public void setEstadoC(boolean estadoC) {
        this.estadoC = estadoC;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        if (e.getSource() == menu.btnReporteDiario) {
            Date fecha1 = menu.jcFechaReporteDario.getDate(), fecha2 = menu.jcFecha2.getDate();
            reportesDiarios(fecha1);
        }
        if (e.getSource() == menu.btnMostrarFacturasEmitidas) {
            Date fecha = menu.jcFacturasEmitidas.getDate();
            MostrarReportesDario(fecha);
        }
        if (e.getSource() == menu.btnMostraTotalFacturado) {
            Date fecha1 = menu.jcFecha1.getDate(), fecha2 = menu.jcFecha2.getDate();
            long fe1 = fecha1.getTime(), fe2 = fecha2.getTime();
            java.sql.Date fechaInicio = new java.sql.Date(fe1);//convertir la fecha a formato sql
            java.sql.Date fechaFinal = new java.sql.Date(fe2);//convertir la fecha a formato sql
            menu.lblTotalFacturado.setText("" + reportes.IngresosTotales(fechaInicio, fechaFinal));
        }
        if (e.getSource() == menu.btnMasReportes) {
            menu.ventanaMasReportes.setSize(1197, 440);
            menu.ventanaMasReportes.setLocationRelativeTo(null);
            menu.ventanaMasReportes.setVisible(true);
        }
        if (e.getSource() == menu.btnAperturaCaja) {
            menu.AperturasCaja.setSize(395, 290);
            menu.AperturasCaja.setLocationRelativeTo(null);
            this.menu.cmbCajasApertura.setModel(aperturas.mostrarCajas());
            menu.AperturasCaja.setVisible(true);
            menu.jcFechaApertura.setDate(this.fecha);
            menu.txtEfectivoApertura.requestFocus();
        }
        if (e.getSource() == menu.btnMostrarInversion) {
            inversion();
        }
        if (e.getSource().equals(menu.btnGuardarApertura)) {
            GuardarAperturas();
            menu.AperturasCaja.setVisible(false);
        }
        if (e.getSource().equals(menu.btnAddTotalV)) {
            if (!menu.lblTotalFacturado.getText().equals("")) {
                Date fechaInicio = menu.jcFecha1.getDate(), fechaFinal = menu.jcFecha2.getDate();
                SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-YYYY");
                String fecha1 = sdf.format(fechaInicio), fecha2 = sdf.format(fechaFinal), total = menu.lblTotalFacturado.getText();
                String[] fila = {fecha1, fecha2, total};
                this.modelo = (DefaultTableModel) menu.tblMostrarTotalV.getModel();
                this.modelo.addRow(fila);
                menu.lblTotalFacturado.setText("");
            } else {

            }
        }
        if (e.getSource() == menu.btnGuardarCierre) {
            RealizarCorte();
        }
        if (e.getSource() == menu.btnProductosMasVendidos) {
            menu.ventanaProductosMasVendidos.setLocationRelativeTo(null);
            menu.ventanaProductosMasVendidos.setSize(1165, 350);
            menu.ventanaProductosMasVendidos.setVisible(true);
        }
        if (e.getSource() == menu.btnMostrarPmasV) {
            Date fecha1 = menu.jc1.getDate(), fecha2 = menu.jc2.getDate();
            mostrarProductosMasVendidios(fecha1, fecha2);
        }
        if(e.getSource() == menu.btnBuscarFiltroReporte)
        {
            Date fecha1 = menu.jcFechaReport1.getDate(), fecha2 = menu.jcFechaReport2.getDate();
            SumaTotalFiltroReporte(fecha1, fecha2);
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        // throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        if (e.getSource() == menu.tblReporte) {
            if (e.getClickCount() == 2) {
                int id = 0, filaseleccionada = menu.tblReporte.getSelectedRow();
                try {
                    if (filaseleccionada == -1) {

                    } else {
                        this.modelo = (DefaultTableModel) menu.tblReporte.getModel();
                        id = Integer.parseInt(this.modelo.getValueAt(filaseleccionada, 0).toString());
                        MostrarDetalleFactura(id);
                        menu.vistaDetalleFacturas.setSize(782, 320);//762, 363
                        menu.vistaDetalleFacturas.setVisible(true);
                        menu.vistaDetalleFacturas.setLocationRelativeTo(null);
                    }
                } catch (Exception err) {
                    JOptionPane.showMessageDialog(null, err);
                }
            }
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        // throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.

    }

    @Override
    public void mouseReleased(MouseEvent e) {
        // throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void mouseExited(MouseEvent e) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    //para la inversion
    public void inversion() {
        float cordobas = this.reportes.inversionCordobas(),
                dolar = this.reportes.inversionDolar(),
                precioDolar = Float.parseFloat(menu.txtPrecioDolar.getText()),
                total = 0;
        if(menu.isNumeric(String.valueOf(precioDolar))){
            total = cordobas + (dolar * precioDolar);
            menu.lblInversion.setText("" + total);
        }
    }

    public void reportesDiarios(Date fecha) {
        float totalVendidio = 0, exisCaja = 0, ingresosVentaE = 0, ingresosVentasT = 0, ingresosPagosE, ingresosPagoT = 0, ingresosEfectivo = 0, Ingresosbancos = 0, creditos = 0, egresos = 0, base = 0;
        long f1 = fecha.getTime();
        java.sql.Date fechaInicio = new java.sql.Date(f1);//convertir la fecha a formato sql
        //base
        base = reportes.baseEfectivoDiario(fechaInicio);
        //ingresos por ventas en efectivo
        ingresosVentaE = reportes.ingresoEfectivoCajaDiario(fechaInicio);
        //ingresos por ventas cobradas con tarjeta
        ingresosVentasT = reportes.IngresoAbancosDiario(fechaInicio);
        //ingresos por pagos cobrados en efectivo
        ingresosPagosE = reportes.totalPagosEfectivoDiario(fechaInicio);
        //ingresos por pagos cobrados con tarjeta
        ingresosPagoT = reportes.totalPagosTarjetaDiario(fechaInicio);
        //ingresos totales diarios en efectivo
        ingresosEfectivo = reportes.ingresoEfectivoCajaDiario(fechaInicio) + reportes.totalPagosEfectivoDiario(fechaInicio) + reportes.ingresoDiarioEfectivo(fechaInicio);
        //ingreso a bancos por ventas con tarjeta y pagos con tarjeta diarios
        Ingresosbancos = reportes.IngresoAbancosDiario(fechaInicio) + reportes.totalPagosTarjetaDiario(fechaInicio);
        //creditos realizados 
        //creditos = reportes.TotalCreditosDiario(fechaInicio)-(reportes.totalPagosEfectivoDiario(fechaInicio) + reportes.totalPagosTarjetaDiario(fechaInicio));
        creditos = reportes.TotalCreditosDiario(fechaInicio);
        //egresos realizados
        egresos = reportes.TotalGastosDiario(fechaInicio);
        //total vendido
        totalVendidio = reportes.IngresosTotalesDiario(fechaInicio);
        //existencia real en caja
        exisCaja = (ingresosEfectivo + base) - egresos;
        //llenar los lbls
        menu.lblBase.setText("" + base);
        menu.lblVentasEfectivoDiario.setText("" + ingresosVentaE);
        menu.lblVentasTarjetaDiario.setText("" + ingresosVentasT);
        menu.lblIngresosPagosEfectivoDiario.setText("" + ingresosPagosE);
        menu.lblIngresosPagosTarjetaDiario.setText("" + ingresosPagoT);
        menu.lblIngresoEfectivo.setText(""+reportes.ingresoDiarioEfectivo(fechaInicio));
        menu.lblCreditosDiarios.setText("" + creditos);
        menu.lblEgresosDiarios.setText("" + egresos);
        menu.lblTotalExistenciaCajaDiario.setText("" + exisCaja);
        menu.lblIngresosBancosDiario.setText("" + Ingresosbancos);
        menu.lbltotalVendidoDiario.setText("" + totalVendidio);

    }
    //funcion para filtros de reportes por rangos
    public void SumaTotalFiltroReporte(Date fecha1, Date fecha2) {
        float totalVendidio = 0, exisCaja = 0, ingresosEfectivo = 0, Ingresosbancos = 0, creditos = 0, Egresos = 0;
        long f1 = fecha1.getTime(), f2 = fecha2.getTime();
        java.sql.Date fechaInicio = new java.sql.Date(f1);//convertir la fecha a formato sql
        java.sql.Date fechaFinal = new java.sql.Date(f2);//convertir la fecha a formato sql
        //ventas en efectivo
        menu.lblTotalVentasEfectivoFiltro.setText(""+reportes.ingresoEfectivoCaja(fechaInicio, fechaFinal));
        //ingreso de efectivo a caja
        ingresosEfectivo = reportes.ingresoEfectivoCaja(fechaInicio, fechaFinal) + reportes.totalPagosEfectivo(fechaInicio, fechaFinal) + reportes.ingresoEfecitivoRango(fechaInicio, fechaFinal);
        menu.lblIngresosEfectivo.setText(""+reportes.ingresoEfecitivoRango(fechaInicio, fechaFinal));
        //ingresos ventas con tarjetas
        menu.lblVentasTarjetaFiltro.setText(""+reportes.IngresoAbancos(fechaInicio, fechaFinal));
        //ingreso´por abonos en efectivo
        menu.lblAbonosEfectivoFiltro.setText(""+reportes.totalPagosEfectivo(fechaInicio, fechaFinal));
        //ingreso por abonos con tarjeta
        menu.lblAbonosTarjetaFiltro.setText(""+reportes.totalPagosTarjeta(fechaInicio, fechaFinal));
        //ingresos a bancos
        Ingresosbancos = reportes.IngresoAbancos(fechaInicio, fechaFinal) + reportes.totalPagosTarjeta(fechaInicio, fechaFinal);
        menu.lblTotalBancosReportFiltro.setText(""+Ingresosbancos);
        //creditos realizados
        creditos = reportes.TotalCreditosMensual(fechaInicio, fechaFinal)-(reportes.totalPagosEfectivo(fechaInicio, fechaFinal) + reportes.totalPagosTarjeta(fechaInicio, fechaFinal));
        menu.lblCreditosReportFiltro.setText(""+creditos);
        //salida de efectivo
        Egresos = reportes.TotalGastos(fechaInicio, fechaFinal);
        menu.lblSalidaEfectivoFiltro.setText(""+Egresos);
        //total vendido
        totalVendidio = reportes.IngresosTotales(fechaInicio, fechaFinal);
        menu.lblTotalVendidoReportFiltro.setText(""+totalVendidio);
        //total existencia en caja
        exisCaja = ingresosEfectivo - Egresos;
        menu.lblTotalEfectivoCajaFiltro.setText(""+exisCaja);
    }

    public void ReporteGlobal() {
        float totalVendidio = 0, exisCaja = 0, ingresosVentasE = 0, IngresosVentasT = 0, IngresosPagosE = 0, IngresosPagosT = 0, Ingresosbancos = 0, creditos = 0, Egresos = 0, apertura = 0, IngresoEfectivo = 0 ;
        //aperturas a restar al efectivo en caja
        apertura = reportes.TotalAperturasCajaGlobal() - reportes.PrimeraApertura();
        //ingreso de efectivo a caja
        ingresosVentasE = reportes.ingresoEfectivoCajaGlobal();
        menu.lblIngresosCajaMes.setText("" + ingresosVentasE);
        //ingresos por ventas con tarjeta
        IngresosVentasT = reportes.IngresoAbancosGlobal();
        menu.lblIngresosVentasTarjetaMes.setText("" + IngresosVentasT);
        //ingresos por pagos en efectivo
        IngresosPagosE = reportes.totalPagosEfectivoGlobal();
        menu.lblIngresosPagosEfectivoMes.setText("" + IngresosPagosE);
        //Ingresos por pagos con tarjeta
        IngresosPagosT = reportes.totalPagosTarjetaGlobal();
        menu.lblIngresosPagosTarjetaMes.setText("" + IngresosPagosT);
        //
        IngresoEfectivo = reportes.TotalIngresoEfectivoGlobal();
        menu.lblIngresoEfectivoGlobal.setText(""+IngresoEfectivo);
        //ingresos a bancos
        Ingresosbancos = reportes.IngresoAbancosGlobal() + reportes.totalPagosTarjetaGlobal();
        menu.lblIngresosBancoFiltro.setText("" + Ingresosbancos);
        //creditos realizados
        creditos = reportes.TotalCreditosGlobal() - (reportes.totalPagosEfectivoGlobal() + reportes.totalPagosTarjetaGlobal());
        menu.lblCreditosFiltro.setText("" + creditos);
        //salida de efectivo
        Egresos = reportes.TotalGastosGlobal();
        menu.lblEgresosFiltro.setText("" + Egresos);
        //total vendido
        totalVendidio = reportes.IngresosTotalesGlobal();
        menu.lblTotalVendidoFiltro.setText("" + totalVendidio);
        //total existencia en caja
        exisCaja =(IngresosPagosE + ingresosVentasE + IngresoEfectivo) - Egresos;
        menu.lblExistenciaCajaFiltro.setText("" + exisCaja);
    }

    public void MostrarReportesDario(Date fecha1)//metodo para llenar la tabla de reortes por rango o mensual del menu reportes
    {
        long f1 = fecha1.getTime();//
        java.sql.Date fechaInicio = new java.sql.Date(f1);//convertir la fecha a formato sql
        /*String totalCreditoMensual = reportes.TotalCreditosMensual(fechaInicio, fechaFinal);//obtengo el valor de total de creditos cn la funcion TotalCreditoMensula de la clase reortes
        String totalGastos = reportes.TotalGastos(fechaInicio, fechaFinal);//total de gastos 
        String totalPagos = reportes.totalPagos(fechaInicio, fechaFinal);//total pagos 
        float creditosPendiente = Float.parseFloat(totalCreditoMensual) - Float.parseFloat(totalPagos);//refleja el creditos menos los pagos*/
        menu.tblReporte.getTableHeader().setFont(new Font("Sugoe UI", Font.PLAIN, 14));
        menu.tblReporte.getTableHeader().setOpaque(false);
        menu.tblReporte.getTableHeader().setBackground(new Color(100, 100, 100));
        menu.tblReporte.getTableHeader().setForeground(new Color(255, 255, 255));
        menu.tblReporte.getTableHeader().setPreferredSize(new java.awt.Dimension(0, 35));
        try {
            menu.tblReporte.setModel(reportes.ReporteDiario(fechaInicio));
            /*menu.lblTotalCreditosFiltroReporte.setText(String.valueOf(creditosPendiente));//lleno el lblTotalCreditoFiltroRepote con el  total creditos 
            menu.lblGastos.setText(totalGastos);//lleno lblGastos con el total de gastos
            menu.lblTotalPagos.setText(totalPagos);*/

        } catch (Exception err) {

        }

    }

    public void MostrarDetalleFactura(int id)//metodo para llenar la tabla que muestra el detalle de las facturas de reportes
    {
        menu.tblMostrarDetalleFactura.getTableHeader().setFont(new Font("Sugoe UI", Font.PLAIN, 14));
        menu.tblMostrarDetalleFactura.getTableHeader().setOpaque(false);
        menu.tblMostrarDetalleFactura.getTableHeader().setBackground(new Color(100, 100, 100));
        menu.tblMostrarDetalleFactura.getTableHeader().setForeground(new Color(255, 255, 255));
        menu.tblMostrarDetalleFactura.setModel(reportes.DetalleFactura(id));
    }

    public void LimpiarAperturas() {
        menu.txtEfectivoApertura.setText("");
    }

    public void LimpiarCierre() {
        menu.txtExistenciaEfectivoCierre.setText("");
        menu.txtBancosCierre.setText("");
        menu.txtCreditosCierre.setText("");
        menu.txtEgresosCierre.setText("");
        menu.txtTotalVendidoCierre.setText("");
        menu.txtDescripcionCierre.setText("");
        menu.txtFechaInicioCierre.setText("");
        menu.txtFechaFinalCierre.setText("");
        menu.txtMesCierre.setText("");
        menu.txtIngresoEfectivoCierre.setText("");
        menu.txtCajaCierre.setText("");
    }

    public void GuardarAperturas() {
        String efectivo = menu.txtEfectivoApertura.getText();
        Date fecha = menu.jcFechaApertura.getDate();
        long fec = fecha.getTime(), fechaActual = this.fecha.getTime();
        java.sql.Date f = new java.sql.Date(fec);
        java.sql.Date f1 = new java.sql.Date(fechaActual);
        int caja = aperturas.IdCaja(menu.cmbCajasApertura.getSelectedItem().toString());
        if (menu.isNumeric(menu.txtEfectivoApertura.getText())) {
            if (!efectivo.equals("")) {
                float efectivoA = Float.parseFloat(efectivo);
                aperturas.GuardarAperturas(f, caja, efectivoA);
                menu.lblBase.setText("" + reportes.baseEfectivoDiario(f1));
                reportesDiarios(f1);
                LimpiarAperturas();
                ReporteGlobal();
            }

        } else {

        }
    }
//funcion pára realizar el corte por los momentos no se esta usando
    public void RealizarCorte() {
        String descripcion = menu.txtDescripcionCierre.getText(), fecha = menu.txtFechaInicioCierre.getText();;
        String efectivo = menu.txtIngresoEfectivoCierre.getText(),
                bancos = menu.txtBancosCierre.getText(),
                creditos = menu.txtCreditosCierre.getText(),
                totalV = menu.txtTotalVendidoCierre.getText(),
                Egresos = menu.txtEgresosCierre.getText(),
                existCaja = menu.txtExistenciaEfectivoCierre.getText(),
                fechaInicio = menu.txtFechaInicioCierre.getText(),
                fechaFinal = menu.txtFechaFinalCierre.getText(),
                mes = menu.txtMesCierre.getText();
        int caja = aperturas.IdCaja(menu.cmbCajasApertura.getSelectedItem().toString());
        if (menu.isNumeric(efectivo) && menu.isNumeric(bancos) && menu.isNumeric(creditos) && menu.isNumeric(totalV) && menu.isNumeric(Egresos) && menu.isNumeric(existCaja)) {
            if (!efectivo.equals("") && !bancos.equals("") && !creditos.equals("") && !totalV.equals("") && !Egresos.equals("") && !existCaja.equals("")) {
                float efectivoC = Float.parseFloat(efectivo),
                        bancosC = Float.parseFloat(bancos),
                        creditosC = Float.parseFloat(creditos),
                        totalVC = Float.parseFloat(totalV),
                        EgresosC = Float.parseFloat(Egresos),
                        ExistenciaCaja = Float.parseFloat(existCaja);
                aperturas.GuardarCierre(fechaInicio, fechaFinal, mes, caja, efectivoC, bancosC, creditosC, EgresosC, totalVC, ExistenciaCaja, descripcion);
                LimpiarCierre();
            }

        } else {

        }
    }

    public void EstiloTablaTotalV() {
        menu.tblMostrarTotalV.getTableHeader().setFont(new Font("Sugoe UI", Font.PLAIN, 14));
        menu.tblMostrarTotalV.getTableHeader().setOpaque(false);
        menu.tblMostrarTotalV.getTableHeader().setBackground(new Color(100, 100, 100));
        menu.tblMostrarTotalV.getTableHeader().setForeground(new Color(255, 255, 255));
    }

    public void mostrarProductosMasVendidios(Date fecha1, Date fecha2) {
        menu.tblProductosMasVendidos.getTableHeader().setFont(new Font("Sugoe UI", Font.PLAIN, 14));
        menu.tblProductosMasVendidos.getTableHeader().setOpaque(false);
        menu.tblProductosMasVendidos.getTableHeader().setBackground(new Color(100, 100, 100));
        menu.tblProductosMasVendidos.getTableHeader().setForeground(new Color(255, 255, 255));
        long f1 = fecha1.getTime(), f2 = fecha2.getTime();
        java.sql.Date ff1 = new java.sql.Date(f1);
        java.sql.Date ff2 = new java.sql.Date(f2);
        menu.tblProductosMasVendidos.setModel(reportes.productosMasVendidos(ff1, ff2));
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.VK_ENTER == e.getKeyCode()) {
            //busqueda de factura por numero de facturas
            String id = menu.txtBuscarFactura.getText();
            if (menu.isNumeric(id)) {
                Date fecha = menu.jcFacturasEmitidas.getDate();
                long ff1 = fecha.getTime();
                java.sql.Date f = new java.sql.Date(ff1);
                try {
                    menu.tblReporte.setModel(this.reportes.BuscarFactura(Integer.parseInt(id)));
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "");
                }
            }

        }
        if(e.VK_SPACE == e.getKeyCode()){
            GuardarAperturas();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

}
