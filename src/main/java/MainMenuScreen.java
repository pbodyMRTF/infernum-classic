

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import java.util.Random;


public class MainMenuScreen implements Screen {
    final Jgame game;
    BitmapFont font;
    String titleText;
    private static final int PARTICLE_COUNT = 50;
    private float titleScale = 1.0f;
    private float titlePulse = 0.0f;
    private float menuAlpha = 0.0f;
    private float backgroundHue = 0.0f;
    private int selectedOption = 0;
    private float selectionBlink = 0.0f;
    private float[] particleX = new float[50];
    private float[] particleY = new float[50];
    private float[] particleSpeed = new float[50];
    private float[] particleSize = new float[50];
    SpriteBatch batch = new SpriteBatch();
    ShapeRenderer shapeRenderer = new ShapeRenderer();

    public MainMenuScreen(Jgame game) {
        this.game = game;
        this.font = game.font;
        new Random();
        this.titleText = "    Infernum:\n Classic Edition";
        initParticles();
    }

    private void initParticles() {
        Random rand = new Random();
        for (int i = 0; i < 50; i++) {
            this.particleX[i] = rand.nextFloat() * Gdx.graphics.getWidth();
            this.particleY[i] = rand.nextFloat() * Gdx.graphics.getHeight();
            this.particleSpeed[i] = 20.0f + (rand.nextFloat() * 40.0f);
            this.particleSize[i] = 2.0f + (rand.nextFloat() * 4.0f);
        }
    }

    @Override // com.badlogic.gdx.Screen
    public void render(float delta) {
        updateAnimations(delta);
        Color bgColor = new Color();
        bgColor.fromHsv(this.backgroundHue, 0.6f, 0.3f);
        Gdx.gl.glClearColor(bgColor.r, bgColor.g, bgColor.b, 1.0f);
        Gdx.gl.glClear(16384);
        int w = Gdx.graphics.getWidth();
        int h = Gdx.graphics.getHeight();
        drawDecorations(w, h);
        this.batch.begin();
        this.font.getData().setScale(2.3f + (this.titleScale * 0.1f));
        this.font.setColor(1.0f, 1.0f, 1.0f, 1.0f);
        this.font.draw(this.batch, this.titleText, (w / 2.0f) - 300f, h / 2.0f + 50);
        this.font.getData().setScale(1.0f);
        this.font.setColor(1.0f, 1.0f, 1.0f, this.menuAlpha);
        if (this.selectedOption == 0) {
            this.font.setColor(1.0f, 1.0f, 0.0f, this.menuAlpha * this.selectionBlink);
            this.font.draw(this.batch, "> Start <", (w / 2.0f) - 475.0f, h / 2.0f);
        } else {
            this.font.draw(this.batch, "  Start", (w / 2.0f) - 475.0f, h / 2.0f);
        }
        this.font.setColor(1.0f, 1.0f, 1.0f, this.menuAlpha);
        if (this.selectedOption == 1) {
            this.font.setColor(1.0f, 1.0f, 0.0f, this.menuAlpha * this.selectionBlink);
            this.font.draw(this.batch, "> Exit <", (w / 2.0f) - 475.0f, (h / 2.0f) - 50.0f);
        } else {
            this.font.draw(this.batch, "  Exit ", (w / 2.0f) - 475.0f, (h / 2.0f) - 50.0f);
        }
        this.font.setColor(0.7f, 0.7f, 0.7f, this.menuAlpha * 0.6f);
        this.font.getData().setScale(0.7f);
        this.font.draw(this.batch, "powered by LibGDX and LWJGL | " + Jgame.Version , 10.0f, 20.0f);
        this.font.getData().setScale(1.0f);
        this.batch.end();
        handleInput();
    }

    private void updateAnimations(float delta) {
        this.titlePulse += delta * 3.0f;
        this.titleScale = (MathUtils.sin(this.titlePulse) * 0.5f) + 0.5f;
        this.menuAlpha = Math.min(this.menuAlpha + (delta * 1.2f), 1.0f);
        this.backgroundHue = (this.backgroundHue + (delta * 20.0f)) % 360.0f;
        this.selectionBlink += delta * 4.0f;
        this.selectionBlink = (MathUtils.sin(this.selectionBlink) * 0.3f) + 0.7f;
    }

    private void drawParticles(float delta) {
        this.shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        for (int i = 0; i < 50; i++) {
            float[] fArr = this.particleY;
            int r1 = i;
            fArr[r1] = fArr[r1] - (this.particleSpeed[i] * delta);
            if (this.particleY[i] < -10.0f) {
                this.particleY[i] = Gdx.graphics.getHeight() + 10;
                this.particleX[i] = new Random().nextFloat() * Gdx.graphics.getWidth();
            }
            this.shapeRenderer.setColor(1.0f, 1.0f, 1.0f, 0.3f);
            this.shapeRenderer.circle(this.particleX[i], this.particleY[i], this.particleSize[i]);
        }
        this.shapeRenderer.end();
    }

    private void drawDecorations(int w, int h) {
        this.shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        this.shapeRenderer.setColor(1.0f, 1.0f, 1.0f, 0.2f);
        this.shapeRenderer.rect(30.0f, 30.0f, w - (30.0f * 2.0f), h - (30.0f * 2.0f));
        this.shapeRenderer.line(30.0f, h - 30.0f, 30.0f + 20.0f, h - 30.0f);
        this.shapeRenderer.line(30.0f, h - 30.0f, 30.0f, (h - 30.0f) - 20.0f);
        this.shapeRenderer.line(w - 30.0f, h - 30.0f, (w - 30.0f) - 20.0f, h - 30.0f);
        this.shapeRenderer.line(w - 30.0f, h - 30.0f, w - 30.0f, (h - 30.0f) - 20.0f);
        this.shapeRenderer.line(30.0f, 30.0f, 30.0f + 20.0f, 30.0f);
        this.shapeRenderer.line(30.0f, 30.0f, 30.0f, 30.0f + 20.0f);
        this.shapeRenderer.line(w - 30.0f, 30.0f, (w - 30.0f) - 20.0f, 30.0f);
        this.shapeRenderer.line(w - 30.0f, 30.0f, w - 30.0f, 30.0f + 20.0f);
        this.shapeRenderer.end();
    }

    private void handleInput() {
        boolean up = Gdx.input.isKeyJustPressed(Input.Keys.UP) || Gdx.input.isKeyJustPressed(Input.Keys.W);
        boolean down = Gdx.input.isKeyJustPressed(Input.Keys.DOWN) || Gdx.input.isKeyJustPressed(Input.Keys.S);
        if (up || down) {
            selectedOption = (selectedOption + 1) % 2;
        }

        boolean confirm = Gdx.input.isKeyJustPressed(Input.Keys.ENTER) || Gdx.input.isKeyJustPressed(Input.Keys.SPACE);
        if (confirm) {
            if (selectedOption == 0) {
                game.setScreen(new GameScreen(game));
                dispose();
            } else {
                Gdx.app.exit();
            }
        }

        boolean quit = Gdx.input.isKeyPressed(Input.Keys.ESCAPE) || Gdx.input.isKeyPressed(Input.Keys.BACK);
        if (quit) {
            Gdx.app.exit();
        }
    } 

    @Override // com.badlogic.gdx.Screen
    public void show() {
    }

    @Override // com.badlogic.gdx.Screen
    public void resize(int width, int height) {
        initParticles();
    }

    @Override // com.badlogic.gdx.Screen
    public void pause() {
    }

    @Override // com.badlogic.gdx.Screen
    public void resume() {
    }

    @Override // com.badlogic.gdx.Screen
    public void hide() {
    }

    @Override // com.badlogic.gdx.Screen, com.badlogic.gdx.utils.Disposable
    public void dispose() {
        this.batch.dispose();
        this.shapeRenderer.dispose();
    }
}
