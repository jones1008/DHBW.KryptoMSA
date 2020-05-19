package gui;

import companyNetwork.*;
import cryptoManager.CryptoManager;
import logger.Logger;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import persistence.*;

public class GUI extends Application {
    private boolean debugActive = false;
    private static TextArea outputArea;

    public void start(Stage primaryStage) {
        primaryStage.setTitle("MSA | Mosbach Security Agency");
        setupDB();
        CompanyNetwork.instance.initMaps();

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

        outputArea = new TextArea();

        executeButton.setOnAction(event -> execute(commandLineArea, outputArea));

        closeButton.setOnAction(actionEvent ->
        {
            shutdownDB();
            System.exit(0);
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

    public static void setOutputText(String text) {
        String outputText = outputArea.getText();
        if (outputText.equals("")) {
            outputText = text;
        } else {
            outputText += "\n" + text;
        }
        outputArea.setText(outputText);
    }

    private void keyPressed(KeyCode key, TextArea command, TextArea output) {
        switch (key) {
            case F3:
                System.out.println("Debugging toggled");
                this.debugActive = !this.debugActive;
                if (this.debugActive) {
                    output.setText("Debugging on");
                } else {
                    output.setText("Debugging off");
                }
                break;
            case F8:
                System.out.println("Show Logfile");
                output.setText(Logger.getContentOfLatestLogfile());
                break;
            case F5:
                execute(command, output);
                break;
        }
    }

    private void execute(TextArea command, TextArea output) {
        output.setText("");
        System.out.println("--- execute ---");
        CryptoManager cryptoManager = new CryptoManager();
        String resultText;
        String commandText = getCommand(command);

        if (commandText.equals("show algorithm")) {
            resultText = showAlgorithms(cryptoManager);
        } else if (commandText.matches("encrypt message \".*\" using .* and keyfile .*")) {
            resultText = encryptMessage(commandText, cryptoManager);
        } else if (commandText.matches("decrypt message \".*\" using .* and keyfile .*")) {
            resultText = decryptMessage(commandText, cryptoManager);
        } else if (commandText.matches("crack encrypted message \".*\" using .*")) {
            resultText = crackMessage(commandText, cryptoManager);
        } else if (commandText.matches("register participant .* with type .*")) {
            resultText = registerParticipant(commandText);
        } else if (commandText.matches("create channel .* from .* to .*")) {
            resultText = createChannel(commandText);
        } else if (commandText.equals("show channel")) {
            resultText = showChannel();
        } else if (commandText.matches("drop channel .*")) {
            resultText = deleteChannel(commandText);
        } else if (commandText.matches("intrude channel .* by .*")) {
            resultText = intrudeChannel(commandText);
        } else if (commandText.matches("send message \".*\" from .* to .* using .* and keyfile .*")) {
            resultText = sendMessage(commandText);
        } else if (commandText.matches("show postbox .*")) {
            resultText = showPostbox(commandText);
        }
        else {
            resultText = "Invalid Input";
        }

        setOutputText(resultText);
    }

    private String getCommand(TextArea command) {
        return command.getText().trim();
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
        String[] splitted = command.split("\""); // [0]: encrypt message; [1]: [message]; [2]: using [algorithm] and keyfile [keyfile]
        String message = splitted[1];
        splitted = splitted[2].split(" "); // [0]: ""; [1]: using; [2]: [algorithm]; [3]: and; [4]: keyfile; [5]: [keyfile]
        String algorithm = splitted[2];
        String keyfile = splitted[5];

        // create logfile
        if (this.debugActive) {
            manager.setLogger(new Logger("encrypt", algorithm));
        }

        return manager.encrypt(message, algorithm, keyfile);
    }

    private String decryptMessage(String command, CryptoManager manager) {
        String[] splitted = command.split("\""); // [0]: decrypt message; [1]: [message]; [2]: using [algorithm] and keyfile [keyfile]
        String message = splitted[1];
        splitted = splitted[2].split(" "); // [0]: ""; [1]: using; [2]: [algorithm]; [3]: and; [4]: keyfile; [5]: [keyfile]
        String algorithm = splitted[2];
        String keyfile = splitted[5];

        // create logfile
        if (this.debugActive) {
            manager.setLogger(new Logger("decrypt", algorithm));
        }

        return manager.decrypt(message, algorithm, keyfile);
    }

    private String crackMessage(String command, CryptoManager manager) {
        String[] splitted = command.split("\""); // [0]: crack encrypted message; [1]: [message]; [2]: using [algorithm]
        String message = splitted[1];
        splitted = splitted[2].split(" "); // [0]: ""; [1]: using; [2]: [algorithm]
        String algorithm = splitted[2];
        return manager.crack(message, algorithm);
    }

    private String registerParticipant(String command) { // command: register participant [name] with type [type]
        String[] splitted = command.split(" "); // 0: register, 1: participant, 2: [name], 3: with, 4: type, 5: [type]
        String name = splitted[2];
        String type = splitted[5];
        if (!type.equals("normal") && !type.equals("intruder")) {
            return "Invalid type";
        }

        return CompanyNetwork.instance.registerParticipant(name, type);
    }

    private String createChannel(String command) { // command: create channel .* from .* to .*
        String[] splitted = command.split(" ");
        String channelName = splitted[2];
        String participant01 = splitted[4];
        String participant02 = splitted[6];

        return ChannelManager.instance.create(channelName, participant01, participant02);
    }

    private String showChannel() {
        return ChannelManager.instance.showAllChannels();
    }

    private String deleteChannel(String command) {
        String[] splitted = command.split(" "); // [0]: crack encrypted message; [1]: [message]; [2]: using [algorithm]
        return CompanyNetwork.instance.deleteChannel(splitted[2]);
    }

    private String intrudeChannel(String command) {
        String[] splitted = command.split(" ");
        String channelName = splitted[2];
        String participantName = splitted[4];
        return ChannelManager.instance.intrudeChannel(channelName, participantName);
    }

    private String sendMessage(String command) { // command: send message ".*" from .* to .* using .* and keyfile .*
        String[] splitted = command.split("\"");
        String message = splitted[1];
        splitted = splitted[2].trim().split(" "); // 0: from, 1: [from], 2: to, 3: [to], 4: using, 5: [algorithm], 6: and, 7: keyfile, 8: [keyfile]
        String participant01 = splitted[1];
        String participant02 = splitted[3];
        String algorithm = splitted[5];
        String keyfile = splitted[8];

        return ChannelManager.instance.sendMessage(message, participant01, participant02, algorithm, keyfile);
    }

    private String showPostbox(String command) {
        String[] splitted = command.split(" ");
        String name = splitted[2];
        return DBPostbox.instance.showPostbox(name);
    }

    private void setupDB() {
        DB.instance.setupConnection();

        DBParticipant.instance.dropTableParticipants();
        DBType.instance.dropTableTypes();

        DBType.instance.createTableTypes();
        DBAlgorithm.instance.createTableAlgorithms();
        DBParticipant.instance.createTableParticipants();
        DBChannel.instance.createTableChannel();
        DBMessage.instance.createTableMessages();

        DBType.instance.insertDataTableTypes("normal");
        DBType.instance.insertDataTableTypes("intruder");

        DBAlgorithm.instance.insertDataTableAlgorithms("rsa");
        DBAlgorithm.instance.insertDataTableAlgorithms("shift");
    }

    private void initParticipantsAndChannels() {
        CompanyNetwork.instance.registerParticipant("branch_hkg", "normal");
        CompanyNetwork.instance.registerParticipant("branch_cpt", "normal");
        CompanyNetwork.instance.registerParticipant("branch_sfo", "normal");
        CompanyNetwork.instance.registerParticipant("branch_syd", "normal");
        CompanyNetwork.instance.registerParticipant("branch_wuh", "normal");
        CompanyNetwork.instance.registerParticipant("msa", "intruder");

        ChannelManager.instance.create("hkg_wuh", "branch_hkg", "branch_wuh");
        ChannelManager.instance.create("hkg_cpt", "branch_hkg", "branch_cpt");
        ChannelManager.instance.create("cpt_syd", "branch_cpt", "branch_syd");
        ChannelManager.instance.create("syd_sfo", "branch_syd", "branch_sfo");
    }

    private void shutdownDB() {
        DB.instance.shutdown();
    }
}