package controlador;

import modelo.Productos;
import modelo.Facturacion;
import vista.IMenu;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.table.DefaultTableModel;
import modelo.*;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;

/**
 *
 * @author CESAR DIAZ MARADIAGA
 */
public class CtrlProducto implements ActionListener, CaretListener, MouseListener {

    Productos productos;
    IMenu menu;
    Facturacion factura;
    Categorias c;
    Marca L;
    Date fecha;
    String id;
    DefaultTableModel modelo;

    public CtrlProducto(Productos p, IMenu menu) {
        this.productos = p;
        this.menu = menu;
        this.factura = new Facturacion();
        this.c = new Categorias();
        this.L = new Marca();
        this.fecha = new Date();
        this.menu.btnGuardarProducto.addActionListener(this);
        this.menu.btnActualizarProducto.addActionListener(this);
        this.menu.btnNuevoProducto.addActionListener(this);
        this.menu.btnAgregarCategorias.addActionListener(this);
        this.menu.btnAgregarLaboratorio.addActionListener(this);
        this.menu.btnMostrarStockMinimo.addActionListener(this);
        this.menu.txtBuscarProducto.addCaretListener(this);
        this.menu.txtBuscarPorNombre.addCaretListener(this);
        this.menu.txtBuscarPorCategoria.addCaretListener(this);
        this.menu.txtBuscarPorLaboratorio.addCaretListener(this);
        this.menu.txtBuscarCategoriaAdd.addCaretListener(this);
        this.menu.txtLaboratorioAdd.addCaretListener(this);
        this.menu.EditarProducto.addActionListener(this);
        this.menu.BorrarProducto.addActionListener(this);
        this.menu.AddProductoStock.addActionListener(this);
        this.menu.btnCalcularGanancia.addActionListener(this);
        this.menu.btnAgregarProducto.addActionListener(this);
        this.menu.btnBuscarMinStock.addActionListener(this);
        this.menu.rbBuscarCategoria.addActionListener(this);
        this.menu.rbBuscarLaboratorio.addActionListener(this);
        this.menu.rbBuscarNombreCodBarra.addActionListener(this);
        this.menu.txtCategoriaProducto.addMouseListener(this);
        this.menu.txtLaboratorioProducto.addMouseListener(this);
        this.menu.tblAddCategoria.addMouseListener(this);
        this.menu.tblAddLaboratorio.addMouseListener(this);
        this.menu.AgregarProductoStock.addActionListener(this);
        this.menu.btnGenerarReporteStock.addMouseListener(this);
        this.id = null;
        this.modelo = new DefaultTableModel();
        iniciar();
    }

    public void iniciar() {

        MostrarProductos("");
        StockMinimoP("", 15);
        MostrarProductosVender("");
        llenarAddCategoria("");
        llenarAddLaboratorio("");
        DeshabilitarProductos();
        menu.rbBuscarNombreCodBarra.setSelected(true);
        this.menu.jcFechaVProducto.setDate(fecha);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == menu.btnGuardarProducto) {
            String codigoBarra = menu.txtCodBarraProducto.getText(),
                    nombre = menu.txtNombreProducto.getText(),
                    precioCProducto = menu.txtCompraProducto.getText(),
                    precioVProducto = menu.txtVentaProducto.getText(),
                    ganancia = menu.txtGananciaProducto.getText(),
                    cantidad = menu.txtCantidadProducto.getText(),
                    categoria = menu.txtCategoriaProducto.getText(),
                    laboratorio = menu.txtLaboratorioProducto.getText(),
                    ubicacion = menu.txtUbicacionProducto.getText(),
                    descripcion = menu.txtDescripcionProducto.getText(),
                    monedaCompra = menu.cmbMonedaCompraProducto.getSelectedItem().toString(),
                    monedaVenta = menu.cmbMonedaVentaProducto.getSelectedItem().toString();
            Date fechaVencimiento = menu.jcFechaVProducto.getDate();
            long fechaV = fechaVencimiento.getTime();
            java.sql.Date fecha = new java.sql.Date(fechaV);
            //Validacion de que sean ingresados los datos correctos y que no esten vacios
            if (nombre.equals("")) {
                //JOptionPane.showMessageDialog(null, "Llene el campo Nombre", "Advertencia", JOptionPane.WARNING_MESSAGE);
            } else if (precioCProducto.equals("")) {
                JOptionPane.showMessageDialog(null, "Llene el campo Precio Compra", "Advertencia", JOptionPane.WARNING_MESSAGE);
            } else if (precioVProducto.equals("")) {
                JOptionPane.showMessageDialog(null, "Llene el campo Precio Venta", "Advertencia", JOptionPane.WARNING_MESSAGE);
            } else if (fechaVencimiento.equals(null)) {
                JOptionPane.showMessageDialog(null, "Llene el campo Fecha Vencimiento", "Advertencia", JOptionPane.WARNING_MESSAGE);
            } else if (cantidad.equals("")) {
                JOptionPane.showMessageDialog(null, "Llene el campo Cantidad", "Advertencia", JOptionPane.WARNING_MESSAGE);
            } else if (categoria.equals("")) {
                JOptionPane.showMessageDialog(null, "Llene el campo Categoria", "Advertencia", JOptionPane.WARNING_MESSAGE);
            } else if (laboratorio.equals("")) {
                JOptionPane.showMessageDialog(null, "Llene el campo Laboratorio", "Advertencia", JOptionPane.WARNING_MESSAGE);
            } else {//validacion para ingreso de nuemeros 
                if (!isNumeric(precioCProducto)) {
                    //txtCompraProducto.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(255, 0, 0)));
                    JOptionPane.showMessageDialog(null, "Solo numeros campo Precio Compra", "Advertencia", JOptionPane.WARNING_MESSAGE);
                } else if (!isNumeric(precioVProducto)) {
                    JOptionPane.showMessageDialog(null, "Solo numeros campo Precio Venta", "Advertencia", JOptionPane.WARNING_MESSAGE);
                } else if (!isNumeric(cantidad)) {
                    JOptionPane.showMessageDialog(null, "Solo numeros campo Cantidad", "Advertencia", JOptionPane.WARNING_MESSAGE);
                } else if (!isNumeric(laboratorio)) {
                    JOptionPane.showMessageDialog(null, "Solo Numeros Campo Laboratorio", "Advertencia", JOptionPane.WARNING_MESSAGE);
                } else if (!isNumeric(categoria)) {
                    JOptionPane.showMessageDialog(null, "Solo Numeros Campo Categoria", "Advertencia", JOptionPane.WARNING_MESSAGE);
                } else {   //funcion Guardar de la clase Productos para guardar productos
                    productos.Guardar(codigoBarra, nombre, precioCProducto, monedaCompra, precioVProducto, monedaVenta, fecha, cantidad, categoria, laboratorio, ubicacion, descripcion);
                    MostrarProductos("");
                    LimpiarProducto();
                    menu.btnGuardarProducto.setEnabled(true);
                    menu.btnActualizarProducto.setEnabled(false);
                    MostrarProductosVender("");
                    inversion();
                }

            }
        }
        if (e.getSource() == menu.btnCalcularGanancia) {
            float compra, porcentaje, resultado;
            if (isNumeric(menu.txtCompraProducto.getText()) && isNumeric(menu.txtGananciaProducto.getText())) {
                compra = Float.parseFloat(menu.txtCompraProducto.getText());
                porcentaje = compra * Float.parseFloat(menu.txtGananciaProducto.getText()) / 100;
                resultado = porcentaje + compra;
                String precioV = String.valueOf(resultado);
                menu.txtMargenGanancia.setText(String.valueOf(porcentaje));
                menu.txtVentaProducto.setText(precioV);
            } else {
                JOptionPane.showMessageDialog(null, "Solo Numero en Campo Precio Compra y % Ganacia");
            }
        }
        if (e.getSource() == menu.btnActualizarProducto) {
            String codigoBarra = menu.txtCodBarraProducto.getText(),
                    nombre = menu.txtNombreProducto.getText(),
                    precioCProducto = menu.txtCompraProducto.getText(),
                    precioVProducto = menu.txtVentaProducto.getText(),
                    ganancia = menu.txtGananciaProducto.getText(),
                    cantidad = menu.txtCantidadProducto.getText(),
                    categoria = menu.txtCategoriaProducto.getText(),
                    laboratorio = menu.txtLaboratorioProducto.getText(),
                    ubicacion = menu.txtUbicacionProducto.getText(),
                    descripcion = menu.txtDescripcionProducto.getText(),
                    monedaCompra = menu.cmbMonedaCompraProducto.getSelectedItem().toString(),
                    monedaVenta = menu.cmbMonedaVentaProducto.getSelectedItem().toString();
            Date fechaVencimiento = menu.jcFechaVProducto.getDate();
            long fechaV = fechaVencimiento.getTime();
            java.sql.Date fecha = new java.sql.Date(fechaV);
            if (nombre.equals("")) {
                //JOptionPane.showMessageDialog(null, "Llene el campo Nombre", "Advertencia", JOptionPane.WARNING_MESSAGE);
            } else if (precioCProducto.equals("")) {
                JOptionPane.showMessageDialog(null, "Llene el campo Precio Compra", "Advertencia", JOptionPane.WARNING_MESSAGE);
            } else if (precioVProducto.equals("")) {
                JOptionPane.showMessageDialog(null, "Llene el campo Precio Venta", "Advertencia", JOptionPane.WARNING_MESSAGE);
            } else if (fechaVencimiento.equals("")) {
                JOptionPane.showMessageDialog(null, "Llene el campo Fecha Vencimiento", "Advertencia", JOptionPane.WARNING_MESSAGE);
            } else if (cantidad.equals("")) {
                JOptionPane.showMessageDialog(null, "Llene el campo Cantidad", "Advertencia", JOptionPane.WARNING_MESSAGE);
            } else if (categoria.equals("")) {
                JOptionPane.showMessageDialog(null, "Llene el campo Categoria", "Advertencia", JOptionPane.WARNING_MESSAGE);
            } else if (laboratorio.equals("")) {
                JOptionPane.showMessageDialog(null, "Llene el campo Laboratorio", "Advertencia", JOptionPane.WARNING_MESSAGE);
            } else {//validacion para ingreso de nuemeros 
                if (!isNumeric(precioCProducto)) {
                    JOptionPane.showMessageDialog(null, "Solo numeros campo Precio Compra", "Advertencia", JOptionPane.WARNING_MESSAGE);
                } else if (!isNumeric(precioVProducto)) {
                    JOptionPane.showMessageDialog(null, "Solo numeros campo Precio Venta", "Advertencia", JOptionPane.WARNING_MESSAGE);
                } else if (!isNumeric(ganancia)) {
                    JOptionPane.showMessageDialog(null, "Solo numeros campo Ganancia", "Advertencia", JOptionPane.WARNING_MESSAGE);
                } else if (!isNumeric(cantidad)) {
                    JOptionPane.showMessageDialog(null, "Solo numeros campo Cantidad", "Advertencia", JOptionPane.WARNING_MESSAGE);
                } else if (!isNumeric(laboratorio)) {
                    JOptionPane.showMessageDialog(null, "Solo Numeros Campo Laboratorio", "Advertencia", JOptionPane.WARNING_MESSAGE);
                } else if (!isNumeric(categoria)) {
                    JOptionPane.showMessageDialog(null, "Solo Numeros Campo Categoria", "Advertencia", JOptionPane.WARNING_MESSAGE);
                } else {   //funcion Guardar de la clase Productos para guardar productos
                    productos.Actualizar(this.id, codigoBarra, nombre, precioCProducto, monedaCompra, precioVProducto, monedaVenta, fecha, cantidad, categoria, laboratorio, ubicacion, descripcion);
                    MostrarProductos("");
                    MostrarProductosVender("");
                    LimpiarProducto();
                    inversion();
                    menu.btnGuardarProducto.setEnabled(true);
                    menu.btnActualizarProducto.setEnabled(false);
                }

            }
        }
        if (e.getSource() == menu.btnNuevoProducto) {
            HabilitarProductos();
            LimpiarProducto();
            menu.txtCodBarraProducto.requestFocus();
        }
        if (e.getSource() == menu.EditarProducto) {
            int filaseleccionada;
            String id, codBarra, nombre, precioC, precioV, cantidad, categoria, laboratorio, ubicacion, descripcion,monedaCompra, monedaVenta;
            Date fechaVencimiento;
            SimpleDateFormat formatoFecha = new SimpleDateFormat("yyyy-MM-dd");
            try {
                filaseleccionada = menu.tblProductos.getSelectedRow();
                if (filaseleccionada == -1) {
                    JOptionPane.showConfirmDialog(null, "Seleccione una fila", "Advertencia", JOptionPane.WARNING_MESSAGE);
                } else {
                    modelo = (DefaultTableModel) menu.tblProductos.getModel();
                    id = (String) modelo.getValueAt(filaseleccionada, 0);
                    codBarra = (String) modelo.getValueAt(filaseleccionada, 1);
                    nombre = (String) modelo.getValueAt(filaseleccionada, 2);
                    precioC = (String) modelo.getValueAt(filaseleccionada, 3);
                    monedaCompra = (String) modelo.getValueAt(filaseleccionada,4);
                    precioV = (String) modelo.getValueAt(filaseleccionada, 5);
                    monedaVenta = (String) modelo.getValueAt(filaseleccionada, 6);
                    fechaVencimiento = formatoFecha.parse(modelo.getValueAt(filaseleccionada, 7).toString());
                    cantidad = (String) modelo.getValueAt(filaseleccionada, 8);
                    categoria = (String) modelo.getValueAt(filaseleccionada, 9);
                    laboratorio = (String) modelo.getValueAt(filaseleccionada, 10);
                    ubicacion = (String) modelo.getValueAt(filaseleccionada, 11);
                    descripcion = (String) modelo.getValueAt(filaseleccionada, 12);
                    HabilitarProductos();
                    LimpiarProducto();
                    menu.txtCodBarraProducto.setText(codBarra);
                    menu.txtNombreProducto.setText(nombre);
                    menu.txtCompraProducto.setText(precioC);
                    menu.cmbMonedaCompraProducto.setSelectedItem(monedaCompra);
                    menu.txtVentaProducto.setText(precioV);
                    menu.cmbMonedaVentaProducto.setSelectedItem(monedaVenta);
                    menu.jcFechaVProducto.setDate(fechaVencimiento);
                    menu.txtCantidadProducto.setText(cantidad);
                    menu.txtCategoriaProducto.setText(productos.ObtenerIdCategoria(categoria));
                    menu.txtLaboratorioProducto.setText(productos.ObtenerIdMarca(laboratorio));
                    menu.txtUbicacionProducto.setText(ubicacion);
                    menu.txtDescripcionProducto.setText(descripcion);
                    menu.txtGananciaProducto.setText("0");
                    menu.txtMargenGanancia.setText("");
                    this.id = id;
                    menu.btnGuardarProducto.setEnabled(false);
                    menu.btnActualizarProducto.setEnabled(true);
                }
            } catch (Exception err) {
                JOptionPane.showConfirmDialog(null, err + "en la funcion editar producto");
            }
        }
        if (e.getSource() == menu.BorrarProducto) {
            int filaseleccionada = menu.tblProductos.getSelectedRow();
            String id;
            try {
                filaseleccionada = menu.tblProductos.getSelectedRow();
                if (filaseleccionada == -1) {
                    JOptionPane.showMessageDialog(null, "Seleccione una Fila", "Advertencia", JOptionPane.WARNING_MESSAGE);
                } else {
                    int confirmar = JOptionPane.showConfirmDialog(null, "Seguro Que Quieres Borrar Este Producto", "Advertencia", JOptionPane.OK_CANCEL_OPTION);
                    if (confirmar == JOptionPane.YES_OPTION) {
                        modelo = (DefaultTableModel) menu.tblProductos.getModel();
                        id = (String) modelo.getValueAt(filaseleccionada, 0);
                        productos.Eliminar(id);
                        MostrarProductos("");
                        inversion();
                    }

                }
            } catch (Exception err) {
                JOptionPane.showMessageDialog(null, err + "borrar producto");
            }
        }
        if (e.getSource() == menu.AddProductoStock) {
            int filaseleccionada = menu.tblProductos.getSelectedRow();
            String id = "", nombre = "", Cantidad = "";
            try {
                if (filaseleccionada == -1) {
                    //JOptionPane.showMessageDialog(null, "Seleccione una Fila", "Advertencia", JOptionPane.WARNING_MESSAGE);
                } else {
                    menu.VentanaAddProductoStock.setSize(400, 130);
                    menu.VentanaAddProductoStock.setVisible(true);
                    menu.VentanaAddProductoStock.setLocationRelativeTo(null);
                    this.modelo = (DefaultTableModel) menu.tblProductos.getModel();
                    //id = (String) this.modelo.getValueAt(filaseleccionada, 0);
                    nombre = (String) this.modelo.getValueAt(filaseleccionada, 2);
                    menu.lblNombreProductStock.setText("Cantidad de " + nombre + " a Agregar");
                    /*Cantidad = menu.txtCantidadAgregar.getText();//JOptionPane.showInputDialog("Ingrese la Cantidad de " + nombre, "");
                    if(Cantidad!=null)
                    {
                        productos.AgregarProductoStock(id, Cantidad);
                        MostrarProductos("");
                        MostrarProductosVender("");
                    }*/
                }
            } catch (Exception err) {
                JOptionPane.showMessageDialog(null, err + "en la funcion add productoStock");
            }
        }
        if (e.getSource() == menu.btnAgregarProducto) {
            try {
                String Cantidad = "";
                int filaseleccionada = menu.tblProductos.getSelectedRow();
                this.modelo = (DefaultTableModel) menu.tblProductos.getModel();
                String id = (String) this.modelo.getValueAt(filaseleccionada, 0);
                Cantidad = menu.txtCantidadAgregar.getText();//JOptionPane.showInputDialog("Ingrese la Cantidad de " + nombre, "");
                if (Cantidad != null) {
                    productos.AgregarProductoStock(id, Cantidad);
                    MostrarProductos("");
                    MostrarProductosVender("");
                    StockMinimoP("", 0);
                    inversion();
                }
            } catch (Exception err) {
                //JOptionPane.showMessageDialog(null, err+"en la funcion AgregarProducto");
            }
            menu.VentanaAddProductoStock.setVisible(false);
            menu.txtCantidadAgregar.setText("");
        }
        if (e.getSource() == menu.btnAgregarCategorias) {
            menu.ventanaCategoria.setSize(680, 330);
            menu.ventanaCategoria.setLocationRelativeTo(null);
            menu.ventanaCategoria.setVisible(true);
            CtrlCategoria cat = new CtrlCategoria(menu, c);
            cat.MostrarCategorias("");
        }
        if (e.getSource() == menu.btnAgregarLaboratorio) {
            menu.ventanaMarca.setSize(672, 330);
            menu.ventanaMarca.setLocationRelativeTo(null);
            menu.ventanaMarca.setVisible(true);
            CtrlMarca lab = new CtrlMarca(menu, L);
            lab.MostrarMarca("");
        }
        if (e.getSource() == menu.btnMostrarStockMinimo) {
            menu.StockMinimo.setSize(1118, 495);
            menu.StockMinimo.setLocationRelativeTo(null);
            //StockMinimo.setModal(true);
            menu.StockMinimo.setVisible(true);
            StockMinimoP("", 15);
        }
        if (e.getSource() == menu.btnBuscarMinStock) {
            float cant = 0;
            if (menu.txtCantidadStockM.getText().equals("")) {

            } else {
                cant = Float.parseFloat(menu.txtCantidadStockM.getText());
                StockMinimoP(menu.txtCategoriaStockM.getText(), cant);
            }
        }
        if (e.getSource() == menu.rbBuscarCategoria) {
            if (menu.rbBuscarCategoria.isSelected() == true) {
                menu.txtBuscarPorNombre.setEnabled(false);
                menu.txtBuscarPorCategoria.setEnabled(true);
                menu.txtBuscarPorLaboratorio.setEnabled(false);
                menu.txtBuscarPorLaboratorio.setText("");
                menu.txtBuscarPorNombre.setText("");
                menu.txtBuscarPorCategoria.requestFocus();
            }
        }
        if (e.getSource() == menu.rbBuscarLaboratorio) {
            if (menu.rbBuscarLaboratorio.isSelected() == true) {
                menu.txtBuscarPorNombre.setEnabled(false);
                menu.txtBuscarPorCategoria.setEnabled(false);
                menu.txtBuscarPorLaboratorio.setEnabled(true);
                menu.txtBuscarPorNombre.setText("");
                menu.txtBuscarPorCategoria.setText("");
                menu.txtBuscarPorLaboratorio.requestFocus();
            }
        }
        if (e.getSource() == menu.rbBuscarNombreCodBarra) {
            if (menu.rbBuscarNombreCodBarra.isSelected() == true) {
                menu.txtBuscarPorNombre.setEnabled(true);
                menu.txtBuscarPorCategoria.setEnabled(false);
                menu.txtBuscarPorLaboratorio.setEnabled(false);
                menu.txtBuscarPorCategoria.setText("");
                menu.txtBuscarPorLaboratorio.setText("");
                menu.txtBuscarPorNombre.requestFocus();
            }
        }
        
    }

    @Override
    public void caretUpdate(CaretEvent e) {
        if (e.getSource() == menu.txtBuscarProducto) {
            String Filtrar = menu.txtBuscarProducto.getText();
            MostrarProductos(Filtrar);
        }
        if (e.getSource() == menu.txtBuscarPorNombre) {
            String Buscar = menu.txtBuscarPorNombre.getText();
            MostrarProductosVender(Buscar);
        }
        if (e.getSource() == menu.txtBuscarPorLaboratorio) {
            String laboratorio = menu.txtBuscarPorLaboratorio.getText();
            MostrarPorMarca(laboratorio);
        }
        if (e.getSource() == menu.txtBuscarPorCategoria) {
            String categoria = menu.txtBuscarPorCategoria.getText();
            MostrarPorCategoria(categoria);
        }
        if (e.getSource() == menu.txtBuscarCategoriaAdd) {
            String valor = menu.txtBuscarCategoriaAdd.getText();
            llenarAddCategoria(valor);
        }
        if (e.getSource() == menu.txtLaboratorioAdd) {
            String Nombre = menu.txtLaboratorioAdd.getText();
            llenarAddLaboratorio(Nombre);
        }
    }

    public boolean isNumeric(String cadena) {//metodo para la validacion de campos numericos
        try {
            Float.parseFloat(cadena);
            return true;
        } catch (NumberFormatException nfe) {
            return false;
        }
    }

    public void MostrarProductos(String buscar) {
        menu.tblProductos.getTableHeader().setFont(new Font("Sugoe UI", Font.PLAIN, 14));
        menu.tblProductos.getTableHeader().setOpaque(false);
        menu.tblProductos.getTableHeader().setBackground(new Color(100, 100, 100));
        menu.tblProductos.getTableHeader().setForeground(new Color(255, 255, 255));
        menu.tblProductos.setModel(this.productos.Consulta(buscar));
    }

    public void MostrarProductosVender(String Buscar) {
        menu.tblAddProductoFactura.getTableHeader().setFont(new Font("Sugoe UI", Font.PLAIN, 14));
        menu.tblAddProductoFactura.getTableHeader().setOpaque(false);
        menu.tblAddProductoFactura.getTableHeader().setBackground(new Color(100, 100, 100));
        menu.tblAddProductoFactura.getTableHeader().setForeground(new Color(255, 255, 255));
        menu.tblAddProductoFactura.setModel(factura.BusquedaGeneralProductoVender(Buscar));
    }

    public void LimpiarProducto()//metodo para limpiar los campos de formulario Productos
    {
        menu.txtCodBarraProducto.setText("");
        menu.txtNombreProducto.setText("");
        menu.txtCompraProducto.setText("");
        menu.txtVentaProducto.setText("");
        menu.txtCantidadProducto.setText("");
        menu.txtCategoriaProducto.setText("");
        menu.txtLaboratorioProducto.setText("");
        menu.txtUbicacionProducto.setText("");
        menu.txtDescripcionProducto.setText("");
        menu.txtGananciaProducto.setText("");
        menu.txtMargenGanancia.setText("");
        menu.cmbMonedaCompraProducto.setSelectedItem("Córdobas");
        menu.cmbMonedaVentaProducto.setSelectedItem("Córdobas");
    }

    public void HabilitarProductos() {
        menu.txtCodBarraProducto.setEnabled(true);
        menu.txtNombreProducto.setEnabled(true);
        menu.txtCompraProducto.setEnabled(true);
        menu.txtVentaProducto.setEnabled(true);
        menu.jcFechaVProducto.setEnabled(true);
        menu.txtCantidadProducto.setEnabled(true);
        menu.txtCategoriaProducto.setEnabled(true);
        menu.txtLaboratorioProducto.setEnabled(true);
        menu.txtUbicacionProducto.setEnabled(true);
        menu.txtDescripcionProducto.setEnabled(true);
        menu.txtGananciaProducto.setEnabled(true);
        menu.txtMargenGanancia.setEnabled(true);
        menu.btnCalcularGanancia.setEnabled(true);
        menu.btnGuardarProducto.setEnabled(true);
        menu.btnActualizarProducto.setEnabled(false);
        menu.cmbMonedaCompraProducto.setEnabled(true);
        menu.cmbMonedaVentaProducto.setEnabled(true);
    }
//este metodo iversion es el mismo metodo que esta en reportes lo repite para no crear una nueva instancia de la clase reportes
    public void inversion()
    {
        float cordobas = this.productos.inversionCordobas(),
                dolar = this.productos.inversionDolar(),
                precioDolar = Float.parseFloat(menu.txtPrecioDolar.getText()),
                total = cordobas + (dolar*precioDolar);
        menu.lblInversion.setText(""+total);
    }
    
    public void StockMinimoP(String categoria, float cantidad)//llenar tabla de productos bajos de estock
    {
        //los parametros que recibe son para los filtros de bussqueda den la base de datos
        menu.tblStockMin.getTableHeader().setFont(new Font("Sugoe UI", Font.PLAIN, 14));
        menu.tblStockMin.getTableHeader().setOpaque(false);
        menu.tblStockMin.getTableHeader().setBackground(new Color(100, 100, 100));
        menu.tblStockMin.getTableHeader().setForeground(new Color(255, 255, 255));
        menu.tblStockMin.setModel(productos.MinimoStock(categoria, cantidad));
    }

    //metodo para mostrar los productos a vender por filtro de Marca
    public void MostrarPorMarca(String laboratorio) {
        menu.tblAddProductoFactura.setModel(factura.BuscarPorMarca(laboratorio));
    }

    //metodo para mostrar los productos a vender por filtro de Categoria
    public void MostrarPorCategoria(String categoria) {
        menu.tblAddProductoFactura.setModel(factura.BuscarPorCategoria(categoria));
    }

    public void llenarAddCategoria(String nombre)//metodo para llenar la tabla de add categoria a productos
    {
        menu.tblAddCategoria.getTableHeader().setFont(new Font("Sugoe UI", Font.PLAIN, 14));
        menu.tblAddCategoria.getTableHeader().setOpaque(false);
        menu.tblAddCategoria.getTableHeader().setBackground(new Color(100, 100, 100));
        menu.tblAddCategoria.getTableHeader().setForeground(new Color(255, 255, 255));
        menu.tblAddCategoria.setModel(productos.MostrarCategorias(nombre));//
    }

    public void llenarAddLaboratorio(String nombre)//metodo para llenar la tabla de add Laboratorio a productos
    {
        menu.tblAddLaboratorio.getTableHeader().setFont(new Font("Sugoe UI", Font.PLAIN, 14));
        menu.tblAddLaboratorio.getTableHeader().setOpaque(false);
        menu.tblAddLaboratorio.getTableHeader().setBackground(new Color(100, 100, 100));
        menu.tblAddLaboratorio.getTableHeader().setForeground(new Color(255, 255, 255));
        menu.tblAddLaboratorio.setModel(productos.MostrarMarca(nombre));
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        if (e.getSource() == menu.txtCategoriaProducto) {
            menu.AddCategoria.setSize(600, 200);
            menu.AddCategoria.setVisible(true);
            menu.AddCategoria.setLocationRelativeTo(null);
            llenarAddCategoria("");
        }
        if (e.getSource() == menu.txtLaboratorioProducto) {
            menu.AddMarca.setSize(562, 200);
            menu.AddMarca.setVisible(true);
            menu.AddMarca.setLocationRelativeTo(null);
            llenarAddLaboratorio("");
        }
        if (e.getSource() == menu.tblAddCategoria) {
            String id;
            int filaseleccionada = menu.tblAddCategoria.getSelectedRow();
            try {
                if (filaseleccionada == -1) {
                    JOptionPane.showMessageDialog(null, "Seleccione una Fila", "Advertencia", JOptionPane.WARNING_MESSAGE);
                } else {
                    modelo = (DefaultTableModel) menu.tblAddCategoria.getModel();
                    id = (String) modelo.getValueAt(filaseleccionada, 0);
                    menu.txtCategoriaProducto.setText(id);
                    menu.AddCategoria.setVisible(false);
                }
            } catch (Exception err) {

            }
        }
        if (e.getSource() == menu.tblAddLaboratorio) {
            String id;
            int filaseleccionada = menu.tblAddLaboratorio.getSelectedRow();
            try {
                if (filaseleccionada == -1) {
                    JOptionPane.showMessageDialog(null, "Seleccione una Fila", "Advertencia", JOptionPane.WARNING_MESSAGE);
                } else {
                    modelo = (DefaultTableModel) menu.tblAddLaboratorio.getModel();
                    id = (String) modelo.getValueAt(filaseleccionada, 0);
                    menu.txtLaboratorioProducto.setText(id);
                    menu.AddMarca.setVisible(false);
                }
            } catch (Exception err) {

            }
        }
        if(e.getSource() == menu.btnGenerarReporteStock){
            float cant = 0;
            if(menu.txtCantidadStockM.getText().equals(""))
            {
                
            }else{
                cant = Float.parseFloat(menu.txtCantidadStockM.getText());
                try {
                    productos.GenerarReporteStockMin(menu.txtCategoriaStockM.getText(), cant);
                } catch (SQLException ex) {
                    Logger.getLogger(CtrlProducto.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void mouseExited(MouseEvent e) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void DeshabilitarProductos() {
        menu.txtCodBarraProducto.setEnabled(false);
        menu.txtNombreProducto.setEnabled(false);
        menu.txtCompraProducto.setEnabled(false);
        menu.txtVentaProducto.setEnabled(false);
        menu.jcFechaVProducto.setEnabled(false);
        menu.txtCantidadProducto.setEnabled(false);
        menu.txtCategoriaProducto.setEnabled(false);
        menu.txtLaboratorioProducto.setEnabled(false);
        menu.txtUbicacionProducto.setEnabled(false);
        menu.txtDescripcionProducto.setEnabled(false);
        menu.txtGananciaProducto.setEnabled(false);
        menu.txtMargenGanancia.setEnabled(false);
        menu.btnCalcularGanancia.setEnabled(false);
        menu.btnGuardarProducto.setEnabled(false);
        menu.btnActualizarProducto.setEnabled(false);
        menu.cmbMonedaCompraProducto.setEnabled(false);
        menu.cmbMonedaVentaProducto.setEnabled(false);
    }
}
