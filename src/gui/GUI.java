package gui;

import companyNetwork.ChannelManager;
import companyNetwork.CompanyNetwork;
import cryptoManager.CryptoManager;
import logger.Logger;

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
import persistence.HSQLDB;

public class GUI extends Application {
    private boolean debugActive = false;

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

        TextArea outputArea = new TextArea();

        executeButton.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                execute(commandLineArea, outputArea);
            }
        });

        closeButton.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent actionEvent) {
                shutdownDB();
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
        System.out.println("--- execute ---");
        CryptoManager cryptoManager = new CryptoManager();
//        ChannelManager channelManager = new ChannelManager();
        String resultText = "";

        if (getCommand(command).equals("show algorithm")) {
            resultText = showAlgorithms(cryptoManager);
        } else if (getCommand(command).matches("encrypt message \".*\" using .* and keyfile .*")) {
            resultText = encryptMessage(getCommand(command), cryptoManager);
        } else if (getCommand(command).matches("decrypt message \".*\" using .* and keyfile .*")) {
            resultText = decryptMessage(getCommand(command), cryptoManager);
        } else if (getCommand(command).matches("crack encrypted message \".*\" using .*")) {
            resultText = crackMessage(getCommand(command), cryptoManager);
        } else if (getCommand(command).matches("register participant .* with type .*")) {
            resultText = registerParticipant(getCommand(command));
        } else if (getCommand(command).matches("create channel .* from .* to .*")) {
            resultText = createChannel(getCommand(command));
        } else if (getCommand(command).equals("show channel")) {
            resultText = showChannel();
        } else if (getCommand(command).matches("drop channel .*")) {
            resultText = deleteChannel(getCommand(command));
        } else if (getCommand(command).matches("intrude channel .* by .*")) {
            resultText = intrudeChannel(getCommand(command));
        } else if (getCommand(command).matches("send message \".*\" from .* to .* using .* and keyfile .*")) {
            resultText = sendMessage(getCommand(command));
        } else {
            resultText = "Invalid Input";
        }

        output.setText(resultText);
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

        initTestBranches();

        return ChannelManager.instance.create(channelName, participant01, participant02);
    }

    private String showChannel() {
        return ChannelManager.instance.showAllChannels();
    }

    private String deleteChannel(String command) {
        String[] splitted = command.split(" "); // [0]: crack encrypted message; [1]: [message]; [2]: using [algorithm]
        return ChannelManager.instance.deleteChannel(splitted[2]);
    }

    private String intrudeChannel(String command) {
        return "";
    }

    private String sendMessage(String command) {
        return "";
    }

    private void setupDB() {
        HSQLDB.instance.setupConnection();

        HSQLDB.instance.dropTableParticipants();
        HSQLDB.instance.dropTableTypes();

        HSQLDB.instance.createTableTypes();
        HSQLDB.instance.createTableAlgorithms();
        HSQLDB.instance.createTableParticipants();
        HSQLDB.instance.createTableChannel();
        HSQLDB.instance.createTableMessages();

        HSQLDB.instance.insertDataTableTypes("normal");
        HSQLDB.instance.insertDataTableTypes("intruder");

        HSQLDB.instance.insertDataTableAlgorithms("rsa");
        HSQLDB.instance.insertDataTableAlgorithms("shift");
    }

    private void shutdownDB() {
        HSQLDB.instance.shutdown();
    }

    private void initTestBranches() {
        System.out.println(CompanyNetwork.instance.registerParticipant("branch_hkg", "normal"));
        System.out.println(CompanyNetwork.instance.registerParticipant("branch_wuh", "normal"));
    }
}