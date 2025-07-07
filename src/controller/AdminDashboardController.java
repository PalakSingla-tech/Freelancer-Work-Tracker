package controller;

import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;
import service.AdminService;
import java.net.URL;
import java.util.ResourceBundle;


public class AdminDashboardController implements Initializable {
    @FXML
    private ImageView Exit;

    @FXML
    private Label Menu;

    @FXML
    private Label MenuBack;

    @FXML
    private AnchorPane slider;

    @FXML private Label activeUserLabel;

    private final AdminService adminService = new AdminService();

    @Override
    public void initialize(URL location, ResourceBundle rb)
    {
        int activeCount = adminService.getActiveUserCount();
        activeUserLabel.setText("Active Users: " + activeCount);
        Exit.setOnMouseClicked(event -> {
            System.exit(0);
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
