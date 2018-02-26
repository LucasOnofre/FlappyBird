package com.flappybird;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Rectangle;

import java.awt.Shape;
import java.util.Random;

public class FlappyBird extends ApplicationAdapter {

    private Texture     fundo;
    private Texture     canoTopo;
    private Texture     canoBaixo;
    private Texture[]   passaros;

    private Random      numeroRandomico;

    private Circle      passaroCirculo;
    private Rectangle   retanguloCanoTopo;
    private Rectangle   retanguloCanoBaixo;

    private BitmapFont      fonte;
    private SpriteBatch     batch;
    private ShapeRenderer   shape;


    //Atributos de configuracao
    private int     pontuacao = 0;
    private int     estadoJogo = 0;
    private int     alturaDispositivo;
    private int     larguraDispositivo;

    private float   deltaTime;
    private float   variacao = 0;
    private float   espacoEntreCanos;
    private float   velocidadeQueda = 0;
    private float   posicaoInicialVertical;
    private float   alturaEntreCanosRandomica;
    private float   posicaoMovimentoCanoHorizontal;

    private boolean marcouPonto = false;


    @Override
    public void create () {

        batch = new SpriteBatch();

        //INICIALIZANDO AS FORMAS
        numeroRandomico     = new Random();
        passaroCirculo      = new Circle();
        retanguloCanoTopo   = new Rectangle();
        retanguloCanoBaixo  = new Rectangle();

        shape = new ShapeRenderer();

        //CONFIGURANDO E INICIALIZANDO A FONTE
        fonte = new BitmapFont();
        fonte.setColor(Color.WHITE);
        fonte.getData().setScale(7);

        passaros    = new Texture[3];
        passaros[0] = new Texture("passaro1.png");
        passaros[1] = new Texture("passaro2.png");
        passaros[2] = new Texture("passaro3.png");

        fundo       = new Texture("fundo.png");
        canoBaixo   = new Texture("cano_baixo_maior.png");
        canoTopo    = new Texture("cano_topo_maior.png");

        larguraDispositivo      = Gdx.graphics.getWidth();
        alturaDispositivo       = Gdx.graphics.getHeight();
        posicaoInicialVertical  = alturaDispositivo / 2;

        posicaoMovimentoCanoHorizontal = larguraDispositivo;
        espacoEntreCanos = 300;

    }

    @Override
    public void render () {

        deltaTime = Gdx.graphics.getDeltaTime();
        variacao += deltaTime * 10;

        if (variacao > 2) variacao = 0;

        if( estadoJogo == 0 ){//Não iniciado

            if( Gdx.input.justTouched() ){
                estadoJogo = 1;
            }

        }else {

            posicaoMovimentoCanoHorizontal -= deltaTime * 450;
            velocidadeQueda++;

            if (Gdx.input.justTouched()) {
                velocidadeQueda = -15;
            }

            if (posicaoInicialVertical > 0 || velocidadeQueda < 0)
                posicaoInicialVertical = posicaoInicialVertical - velocidadeQueda;

            //Verifica se o cano saiu inteiramente da tela
            if (posicaoMovimentoCanoHorizontal < -canoTopo.getWidth()) {
                posicaoMovimentoCanoHorizontal  = larguraDispositivo;
                alturaEntreCanosRandomica       = numeroRandomico.nextInt(400) - 200;
                marcouPonto = false;
            }

            //Verifica pontuação
            if(posicaoMovimentoCanoHorizontal < 120 ){
                if( !marcouPonto ){
                    pontuacao++;
                    marcouPonto = true;
                }
            }
        }

        batch.begin();

        batch.draw(fundo, 0, 0, larguraDispositivo, alturaDispositivo);

        //canos e passaros
        batch.draw(canoTopo, posicaoMovimentoCanoHorizontal, alturaDispositivo / 2 + espacoEntreCanos / 2 + alturaEntreCanosRandomica);
        batch.draw(canoBaixo, posicaoMovimentoCanoHorizontal, alturaDispositivo / 2 - canoBaixo.getHeight() - espacoEntreCanos / 2 + alturaEntreCanosRandomica);
        batch.draw(passaros[(int) variacao], 120, posicaoInicialVertical);

        //fonte
        fonte.draw(batch, String.valueOf(pontuacao), larguraDispositivo / 2, alturaDispositivo - 50);

        batch.end();

        passaroCirculo.set(120 + passaros[0].getWidth() / 2, posicaoInicialVertical + passaros[0].getHeight() / 2, passaros[0].getWidth() / 2);
        retanguloCanoBaixo = new Rectangle();
        //Desenhar formas
        shape.begin( ShapeRenderer.ShapeType.Filled);
        shape.circle(passaroCirculo.x, passaroCirculo.y, passaroCirculo.radius);
        shape.setColor(Color.RED);
        shape.end();
    }
}
