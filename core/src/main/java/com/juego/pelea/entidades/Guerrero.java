package com.juego.pelea.entidades;

import com.badlogic.gdx.graphics.Texture;

public class Guerrero extends Heroe {
    public Guerrero(float x, float y, Texture sheet) {

        super(x, y, 50, 250, 400, 0.2f, sheet, 15);
    }
}
