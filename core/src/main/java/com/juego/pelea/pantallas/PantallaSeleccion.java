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

public class PantallaSeleccion implements Screen {
    final MainJuego game;
    String nombreJugador;

    OrthographicCamera camera;
    BitmapFont font;
    Texture imagenFondo;
    Texture texturaBoton;
    GlyphLayout layout;

    // IMÁGENES DE LOS PERSONAJES
    Texture imgGuerrero;
    Texture imgMago;
    Texture imgCazador;
    Texture imgHechicera;

    // BOTONES
    Rectangle btnGuerrero, btnMago, btnCazador, btnHechicera;

    Rectangle btnVolver;

    Vector3 touchPoint;

    public PantallaSeleccion(MainJuego game, String nombre) {
        this.game = game;
        this.nombreJugador = nombre;

        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 480);

        font = new BitmapFont();
        font.getData().setScale(1.2f);
        layout = new GlyphLayout();
        touchPoint = new Vector3();

        imagenFondo = new Texture("pixelartfondo2.jpeg");

        // CARGAR TEXTURAS DE PERSONAJES
        imgGuerrero = new Texture("guerrero.png");
        imgMago = new Texture("mago.png");
        imgCazador = new Texture("cazador.png");
        imgHechicera = new Texture("hechicera.png");

        // TEXTURA BOTONES
        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.WHITE);
        pixmap.fill();
        texturaBoton = new Texture(pixmap);
        pixmap.dispose();

        // POSICION DE LAS IMAGENES
        btnGuerrero  = new Rectangle(150, 240, 200, 40);
        btnMago      = new Rectangle(450, 240, 200, 40);
        btnCazador   = new Rectangle(150, 100, 200, 40);
        btnHechicera = new Rectangle(450, 100, 200, 40);

        btnVolver = new Rectangle(20, 420, 125, 40);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        game.batch.setProjectionMatrix(camera.combined);

        game.batch.begin();

        // FONDO
        game.batch.setColor(Color.WHITE);
        game.batch.draw(imagenFondo, 0, 0, 800, 480);

        // TITULO
        font.setColor(Color.YELLOW);
        font.getData().setScale(2.0f);
        layout.setText(font, "ELIGE TU PERSONAJE");
        font.draw(game.batch, "ELIGE TU PERSONAJE", (800 - layout.width)/2, 430);
        font.getData().setScale(1.2f);

        // DIBUJAR IMÁGENES DE PERSONAJES
        // Guerrero
        game.batch.draw(imgGuerrero, btnGuerrero.x + (btnGuerrero.width - 64)/2, btnGuerrero.y + 45, 64, 64);
        dibujarBoton(btnGuerrero, "GUERRERO");

        // Mago
        game.batch.draw(imgMago, btnMago.x + (btnMago.width - 64)/2, btnMago.y + 45, 64, 64);
        dibujarBoton(btnMago, "MAGO");

        // Cazador
        game.batch.draw(imgCazador, btnCazador.x + (btnCazador.width - 64)/2, btnCazador.y + 45, 64, 64);
        dibujarBoton(btnCazador, "CAZADOR");

        // Hechicera
        game.batch.draw(imgHechicera, btnHechicera.x + (btnHechicera.width - 64)/2, btnHechicera.y + 45, 64, 64);
        dibujarBoton(btnHechicera, "FATYMAGA");

        // VOLVER DIBUJO
        font.getData().setScale(1.5f);
        dibujarBoton(btnVolver, "< VOLVER");

        game.batch.end();

        // DETECTAR CLICS
        if (Gdx.input.justTouched()) {
            camera.unproject(touchPoint.set(Gdx.input.getX(), Gdx.input.getY(), 0));

            // VOLVER
            if (btnVolver.contains(touchPoint.x, touchPoint.y)) {
                game.setScreen(new PantallaNombre(game, "pixelartfondo2.jpeg"));
                dispose();
            }

            // LLOGICA DE LA SELECCION DE LOS PERSONAJES
            if (btnGuerrero.contains(touchPoint.x, touchPoint.y)) {
                game.setScreen(new PantallaNivel1(game, nombreJugador, 1));
                dispose();
            }
            if (btnMago.contains(touchPoint.x, touchPoint.y)) {
                game.setScreen(new PantallaNivel1(game, nombreJugador, 2));
                dispose();
            }
            if (btnCazador.contains(touchPoint.x, touchPoint.y)) {
                game.setScreen(new PantallaNivel1(game, nombreJugador, 3));
                dispose();
            }
            if (btnHechicera.contains(touchPoint.x, touchPoint.y)) {
                game.setScreen(new PantallaNivel1(game, nombreJugador, 4));
                dispose();
            }
        }
    }

    private void dibujarBoton(Rectangle rect, String texto) {
        game.batch.setColor(Color.DARK_GRAY);
        game.batch.draw(texturaBoton, rect.x, rect.y, rect.width, rect.height);

        game.batch.setColor(Color.WHITE);
        font.setColor(Color.WHITE);

        layout.setText(font, texto);
        float textoX = rect.x + (rect.width - layout.width) / 2;
        float textoY = rect.y + (rect.height + layout.height) / 2;

        font.draw(game.batch, texto, textoX, textoY);
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
        imgGuerrero.dispose();
        imgMago.dispose();
        imgCazador.dispose();
        imgHechicera.dispose();
    }
}
