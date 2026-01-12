package com.juego.pelea.entidades;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import java.util.ArrayList;
import java.util.Iterator;

public class Mago extends Heroe {

    private ArrayList<ProyectilMagico> misProyectiles;
    private Texture texturaMagia;

    public Mago(float x, float y, Texture textura) {

        super(x, y, 50, 100, 280, 0.2f, textura, 12);

        misProyectiles = new ArrayList<>();

        // TEXTURAMAGIA
        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.CYAN);
        pixmap.fill();
        texturaMagia = new Texture(pixmap);
        pixmap.dispose();
    }

    public void lanzarHechizo() {
        misProyectiles.add(new ProyectilMagico(this.x, this.y + 20, this.mirandoIzquierda, texturaMagia));
    }

    //MOVIMIENTO
    @Override
    public void actualizar(float delta) {
        super.actualizar(delta);

        Iterator<ProyectilMagico> iter = misProyectiles.iterator();
        while (iter.hasNext()) {
            ProyectilMagico p = iter.next();
            p.actualizar(delta);
            if (!p.activo) iter.remove();
        }
    }
    //DIBUJAR
    @Override
    public void dibujar(SpriteBatch batch) {
        super.dibujar(batch);

        for (ProyectilMagico p : misProyectiles) {
            p.dibujar(batch);
        }
    }

    public ArrayList<ProyectilMagico> getMisProyectiles() {
        return misProyectiles;
    }
}
