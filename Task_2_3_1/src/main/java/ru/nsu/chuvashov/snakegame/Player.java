package ru.nsu.chuvashov.snakegame;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import lombok.Getter;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import static ru.nsu.chuvashov.snakegame.MainKt.*;

public class Player {
    @Getter private List<Point> snakeBody  = new ArrayList<>();
    private Point head;
    private Direction currentDirection = Direction.RIGHT;
    @Getter private int score = 0;

    private Image headUpImage;
    private Image headDownImage;
    private Image headLeftImage;
    private Image headRightImage;
    private Image bodyImage;

    public Player() {
        try {
            headUpImage = new Image((MainKt.class.getResourceAsStream("/Player/head_up.png")));
            headDownImage = new Image((MainKt.class.getResourceAsStream("/Player/head_down.png")));
            headLeftImage = new Image((MainKt.class.getResourceAsStream("/Player/head_left.png")));
            headRightImage = new Image((MainKt.class.getResourceAsStream("/Player/head_right.png")));
            bodyImage = new Image(MainKt.class.getResourceAsStream("/Player/body.png"));
        } catch (Exception e) {
            System.out.println("test");
        }
        head = new Point(5, ROWS / 2);
        snakeBody.add(head);
    }

    public void move(InputHandler inputHandler) {
//        inputHandler.update();
        currentDirection = inputHandler.getDirection();
        Point newHead = snakeBody.size() > 1 ? snakeBody.removeLast() : head;

        double x = head.getX();
        double y = head.getY();

        switch (currentDirection) {
            case UP -> y--;
            case DOWN -> y++;
            case LEFT -> x--;
            case RIGHT -> x++;
        }

        newHead.setLocation(x, y);
        inputHandler.setMoved(true);
        if (newHead != head)
            snakeBody.addFirst(newHead);
        head = newHead;
    }

//    @Override
    public void draw(GraphicsContext g) {
        Image currentHead = null;
        switch (currentDirection) {
            case UP -> currentHead = headUpImage;
            case DOWN -> currentHead = headDownImage;
            case LEFT -> currentHead = headLeftImage;
            case RIGHT -> currentHead = headRightImage;
        }
        g.drawImage(currentHead, head.getX() * BLOCK_SIZE, head.getY() * BLOCK_SIZE, BLOCK_SIZE, BLOCK_SIZE);
        for (int i = 1; i < snakeBody.size(); i++) {
            g.drawImage(bodyImage, snakeBody.get(i).getX() * BLOCK_SIZE, snakeBody.get(i).getY() * BLOCK_SIZE, BLOCK_SIZE, BLOCK_SIZE);
        }
    }

//    @Override
//    public boolean update() {
//        return false;
//    }

    public boolean checkWallCollision() {
        return head.getX() < 0 || head.getY() < 0 || head.getX() >= COLS || head.getY() >= ROWS;
    }

    public boolean checkSnakeCollision() {
        for (int i = 1; i < snakeBody.size(); i++) {
            if (head.getX() == snakeBody.get(i).getX() && head.getY() == snakeBody.get(i).getY()) {
                return true;
            }
        }
        return false;
    }

    public void eatFood(List<Food> foods) {
        for (Food food : foods) {
            if (head.getX() == food.getX() && head.getY() == food.getY()) {
                snakeBody.add(new Point(-1, -1));
                score += food.update(this);
            }
        }
    }
}
