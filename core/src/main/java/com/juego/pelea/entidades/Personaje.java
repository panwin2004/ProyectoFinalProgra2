package com.juego.pelea.entidades;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

public abstract class Personaje {
    public float x, y;
    public int vida;
    public int vidaMaxima;
    public float velocidad;
    public int danio;

    public TextureRegion region;
    public Rectangle hitbox;
    public float tiempoHerido = 0;
    public boolean herido = false;
    public boolean mirandoIzquierda = false;

    public float velocidadY = 0;
    public float gravedad = 800;
    public boolean enSuelo = true;

    public Personaje(float x, float y, int vida, float velocidad, Texture textura, int danio) {
        this.x = x;
        this.y = y;
        this.vida = vida;
        this.vidaMaxima = vida;
        this.velocidad = velocidad;
        this.danio = danio;

        this.region = new TextureRegion(textura);
        this.hitbox = new Rectangle(x, y, textura.getWidth(), textura.getHeight());
    }

    // LOGICA DEL DANIO
    public void recibirDanio(int cantidad) {
        if (!herido) {
            this.vida -= cantidad;
            this.herido = true;
            this.tiempoHerido = 0.5f; // Medio segundo en rojo
            if (this.vida < 0) this.vida = 0;
        }
    }

    // ACTUALIZAR LOS TEMPORIZADORES
    public void actualizarEstado(float delta) {
        if (herido) {
            tiempoHerido -= delta;
            if (tiempoHerido <= 0) {
                herido = false;
            }
        }
    }

    public abstract void actualizar(float delta);

    // DIBUJADO
    public void dibujar(SpriteBatch batch) {
        if (mirandoIzquierda && !region.isFlipX()) {
            region.flip(true, false);
        } else if (!mirandoIzquierda && region.isFlipX()) {
            region.flip(true, false);
        }

        if (herido) {
            batch.setColor(Color.RED);
        } else {
            batch.setColor(Color.WHITE);
        }

        batch.draw(region, x, y);

        batch.setColor(Color.WHITE);
    }

    public Rectangle getHitbox() { return hitbox; }
    public int getDanio() { return danio; }
    public int getVidaMaxima() { return vidaMaxima; }
    public boolean estaHerido() { return herido; }
}
