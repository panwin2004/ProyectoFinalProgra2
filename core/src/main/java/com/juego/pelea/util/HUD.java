package com.juego.pelea.util;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class HUD {
    private BitmapFont font;
    private GlyphLayout layout;
    private Texture texturaBarra;
    private GestorNivelUtilitario gestor;
    private float alturaHeroe = 64;


    public HUD(GestorNivelUtilitario gestor) {
        this.gestor = gestor;
        this.font = new BitmapFont();
        this.font.getData().setScale(2);
        this.layout = new GlyphLayout();

        // TECTURA PARA LAS BARRAS
        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.WHITE);
        pixmap.fill();
        texturaBarra = new Texture(pixmap);
        pixmap.dispose();
    }

    public void dibujar(SpriteBatch batch, float tiempoRestante, float tiempoCuentaRegresiva, boolean enCuentaRegresiva, String nombreJugador, float tiempoCarga) {

        // 1. BARRAS DE VIDA DEL UGADOR
        if (gestor.jugador.vida > 0) {
            // ... (tu código de barra de vida roja/verde) ...
            float porc = (float) gestor.jugador.vida / (float) gestor.jugador.vidaMaxima;
            float yBarraHeroe = gestor.jugador.y + alturaHeroe + 10;

            batch.setColor(Color.RED);
            batch.draw(texturaBarra, gestor.jugador.x, yBarraHeroe, 100, 10);
            if (porc > 0) {
                batch.setColor(Color.GREEN);
                batch.draw(texturaBarra, gestor.jugador.x, yBarraHeroe, 100 * porc, 10);
            }

            // BARRA DE RECARGA

            float maxCooldown = gestor.jugador.getCooldownAtaque();
            float porcentajeCarga = tiempoCarga / maxCooldown;


            if (porcentajeCarga > 1) porcentajeCarga = 1;
            float yBarraCarga = yBarraHeroe + 15;
            batch.setColor(Color.DARK_GRAY);
            batch.draw(texturaBarra, gestor.jugador.x, yBarraCarga, 80, 4);

            if (porcentajeCarga >= 1) {
                batch.setColor(Color.CYAN);
            } else {
                batch.setColor(Color.ORANGE);
            }
            batch.draw(texturaBarra, gestor.jugador.x, yBarraCarga, 80 * porcentajeCarga, 4);
        }

        // BARRAS DE VIDA MOSTRUO
        if (gestor.enemigo.vida > 0) {
            float porc = (float) gestor.enemigo.vida / (float) gestor.enemigo.vidaMaxima;


            float yBarraMonstruo = gestor.enemigo.y + gestor.enemigo.hitbox.height + 20;

            batch.setColor(Color.RED);
            batch.draw(texturaBarra, gestor.enemigo.x, yBarraMonstruo, 100, 10);

            if (porc > 0) {
                batch.setColor(Color.YELLOW);
                batch.draw(texturaBarra, gestor.enemigo.x, yBarraMonstruo, 100 * porc, 10);
            }

            if (gestor.enemigo.tieneEscudo()) {
                font.setColor(Color.CYAN);
                font.draw(batch, "!!ESCUDO!!", gestor.enemigo.x - 10, yBarraMonstruo + 30);
            }
        }
        batch.setColor(Color.WHITE);

        // TEXTOS DE LA PANTALLA
        if (enCuentaRegresiva) {
            font.setColor(Color.YELLOW);
            font.getData().setScale(4);
            int numero = (int) tiempoCuentaRegresiva;
            String texto = (numero > 0) ? String.valueOf(numero) : "¡YA!";
            layout.setText(font, texto);
            font.draw(batch, texto, (800 - layout.width)/2, 280);
            font.getData().setScale(2);
        }
        else if (!gestor.juegoTerminado) {
            font.setColor(Color.YELLOW);
            font.draw(batch, "TIEMPO: " + (int)tiempoRestante, 20, 460);
            font.setColor(Color.WHITE);
            font.getData().setScale(1.5f);
            String nombreClase = gestor.jugador.getClass().getSimpleName();
            font.draw(batch, "JUGADOR: " + nombreJugador + " (" + nombreClase + ")", 20, 430);
            font.getData().setScale(2);
        }
        else {
            if (gestor.victoria) {
                font.setColor(Color.GREEN);
                layout.setText(font, "¡VICTORIA!");
                font.draw(batch, "¡VICTORIA!", (800 - layout.width)/2, 350);
                font.setColor(Color.WHITE);
                String txtPuntaje = "PUNTAJE DE " + nombreJugador + ": " + gestor.puntaje;
                layout.setText(font, txtPuntaje);
                font.draw(batch, txtPuntaje, (800 - layout.width)/2, 280);
            } else {
                font.setColor(Color.RED);
                layout.setText(font, "¡GAME OVER!");
                font.draw(batch, "¡GAME OVER!", (800 - layout.width)/2, 350);
            }
            font.setColor(Color.YELLOW);
            layout.setText(font, "Presiona [ESCAPE] para volver al Menu");
            font.draw(batch, "Presiona [ESCAPE] para volver al Menu", (800 - layout.width)/2, 150);
        }
    }

    public void dispose() {
        texturaBarra.dispose();
        font.dispose();
    }
}
