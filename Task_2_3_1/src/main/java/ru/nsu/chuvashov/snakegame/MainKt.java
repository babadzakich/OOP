package ru.nsu.chuvashov.snakegame;

import javafx.application.Application;
import javafx.stage.Stage;

public class MainKt extends Application {
    Controller controller;

    @Override
    public void start(Stage stage) {
        controller = new Controller(stage);
    }

    public static void main(String[] args) {
        launch(args);
    }
}