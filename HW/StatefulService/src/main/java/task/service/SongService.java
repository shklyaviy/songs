package task.service;

import task.model.ListenRequest;
import task.model.Song;

import java.util.List;

public interface SongService {
    List<Song> getSongs();

    Song getSongById(Integer id);

    Song save(Song song);

    Song updateSongById(Integer id, Song song);

    Song deleteSongById(Integer id);

    List<Song> getSortedSongsByAuditions(Integer limit);

    List<Song> listenSongByIds(ListenRequest listenRequest);
    Song listenSongById(Integer id, ListenRequest listenRequest);


//    Song doHandleSong(Song song);
}