/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CDstore;

import modelo.*;
import controlador.*;
import vista.ILogin;

/**
 *
 * @author CESAR DIAZ MARADIAGA
 */
public class CDstore {
    CDstore(){
        
    }
    public static void main(String[] args) {
        ILogin login = new ILogin();
        Login modelLogin = new Login();
        CtrlLogin ctrl = new CtrlLogin(login, modelLogin);
        ctrl.iniciar();
    }
}
