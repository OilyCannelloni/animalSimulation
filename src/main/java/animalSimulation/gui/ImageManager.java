package animalSimulation.gui;

import animalSimulation.Facing;
import javafx.scene.image.Image;

import java.util.HashMap;

public class ImageManager {
    public static String
            IMAGE_PATH = "file:.\\src\\main\\resources\\images\\",
            mapBackground = "jungle_background.png",
            trackingCircle = "tracked.png",
            highlightCircle = "highlighted.png";

    public static Image
            trackingCircleImage = new Image(IMAGE_PATH + trackingCircle),
            highlightCircleImage = new Image(IMAGE_PATH + highlightCircle);

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

        images.put(mapBackground, new Image(IMAGE_PATH + mapBackground));
        images.put("plant_1.png", new Image(IMAGE_PATH + "plant_1.png"));
        images.put("plant_2.png", new Image(IMAGE_PATH + "plant_2.png"));
    }

    public Image getImage(String relativePath) {
        return images.get(relativePath);
    }
}
