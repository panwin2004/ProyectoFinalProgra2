package com.juego.pelea.entidades;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Monstruo extends Personaje {

    // MOVMIENTO Y ESCUDO
    private float direccionX = 1;
    private float limiteIzquierda = 350;
    private float limiteDerecha = 600;

    private float tiempoParaEscudo = 0;
    private boolean escudoActivo = false;
    private float duracionEscudo = 2.0f;
    private float cooldownEscudo = 6.0f;

    public Monstruo(float x, float y, Texture texture) {
        super(x, y, 500, 80, texture, 15);
    }

    @Override
    public void actualizar(float delta) {
        // IA MOVIMIENNTO
        x += velocidad * direccionX * delta;
        if (x > limiteDerecha) { direccionX = -1; mirandoIzquierda = true; }
        if (x < limiteIzquierda) { direccionX = 1; mirandoIzquierda = false; }

        // FISICA
        velocidadY -= gravedad * delta;
        y += velocidadY * delta;
        if (y < 75) { y = 75; velocidadY = 0; enSuelo = true; }

        hitbox.setPosition(x, y);

        //LOGICA ESCUDO
        if (escudoActivo) {
            tiempoParaEscudo -= delta;
            if (tiempoParaEscudo <= 0) {
                escudoActivo = false;
                tiempoParaEscudo = 0;
            }
        } else {
            tiempoParaEscudo += delta;
            if (tiempoParaEscudo >= cooldownEscudo) {
                escudoActivo = true;
                tiempoParaEscudo = duracionEscudo;
            }
        }
    }

    @Override
    public void recibirDanio(int cantidad) {
        if (!escudoActivo) {
            super.recibirDanio(cantidad); // Solo recibe daño si NO hay escudo
        }
    }

    // --- DIBUJADO ESPECIAL (Verde si hay escudo) ---
    @Override
    public void dibujar(SpriteBatch batch) {
        if (escudoActivo) {
            // Lógica de volteo manual (copiada del padre)
            if (mirandoIzquierda && !region.isFlipX()) region.flip(true, false);
            else if (!mirandoIzquierda && region.isFlipX()) region.flip(true, false);

            // COLOR VERDE
            batch.setColor(Color.GREEN);
            batch.draw(region, x, y);
            batch.setColor(Color.WHITE); // Resetear
        } else {
            // Si no hay escudo, usa la lógica normal (Rojo o Blanco)
            super.dibujar(batch);
        }
    }

    public boolean tieneEscudo() { return escudoActivo; }
}
