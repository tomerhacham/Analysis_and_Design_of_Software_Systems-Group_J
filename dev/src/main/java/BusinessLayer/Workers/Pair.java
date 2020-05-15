package BusinessLayer.Workers;

public class Pair<K, V> {

    private  K key;
    private  V value;

    public Pair(K keyElement, V valElement) {
        this.key = keyElement;
        this.value = valElement;
    }

    public K getKey() {
        return key;
    }

    public V getValue() {
        return val;
    }

    public void setKey(K key) {
        this.key = key;
    }

    public void setValue(V val) {
        this.val = val;
    }
}

