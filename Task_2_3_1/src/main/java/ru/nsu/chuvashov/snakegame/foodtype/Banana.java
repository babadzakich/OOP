package ru.nsu.chuvashov.snakegame.foodtype;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import ru.nsu.chuvashov.snakegame.Food;
import ru.nsu.chuvashov.snakegame.Player;

import java.awt.*;
import java.util.Objects;

import static ru.nsu.chuvashov.snakegame.MainKt.*;

public class Banana implements Food {
    private final Image food;
    private int foodX;
    private int foodY;

    Banana() {
        food = new Image(Objects.requireNonNull(getClass().getClassLoader().getResourceAsStream("/banana.png")));
    }

    @Override
    public void draw(GraphicsContext gc) {
        gc.drawImage(food, foodX * BLOCK_SIZE, foodY * BLOCK_SIZE, BLOCK_SIZE, BLOCK_SIZE);
    }

    @Override
    public int update(Player player) {
        while (true) {
            foodX = (int) (Math.random() * COLS);
            foodY = (int) (Math.random() * ROWS);
            boolean flag = false;
            for (Point point : player.getSnakeBody()) {
                if (point.getX() == foodX && point.getY() == foodY) {
                    flag = true;
                    break;
                }
            }
            if (!flag) {
                break;
            }
        }
        return -10;
    }

    @Override
    public double getX() {
        return foodX;
    }

    @Override
    public double getY() {
        return foodY;
    }
}
