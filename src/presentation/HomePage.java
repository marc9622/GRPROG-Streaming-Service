package presentation;

import java.util.List;

import javax.swing.JPanel;

import domain.Media;
import domain.User;

public class HomePage {
    
    private JPanel panel;

    public HomePage(User user, List<Media> allMedia, Application applicationListener) {
        panel = new JPanel();
    }

    public JPanel getPanel() {
        return panel;
    }

}
