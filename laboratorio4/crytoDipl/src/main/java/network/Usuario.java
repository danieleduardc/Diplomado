package network;

import java.io.Serializable;

public class Usuario implements Serializable {

    private String nombre;
    private double monto;

    public Usuario(String nombre, double monto) {
        this.nombre = nombre;
        this.monto = monto;
    }

    @Override
    public String toString() {
        return "Usuario{" +
                "nombre='" + nombre + '\'' +
                ", monto=" + monto +
                '}';
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public double getMonto() {
        return monto;
    }

    public void setMonto(double monto) {
        this.monto = monto;
    }
}
