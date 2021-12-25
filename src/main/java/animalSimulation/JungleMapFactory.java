package animalSimulation;

import animalSimulation.gui.ImageManager;

import java.util.HashMap;
import java.util.Objects;

public class JungleMapFactory {
    HashMap<String, Class<? extends JungleMap>> mapTypes;
    private final ImageManager imageManager;

    public JungleMapFactory(ImageManager imageManager) {
        this.imageManager = imageManager;
        this.mapTypes = new HashMap<>();
        this.mapTypes.put("JungleMap", JungleMap.class);
        this.mapTypes.put("WrappedJungleMap", WrappedJungleMap.class);
    }

    public JungleMap createMap(String mapType, String mapName, HashMap<String, Integer> mapData) {
        int height = mapData.get("Height");
        height = Math.min(height, 30);

        int width = mapData.get("Width");
        width = Math.min(width, 100);

        int junglePercentage = mapData.get("Jungle %");
        junglePercentage = Math.max(junglePercentage, 0);
        junglePercentage = Math.min(junglePercentage, 80);

        int respawnThreshold = mapData.get("Respawn Threshold");
        int respawnCopies = mapData.get("Respawn Copies");
        int respawnRepeat = mapData.get("Respawn Repeat");

        int startEnergy = mapData.get("Start Energy");
        startEnergy = Math.max(startEnergy, 1);

        int moveEnergy = mapData.get("Move Energy");

        int initAnimals = mapData.get("Initial Animals");
        initAnimals = Math.max(initAnimals, 5);

        int plantEnergy = mapData.get("Plant Energy");

        if (Objects.equals(mapType, "JungleMap")) {
            JungleMap map = new JungleMap(
                    imageManager,
                    startEnergy,
                    moveEnergy,
                    initAnimals,
                    plantEnergy,
                    width,
                    height,
                    junglePercentage,
                    respawnThreshold,
                    respawnCopies,
                    respawnRepeat
            );
            map.name = mapName;
            return map;
        }

        if (Objects.equals(mapType, "WrappedJungleMap")) {
            WrappedJungleMap map = new WrappedJungleMap(
                    imageManager,
                    startEnergy,
                    moveEnergy,
                    initAnimals,
                    plantEnergy,
                    width,
                    height,
                    junglePercentage,
                    respawnThreshold,
                    respawnCopies,
                    respawnRepeat
            );
            map.name = mapName;
            return map;
        }
        return null;
    }
}
