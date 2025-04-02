package ru.nsu.chuvashov.snakegame;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;
import ru.nsu.chuvashov.snakegame.foodtype.Apple;

public class MainKt extends Application {
    public static final int ROWS = 15;
    public static final int COLS = 20;
    public static final int BLOCK_SIZE = 40;
    public static final int WIDTH = COLS * BLOCK_SIZE;
    public static final int HEIGHT = ROWS * BLOCK_SIZE;

    private GraphicsContext gc;
    private Snake snake;
    private Food food;
    private InputHandler inputHandler;

    private boolean gameOver = false;

    @Override
    public void start(Stage stage) {
        snake = new Snake();
        food = new Apple();
        food.generate(snake);
        stage.setTitle("Snake Game");
        Group root = new Group();
        Canvas canvas = new Canvas(WIDTH, HEIGHT);
        root.getChildren().add(canvas);
        Scene scene = new Scene(root, WIDTH, HEIGHT);
        inputHandler = new InputHandler(scene);
        stage.setScene(scene);
        stage.show();
        gc = canvas.getGraphicsContext2D();

        drawScore(gc);
        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(130), event -> run()));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }

    private void run() {
        if (gameOver) {
            gc.setFill(Color.RED);
            gc.setFont(new Font("Digital-7", 70));
            gc.fillText("Game Over", WIDTH / 3.5, HEIGHT / 2.0);
            return;
        }
        drawBackground(gc);
        food.draw(gc);
        snake.draw(gc);
        drawScore(gc);

        snake.move(inputHandler);

        snake.eatFood(food);

        if (snake.checkSnakeCollision() || snake.checkWallCollision()) {
            gameOver = true;
        }
    }

    private void drawBackground(GraphicsContext gc) {
        for (int i = 0; i < COLS; i++) {
            for (int j = 0; j < ROWS; j++) {
                if ((i + j) % 2 == 0) {
                    gc.setFill(Color.web("AAD751"));
                } else {
                    gc.setFill(Color.web("A2D149"));
                }
                gc.fillRect(i * BLOCK_SIZE, j * BLOCK_SIZE, BLOCK_SIZE, BLOCK_SIZE);
            }
        }
    }

    private void drawScore(GraphicsContext gc) {
        gc.setFill(Color.WHITE);
        gc.setFont(new Font("Digital-7", 35));
        gc.fillText("Score: " + snake.getScore(), 10, 35);
    }

    public static void main(String[] args) {
        launch(args);
    }
}