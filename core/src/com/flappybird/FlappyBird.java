package com.flappybird;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class FlappyBird extends ApplicationAdapter {

	//CLASSE USADA PARA ANIMAÇÕES

	private SpriteBatch		batch;

	private Texture			fundo;
	private Texture			canoTop;
	private Texture []     	passaros;
	private Texture			canoBaixo;

	private int 			alturaDispositivo;
	private int 			larguraDispositivo;

	private float			espacoCanos;
	private float			deltaTime;
	private float			variacao = 0;
	private float 			velocidadeQueda = 0;
	private float			posicaoInicialVertical;
	private float			posicaoMovimentoCanoHorizontal;

	
	@Override
	public void create () {


		//VARIAVEIS QUE PEGAM A LARGURA E ALTURA DA TELA

		larguraDispositivo 		= Gdx.graphics.getWidth();
		alturaDispositivo 		= Gdx.graphics.getHeight();

		//VARIAVEL QUE PEGA O MEIO DA TELA, ONDE IRÁ INICIAR O PASSARO

		posicaoInicialVertical	= alturaDispositivo / 2;

		//INICIALIZAÇÃO DAS CLASSES DA BIBLIOTECA

		batch 	= new SpriteBatch();

		//INICIALIZAÇÃO DOS CANOS

		fundo	  = new Texture("fundo.png");
		canoTop	  = new Texture("cano_topo_maior.png");
		canoBaixo = new Texture("cano_baixo_maior.png");

		//INICIALIZAÇÃO DA VARIAVEL PASSARO  QUE USA 3 IMAGENS PARA FAZER A IMPRESSÃO DE BATER DE ASAS

		passaros    =  new Texture[3];
		passaros[0] =  new Texture("passaro1.png");
		passaros[1] =  new Texture("passaro2.png");
		passaros[2] =  new Texture("passaro3.png");

		espacoCanos = 300;
		posicaoMovimentoCanoHorizontal = larguraDispositivo -100;
	}

		@Override
		public void render () {
			//RECUPERA O DELTA TIME
			deltaTime = Gdx.graphics.getDeltaTime();

		posicaoMovimentoCanoHorizontal -= deltaTime * 400;

		velocidadeQueda++;

		variacao += deltaTime * 9;

		//FAZ O CANO REPETIR AO PASSAR NA TELA
			if (posicaoMovimentoCanoHorizontal < -canoTop.getWidth()) {
				posicaoMovimentoCanoHorizontal = larguraDispositivo;
			}

		//CONDIÇÃO QUE FAZ O PASSARO CAIR, TIRANDO DA POSICAO INICIAL

		if (posicaoInicialVertical > 0 || velocidadeQueda < 0){

			posicaoInicialVertical -= velocidadeQueda;
		}

		//CONDIÇÃO PARA O BATER DE ASAS DO PASSARO SER INFINITO

		if (variacao > 2){

			variacao = 0;
		}

		//CONDIÇÃO QUE PEGA QUANDO A TELA É TOCADA E DECRSCENTA  DA VELOCIDADE DE QUEDA
			//PARA QUE ELA DIMINUA E DE A IMPRESSÃO DE QUE O PASSARO SUBIU DE NOVO

		if (Gdx.input.justTouched()){

			velocidadeQueda = -17;
		}

		//COMEÇANDO A BIBLIOTECA E A ANIMAÇÃO

		batch.begin();

		//DESENHANDO O FUNDO DO APP, PEGANDO ALTURA E LARGURA DA TELA E ETC

		batch.draw(fundo,0,0,larguraDispositivo,alturaDispositivo);

		//DESENHANDO O CANOS

			batch.draw(canoTop,posicaoMovimentoCanoHorizontal,alturaDispositivo/2 + espacoCanos/2 );
			batch.draw(canoBaixo,posicaoMovimentoCanoHorizontal,alturaDispositivo/2 - canoBaixo.getHeight() - espacoCanos/2 );

		//DESENHANDO O PASSARO, PEGANDO A POSICAO DO MESMO E USANDO A VARIAÇÃO PARA TER O BATER DE ASAS

		batch.draw(passaros[(int) variacao],30,posicaoInicialVertical );

		//FINALIZANDO A ANIMAÇÃO

		batch.end();
		}
	}

