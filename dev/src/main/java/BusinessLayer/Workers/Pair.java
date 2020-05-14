package BusinessLayer.Workers;

public class Pair<K, V> {

    private  K key;
    private  V val;

    public Pair(K keyElement, V valElement) {
        this.key = keyElement;
        this.val = valElement;
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

