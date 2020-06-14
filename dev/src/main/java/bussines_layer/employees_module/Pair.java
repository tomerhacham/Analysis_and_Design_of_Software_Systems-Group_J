package bussines_layer.employees_module;

public class Pair<K, V> {
    private  K key;
    private  V value;

    public Pair(K keyElement, V valElement) {
        this.key = keyElement;
        this.value = valElement;
    }

    public K getMorning() {
        return key;
    }

    public V getNight() {
        return value;
    }

    public void setMorning(K key) {
        this.key = key;
    }

    public void setNight(V val) {
        this.value = val;
    }
}

