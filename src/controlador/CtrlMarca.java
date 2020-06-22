package controlador;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.table.DefaultTableModel;
import modelo.*;
import vista.IMenu;

public class CtrlMarca implements ActionListener, CaretListener {

    IMenu menu;
    Marca marca;
    String id;
    DefaultTableModel modelo;

    public CtrlMarca(IMenu menu, Marca L) {
        this.menu = menu;
        this.marca = L;
        this.id = null;
        this.modelo = new DefaultTableModel();
        MostrarMarca("");
        DeshabilitarMarca();
        this.menu.btnGuardarLaborotorio.addActionListener(this);
        this.menu.btnActualizarLaboratorio.addActionListener(this);
        this.menu.btnNuevoLaboratorio.addActionListener(this);
        this.menu.EditarLaboratorio.addActionListener(this);
        this.menu.BorrarLaboratorio.addActionListener(this);
        this.menu.txtBuscarLaboratorio.addCaretListener(this);
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == menu.btnGuardarLaborotorio) {
            String nombre = menu.txtNombreLaboratorio.getText(), descripcion = menu.txtDescripcionLaboratorio.getText();
            if (!nombre.equals("")) {
                marca.Guardar(nombre, descripcion);
                MostrarMarca("");
                LimpiarMarca();
            } else {
                //JOptionPane.showMessageDialog(null, "Llene el campo Nombre", "Advertencia", JOptionPane.INFORMATION_MESSAGE);
            }
        }
        if(e.getSource() == menu.btnActualizarLaboratorio)
        {
            String nombre = menu.txtNombreLaboratorio.getText(), descripcion = menu.txtDescripcionLaboratorio.getText();
            if (!nombre.equals("")) {
                marca.Actualizar(id, nombre, descripcion);
                MostrarMarca("");
                LimpiarMarca();
                menu.btnActualizarLaboratorio.setEnabled(false);
                menu.btnGuardarLaborotorio.setEnabled(true);
        } else {
            //JOptionPane.showMessageDialog(null, "Llene el campo Nombre", "Advertencia", JOptionPane.INFORMATION_MESSAGE);
        }
        }
        if(e.getSource() == menu.btnNuevoLaboratorio)
        {
            HabilitarMarca();
        }
        if(e.getSource() == menu.EditarLaboratorio)
        {
            int filaseleccionada;
        String id, nombre, descripcion;
        try {
            filaseleccionada = menu.tblLaboratorio.getSelectedRow();
            if (filaseleccionada == -1) {
                JOptionPane.showMessageDialog(null, "Seleccione una Fila", "Advertencia", JOptionPane.WARNING_MESSAGE);
            } else {
                modelo = (DefaultTableModel) menu.tblLaboratorio.getModel();
                id = (String) modelo.getValueAt(filaseleccionada, 0);
                nombre = (String) modelo.getValueAt(filaseleccionada, 1);
                descripcion = (String) modelo.getValueAt(filaseleccionada, 2);
                HabilitarMarca();
                LimpiarMarca();
                menu.txtNombreLaboratorio.setText(nombre);
                menu.txtDescripcionLaboratorio.setText(descripcion);
                this.id = id;
                menu.btnActualizarLaboratorio.setEnabled(true);
                menu.btnGuardarLaborotorio.setEnabled(false);
            }
        } catch (Exception err) {
            JOptionPane.showMessageDialog(null, err);
        }
        }
        if(e.getSource() == menu.BorrarLaboratorio)
        {
            int filaseleccionada;
        String id;
        try {
            filaseleccionada = menu.tblLaboratorio.getSelectedRow();
            if (filaseleccionada == -1) {
                JOptionPane.showMessageDialog(null, "Seleccione una Fila", "Advertencia", JOptionPane.WARNING_MESSAGE);
            } else {
                int confirmar = JOptionPane.showConfirmDialog(null, "Seguro que Quieres Borrar Este Laboratorio", "Advertencia", JOptionPane.OK_CANCEL_OPTION);
                if (confirmar == JOptionPane.YES_OPTION) {
                    modelo = (DefaultTableModel) menu.tblLaboratorio.getModel();
                    id = (String) modelo.getValueAt(filaseleccionada, 0);
                    marca.Eliminar(id);
                    MostrarMarca("");
                }
            }
        } catch (Exception err) {
            JOptionPane.showMessageDialog(null, err);
        }
        }
    }

    @Override
    public void caretUpdate(CaretEvent e) {
        if (e.getSource() == menu.txtBuscarLaboratorio) {
            MostrarMarca(menu.txtBuscarLaboratorio.getText());
        }
    }

    //metodo para llenar la tabla de laboratorios con filtro por Nombre de marca
    public void MostrarMarca(String Buscar) {
        menu.tblLaboratorio.getTableHeader().setFont(new Font("Sugoe UI", Font.PLAIN, 14));
        menu.tblLaboratorio.getTableHeader().setOpaque(false);
        menu.tblLaboratorio.getTableHeader().setBackground(new Color(100, 100, 100));
        menu.tblLaboratorio.getTableHeader().setForeground(new Color(255, 255, 255));
        menu.tblLaboratorio.setModel(marca.Consulta(Buscar));
    }

    //metodo para limpiar campos de El formulario Laboratorio
    public void LimpiarMarca() {
        menu.txtNombreLaboratorio.setText("");
        menu.txtDescripcionLaboratorio.setText("");
    }
    //metodo para Habilitar los elementos inabilitados por el metodo DeshabilitarMarca
    public void HabilitarMarca() {
        menu.txtNombreLaboratorio.setEnabled(true);
        menu.txtDescripcionLaboratorio.setEnabled(true);
        menu.btnNuevoLaboratorio.setEnabled(true);
        menu.btnGuardarLaborotorio.setEnabled(true);
        menu.btnActualizarLaboratorio.setEnabled(false);
    }
    //metodo para dehabilitar elementos de formulario Laboratorio
    public void DeshabilitarMarca() {
        menu.txtNombreLaboratorio.setEnabled(false);
        menu.txtDescripcionLaboratorio.setEnabled(false);
        menu.btnNuevoLaboratorio.setEnabled(true);
        menu.btnGuardarLaborotorio.setEnabled(false);
        menu.btnActualizarLaboratorio.setEnabled(false);
    }
}
