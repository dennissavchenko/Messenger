package org.example.demo;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

public class MessengerController {

    @FXML
    TextField textField;

    @FXML
    VBox scrollBox;

    @FXML
    Button topicButton;

    private Client client;

    public void initialize() {
        client = new Client(scrollBox);
    }

    private void setStyleClass(Button button, String style) {
        button.getStyleClass().removeAll("non-chosen-topic", "chosen-topic");
        button.getStyleClass().add(style);
    }
    public void changeTopic(ActionEvent actionEvent) {
        Button clickedButton = (Button) actionEvent.getSource();
        switch (clickedButton.getText()) {
            case "Capital letters" -> {
                if (client.getCapital()) {
                    client.setCapital(false);
                    setStyleClass(clickedButton, "non-chosen-topic");
                } else {
                    client.setCapital(true);
                    setStyleClass(clickedButton, "chosen-topic");
                }
            }
            case "Reversed" -> {
                if (client.getReversed()) {
                    client.setReversed(false);
                    setStyleClass(clickedButton, "non-chosen-topic");
                } else {
                    client.setReversed(true);
                    setStyleClass(clickedButton, "chosen-topic");
                }
            }
            case "Byte" -> {
                if (client.getByte()) {
                    client.setByte(false);
                    setStyleClass(clickedButton, "non-chosen-topic");
                } else {
                    client.setByte(true);
                    setStyleClass(clickedButton, "chosen-topic");
                }
            }
        }
    }

    public void sendMessage() {
        client.sendMessage(textField.getText(), topicButton.getText());
        if(!textField.getText().isEmpty()) {
            HBox hBox = new HBox();
            hBox.setAlignment(Pos.CENTER_RIGHT);
            hBox.setPadding(new Insets(5, 0, 5, 25));

            Text text = new Text(textField.getText());
            TextFlow textFlow = new TextFlow(text);

            textFlow.getStyleClass().add("chosen-topic");

            textFlow.setPadding(new Insets(5, 10, 5, 10));
            text.setFill(Color.color(1, 1, 1));

            hBox.getChildren().add(textFlow);
            scrollBox.getChildren().add(hBox);
        }
        textField.clear();
    }

    public static void addMessage(String message, VBox vBox) {
        if(!message.isEmpty()) {
            HBox hBox = new HBox();
            hBox.setAlignment(Pos.CENTER_LEFT);
            hBox.setPadding(new Insets(5, 25, 5, 0));

            Text text = new Text(message);
            TextFlow textFlow = new TextFlow(text);

            textFlow.getStyleClass().add("server-response");

            textFlow.setPadding(new Insets(5, 10, 5, 10));
            text.setFill(Color.color(0, 0, 0));

            hBox.getChildren().add(textFlow);
            vBox.getChildren().add(hBox);
        }
    }

    public void changeMessageTopic(ActionEvent actionEvent) {
        Button clickedButton = (Button) actionEvent.getSource();
        switch (clickedButton.getText()) {
            case "CL" -> clickedButton.setText("RV");
            case "RV" -> clickedButton.setText("BV");
            case "BV" -> clickedButton.setText("CL");
        }
    }
}
