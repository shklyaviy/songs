package task.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import task.error.SongException;
import task.error.SongNotFoundException;
import task.error.SongValidationException;
import task.model.Error;
import task.model.ListenRequest;
import task.model.Song;
import task.service.SongService;

import java.util.*;

@RestController
public class SongControllerImpl implements SongController {
    @Autowired
    private SongService service;

    @Override
    public List<Song> getSongs() {
        return service.getSongs();
    }

    @Override
    public Song getSongById(Integer id) {
        checkId(id);
        return service.getSongById(id);
    }

    @Override
    public Song createSong(Song song) {
        checkSong(song);
        return service.save(song);
    }

    @Override
    public Song updateSongById(Integer id, Song song) {
        checkId(id);
        checkSong(song);
        return service.updateSongById(id, song);
    }

    @Override
    public Song deleteSongById(Integer id) {
        checkId(id);
        return service.deleteSongById(id);
    }

    @Override
    public List<Song> getSortedSongsByAuditions(Integer limit) {
        return service.getSortedSongsByAuditions(limit);
    }

    @Override
    public List<Song> listenSongByIds(ListenRequest listenRequest) {
        List<Integer> ids = listenRequest.getSongs();
        for (Integer id : ids) {
            checkId(id);
        }
        checkAditions(listenRequest.getAuditions());
        return service.listenSongByIds(listenRequest);
    }

    @Override
    public Song listenSongById(Integer id, ListenRequest listenRequest) {
        checkAditions(listenRequest.getAuditions());
        checkId(id);
        return service.listenSongById(id, listenRequest);
    }


    @ExceptionHandler(SongValidationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Error handleValidationException(SongValidationException ex) {
        return new Error(ex.getMessage());
    }

    @ExceptionHandler(SongNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Error handleNotFoundException(SongNotFoundException ex) {
        return new Error(ex.getMessage());
    }

    @ExceptionHandler(SongException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Error handleNotFoundException(SongException ex) {
        return new Error(ex.getMessage());
    }

    void checkId(Integer id) {
        if (id == null || id < 1) {
            throw new SongValidationException("error.message");
        }
    }

    void checkAditions(Integer auditions) {
        if (auditions == null || auditions <= 0) {
            throw new SongValidationException("error.message");
        }
    }


    void checkSong(Song song) {
        if (song == null || song.getName() == null || song.getName().isBlank()
                || song.getArtistName() == null || song.getArtistName().isBlank()) {
            throw new SongValidationException("error.message");
        }
        checkAditions(song.getAuditions());
    }
}