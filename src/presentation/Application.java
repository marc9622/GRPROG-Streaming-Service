package presentation;

import java.io.IOException;

import domain.ApplicationData;
import domain.MediaParsing.InvalidStringFormatException;
import presentation.WelcomePage.UserListButtons.UserButton;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Application implements ActionListener {
    
    private final ApplicationData data;
    private final ApplicationWindow window;

    public Application() {
        data = new ApplicationData();
        window = new ApplicationWindow();

        // Read the media files
        try {
            data.readMedia();
        }
        
        // Catch the exceptions
        catch (IOException | InvalidStringFormatException e) {
            System.out.println(e.getMessage());
            
            // TODO: Could not read files
        }
        
        window.changeToPage(ApplicationWindow.Page.WELCOME, data.getUsers(), this);
    }

    public void actionPerformed(ActionEvent e) {
        if(e.getSource() instanceof UserButton button) {
            String username = button.username;
            System.out.println(username);
        }
        else throw new UnsupportedOperationException("Not supported yet.");
    }

}
