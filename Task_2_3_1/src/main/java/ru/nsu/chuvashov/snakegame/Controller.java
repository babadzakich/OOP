package ru.nsu.chuvashov.snakegame;

import java.util.ArrayList;
import java.util.List;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;
import ru.nsu.chuvashov.snakegame.actors.Background;
import ru.nsu.chuvashov.snakegame.actors.Player;
import ru.nsu.chuvashov.snakegame.actors.foodtype.Apple;
import ru.nsu.chuvashov.snakegame.actors.foodtype.GoldenApple;

/**
 * Our game controller.
 */
public class Controller {
    public static final int ROWS = 15;
    public static final int COLS = 20;
    public static final int BLOCK_SIZE = 30;
    public static final int WIDTH = COLS * BLOCK_SIZE;
    public static final int HEIGHT = ROWS * BLOCK_SIZE;
    public static final int FOOD_AMOUNT = 5;

    public final GraphicsContext gc;
    public static InputHandler inputHandler;
    private final List<Actor> actors = new ArrayList<>();
    public final Player player;

    private boolean gameOver = false;
    private boolean win = false;

    Controller(Stage stage) {
        stage.setTitle("Snake Game");
        Group root = new Group();
        Canvas canvas = new Canvas(WIDTH, HEIGHT);
        root.getChildren().add(canvas);
        Scene scene = new Scene(root, WIDTH, HEIGHT);
        inputHandler = new InputHandler(scene);
        stage.setScene(scene);
        stage.show();
        gc = canvas.getGraphicsContext2D();

        actors.add(new Background());
        actors.add(player = new Player(COLS / 4, ROWS / 4,
                "/Player/head_right.png", "/Player/body_horizontal.png",
                "/Player/body_topleft.png", "/Player/tail_right.png"));

        for (int i = 0; i < FOOD_AMOUNT; i++) {
            actors.add(i % 2 == 0 ? new Apple() : new GoldenApple());
            actors.getLast().update(this);
        }

        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(0.150), event -> run()));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    private void run() {
        if (gameOver) {
            gc.setFill(Color.RED);
            gc.setFont(new Font("Digital-7", BLOCK_SIZE * 1.75));
            gc.fillText("Game Over", WIDTH / 3.5, HEIGHT / 2.0);
            return;
        }

        for (Actor actor : actors) {
            actor.draw(gc);
        }

        if (win) {
            gc.setFill(Color.RED);
            gc.setFont(new Font("Digital-7", BLOCK_SIZE));
            gc.fillText("You won, Congratulations!", WIDTH / 3.5, HEIGHT / 2.0);
            return;
        }

        for (Actor actor : actors) {
            if (!gameOver) {
                gameOver = !actor.update(this);
            } else {
                return;
            }
        }

        if (player.score >= 200) {
            win = true;
        }
    }
}
