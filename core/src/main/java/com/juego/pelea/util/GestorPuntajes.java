package com.juego.pelea.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import java.util.ArrayList;
import java.util.Collections;

public class GestorPuntajes {
    private static final String PREF_NAME = "mis_juego_puntajes";
    private static final String KEY_SCORES = "lista_puntajes";

    public static void guardarPuntaje(String nombre, String clase, int valor) {

        ArrayList<Puntaje> lista = obtenerPuntajes();
        lista.add(new Puntaje(nombre, clase, valor));
        Collections.sort(lista);


        StringBuilder sb = new StringBuilder();
        for (Puntaje p : lista) {
            sb.append(p.nombre).append(",").append(p.clase).append(",").append(p.valor).append(";");
        }

        Preferences prefs = Gdx.app.getPreferences(PREF_NAME);
        prefs.putString(KEY_SCORES, sb.toString());
        prefs.flush();
    }

    public static ArrayList<Puntaje> obtenerPuntajes() {

        ArrayList<Puntaje> lista = new ArrayList<>();
        Preferences prefs = Gdx.app.getPreferences(PREF_NAME);
        String savedString = prefs.getString(KEY_SCORES, "");

        if (!savedString.isEmpty()) {
            String[] entradas = savedString.split(";");
            for (String entrada : entradas) {
                String[] datos = entrada.split(",");
                if (datos.length == 3) {
                    lista.add(new Puntaje(datos[0], datos[1], Integer.parseInt(datos[2])));
                }
            }
        }
        return lista;
    }

    // BORRAR
    public static void resetearPuntajes() {
        Preferences prefs = Gdx.app.getPreferences(PREF_NAME);
        prefs.clear();
        prefs.flush();
    }

    // SOBRE ESCRIBIR
    public static void actualizarLista(ArrayList<Puntaje> nuevaLista) {
        StringBuilder sb = new StringBuilder();
        for (Puntaje p : nuevaLista) {
            sb.append(p.nombre).append(",").append(p.clase).append(",").append(p.valor).append(";");
        }

        Preferences prefs = Gdx.app.getPreferences(PREF_NAME);
        prefs.putString(KEY_SCORES, sb.toString());
        prefs.flush();
    }
}
