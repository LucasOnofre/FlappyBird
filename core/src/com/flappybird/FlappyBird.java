package com.flappybird;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Rectangle;

import java.awt.Shape;
import java.util.Random;

public class FlappyBird extends ApplicationAdapter {

    //CLASSE USADA PARA ANIMAÇÕES

    private SpriteBatch batch;

    private Boolean marcou = false;

    private Texture fundo;
    private Texture canoTop;
    private Texture canoBaixo;
    private Texture[] passaros;

    private int pontuacao = 0;
    private int estadoJogo = 0;
    private int alturaDispositivo;
    private int larguraDispositivo;

    private float deltaTime;
    private float espacoCanos;
    private float variacao = 0;
    private float alturaEntreCanosRandomica;
    private float velocidadeQueda = 0;
    private float posicaoInicialVertical;
    private float posicaoMovimentoCanoHorizontal;

    private Random numeroRandomico;

    private BitmapFont fonte;

    private Circle  passaroCircle;

    private Rectangle   recCanoTopo;
    private Rectangle   recCanoBottom;

    private ShapeRenderer shapeRenderer;


    @Override
    public void create() {


        //VARIAVEIS QUE PEGAM A LARGURA E ALTURA DA TELA

        larguraDispositivo  = Gdx.graphics.getWidth();
        alturaDispositivo   = Gdx.graphics.getHeight();

        //VARIAVEL QUE PEGA O MEIO DA TELA, ONDE IRÁ INICIAR O PASSARO

        posicaoInicialVertical = alturaDispositivo / 2;

        //INICIALIZAÇÃO DA FONTE E CONFIGURAÇÃO DA FONTE

        fonte =  new BitmapFont();
        fonte.setColor(Color.WHITE);
        fonte.getData().setScale(7);

        //FORMAS PARA COLISÃO

        passaroCircle      = new Circle();
        recCanoTopo        = new Rectangle();
        recCanoBottom      = new Rectangle();





        //INICIALIZAÇÃO DAS CLASSES DA BIBLIOTECA

        batch = new SpriteBatch();

        //INICIALIZAÇÃO DOS CANOS

        fundo       = new Texture("fundo.png");
        canoTop     = new Texture("cano_topo_maior.png");
        canoBaixo   = new Texture("cano_baixo_maior.png");


        //NUMERO RANDOMICO
        numeroRandomico = new Random();

        //INICIALIZAÇÃO DA VARIAVEL PASSARO  QUE USA 3 IMAGENS PARA FAZER A IMPRESSÃO DE BATER DE ASAS

        passaros = new Texture[3];
        passaros[0] = new Texture("passaro1.png");
        passaros[1] = new Texture("passaro2.png");
        passaros[2] = new Texture("passaro3.png");

        espacoCanos = 250;
        posicaoMovimentoCanoHorizontal = larguraDispositivo - 100;
    }

    @Override
    public void render() {

        //RECUPERA O DELTA TIME

        deltaTime = Gdx.graphics.getDeltaTime();
        variacao += deltaTime * 9;

        //CONDIÇÃO PARA O BATER DE ASAS DO PASSARO SER INFINITO

        if (variacao > 2) {

            variacao = 0;
        }
        //VERIFICA SE O JOGO INICIOU

        if (estadoJogo == 0) {
            if (Gdx.input.justTouched()) {
                estadoJogo = 1;
            }
        } else {

            posicaoMovimentoCanoHorizontal -= deltaTime * 400;

            velocidadeQueda++;

            variacao += deltaTime * 9;

            //FAZ O CANO REPETIR AO PASSAR NA TELA
            if (posicaoMovimentoCanoHorizontal < -canoTop.getWidth()) {
                posicaoMovimentoCanoHorizontal = larguraDispositivo;
                alturaEntreCanosRandomica = numeroRandomico.nextInt(550) - 200;
                marcou = false;

            }

            //CONDIÇÃO QUE FAZ O PASSARO CAIR, TIRANDO DA POSICAO INICIAL

            if (posicaoInicialVertical > 0 || velocidadeQueda < 0) {

                posicaoInicialVertical -= velocidadeQueda;
            }

            //CONDIÇÃO PARA PONTUAÇÃO

            if (posicaoMovimentoCanoHorizontal < 120){
                if (!marcou){
                    pontuacao++;
                    marcou = true;
                }

            }


            //CONDIÇÃO QUE PEGA QUANDO A TELA É TOCADA E DECRSCENTA  DA VELOCIDADE DE QUEDA
            //PARA QUE ELA DIMINUA E DE A IMPRESSÃO DE QUE O PASSARO SUBIU DE NOVO

            if (Gdx.input.justTouched()) {

                velocidadeQueda = -17;
            }

            //COMEÇANDO A BIBLIOTECA E A ANIMAÇÃO
        }


        batch.begin();

        //DESENHANDO O FUNDO DO APP, PEGANDO ALTURA E LARGURA DA TELA E ETC

        batch.draw(fundo, 0, 0, larguraDispositivo, alturaDispositivo);

        //DESENHANDO O CANOS

        batch.draw(canoTop, posicaoMovimentoCanoHorizontal, alturaDispositivo / 2 + espacoCanos / 2 + alturaEntreCanosRandomica);
        batch.draw(canoBaixo, posicaoMovimentoCanoHorizontal, alturaDispositivo / 2 - canoBaixo.getHeight() - espacoCanos / 2 + alturaEntreCanosRandomica);

        //DESENHANDO O PASSARO, PEGANDO A POSICAO DO MESMO E USANDO A VARIAÇÃO PARA TER O BATER DE ASAS

        batch.draw(passaros[(int) variacao], 120, posicaoInicialVertical);

        //DESENHANDO A FONTE
        fonte.draw(batch,String.valueOf(pontuacao),larguraDispositivo/2,alturaDispositivo -50);

        //Desenhando formas
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.end();

        //FINALIZANDO A ANIMAÇÃO

        batch.end();
    }
}

