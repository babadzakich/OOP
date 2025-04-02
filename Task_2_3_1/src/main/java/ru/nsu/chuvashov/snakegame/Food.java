package ru.nsu.chuvashov.snakegame;

import javafx.scene.canvas.GraphicsContext;

public interface Food {
    void draw(GraphicsContext gc);
    void generate(Snake snake);
    double getX();
    double getY();
}
