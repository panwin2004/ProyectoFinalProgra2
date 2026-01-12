package com.juego.pelea.pantallas;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;


public class PantallaNombre implements Screen {
    final MainJuego game;
    String nombreIngresado = "";

    OrthographicCamera camera;
    Vector3 touchPoint;
    Texture texturaBoton;
    GlyphLayout layout;
    Rectangle btnVolver;
    Texture imagenFondo;

    public PantallaNombre(MainJuego game, String rutaFondo) {
        this.game = game;

        imagenFondo = new Texture(rutaFondo);
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 480);
        touchPoint = new Vector3();
        layout = new GlyphLayout();

        // VOLVER
        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.WHITE);
        pixmap.fill();
        texturaBoton = new Texture(pixmap);
        pixmap.dispose();
        btnVolver = new Rectangle(20, 420, 125, 40);


        Gdx.input.setInputProcessor(new InputAdapter() {
            @Override
            public boolean keyTyped(char character) {
                if (character == '\b' && nombreIngresado.length() > 0) {
                    nombreIngresado = nombreIngresado.substring(0, nombreIngresado.length() - 1);
                }
                else if (Character.isLetterOrDigit(character) || character == ' ') {
                    if (nombreIngresado.length() < 12) {
                        nombreIngresado += character;
                    }
                }
                else if (character == '\r' || character == '\n') {
                    confirmarNombre();
                }
                return true;
            }
        });
    }

    private void confirmarNombre() {
        if (!nombreIngresado.isEmpty()) {
            game.setScreen(new PantallaSeleccion(game, nombreIngresado));
            dispose();
        }
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        game.batch.setProjectionMatrix(camera.combined);

        game.batch.begin();

        // DIBUJAR EL FONDO PRIMERO
        game.batch.setColor(Color.WHITE);
        game.batch.draw(imagenFondo, 0, 0, 800, 480);

        // TEXTO INSTRUCCIONES
        game.font.setColor(Color.CYAN);
        game.font.getData().setScale(2);
        layout.setText(game.font, "ESCRIBE TU NOMBRE:");
        game.font.draw(game.batch, "ESCRIBE TU NOMBRE:", (800 - layout.width)/2, 350);

        // NOMBRE INGRESADO
        game.font.setColor(Color.WHITE);
        game.font.getData().setScale(3);
        layout.setText(game.font, nombreIngresado + "_");
        game.font.draw(game.batch, nombreIngresado + "_", (800 - layout.width)/2, 250);

        game.font.setColor(Color.YELLOW);
        game.font.getData().setScale(1.5f);
        game.font.draw(game.batch, "Presiona [ENTER] para continuar", 250, 150);

        // BOTON VOLVER
        game.batch.setColor(Color.DARK_GRAY);
        game.batch.draw(texturaBoton, btnVolver.x, btnVolver.y, btnVolver.width, btnVolver.height);

        game.font.getData().setScale(1.5f);
        game.font.setColor(Color.WHITE);
        layout.setText(game.font, "< VOLVER");
        float tx = btnVolver.x + (btnVolver.width - layout.width)/2;
        float ty = btnVolver.y + (btnVolver.height + layout.height)/2;
        game.font.draw(game.batch, "< VOLVER", tx, ty);

        game.batch.end();

        // LOGICA VOLVER
        if (Gdx.input.justTouched()) {
            camera.unproject(touchPoint.set(Gdx.input.getX(), Gdx.input.getY(), 0));
            if (btnVolver.contains(touchPoint.x, touchPoint.y)) {
                game.setScreen(new PantallaMenu(game));
                dispose();
            }
        }
    }

    @Override public void show() {}
    @Override public void resize(int width, int height) {}
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}

    @Override
    public void dispose() {
        texturaBoton.dispose();
        imagenFondo.dispose();
    }
}
