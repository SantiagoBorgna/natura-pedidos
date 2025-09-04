package app;

import app.ui.MainWindow;

import javax.swing.*;
import java.awt.*;

public class App {
    public static Image logo = Toolkit.getDefaultToolkit().getImage(App.class.getResource("/images/logo-natura.jpg"));

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MainWindow ventana = new MainWindow();
            ventana.setVisible(true);
        });
    }
}