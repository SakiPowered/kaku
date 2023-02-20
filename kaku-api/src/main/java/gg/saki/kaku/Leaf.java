package gg.saki.kaku;

public class Leaf<T> {

    private final Configuration configuration;
    private final Class<T> type;
    private final String path;

    public Leaf(Configuration configuration, Class<T> type, T value, String path){
        this.configuration = configuration;
        this.type = type;
        this.path = path;

        configuration.addDefault(path, value);
    }

    public T get(){
        return this.configuration.getObject(path, type);
    }

    public Leaf<T> set(T value){
        this.configuration.set(path, value);
        this.configuration.save();
        return this;
    }
}
