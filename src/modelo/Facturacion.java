package modelo;

import java.sql.*;
import java.text.DecimalFormat;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.DefaultComboBoxModel;

/**
 *
 * @author CESAR DIAZ MARADIAGA
 */
public class Facturacion extends Conexiondb {

    String consulta;
    DefaultTableModel modelo;
    Connection cn;
    PreparedStatement pst;
    DefaultComboBoxModel combo;
    int banderin;
    private String[] producto;
    private float stock, precioDolar;
    private String monedaVenta;

    public Facturacion() {
        this.cn = null;
        this.pst = null;
    }

    public String[] getProducto() {
        return producto;
    }

    public float getStock() {
        return stock;
    }

    public String getMonedaVenta() {
        return monedaVenta;
    }

    public void setPrecioDolar(float precio) {
        this.precioDolar = precio;
    }
//Guardar Factura

    public void GuardarFactura(int caja, Date fecha, String nombreComprador, String credito, String pago, String iva, String total) {
        cn = Conexion();
        this.consulta = "INSERT INTO facturas(caja ,fecha, nombre_comprador, credito, tipoVenta, impuestoISV, totalFactura) VALUES(?,?,?,?,?,?,?)";
        int idCredito = 0, formaPago = Integer.parseInt(pago);
        //TODO cambiar el valor iva por el que recibe la funcion
        float impuestoIVA = Float.parseFloat("0"), totalFactura = Float.parseFloat(total);
        if (!credito.equals("")) {
            idCredito = Integer.parseInt(credito);
            try {
                pst = this.cn.prepareStatement(this.consulta);
                pst.setInt(1, caja);
                pst.setDate(2, fecha);
                pst.setString(3, nombreComprador);
                pst.setInt(4, idCredito);
                pst.setInt(5, formaPago);
                pst.setFloat(6, impuestoIVA);
                pst.setFloat(7, totalFactura);
                this.banderin = pst.executeUpdate();
                if (banderin > 0) {
                    //JOptionPane.showMessageDialog(null, "Factura Guardada Exitosamente", "Informacion", JOptionPane.WARNING_MESSAGE);
                }
                cn.close();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e);
            }
        } else {
            try {
                pst = this.cn.prepareStatement(this.consulta);
                pst.setInt(1, caja);
                pst.setDate(2, fecha);
                pst.setString(3, nombreComprador);
                pst.setNull(4, java.sql.Types.INTEGER);
                pst.setInt(5, formaPago);
                pst.setFloat(6, impuestoIVA);
                pst.setFloat(7, totalFactura);
                this.banderin = pst.executeUpdate();
                if (banderin > 0) {
                    //JOptionPane.showMessageDialog(null, "Factura Guardada Exitosamente", "Informacion", JOptionPane.WARNING_MESSAGE);
                }
                cn.close();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, e);
            }
        }

    }
//Guardar detalleFactura

    public void DetalleFactura(String factura, String producto, String precio, String cantidad, String total) {
        cn = Conexion();
        int idFactura = Integer.parseInt(factura), idProducto = Integer.parseInt(producto);
        float precioP = Float.parseFloat(precio), cantidadP = Float.parseFloat(cantidad), totalD = Float.parseFloat(total);
        this.consulta = "INSERT INTO detalleFactura(factura, producto, precioProducto, cantidadProducto, totalVenta) VALUES(?,?,?,?,?)";
        try {
            pst = this.cn.prepareStatement(this.consulta);
            pst.setInt(1, idFactura);
            pst.setInt(2, idProducto);
            pst.setFloat(3, precioP);
            pst.setFloat(4, cantidadP);
            pst.setFloat(5, totalD);
            this.banderin = pst.executeUpdate();
            if (banderin > 0) {
                //JOptionPane.showMessageDialog(null, "detalle guardado");
            }
            cn.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }
//metodo para busqueda general y por nombre y cod. barra de producto

    public DefaultTableModel BusquedaGeneralProductoVender(String buscar) {
        cn = Conexion();
        this.consulta = "SELECT productos.id, productos.codigoBarra, productos.nombre AS nombreProducto, precioVenta, monedaVenta, fechaVencimiento, stock, ubicacion, productos.descripcion, categorias.nombre AS nombreCategoria, marca.nombre as nombreMarca FROM productos LEFT JOIN categorias ON(productos.categoria=categorias.id) LEFT JOIN marca ON(productos.marca=marca.id) WHERE CONCAT(productos.codigoBarra, productos.nombre) LIKE '%" + buscar + "%'";
        String[] registros = new String[11];
        String[] titulos = {"Id", "Codigo Barra", "Nombre", "precioVenta", "Moneda", "Fecha Vencimiento", "Stock", "Categoria", "marca", "Ubicacion", "Descripcion"};
        modelo = new DefaultTableModel(null, titulos) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };
        try {
            Statement st = this.cn.createStatement();
            ResultSet rs = st.executeQuery(consulta);
            while (rs.next()) {
                registros[0] = rs.getString("id");
                registros[1] = rs.getString("codigoBarra");
                registros[2] = rs.getString("nombreProducto");
                registros[3] = rs.getString("precioVenta");
                registros[4] = rs.getString("monedaVenta");
                registros[5] = rs.getString("fechaVencimiento");
                registros[6] = rs.getString("stock");
                registros[7] = rs.getString("nombreCategoria");
                registros[8] = rs.getString("nombreMarca");
                registros[9] = rs.getString("ubicacion");
                registros[10] = rs.getString("descripcion");
                this.modelo.addRow(registros);
            }
            cn.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        }

        return modelo;
    }
//metodo para filtro de busqueda del producto por categoria de producto

    public DefaultTableModel BuscarPorCategoria(String categoria) {
        cn = Conexion();
        this.consulta = "SELECT productos.id, productos.codigoBarra, productos.nombre AS nombreProducto, precioVenta, monedaVenta, fechaVencimiento, stock, ubicacion, productos.descripcion, categorias.nombre AS nombreCategoria, marca.nombre as nombreMarca FROM productos INNER JOIN categorias ON(productos.categoria=categorias.id) INNER JOIN marca ON(productos.marca=marca.id) WHERE CONCAT(categorias.nombre) LIKE '%" + categoria + "%'";
        String[] registros = new String[11];
        String[] titulos = {"Id", "Codigo Barra", "Nombre", "precioVenta", "Moneda", "Fecha Vencimiento", "Stock", "Categoria", "marca", "Descuento", "Ubicacion", "Descripcion"};
        modelo = new DefaultTableModel(null, titulos) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };
        try {
            Statement st = this.cn.createStatement();
            ResultSet rs = st.executeQuery(consulta);
            while (rs.next()) {
                registros[0] = rs.getString("id");
                registros[1] = rs.getString("codigoBarra");
                registros[2] = rs.getString("nombreProducto");
                registros[3] = rs.getString("precioVenta");
                registros[4] = rs.getString("monedaVenta");
                registros[5] = rs.getString("fechaVencimiento");
                registros[6] = rs.getString("stock");
                registros[7] = rs.getString("nombreCategoria");
                registros[8] = rs.getString("nombreMarca");
                registros[9] = rs.getString("ubicacion");
                registros[10] = rs.getString("descripcion");
                this.modelo.addRow(registros);
            }
            cn.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        }

        return modelo;
    }
//metodo para filtro de busqueda del producto por laboratorio

    public DefaultTableModel BuscarPorMarca(String marca) {
        cn = Conexion();
        this.consulta = "SELECT productos.id, productos.codigoBarra, productos.nombre AS nombreProducto, precioVenta, monedaVenta, fechaVencimiento, stock, ubicacion, productos.descripcion, categorias.nombre AS nombreCategoria, marca.nombre as nombreMarca FROM productos INNER JOIN categorias ON(productos.categoria=categorias.id) INNER JOIN marca ON(productos.marca=marca.id) WHERE CONCAT(marca.nombre) LIKE '%" + marca + "%'";
        String[] registros = new String[11];
        String[] titulos = {"Id", "Codigo Barra", "Nombre", "precioVenta", "Moneda", "Fecha Vencimiento", "Stock", "Categoria", "marca", "Descuento", "Ubicacion", "Descripcion"};
        modelo = new DefaultTableModel(null, titulos) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };
        try {
            Statement st = this.cn.createStatement();
            ResultSet rs = st.executeQuery(consulta);
            while (rs.next()) {
                registros[0] = rs.getString("id");
                registros[1] = rs.getString("codigoBarra");
                registros[2] = rs.getString("nombreProducto");
                registros[3] = rs.getString("precioVenta");
                registros[4] = rs.getString("monedaVenta");
                registros[5] = rs.getString("fechaVencimiento");
                registros[6] = rs.getString("stock");
                registros[7] = rs.getString("nombreCategoria");
                registros[8] = rs.getString("nombreMarca");
                registros[9] = rs.getString("ubicacion");
                registros[10] = rs.getString("descripcion");
                this.modelo.addRow(registros);
            }
            cn.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        }

        return modelo;
    }
//metodo para obtener los tipos de pago

    public DefaultComboBoxModel FormasPago() {
        cn = Conexion();
        this.consulta = "SELECT * FROM formapago";
        combo = new DefaultComboBoxModel();
        try {
            Statement st = this.cn.createStatement();
            ResultSet rs = st.executeQuery(this.consulta);
            while (rs.next()) {
                combo.addElement(rs.getString("tipoVenta"));
            }
            cn.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        }
        return combo;
    }
//metodo que me retorna el id de la factura y sumo 1 para mostrar la factura siguiente 

    public String ObtenerIdFactura() {
        cn = Conexion();
        int sumaId = 0, s;
        String id = "", obtenerId = "";
        this.consulta = "SELECT MAX(id) AS id FROM facturas";
        try {
            Statement st = this.cn.createStatement();
            ResultSet rs = st.executeQuery(this.consulta);
            while (rs.next()) {
                obtenerId = rs.getString("id");
            }
            if (obtenerId != null) {
                sumaId = Integer.parseInt(obtenerId) + 1;
                id = String.valueOf(sumaId);
            } else {
                obtenerId = "0";
                sumaId = Integer.parseInt(obtenerId) + 1;
                id = String.valueOf(sumaId);
            }
            cn.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        }
        return id;
    }

    //metodo para obtener el id de la forma de pago segun el metodo de pago que recibe
    public String ObtenerFormaPago(String pago) {
        cn = Conexion();
        this.consulta = "SELECT id FROM formapago WHERE tipoVenta = '" + pago + "'";
        String id = "";
        try {
            Statement st = this.cn.createStatement();
            ResultSet rs = st.executeQuery(this.consulta);
            while (rs.next()) {
                id = rs.getString("id");
            }
            cn.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        }
        return id;
    }

    public void Vender(String id, String cantidad) {
        cn = Conexion();
        Float cantidadP = Float.parseFloat(cantidad);
        int idP = Integer.parseInt(id);
        this.consulta = "{CALL venderProductoStock(?,?)}";
        try {
            CallableStatement cst = this.cn.prepareCall(this.consulta);
            cst.setInt(1, idP);
            cst.setFloat(2, cantidadP);
            cst.execute();
            cn.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    public void ActualizarFactura(int caja, String id, Date fecha, String nombreComprador, String credito, String pago, String iva, String total) {
        cn = Conexion();
        if (credito.equals("")) {
            credito = null;
        }
        this.consulta = "UPDATE facturas SET caja = ?, credito = ?, nombre_comprador = ?,fecha = ? , tipoVenta = ?, impuestoISV = ?, totalFactura = ? WHERE id=?";
        try {
            pst = this.cn.prepareStatement(this.consulta);
            pst.setInt(1, caja);
            pst.setString(2, credito);
            pst.setString(3, nombreComprador);
            pst.setDate(4, fecha);
            pst.setString(5, pago);
            //TODO cambiar el iva
            pst.setString(6, "0"/*iva*/);
            pst.setString(7, total);
            pst.setString(8, id);
            pst.execute();
            this.banderin = pst.executeUpdate();
            if (this.banderin > 0) {
                JOptionPane.showMessageDialog(null, "factura actualizada correctamente");
            }
            cn.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    public void ActualizarDetalle(String id, String producto, String precio, String cant, String total) {
        cn = Conexion();
        this.consulta = "UPDATE detalleFactura SET producto = ?, precioProducto = ?, cantidadProducto = ?, totalVenta = ? WHERE id=?";
        try {
            pst = this.cn.prepareStatement(this.consulta);
            pst.setString(1, producto);
            pst.setString(2, precio);
            pst.setString(3, cant);
            pst.setString(4, total);
            pst.setString(5, id);
            pst.execute();
            this.banderin = pst.executeUpdate();
            if (this.banderin > 0) {

            }
            cn.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        }

    }

    public void obtenerPorCodBarra(String codBarra) {
        DecimalFormat formato = new DecimalFormat("#############.00");
        this.producto = new String[6];
        float importe;
        this.cn = Conexion();
        this.consulta = "SELECT id,codigoBarra, nombre, precioVenta, monedaVenta, stock FROM productos WHERE codigoBarra = ?";
        try {
            this.pst = this.cn.prepareStatement(this.consulta);
            this.pst.setString(1, codBarra);
            ResultSet rs = this.pst.executeQuery();
            while (rs.next()) {
                producto[0] = rs.getString("id");
                producto[1] = rs.getString("codigoBarra");
                producto[2] = "1.0";
                producto[3] = rs.getString("nombre");
                producto[4] = rs.getString("precioVenta");
                this.stock = rs.getFloat("stock");
                this.monedaVenta = rs.getString("monedaVenta");
            }
            //producto[2] = Cantidad;
            if (producto[2] == null || producto[4] == null) {
                
            }else{
                importe = Float.parseFloat(producto[2]) * Float.parseFloat(producto[4]);
                if (this.monedaVenta.equals("Dolar")) {
                    importe = importe * precioDolar;
                }
                producto[5] = formato.format(importe);
            }
            this.cn.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e + " en lafuncion obtenerPorCodBarra");
        }
    }

    public void monedaVentaProducto(String id) {
        this.cn = Conexion();
        this.consulta = "SELECT monedaVenta FROM productos WHERE id = ?";

        try {
            this.pst = this.cn.prepareStatement(this.consulta);
            this.pst.setString(1, id);
            ResultSet rs = this.pst.executeQuery();
            while (rs.next()) {
                this.monedaVenta = rs.getString("monedaVenta");
            }
            this.cn.close();
        } catch (Exception e) {
        }
    }
    
    public void ActualizarDevolucion(int id, float iva, float total){
        this.cn = Conexion();
        this.consulta = "UPDATE facturas SET impuestoISV = ?, totalFactura = ? WHERE id = ?";
        try {
            this.pst = this.cn.prepareStatement(this.consulta);
            this.pst.setFloat(1, iva);
            this.pst.setFloat(2, total);
            this.pst.setInt(3, id);
            this.pst.executeUpdate();
            this.cn.close();
        }
        catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e+" en la funcion ActualizarDevolucion en modelo Facturacion");
        }
    }
    //esta funcio no se esta utilizando
    public void eliminarDetalle(int id){
        this.cn = Conexion();
        this.consulta = "DELETE FROM detalleFactura WHERE id = ?";
        try {
            this.pst = this.cn.prepareStatement(this.consulta);
            this.pst.setInt(1, id);
            this.pst.executeUpdate();
            this.cn.close();
        }
        catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e+" en la funcion eliminarDetalle en modelo Facturacion");
        }
    }
}
