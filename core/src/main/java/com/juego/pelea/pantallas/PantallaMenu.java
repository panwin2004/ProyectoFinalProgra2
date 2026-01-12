package com.juego.pelea.pantallas;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;

//NO OLVIDARME DE AGREGAR EL BOTON SALIR

public class PantallaMenu implements Screen {
    final MainJuego game;

    OrthographicCamera camera;
    BitmapFont font;
    Texture texturaBoton;
    Texture imagenFondo;

    GlyphLayout layout;

    // RECTANGULOS PARA CADA BOTON
    Rectangle btnJugar;
    Rectangle btnPuntajes;
    Rectangle btnSalir;

    Vector3 touchPoint;

    public PantallaMenu(MainJuego game) {
        this.game = game;

        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 480);

        font = new BitmapFont();
        font.getData().setScale(2);

        layout = new GlyphLayout();

        imagenFondo = new Texture("pixelartfondo2.jpeg");

        // TEXTURA DE LOS BOTONES
        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.WHITE);
        pixmap.fill();
        texturaBoton = new Texture(pixmap);
        pixmap.dispose();

        // UBICACION DE LOS BOTONES

        btnJugar = new Rectangle(250, 280, 300, 60);    // Más arriba
        btnPuntajes = new Rectangle(250, 180, 300, 60); // En medio
        btnSalir = new Rectangle(250, 80, 300, 60);     // Abajo (NUEVO)

        touchPoint = new Vector3();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        game.batch.setProjectionMatrix(camera.combined);

        game.batch.begin();

        // EL FONDO
        game.batch.setColor(Color.WHITE);
        game.batch.draw(imagenFondo, 0, 0, camera.viewportWidth, camera.viewportHeight);

        // TITULO DEL JUEGO
        font.setColor(Color.YELLOW);
        String titulo = "MATA-A-CHICLE-BOMBA";
        layout.setText(font, titulo);
        font.draw(game.batch, titulo, (800 - layout.width) / 2, 450);





        // DIBUJO BOTONES

        // BOTÓN JUGAR
        game.batch.setColor(Color.DARK_GRAY);
        game.batch.draw(texturaBoton, btnJugar.x, btnJugar.y, btnJugar.width, btnJugar.height);

        font.setColor(Color.WHITE);
        layout.setText(font, "JUGAR");
        // Centramos el texto dentro del botón matemáticamente
        font.draw(game.batch, "JUGAR", btnJugar.x + (btnJugar.width - layout.width)/2, btnJugar.y + 45);




        // BOTÓN PUNTAJES
        game.batch.setColor(Color.DARK_GRAY);
        game.batch.draw(texturaBoton, btnPuntajes.x, btnPuntajes.y, btnPuntajes.width, btnPuntajes.height);

        font.setColor(Color.WHITE);
        layout.setText(font, "PUNTAJES");
        font.draw(game.batch, "PUNTAJES", btnPuntajes.x + (btnPuntajes.width - layout.width)/2, btnPuntajes.y + 45);




        // BOTÓN SALIR
        game.batch.setColor(Color.DARK_GRAY);
        game.batch.draw(texturaBoton, btnSalir.x, btnSalir.y, btnSalir.width, btnSalir.height);

        font.setColor(Color.WHITE);
        layout.setText(font, "SALIR");
        font.draw(game.batch, "SALIR", btnSalir.x + (btnSalir.width - layout.width)/2, btnSalir.y + 45);

        game.batch.end();




        // BOTONES
        if (Gdx.input.justTouched()) {

            camera.unproject(touchPoint.set(Gdx.input.getX(), Gdx.input.getY(), 0));

            // CLIC JUGAR
            if (btnJugar.contains(touchPoint.x, touchPoint.y)) {

                game.setScreen(new PantallaNombre(game, "pixelartfondo2.jpeg"));
                dispose();
            }

            // CLIC PUNTAJES
            if (btnPuntajes.contains(touchPoint.x, touchPoint.y)) {
                game.setScreen(new PantallaPuntajes(game));
                dispose();
            }

            // CLIC SALIR
            if (btnSalir.contains(touchPoint.x, touchPoint.y)) {
                Gdx.app.exit();
            }
        }
    }

    @Override public void resize(int width, int height) {}
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}
    @Override public void show() {}

    @Override
    public void dispose() {
        font.dispose();
        texturaBoton.dispose();
        imagenFondo.dispose();
    }
}
