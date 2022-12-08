package presentation;

import domain.ApplicationData;

public class Application {
    
    private ApplicationData data;
    private ApplicationWindow window;

    public Application() {
        data = new ApplicationData();
        window = new ApplicationWindow();
    }

}
