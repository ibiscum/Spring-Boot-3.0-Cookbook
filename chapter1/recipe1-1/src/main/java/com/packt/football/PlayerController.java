package com.packt.football;

import java.util.List;
import org.springframework.web.bind.annotation.*;
import org.apache.commons.text.StringEscapeUtils;

@RequestMapping("/players")
@RestController
public class PlayerController {
    @GetMapping
    public List<String> listPlayers() {
        return List.of("Ivana ANDRES", "Alexia PUTELLAS");
    }

    @PostMapping
    public String createPlayer(@RequestBody String name) {
        return "Player " + name + " created";
    }

    @GetMapping("/{name}")
    public String readPlayer(@PathVariable String name) {
        return name;
    }

    @DeleteMapping("/{name}")
    public String deletePlayer(@PathVariable String name) {
        String safeName = StringEscapeUtils.escapeHtml4(name);
        return "Player " + safeName + " deleted";
    }

    @PutMapping("/{name}")
    public String updatePlayer(@PathVariable String name, @RequestBody String newName) {
        String safeName = StringEscapeUtils.escapeHtml4(name);
        String safeNewName = StringEscapeUtils.escapeHtml4(newName);
        return "Player " + safeName + " updated to " + safeNewName;
    }
}
