package ru.nsu.chuvashov.snakegame;

import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import lombok.Setter;

import java.util.*;

public class InputHandler {
    private final Set<KeyCode> keys = new HashSet<>();
    private final Queue<Direction> queue = new LinkedList<>();
    private Direction currentDirection = Direction.RIGHT;
    private final int QUEUE_SIZE = 2;
    @Setter private boolean moved = false;

    public InputHandler(Scene scene) {
        scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                if (!moved) {
                    return;
                }
                KeyCode code = keyEvent.getCode();
                if (code == KeyCode.UP && currentDirection != Direction.DOWN) {
                    currentDirection = Direction.UP;
                } else if (code == KeyCode.DOWN && currentDirection != Direction.UP) {
                    currentDirection = Direction.DOWN;
                } else if (code == KeyCode.LEFT && currentDirection != Direction.RIGHT) {
                    currentDirection = Direction.LEFT;
                } else if (code == KeyCode.RIGHT && currentDirection != Direction.LEFT) {
                    currentDirection = Direction.RIGHT;
                }
                moved = false;
            }
        });
//        scene.setOnKeyReleased(e -> keys.remove(e.getCode()));
    }

//    public void update() {
//        if (keys.contains(KeyCode.UP) && currentDirection != Direction.DOWN) {
//            addQueue(Direction.UP);
//        } else if (keys.contains(KeyCode.DOWN) && currentDirection != Direction.UP) {
//            addQueue(Direction.DOWN);
//        } else if (keys.contains(KeyCode.LEFT) && currentDirection != Direction.RIGHT) {
//            addQueue(Direction.LEFT);
//        } else if (keys.contains(KeyCode.RIGHT) && currentDirection != Direction.LEFT) {
//            addQueue(Direction.RIGHT);
//        }
//    }
//
//    private void addQueue(Direction direction) {
//        if (queue.size() < QUEUE_SIZE) {
//            queue.add(direction);
//        }
//    }

    public Direction getDirection() {
//    if (!queue.isEmpty()) {
//        currentDirection = queue.poll();
//    }
        return currentDirection;
    }
}
