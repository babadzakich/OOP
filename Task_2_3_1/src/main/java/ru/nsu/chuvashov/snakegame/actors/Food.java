package ru.nsu.chuvashov.snakegame.actors;

import static ru.nsu.chuvashov.snakegame.Controller.COLS;
import static ru.nsu.chuvashov.snakegame.Controller.ROWS;

import java.awt.*;
import ru.nsu.chuvashov.snakegame.Actor;
import ru.nsu.chuvashov.snakegame.Controller;

/**
 * Food on field class.
 */
public interface Food extends Actor {
    @Override
    default boolean update(Controller controller) {
        Point head = controller.player.getSnakeBody().getFirst();
        if (head.getX() == getX() && head.getY() == getY()) {
            controller.player.getSnakeBody().add(new Point(-1, -1));
            while (true) {
                setX((int) (Math.random() * COLS));
                setY((int) (Math.random() * ROWS));
                boolean flag = false;
                for (Point point : controller.player.getSnakeBody()) {
                    if (point.getX() == getX() && point.getY() == getY()) {
                        flag = true;
                        break;
                    }
                }
                if (!flag) {
                    break;
                }
            }
            controller.player.score += calculateScore();
        }
        return true;
    }

    int calculateScore();

    double getX();

    void setX(int x);

    double getY();

    void setY(int y);
}
