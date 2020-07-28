package Clases;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Observable;
import javax.swing.table.DefaultTableModel;

public class DAO_Empleado extends Observable implements Serializable {

    //obtener todos los empleados
    public void obtenerEmpleados(DefaultTableModel modelo) throws SQLException {
        int filas = modelo.getRowCount();
        for (int i = 1; i <= filas; i++) {
            modelo.removeRow(0);
        }
        ResultSet rs = ConexionUnica.getInstance().select("SELECT * FROM Empleados");
        while (rs.next()) {
            modelo.addRow(new Object[]{rs.getString("clave"), rs.getString("nombre"), rs.getString("direccion"), rs.getString("telefono")});
        }
        rs.close();
        setChanged();
        notifyObservers("Se han consultado todos los empleados");
    }

    //obtener un empleado por su clave
    public void obtenerEmpleado(DefaultTableModel modelo, String clave) throws SQLException {
        int filas = modelo.getRowCount();
        for (int i = 1; i <= filas; i++) {
            modelo.removeRow(0);
        }
        ResultSet rs = ConexionUnica.getInstance().select("SELECT * FROM Empleados WHERE clave = '" + clave + "'");
        rs.next();
        modelo.addRow(new Object[]{rs.getString("clave"), rs.getString("nombre"), rs.getString("direccion"), rs.getString("telefono")});
        rs.close();
        setChanged();
        notifyObservers("Se ha consultado un empleado. ID: " + clave);
    }

    //anadir un empleado
    public void anadirEmpleado(String clave, String nombre, String direccion, String telefono) throws SQLException {
        PreparedStatement st = ConexionUnica.getInstance().getConnection().prepareStatement("INSERT INTO Empleados (clave, nombre, direccion, telefono) VALUES (?, ?, ?, ?)");
        st.setString(1, clave);
        st.setString(2, nombre);
        st.setString(3, direccion);
        st.setString(4, telefono);
        st.executeUpdate();
        st.close();
        setChanged();
        notifyObservers("Se ha añadido un nuevo empleado. ID: " + clave);
    }

    //actualizar un empleado por su clave
    public void actualizarEmpleado(String clave, String nombre, String direccion, String telefono) throws SQLException {
        PreparedStatement st;
        st = ConexionUnica.getInstance().getConnection().prepareStatement("UPDATE Empleados SET nombre = ? WHERE clave = ?");
        st.setString(1, nombre);
        st.setString(2, clave);
        st.executeUpdate();

        st = ConexionUnica.getInstance().getConnection().prepareStatement("UPDATE Empleados SET direccion = ? WHERE clave = ?");
        st.setString(1, direccion);
        st.setString(2, clave);
        st.executeUpdate();

        st = ConexionUnica.getInstance().getConnection().prepareStatement("UPDATE Empleados SET telefono = ? WHERE clave = ?");
        st.setString(1, telefono);
        st.setString(2, clave);
        st.executeUpdate();
        st.close();
        setChanged();
        notifyObservers("Se ha actualizado un empleado. ID:" + clave);
    }

    //eliminar un empleado por su clave
    public void eliminarCliente(String clave) throws SQLException {
        PreparedStatement st = ConexionUnica.getInstance().getConnection().prepareStatement("DELETE FROM Empleados WHERE clave = ?");
        st.setString(1, clave);
        st.executeUpdate();
        st.close();
        setChanged();
        notifyObservers("Se ha eliminado un empleado. ID:" + clave);
    }
}
