import java.util.HashMap;
import java.util.Map;

public class Container<E extends Object> {
    private Map<String, E> storedObjects;

    public Container(){
        storedObjects = new HashMap<>();
    }

    public Container<E> addValue(String name, E value){
        storedObjects.put(name, value);
        return this;
    }

    public int getSize(){
        return storedObjects.size();
    }

    public Map<String, E> getStoredObjects() {
        return storedObjects;
    }

    public Object getByName(String name){
        return storedObjects.get(name);
    }
}
