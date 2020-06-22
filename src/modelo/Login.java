package modelo;

import controlador.CtrlProducto;
import vista.ILogin;
import java.sql.*;
import javax.swing.JOptionPane;
import vista.IMenu;

/**
 *
 * @author CESAR DIAZ MARADIAGA
 */
public class Login extends Conexiondb {

    private String nombreUsuario, passUsuario;

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public String getPassUsuario() {
        return passUsuario;
    }

    public void setPassUsuario(String passUsuario) {
        this.passUsuario = passUsuario;
    }

    public Login(/*String nombreUsuario, String passUsuario*/) {
        /*this.nombreUsuario = nombreUsuario;
        this.passUsuario = passUsuario;*/
    }

    public int ValidacionAdmin() 
    {
        Connection cn = Conexion();
        String consultaUsuario = "SELECT * FROM usuarios WHERE nombreUsuario = ? AND password = ?";
        int bandera = 0;
        try {
            PreparedStatement pst = cn.prepareStatement(consultaUsuario);
            pst.setString(1, this.nombreUsuario);
            pst.setString(2, this.passUsuario);
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                if (rs.getString("nombreUsuario").equals(this.nombreUsuario) && rs.getString("password").equals(this.passUsuario)) {   
                        if(rs.getString("permiso").equals("Administrador"))
                        {
                            bandera = 1;//1 si es administardor
                        }else if(rs.getString("permiso").equals("Ventas"))
                        {
                            bandera = 2;//2 si es de ventas
                        }
                } else {
                    bandera = 0;//0 si no existe
                }

            }
            cn.close();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex+"login");
        }
        return bandera;
    }
}
