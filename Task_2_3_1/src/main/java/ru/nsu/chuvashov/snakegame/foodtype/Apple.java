package ru.nsu.chuvashov.snakegame.foodtype;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import ru.nsu.chuvashov.snakegame.Food;
import ru.nsu.chuvashov.snakegame.Snake;

import java.awt.Point;
import java.util.Objects;

import static ru.nsu.chuvashov.snakegame.MainKt.*;

public class Apple implements Food {
    private final Image food;
    private int foodX;
    private int foodY;

    public Apple() {
        food = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/apple.png")));
    }

    @Override
    public void generate(Snake snake) {
        while (true) {
            foodX = (int) (Math.random() * COLS);
            foodY = (int) (Math.random() * ROWS);
            boolean flag = false;
            for (Point point : snake.getSnakeBody()) {
                if (point.getX() == foodX && point.getY() == foodY) {
                    flag = true;
                    break;
                }
            }
            if (!flag) {
                break;
            }
        }
    }

    @Override
    public double getX() {
        return foodX;
    }

    @Override
    public double getY() {
        return foodY;
    }

    @Override
    public void draw(GraphicsContext gc) {
        gc.drawImage(food, foodX * BLOCK_SIZE, foodY * BLOCK_SIZE, BLOCK_SIZE, BLOCK_SIZE);
    }
}
