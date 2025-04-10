package ru.nsu.chuvashov.snakegame.actors.foodtype;

import static ru.nsu.chuvashov.snakegame.Controller.*;

import java.util.Objects;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import ru.nsu.chuvashov.snakegame.actors.Food;

/**
 * Golden apple which gives extra points.
 */
public class GoldenApple implements Food {
    private final Image food;
    private int foodX;
    private int foodY;

    /**
     * Constructor.
     */
    public GoldenApple() {
        food = new Image(Objects.requireNonNull(getClass()
                .getResourceAsStream("/Food/golden_apple.png")));
        setX((int) (Math.random() * COLS));
        setY((int) (Math.random() * ROWS));
    }

    @Override
    public void draw(GraphicsContext gc) {
        gc.drawImage(food, foodX * BLOCK_SIZE,
                foodY * BLOCK_SIZE, BLOCK_SIZE, BLOCK_SIZE);
    }

    @Override
    public int calculateScore() {
        return 20;
    }

    @Override
    public double getX() {
        return foodX;
    }

    @Override
    public void setX(int x) {
        foodX = x;
    }

    @Override
    public double getY() {
        return foodY;
    }

    @Override
    public void setY(int y) {
        foodY = y;
    }
}
