package escan.com.networkingLibraryDemo.RecyclerViewDemo.NetowrkingDemo.recyclerViewDemo;

public class DataModel {

    private String name;
    private String version;
    private byte id_;
    private int image;


    public DataModel(String name, String version, byte id_, int image) {
        this.name = name;
        this.version = version;
        this.id_ = id_;
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public String getVersion() {
        return version;
    }

    public byte getId() {
        return id_;
    }

    public int getImage() {
        return image;
    }

}
