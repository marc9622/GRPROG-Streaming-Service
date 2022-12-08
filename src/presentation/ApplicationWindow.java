package presentation;

import javax.swing.JFrame;

public class ApplicationWindow {
    
    private JFrame frame;

    private WelcomePage welcomePage;
    private HomePage homePage;
    private InformationPage informationPage;
    private PlaybackPage playbackPage;

    public ApplicationWindow() {
        welcomePage = new WelcomePage();
        homePage = new HomePage();
        informationPage = new InformationPage();
        playbackPage = new PlaybackPage();

        frame = welcomePage.getFrame();
    }

}
