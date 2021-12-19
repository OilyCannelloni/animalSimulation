package animalSimulation;

import java.util.HashMap;

public class JungleMapFactory {
    HashMap<String, Class<? extends JungleMap>> mapTypes;

    public JungleMapFactory() {
        this.mapTypes = new HashMap<>();
        this.mapTypes.put("JungleMap", JungleMap.class);
        this.mapTypes.put("WrappedJungleMap", WrappedJungleMap.class);
    }


    public JungleMap createMap(String mapType, HashMap<String, Integer> mapData) {
        Class<? extends JungleMap> type = this.mapTypes.get(mapType);
        int height = mapData.get("Height");
        height = Math.min(height, 30);

        int width = mapData.get("Width");
        width = Math.min(width, 100);

        int junglePercentage = mapData.get("Jungle %");
        junglePercentage = Math.max(junglePercentage, 0);
        junglePercentage = Math.min(junglePercentage, 80);

        int respawnThreshold = 0;

        int respawnCopies = 0;

        int respawnRepeat = 0;

        JungleMap map = new JungleMap(
                width,
                height,
                junglePercentage,
                respawnThreshold,
                respawnCopies,
                respawnRepeat
        );

        return type.cast(map);
    }
}
