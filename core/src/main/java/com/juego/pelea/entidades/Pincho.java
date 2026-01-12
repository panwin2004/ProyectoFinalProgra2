package com.juego.pelea.entidades;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

public class Pincho {
    public float x, y;
    public float velocidad = 400;
    public Rectangle hitbox;
    public Texture textura;
    public boolean activo = true;

    public Pincho(float x, float y, Texture textura) {
        this.x = x;
        this.y = y;
        this.textura = textura;
        this.hitbox = new Rectangle(x, y, 32, 10);
    }

    public void actualizar(float delta) {
        x -= velocidad * delta;
        hitbox.setPosition(x, y);

        if (x < -50) activo = false;
    }

    public void dibujar(SpriteBatch batch) {
        batch.draw(textura, x, y, 32, 10);
    }

    public Rectangle getHitbox() {
        return hitbox;
    }
}
