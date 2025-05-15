package ru.nsu.chuvashov.snakegame;

import javafx.scene.canvas.GraphicsContext;

/**
 * Actor interface that all units in game will implement.
 */
public interface Actor {
    void draw(GraphicsContext gc);

    boolean update(Controller controller);
}
