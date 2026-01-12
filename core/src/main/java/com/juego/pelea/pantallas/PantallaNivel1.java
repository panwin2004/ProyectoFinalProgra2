package com.juego.pelea.pantallas;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;

import com.juego.pelea.entidades.*;
import com.juego.pelea.util.GestorNivelUtilitario;
import com.juego.pelea.util.HUD;
import com.juego.pelea.util.GestorPuntajes;

public class PantallaNivel1 implements Screen {

    final MainJuego game;

    String nombreJugador;
    OrthographicCamera camera;
    GestorNivelUtilitario gestor;
    HUD hud;
    Texture imgHeroe;
    Texture imgMonstruo;
    Texture imagenFondo;

    float tiempoRestante = 100f;
    boolean enCuentaRegresiva = true;
    float tiempoCuentaRegresiva = 3.9f;
    float tiempoDesdeUltimoAtaque = 0;
    boolean estaPausado = false;
    boolean scoreGuardado = false;

    public PantallaNivel1(MainJuego game, String nombreRecibido, int idPersonaje) {
        this.game = game;
        this.nombreJugador = nombreRecibido;

        // CONFIGURAR CÁMARA (MUNDO DE 800x480)
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 480);

        imagenFondo = new Texture("fondoluna.jpg");
        imgMonstruo = new Texture("monstruo3.png");

        switch (idPersonaje) {
            case 1: imgHeroe = new Texture("guerrero.png"); gestor = new GestorNivelUtilitario(new Guerrero(50, 500, imgHeroe), new Monstruo(400, 500, imgMonstruo)); break;
            case 2: imgHeroe = new Texture("mago.png"); gestor = new GestorNivelUtilitario(new Mago(50, 500, imgHeroe), new Monstruo(400, 500, imgMonstruo)); break;
            case 3: imgHeroe = new Texture("cazador.png"); gestor = new GestorNivelUtilitario(new Cazador(50, 500, imgHeroe), new Monstruo(400, 500, imgMonstruo)); break;
            case 4: imgHeroe = new Texture("hechicera.png"); gestor = new GestorNivelUtilitario(new Hechicera(50, 500, imgHeroe), new Monstruo(400, 500, imgMonstruo)); break;
            default: imgHeroe = new Texture("guerrero.png"); gestor = new GestorNivelUtilitario(new Guerrero(50, 500, imgHeroe), new Monstruo(400, 500, imgMonstruo)); break;
        }

        hud = new HUD(gestor);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // ACTUALIZAR CÁMARA
        camera.update();
        game.batch.setProjectionMatrix(camera.combined);

        // LÓGICA DE TECLA PAUSA
        if (Gdx.input.isKeyJustPressed(Input.Keys.P)) {
            estaPausado = !estaPausado;
        }

        // LÓGICA DEL JUEGO
        if (!estaPausado) {
            // JUEGO CORRIENDO
            if (enCuentaRegresiva) {
                tiempoCuentaRegresiva -= delta;
                if (tiempoCuentaRegresiva <= 0) enCuentaRegresiva = false;
            }
            else if (!gestor.juegoTerminado) {
                tiempoRestante -= delta;
                tiempoDesdeUltimoAtaque += delta;
                if (tiempoRestante <= 0) { tiempoRestante = 0; gestor.juegoTerminado = true; }

                if (gestor.jugador.vida > 0) {
                    gestor.jugador.actualizar(delta);
                    gestor.jugador.actualizarEstado(delta);
                }
                if (gestor.enemigo.vida > 0) {
                    gestor.enemigo.actualizar(delta);
                    gestor.enemigo.actualizarEstado(delta);
                }

                gestor.actualizarLogica(delta, tiempoRestante);

                if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE)) {
                    if (tiempoDesdeUltimoAtaque > gestor.jugador.getCooldownAtaque()) {
                        tiempoDesdeUltimoAtaque = 0;
                        gestor.realizarAtaqueJugador(tiempoRestante);
                    }
                }
            }
            else {
                // FIN DEL JUEGO
                if (gestor.victoria && !scoreGuardado) {
                    String clase = gestor.jugador.getClass().getSimpleName();
                    GestorPuntajes.guardarPuntaje(nombreJugador, clase, gestor.puntaje);
                    scoreGuardado = true;
                }
                if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
                    game.setScreen(new PantallaMenu(game));
                    dispose();
                }
            }
        } else {
            // PAUSA
            // Si está pausado y presionan Escape, volvemos al menú no olvidarme
            if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
                game.setScreen(new PantallaMenu(game));
                dispose();
            }
        }

        // DIBUJADO
        game.batch.begin();

        game.batch.setColor(Color.WHITE);
        game.batch.draw(imagenFondo, 0, 0, 800, 480);

        if (gestor.jugador.vida > 0) gestor.jugador.dibujar(game.batch);
        if (gestor.enemigo.vida > 0) gestor.enemigo.dibujar(game.batch);
        gestor.dibujarElementos(game.batch);

        hud.dibujar(game.batch, tiempoRestante, tiempoCuentaRegresiva, enCuentaRegresiva, nombreJugador,tiempoDesdeUltimoAtaque);

        // DUBUJO PAUSA
        if (estaPausado) {
            game.font.setColor(Color.YELLOW);
            game.font.getData().setScale(3);
            game.font.draw(game.batch, "PAUSA", 350, 260);

            // SALIR INSTTRUCCIONES
            game.font.setColor(Color.YELLOW);
            game.font.getData().setScale(1.5f);
            game.font.draw(game.batch, "Presiona [P] para continuar \nPresiona [ESC] para Menu", 280, 200);

            // RESET
            game.font.getData().setScale(1);
            game.font.setColor(Color.WHITE);
        }

        game.batch.end();
    }

    @Override public void resize(int width, int height) {}
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}
    @Override public void show() {}

    @Override
    public void dispose() {
        imgHeroe.dispose();
        imgMonstruo.dispose();
        imagenFondo.dispose();
        gestor.dispose();
        hud.dispose();
    }
}
