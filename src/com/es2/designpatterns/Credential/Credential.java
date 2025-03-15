package src.com.es2.designpatterns.Credential;

public class Credential {
    private String id;
    private String name;
    private String value;
    private Date timestamp;
    // private Map<String, Object> metadata;
    
    public Credential(String id, String name, String value) {
        this.id = id;
        this.name = name;
        this.value = value;
        this.timestamp = new Date();
        this.metadata = new HashMap<>();
    }
    
    public String getId() {
        return id;
    }
    
    public String getName() {
        return name;
    }
    
    public String getValue() {
        return value;
    }
    
    public Date getTimestamp() {
        return timestamp;
    }
    
    public Object getMetadata(String key) {
        return metadata.get(key);
    }
    
    public void setMetadata(String key, Object value) {
        metadata.put(key, value);
    }
    
    @Override
    public String toString() {
        return "Credential{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }
}
