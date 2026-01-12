package com.juego.pelea.entidades;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;

public class Heroe extends Personaje {

    protected float fuerzaSalto;
    protected float cooldownAtaque;

    public Heroe(float x, float y, int vida, float velocidad, float fuerzaSalto, float cooldownAtaque, Texture textura, int danio) {
        super(x, y, vida, velocidad, textura, danio);
        this.fuerzaSalto = fuerzaSalto;
        this.cooldownAtaque = cooldownAtaque;
    }

    public float getCooldownAtaque() {
        return cooldownAtaque;
    }

    @Override
    public void actualizar(float delta) {
        // MOVMIENTO
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            x -= velocidad * delta;
            mirandoIzquierda = true;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            x += velocidad * delta;
            mirandoIzquierda = false;
        }

        // SALTO
        if (Gdx.input.isKeyJustPressed(Input.Keys.UP) && enSuelo) {
            velocidadY = fuerzaSalto;
            enSuelo = false;
        }

        // FISICAS
        velocidadY -= gravedad * delta;
        y += velocidadY * delta;

        // PISO
        if (y < 75) {
            y = 75;
            velocidadY = 0;
            enSuelo = true;
        }

        // ACTUALIZAR HITBOX
        hitbox.setPosition(x, y);
    }

    public void intentarAtacar() {
    }
}
