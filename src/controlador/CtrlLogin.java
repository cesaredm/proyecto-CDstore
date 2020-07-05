package controlador;

import modelo.*;
import vista.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author CESAR DIAZ MARADIAGA
 */
public class CtrlLogin implements ActionListener, KeyListener {

    private ILogin login;
    private Login modelLogin;
    public CtrlMenuOpciones menu;
    public IMenu m;
    Date fecha;
    File licencia;

    public CtrlLogin(ILogin login, Login modelLogin) {
        this.login = login;
        this.modelLogin = modelLogin;
        this.m = new IMenu();
        this.fecha = new Date();
        modelLogin.validarExist();//validamos la existencia de fecha de actualizacion
        modelLogin.fechaIntalacion(this.fecha.getTime());//si no existe una fecha de instalacion se guardara la fecha si no no hara nada 
        this.login.btnAceptar.addActionListener(this);
        this.login.btnAceptar.addKeyListener(this);
        this.login.txtNombreUsuario.selectAll();
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
            //switch para metodo de prueba de 20 dias
            /*switch (ValidarLicencia()) {
                case "noValido": {
                    JOptionPane.showMessageDialog(null, "Su licencia de prueba finalizó..! \n para adquirir su licencia llama al : +505 85361324");
                }
                break;
                case "valido": {
                    if (!nombre.equals("") && !pass.equals("")) {
                        modelLogin.setNombreUsuario(nombre);
                        modelLogin.setPassUsuario(pass);
                        switch (modelLogin.ValidacionAdmin()) {
                            case 1: {
                                this.menu = new CtrlMenuOpciones(m, 1);
                                menu.iniciarMenu();
                                this.login.dispose();
                            }
                            break;
                            case 2: {
                                this.menu = new CtrlMenuOpciones(m, 2);
                                menu.iniciarMenu();
                                this.login.dispose();
                            }
                            break;
                            case 0:
                                JOptionPane.showMessageDialog(null, "Nombre o contraseña invalida");
                                break;
                        }
                    } else {
                        JOptionPane.showMessageDialog(null, "Complete los campos usuario y password");
                    }
                }
                break;
            }*/

            //metodo sin validacion de metodo de prueba osea licencia de por vida ..
            if (!nombre.equals("") && !pass.equals("")) {
                modelLogin.setNombreUsuario(nombre);
                modelLogin.setPassUsuario(pass);
                switch(modelLogin.ValidacionAdmin()){
                    case 1:{
                        this.menu = new CtrlMenuOpciones(m, 1, nombre);
                        menu.iniciarMenu();
                        this.login.dispose();
                    }break;
                    case 2:{
                        this.menu = new CtrlMenuOpciones(m, 2, nombre);
                        menu.iniciarMenu();
                        this.login.dispose();
                    }break;
                    case 0: JOptionPane.showMessageDialog(null, "Nombre o contraseña invalida");
                    break;
                }
            }else{
                JOptionPane.showMessageDialog(null, "Complete los campos usuario y password");
            }
        }
    }

    //metodo para la licencia de prueba de 20 dias
    public String ValidarLicencia() {
        String response = "";
        long f1 = modelLogin.getFechaIntalacion(), f2 = this.fecha.getTime();
        //resta de la fecha de instalacion - fecha final me da los dias trancurridos de uso 
        int dias = (int) ((f2 - f1) / 86400000);
        //si los dias de uso son menor de 20 se puede seguir usando
        if (dias < 20) {
            response = "valido";
        } else if (dias >= 20) {//si los dias de uso son mayor o igual a los 20 dias se negara la entrada al sistema
            response = "noValido";
        }
        return response;
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.VK_ENTER == e.getKeyCode()) {
            String nombre = login.txtNombreUsuario.getText(), pass = login.txtPassUsuario.getText();
            //switch para metodo de prueba de 20 dias
            /*switch (ValidarLicencia()) {
                case "noValido": {
                    JOptionPane.showMessageDialog(null, "Su licencia de prueba finalizó..! \n para adquirir su licencia llama al : +505 85361324");
                }
                break;
                case "valido": {
                    if (!nombre.equals("") && !pass.equals("")) {
                        modelLogin.setNombreUsuario(nombre);
                        modelLogin.setPassUsuario(pass);
                        switch (modelLogin.ValidacionAdmin()) {
                            case 1: {
                                this.menu = new CtrlMenuOpciones(m, 1);
                                menu.iniciarMenu();
                                this.login.dispose();
                            }
                            break;
                            case 2: {
                                this.menu = new CtrlMenuOpciones(m, 2);
                                menu.iniciarMenu();
                                this.login.dispose();
                            }
                            break;
                            case 0:
                                JOptionPane.showMessageDialog(null, "Nombre o contraseña invalida");
                                break;
                        }
                    } else {
                        JOptionPane.showMessageDialog(null, "Complete los campos usuario y password");
                    }
                }
                break;
            }*/
            if (!nombre.equals("") && !pass.equals("")) {
                modelLogin.setNombreUsuario(nombre);
                modelLogin.setPassUsuario(pass);
                switch(modelLogin.ValidacionAdmin()){
                    case 1:{
                        this.menu = new CtrlMenuOpciones(m, 1, nombre);
                        menu.iniciarMenu();
                        this.login.dispose();
                    }break;
                    case 2:{
                        this.menu = new CtrlMenuOpciones(m, 2, nombre);
                        menu.iniciarMenu();
                        this.login.dispose();
                    }break;
                    case 0: JOptionPane.showMessageDialog(null, "Nombre o contraseña invalida");
                    break;
                }
            }else{
                JOptionPane.showMessageDialog(null, "Complete los campos usuario y password");
            }
        }
    }
}
