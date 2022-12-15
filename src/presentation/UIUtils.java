package presentation;

import java.awt.Dimension;
import java.awt.Image;

import javax.swing.Box.Filler;
import javax.swing.ImageIcon;
import javax.swing.JPanel;

final class UIUtils {
    private UIUtils() {}
    
    static final int DEFAULT_WINDOW_WIDTH = 1200;
    static final int DEFAULT_WINDOW_HEIGHT = 800;

    final static class Fillers {
        private Fillers() {}

        private static final Filler fillerHelper(int minWidth, int minHeight, int prefWidth, int prefHeight, int maxWidth, int maxHeight) {
            return new Filler(new Dimension(minWidth, minHeight), new Dimension(prefWidth, prefHeight), new Dimension(maxWidth, maxHeight));
        }

        static final Filler HORIZONTAL_SMALL() {
            return fillerHelper(0, 0, 25, 0, 75, 0);
        }
    
        static final Filler HORIZONTAL_MEDIUM() {
            return fillerHelper(0, 0, 75, 0, 200, 0);
        }
    
        static final Filler HORIZONTAL_LARGE() {
            return fillerHelper(0, 0, 100, 0, 250, 0);
        }
    
        static final Filler VERTICAL_SMALL() {
            return fillerHelper(0, 0, 0, 25, 0, 75);
        }
    
        static final Filler VERTICAL_MEDIUM() {
            return fillerHelper(0, 0, 0, 75, 0, 200);
        }
    
        static final Filler VERTICAL_LARGE() {
            return fillerHelper(0, 0, 0, 100, 0, 250);
        }
        
    }

    final static class Images {
        private Images() {}

        static final ImageIcon BACKGROUND() {
            return new ImageIcon("./Images/Background.png");
        }
        
        static final ImageIcon BUTTON() {
            return new ImageIcon("./Images/Button.png");
        }
    
    }

    final static class Fonts {
        private Fonts() {}

        static final float SIZE_SMALL  = 12;
        static final float SIZE_MEDIUM = 16;
        static final float SIZE_LARGE  = 32;
        static final float SIZE_TITLE  = 64;
    }

    static class BackgroundPanel extends JPanel {

        Image image;

        BackgroundPanel(ImageIcon imageIcon) {
            image = imageIcon.getImage();
        }

        protected void paintComponent(java.awt.Graphics g) {
            super.paintComponent(g);
            g.drawImage(image, 0, 0, this.getWidth(), this.getHeight(), null);
        }

    }

}