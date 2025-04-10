package ru.nsu.chuvashov.snakegame.actors;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import lombok.Getter;
import ru.nsu.chuvashov.snakegame.*;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static ru.nsu.chuvashov.snakegame.Controller.*;

public class Player implements Actor {
    @Getter private List<Point> snakeBody  = new ArrayList<>();
    private Point head;
    private Point prevHead;
    private Color bodyColor = Color.LIGHTBLUE;
    private Direction currentDirection = Direction.RIGHT, prevDirection = Direction.RIGHT;
    public int score = 0;
//    private Image[][] directions = new Image[][]{}

    private Image headImage;      // Спрайт головы (уже есть)
    private Image bodyHorizontal; // Горизонтальный сегмент
    private Image bodyTurnUL;     // Поворот из Up в Left
    private Image tail;

    public Player(int x, int y, String head, String body, String turn, String tail) {
        headImage = new Image((Objects.requireNonNull(getClass().getResourceAsStream(head))));
        bodyHorizontal = new Image(Objects.requireNonNull(getClass().getResourceAsStream(body)));
        bodyTurnUL = new Image(Objects.requireNonNull(getClass().getResourceAsStream(turn)));
        this.tail = new Image((Objects.requireNonNull(getClass().getResourceAsStream(tail))));
        this.head = new Point(x,y);
        prevHead = this.head;
        snakeBody.add(this.head);
    }



    @Override
    public void draw(GraphicsContext g) {
        double rotationAngle = 0;
        switch (currentDirection) {
            case UP -> rotationAngle = 270;
            case DOWN -> rotationAngle = 90;
            case LEFT -> rotationAngle = 180;
            case RIGHT -> rotationAngle = 0;
        }
        g.save();

        double centerX = head.getX() * BLOCK_SIZE + BLOCK_SIZE / 2;
        double centerY = head.getY() * BLOCK_SIZE + BLOCK_SIZE / 2;

        g.translate(centerX, centerY);
        g.rotate(rotationAngle);

        g.drawImage(headImage, -BLOCK_SIZE / 2, -BLOCK_SIZE / 2, BLOCK_SIZE, BLOCK_SIZE);

        g.restore();

//        for (int i = 1; i < snakeBody.size() - 1; i++) {
//            Point prev = snakeBody.get(i-1);
//            Point current = snakeBody.get(i);
//            Point next = snakeBody.get(i+1);
//
//            Direction dirFrom = getDirection(prev, current);
//            Direction dirTo = getDirection(current, next);
//
//            Image segmentImage = determineBodySprite(dirFrom, dirTo);
//            drawBodySegment(g, current, segmentImage);
//        }
        g.setFill(bodyColor);
        for (int i = 1; i < snakeBody.size(); i++) {
            g.fillRect(snakeBody.get(i).x * BLOCK_SIZE, snakeBody.get(i).y * BLOCK_SIZE, BLOCK_SIZE, BLOCK_SIZE);
//            g.drawImage(bodyHorizontal, snakeBody.get(i).getX() * BLOCK_SIZE, snakeBody.get(i).getY() * BLOCK_SIZE, BLOCK_SIZE, BLOCK_SIZE);
        }


        g.setFill(Color.WHITE);
        g.setFont(new Font("Digital-7", BLOCK_SIZE * 0.875));
        g.fillText("Score: " + score, 10, 35);
    }

    @Override
    public boolean update(Controller controller) {
        return move(controller);
    }

    private boolean move(Controller controller) {
        prevDirection = currentDirection;
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
        prevHead = head;
        head = newHead;
        return !(checkSnakeCollision() || checkWallCollision());
    }

    private boolean checkWallCollision() {
        return head.getX() < 0 || head.getY() < 0 || head.getX() >= COLS || head.getY() >= ROWS;
    }

    private boolean checkSnakeCollision() {
        for (int i = 1; i < snakeBody.size(); i++) {
            if (head.getX() == snakeBody.get(i).getX() && head.getY() == snakeBody.get(i).getY()) {
                return true;
            }
        }
        return false;
    }


}
