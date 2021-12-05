package animalSimulation;

import javax.swing.*;
import java.awt.*;

public class BGPanel extends JPanel {
    private final Image img;

    public BGPanel(ImageIcon icon) {
        this(icon.getImage());
    }

    public BGPanel(Image img) {
        this.img = img;
        Dimension size = new Dimension(img.getWidth(null), img.getHeight(null));
        setPreferredSize(size);
        setMinimumSize(size);
        setMaximumSize(size);
        setSize(size);
        setLayout(null);
    }

    public void paintComponent(Graphics g) {
        g.drawImage(this.img, 0, 0, null);
    }
}
