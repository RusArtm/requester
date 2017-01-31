package ru.atomar.java;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
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
    RequestTemplate mTemplate;
    ComboBox<String> mTemplateCB;
    AsyncRequestSender.RequestListener senderListener;

    public RequestWindow() {

        senderListener = new AsyncRequestSender.RequestListener() {
            @Override
            public void onNewLine(String line) {
                mOutput.appendText(line);
            }
        };

        BorderPane mainPane = new BorderPane();
        Scene scene = new Scene(mainPane);

        VBox vBox = new VBox();
        vBox.setPadding(new Insets(4, 4, 4, 4));
        HBox hBox;
        Label label;
        Button button;


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
        mFile.setPrefWidth(400);
        hBox.getChildren().addAll(label, mFile);
        vBox.getChildren().add(hBox);

        label = new Label();
        label.setText("Content:");
        mContent = new TextArea();
        mContent.setPrefSize(800, 60);
        vBox.getChildren().addAll(label, mContent);

        hBox = new HBox();
        hBox.setPadding(new Insets(4, 4, 4, 4));
        Button sendButton = new Button("Send");
        sendButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                AsyncRequestSender sender = new AsyncRequestSender(mUrl.getText(), mFile.getText(), mContent.getText(), senderListener);
                sender.start();
            }
        });
        sendButton.setPadding(new Insets(4, 4, 4, 4));
        hBox.getChildren().add(sendButton);

        button = new Button("Clear");
        button.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                mOutput.clear();
            }
        });
        hBox.getChildren().add(button);
        vBox.getChildren().add(hBox);

        label = new Label();
        label.setText("Response:");
        mOutput = new TextArea();
        mOutput.setPrefSize(800, 600);
        vBox.getChildren().addAll(label, mOutput);

        mainPane.setCenter(vBox);

        // Templates
        vBox = new VBox();
        vBox.setPadding(new Insets(4, 4, 26, 4));

        loadTemplate("default");
        mTemplateCB = buildTemplateList("");
        vBox.getChildren().add(mTemplateCB);

        hBox = new HBox();
        vBox.getChildren().add(hBox);

        button = new Button("Load");
        button.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                String val = mTemplateCB.getValue();
                if (!val.endsWith(RequestTemplate.EXTENTION))
                    val = val + RequestTemplate.EXTENTION;
                loadTemplate(val);
            }
        });
        button.setPrefWidth(50);
        hBox.getChildren().add(button);

        button = new Button("Save");
        button.setPrefWidth(50);
        button.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                String val = mTemplateCB.getValue();
                if (!val.endsWith(RequestTemplate.EXTENTION))
                    val = val + RequestTemplate.EXTENTION;
                saveTemplate(val);
            }
        });
        hBox.getChildren().add(button);


        mainPane.setTop(vBox);

        mStage = new Stage();
        mStage.setScene(scene);
        mStage.setTitle("Requester. Send HTTP 1 Request easily.");

        mStage.show();
    }

    private ComboBox<String> buildTemplateList(final String current) {
        final ObservableList olist = FXCollections.observableArrayList(RequestTemplate.getTemplateList());
        olist.add(0, "default" + RequestTemplate.EXTENTION);
        final ComboBox<String> cb;
        if (mTemplateCB == null)
            cb = new ComboBox<String>(olist);
        else {
            cb = mTemplateCB;
            cb.setItems(olist);
        }
        cb.setPrefWidth(200);
        cb.setEditable(true);

        if (!current.equals(""))
            cb.setValue(current);
        else
            cb.setValue("default" + RequestTemplate.EXTENTION);

        return cb;
    }

    private void loadTemplate(String name) {
        mTemplate = new RequestTemplate(name);
        mUrl.setText(mTemplate.host);
        mFile.setText(mTemplate.file);
        mContent.setText(mTemplate.params);
    }

    private void saveTemplate(String name) {
        mTemplate = new RequestTemplate(name, mUrl.getText(), mFile.getText(), mContent.getText());
        mTemplate.save();
        mTemplateCB = buildTemplateList(name);
    }

}
