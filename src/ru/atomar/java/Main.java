package ru.atomar.java;

import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {

    private static final String version = "Version 0.0.11 20180209";

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        RequestWindow window = new RequestWindow(version);
    }
}
