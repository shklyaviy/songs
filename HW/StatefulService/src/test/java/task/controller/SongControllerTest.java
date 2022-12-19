package task.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import task.model.Error;
import task.model.Song;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class SongControllerTest {
    @Autowired
    MockMvc mvc;
    @Autowired
    ObjectMapper mapper;

    private final String defaultName = "Nirvana";
    private final String defaultArtistName = "Smells like teen spirit";
    private final Integer defaultAuditions = 1000;

    @BeforeEach
    void setup() throws Exception {
        Assertions.assertEquals(0, getSongs().size());
    }

    @AfterEach
    void cleanup() throws Exception {
        for (Song song : getSongs()) {
            deleteSongById(song.getId());
        }
    }

    @Test
    @DisplayName("Full test")
    void fullTest() throws Exception {
        String artistName = defaultName;
        String name = defaultArtistName;
        Integer auditions = defaultAuditions;
        Song song = createSong(new Song(artistName, name, auditions));
        Assertions.assertNotNull(song);
        Assertions.assertNotNull(song.getId());
        Assertions.assertEquals(artistName, song.getArtistName());
        Assertions.assertEquals(name, song.getName());
        Assertions.assertEquals(auditions, song.getAuditions());
        Song songById = getSongById(song.getId());
        Assertions.assertNotNull(songById);
        Integer id = songById.getId();
        Assertions.assertEquals(song.getId(), id);
        Assertions.assertEquals(song.toString(), songById.toString());

        Assertions.assertEquals(1, getSongs().size());

        String updateArtistName = "Eminem";
        String updateName = "Rap God";
        Integer updateAuditions = 5000;
        Song songUpdate = updateSong(id, new Song(updateArtistName, updateName, updateAuditions));
        Assertions.assertNotNull(songUpdate);
        Assertions.assertEquals(id, songUpdate.getId());
        Assertions.assertEquals(updateName, songUpdate.getName());

        Assertions.assertEquals(1, getSongs().size());

        Song songAfterUpdate = getSongById(song.getId());
        Assertions.assertNotNull(songAfterUpdate);
        Assertions.assertEquals(id, songAfterUpdate.getId());
        Assertions.assertEquals(updateName, songAfterUpdate.getName());

        Song deletedSong = deleteSongById(id);
        Assertions.assertNotNull(deletedSong);
        Assertions.assertEquals(id, deletedSong.getId());
        Assertions.assertEquals(updateName, deletedSong.getName());

        Error songByIdError = getSongByIdError(id, 404);
        Assertions.assertNotNull(songByIdError);
        Assertions.assertNotNull(songByIdError.getErrorMessage());
    }

    @ParameterizedTest
    @DisplayName("Create test with empty, blank or null name")
    @ValueSource(strings = {" "})
    @NullAndEmptySource
    void errorCreateTest(String name) throws Exception {
        Error error = createSongError(new Song(defaultArtistName, name, defaultAuditions), 400);
        Assertions.assertNotNull(error);
        Assertions.assertNotNull(error.getErrorMessage());

        Assertions.assertEquals(0, getSongs().size());
    }

    @ParameterizedTest
    @DisplayName("Update test with empty, blank or null text")
    @ValueSource(strings = {" "})
    @NullAndEmptySource
    void errorUpdateTest(String name) throws Exception {
        Song song = createSong(new Song(defaultArtistName, "Sappy", defaultAuditions));
        Integer id = song.getId();

        Error error = updateSongError(id, new Song(defaultArtistName, name, defaultAuditions), 400);
        Assertions.assertNotNull(error);
        Assertions.assertNotNull(error.getErrorMessage());

        Song songAfterUpdate = getSongById(id);
        Assertions.assertNotNull(songAfterUpdate);
        Assertions.assertEquals(song.getId(), songAfterUpdate.getId());
        Assertions.assertEquals(song.toString(), songAfterUpdate.toString());
    }

    @Test
    @DisplayName("Update test with not existed song")
    void updateNotExistTest() throws Exception {
        Error error = updateSongError(1, new Song(defaultArtistName, defaultName, defaultAuditions), 404);
        Assertions.assertNotNull(error);
        Assertions.assertNotNull(error.getErrorMessage());
    }

    @Test
    @DisplayName("Get by id with not existed song")
    void getByIdNotExistTest() throws Exception {
        Error error = getSongByIdError(1, 404);
        Assertions.assertNotNull(error);
        Assertions.assertNotNull(error.getErrorMessage());
    }

    @Test
    @DisplayName("Delete by id with not existed song")
    void deleteByIdNotExistTest() throws Exception {
        Error error = deleteSongByIdError(1, 404);
        Assertions.assertNotNull(error);
        Assertions.assertNotNull(error.getErrorMessage());
    }


    List<Song> getSongs() throws Exception {
        String response = mvc.perform(get(SongController.MAPPING))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse()
                .getContentAsString();
        return mapper.readValue(response, new TypeReference<List<Song>>() {
        });
    }

    Song getSongById(Integer id) throws Exception {
        String response = mvc.perform(get(SongController.MAPPING + "/" + id))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse()
                .getContentAsString();
        return mapper.readValue(response, Song.class);
    }

    Error getSongByIdError(Integer id, int expectedStatus) throws Exception {
        String response = mvc.perform(get(SongController.MAPPING + "/" + id))
                .andExpect(status().is(expectedStatus))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse()
                .getContentAsString();
        return mapper.readValue(response, Error.class);
    }

    Song createSong(Song song) throws Exception {
        String response = mvc.perform(post(SongController.MAPPING)
                        .content(mapper.writeValueAsString(song))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse()
                .getContentAsString();
        return mapper.readValue(response, Song.class);
    }

    Error createSongError(Song song, int expectedStatus) throws Exception {
        String response = mvc.perform(post(SongController.MAPPING)
                        .content(mapper.writeValueAsString(song))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(expectedStatus))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse()
                .getContentAsString();
        return mapper.readValue(response, Error.class);
    }

    Song updateSong(Integer id, Song song) throws Exception {
        String response = mvc.perform(put(SongController.MAPPING + "/" + id)
                        .content(mapper.writeValueAsString(song))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse()
                .getContentAsString();
        return mapper.readValue(response, Song.class);
    }

    Error updateSongError(Integer id, Song song, int expectedStatus) throws Exception {
        String response = mvc.perform(put(SongController.MAPPING + "/" + id)
                        .content(mapper.writeValueAsString(song))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(expectedStatus))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse()
                .getContentAsString();
        return mapper.readValue(response, Error.class);
    }

    Song deleteSongById(Integer id) throws Exception {
        String response = mvc.perform(delete(SongController.MAPPING + "/" + id))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse()
                .getContentAsString();
        return mapper.readValue(response, Song.class);
    }

    Error deleteSongByIdError(Integer id, int expectedStatus) throws Exception {
        String response = mvc.perform(delete(SongController.MAPPING + "/" + id))
                .andExpect(status().is(expectedStatus))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse()
                .getContentAsString();
        return mapper.readValue(response, Error.class);
    }
}