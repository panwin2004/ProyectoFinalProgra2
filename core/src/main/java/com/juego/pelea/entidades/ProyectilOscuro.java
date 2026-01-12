package com.juego.pelea.entidades;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

public class ProyectilOscuro {
    public float x, y;
    private float velocidadX;
    private float startX;
    private float alcanceMaximo = 400;
    private Texture textura;
    private Rectangle hitbox;
    public boolean activo = true;

    public ProyectilOscuro(float startX, float startY, boolean mirandoIzquierda, Texture textura) {
        this.x = startX;
        this.y = startY;
        this.startX = startX;
        this.textura = textura;

        // HITBOX DE PODER
        this.hitbox = new Rectangle(x, y, 40, 40);

        // VELOCIDAD DEL PODER
        if (mirandoIzquierda) {
            velocidadX = -400;
        } else {
            velocidadX = 400;
        }
    }

    public void actualizar(float delta) {
        x += velocidadX * delta;
        hitbox.setPosition(x, y);

        if (x < 0 || x > 800) activo = false;

        // ALCANCE DE LA HABILIDAD
        if (Math.abs(x - startX) > alcanceMaximo) {
            activo = false;
        }
    }

    public void dibujar(SpriteBatch batch) {
        batch.draw(textura, x, y, 40, 40);
    }

    public Rectangle getHitbox() {
        return hitbox;
    }
}
