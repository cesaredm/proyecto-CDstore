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
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.JOptionPane;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import modelo.*;
import vista.IMenu;

/**
 *
 * @author CESAR DIAZ MARADIAGA
 */
public class CtrlFacturacion implements ActionListener, CaretListener, MouseListener, KeyListener {

    IMenu menu;
    Facturacion factura;
    Date fecha;
    DefaultTableModel modelo;
    CtrlProducto productos;
    CtrlCreditos creditos;
    CtrlReportes reportes;
    Productos modelProduct;
    Reportes r;
    Creditos c;
    SpinnerNumberModel sModel;
    JSpinner spiner;
    static float total;
    float subTotal, isv, descuento;
    String[] nD;

    public CtrlFacturacion(IMenu menu, Facturacion factura) {
        this.fecha = new Date();
        this.total = 0;
        this.isv = 0;
        this.subTotal = 0;
        this.descuento = 0;
        this.menu = menu;
        this.factura = factura;
        this.menu.cmbFormaPago.setModel(factura.FormasPago());
        this.c = new Creditos();
        this.r = new Reportes();
        this.modelo = new DefaultTableModel();
        this.creditos = new CtrlCreditos(menu, c);
        this.reportes = new CtrlReportes(menu, r);
        this.modelProduct = new Productos();
        this.productos = new CtrlProducto(modelProduct, menu);
        this.menu.btnActualizarFactura.setVisible(false);
        this.menu.btnGuardarFactura.addActionListener(this);
        this.menu.btnGuardarFactura.addKeyListener(this);
        this.menu.btnActualizarFactura.addActionListener(this);
        this.menu.btnEliminarFilaFactura.addActionListener(this);
        this.menu.btnNuevaFactura.addActionListener(this);
        this.menu.btnCreditoFactura.addActionListener(this);
        this.menu.tblAddCreditoFactura.addMouseListener(this);
        this.menu.tblAddProductoFactura.addMouseListener(this);
        this.menu.btnEditarImpuesto.addActionListener(this);
        this.menu.btnAgregarProductoFactura.addActionListener(this);
        this.menu.btnEditarFactura.addActionListener(this);
        this.menu.Descuento.addActionListener(this);
        this.menu.btnLimpiarCliente.addActionListener(this);
        this.menu.btnAgregar.addActionListener(this);
        this.menu.btnAgregar.addKeyListener(this);
        this.menu.txtCodBarraFactura.addKeyListener(this);
        this.menu.btnAgregarProductoFactura.addKeyListener(this);
        this.menu.addDescuento.addActionListener(this);
        this.menu.addMasProducto.addActionListener(this);
        this.menu.tblFactura.addKeyListener(this);
        this.menu.txtPagoCon.addCaretListener(this);
        this.menu.txtPagoCon.addKeyListener(this);
        this.menu.txtCambio.addCaretListener(this);
        this.sModel = new SpinnerNumberModel();
        this.sModel.setMinimum(0.00);
        this.sModel.setValue(0.00);
        this.sModel.setStepSize(0.01);
        this.spiner = new JSpinner(sModel);
        EstiloTablaFacturacion();
        editarISV("");
        DeshabilitarBtnGuardarFactura();
        this.menu.jcFechaFactura.setDate(fecha);
        menu.txtNumeroFactura.setText(factura.ObtenerIdFactura());
        menu.txtCodBarraFactura.requestFocus();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        if (e.getSource() == menu.btnGuardarFactura) {
            guardarFactura();
        }
        if (e.getSource() == menu.btnActualizarFactura) {
            actualizarFactura();
        }
        if (e.getSource() == menu.btnEditarFactura) {
            editarFactura();
        }
        if (e.getSource() == menu.btnAgregarProductoFactura) {
            mostrarVentanaProductos();
        }
        if (e.getSource() == menu.btnEliminarFilaFactura) {
            eliminarFilaFactura();
        }
        if (e.getSource() == menu.btnNuevaFactura) {
            nuevaFactura();
        }
        if (e.getSource() == menu.btnEditarImpuesto) {
            actualizarIVA();
        }
        if (e.getSource() == menu.btnCreditoFactura) {
            mostrarVentanaCreditos();
        }
        if (e.getSource() == menu.Descuento) {
//            int filaseleccionada = menu.tblAddProductoFactura.getSelectedRow();
//            String nombreProducto = "";
//            if (filaseleccionada == -1) {
//
//            } else {
//                try {
//                    nombreProducto = (String) menu.tblAddProductoFactura.getValueAt(filaseleccionada, 2);
//                    this.descuento = Float.parseFloat(JOptionPane.showInputDialog(null, "Descuento C$:", "Agregar descuento a " + nombreProducto, JOptionPane.QUESTION_MESSAGE));
//                } catch (Exception err) {
//                    //JOptionPane.showMessageDialog(null, err);
//                }
//            }
        }
        if (e.getSource() == menu.btnLimpiarCliente) {
            limpiarFormularioClienteFactura();
        }
        if (e.getSource() == menu.btnAgregar) {
            AgregarProductoFacturaEnter();
        }
        if (e.getSource() == menu.addDescuento) {
            addDescuento();
        }
        if (e.getSource() == menu.addMasProducto) {
            addMasProducto();
        }
    }

    @Override
    public void caretUpdate(CaretEvent e) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        if(e.getSource() == menu.txtPagoCon){
            String total = menu.txtTotal.getText(), pagoCon = menu.txtPagoCon.getText();
            float cambio = 0;
            if(menu.isNumeric(pagoCon)){
                cambio = Float.parseFloat(pagoCon) - Float.parseFloat(total);
                menu.txtCambio.setText(String.valueOf(cambio));
            }else if (pagoCon.equals("")){
                menu.txtCambio.setText("");
            }
        }

    }

    @Override
    public void mouseClicked(MouseEvent e) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        if (e.getSource() == menu.tblAddProductoFactura) {
            addProductoFactura();
        }
        if (e.getSource() == menu.tblAddCreditoFactura) {
            if (e.getClickCount() == 2) {
                addCreditoFactura();
            }
        }

    }

    @Override
    public void mousePressed(MouseEvent e
    ) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void mouseReleased(MouseEvent e
    ) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void mouseEntered(MouseEvent e
    ) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void mouseExited(MouseEvent e
    ) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.VK_ENTER == e.getKeyCode()) {
            AgregarProductoFacturaEnter();
        }

        if (e.VK_F10 == e.getKeyCode()) {
            mostrarVentanaProductos();
        }
        if (e.VK_SPACE == e.getKeyCode()) {
            guardarFactura();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }

    public void guardarFactura() {
        try {
            this.modelo = (DefaultTableModel) menu.tblFactura.getModel();//obtengo el modelo de tabla factura y sus datos
            int filas = this.modelo.getRowCount();//Cuento las filas de la tabla Factura
            //guardara la factura solo si el boton guardar factura esta habilatado
            if (menu.btnGuardarFactura.isEnabled()) {
                Date fecha;
                String[] ArregloImprimir = new String[filas];
                String factura = "", id, cantidad, precio, totalDetalle, idCredito, iva, totalFactura, formaPago, idFormaPago, comprador, cliente, subtotal, nombreProduct, tipoVenta;//variables para capturar los datos a guardar
                subtotal = menu.txtSubTotal.getText();
                comprador = menu.txtCompradorFactura.getText();
                cliente = menu.txtNClienteFactura.getText() + " " + menu.txtAClienteFactura.getText();
                fecha = menu.jcFechaFactura.getDate();//capturo la fecha del dateshooser
                long fechaF = fecha.getTime();//
                java.sql.Date fechaFactura = new java.sql.Date(fechaF);//convertir la fecha obtenida a formato sql
                idCredito = menu.txtCreditoFactura.getText();//obtengo el numero de credito al que pertenecera la factura
                if (idCredito.equals("")) {
                    tipoVenta = "Contado";
                } else {
                    tipoVenta = "Credito";
                }
                iva = menu.txtImpuesto.getText();//obtengo el iva
                totalFactura = menu.txtTotal.getText();//obtengo total de factura
                formaPago = (String) menu.cmbFormaPago.getSelectedItem();//capturo el nombre de forma de pago 
                idFormaPago = this.factura.ObtenerFormaPago(formaPago);//capturo el id de la forma de pago que retorna la funcion obtenerformapago de la clase facturacion
                this.factura.GuardarFactura(1, fechaFactura, comprador, idCredito, idFormaPago, iva, totalFactura);//envio los datos a guardar de la factura
                for (int cont = 0; cont < filas; cont++)//for para recorrer la tabla factura
                {
                    id = (String) this.modelo.getValueAt(cont, 0);//capturo el id de producto para guardar en detallefactura
                    cantidad = (String) this.modelo.getValueAt(cont, 2);//capturo la cantidad de producto de la columna dos y la paso a String para guardar en detallefactura
                    nombreProduct = (String) this.modelo.getValueAt(cont, 3);//capturo el nombre de producto
                    precio = (String) this.modelo.getValueAt(cont, 4);//capturo el precio de producto para guardar en detallefactura
                    totalDetalle = (String) this.modelo.getValueAt(cont, 5);//capturo el total de detalle compra de producto para guardar en detallefactura
                    factura = menu.txtNumeroFactura.getText();//capturo id de factura ala que pertenece el detalle de factura
                    this.factura.DetalleFactura(factura, id, precio, cantidad, totalDetalle);//envio los datos a guardar de los detalles
                    //this.factura.Vender(id,cantidad);//funcion para diminuir el stock segun la cantidad que se venda
//                    validar si el nombre del producto es mayor de 10 caracteres
                    if (nombreProduct.length() > 10) {
                        nombreProduct = nombreProduct.substring(0, 10);
                    }
                    ArregloImprimir[cont] = nombreProduct + " " + cantidad + "   " + precio + "  " + totalDetalle + "\n";
                }
                menu.txtNumeroFactura.setText(this.factura.ObtenerIdFactura());//Actualizo el campo numero de factura con la funcion obtenerIdFactura 
                menu.txtCodBarraFactura.requestFocus();
                LimpiarTablaFactura();//limpio la factura
                DeshabilitarBtnGuardarFactura();
                productos.MostrarProductos("");
                productos.MostrarProductosVender("");
                creditos.ActualizarEstadoCreditoApendiente();
                creditos.ActualizarEstadoCreditoAabierto();
                creditos.MostrarCreditos("");
                creditos.MostrarCreditosCreados("");
                reportes.MostrarReportesDario(this.fecha);
                reportes.reportesDiarios(this.fecha);
                reportes.ReporteGlobal();
                reportes.SumaTotalFiltroReporte(this.fecha, this.fecha);
                reportes.inversion();
                reportes.mostrarProductosMasVendidios(this.fecha, this.fecha);
                creditos.MostrarCreditos("");
                creditos.MostrarCreditosAddFactura("");
                Imprimir(factura, comprador, cliente, tipoVenta, formaPago, ArregloImprimir, subtotal, iva, totalFactura, fechaFactura.toString());
            } else {
                JOptionPane.showMessageDialog(null, "La factura esta vacia");
            }
        } catch (Exception err) {
            //JOptionPane.showMessageDialog(null, err);
        }
    }

    public void mostrarVentanaProductos() {
        menu.AddProductoFactura.setSize(1071, 456);
        menu.AddProductoFactura.setVisible(true);
        menu.AddProductoFactura.setLocationRelativeTo(null);
        if (menu.rbBuscarNombreCodBarra.isSelected() == true) {
            menu.txtBuscarPorNombre.setEnabled(true);
            menu.txtBuscarPorCategoria.setEnabled(false);
            menu.txtBuscarPorLaboratorio.setEnabled(false);
            menu.txtBuscarPorNombre.requestFocus();
            menu.txtBuscarPorNombre.selectAll();
        }
    }

    public void AgregarProductoFacturaEnter() {
        String codBarra = menu.txtCodBarraFactura.getText();
        String precioDolar = menu.txtPrecioDolar.getText(), id = "";
        int filas = 0;
        float totalImports = 0, sacarImpuesto = 0, porcentajeImp = 0, cantidadUpdate = 0, importeUpdate = 0, cantidadActual = 0, precio = 0;
        if (!menu.isNumeric(precioDolar) || precioDolar.equals("0")) {
            menu.txtPrecioDolar.setText("1");
        } else {
            precioDolar = menu.txtPrecioDolar.getText();
            this.factura.setPrecioDolar(Float.parseFloat(precioDolar));
            this.factura.obtenerPorCodBarra(codBarra);
            if (this.factura.getStock() > 0) {
                if (this.factura.getProducto()[0] != null) {
                    this.modelo = (DefaultTableModel) menu.tblFactura.getModel();
                    this.modelo.addRow(this.factura.getProducto());
                    filas = this.modelo.getRowCount();
                    this.factura.Vender(this.factura.getProducto()[0], this.factura.getProducto()[2]);
                    for (int i = 0; i < filas; i++) {
                        totalImports += Float.parseFloat(this.modelo.getValueAt(i, 5).toString());
                    }
                    sacarImpuesto = Float.parseFloat(1 + "." + menu.lblImpuestoISV.getText());//concatenacion para sacar el valor 1.xx para sacar el iva
                    //obtengo el IVA en entero "15" o cualquier que sea el impuesto
                    porcentajeImp = Float.parseFloat(menu.lblImpuestoISV.getText());// "descProduct = descuento de producto"
                    DecimalFormat formato = new DecimalFormat("#############.##");
                    this.total = totalImports;
                    this.isv = ((this.total / sacarImpuesto) * porcentajeImp) / 100;
                    this.subTotal = this.total - this.isv;
                    menu.txtSubTotal.setText("" + formato.format(this.subTotal));
                    menu.txtImpuesto.setText("" + formato.format(this.isv));
                    menu.txtTotal.setText("" + this.total);
                    menu.txtCodBarraFactura.setText("");
                    DeshabilitarBtnGuardarFactura();
                    productos.MostrarProductosVender("");
                } else {
                    JOptionPane.showMessageDialog(null, "El producto no esta ingresado...");
                    menu.txtCodBarraFactura.setText("");
                }
            } else {
                JOptionPane.showMessageDialog(null, "No hay suficiente producto en stock para realizar la venta");
            }
        }
    }

    public void Retornar() {
        this.modelo = (DefaultTableModel) menu.tblFactura.getModel();
        menu.pnlVentas.setVisible(false);
        menu.pnlReportes.setVisible(true);
        menu.btnActualizarFactura.setVisible(false);

        try {
            this.modelo = (DefaultTableModel) menu.tblFactura.getModel();
            int filas = this.modelo.getRowCount();//numero de filas de la tabla factura
            for (int i = 0; i < filas; i++) {
                this.modelo.removeRow(0);//remover filas de la tabla factura
            }
            productos.MostrarProductosVender("");//acturalizar tabla que muestra productos a vender
            //limpiar
            menu.btnGuardarFactura.setEnabled(true);
            menu.txtNClienteFactura.setText("");
            menu.txtAClienteFactura.setText("");
            menu.lblIdClienteFactura.setText("");
            menu.txtCreditoFactura.setText("");
            menu.txtCompradorFactura.setText("");
            this.total = 0;
            this.subTotal = 0;
            this.isv = 0;
            //inicializar a 0.0
            menu.txtSubTotal.setText("" + this.total);
            menu.txtImpuesto.setText("" + this.subTotal);
            menu.txtTotal.setText("" + this.isv);

        } catch (Exception err) {
            JOptionPane.showMessageDialog(null, err);
        }
        menu.txtNumeroFactura.setText(factura.ObtenerIdFactura());//actualizar numero de factura
    }

    public void Imprimir(String Nfactura, String comprador, String cliente, String tipoVenta, String formaPago, String[] Datos, String subtotal, String isv, String total, String fecha) {
        InfoFactura info = new InfoFactura();
        info.obtenerInfoFactura();
        //TODO agregar las variables isv y subtotal en la funcion 
        Ticket d = new Ticket(info.getNombre(), info.getDireccion(), info.getTelefono(), info.getRfc(), info.getRango(), "1", Nfactura, "Cajero", comprador, cliente, tipoVenta, formaPago, fecha, Datos, "", "", total, "", "");
        d.print();
    }

    //metodo para editar el impuesto de la factura
    public void editarISV(String isv) {
        if (isv.equals("")) {
            menu.lblImpuestoISV.setText("15");
        } else {
            menu.lblImpuestoISV.setText(isv);
        }
    }

    public void LimpiarTablaFactura() {
        try {
            this.modelo = (DefaultTableModel) menu.tblFactura.getModel();
            int filas = this.modelo.getRowCount();
            for (int i = 0; i < filas; i++) {
                this.modelo.removeRow(0);
            }
            productos.MostrarProductosVender("");
            menu.txtNClienteFactura.setText("");
            menu.txtAClienteFactura.setText("");
            menu.lblIdClienteFactura.setText("");
            menu.txtCreditoFactura.setText("");
            menu.txtCompradorFactura.setText("");
            menu.cmbFormaPago.setSelectedItem("Efectivo");
            menu.txtPagoCon.setText("");
            menu.txtCambio.setText("");
            this.total = 0;
            this.subTotal = 0;
            this.isv = 0;
            menu.txtSubTotal.setText("" + this.total);
            menu.txtImpuesto.setText("" + this.subTotal);
            menu.txtTotal.setText("" + this.isv);
        } catch (Exception err) {
            JOptionPane.showMessageDialog(null, err);
        }

    }

    public void EstiloTablaFacturacion() {
        menu.tblFactura.getTableHeader().setFont(new Font("Sugoe UI", Font.PLAIN, 14));
        menu.tblFactura.getTableHeader().setOpaque(false);
        menu.tblFactura.getTableHeader().setBackground(new Color(100, 100, 100));
        menu.tblFactura.getTableHeader().setForeground(new Color(255, 255, 255));
        menu.tblFactura.getTableHeader().setPreferredSize(new java.awt.Dimension(0, 35));
    }

    public void DeshabilitarBtnGuardarFactura() {
        if (menu.tblFactura.getRowCount() > 0) {
            menu.btnGuardarFactura.setEnabled(true);
        } else {
            menu.btnGuardarFactura.setEnabled(false);
        }
    }

    public void addProductoFactura() {
        int filaseleccionada = menu.tblAddProductoFactura.getSelectedRow();
        try {
            String id, codigo, nombre, precio, cantidad, total, importe, stockA, monedaVenta = "";
            float imp = 0, calcula, impuesto, descProduct = 0, stock, cantidadPVender,
                    precioDolar = Float.parseFloat(menu.txtPrecioDolar.getText()),
                    sacarImpuesto = Float.parseFloat(1 + "." + menu.lblImpuestoISV.getText()),//concatenacion para sacar el valor 1.xx para sacar el iva
                    porcentajeImp = Float.parseFloat(menu.lblImpuestoISV.getText());// "descProduct = descuento de producto"

            if (filaseleccionada == -1) {

            } else {
//                        if (this.descuento == 0) {   //no se aplica descuento
                //capturar los datos de la tabla producto para mandarlos a tabla factura
                this.modelo = (DefaultTableModel) menu.tblAddProductoFactura.getModel();
                id = modelo.getValueAt(filaseleccionada, 0).toString();
                codigo = modelo.getValueAt(filaseleccionada, 1).toString();
                nombre = modelo.getValueAt(filaseleccionada, 2).toString();
                precio = modelo.getValueAt(filaseleccionada, 3).toString();
                monedaVenta = (String) modelo.getValueAt(filaseleccionada, 4);
                stock = Float.parseFloat(modelo.getValueAt(filaseleccionada, 6).toString());
                cantidad = JOptionPane.showInputDialog(null, "Cantidad:");
                //si cantidad no recibe la cantidad se le va asignar 0
                if (cantidad.equals("")) {
                    cantidad = "0";
                }
                //convertir a flota la variable cantidad
                cantidadPVender = Float.parseFloat(cantidad);
                //validacion para la venta sugun lo que hay en stock osea no se pueda vender mas de lo que hay en stock
                if (cantidadPVender <= stock) {
                    //si cantidadPVender es igual a 0 no realizar ninguna accion
                    if (cantidadPVender == 0) {

                    } else {
                        if (monedaVenta.equals("Córdobas")) {
                            imp = (Float.parseFloat(precio) * cantidadPVender);//importe total de compra de producto
                        } else if (monedaVenta.equals("Dolar")) {
                            imp = (Float.parseFloat(precio) * cantidadPVender) * precioDolar;//importe total de compra de producto
                        }
                        //formato para los totales
                        DecimalFormat formato = new DecimalFormat("#############.##");
                        importe = String.valueOf(imp);
                        //realizando los calculos de importe
                        this.modelo = (DefaultTableModel) menu.tblFactura.getModel();
                        //pasar producto de tabla productos a tabla de factura
                        String[] FilaElementos = {id, codigo, cantidad, nombre, precio, importe};
                        this.modelo.addRow(FilaElementos);
//                                    calcula = (Float.parseFloat(importe));//convertir importe a float
                        int filas = this.modelo.getRowCount();
                        float totalImports = 0;
                        for (int i = 0; i < filas; i++) {
                            totalImports += Float.parseFloat(this.modelo.getValueAt(i, 5).toString());//calcular el total de factura
                        }
                        this.total = totalImports;
                        impuesto = ((this.total / sacarImpuesto) * porcentajeImp) / 100;//calcular el impuesto
                        this.isv = impuesto;//impuesto
                        this.subTotal = this.total - this.isv;//clacular subtotal de factura

                        menu.txtImpuesto.setText("" + formato.format(this.isv));//establecer el valor impuesto en el campo impuesto de factura
                        menu.txtSubTotal.setText("" + formato.format(this.subTotal));//establecer el valor impuesto en el campo sub total de factura
                        menu.txtTotal.setText("" + this.total);//establecer el valor impuesto en el campo Total de factura
                        this.factura.Vender(id, cantidad);//llamar procedimiento sql para vender
                        productos.MostrarProductosVender("");
                        menu.txtBuscarPorNombre.selectAll();
                        DeshabilitarBtnGuardarFactura();
                    }

                } else {
                    JOptionPane.showMessageDialog(null, "No hay suficiente producto en stock para realizar esta venta", "Advertencia", JOptionPane.WARNING_MESSAGE);
                }

//                        } else if (this.descuento > 0) {   //aplicar descuento
//                            //capturar los datos de la tabla producto para mandarlos a tabla factura
//                            this.modelo = (DefaultTableModel) menu.tblAddProductoFactura.getModel();
//                            id = modelo.getValueAt(filaseleccionada, 0).toString();
//                            codigo = modelo.getValueAt(filaseleccionada, 1).toString();
//                            nombre = modelo.getValueAt(filaseleccionada, 2).toString();
//                            precio = modelo.getValueAt(filaseleccionada, 3).toString();
//                            monedaVenta = (String) modelo.getValueAt(filaseleccionada, 4).toString();
//                            stock = Float.parseFloat(modelo.getValueAt(filaseleccionada, 6).toString());
//                            //obtengo el precio restandole el descuento 
//                            descProduct = Float.parseFloat(precio) - this.descuento;
//                            //la cantidad que se va a vender
//                            cantidad = JOptionPane.showInputDialog(null, "Cantidad:");
//                            if (cantidad.equals("")) {
//                                cantidad = "0";
//                            }
//                            //convertir a float la variable cantidad
//                            cantidadPVender = Float.parseFloat(cantidad);
//                            //condicion para validar el estock con lo que se va a vender 
//                            if (cantidadPVender < stock || cantidadPVender == stock) {
//                                if (cantidadPVender == 0) {
//
//                                } else {
//                                    if (monedaVenta.equals("Córdobas")) {
//                                        imp = (descProduct * cantidadPVender);//importe total de compra de producto con descuento
//                                    } else if (monedaVenta.equals("Dolar")) {
//                                        float precioConDesc = (descProduct * cantidadPVender);//importe total de compra de producto con descuento
//                                        imp = precioConDesc * precioDolar;
//                                    }
//                                    DecimalFormat formato = new DecimalFormat("#############.##");
//                                    importe = String.valueOf(imp);
//                                    //realizando los calculos de importe
//                                    this.modelo = (DefaultTableModel) menu.tblFactura.getModel();
//                                    //pasar producto de tabla productos a tabla de factura
//                                    String[] FilaElementos = {id, codigo, cantidad, nombre, String.valueOf(descProduct), importe};
//                                    this.modelo.addRow(FilaElementos);
////                                    calcula = (Float.parseFloat(importe));//convertir importe a float
//                                    int filas = this.modelo.getRowCount();
//                                    for (int i = 0; i < filas; i++) {
//                                        this.total += Float.parseFloat(this.modelo.getValueAt(i, 5).toString());//calcular el total de factura
//                                    }
//                                    impuesto = (float) ((this.total / sacarImpuesto) * porcentajeImp) / 100;//calcular el impuesto
//                                    this.isv = impuesto;//impuesto
//                                    this.subTotal = this.total - this.isv;//clacular subtotal de factura
//
//                                    menu.txtImpuesto.setText("" + formato.format(this.isv));//establecer el valor impuesto en el campo impuesto de factura
//                                    menu.txtSubTotal.setText("" + formato.format(this.subTotal));//establecer el valor impuesto en el campo sub total de factura
//                                    menu.txtTotal.setText("" + this.total);//establecer el valor impuesto en el campo Total de factura
//                                    this.descuento = 0;
//                                    this.factura.Vender(id, cantidad);
//                                    productos.MostrarProductosVender("");
//                                    menu.txtBuscarPorNombre.selectAll();
//                                    DeshabilitarBtnGuardarFactura();
//                                }
//
//                            } else {
//                                JOptionPane.showMessageDialog(null, "No hay suficiente producto en stock para realizar esta venta", "Advertencia", JOptionPane.WARNING_MESSAGE);
//                                this.descuento = 0;//si no hay suficiente producto en stock inicializamos la variable descuento a 0
//                            }
//
//                        }
            }
        } catch (Exception err) {
            //JOptionPane.showMessageDialog(null, e);
        }
    }

    public void addCreditoFactura() {
        int filaseleccionada = menu.tblAddCreditoFactura.getSelectedRow();
        String nombre, apellido, credito;
        try {
            if (filaseleccionada == -1) {

            } else {
                this.modelo = (DefaultTableModel) menu.tblAddCreditoFactura.getModel();
                credito = (String) this.modelo.getValueAt(filaseleccionada, 0);
                nombre = (String) this.modelo.getValueAt(filaseleccionada, 2);
                apellido = (String) this.modelo.getValueAt(filaseleccionada, 3);

                menu.txtCreditoFactura.setText(credito);
                menu.txtNClienteFactura.setText(nombre);
                menu.txtAClienteFactura.setText(apellido);
                menu.cmbFormaPago.setSelectedItem("Pendiente");
                menu.AddCreditoFactura.setVisible(false);
                menu.txtCodBarraFactura.requestFocus();
            }
        } catch (Exception err) {
            JOptionPane.showMessageDialog(null, err);
        }
    }

    public void mostrarVentanaCreditos() {
        menu.AddCreditoFactura.setSize(681, 363);
        menu.AddCreditoFactura.setVisible(true);
        menu.AddCreditoFactura.setLocationRelativeTo(null);
    }

    public void addMasProducto() {
        String nombre = "", id = "", codBarra = "";
        float cantidadIngresar = 0, cantidadActual = 0, precio = 0, cantidadUpdate = 0, importeUpdate = 0, totalImports = 0, precioDolar = 0, sacarImpuesto = 0, porcentajeImp = 0;
        int filas = 0, filaseleccionada = 0;
        try {
            this.modelo = (DefaultTableModel) menu.tblFactura.getModel();
            filaseleccionada = menu.tblFactura.getSelectedRow();
            filas = this.modelo.getRowCount();
            if (filaseleccionada == -1) {
                JOptionPane.showMessageDialog(null, "Seleccione un producto", "Advertencia", JOptionPane.WARNING_MESSAGE);
            } else {
                
                precioDolar = Float.parseFloat(menu.txtPrecioDolar.getText());
                id = (String) this.modelo.getValueAt(filaseleccionada, 0);
                nombre = (String) this.modelo.getValueAt(filaseleccionada, 3);
                codBarra = (String) this.modelo.getValueAt(filaseleccionada, 1);
                factura.obtenerPorCodBarra(codBarra);
                JOptionPane.showMessageDialog(null, spiner, "Cantidad de " + nombre + " a agregar:", JOptionPane.INFORMATION_MESSAGE);
                cantidadIngresar = Float.parseFloat(spiner.getValue().toString());
                if (cantidadIngresar <= factura.getStock()) {
                    cantidadActual = Float.parseFloat(this.modelo.getValueAt(filaseleccionada, 2).toString());
                    precio = Float.parseFloat(this.modelo.getValueAt(filaseleccionada, 4).toString());
                    this.factura.monedaVentaProducto(id);
                    cantidadUpdate = cantidadActual + cantidadIngresar;
                    if (this.factura.getMonedaVenta().equalsIgnoreCase("Dolar")) {
                        importeUpdate = (cantidadUpdate * precio) * precioDolar;
                    } else if (this.factura.getMonedaVenta().equalsIgnoreCase("Córdobas")) {
                        importeUpdate = (cantidadUpdate * precio);
                    }
                    this.modelo.setValueAt(String.valueOf(cantidadUpdate), filaseleccionada, 2);
                    this.modelo.setValueAt(String.valueOf(importeUpdate), filaseleccionada, 5);
                    this.factura.Vender(id, String.valueOf(cantidadIngresar));
                    for (int i = 0; i < filas; i++) {
                        totalImports += Float.parseFloat(this.modelo.getValueAt(i, 5).toString());
                    }
                    //fromato para los totales
                    DecimalFormat formato = new DecimalFormat("#############.##");
                    //formatreo de le inpuesto IVA
                    sacarImpuesto = Float.parseFloat(1 + "." + menu.lblImpuestoISV.getText());//concatenacion para sacar el valor 1.xx para sacar el iva
                    //TOTAL DE FACTURA
                    this.total = totalImports;
                    //obtengo el IVA en entero "15" o cualquier que sea el impuesto
                    porcentajeImp = Float.parseFloat(menu.lblImpuestoISV.getText());// "descProduct = descuento de producto"
                    this.isv = (float) ((this.total / sacarImpuesto) * porcentajeImp) / 100;
                    //calcular el subtotal de la factura
                    this.subTotal = this.total - this.isv;
                    //setear los campos total, subtotal, IVA
                    menu.txtSubTotal.setText("" + formato.format(this.subTotal));
                    menu.txtImpuesto.setText("" + formato.format(this.isv));
                    menu.txtTotal.setText("" + this.total);
                    menu.txtCodBarraFactura.requestFocus();
                    spiner.setValue(0.00);
                    productos.MostrarProductosVender("");
                }else{
                    JOptionPane.showMessageDialog(null, "No hay suficiente producto en stock para realizar la venta");
                }
            }
        } catch (Exception err) {
        }
    }

    public void addDescuento() {
        //variables para el nombre e id de producto
        String nombre = "", id = "";
        //variable para el numero de filas de la tabla factura
        int filaseleccionada = 0, filas = 0, confirmar = 0;
        //variables para las operaciones
        float cantidad = 0, precioUpdate = 0, precio = 0, importeUpdate = 0, totalImports = 0, sacarImpuesto = 0, porcentajeImp, precioDolar = 0, descuento = 0;
        //variable para obtener la filaseleccionada de la tabla factura
        filaseleccionada = menu.tblFactura.getSelectedRow();
        try {
            //modelo va a ser igual al modelo de la tabla factura
            this.modelo = (DefaultTableModel) menu.tblFactura.getModel();
            //obtener el nuemero de filas de la tabla factura
            filas = this.modelo.getRowCount();
            //validar si hay una fila seleccionada
            if (filaseleccionada == -1) {
                JOptionPane.showMessageDialog(null, "Seleccione un producto.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            } else {
                //obtengo los valores de la tabla factura para las operaciones
                precioDolar = Float.parseFloat(menu.txtPrecioDolar.getText());
                cantidad = Float.parseFloat(this.modelo.getValueAt(filaseleccionada, 2).toString());
                nombre = (String) this.modelo.getValueAt(filaseleccionada, 3);
                precio = Float.parseFloat(this.modelo.getValueAt(filaseleccionada, 4).toString());
                id = (String) this.modelo.getValueAt(filaseleccionada, 0);
                confirmar = JOptionPane.showConfirmDialog(null, spiner, "Agregar descuento a " + nombre, JOptionPane.OK_CANCEL_OPTION);
                if (confirmar == JOptionPane.YES_OPTION) {
                    descuento = Float.parseFloat(spiner.getValue().toString());
                    //realiza el escuento
                    precioUpdate = precio - descuento;
                    //obtengo desde el modelo facturta la moneda de venta de el producto na aplicarle el descuento
                    this.factura.monedaVentaProducto(id);
                    //validar que moneda de venta tiene el producto a aplicarse el descuento
                    if (this.factura.getMonedaVenta().equals("Dolar")) {
                        importeUpdate = (precioUpdate * cantidad) * precioDolar;
                    } else if (this.factura.getMonedaVenta().equals("Córdobas")) {
                        importeUpdate = precioUpdate * cantidad;
                    }
                    //actualizar el importe y el precio
                    this.modelo.setValueAt(String.valueOf(precioUpdate), filaseleccionada, 4);
                    this.modelo.setValueAt(String.valueOf(importeUpdate), filaseleccionada, 5);
                    //recorrer la colummna importe para sacar el total de factura
                    for (int i = 0; i < filas; i++) {
                        totalImports += Float.parseFloat(this.modelo.getValueAt(i, 5).toString());
                    }
                    //fromato para los totales
                    DecimalFormat formato = new DecimalFormat("#############.##");
                    //formatreo de le inpuesto IVA
                    sacarImpuesto = Float.parseFloat(1 + "." + menu.lblImpuestoISV.getText());//concatenacion para sacar el valor 1.xx para sacar el iva
                    //TOTAL DE FACTURA
                    this.total = totalImports;
                    //obtengo el IVA en entero "15" o cualquier que sea el impuesto
                    porcentajeImp = Float.parseFloat(menu.lblImpuestoISV.getText());// "descProduct = descuento de producto"
                    this.isv = (float) ((this.total / sacarImpuesto) * porcentajeImp) / 100;
                    //calcular el subtotal de la factura
                    this.subTotal = this.total - this.isv;
                    //setear los campos total, subtotal, IVA
                    menu.txtSubTotal.setText("" + formato.format(this.subTotal));
                    menu.txtImpuesto.setText("" + formato.format(this.isv));
                    menu.txtTotal.setText("" + this.total);
                    menu.txtCodBarraFactura.requestFocus();
                    spiner.setValue(0.00);
                }
            }
        } catch (Exception err) {
            JOptionPane.showMessageDialog(null, err + " en Agregar descuento al producto en factura");
        }
    }

    public void agregarProductoBoton() {
        String codBarra = menu.txtCodBarraFactura.getText();
        String precioDolar = menu.txtPrecioDolar.getText(), id = "";
        int filas = 0;
        float totalImports = 0, sacarImpuesto = 0, porcentajeImp = 0, cantidadUpdate = 0, importeUpdate = 0, cantidadActual = 0, precio = 0;
        if (!menu.isNumeric(precioDolar) || precioDolar.equals("0")) {
            menu.txtPrecioDolar.setText("1");
        } else {
            precioDolar = menu.txtPrecioDolar.getText();
            this.factura.setPrecioDolar(Float.parseFloat(precioDolar));
            this.factura.obtenerPorCodBarra(codBarra);
            if (this.factura.getProducto()[0] != null) {
                this.modelo = (DefaultTableModel) menu.tblFactura.getModel();
                this.modelo.addRow(this.factura.getProducto());
                filas = this.modelo.getRowCount();
                this.factura.Vender(this.factura.getProducto()[0], this.factura.getProducto()[2]);
                for (int i = 0; i < filas; i++) {
                    totalImports += Float.parseFloat(this.modelo.getValueAt(i, 5).toString());
                }
                sacarImpuesto = Float.parseFloat(1 + "." + menu.lblImpuestoISV.getText());//concatenacion para sacar el valor 1.xx para sacar el iva
                //obtengo el IVA en entero "15" o cualquier que sea el impuesto
                porcentajeImp = Float.parseFloat(menu.lblImpuestoISV.getText());// "descProduct = descuento de producto"
                DecimalFormat formato = new DecimalFormat("#############.##");
                this.total = totalImports;
                this.isv = ((this.total / sacarImpuesto) * porcentajeImp) / 100;
                this.subTotal = this.total - this.isv;
                menu.txtSubTotal.setText("" + formato.format(this.subTotal));
                menu.txtImpuesto.setText("" + formato.format(this.isv));
                menu.txtTotal.setText("" + this.total);
                menu.txtCodBarraFactura.setText("");
                DeshabilitarBtnGuardarFactura();
                menu.txtCodBarraFactura.requestFocus();
            } else {
                JOptionPane.showMessageDialog(null, "El producto no esta ingresado...");
                menu.txtCodBarraFactura.setText("");
            }
        }
    }

    public void actualizarFactura() {
        try {
            this.modelo = (DefaultTableModel) menu.tblFactura.getModel();//obtengo el modelo de tabla factura y sus datos
            int filas = this.modelo.getRowCount();//Cuento las filas de la tabla Factura
            if (filas == nD.length)//nD quiere decir numero de detalles condicion para guardar solo los cambios de las filas de la factura actual no se pueda agregar mas productos ni quitar solo cambiar ya que solo es edicio de la facura
            {
                Date fecha;
                String factura, id, cantidad, precio, totalDetalle, idCredito, iva, totalFactura, formaPago, idFormaPago, nombreComprador;//variables para capturar los datos a guardar
                fecha = menu.jcFechaFactura.getDate();//capturo la fecha del dateshooser
                long fechaF = fecha.getTime();//
                java.sql.Date fechaFactura = new java.sql.Date(fechaF);//convertir la fecha obtenida a formato sql
                nombreComprador = menu.txtCompradorFactura.getText();
                idCredito = menu.txtCreditoFactura.getText();//obtengo el credito
                iva = menu.txtImpuesto.getText();//obtengo el iva
                totalFactura = menu.txtTotal.getText();//obtengo total de factura
                formaPago = (String) menu.cmbFormaPago.getSelectedItem();//capturo el nombre de forma de pago 
                idFormaPago = this.factura.ObtenerFormaPago(formaPago);//capturo el id de la forma de pago que retorna la funcion obtenerformapago de la clase facturacion
                factura = menu.txtNumeroFactura.getText();//capturo id de factura ala que pertenece el detalle de factura
                this.factura.ActualizarFactura(1, factura, fechaFactura, nombreComprador, idCredito, idFormaPago, iva, totalFactura);//envio los datos a actualizar de la factura
                for (int cont = 0; cont < filas; cont++)//for para recorrer la tabla factura
                {
                    id = (String) this.modelo.getValueAt(cont, 0);//capturo el id de producto para guardar en detallefactura
                    cantidad = (String) this.modelo.getValueAt(cont, 2);//capturo la cantidad de producto de la columna dos y la paso a String para guardar en detallefactura
                    precio = (String) this.modelo.getValueAt(cont, 4);//capturo el precio de producto para guardar en detallefactura
                    totalDetalle = (String) this.modelo.getValueAt(cont, 5);//capturo el total de detalle compra de producto para guardar en detallefactura
                    this.factura.ActualizarDetalle(nD[cont], id, precio, cantidad, totalDetalle);//envio los datos para actualizar los detalles de la factura
                    //this.factura.Vender(id, cantidad);//funcion para diminuir el stock segun la cantidad que se venda
                }
                menu.txtNumeroFactura.setText(this.factura.ObtenerIdFactura());//Actualizo el campo numero de factura con la funcion obtenerIdFactura
                LimpiarTablaFactura();//limpio la factura
                menu.txtCreditoFactura.setText("");
                DeshabilitarBtnGuardarFactura();//deshabilito el boton guadar factura
                menu.pnlVentas.setVisible(false);
                menu.pnlReportes.setVisible(true);
                menu.btnActualizarFactura.setVisible(false);
                //
                menu.btnVentas.setVisible(true);
                menu.btnReportes.setVisible(true);
                menu.btnCerrarSesion.setVisible(true);
                menu.btnUsuarios.setVisible(true);
                menu.btnClientes.setVisible(true);
                menu.btnNotificaciones.setVisible(true);
                menu.btnTransacciones.setVisible(true);
                menu.btnInventario.setVisible(true);
                menu.btnInfoFactura.setVisible(true);
                menu.btnAgregar.setEnabled(true);
                menu.txtCodBarraFactura.setEnabled(true);
                //menu.btnGuardarFactura.setEnabled(true);//deshabilitar boton guardarFactura
                menu.btnAgregarProductoFactura.setEnabled(true);//deshabilitar boton AgregarProducto a factura
                menu.btnNuevaFactura.setEnabled(true);//deshabilitar boton Nueva Factura
                menu.btnEliminarFilaFactura.setEnabled(true);//deshabilitar boton EliminarFila Factura
                //
                productos.MostrarProductos("");//actualizar la tabla de productos inventario
                productos.MostrarProductosVender("");//actualizar la tabla de productos a vender
                creditos.ActualizarEstadoCreditoAabierto();//Actualizar Credito 
                creditos.ActualizarEstadoCreditoApendiente();//Actualizar Credito
                reportes.reportesDiarios(this.fecha);
                reportes.MostrarReportesDario(this.fecha);//actualizar reportes
                reportes.ReporteGlobal();
                reportes.SumaTotalFiltroReporte(this.fecha, this.fecha);//actualizar datos de reportes
                reportes.inversion();//actualizar Dato de Inversion
                creditos.MostrarCreditos("");//Actualizar creditos
                creditos.MostrarCreditosAddFactura("");//actualizar los creditos en factura
            } else {
                JOptionPane.showMessageDialog(null, "La factura depende de " + nD.length + " filas");
            }

        } catch (Exception err) {
            JOptionPane.showMessageDialog(null, err);
        }
    }

    public void editarFactura() {
        int filaF = menu.tblFactura.getRowCount();
        String cP, idProd;
        for (int i = 0; i < filaF; i++) {
            idProd = menu.tblFactura.getValueAt(i, 0).toString();
            cP = menu.tblFactura.getValueAt(i, 2).toString();
            modelProduct.AgregarProductoStock(idProd, cP);
        }
        LimpiarTablaFactura();
        String nombre = "", apellido = "";
        //obtengo la fila seleccionda de la tabla reporte diario    obtengo el numero las filas de la tabla detalleFactura
        int filaseleccionada = menu.tblReporte.getSelectedRow(), filas = menu.tblMostrarDetalleFactura.getRowCount();
        //la variable modelo va a tomar el modelo de la tabla factura
        this.modelo = (DefaultTableModel) menu.tblFactura.getModel();
        //variables para obtener los valores que se ocupan para la actualizacion
        String idFactura = "", idP = "", codBarra = "", nombreP = "", precioP = "", cantidadP = "", importe = "", pago = "", detalle = "", comprador = "", fecha = "", credito = "";
        //convertir el formato sql a Date con simpleDateFormat
        SimpleDateFormat spf = new SimpleDateFormat("yyyy-MM-dd");
        //nD quiere decir numero de detalles es la variable que guarda el numero de detalles que van en la factura a editar
        this.nD = new String[filas];
        //variables float para hacer la operaciones 
        float totalFactura, iva, subTotal;
        try {
            //idFactura obtiene el id de factura de la tabla reporte diario
            idFactura = menu.tblReporte.getValueAt(filaseleccionada, 0).toString();
            //obtengo la fecha de la factura
            fecha = menu.tblReporte.getValueAt(filaseleccionada, 1).toString();
            //obtengo el nombre comprador
            comprador = menu.tblReporte.getValueAt(filaseleccionada, 3).toString();//antes 4
            //obtengo el impuesto
            //TODO descomentar isv
            //iva = Float.parseFloat(menu.tblReporte.getValueAt(filaseleccionada, 2).toString());
            //obtengo el total de factura
            totalFactura = Float.parseFloat(menu.tblReporte.getValueAt(filaseleccionada, 2).toString());//antes 3
            //obtengo el credito
            credito = (String) menu.tblReporte.getValueAt(filaseleccionada, 5);//antes 6
            nombre = r.nombreCliente(credito);//obtengo el nombre de credito
            apellido = r.apellidoCliente(credito);//obtengo el apellido de credito
            //obtengo la forma de pago
            pago = menu.tblReporte.getValueAt(filaseleccionada, 4).toString();//antes 5
            //realiza el calculo para obtener el subtotal
           // subTotal = totalFactura - iva;
            //validacion de lo que estoy obteniendo en la variable credito
            if (credito == null) {
                credito = "";
            }
            //lleno los campos del formulario factura
            menu.txtCreditoFactura.setText(credito);
            menu.jcFechaFactura.setDate(spf.parse(fecha));
            //menu.txtSubTotal.setText("" + subTotal);
            menu.txtTotal.setText("" + totalFactura);
            menu.txtImpuesto.setText("0.0" /*+ iva*/);
            menu.txtNumeroFactura.setText(idFactura);
            menu.txtCompradorFactura.setText(comprador);
            menu.txtNClienteFactura.setText(nombre);
            menu.txtAClienteFactura.setText(apellido);
            menu.cmbFormaPago.setSelectedItem(pago);
            //for para recorrer la tabla detalleFactura
            for (int i = 0; i < filas; i++) {
                detalle = menu.tblMostrarDetalleFactura.getValueAt(i, 0).toString();//obtengo numero de detalle
                idP = menu.tblMostrarDetalleFactura.getValueAt(i, 1).toString();//obtengo id de producto
                codBarra = menu.tblMostrarDetalleFactura.getValueAt(i, 2).toString();//obtengo cod barra del producto
                nombreP = menu.tblMostrarDetalleFactura.getValueAt(i, 3).toString();//obtengo nombre del producto
                cantidadP = menu.tblMostrarDetalleFactura.getValueAt(i, 4).toString();//obtengo cantidad de producto vendido en la factura
                precioP = menu.tblMostrarDetalleFactura.getValueAt(i, 5).toString();//obtengo precio del producto
                importe = menu.tblMostrarDetalleFactura.getValueAt(i, 6).toString();//obtengo total de venta del producto

                nD[i] = detalle;//lleno el array los id correspondiente a cada detalles
                String[] addFila = {idP, codBarra, cantidadP, nombreP, precioP, importe};//creo el arreglo con los datos obtenidos de la tabla detalle
                this.modelo.addRow(addFila);//agrego la fila de array creado anteriormente a la tabla factura para la edicion
            }
            //System.out.println(nD[0]+" "+nD[1]);  
            menu.pnlVentas.setVisible(true);//mostrar panel de ventas 
            menu.btnActualizarFactura.setVisible(true);//mostrar boton actualizar
            menu.btnGuardarFactura.setEnabled(false);//deshabilitar boton guardarFactura
            menu.btnAgregarProductoFactura.setEnabled(false);//deshabilitar boton AgregarProducto a factura
            menu.btnNuevaFactura.setEnabled(false);//deshabilitar boton Nueva Factura
            menu.btnEliminarFilaFactura.setEnabled(false);//deshabilitar boton EliminarFila Factura
            menu.pnlReportes.setVisible(false);//ocultar panel Reportes
            menu.btnInfoFactura.setVisible(false);
            menu.vistaDetalleFacturas.setVisible(false);//ocultar la ventana de detalle de factura de reportes
            menu.btnVentas.setVisible(false);
            menu.btnReportes.setVisible(false);
            menu.btnCerrarSesion.setVisible(false);
            menu.btnUsuarios.setVisible(false);
            menu.btnClientes.setVisible(false);
            menu.btnAgregar.setEnabled(false);
            menu.txtCodBarraFactura.setEnabled(false);
            menu.btnNotificaciones.setVisible(false);
            menu.btnTransacciones.setVisible(false);
            menu.btnInventario.setVisible(false);
        } catch (Exception err) {
            JOptionPane.showMessageDialog(null, err + "guardar facturas");
        }
    }

    public void eliminarFilaFactura() {
        int filaseleccionada = menu.tblFactura.getSelectedRow();
        float importe, totalActual, sacarImpuesto, porcentajeImp;
        String cantidad, id;
        try {
            if (filaseleccionada == -1) {

            } else {
                this.modelo = (DefaultTableModel) menu.tblFactura.getModel();
                id = (String) modelo.getValueAt(filaseleccionada, 0);
                cantidad = (String) modelo.getValueAt(filaseleccionada, 2);
                importe = Float.parseFloat(modelo.getValueAt(filaseleccionada, 5).toString());
                totalActual = Float.parseFloat(menu.txtTotal.getText());
                //fromato para los totales
                DecimalFormat formato = new DecimalFormat("#############.##");
                //formatreo de le inpuesto IVA
                sacarImpuesto = Float.parseFloat(1 + "." + menu.lblImpuestoISV.getText());//concatenacion para sacar el valor 1.xx para sacar el iva
                //obtengo el IVA en entero "15" o cualquier que sea el impuesto
                porcentajeImp = Float.parseFloat(menu.lblImpuestoISV.getText());// "descProduct = descuento de producto"
                this.total = totalActual - importe;
                this.isv = ((this.total / sacarImpuesto) * porcentajeImp) / 100;
                this.subTotal = this.total - isv;
                menu.txtTotal.setText("" + this.total);
                menu.txtSubTotal.setText("" + formato.format(this.subTotal));
                menu.txtImpuesto.setText("" + formato.format(this.isv));
                modelProduct.AgregarProductoStock(id, cantidad);
                productos.MostrarProductosVender("");
                this.modelo.removeRow(filaseleccionada);
                DeshabilitarBtnGuardarFactura();
                menu.txtCodBarraFactura.requestFocus();
            }
        } catch (Exception err) {
            JOptionPane.showMessageDialog(null, err);
        }
    }

    public void nuevaFactura() {
        DeshabilitarBtnGuardarFactura();
        try {
            String id, cantidad;
            this.modelo = (DefaultTableModel) menu.tblFactura.getModel();
            int filas = this.modelo.getRowCount();
            for (int i = 0; i < filas; i++) {
                id = (String) this.modelo.getValueAt(i, 0);
                cantidad = (String) this.modelo.getValueAt(i, 2);
                modelProduct.AgregarProductoStock(id, cantidad);
            }
            LimpiarTablaFactura();
            DeshabilitarBtnGuardarFactura();
            menu.txtCodBarraFactura.requestFocus();
        } catch (Exception err) {
            JOptionPane.showMessageDialog(null, err);
        }
    }

    public void actualizarIVA() {
        int confirmar = JOptionPane.showConfirmDialog(null, spiner, "Valor de Impuesto IVA:", JOptionPane.OK_CANCEL_OPTION);
        if (confirmar == JOptionPane.YES_OPTION) {
            menu.lblImpuestoISV.setText(spiner.getValue().toString());
            spiner.setValue(0.00);
        }
    }

    public void limpiarFormularioClienteFactura() {
        menu.txtNClienteFactura.setText("");
        menu.txtAClienteFactura.setText("");
        menu.lblIdClienteFactura.setText("");
        menu.txtCreditoFactura.setText("");
        menu.cmbFormaPago.setSelectedItem("Efectivo");
    }
}
