package animalSimulation;

import javax.swing.*;


public class IconManager {
    String IMAGE_PATH = "./src/main/resources/images/";
    ImageIcon[][] animalIcons;

    public void load() {
        animalIcons = new ImageIcon[8][6];
        for (Facing f : Facing.values()) {
            String fStr = f.toString().toLowerCase();
            for (int energy = 0; energy <= 5; energy++) {
                String img_name = "animal_" + fStr + "_" + energy + ".png";
                animalIcons[f.ordinal()][energy] = new ImageIcon(IMAGE_PATH + img_name);
            }
        }
    }

    public ImageIcon getAnimalIcon(Facing facing, int energy) {
        return animalIcons[facing.ordinal()][energy];
    }

    public IconManager(){
        this.load();
    }
}
