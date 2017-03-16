package com.cursoandroid.flappybird;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;


import java.awt.Color;
import java.util.Random;

public class FlappyBird extends ApplicationAdapter {

    private SpriteBatch batch;
    private Texture[] passaro;
    private Texture fundo;
    private Texture canoBaixo;
    private Texture canoAlto;
    private Texture gameOver;
    private Random numeroRandomico;
    private BitmapFont fonte;
    private BitmapFont mensagem;
    private Circle passaroCirculo;
    private Rectangle canoTopoRetangulo;
    private Rectangle canoBaixoRetangulo;
    private ShapeRenderer shapeRenderer;


    //atributos de configuração
    private int movimento=0;
    private float largura;
    private float altura;
    private int estadoJogo = 0;
    private int pontuacao=0;

    private float variacao= 0;
    private float velocidadeQueda =0;
    private float posicaoInicialVertical;
    private float posicaoMovimentoCanoHorizontal ;
    private float espacoEntreCanos;
    private float deltaTime;
    private float alturaEntreCanosRandomica;
    private boolean marcouPonto=false;

    //camera
    private OrthographicCamera camera;
    private Viewport viewport;
    private final float VIRTUAL_WIDTH=768;
    private final float VIRTUAL_HEIGHT=1024;


	@Override
	public void create () {

        batch = new SpriteBatch();
        numeroRandomico = new Random();
        passaroCirculo = new Circle();
        //canoBaixoRetangulo= new Rectangle();
        //canoTopoRetangulo = new Rectangle();
        //shapeRenderer = new ShapeRenderer();
        fonte = new BitmapFont();
        fonte.setColor(com.badlogic.gdx.graphics.Color.WHITE);
        fonte.getData().setScale(6);

        mensagem = new BitmapFont();
        mensagem.setColor(com.badlogic.gdx.graphics.Color.WHITE);
        mensagem.getData().setScale(3);

        passaro = new Texture[3];
        passaro[0] = new Texture("passaro1.png");
        passaro[1] = new Texture("passaro2.png");
        passaro[2]= new Texture("passaro3.png");
        fundo = new Texture("fundo.png");
        canoBaixo = new Texture("cano_baixo.png");
        canoAlto = new Texture("cano_topo.png");
        gameOver = new Texture("game_over.png");

        //configuração de camera
        camera =new OrthographicCamera();
        camera.position.set(VIRTUAL_WIDTH/2, VIRTUAL_HEIGHT/2, 0);
        viewport = new StretchViewport(VIRTUAL_WIDTH, VIRTUAL_HEIGHT,camera);

        largura= VIRTUAL_WIDTH;
        altura = VIRTUAL_HEIGHT;

        posicaoInicialVertical = altura/2;
        posicaoMovimentoCanoHorizontal=largura;
        espacoEntreCanos=300;

	}

	@Override
	public void render () {

        camera.update();

        //limpar frames anteriores
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        deltaTime = Gdx.graphics.getDeltaTime();
        variacao += deltaTime * 10;
        if (variacao>2) variacao=0;

        if (estadoJogo == 0){
            if (Gdx.input.justTouched()){
                estadoJogo=1;
            }
        }else {

            velocidadeQueda++;

            if (posicaoInicialVertical>0 || velocidadeQueda<0) posicaoInicialVertical-=velocidadeQueda;

            if (estadoJogo == 1){
                posicaoMovimentoCanoHorizontal -= deltaTime * 200;

                if (Gdx.input.justTouched()){
                    velocidadeQueda=-15;
                }

                if (posicaoMovimentoCanoHorizontal < -canoAlto.getWidth()){
                    posicaoMovimentoCanoHorizontal = largura;
                    alturaEntreCanosRandomica = numeroRandomico.nextInt(400)-200;
                    marcouPonto=false;
                }

                if (posicaoMovimentoCanoHorizontal<120){
                    if (!marcouPonto){
                        pontuacao++;
                        marcouPonto=true;
                    }

                }
            }else {//tela de game over

             if (Gdx.input.justTouched()){
                 estadoJogo=0;
                 pontuacao=0;
                 velocidadeQueda=0;
                 posicaoInicialVertical = altura/2;
                 posicaoMovimentoCanoHorizontal = largura;
             }

            }
        }

        //configurar dados de projeção de câmera
        batch.setProjectionMatrix(camera.combined);

        batch.begin();

        batch.draw(fundo, 0, 0, largura, altura);
        batch.draw(canoAlto, posicaoMovimentoCanoHorizontal, altura/2 + espacoEntreCanos/2 + alturaEntreCanosRandomica);
        batch.draw(canoBaixo, posicaoMovimentoCanoHorizontal, altura/2 - canoBaixo.getHeight() - espacoEntreCanos/2 + alturaEntreCanosRandomica);
        batch.draw(passaro[(int)variacao], 120, posicaoInicialVertical );
        fonte.draw(batch, String.valueOf(pontuacao), largura/2, altura - 50);

        if (estadoJogo==2){
            batch.draw(gameOver, largura/2 - gameOver.getWidth()/2, altura/2);
            mensagem.draw(batch, "Toque para reiniciar!", largura/2 - 200, altura/2 - gameOver.getHeight()/2);
        }

        batch.end();

        passaroCirculo.set(120 + passaro[0].getWidth()/2, posicaoInicialVertical + passaro[0].getHeight()/2, passaro[0].getWidth()/2);
        canoBaixoRetangulo = new Rectangle(posicaoMovimentoCanoHorizontal,
                altura/2 - canoBaixo.getHeight() - espacoEntreCanos/2 + alturaEntreCanosRandomica,
                canoBaixo.getWidth(), canoBaixo.getHeight());
        canoTopoRetangulo = new Rectangle(posicaoMovimentoCanoHorizontal, altura/2 + espacoEntreCanos/2 + alturaEntreCanosRandomica,
                canoAlto.getWidth(), canoAlto.getHeight());

        //desenhar formas
       /* shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.circle(passaroCirculo.x, passaroCirculo.y,passaroCirculo.radius);
        shapeRenderer.rect(canoBaixoRetangulo.x,canoBaixoRetangulo.y, canoBaixoRetangulo.width, canoBaixoRetangulo.height);
        shapeRenderer.rect(canoTopoRetangulo.x,canoTopoRetangulo.y, canoTopoRetangulo.width, canoTopoRetangulo.height);
        shapeRenderer.setColor(com.badlogic.gdx.graphics.Color.RED);
        shapeRenderer.end();*/

        //teste de colisão
        if (Intersector.overlaps(passaroCirculo, canoBaixoRetangulo) || Intersector.overlaps(passaroCirculo, canoTopoRetangulo)
                || posicaoInicialVertical<=0 || posicaoInicialVertical >= altura) {
            estadoJogo=2;
    }


	}

    @Override
    public void resize(int width, int height) {
        viewport.update(width,height);
    }
}
