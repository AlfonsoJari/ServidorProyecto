package Clases;

import javax.persistence.*;

@Entity
@Table(name="empleados")
public class Empleados {

    @Id
    @Column
    private String clave;
    
    @Column
    private String nombre;
    
    @Column
    private String direccion;
    
    @Column
    private int telefono;

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public int getTelefono() {
        return telefono;
    }

    public void setTelefono(int telefono) {
        this.telefono = telefono;
    }

}
