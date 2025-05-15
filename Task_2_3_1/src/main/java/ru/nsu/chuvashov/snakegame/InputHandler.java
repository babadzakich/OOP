package ru.nsu.chuvashov.snakegame;

import java.util.*;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import lombok.Setter;

/**
 * Handler for snake movement.
 */
public class InputHandler {
    private final Set<KeyCode> keys = new HashSet<>();
    private final Queue<Direction> queue = new LinkedList<>();
    private Direction currentDirection = Direction.RIGHT;
    private final int queueSize = 2;
    @Setter private boolean moved = false;

    /**
     * Constructor.
     *
     * @param scene - our scene.
     */
    public InputHandler(Scene scene) {
        scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            public void addQueue(Direction direction) {
                if (queue.size() <= queueSize) {
                    queue.add(direction);
                }
            }

            @Override
            public void handle(KeyEvent keyEvent) {
                KeyCode code = keyEvent.getCode();
                if (code == KeyCode.UP && currentDirection != Direction.DOWN) {
                    currentDirection = Direction.UP;
                    addQueue(currentDirection);
                } else if (code == KeyCode.DOWN && currentDirection != Direction.UP) {
                    currentDirection = Direction.DOWN;
                    addQueue(currentDirection);
                } else if (code == KeyCode.LEFT && currentDirection != Direction.RIGHT) {
                    currentDirection = Direction.LEFT;
                    addQueue(currentDirection);
                } else if (code == KeyCode.RIGHT && currentDirection != Direction.LEFT) {
                    currentDirection = Direction.RIGHT;
                    addQueue(currentDirection);
                }
            }
        });
    }

    /**
     * Get direction for snake.
     *
     * @return direction for snake.
     */
    public Direction getDirection() {
        return queue.isEmpty() ? currentDirection : queue.poll();
    }
}
