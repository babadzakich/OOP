package ru.nsu.chuvashov.snakegame;

import javafx.scene.canvas.GraphicsContext;

public interface Actor {
    void draw(GraphicsContext gc);
    boolean update(Controller controller);
}
