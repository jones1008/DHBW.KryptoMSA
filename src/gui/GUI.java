package gui;

import cryptoManager.CryptoManager;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class GUI extends Application {
    private boolean debugActive = false;

    public void start(Stage primaryStage) {
        primaryStage.setTitle("MSA | Mosbach Security Agency");

        HBox hBox = new HBox();
        hBox.setPadding(new Insets(15, 12, 15, 12));
        hBox.setSpacing(10);
        hBox.setStyle("-fx-background-color: #336699;");

        Button executeButton = new Button("Execute");
        executeButton.setPrefSize(100, 20);

        Button closeButton = new Button("Close");
        closeButton.setPrefSize(100, 20);

        TextArea commandLineArea = new TextArea();
        commandLineArea.setWrapText(true);

        TextArea outputArea = new TextArea();

        executeButton.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                execute(commandLineArea, outputArea);
            }
        });

        closeButton.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent actionEvent) {
                System.exit(0);
            }
        });

        outputArea.setWrapText(true);
        outputArea.setEditable(false);

        hBox.getChildren().addAll(executeButton, closeButton);

        VBox vbox = new VBox(20);
        vbox.setPadding(new Insets(25, 25, 25, 25));
        vbox.getChildren().addAll(hBox, commandLineArea, outputArea);

        Scene scene = new Scene(vbox, 950, 500);
        scene.addEventHandler(KeyEvent.KEY_PRESSED, (key) -> keyPressed(key.getCode(), commandLineArea, outputArea));
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void keyPressed(KeyCode key, TextArea command, TextArea output) {
        switch (key) {
            case F3:
                System.out.println("Debugging toggled");
                this.debugActive = !this.debugActive;
                break;
            case F8:
                System.out.println("Show Logfile");
                break;
            case F5:
                execute(command, output);
                break;
        }
    }

    private void execute(TextArea command, TextArea output) {
        System.out.println("--- execute ---");
        CryptoManager manager = new CryptoManager(this.debugActive);
        String resultText = "";

        if (command.getText().equals("show algorithm")) {
            resultText = showAlgorithms(manager);
        } else if (command.getText().matches("encrypt message \".*\" using .* and keyfile .*")) {
            resultText = encryptMessage(command.getText(), manager);
        } else if (command.getText().matches("decrypt message \".*\" using .* and keyfile .*")) {
            resultText = decryptMessage(command.getText(), manager);
        } else if (command.getText().matches("crack encrypted message \".*\" using .*")) {
            resultText = crackMessage(command.getText(), manager);
        } else if (command.getText().matches("register participant .* with type .*")) {
            resultText = registerParticipant(command.getText());
        } else if (command.getText().matches("create channel .* from .* to .*")) {
            resultText = createChannel(command.getText());
        } else if (command.getText().equals("show channel")) {
            resultText = showChannel();
        } else if (command.getText().matches("drop channel .*")) {
            resultText = dropChannel(command.getText());
        } else if (command.getText().matches("intrude channel .* by .*")) {
            resultText = intrudeChannel(command.getText());
        } else if (command.getText().matches("send message \".*\" from .* to .* using .* and keyfile .*")) {
            resultText = sendMessage(command.getText());
        } else {
            resultText = "Invalid Input";
        }

        output.setText(resultText);
    }

    private String showAlgorithms(CryptoManager manager) {
        String[] algorithms = manager.showAlgorithms();
        String result = "";
        for(String algo : algorithms) {
            result += algo;
            result += ", ";
        }
        result = result.substring(0, result.length() - 2);
        return result;
    }

    private String encryptMessage(String command, CryptoManager manager) {
        if (this.debugActive) {
            // create logfile
        }
        String[] splitted = command.split("\""); // [0]: encrypt message; [1]: [message]; [2]: using [algorithm] and keyfile [keyfile]
        String message = splitted[1];
        splitted = splitted[2].split(" "); // [0]: ""; [1]: using; [2]: [algorithm]; [3]: and; [4]: keyfile; [5]: [keyfile]
        String algorithm = splitted[2];
        String keyfile = splitted[5];
        return manager.encrypt(message, algorithm, keyfile);
    }

    private String decryptMessage(String command, CryptoManager manager) {
        if (this.debugActive) {
            // create logfile
        }
        String[] splitted = command.split("\""); // [0]: decrypt message; [1]: [message]; [2]: using [algorithm] and keyfile [keyfile]
        String message = splitted[1];
        splitted = splitted[2].split(" "); // [0]: ""; [1]: using; [2]: [algorithm]; [3]: and; [4]: keyfile; [5]: [keyfile]
        String algorithm = splitted[2];
        String keyfile = splitted[5];
        return manager.decrypt(message, algorithm, keyfile);
    }

    private String crackMessage(String command, CryptoManager manager) {
        String[] splitted = command.split("\""); // [0]: crack encrypted message; [1]: [message]; [2]: using [algorithm]
        String message = splitted[1];
        splitted = splitted[2].split(" "); // [0]: ""; [1]: using; [2]: [algorithm]
        String algorithm = splitted[2];
        return manager.crack(message, algorithm);
    }

    private String registerParticipant(String command) {
        return "";
    }

    private String createChannel(String command) {
        return "";
    }

    private String showChannel() {
        return "";
    }

    private String dropChannel(String command) {
        return "";
    }

    private String intrudeChannel(String command) {
        return "";
    }

    private String sendMessage(String command) {
        return "";
    }
}