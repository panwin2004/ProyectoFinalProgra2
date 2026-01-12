package com.juego.pelea.pantallas;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
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
import com.juego.pelea.util.GestorPuntajes;
import com.juego.pelea.util.Puntaje;
import java.util.ArrayList;

public class PantallaPuntajes implements Screen {
    final MainJuego game;

    ArrayList<Puntaje> listaPuntajes;
    OrthographicCamera camera;
    Vector3 touchPoint;
    Texture texturaBoton;
    Texture texturaScroll;
    GlyphLayout layout;
    Rectangle btnVolver;
    Rectangle btnReset;

    float scrollY = 0;
    float alturaTotalLista = 0;

    // DONDE SE VEN LOS PUNTAJES
    Rectangle zonaVisible = new Rectangle(0, 120, 800, 260);

    public PantallaPuntajes(MainJuego game) {
        this.game = game;
        listaPuntajes = GestorPuntajes.obtenerPuntajes();
        actualizarAlturaLista();

        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 480);
        touchPoint = new Vector3();
        layout = new GlyphLayout();

        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.WHITE);
        pixmap.fill();
        texturaBoton = new Texture(pixmap);
        texturaScroll = new Texture(pixmap);
        pixmap.dispose();

        btnVolver = new Rectangle(20, 420, 125, 40);
        btnReset = new Rectangle(300, 50, 200, 50);

        Gdx.input.setInputProcessor(new InputAdapter() {
            @Override
            public boolean scrolled(float amountX, float amountY) {
                scrollY += amountY * 30;
                limitarScroll();
                return true;
            }
        });
    }

    private void actualizarAlturaLista() {
        alturaTotalLista = listaPuntajes.size() * 40;
    }

    private void limitarScroll() {
        if (scrollY < 0) scrollY = 0;
        float maxScroll = alturaTotalLista - zonaVisible.height + 50;
        if (maxScroll < 0) maxScroll = 0;
        if (scrollY > maxScroll) scrollY = maxScroll;
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) { scrollY += 200 * delta; limitarScroll(); }
        if (Gdx.input.isKeyPressed(Input.Keys.UP)) { scrollY -= 200 * delta; limitarScroll(); }

        camera.update();
        game.batch.setProjectionMatrix(camera.combined);

        boolean clicIzquierdo = Gdx.input.isButtonJustPressed(Input.Buttons.LEFT);
        if (clicIzquierdo) {
            camera.unproject(touchPoint.set(Gdx.input.getX(), Gdx.input.getY(), 0));
        }

        game.batch.begin();

        game.font.setColor(Color.YELLOW);
        game.font.getData().setScale(3);
        centrarTexto("MEJORES PUNTAJES", 450);

        game.batch.setColor(Color.DARK_GRAY);
        game.batch.draw(texturaBoton, btnVolver.x, btnVolver.y, btnVolver.width, btnVolver.height);
        game.font.setColor(Color.WHITE);
        game.font.getData().setScale(1.5f);
        centrarTextoEnBoton(btnVolver, "< VOLVER");

        game.batch.setColor(Color.RED);
        game.batch.draw(texturaBoton, btnReset.x, btnReset.y, btnReset.width, btnReset.height);
        game.font.setColor(Color.WHITE);
        centrarTextoEnBoton(btnReset, "BORRAR TODO");

        // LISTAA CON SCROLL Y BOTONES DE BORRAR
        game.batch.flush();
        if (Gdx.graphics.getGL20() != null) {
            Gdx.gl.glEnable(GL20.GL_SCISSOR_TEST);
            float factorX = (float)Gdx.graphics.getWidth() / 800f;
            float factorY = (float)Gdx.graphics.getHeight() / 480f;
            Gdx.gl.glScissor((int)(zonaVisible.x * factorX), (int)(zonaVisible.y * factorY), (int)(zonaVisible.width * factorX), (int)(zonaVisible.height * factorY));
        }

        game.font.setColor(Color.WHITE);
        game.font.getData().setScale(2);

        float yBase = zonaVisible.y + zonaVisible.height - 10 + scrollY;

        if (listaPuntajes.isEmpty()) {
            centrarTexto("No hay puntajes aún.", 300);
        } else {
            for (int i = 0; i < listaPuntajes.size(); i++) {
                Puntaje p = listaPuntajes.get(i);
                String texto = (i + 1) + ". " + p.nombre + " (" + p.clase + ") - " + p.valor;
                float yTexto = yBase - (i * 40);

                if (yTexto > zonaVisible.y - 50 && yTexto < zonaVisible.y + zonaVisible.height + 50) {

                    // DIBUJAR TEXTO
                    centrarTexto(texto, yTexto);

                    // DIBUJAR BOTÓN "X" AL LADO
                    float xBoton = 650;
                    float yBoton = yTexto - 20;
                    float tamBoton = 25;

                    game.batch.setColor(Color.RED);
                    game.batch.draw(texturaBoton, xBoton, yBoton, tamBoton, tamBoton);
                    game.font.getData().setScale(1.2f);
                    game.font.draw(game.batch, "X", xBoton + 7, yBoton + 20);
                    game.font.getData().setScale(2);

                    // LÓGICA DE BORRAR UNO ESPECÍFICO
                    if (clicIzquierdo &&
                        touchPoint.x > xBoton && touchPoint.x < xBoton + tamBoton &&
                        touchPoint.y > yBoton && touchPoint.y < yBoton + tamBoton &&
                        touchPoint.y > zonaVisible.y && touchPoint.y < zonaVisible.y + zonaVisible.height) {

                        // Borrar de la lista en memoria
                        listaPuntajes.remove(i);
                        // Guardar la nueva lista en disco
                        GestorPuntajes.actualizarLista(listaPuntajes);
                        // Recalcular scroll
                        actualizarAlturaLista();

                        break;
                    }
                }
            }
        }

        game.batch.flush();
        Gdx.gl.glDisable(GL20.GL_SCISSOR_TEST);

        // SCROLLBAR
        if (alturaTotalLista > zonaVisible.height) {
            float barraX = 760;
            float barraAncho = 10;
            game.batch.setColor(Color.DARK_GRAY);
            game.batch.draw(texturaScroll, barraX, zonaVisible.y, barraAncho, zonaVisible.height);

            float porcentajeScroll = scrollY / (alturaTotalLista - zonaVisible.height + 50);
            if (porcentajeScroll > 1) porcentajeScroll = 1;

            float altoIndicador = Math.max(30, zonaVisible.height * (zonaVisible.height / alturaTotalLista));
            float yIndicador = (zonaVisible.y + zonaVisible.height - altoIndicador) - (porcentajeScroll * (zonaVisible.height - altoIndicador));

            game.batch.setColor(Color.LIGHT_GRAY);
            game.batch.draw(texturaScroll, barraX, yIndicador, barraAncho, altoIndicador);
        }

        game.batch.end();

        // CLICS DE LOSBOTONES FIJOS
        if (clicIzquierdo) {
            if (btnVolver.contains(touchPoint.x, touchPoint.y)) {
                game.setScreen(new PantallaMenu(game));
                dispose();
            }
            if (btnReset.contains(touchPoint.x, touchPoint.y)) {
                GestorPuntajes.resetearPuntajes();
                listaPuntajes.clear();
                actualizarAlturaLista();
                scrollY = 0;
            }
        }
    }

    private void centrarTexto(String texto, float y) {
        layout.setText(game.font, texto);
        game.font.draw(game.batch, texto, (800 - layout.width)/2, y);
    }

    private void centrarTextoEnBoton(Rectangle rect, String texto) {
        layout.setText(game.font, texto);
        float tx = rect.x + (rect.width - layout.width)/2;
        float ty = rect.y + (rect.height + layout.height)/2;
        game.font.draw(game.batch, texto, tx, ty);
    }

    @Override public void show() {}
    @Override public void resize(int width, int height) {}
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}

    @Override
    public void dispose() {
        texturaBoton.dispose();
        texturaScroll.dispose();
    }
}
