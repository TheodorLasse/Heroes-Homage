package src.player;

import java.util.EnumMap;

public class PlayerResources {
    final private EnumMap<Resource, Integer> resources;

    public PlayerResources(){
        resources = new EnumMap<>(Resource.class);
        for (Resource r: Resource.values()){
            resources.put(r, 0);
        }
    }

    public void addResource(Resource resource, int amount){
        assert amount >= 0;
        resources.replace(resource, resources.get(resource) + amount);
    }

    public int getValue(Resource r){
        return resources.get(r);
    }
}
