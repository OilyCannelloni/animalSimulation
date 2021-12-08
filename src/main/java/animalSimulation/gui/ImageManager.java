package animalSimulation.gui;

import animalSimulation.Facing;
import javafx.scene.image.Image;

import java.util.HashMap;

public class ImageManager {
    String IMAGE_PATH = "file:.\\src\\main\\resources\\images\\";
    HashMap<String, Image> images;

    public ImageManager() {
        this.load();
    }

    public void load() {
        images = new HashMap<>();
        for (Facing f : Facing.values()) {
            String fStr = f.toString().toLowerCase();
            for (int energy = 0; energy <= 5; energy++) {
                String name = "animal_" + fStr + "_" + energy + ".png";
                String path = IMAGE_PATH + name;
                images.put(name, new Image(path));
            }
        }
    }

    public Image getImage(String relativePath) {
        return images.get(relativePath);
    }
}
