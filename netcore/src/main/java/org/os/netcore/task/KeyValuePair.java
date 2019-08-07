package org.os.netcore.task;


public class KeyValuePair<K, V> {
    private K key;
    private V value;

    public KeyValuePair(K k, V v) {
        this.key = k;
        this.value = v;
    }

    public K getKey() {
        return key;
    }

    public void setKey(K key) {
        this.key = key;
    }

    public V getValue() {
        return value;
    }

    public void setValue(V value) {
        this.value = value;
    }
}
