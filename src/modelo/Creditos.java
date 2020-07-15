/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

import javax.swing.table.DefaultTableModel;
import java.sql.*;
import javax.swing.JOptionPane;

/**
 *
 * @author CESAR DIAZ MARADIAGA
 */
public class Creditos extends Conexiondb {

    DefaultTableModel modelo;
    Connection cn;
    String consulta;
    String[] resgistros;
    PagosCreditos pagos;
    PreparedStatement pst;
    int banderin;

    public Creditos() {
        this.cn = null;
        this.pst = null;
        this.consulta = null;
        this.banderin = 0;
        this.pagos = new PagosCreditos();
    }

    //Funcion para guardar los creditos
    public void GuardarCredito(int cliente, Date fecha, String estado) {
        cn = Conexion();
        //consulta sql para guardar creditos
        this.consulta = "INSERT INTO creditos(cliente, estado, fecha) VALUES(?,?,?)";
        try {
            pst = this.cn.prepareStatement(this.consulta);
            pst.setInt(1, cliente);
            pst.setString(2, estado);
            pst.setDate(3, fecha);
            this.banderin = pst.executeUpdate();
            if (this.banderin > 0) {
                JOptionPane.showMessageDialog(null, "Credito Agregado exitosamente", "Informacion", JOptionPane.INFORMATION_MESSAGE);
            }
            cn.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    //Funcion para actualizar los creditos
    public void Actualizar(int id, int cliente, Date fecha, String estado) {
        cn = Conexion();
        this.consulta = "UPDATE creditos SET cliente = ?, estado = ?, fecha = ? WHERE id=?";
        try {
            pst = this.cn.prepareStatement(this.consulta);
            pst.setInt(1, cliente);
            pst.setString(2, estado);
            pst.setDate(3, fecha);
            pst.setInt(4, id);
            this.banderin = pst.executeUpdate();
            if (this.banderin > 0) {
                JOptionPane.showMessageDialog(null, "Credito " + id + " Actualizado exitosamente", "Informacion", JOptionPane.INFORMATION_MESSAGE);
            }
            cn.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    //funcion para eliminar creditos
    public void Eliminar(int id) {
        cn = Conexion();
        this.consulta = "DELETE FROM creditos WHERE id = ?";
        try {
            pst = this.cn.prepareStatement(this.consulta);
            pst.setInt(1, id);
            this.banderin = pst.executeUpdate();
            if (this.banderin > 0) {
                JOptionPane.showMessageDialog(null, "Credito " + id + " Borrado exitosamente", "Informacion", JOptionPane.INFORMATION_MESSAGE);
            }
            cn.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    //funcion de consulta de datos de creditos y retornar una tabla con los creditos para mostrarla en interfaz
    public DefaultTableModel Mostrar(String buscar) {
        cn = Conexion();
        this.consulta = "SELECT creditos.id,SUM(facturas.totalFactura) AS totalCredito, clientes.id as idCliente,nombres,apellidos, creditos.estado FROM creditos INNER JOIN clientes ON(creditos.cliente = clientes.id) INNER JOIN facturas ON(facturas.credito = creditos.id) WHERE CONCAT(creditos.id, clientes.nombres, clientes.apellidos) LIKE '%" + buscar + "%' AND creditos.estado = 'Pendiente' GROUP BY clientes.nombres";
        String[] titulos = {"N° Credito", "Total de Credito", "Id Cliente", "Nombres", "Apellidos", "Estado"};
        float saldo = 0, monto = 0;
        this.resgistros = new String[6];
        this.modelo = new DefaultTableModel(null, titulos) {
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };
        try {
            pst = this.cn.prepareStatement(this.consulta);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                //en la variable monto obtengo el total de pagos de el cliente
                monto = this.pagos.PagosCliente(rs.getString("idCliente"));
                //en la variable saldo obtengo lo que queda de la resta de lo que debe el cliente menos total de pagos que ha hecho
                saldo = Float.parseFloat(rs.getString("totalCredito")) - monto;
                this.resgistros[0] = rs.getString("id");
                this.resgistros[1] = String.valueOf(saldo);
                this.resgistros[2] = rs.getString("idCliente");
                this.resgistros[3] = rs.getString("nombres");
                this.resgistros[4] = rs.getString("apellidos");
                this.resgistros[5] = rs.getString("estado");
                this.modelo.addRow(resgistros);
            }
            cn.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e + " mostrar creditos");
        }
        return this.modelo;
    }

    //funcion para mostrar Los creditos abiertos o creados
    public DefaultTableModel MostrarCreditosCreados(String buscar) {
        cn = Conexion();
        this.consulta = "SELECT creditos.id as idCredito,cliente, clientes.nombres, apellidos, creditos.fecha, estado FROM creditos INNER JOIN clientes ON(clientes.id = creditos.cliente) WHERE CONCAT(creditos.id, clientes.nombres, clientes.apellidos) LIKE '%" + buscar + "%'";
        String[] titulos = {"N° Credito", "Id Cliente", "Nombres", "Apellidos", "fecha", "Estado"};
        this.resgistros = new String[6];
        this.modelo = new DefaultTableModel(null, titulos) {
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };
        try {
            pst = this.cn.prepareStatement(this.consulta);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                this.resgistros[0] = rs.getString("idCredito");
                this.resgistros[1] = rs.getString("cliente");
                this.resgistros[2] = rs.getString("nombres");
                this.resgistros[3] = rs.getString("apellidos");
                this.resgistros[4] = rs.getString("fecha");
                this.resgistros[5] = rs.getString("estado");
                this.modelo.addRow(resgistros);
            }
            cn.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e + " mostrar creditos");
        }
        return this.modelo;
    }
    //mostrar solo los creditos abiertos y pendientes menos los cancelados 
    public DefaultTableModel MostrarCreditosAddFactura(String buscar) {
        cn = Conexion();
        this.consulta = "SELECT creditos.id as idCredito,cliente, clientes.nombres, apellidos, creditos.fecha, estado FROM creditos INNER JOIN clientes ON(clientes.id = creditos.cliente) WHERE CONCAT(creditos.id, clientes.nombres, clientes.apellidos) LIKE '%" + buscar + "%' AND creditos.estado!='Cancelado'";
        String[] titulos = {"N° Credito", "Id Cliente", "Nombres", "Apellidos", "fecha", "Estado"};
        this.resgistros = new String[6];
        this.modelo = new DefaultTableModel(null, titulos) {
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };
        try {
            pst = this.cn.prepareStatement(this.consulta);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                this.resgistros[0] = rs.getString("idCredito");
                this.resgistros[1] = rs.getString("cliente");
                this.resgistros[2] = rs.getString("nombres");
                this.resgistros[3] = rs.getString("apellidos");
                this.resgistros[4] = rs.getString("fecha");
                this.resgistros[5] = rs.getString("estado");
                this.modelo.addRow(resgistros);
            }
            cn.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e + " mostrar creditos");
        }
        return this.modelo;
    }

    //funcion para actualizar el estado del credito segun lo que debe o a ha pagado el cliente
    public void ActualizarEstadoCredito(int id, String estado) {
        cn = Conexion();
        this.consulta = "UPDATE creditos SET estado=? WHERE id=?";
        try {
            pst = this.cn.prepareStatement(this.consulta);
            pst.setString(1, estado);
            pst.setInt(2, id);
            this.banderin = pst.executeUpdate();
            if (this.banderin > 0) {
                //JOptionPane.showMessageDialog(null,"Credito "+id+" Cancelado");
            }
            cn.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e + " Actualizar Estado de Credito");
        }
    }

    //funcion que que me obtiene el total de credito que debe el cliente
    public float TotalCreditoCliente(int id) {
        cn = Conexion();
        float creditoCliente = 0;
        int idCredito = 0;
        String estado = "";
        this.consulta = "SELECT creditos.id,SUM(facturas.totalFactura) AS totalCredito, clientes.id as idCliente,nombres,apellidos, creditos.estado FROM creditos INNER JOIN clientes ON(creditos.cliente = clientes.id) INNER JOIN facturas ON(facturas.credito = creditos.id) WHERE clientes.id = ? AND creditos.estado = 'Abierto'";
        try {
            pst = this.cn.prepareStatement(this.consulta);
            pst.setInt(1, id);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                creditoCliente = rs.getFloat("totalCredito");
                idCredito = rs.getInt("id");
                estado = rs.getString("estado");
            }
            cn.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e + " total credito cliente");
        }
        return creditoCliente;
    }
    //
    public DefaultTableModel MostrarFacturasPorCreditdos(int id)
    {
       this.cn = Conexion();
       //TODO agregar campo impuestoISV ala consulta y al titulo
       String[] titulos = {"N° Fact.","TotalFactura","N° Credito","Nombre", "Apellido"};
       resgistros = new String[6];
       this.modelo = new DefaultTableModel(null, titulos);
       this.consulta = "SELECT facturas.id AS Factura, impuestoISV as IVA, totalfactura, creditos.id AS Credito, clientes.nombres,apellidos FROM facturas "
               + "INNER JOIN creditos on(facturas.credito = creditos.id) INNER JOIN clientes ON(clientes.id = creditos.cliente) WHERE creditos.id=? AND creditos.estado = 'Pendiente'";
        try {
            this.pst = this.cn.prepareStatement(this.consulta);
            pst.setInt(1, id);
            ResultSet rs = pst.executeQuery();
            while(rs.next())
            {
                resgistros[0] = rs.getString("Factura");
//                resgistros[1] = rs.getString("IVA");
                resgistros[1] = rs.getString("totalFactura");
                resgistros[2] = rs.getString("Credito");
                resgistros[3] = rs.getString("nombres");
                resgistros[4] = rs.getString("apellidos");
                this.modelo.addRow(resgistros);
            }
            cn.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        }
        return this.modelo;
    }
    //
    public void ActualizarCreditoFactura(int idFactura, int idCredito)
    {
        this.cn = Conexion();
        this.consulta = "UPDATE facturas SET credito = ? FROM id = ?";
        try {
            pst = cn.prepareStatement(consulta);
            pst.setInt(1, idCredito);
            pst.setInt(2, idFactura);
            pst.execute();
            cn.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }
    public boolean VerificarExistenciaDeCredito(String id)
    {
        this.cn = Conexion();
        String valor="";
        boolean validar = true;
        this.consulta = "SELECT clientes.id FROM clientes INNER JOIN creditos ON(clientes.id=creditos.cliente) WHERE clientes.id = ?";
        try {
            pst = cn.prepareStatement(this.consulta);
            pst.setString(1, id);
            ResultSet rs = pst.executeQuery();
            while(rs.next())
            {
                valor = rs.getString("id");
            }
            if(valor.equals(""))
            {
                validar = false;
            }else
            {
                validar = true;
            }
            this.cn.close();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e+"funcion VerificarExistenciaDeCredito");
        }
        return validar;
    }
    //metodo para obtener el nombre de cliente que tiene el credito
    public String NombreCliente(String idCredito)
    {
        String nombre = "";
        this.cn = Conexion();
        this.consulta = "SELECT c.nombres, c.apellidos FROM clientes AS c INNER JOIN creditos ON(c.id = creditos.cliente) WHERE creditos.id = ?";
        try {
            this.pst = this.cn.prepareStatement(this.consulta);
            this.pst.setString(1, idCredito);
            ResultSet rs = this.pst.executeQuery();
            while(rs.next()){
                nombre = rs.getString("nombres")+" "+rs.getString("apellidos");
            }
        } catch (SQLException e) {
             JOptionPane.showMessageDialog(null, e+"funcion NombreCliente en el modelo credito");
        }
        return nombre;
    }
}
