package ru.nsu.chuvashov.snakegame;

import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.scene.*;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;

public class MainKt extends Application {
    public static final int BLOCK_SIZE = 40;
    public static final int BLOCK_WIDTH = BLOCK_SIZE * 20;
    public static final int BLOCK_HEIGHT = BLOCK_SIZE * 15;

    private Direction direction = Direction.RIGHT;
    private boolean moved = false;
    private boolean running = false;

    private AnimationTimer animationTimer;
    private long lastTime = 0;
    private final double speed = 0.13 * 1_000_000_000L; // 0.2 секунды в наносекундах

    private ObservableList<Node> snake;
    private Rectangle food;

    private Parent createContent() {
        Pane root = new Pane();
        root.setPrefSize(BLOCK_WIDTH, BLOCK_HEIGHT);
        Group snakeBody = new Group();
        snake = snakeBody.getChildren();

        food  = new Rectangle(BLOCK_SIZE, BLOCK_SIZE);
        food.setFill(Color.BLUE);
        food.setTranslateX((int) (Math.random() * (BLOCK_WIDTH - BLOCK_SIZE)) / BLOCK_SIZE * BLOCK_SIZE);
        food.setTranslateY((int) (Math.random() * (BLOCK_HEIGHT - BLOCK_SIZE)) / BLOCK_SIZE * BLOCK_SIZE);

        animationTimer = new AnimationTimer() {
            @Override
            public void handle(long l) {
                if (!running) {
                    return;
                }
                if (l - lastTime >= speed) {
                    updateSnake();
                    lastTime = l;
                }
            }
        };

        root.getChildren().addAll(food, snakeBody);
        return root;
    }

    private void updateSnake() {


        boolean toRemove = snake.size() > 1;

        Node tail = toRemove ? snake.removeLast() : snake.getFirst();

        double tailX = tail.getTranslateX();
        double tailY = tail.getTranslateY();

        switch (direction) {
            case UP:
                tail.setTranslateX(snake.getFirst().getTranslateX());
                tail.setTranslateY(snake.getFirst().getTranslateY() - BLOCK_SIZE);
                break;
            case DOWN:
                tail.setTranslateX(snake.getFirst().getTranslateX());
                tail.setTranslateY(snake.getFirst().getTranslateY() + BLOCK_SIZE);
                break;
            case LEFT:
                tail.setTranslateX(snake.getFirst().getTranslateX() - BLOCK_SIZE);
                tail.setTranslateY(snake.getFirst().getTranslateY());
                break;
            case RIGHT:
                tail.setTranslateX(snake.getFirst().getTranslateX() + BLOCK_SIZE);
                tail.setTranslateY(snake.getFirst().getTranslateY());
                break;
        }

        moved = true;

        if (toRemove) {
            snake.addFirst(tail);
        }

        for (Node node : snake) {
            if (node != tail && tail.getTranslateX() == node.getTranslateX() && tail.getTranslateY() == node.getTranslateY()) {
                restartGame();
                break;
            }
        }

        if (tail.getTranslateX() < 0
                || tail.getTranslateY() < 0
                || tail.getTranslateX() >= BLOCK_WIDTH
                || tail.getTranslateY() >= BLOCK_HEIGHT) {
            restartGame();
        }

        if (tail.getTranslateX() == food.getTranslateX()
                && tail.getTranslateY() == food.getTranslateY()) {
            food.setTranslateX((int) (Math.random() * (BLOCK_WIDTH - BLOCK_SIZE)) / BLOCK_SIZE * BLOCK_SIZE);
            food.setTranslateY((int) (Math.random() * (BLOCK_HEIGHT - BLOCK_SIZE)) / BLOCK_SIZE * BLOCK_SIZE);

            Rectangle rect = new Rectangle(BLOCK_SIZE, BLOCK_SIZE);
            rect.setTranslateX(tailX);
            rect.setTranslateY(tailY);
            snake.add(rect);
        }
    }

    private void restartGame() {
        stopGame();
        startGame();
    }

    private void stopGame() {
        running = false;
        animationTimer.stop();
        snake.clear();
    }

    private void startGame() {
        direction = Direction.RIGHT;
        Rectangle head = new Rectangle(BLOCK_SIZE, BLOCK_SIZE);
        snake.add(head);
        running = true;
        lastTime = System.nanoTime();
        animationTimer.start();
    }

    @Override
    public void start(Stage primaryStage) {
        Scene scene = new Scene(createContent());

        scene.setOnKeyPressed(event -> {
            if (!moved) {
                return;
            }
            switch (event.getCode()) {
                case UP:
                    if (direction != Direction.DOWN) {
                        direction = Direction.UP;
                    }
                    break;
                case DOWN:
                    if (direction != Direction.UP) {
                        direction = Direction.DOWN;
                    }
                    break;
                case LEFT:
                    if (direction != Direction.RIGHT) {
                        direction = Direction.LEFT;
                    }
                    break;
                case RIGHT:
                    if (direction != Direction.LEFT) {
                        direction = Direction.RIGHT;
                    }
                    break;
            }

            moved = false;
        });

        primaryStage.setTitle("Snake Game");
        primaryStage.setScene(scene);
        primaryStage.show();
        startGame();
    }

    public static void main(String[] args) {
        launch(args);
    }
}