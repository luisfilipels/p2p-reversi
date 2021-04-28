package readData;

import gameLogic.GameControllerSingleton;
import networking.NetworkHandlerSingleton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.stage.Stage;
import utils.SessionDataSingleton;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Controller {

    @FXML
    private TextField userNameField;

    @FXML
    private TextField inPort;

    @FXML
    private TextField outPort;

    @FXML
    private Button confirmButton;

    @FXML
    private TextField remoteMachineAddressField;

    @FXML
    private ToggleButton whiteToggle;

    @FXML
    private ToggleButton aidToggle;

    private static final String PATTERN =
            "^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
                    "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
                    "([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\." +
                    "([01]?\\d\\d?|2[0-4]\\d|25[0-5])$";

    // Got this function from StackOverFlow
    public static boolean validate(final String ip){
        Pattern pattern = Pattern.compile(PATTERN);
        Matcher matcher = pattern.matcher(ip);
        return matcher.matches();
    }

    @FXML
    public void handleConfirmButton (ActionEvent event) {
        String remoteIPString = remoteMachineAddressField.getText().trim();

        if (userNameField.getText().equals("")) return;
        if (!remoteIPString.equals("localhost")) {
            if (remoteIPString.equals("") || !validate(remoteIPString)) return;
        }

        SessionDataSingleton userData = SessionDataSingleton.getInstance();

        userData.setUserName(userNameField.getText().trim());
        userData.setRemoteAddress(remoteIPString);

        if (whiteToggle.isSelected()) {
            if (outPort.getText().trim().equals("")) userData.setSendPort(1024);
            else userData.setSendPort(Integer.parseInt(outPort.getText().trim()));

            if (inPort.getText().trim().equals("")) userData.setReceivePort(1025);
            else userData.setReceivePort(Integer.parseInt(inPort.getText().trim()));

            userData.setUserColor('w');
            GameControllerSingleton.getInstance().startTurn();
        } else {

            if (outPort.getText().trim().equals("")) userData.setSendPort(1025);
            else userData.setSendPort(Integer.parseInt(outPort.getText().trim()));

            if (inPort.getText().trim().equals("")) userData.setReceivePort(1024);
            else userData.setReceivePort(Integer.parseInt(inPort.getText().trim()));

            userData.setUserColor('b');
            GameControllerSingleton.getInstance().localEndTurn();
        }

        if (aidToggle.isSelected()) {
            userData.setAidActivated(true);
            GameControllerSingleton.getInstance().getViewController().helpToggle.setSelected(true);
        } else {
            userData.setAidActivated(false);
            GameControllerSingleton.getInstance().getViewController().helpToggle.setSelected(false);
        }

        NetworkHandlerSingleton handler = NetworkHandlerSingleton.getHandler();
        handler.startRMI();

        Node source = (Node) event.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    }

}
