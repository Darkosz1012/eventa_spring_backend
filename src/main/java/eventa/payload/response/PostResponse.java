package eventa.payload.response;

public class PostResponse {

    private String content;

    private String username;

    private String createdAt;

    public PostResponse(String content, String username, String createdAt) {
        this.content = content;
        this.username = username;
        this.createdAt = createdAt;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
}
