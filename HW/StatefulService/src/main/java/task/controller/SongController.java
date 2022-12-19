package task.controller;

import org.springframework.web.bind.annotation.*;
import task.model.ListenRequest;
import task.model.Song;

import java.util.List;

@RequestMapping(SongController.MAPPING)
public interface SongController {
    String MAPPING  = "/songs";

    @GetMapping(value = "", produces = "application/json")
    List<Song> getSongs();

    @GetMapping(value = "/{id}", produces = "application/json")
    Song getSongById(@PathVariable Integer id);

    @PostMapping(value = "", produces = "application/json")
    Song createSong(@RequestBody Song song);

    @PutMapping(value = "/{id}", produces = "application/json")
    Song updateSongById(@PathVariable Integer id, @RequestBody Song song);

    @DeleteMapping(value = "/{id}", produces = "application/json")
    Song deleteSongById(@PathVariable Integer id);

    @GetMapping(value = "/listen", produces = "application/json")
    List<Song> getSortedSongsByAuditions(@RequestParam(required = false, defaultValue = "5") Integer limit);

    @PostMapping(value = "/listen", produces = "application/json")
    List<Song> listenSongByIds(@RequestBody ListenRequest listenRequest);
    @PostMapping(value = "/{id}/listen", produces = "application/json")
    Song listenSongById(@PathVariable Integer id, @RequestBody ListenRequest listenRequest);
}