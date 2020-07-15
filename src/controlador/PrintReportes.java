/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/*
 * PrintReportes.java
 * 
 * Copyright 2013 Josue Camara <picharras@picharras-HP-Folio>
 * 
 * Este programa es software libre; puede redistribuirlo y/o modificarlo
 * bajo los términos de la Licencia Pública General GNU publicada por
 * la Free Software Foundation; versión 2 de la Licencia, o
 * (a su elección) cualquier versión posterior.
 * 
 * Este programa se distribuye con la esperanza de que sea útil,
 * pero SIN NINGUNA GARANTIA; sin siquiera la garantía implícita de
 * COMERCIABILIDAD O IDONEIDAD PARA UN FIN PARTICULAR. Ver el
 * Licencia Pública General GNU para más detalles.
 * 
 * Debería haber recibido una copia de la Licencia Pública General GNU
 * junto con este programa; si no, escriba al Software Libre
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston,
 * MA 02110-1301, ESTADOS UNIDOS.
 * 
 */
package controlador;

import java.io.FileWriter;
import javax.print.Doc;
import javax.print.DocFlavor;
import javax.print.DocPrintJob;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.ServiceUI;
import javax.print.SimpleDoc;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.swing.JOptionPane;

/**
 *
 * @author CESAR DIAZ MARADIAGA
 */
public class PrintReportes {

  
  //Ticket attribute content
  private String contentTicketDiario = ""+
  "{{nombreTienda}}           \n"+
  "REPORTE DEL DIA {{fecha}}      \n"+
  "\n"+
  "Efctivo de apertura caja          {{base}}\n"+
  "Ingreso por ventas en efectivo    {{ventasE}}\n"+
  "Ingreso por ventas con tarjeta    {{ventasT}}\n"+
  "Ingresos por pagos en efectivo    {{pagosE}}\n"+
  "Ingresos por pagos con tarjeta    {{pagosT}}\n"+
  "Ingresos de efectivo              {{ingresosE}}\n"+
  "Creditos                          {{creditos}}\n"+
  "Egresos de efectivo de caja       {{egresos}}\n"+
  "==============================================N\n"+
  "Total efectivo en caja            {{existCaja}}\n"+
  "Total Bancos                      {{bancos}}\n"+
  "Total vendido                     {{totalV}}\n"+
  "..\n\n\n\n\n";
  private String ticketTotalV = ""+
  "{{nombreTienda}}            \n"+
  "\n"+
  "Fecha Inicio      Fecha Final      Total Vendido\n"+
  "\n"+
  "{{datos}}"+
          "\n\n\n\n\n";
  private String contentTicketGlobal = ""+
  "{{nombreTienda}}           \n"+
  "REPORTE GENERAL GLOBAL      \n"+
  "\n"+
  "Ingreso por ventas en efectivo     {{ventasE}}\n"+
  "Ingreso por ventas con tarjeta     {{ventasT}}\n"+
  "Ingresos por pagos en efectivo     {{pagosE}}\n"+
  "Ingresos por pagos con tarjeta     {{pagosT}}\n"+
  "Ingresos de efectivo               {{ingresosE}}\n"+
  "Creditos                           {{creditos}}\n"+
  "Egresos de efectivo de caja        {{egresos}}\n"+
  "===============================================N\n"+
  "Total efectivo en caja             {{existCaja}}\n"+
  "Total Bancos                       {{bancos}}\n"+
  "Total vendido                      {{totalV}}\n"+
  "..\n\n\n\n\n";
  
  //bussines Intelligense
  private String BI = ""+
  "{{nombreTienda}}            \n"+
  "Productos mas vendidos o solicitados\n"+
  "Fecha: {{fecha1}} Hasta {{fecha2}}\n"+
  "------------------------------------------------\n"+
  "N°   Nombre                         Vendido\n"+
  "-------------------------------------------------\n"+
  "{{producto}}\n"+
   "\n\n\n\n\n";
  
  //TICKET DE ABONOS DE CREDITOS
  private String ReciboPago = ""+
  "{{tienda}}\n"+
  "COMPROBANTE DE PAGO\n"+
  "Fecha : {{fecha}}\n"+
  "Cliente: {{cliente}}\n"+
  "N° Credito: {{credito}}\n"+
  "Monto de pago :{{monto}}\n"+
  "Saldo: {{saldo}}\n\n";
  //constructor
    PrintReportes() {

    }
   
    public void llenarTicketPago(String tienda, String fecha, String cliente, String credito, String monto, String saldo)
    {
        this.ReciboPago = this.ReciboPago.replace("{{tienda}}", tienda);
        this.ReciboPago = this.ReciboPago.replace("{{fecha}}", fecha);
        this.ReciboPago = this.ReciboPago.replace("{{cliente}}", cliente);
        this.ReciboPago = this.ReciboPago.replace("{{credito}}", credito);
        this.ReciboPago = this.ReciboPago.replace("{{monto}}", monto);
        this.ReciboPago = this.ReciboPago.replace("{{saldo}}", saldo);
        System.out.println(this.ReciboPago);
    }
    
    public void llenarTicketDiario(String NombreTienda, String fecha, String base, String ventasEfectivo, String ventasT, String pagosE, String pagosT, String ingresosE, String creditos, String egreso, String existCaja, String bancos, String totalV)
    {
        this.contentTicketDiario = this.contentTicketDiario.replace("{{nombreTienda}}", NombreTienda);
        this.contentTicketDiario = this.contentTicketDiario.replace("{{fecha}}",fecha);
        this.contentTicketDiario = this.contentTicketDiario.replace("{{base}}",base);
        this.contentTicketDiario = this.contentTicketDiario.replace("{{ventasE}}",ventasEfectivo);
        this.contentTicketDiario = this.contentTicketDiario.replace("{{ventasT}}",ventasT);
        this.contentTicketDiario = this.contentTicketDiario.replace("{{pagosE}}",pagosE);
        this.contentTicketDiario = this.contentTicketDiario.replace("{{pagosT}}",pagosT);
        this.contentTicketDiario = this.contentTicketDiario.replace("{{ingresosE}}",ingresosE);
        this.contentTicketDiario = this.contentTicketDiario.replace("{{creditos}}",creditos);
        this.contentTicketDiario = this.contentTicketDiario.replace("{{egresos}}",egreso);
        this.contentTicketDiario = this.contentTicketDiario.replace("{{existCaja}}",existCaja);
        this.contentTicketDiario = this.contentTicketDiario.replace("{{bancos}}",bancos);
        this.contentTicketDiario = this.contentTicketDiario.replace("{{totalV}}",totalV);
        System.out.println(this.contentTicketDiario);
    }
    public void llenarTicketTotalV(String[] datos, String tienda)
    {
        StringBuffer a = new StringBuffer("");
        for(int i=0;i<datos.length;i++)
        {
            a.append(datos[i]);
        }
        this.ticketTotalV = this.ticketTotalV.replace("{{nombreTienda}}", tienda);
        this.ticketTotalV = this.ticketTotalV.replace("{{datos}}",a);
        //System.out.println(this.ticketTotalV);
    }
    //bussines 
    public void BIP(String tienda, String fecha1, String fecha2 ,String[] P)
    {
        StringBuffer Producto = new StringBuffer("");
        int N = 0;
        for(int i=0;i<P.length;i++)
        {
            Producto.append(P[i]+"\n");
        }
        this.BI = this.BI.replace("{{nombreTienda}}", tienda);
        this.BI = this.BI.replace("{{fecha1}}", fecha1);
        this.BI = this.BI.replace("{{fecha2}}", fecha2);
        this.BI = this.BI.replace("{{producto}}",Producto);
        System.out.println(this.BI);
    }
    public void llenarTicketGlobal(String NombreTienda, String ventasEfectivo, String ventasT, String pagosE, String pagosT, String ingresosE, String creditos, String egreso, String existCaja, String bancos, String totalV)
    {
        this.contentTicketGlobal = this.contentTicketGlobal.replace("{{nombreTienda}}", NombreTienda);
        this.contentTicketGlobal = this.contentTicketGlobal.replace("{{ventasE}}",ventasEfectivo);
        this.contentTicketGlobal = this.contentTicketGlobal.replace("{{ventasT}}",ventasT);
        this.contentTicketGlobal = this.contentTicketGlobal.replace("{{pagosE}}",pagosE);
        this.contentTicketGlobal = this.contentTicketGlobal.replace("{{pagosT}}",pagosT);
        this.contentTicketGlobal = this.contentTicketGlobal.replace("{{ingresosE}}",ingresosE);
        this.contentTicketGlobal = this.contentTicketGlobal.replace("{{creditos}}",creditos);
        this.contentTicketGlobal = this.contentTicketGlobal.replace("{{egresos}}",egreso);
        this.contentTicketGlobal = this.contentTicketGlobal.replace("{{existCaja}}",existCaja);
        this.contentTicketGlobal = this.contentTicketGlobal.replace("{{bancos}}",bancos);
        this.contentTicketGlobal = this.contentTicketGlobal.replace("{{totalV}}",totalV);
        System.out.println(this.contentTicketGlobal);
    }
  public void print(String TipoReport) {
    //Especificamos el tipo de dato a imprimir
    //Tipo: bytes; Subtipo: autodetectado
    DocFlavor flavor = DocFlavor.BYTE_ARRAY.AUTOSENSE;
    
    //Aca obtenemos el servicio de impresion por defatul
    //Si no quieres ver el dialogo de seleccionar impresora usa esto
    //PrintService defaultService = PrintServiceLookup.lookupDefaultPrintService();
    
    
    //Con esto mostramos el dialogo para seleccionar impresora
    //Si quieres ver el dialogo de seleccionar impresora usalo
    //Solo mostrara las impresoras que soporte arreglo de bits
    PrintRequestAttributeSet pras = new HashPrintRequestAttributeSet();
    PrintService printService[] = PrintServiceLookup.lookupPrintServices(flavor, pras);
    PrintService defaultService = PrintServiceLookup.lookupDefaultPrintService();
    PrintService service = ServiceUI.printDialog(null, 700, 200, printService, defaultService, flavor, pras);
      
    //Creamos un arreglo de tipo byte
    byte[] bytes = {};

    //Aca convertimos el string(cuerpo del ticket) a bytes tal como
    //lo maneja la impresora(mas bien ticketera :p)
    switch(TipoReport){
        case "Diario":{
             bytes = this.contentTicketDiario.getBytes();
        }break;
        case "TotalV":{
            bytes = this.ticketTotalV.getBytes();
        }break;
        case "Global":
        {
            bytes = this.contentTicketGlobal.getBytes();
        }break;
        case "BI":{
            bytes = this.BI.getBytes();
        }break;
        case "Pago":{
            bytes = this.ReciboPago.getBytes();
        }break;
    }
    

    //Creamos un documento a imprimir, a el se le appendeara
    //el arreglo de bytes
    Doc doc = new SimpleDoc(bytes,flavor,null);
      
    //Creamos un trabajo de impresión
    DocPrintJob job = service.createPrintJob();

    //Imprimimos dentro de un try de a huevo
    try {
      //El metodo print imprime
      job.print(doc, null);
    } catch (Exception er) {
      JOptionPane.showMessageDialog(null,"Error al imprimir: " + er.getMessage());
    }
  }


}
