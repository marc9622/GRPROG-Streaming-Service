package presentation;

import javax.swing.JPanel;

import domain.User;

public class HomePage {
    
    public final JPanel panel;

    public HomePage(User user) {
        panel = new JPanel();
    }

    public JPanel getPanel() {
        return panel;
    }

}
