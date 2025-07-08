package controller;

import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;


public class UserDashboardController implements Initializable {
    @FXML
    private ImageView Exit;

    @FXML
    private Label Menu;

    @FXML
    private Label MenuBack;

    @FXML
    private AnchorPane slider;

    @FXML private Label logout;

    @Override
    public void initialize(URL location, ResourceBundle rb)
    {
        Exit.setOnMouseClicked(event -> {
            System.exit(0);
        });

        logout.setOnMouseClicked(event -> {
            try {
                // Load the login/register screen
                Parent root = FXMLLoader.load(getClass().getResource("/views/Login.fxml"));
                Stage stage = new Stage();
                stage.setTitle("Login/Register");
                stage.setScene(new Scene(root));
                stage.show();

                // Close the current dashboard window
                Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                currentStage.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        slider.setTranslateX(-176);
        Menu.setOnMouseClicked(event -> {
            TranslateTransition t = new TranslateTransition();
            t.setDuration(Duration.seconds(0.4));
            t.setNode(slider);

            t.setToX(0);
            t.play();

            slider.setTranslateX(-176);
            t.setOnFinished((ActionEvent e) -> {
                Menu.setVisible(false);
                MenuBack.setVisible(true);
            });
        });

        MenuBack.setOnMouseClicked(event -> {
            TranslateTransition t = new TranslateTransition();
            t.setDuration(Duration.seconds(0.4));
            t.setNode(slider);

            t.setToX(-176);
            t.play();

            slider.setTranslateX(0);
            t.setOnFinished((ActionEvent e) -> {
                Menu.setVisible(true);
                MenuBack.setVisible(false);
            });
        });
    }
}
