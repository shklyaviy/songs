package task.model;

public class Song {
    private String artistName;
    private String name;
    private int auditions;
    private int id;

    public Song(String artistName, String name, Integer auditions) {
        this.artistName = artistName;
        this.name = name;
        this.auditions = auditions;
    }

    public Song() {
        this.artistName = null;
        this.name = null;
        this.auditions = 0;

    }

    public String getArtistName() {
        return artistName;
    }

    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAuditions() {
        return auditions;
    }

    public void setAuditions(int auditors) {
        this.auditions = auditors;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void listen(int auditions) {this.auditions += auditions;}

    @Override
    public String toString() {
        return "Song{" +
                "artistName='" + artistName + '\'' +
                ", name='" + name + '\'' +
                ", auditions=" + auditions +
                ", id=" + id +
                '}';
    }
}
