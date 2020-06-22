
package controlador;

import modelo.*;
import vista.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JOptionPane;

/**
 *
 * @author CESAR DIAZ MARADIAGA
 */
public class CtrlLogin implements ActionListener{

    private ILogin login;
    private Login modelLogin;
    public CtrlMenuOpciones menu;
    public IMenu m;
    public CtrlLogin(ILogin login, Login modelLogin) {
        this.login = login;
        this.modelLogin = modelLogin;
        this.m = new IMenu();
        //this.menu = new CtrlMenuOpciones(m);
        this.login.btnAceptar.addActionListener(this);
    }

    public void iniciar() {
        this.login.setVisible(true);
        this.login.setLocationRelativeTo(null);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        if (e.getSource() == login.btnAceptar) {
            String nombre = login.txtNombreUsuario.getText(), pass = login.txtPassUsuario.getText();
            if (!nombre.equals("") && !pass.equals("")) {
                modelLogin.setNombreUsuario(nombre);
                modelLogin.setPassUsuario(pass);
                if(modelLogin.ValidacionAdmin() == 1){//usuario con permisos administrativos
                    this.menu = new CtrlMenuOpciones(m,1);
                    menu.iniciarMenu();
                    this.login.dispose();  
                }else if(modelLogin.ValidacionAdmin() == 0)//usuario no existe 
                {
                    JOptionPane.showMessageDialog(null, "Nombre o contraseña invalida");
                }else if(modelLogin.ValidacionAdmin() == 2)//usuario con permiso de ventas nada mas
                {
                    this.menu = new CtrlMenuOpciones(m,2);
                    menu.iniciarMenu();
                    this.login.dispose(); 
                }
            } else {
                JOptionPane.showMessageDialog(null, "llene el campo nombre y contraseña");
            }
        }
    }
}