package com.juego.pelea.entidades;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import java.util.ArrayList;
import java.util.Iterator;

public class Cazador extends Heroe {

    private ArrayList<Flecha> misFlechas;
    private Texture texturaFlecha;

    public Cazador(float x, float y, Texture sheet) {

        super(x, y, 50, 200, 350, 1f, sheet, 30);

        misFlechas = new ArrayList<>();

        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(new Color(0.6f, 0.4f, 0.2f, 1f));
        pixmap.fill();
        texturaFlecha = new Texture(pixmap);
        pixmap.dispose();
    }

    // METODO DISPARAR
    public void disparar() {
        misFlechas.add(new Flecha(this.x, this.y + 20, this.mirandoIzquierda, texturaFlecha));
    }

    @Override
    public void actualizar(float delta) {
        super.actualizar(delta);

        Iterator<Flecha> iter = misFlechas.iterator();
        while (iter.hasNext()) {
            Flecha f = iter.next();
            f.actualizar(delta);
            if (!f.activa) iter.remove();
        }
    }

    @Override
    public void dibujar(SpriteBatch batch) {
        super.dibujar(batch);

        for (Flecha f : misFlechas) {
            f.dibujar(batch);
        }
    }

    public ArrayList<Flecha> getMisFlechas() {
        return misFlechas;
    }
}
