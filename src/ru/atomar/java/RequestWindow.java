package ru.atomar.java;

import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * Created by RusAr on 31.01.2017.
 */
public class RequestWindow {

    Stage mStage;
    TextArea mOutput;
    TextField mUrl;
    TextField mFile;
    TextArea mContent;

    public RequestWindow() {
        BorderPane mainPane = new BorderPane();
        Scene scene = new Scene(mainPane);

        VBox vBox = new VBox();
        HBox hBox;
        Label label;

        hBox = new HBox();
        label = new Label();
        label.setText("Host:");
        mUrl = new TextField();
        mUrl.setText("localhost");
        mUrl.setPrefWidth(150);
        hBox.getChildren().addAll(label, mUrl);
        label = new Label();
        label.setText("Res:");
        mFile = new TextField();
        mFile.setText("/");
        mFile.setPrefWidth(150);
        hBox.getChildren().addAll(label, mFile);
        vBox.getChildren().add(hBox);

        label = new Label();
        label.setText("Content:");
        mContent = new TextArea();
        mContent.setPrefSize(800, 60);
        vBox.getChildren().addAll(label, mContent);

        Button sendButton = new Button("Send");
        sendButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                mOutput.clear();
                mOutput.setText(RequestUtils.sendHttp1Request(mUrl.getText(), mFile.getText(), mContent.getText()));
            }
        });
        vBox.getChildren().add(sendButton);

        label = new Label();
        label.setText("Response:");
        mOutput = new TextArea();
        mOutput.setPrefSize(800, 600);
        vBox.getChildren().addAll(label, mOutput);

        mainPane.setCenter(vBox);

        mStage = new Stage();
        mStage.setScene(scene);

        mStage.show();
    }

}
