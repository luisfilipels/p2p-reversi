package readData;

import gameLogic.GameControllerSingleton;
import javafx.scene.text.Text;
import networking.NetworkHandlerSingleton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.stage.Stage;
import utils.SessionDataSingleton;

import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Controller {

    @FXML
    private TextField userNameField;

    @FXML
    private Button confirmButton;

    @FXML
    private TextField remoteMachineAddressField;

    @FXML
    private ToggleButton whiteToggle;

    @FXML
    private ToggleButton aidToggle;

    @FXML
    private Text alertText;

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

        NetworkHandlerSingleton handler = NetworkHandlerSingleton.getHandler();
        int countAttempts = 0;
        String textToSet = "";
        while (countAttempts < 2) {
            try {
                handler.startRMI();
                break;
            } catch (RemoteException e) {
                textToSet = "Falha na conexão!";
            } catch (NotBoundException e) {
                textToSet = "Servidor de nomes não existe no endereço informado!";
            } catch (MalformedURLException e) {
                countAttempts = 3;
                textToSet = "URL inválida!";
            }
            countAttempts++;
        }
        if (countAttempts >= 2) {
            alertText.setText(textToSet);
            return;
        }

        if (whiteToggle.isSelected()) {
            userData.setUserColor('w');
            GameControllerSingleton.getInstance().startTurn();
        } else {
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

        handler.startReceiver();

        Node source = (Node) event.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    }

}
