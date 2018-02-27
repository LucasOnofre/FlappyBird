package com.flappybird;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.awt.Shape;
import java.util.Random;

public class FlappyBird extends ApplicationAdapter {

    private Texture     fundo;
    private Texture     canoTopo;
    private Texture     canoBaixo;
    private Texture     gameOver;
    private Texture[]   passaros;

    private Random      numeroRandomico;

    //  private ShapeRenderer   shape;
     private Circle      passaroCirculo;
     private Rectangle   retanguloCanoTopo;
     private Rectangle   retanguloCanoBaixo;

    private BitmapFont      fonte;
    private BitmapFont      mensagem;

    private SpriteBatch     batch;

    //Atributos de configuracao
    private int     pontuacao = 0;
    private int     estadoJogo = 0;  // 0 jogo nao iniciado, 1 jogo iniciado, 2 jogo game over


    private float   deltaTime;
    private float   variacao = 0;

    private float   espacoEntreCanos;
    private float   velocidadeQueda = 0;
    private float   larguraDispositivo;
    private float   posicaoInicialVertical;
    private float   alturaDispositivo;
    private float   alturaEntreCanosRandomica;
    private float   posicaoMovimentoCanoHorizontal;

    private boolean marcouPonto = false;

    private OrthographicCamera camera;
    private Viewport viewport;

    private final float VIRTUAL_WIDTH   = 768;
    private final float VIRTUAL_HEIGHT  = 1024;


    @Override
    public void create () {

        batch = new SpriteBatch();

        //INICIALIZANDO AS FORMAS
        numeroRandomico     = new Random();
        passaroCirculo      = new Circle();
        retanguloCanoTopo   = new Rectangle();
        retanguloCanoBaixo  = new Rectangle();

       // shape = new ShapeRenderer();

        //configurando a mensagem
        mensagem = new BitmapFont();
        mensagem.setColor(Color.WHITE);
        mensagem.getData().setScale(5);

        //CONFIGURANDO E INICIALIZANDO A FONTE
        fonte = new BitmapFont();
        fonte.setColor(Color.WHITE);
        fonte.getData().setScale(7);

        passaros    = new Texture[3];
        passaros[0] = new Texture("passaro1.png");
        passaros[1] = new Texture("passaro2.png");
        passaros[2] = new Texture("passaro3.png");

        fundo       = new Texture("fundo.png");
        canoTopo    = new Texture("cano_topo_maior.png");
        canoBaixo   = new Texture("cano_baixo_maior.png");

        gameOver    = new Texture("game_over.png");

        larguraDispositivo      = VIRTUAL_WIDTH;
        alturaDispositivo       = VIRTUAL_HEIGHT;
        posicaoInicialVertical  = alturaDispositivo / 2;

        posicaoMovimentoCanoHorizontal = larguraDispositivo;
        espacoEntreCanos = 240;

        //Configurando camera
        camera      = new OrthographicCamera();
        camera.position.set(VIRTUAL_WIDTH / 2,VIRTUAL_HEIGHT / 2,0);
        viewport    = new StretchViewport(VIRTUAL_WIDTH,VIRTUAL_HEIGHT,camera);

    }

    @Override
    public void render () {

        camera.update();

        //limpar frames anteriores

       Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        deltaTime = Gdx.graphics.getDeltaTime();
        variacao += deltaTime * 10;

        if (variacao > 2) {
            variacao = 0;
        }

        if( estadoJogo == 0 ){

            if( Gdx.input.justTouched() ){
                estadoJogo = 1;
            }

            //Estadojogo = 1
        }else {
            velocidadeQueda++;

            if (posicaoInicialVertical > 0 || velocidadeQueda < 0)
                posicaoInicialVertical = posicaoInicialVertical - velocidadeQueda;

            if (estadoJogo == 1) {

                posicaoMovimentoCanoHorizontal -= deltaTime * 550;

                if (Gdx.input.justTouched()) {
                    velocidadeQueda = -15;
                }


                //Verifica se o cano saiu inteiramente da tela
                if (posicaoMovimentoCanoHorizontal < -canoTopo.getWidth()) {
                    posicaoMovimentoCanoHorizontal = larguraDispositivo;
                    alturaEntreCanosRandomica = numeroRandomico.nextInt(600) - 300;
                    marcouPonto = false;
                }

                //Verifica pontuação
                if (posicaoMovimentoCanoHorizontal < 120) {
                    if (!marcouPonto) {
                        pontuacao++;
                        marcouPonto = true;
                    }
                }
            }else{ //Estado 2

                if (Gdx.input.justTouched()){

                    estadoJogo                      = 0;
                    pontuacao                       = 0;
                    velocidadeQueda                 = 0;
                    posicaoMovimentoCanoHorizontal  = larguraDispositivo;
                    posicaoInicialVertical          = alturaDispositivo / 2;
                }
            }
        }

        //configuração da camera
        batch.setProjectionMatrix(camera.combined);

        batch.begin();

        batch.draw(fundo, 0, 0, larguraDispositivo, alturaDispositivo);

        //canos e passaros
        batch.draw(canoTopo, posicaoMovimentoCanoHorizontal, alturaDispositivo / 2 + espacoEntreCanos / 2 + alturaEntreCanosRandomica);
        batch.draw(canoBaixo, posicaoMovimentoCanoHorizontal, alturaDispositivo / 2 - canoBaixo.getHeight() - espacoEntreCanos / 2 + alturaEntreCanosRandomica);
        batch.draw(passaros[(int) variacao], 120, posicaoInicialVertical);

        //fonte
        fonte.draw(batch, String.valueOf(pontuacao), larguraDispositivo / 2, alturaDispositivo - 50);

        if (estadoJogo == 2){
            batch.draw(gameOver,larguraDispositivo/2 - gameOver.getWidth()/2, alturaDispositivo/2);
            mensagem.draw(batch,"Toque para reiniciar",larguraDispositivo/2 - 310,alturaDispositivo/2 - gameOver.getHeight()/2 );

        }

        batch.end();

        passaroCirculo.set(120 + passaros[0].getWidth() / 2, posicaoInicialVertical + passaros[0].getHeight() / 2, passaros[0].getWidth() / 2);

        retanguloCanoBaixo = new Rectangle(
                posicaoMovimentoCanoHorizontal,
                    alturaDispositivo / 2 - canoBaixo.getHeight() - espacoEntreCanos / 2 + alturaEntreCanosRandomica,
                    canoBaixo.getWidth(),
                    canoBaixo.getHeight());

        retanguloCanoTopo = new Rectangle(
                posicaoMovimentoCanoHorizontal,
                alturaDispositivo / 2 + espacoEntreCanos / 2 + alturaEntreCanosRandomica,
                canoTopo.getWidth(),
                canoTopo.getHeight());

        //Desenhar formas
     /*   shape.begin( ShapeRenderer.ShapeType.Filled);
        shape.circle(passaroCirculo.x, passaroCirculo.y, passaroCirculo.radius);
        shape.rect(retanguloCanoBaixo.x,retanguloCanoBaixo.y,retanguloCanoBaixo.width,retanguloCanoBaixo.height);
        shape.rect(retanguloCanoTopo.x,retanguloCanoTopo.y,retanguloCanoTopo.width,retanguloCanoTopo.height);
        shape.setColor(Color.RED);
        shape.end(); */


        //Teste de colisões

        if(Intersector.overlaps(passaroCirculo,retanguloCanoBaixo) ||Intersector.overlaps(passaroCirculo,retanguloCanoTopo)
                || posicaoInicialVertical <= 0 || posicaoInicialVertical >= alturaDispositivo){
           estadoJogo = 2;
        }
    }

    //é chamado sempre que a resolução é alterada
    @Override
    public void resize(int width, int height) {
        viewport.update(width,height);
    }
}
