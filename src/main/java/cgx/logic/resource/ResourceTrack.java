package cgx.logic.resource;

/**
 * 资源获得/消费来源
 */
public enum ResourceTrack {
    ;

    ResourceTrack(int id) {
        _id = id;
    }

    public int getId() {
        return _id;
    }

    private final int _id;
}
