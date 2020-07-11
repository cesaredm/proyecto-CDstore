/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controlador;

/*
 * Ticket.java

 * 
 */

import java.awt.*;
import java.awt.print.*;
import javax.swing.JOptionPane;
import javax.print.DocFlavor;
import javax.print.DocPrintJob;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.SimpleDoc;
import javax.print.Doc;
import javax.print.ServiceUI;
import javax.print.attribute.*;

public class Ticket {
  
  //Ticket attribute content
  private String contentTicket = ""+
    "{{nameLocal}}\n"+
    "{{direccion}}\n"+
    "=================================N \n"+
    "Tel:{{telefono}}\n"+
    "RUT:{{RFC}}\n"+
    "Rango Permitido: {{Rango}}\n"+
    "Caja # {{box}} - Factura # {{ticket}}\n"+
    "ATENDIO: {{Cajero}} \n"+
    "Cliente: {{cliente}} {{comprador}}\n"+
    "Venta:   {{tipoVenta}} \n"+
    "Pago:    {{formaPago}}\n"+
    "Fecha:   {{dateTime}} \n"+
          "\n"+
    "DESCRIP    CANT. PRECIO IMPORTE\n"+
    "=================================N \n"+
    "{{items}} \n"+
    "=================================N \n"+
    "SUBTOTAL: {{subTotal}}\n"+
    "IVA: {{iva}}\n"+
    "TOTAL:{{total}} \n"+
    "RECIBIDO: {{recibo}}\n"+
    "CAMBIO: {{change}}\n"+
    "=================================N \n"+
    "GRACIAS POR SU COMPRA..\n"+
    "ESPERAMOS SU VISITA NUEVAMENTE.\n"+
    "\n \n";
    
  //El constructor que setea los valores a la instancia
  Ticket(String nameLocal, String direccion, String telefono, String RFC, String Rango, String box, String ticket, String caissier, String comprador, String cliente, String tipoVenta, String formaPago, String dateTime, String[] items, String subTotal, String iva, String total, String recibo, String change) {
    StringBuffer a = new StringBuffer("");
    this.contentTicket = this.contentTicket.replace("{{nameLocal}}", nameLocal);
    this.contentTicket = this.contentTicket.replace("{{direccion}}", direccion);
    this.contentTicket = this.contentTicket.replace("{{telefono}}", telefono);
    this.contentTicket = this.contentTicket.replace("{{RFC}}", RFC);
    this.contentTicket = this.contentTicket.replace("{{Rango}}", Rango);
    this.contentTicket = this.contentTicket.replace("{{box}}", box);
    this.contentTicket = this.contentTicket.replace("{{ticket}}", ticket);
    this.contentTicket = this.contentTicket.replace("{{Cajero}}", caissier);
    this.contentTicket = this.contentTicket.replace("{{comprador}}", comprador);
    this.contentTicket = this.contentTicket.replace("{{cliente}}", cliente);
    this.contentTicket = this.contentTicket.replace("{{tipoVenta}}", tipoVenta);
    this.contentTicket = this.contentTicket.replace("{{formaPago}}", formaPago);
    this.contentTicket = this.contentTicket.replace("{{dateTime}}", dateTime);
    //recorro el array de Productos ITEMS
    for(int i=0;i<items.length;i++)
    {
        a.append(items[i]);
    }
    this.contentTicket = this.contentTicket.replace("{{items}}",a);
    this.contentTicket = this.contentTicket.replace("{{subTotal}}", subTotal);
    this.contentTicket = this.contentTicket.replace("{{iva}}", iva);
    this.contentTicket = this.contentTicket.replace("{{total}}", total);
    this.contentTicket = this.contentTicket.replace("{{recibo}}", recibo);
    this.contentTicket = this.contentTicket.replace("{{change}}", change);
  }
    
  public void print() {
    System.out.println(this.contentTicket);
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
    byte[] bytes;

    //Aca convertimos el string(cuerpo del ticket) a bytes tal como
    //lo maneja la impresora(mas bien ticketera :p)
    bytes = this.contentTicket.getBytes();

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

