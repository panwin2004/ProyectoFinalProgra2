package com.juego.pelea.util;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.juego.pelea.entidades.*;
import java.util.ArrayList;
import java.util.Iterator;

public class GestorNivelUtilitario {

    // REFERENCIAS PÚBLICAS
    public Heroe jugador;
    public Monstruo enemigo;

    // LISTAS Y RECURSOS
    private ArrayList<Pincho> listaPinchos;
    private Texture texturaPincho;

    // LÓGICA
    private float tiempoInvulnerabilidad = 0;
    private float tiempoParaDisparo = 0;
    private float cooldownDisparo = 2.5f;

    // ESTADO DEL JUEGO
    public boolean juegoTerminado = false;
    public boolean victoria = false;
    public int puntaje = 0;

    public GestorNivelUtilitario(Heroe jugador, Monstruo enemigo) {
        this.jugador = jugador;
        this.enemigo = enemigo;
        this.listaPinchos = new ArrayList<>();

        // TEXTURA AMARILLA PINCHOS
        Pixmap pixmapPincho = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmapPincho.setColor(Color.YELLOW);
        pixmapPincho.fill();
        texturaPincho = new Texture(pixmapPincho);
        pixmapPincho.dispose();
    }

    public void actualizarLogica(float delta, float tiempoRestante) {
        if (tiempoInvulnerabilidad > 0) tiempoInvulnerabilidad -= delta;

        // GENERAR PINCHOS
        if (enemigo.vida > 0) {
            tiempoParaDisparo += delta;
            if (tiempoParaDisparo > cooldownDisparo) {
                tiempoParaDisparo = 0;
                listaPinchos.add(new Pincho(enemigo.x, enemigo.y + 20, texturaPincho));
            }
        }

        // COLISION PINCHOS
        Iterator<Pincho> iter = listaPinchos.iterator();
        while (iter.hasNext()) {
            Pincho p = iter.next();
            p.actualizar(delta);

            if (p.getHitbox().overlaps(jugador.getHitbox()) && jugador.vida > 0 && tiempoInvulnerabilidad <= 0) {
                jugador.recibirDanio(10);
                tiempoInvulnerabilidad = 1.0f;
                iter.remove();

                if (jugador.vida <= 0) {
                    juegoTerminado = true;
                    victoria = false;
                }
            }
            if (!p.activo) iter.remove();
        }

        verificarProyectiles(tiempoRestante);
    }

    // METODO ATAQUE
    public void realizarAtaqueJugador(float tiempoRestante) {
        jugador.intentarAtacar();

        if (jugador instanceof Cazador) {
            ((Cazador) jugador).disparar();
        }
        else if (jugador instanceof Mago) {
            ((Mago) jugador).lanzarHechizo();
        }
        else if (jugador instanceof Hechicera) {
            ((Hechicera) jugador).lanzarPoder();
        }
        else {

            resolverAtaqueMelee(tiempoRestante);
        }
    }

    private void verificarProyectiles(float tiempoRestante) {
        // Cazador
        if (jugador instanceof Cazador) {
            ArrayList<Flecha> flechas = ((Cazador) jugador).getMisFlechas();
            Iterator<Flecha> iter = flechas.iterator();
            while (iter.hasNext()) {
                Flecha f = iter.next();
                if (f.getHitbox().overlaps(enemigo.getHitbox()) && enemigo.vida > 0) {
                    aplicarDanioAlEnemigo(jugador.getDanio(), tiempoRestante);
                    iter.remove();
                }
            }
        }
        // Mago
        else if (jugador instanceof Mago) {
            ArrayList<ProyectilMagico> magias = ((Mago) jugador).getMisProyectiles();
            Iterator<ProyectilMagico> iter = magias.iterator();
            while (iter.hasNext()) {
                ProyectilMagico pm = iter.next();
                if (pm.getHitbox().overlaps(enemigo.getHitbox()) && enemigo.vida > 0) {
                    aplicarDanioAlEnemigo(jugador.getDanio(), tiempoRestante);
                    pm.activo = false;
                }
            }
        }
        // Hechicera
        else if (jugador instanceof Hechicera) {
            ArrayList<ProyectilOscuro> poderes = ((Hechicera) jugador).getMisProyectiles();
            Iterator<ProyectilOscuro> iter = poderes.iterator();
            while (iter.hasNext()) {
                ProyectilOscuro po = iter.next();
                if (po.getHitbox().overlaps(enemigo.getHitbox()) && enemigo.vida > 0) {
                    aplicarDanioAlEnemigo(jugador.getDanio(), tiempoRestante);
                    po.activo = false;
                }
            }
        }
    }

    public void resolverAtaqueMelee(float tiempoRestante) {
        if (!(jugador instanceof Cazador) && !(jugador instanceof Mago) && !(jugador instanceof Hechicera)) {
            if (jugador.getHitbox().overlaps(enemigo.getHitbox()) && enemigo.vida > 0) {
                aplicarDanioAlEnemigo(jugador.getDanio(), tiempoRestante);
            }
        }
    }

    private void aplicarDanioAlEnemigo(int danio, float tiempoRestante) {
        enemigo.recibirDanio(danio);
        if (enemigo.vida <= 0) {
            enemigo.y = -1000;

            // CALCULAR PUNTAJE
            puntaje = (int) tiempoRestante * 10;

            juegoTerminado = true;
            victoria = true;
        }
    }

    public void dibujarElementos(SpriteBatch batch) {
        for (Pincho p : listaPinchos) {
            p.dibujar(batch);
        }
    }

    public void dispose() {
        texturaPincho.dispose();
    }
}
