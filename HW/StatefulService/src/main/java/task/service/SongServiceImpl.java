package task.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import task.model.ListenRequest;
import task.model.Song;
import task.repository.SongRepository;

import java.util.List;

@Service
public class SongServiceImpl implements SongService {
    @Autowired
    private SongRepository repository;

    @Override
    public List<Song> getSongs() {
        return repository.findAll();
    }

    @Override
    public Song getSongById(Integer id) {
        return repository.getSongById(id);
    }

    @Override
    public Song save(Song song) {
        return repository.save(song);
    }

    @Override
    public Song updateSongById(Integer id, Song song) {
        return repository.updateSongById(id, song);
    }

    @Override
    public Song deleteSongById(Integer id) {
        return repository.deleteSongById(id);
    }

    @Override
    public List<Song> getSortedSongsByAuditions(Integer limit) {
        return repository.getSortedSongsByAuditions(limit);
    }

    @Override
    public List<Song> listenSongByIds(ListenRequest listenRequest) {
        return repository.listenSongByIds(listenRequest);
    }
    @Override
    public Song listenSongById(Integer id, ListenRequest listenRequest) {
        return repository.listenSongById(id, listenRequest);
    }

//    @Override
//    public Song doHandleMessage(Song song) {
//        song.setValue(song.getValue().toUpperCase());
//        return song;
//    }
}