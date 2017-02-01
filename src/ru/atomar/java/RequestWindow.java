package ru.atomar.java;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
        vBox.setPadding(new Insets(8, 8, 8, 8));
        HBox hBox;
        Label label;
        Button button;


        hBox = new HBox();
        hBox.setSpacing(16);
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
        mContent.setPrefSize(800, 80);
        mContent.setWrapText(true);
        vBox.getChildren().addAll(label, mContent);

        hBox = new HBox();
        hBox.setSpacing(16);
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
        hBox = new HBox();
        hBox.setSpacing(16);
        hBox.setPadding(new Insets(16, 8, 16, 8));
        hBox.setStyle("-fx-background-color: #e0e0e0;");

        loadTemplate("default");
        mTemplateCB = new ComboBox<String>();
        mTemplateCB.setPrefWidth(400);
        mTemplateCB.setEditable(true);
        hBox.getChildren().add(mTemplateCB);
        refreshTemplateList("");

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

        button = new Button("Delete");
        button.setPrefWidth(50);
        button.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                String val = mTemplateCB.getValue();
                if (!val.endsWith(RequestTemplate.EXTENTION))
                    val = val + RequestTemplate.EXTENTION;
                (new RequestTemplate(val)).delete();
            }
        });
        hBox.getChildren().add(button);


        mainPane.setTop(hBox);

        mStage = new Stage();
        mStage.setScene(scene);
        mStage.setTitle("Requester. Send HTTP 1 Request easily.");

        mStage.show();
    }

    private void refreshTemplateList(final String current) {
        final ObservableList olist = FXCollections.observableArrayList(RequestTemplate.getTemplateList());
        olist.add(0, "default" + RequestTemplate.EXTENTION);
        mTemplateCB = this.mTemplateCB;
        mTemplateCB.setItems(olist);

        if (!current.equals(""))
            mTemplateCB.setValue(current);
        else
            mTemplateCB.setValue("default" + RequestTemplate.EXTENTION);
    }

    private void loadTemplate(String name) {
        mTemplate = new RequestTemplate(name);
        mUrl.setText(mTemplate.host);
        mFile.setText(mTemplate.file);
        mContent.setText(mTemplate.params);
    }

    /**
     * @param name
     */
    private void saveTemplate(String name) {
        mTemplate = new RequestTemplate(name, mUrl.getText(), mFile.getText(), mContent.getText());
        mTemplate.save();
        refreshTemplateList(name);
    }

}
