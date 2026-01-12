package com.juego.pelea.entidades;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;

public class Flecha {
    public float x, y;
    private float velocidadX;
    private float velocidadY;
    private float gravedad = 800;
    private TextureRegion region;
    private Rectangle hitbox;
    public boolean activa = true;
    private float rotacion = 0;

    public Flecha(float startX, float startY, boolean mirandoIzquierda, Texture textura) {
        this.x = startX;
        this.y = startY;
        this.region = new TextureRegion(textura);

        // HITBOX
        this.hitbox = new Rectangle(x, y, 32, 8);

        // PARABOLA
        if (mirandoIzquierda) {
            velocidadX = -450;
            region.flip(true, false);
        } else {
            velocidadX = 450;
        }
        velocidadY = 400;
    }

    public void actualizar(float delta) {
        velocidadY -= gravedad * delta;
        x += velocidadX * delta;
        y += velocidadY * delta;

        hitbox.setPosition(x, y);

        // CALCULAR EL ANGULO
        rotacion = MathUtils.atan2(velocidadY, velocidadX) * MathUtils.radiansToDegrees;

        if (y < 75) {
            activa = false;
        }
    }

    public void dibujar(SpriteBatch batch) {
        batch.draw(region, x, y, 16, 4, 32, 8, 1, 1, rotacion);
    }

    public Rectangle getHitbox() {
        return hitbox;
    }
}
