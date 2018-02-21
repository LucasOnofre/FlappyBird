package com.flappybird;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class FlappyBird extends ApplicationAdapter {

	//CLASSE USADA PARA ANIMAÇÕES
	private SpriteBatch batch;
	private Texture		fundo;
	private Texture []     passaros;
	private int 		alturaDispositivo;
	private int 		larguraDispositivo;
	private float		variacao = 0;
	private float 		velocidadeQueda = 0;
	private float		posicaoInicialVertical;

	
	@Override
	public void create () {

		larguraDispositivo 		= Gdx.graphics.getWidth();
		alturaDispositivo 		= Gdx.graphics.getHeight();
		posicaoInicialVertical	= alturaDispositivo / 2;

		batch 	= new SpriteBatch();
		fundo	= new Texture("fundo.png");

		passaros    = new Texture[3];
		passaros[0] =  new Texture("passaro1.png");
		passaros[1] =  new Texture("passaro2.png");
		passaros[2] =  new Texture("passaro3.png");

	}

		@Override
		public void render () {

		velocidadeQueda++;
		variacao += Gdx.graphics.getDeltaTime() * 9;

		if (posicaoInicialVertical > 0) {

			posicaoInicialVertical -= velocidadeQueda;
		}
		
		if (variacao > 2){

			variacao = 0;
		}


		batch.begin();

		batch.draw(fundo,0,0,larguraDispositivo,alturaDispositivo);

		batch.draw(passaros[(int) variacao],30,posicaoInicialVertical );

		batch.end();
		}
	}

