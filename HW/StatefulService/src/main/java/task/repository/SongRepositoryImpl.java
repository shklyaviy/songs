package task.repository;

import org.springframework.stereotype.Repository;
import task.error.SongNotFoundException;
import task.model.ListenRequest;
import task.model.Song;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Repository
public class SongRepositoryImpl implements SongRepository {
    private final Map<Integer, Song> data = new ConcurrentHashMap<>();
    private final AtomicInteger autoId = new AtomicInteger(0);

    @Override
    public List<Song> findAll() {
        return new ArrayList<>(data.values());
    }

    @Override
    public Song getSongById(Integer id) {
        Song result = data.get(id);
        if (result == null) {
            throw new SongNotFoundException("error.message");
        }
        return result;
    }

    @Override
    public Song save(Song song) {
        int id = autoId.incrementAndGet();
        song.setId(id);
        data.put(id, song);
        return song;
    }

    @Override
    public Song updateSongById(Integer id, Song song) {
        Song oldValue = getSongById(id);
        song.setId(id);
        data.put(id, song);
        return song;
    }

    @Override
    public Song deleteSongById(Integer id) {
        Song oldValue = getSongById(id);
        Song result = data.remove(id);
        if (result == null) {
            throw new SongNotFoundException("error.message");
        }
        return result;
    }
    @Override
    public List<Song> getSortedSongsByAuditions(Integer limit) {
        List<Song> songs = findAll();
        songs.sort(new Comparator<Song>() {
            @Override
            public int compare(Song song1, Song song2) {
                return song2.getAuditions() - song1.getAuditions();
            }
        });
        if (songs.size() > limit) {
            songs = songs.subList(0, limit);
        }
        return songs;
    }


    @Override
    public List<Song> listenSongByIds(ListenRequest listenRequest) {
        List<Song> songs = new ArrayList<Song>();
        List<Integer> ids = listenRequest.getSongs();
        Integer auditions = listenRequest.getAuditions();
        for (Integer id : ids) {
            getSongById(id).listen(auditions);
            songs.add(getSongById(id));
        }
        return songs;
    }

    @Override
    public Song listenSongById(Integer id, ListenRequest listenRequest) {
        Integer auditions = listenRequest.getAuditions();
        getSongById(id).listen(auditions);
        return getSongById(id);
    }
}