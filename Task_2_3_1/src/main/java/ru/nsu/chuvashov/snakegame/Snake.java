package ru.nsu.chuvashov.snakegame;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import lombok.Getter;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

import static ru.nsu.chuvashov.snakegame.MainKt.*;

public class Snake {
    @Getter private List<Point> snakeBody  = new ArrayList<>();
    private Point head;
    private Direction currentDirection = Direction.RIGHT;
    @Getter private int score = 0;

    private Image headImage;
    private Image bodyImage;

    public Snake() {
        headImage = new Image(MainKt.class.getResourceAsStream("/head_right.png"));
        bodyImage = new Image(MainKt.class.getResourceAsStream("/body_horizontal.png"));
        head = new Point(5, ROWS / 2);
        snakeBody.add(head);
        for (int i = 0; i < 2; i++) {
            snakeBody.add(new Point(5, ROWS / 2));
        }
    }

    public void move(InputHandler inputHandler) {
//        inputHandler.update();
        currentDirection = inputHandler.getDirection();
        Point newHead = snakeBody.removeLast();

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
        snakeBody.addFirst(newHead);
        head = newHead;
    }

    public void draw(GraphicsContext g) {
        g.setFill(Color.web("4674E9"));
        g.fillRoundRect(head.getX() * BLOCK_SIZE, head.getY() * BLOCK_SIZE, BLOCK_SIZE - 1, BLOCK_SIZE - 1, 35, 35);
//        g.drawImage(headImage, head.getX() * BLOCK_SIZE, head.getY() * BLOCK_SIZE, BLOCK_SIZE, BLOCK_SIZE);
        for (int i = 1; i < snakeBody.size(); i++) {
            g.fillRoundRect(snakeBody.get(i).getX() * BLOCK_SIZE, snakeBody.get(i).getY() * BLOCK_SIZE, BLOCK_SIZE - 1, BLOCK_SIZE - 1, 20, 20);
//            g.drawImage(bodyImage, snakeBody.get(i).getX() * BLOCK_SIZE, snakeBody.get(i).getY() * BLOCK_SIZE, BLOCK_SIZE, BLOCK_SIZE);
        }
    }

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

    public void eatFood(Food food) {
        if (head.getX() == food.getX() && head.getY() == food.getY()) {
            snakeBody.add(new Point(-1, -1));
            food.generate(this);
            score += 10;
        }
    }
}
