package ru.atomar.java;
/**
 * Version 0.0.1 20171229
 */

import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        RequestWindow window = new RequestWindow();
    }
}
