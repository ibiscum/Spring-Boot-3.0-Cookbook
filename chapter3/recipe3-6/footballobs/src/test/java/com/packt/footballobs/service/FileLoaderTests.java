package com.packt.footballobs.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import org.junit.jupiter.api.Test;

class FileLoaderTests {

    @Test
    void loadFileReadsTeamListFromJson() throws Exception {
        Path temporaryDirectory = Files.createTempDirectory("footballobs-test");
        Path teamFile = temporaryDirectory.resolve("teams.json");
        Files.writeString(teamFile, "[\"FC Barcelona\", \"Real Madrid\"]");

        FileLoader fileLoader = new FileLoader(temporaryDirectory.toString());
        Method loadFile = FileLoader.class.getDeclaredMethod("loadFile", String.class);
        loadFile.setAccessible(true);
        loadFile.invoke(fileLoader, teamFile.toString());

        assertThat(fileLoader.getFileName()).isEqualTo(teamFile.toString());
        assertThat(fileLoader.getTeams()).containsExactly("FC Barcelona", "Real Madrid");
    }
}
