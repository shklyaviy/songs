package task.model;

import java.util.List;

public class ListenRequest {
    private Integer auditions;

    private List<Integer> songs;

    public Integer getAuditions() {
        return auditions;
    }

    public void setAuditions(Integer auditions) {
        this.auditions = auditions;
    }

    public List<Integer> getSongs() {
        return songs;
    }

    public void setSongs(List<Integer> songs) {
        this.songs = songs;
    }
}
