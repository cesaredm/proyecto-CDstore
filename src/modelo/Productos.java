package modelo;

import controlador.CtrlProducto;
import java.awt.List;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
import static javax.swing.WindowConstants.DISPOSE_ON_CLOSE;
import javax.swing.table.DefaultTableModel;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JasperViewer;

/**
 *
 * @author CESAR DIAZ MARADIAGA
 */
public class Productos extends Conexiondb {

    DefaultTableModel modelo;
    DefaultComboBoxModel combo;
    Connection cn;
    PreparedStatement pst;
    String consulta;
    int banderin;
    private boolean existe = true;
    
    public Productos() {
        this.cn = null;
        this.combo = new DefaultComboBoxModel();
        this.pst = null;
    }

    public boolean isExiste() {
        return existe;
    }

    public void setExiste(boolean existe) {
        this.existe = existe;
    }
    
    public void Guardar(String codigoBarra, String nombre, String precioCompra, String monedaCompra, String precioVenta, String monedaVenta, Date fechaVencimiento, String stock, String categoria, String laboratorio, String ubicacion, String descripcion) {
        cn = Conexion();
        this.consulta = "INSERT INTO productos(codigoBarra, nombre, precioCompra, monedaCompra, precioVenta, monedaVenta, fechaVencimiento, stock, categoria, marca, ubicacion, descripcion) VALUES(?,?,?,?,?,?,?,?,?,?,?,?)";
        //TODO sustituir el 0 por precioCompra 
        float compra = Float.parseFloat("0"), venta = Float.parseFloat(precioVenta), cantidad = Float.parseFloat(stock);
        int Idcategoria = Integer.parseInt(categoria), Idlaboratorio = Integer.parseInt(laboratorio);
        try {
            pst = this.cn.prepareStatement(this.consulta);
            pst.setString(1, codigoBarra);
            pst.setString(2, nombre);
            pst.setFloat(3, compra);
            pst.setString(4, monedaCompra);
            pst.setFloat(5, venta);
            pst.setString(6, monedaVenta);
            pst.setDate(7, fechaVencimiento);
            pst.setFloat(8, cantidad);
            pst.setInt(9, Idcategoria);
            pst.setInt(10, Idlaboratorio);
            pst.setString(11, ubicacion);
            pst.setString(12, descripcion);
            this.banderin = pst.executeUpdate();
            if (this.banderin > 0) {
                JOptionPane.showMessageDialog(null, "Producto guardado exitosamente", "Informacion", JOptionPane.INFORMATION_MESSAGE);
            }
            cn.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    public void Actualizar(String id, String codigoBarra, String nombre, String precioCompra, String monedaCompra, String precioVenta, String monedaVenta, Date fechaVencimiento, String stock, String categoria, String laboratorio, String ubicacion, String descripcion) {
        cn = Conexion();
        this.consulta = "UPDATE productos SET codigoBarra=?, nombre=?, precioCompra=?, monedaCompra=?, precioVenta=?, monedaVenta=?, fechaVencimiento=?, stock=?, categoria=?, marca=?, ubicacion=?, descripcion=? WHERE id = ?";
        float compra = Float.parseFloat(precioCompra), venta = Float.parseFloat(precioVenta), cantidad = Float.parseFloat(stock);
        int Idcategoria = Integer.parseInt(categoria), Idlaboratorio = Integer.parseInt(laboratorio);
        try {
            pst = this.cn.prepareStatement(this.consulta);
            pst.setString(1, codigoBarra);
            pst.setString(2, nombre);
            pst.setFloat(3, compra);
            pst.setString(4, monedaCompra);
            pst.setFloat(5, venta);
            pst.setString(6, monedaVenta);
            pst.setDate(7, fechaVencimiento);
            pst.setFloat(8, cantidad);
            pst.setInt(9, Idcategoria);
            pst.setInt(10, Idlaboratorio);
            pst.setString(11, ubicacion);
            pst.setString(12, descripcion);
            pst.setString(13, id);
            this.banderin = pst.executeUpdate();
            if (this.banderin > 0) {
                JOptionPane.showMessageDialog(null, "Producto actualizado exitosamente", "Informacion", JOptionPane.INFORMATION_MESSAGE);
            }
            cn.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    public void Eliminar(String id) {
        cn = Conexion();
        this.consulta = "DELETE FROM productos WHERE id=" + id;
        try {
            pst = this.cn.prepareStatement(consulta);
            this.banderin = pst.executeUpdate();
            if (banderin > 0) {
                JOptionPane.showMessageDialog(null, "Dato borrado exitosamente", "Informacion", JOptionPane.INFORMATION_MESSAGE);
            }
            cn.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    public DefaultTableModel Consulta(String buscar) {
        cn = Conexion();
        //TODO agregar los campos precioCompra y moneda compra en la consulta
        this.consulta = "SELECT productos.id, productos.codigoBarra, productos.nombre AS nombreProducto, precioVenta, monedaVenta, fechaVencimiento, stock, ubicacion, productos.descripcion, categorias.nombre AS nombreCategoria, marca.nombre as nombreMarca FROM productos LEFT JOIN categorias ON(productos.categoria=categorias.id) LEFT JOIN marca ON(productos.marca=marca.id) WHERE CONCAT(productos.codigoBarra, productos.nombre) LIKE '%" + buscar + "%' ORDER BY productos.id DESC";
        String[] registros = new String[14];
        String[] titulos = {"Id", "Codigo Barra", "Nombre","precioVenta", "Moneda", "Fecha Vencimiento", "Stock", "Categoria", "marca", "Ubicación", "Descripción"};
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
//                registros[3] = rs.getString("precioCompra");
//                registros[4] = rs.getString("monedaCompra");
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
    //Mostrar todas la categorias para agregra al producto
    public DefaultTableModel MostrarCategorias(String nombre) {
        cn = Conexion();
        this.consulta = "SELECT * FROM categorias WHERE nombre LIKE '%" + nombre + "%'";
        String[] resultados = new String[3];
        String[] titulos = {"Id", "Nombre", "Descripcion"};
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

                resultados[0] = rs.getString("id");
                resultados[1] = rs.getString("nombre");
                resultados[2] = rs.getString("descripcion");
                this.modelo.addRow(resultados);
            }
            cn.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        }
        return modelo;
    }
    //Mostrar todas la Laboratorio para agregra al producto
    public DefaultTableModel MostrarMarca(String nombre) {
        cn = Conexion();
        this.consulta = "SELECT * FROM marca WHERE nombre LIKE '%" + nombre + "%'";
        String[] resultados = new String[3];
        String[] titulos = {"Id", "Nombre", "Descripcion"};
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

                resultados[0] = rs.getString("id");
                resultados[1] = rs.getString("nombre");
                resultados[2] = rs.getString("descripcion");
                modelo.addRow(resultados);
            }
            cn.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        }
        return modelo;
    }
    
    public String ObtenerIdMarca(String nombre)//metodo para obtener Id de laboratorio para modificar producto
    {
        String id = "";
        cn = Conexion();
        this.consulta = "SELECT id FROM marca WHERE nombre='" + nombre + "'";
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

    public String ObtenerIdCategoria(String nombre)//metodo para obtener Id de categoria para modificar producto
    {
        String id = "";
        cn = Conexion();
        this.consulta = "SELECT id FROM categorias WHERE nombre='" + nombre + "'";
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

    public void AgregarProductoStock(String id, String cantidad)//metodo para agregar producto al stock
    {
        cn = Conexion();
        float c = Float.parseFloat(cantidad);
        int idP = Integer.parseInt(id);
        this.consulta = "{CALL agregarProductoStock(?,?)}";
        try {
            CallableStatement cst = this.cn.prepareCall(this.consulta);
            cst.setInt(1, idP);
            cst.setFloat(2, c);
            this.banderin = cst.executeUpdate();
            if (banderin > 0) {
                //JOptionPane.showMessageDialog(null, "Se Agrego Exitosamente", "Informacion", JOptionPane.INFORMATION_MESSAGE);
            }
            cn.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    public DefaultTableModel MinimoStock(String categoria, float cantidad) {
        cn = Conexion();
        this.consulta = "SELECT productos.id, productos.codigoBarra, productos.nombre AS nombreProducto, precioCompra, precioVenta, fechaVencimiento,stock, ubicacion, productos.descripcion, categorias.nombre AS nombreCategoria, marca.nombre as nombreMarca FROM productos INNER JOIN categorias ON(productos.categoria=categorias.id) INNER JOIN marca ON(productos.marca=marca.id) WHERE productos.stock < " + cantidad + " AND categorias.nombre LIKE '%" + categoria + "%'";
        String[] titulos = {"Id", "Codigo Barra", "Nombre", "precioCompra", "precioVenta", "Fecha Vencimiento", "Stock", "Categoria", "Laboratorio", "Ubicacion", "Descripcion"};
        String[] registros = new String[12];
        modelo = new DefaultTableModel(null, titulos) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };
        try {
            Statement pst = this.cn.createStatement();
            //pst.setInt(1, cantidad);
            //pst.setString(2, categoria);
            ResultSet rs = pst.executeQuery(consulta);
            while (rs.next()) {
                registros[0] = rs.getString("id");
                registros[1] = rs.getString("codigoBarra");
                registros[2] = rs.getString("nombreProducto");
                registros[3] = rs.getString("precioCompra");
                registros[4] = rs.getString("precioVenta");
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
        return this.modelo;
    }
    
    public void GenerarReporteStockMin(String categ, float cantidad) throws SQLException
    {
        try {
            this.cn = Conexion();
                JasperReport Reporte = null;
                String path = "/Reportes/minStock.jasper";
                Map parametros = new HashMap();
                parametros.put("cantidad", cantidad);
                parametros.put("categoria", categ);
                //Reporte = (JasperReport) JRLoader.loadObject(path);
                Reporte = (JasperReport) JRLoader.loadObject(getClass().getResource("/Reportes/minStock.jasper"));
                JasperPrint jprint = JasperFillManager.fillReport(Reporte, parametros, cn);
                JasperViewer vista = new JasperViewer(jprint,false);
                vista.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
                vista.setVisible(true);
                cn.close();
            } catch (JRException ex) {
                Logger.getLogger(CtrlProducto.class.getName()).log(Level.SEVERE, null, ex);
            }
    }
        //metodo para obtener el total de inversion en el negocio precio de compra
    public float inversion(){
        cn = Conexion();
        float inversion = 0;
        this.consulta = "SELECT SUM(precioCompra*stock) AS inversion FROM productos";
        try {
            PreparedStatement pst = this.cn.prepareStatement(this.consulta);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {                
                inversion = rs.getFloat("inversion");
            }
            this.cn.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e+"funcion inversion en modelo");
        }
        return inversion;
    }
    public float inversionCordobas()
    {
        cn = Conexion();
        float inversion = 0;
        this.consulta = "SELECT SUM(precioCompra*stock) AS inversion FROM productos WHERE monedaCompra = 'Córdobas'";
        try {
            PreparedStatement pst = this.cn.prepareStatement(this.consulta);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {                
                inversion = rs.getFloat("inversion");
            }
            this.cn.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e+"funcion inversion en modelo");
        }
        return inversion;
    }
    public float inversionDolar()
    {
        cn = Conexion();
        float inversion = 0;
        this.consulta = "SELECT SUM(precioCompra*stock) AS inversion FROM productos WHERE monedaCompra = 'Dolar'";
        try {
            PreparedStatement pst = this.cn.prepareStatement(this.consulta);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {                
                inversion = rs.getFloat("inversion");
            }
            this.cn.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e+"funcion inversion en modelo");
        }
        return inversion;
    }
    public void ExitsCodBarra(String codBarra){
        
        String producto = "";
        this.cn = Conexion();
        this.consulta = "SELECT codigoBarra FROM productos WHERE codigoBarra = ?";
        try {
            this.pst = this.cn.prepareStatement(this.consulta);
            this.pst.setString(1, codBarra);
            ResultSet rs = this.pst.executeQuery();
            while(rs.next()){
                producto = rs.getString("codigoBarra");
            }
            if(producto.equals("")){
                setExiste(false);
            }else{
                setExiste(true);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e+" en la funcion ExistCodBarra en modelo productos");
        }
    }
}
