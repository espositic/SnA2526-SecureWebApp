package it.uniba.model;

public class Post {
    private String author;
    private String filename;
    private String date;
    private String content;

    public Post() {}
    // Costruttore, Getter e Setter standard
    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }
    public String getFilename() { return filename; }
    public void setFilename(String filename) { this.filename = filename; }
    public String getDate() { return date; }
    public void setDate(String date) { this.date = date; }
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
}