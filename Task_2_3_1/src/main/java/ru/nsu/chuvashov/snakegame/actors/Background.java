package ru.nsu.chuvashov.snakegame.actors;

import static ru.nsu.chuvashov.snakegame.Controller.*;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import ru.nsu.chuvashov.snakegame.Actor;
import ru.nsu.chuvashov.snakegame.Controller;

/**
 * Background painting class.
 */
public class Background implements Actor {
    @Override
    public void draw(GraphicsContext gc) {
        for (int i = 0; i < COLS; i++) {
            for (int j = 0; j < ROWS; j++) {
                if (i % 2 == 0) {
                    if (j % 2 == 0) {
                        gc.setFill(Color.web("aad751"));
                    } else {
                        gc.setFill(Color.web("a2d149"));
                    }
                } else {
                    if (j % 2 == 0) {
                        gc.setFill(Color.web("a2d149"));
                    } else {
                        gc.setFill(Color.web("aad751"));
                    }
                }
                gc.fillRect(i * BLOCK_SIZE, j * BLOCK_SIZE, BLOCK_SIZE, BLOCK_SIZE);
            }
        }
    }

    @Override
    public boolean update(Controller controller) {
        return true;
    }
}
