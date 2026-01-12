package com.juego.pelea.entidades;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import java.util.ArrayList;
import java.util.Iterator;

public class Hechicera extends Heroe {

    private ArrayList<ProyectilOscuro> misProyectiles;
    private Texture texturaPoder;

    public Hechicera(float x, float y, Texture textura) {
        super(x, y, 50, 150, 280, 3.0f, textura, 60);

        misProyectiles = new ArrayList<>();

        // TEXTURA MORADA POER
        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.PURPLE);
        pixmap.fill();
        texturaPoder = new Texture(pixmap);
        pixmap.dispose();
    }

    // METODO DISPARAR
    public void lanzarPoder() {
        misProyectiles.add(new ProyectilOscuro(this.x, this.y + 20, this.mirandoIzquierda, texturaPoder));
    }

    @Override
    public void actualizar(float delta) {
        super.actualizar(delta);

        Iterator<ProyectilOscuro> iter = misProyectiles.iterator();
        while (iter.hasNext()) {
            ProyectilOscuro p = iter.next();
            p.actualizar(delta);
            if (!p.activo) iter.remove();
        }
    }

    @Override
    public void dibujar(SpriteBatch batch) {
        super.dibujar(batch);

        for (ProyectilOscuro p : misProyectiles) {
            p.dibujar(batch);
        }
    }

    public ArrayList<ProyectilOscuro> getMisProyectiles() {
        return misProyectiles;
    }
}
