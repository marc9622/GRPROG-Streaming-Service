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

        frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(true);
        frame.setSize(800, 600);
        frame.add(welcomePage.getPanel());
        frame.setVisible(true);
    }

}
