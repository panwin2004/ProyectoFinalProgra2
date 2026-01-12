package com.juego.pelea.util;

public class Puntaje implements Comparable<Puntaje> {
    public String nombre;
    public String clase;
    public int valor;

    public Puntaje(String nombre, String clase, int valor) {
        this.nombre = nombre;
        this.clase = clase;
        this.valor = valor;
    }

    // Esto sirve para ordenar la lista de mayor a menor automáticamente
    @Override
    public int compareTo(Puntaje otro) {
        return otro.valor - this.valor;
    }

    @Override
    public String toString() {
        return nombre + " (" + clase + "): " + valor;
    }
}
