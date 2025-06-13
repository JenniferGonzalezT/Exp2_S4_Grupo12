/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.proyecto_biblioteca.objetos;

import java.util.Objects;

/**
 * Clase Usuario
 * @author jennifer y guiselle
 */

public class Usuario {
    private String nombre;
    private String apellido;
    private String rut;


    public Usuario() {
    }

    public Usuario(String nombre, String apellido, String rut) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.rut = rut;
    }


    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getRut() {
        return rut;
    }

    public void setRut(String rut) {
        this.rut = rut;
    }


    @Override
    public String toString() {
        return "Usuario: " + nombre + " " + apellido + ", rut: " + rut;
    }

    @Override
    public int hashCode() {
        return Objects.hash(rut);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Usuario other = (Usuario) obj;
        return Objects.equals(this.rut, other.rut);
    }

}
