/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import vista.IMenu;
import modelo.InfoFactura;
/**
 *
 * @author CESAR DIAZ MARADIAGA
 */
public class CtrlImprimirReport extends PrintReportes implements ActionListener{
    IMenu menu;
    InfoFactura info;
    DefaultTableModel modelo;
    public CtrlImprimirReport(IMenu menu, InfoFactura info)
    {
        this.menu = menu;
        this.info = info;
        this.modelo = new DefaultTableModel();
        this.menu.btnMostrarInversion.addActionListener(this);
        this.menu.btnImprimirReporteDiario.addActionListener(this);
        this.menu.btnImprimirTotalV.addActionListener(this);
        this.menu.btnImprimirReporteGlobal.addActionListener(this);
        this.menu.btnImprimirPmasV.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource() == menu.btnImprimirReporteGlobal)
        {
            info.obtenerInfoFactura();
            String nombreTienda = info.getNombre();
            String efectivoB = menu.lblIngresosCajaMes.getText(), ventasT = menu.lblIngresosVentasTarjetaMes.getText(), pagosE = menu.lblIngresosPagosEfectivoMes.getText(),
                    pagosT = menu.lblIngresosPagosTarjetaMes.getText(),creditos= menu.lblCreditosFiltro.getText(),existCaja=menu.lblExistenciaCajaFiltro.getText(),
                    bancos = menu.lblIngresosBancoFiltro.getText(),totalV=menu.lblTotalVendidoFiltro.getText(), egresos = menu.lblEgresosFiltro.getText();
                llenarTicketGlobal(nombreTienda,efectivoB, ventasT, pagosE, pagosT, creditos, egresos, existCaja, bancos, totalV); 
                try {
                print("Global");
            } catch (Exception err) {
                //JOptionPane.showMessageDialog(null, err+" en la funcion de btn ImprimirReporteglobal");
            }
                
        }
        
        if(e.getSource() == menu.btnImprimirReporteDiario)
        {
            info.obtenerInfoFactura();
            String nombreTienda = info.getNombre();
            Date fechaInicio = menu.jcFechaReporteDario.getDate();
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-YYYY");
            String fechaR = sdf.format(fechaInicio),base = menu.lblBase.getText(), ventasE = menu.lblVentasEfectivoDiario.getText(), ventasT = menu.lblVentasTarjetaDiario.getText(),
                    pagosE = menu.lblIngresosPagosEfectivoDiario.getText(), pagosT = menu.lblIngresosPagosTarjetaDiario.getText(),
                    creditos = menu.lblCreditosDiarios.getText(), egresos = menu.lblEgresosDiarios.getText(), existCaja = menu.lblTotalExistenciaCajaDiario.getText(),
                    bancos = menu.lblIngresosBancosDiario.getText(), totalV = menu.lbltotalVendidoDiario.getText();
            llenarTicketDiario(nombreTienda,fechaR,base, ventasE, ventasT, pagosE, pagosT, creditos, egresos, existCaja, bancos, totalV);
            try {
                print("Diario");
            } catch (Exception err) {
                //JOptionPane.showMessageDialog(null, "");
            }
        }
        
        if(e.getSource() == menu.btnImprimirTotalV)
        {
            info.obtenerInfoFactura();
            String nombreTienda = info.getNombre();
            String[] datos;
            String f1, f2, t;
            int filas = menu.tblMostrarTotalV.getRowCount();
            datos = new String[filas];
            for(int i = 0;i<filas;i++)
            {
                f1 = (String) menu.tblMostrarTotalV.getValueAt(i, 0);
                f2 = (String) menu.tblMostrarTotalV.getValueAt(i, 1);
                t = (String) menu.tblMostrarTotalV.getValueAt(i, 2);
                datos[i] =f1+"      "+f2+"      "+t+"\n";
            }
            llenarTicketTotalV(datos, nombreTienda);
            LimpiarTablaTotalV();
            try {
                print("TotalV");
            } catch (Exception err) {
            }
            
        }
        if(e.getSource() == menu.btnImprimirPmasV)
        {
            info.obtenerInfoFactura();
            int filas = menu.tblProductosMasVendidos.getRowCount(),n=1;
            Date f1 = menu.jc1.getDate(), f2 = menu.jc2.getDate();
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-YYYY");
            String fecha1 = sdf.format(f1), fecha2 = sdf.format(f2), tienda = info.getNombre(),nombre, marca, vendido;
            String[] producto = new String[filas];
            for(int i=0;i<filas;i++)
            {
                n = n+i;
                nombre = (String) menu.tblProductosMasVendidos.getValueAt(i, 1);
                marca = (String) menu.tblProductosMasVendidos.getValueAt(i, 2);
                vendido = (String) menu.tblProductosMasVendidos.getValueAt(i, 4);
                producto[i] = n+" "+nombre+" "+marca+"          "+vendido;
            }
            BIP(tienda, fecha1, fecha2, producto);
            try {
                print("BI");
            } catch (Exception err) {
            }
        }
    }
    public void LimpiarTablaTotalV()
    {
        try {
            this.modelo = (DefaultTableModel) menu.tblMostrarTotalV.getModel();
            int filas = this.modelo.getRowCount();
            for(int i = 0; i<filas;i++)
            {
                this.modelo.removeRow(0);
            }
        } catch (Exception e) {
            
        }
        
    }
}
