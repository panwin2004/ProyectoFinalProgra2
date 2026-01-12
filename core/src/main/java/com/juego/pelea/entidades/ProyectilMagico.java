package com.juego.pelea.entidades;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

public class ProyectilMagico {
    public float x, y;
    private float velocidadX;

    private Texture textura;
    private Rectangle hitbox;
    public boolean activo = true;

    public ProyectilMagico(float startX, float startY, boolean mirandoIzquierda, Texture textura) {
        this.x = startX;
        this.y = startY;
        this.textura = textura;
        this.hitbox = new Rectangle(x, y, 16, 16);

        if (mirandoIzquierda) {
            velocidadX = -600;
        } else {
            velocidadX = 600;
        }
    }

    public void actualizar(float delta) {
        x += velocidadX * delta;
        hitbox.setPosition(x, y);

        if (x < 0 || x > 800) {
            activo = false;
        }
    }

    public void dibujar(SpriteBatch batch) {
        batch.draw(textura, x, y, 16, 16);
    }

    public Rectangle getHitbox() {
        return hitbox;
    }
}
