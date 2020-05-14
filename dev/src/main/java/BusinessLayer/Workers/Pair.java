package BusinessLayer.Workers;

public class Pair<K, V> {

    private  K key;
    private  V val;

    public static <K, V> Pair<K, V> createPair(K element0, V element1) {
        return new Pair<K, V>(element0, element1);
    }

    public Pair(K element0, V element1) {
        this.key = element0;
        this.val = element1;
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

