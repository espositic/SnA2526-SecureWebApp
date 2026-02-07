package it.uniba.model;

public class Post {
    private String email;
    private String filename;
    private String content;
    private String date;

    public Post(String email, String filename, String content, String date) {
        this.email = email;
        this.filename = filename;
        this.content = content;
        this.date = date;
    }

    public String getEmail() {
        return email;
    }

    public String getFilename() {
        return filename;
    }

    public String getContent() {
        return content;
    }

    public String getDate() {
        return date;
    }
}