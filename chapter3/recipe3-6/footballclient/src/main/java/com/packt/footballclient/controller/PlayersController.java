// package com.packt.footballclient.controller;

// import java.util.List;
// import java.util.stream.Collectors;

// import org.slf4j.Logger;
// import org.slf4j.LoggerFactory;
// import org.springframework.boot.web.client.RestTemplateBuilder;
// import org.springframework.web.bind.annotation.GetMapping;
// import org.springframework.web.bind.annotation.RequestMapping;
// import org.springframework.web.bind.annotation.RestController;
// import org.springframework.web.client.RestTemplate;

// import com.packt.footballclient.domain.PlayerRanking;

// @RestController
// @RequestMapping("/players")
// public class PlayersController {

//     private RestTemplate restTemplate;

//     private static final Logger logger = LoggerFactory.getLogger(PlayersController.class);

//     public PlayersController(RestTemplateBuilder restTemplateBuilder) {
//         this.restTemplate = restTemplateBuilder.build();
//     }

//     @GetMapping
//     public List<PlayerRanking> getPlayers() {

//         logger.info("Preparing ranking for all players");
//         String url = "http://localhost:8080/football/ranking";
//         List<String> players = List.of("Aitana Bonmatí", "Alexia Putellas", "Andrea Falcón", "Andrea Pereira",
//                 "Andressa Alves", "Asisat Oshoala", "Candela Andújar", "Caroline Graham Hansen", "Hamraoui",
//                 "Jana Fernández", "Jennifer Hermoso", "Kheira Hamraoui", "Leila Ouahabi", "Lieke Martens",
//                 "Ludmila da Silva", "Mariona Caldentey", "Martens", "Melanie Serrano", "Misa Rodríguez",
//                 "Patri Guijarro", "Patri López", "Pereira", "Sandra Paños");
//         return players.stream().map(player -> {
//             int ranking = this.restTemplate.getForObject(url + "/" + player, int.class);
//             return new PlayerRanking(player, ranking);
//         }).collect(Collectors.toList());
//     }
// }

package com.packt.footballclient.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClient;

import com.packt.footballclient.domain.PlayerRanking;

@RestController
@RequestMapping("/players")
public class PlayersController {

    private final RestClient restClient;

    private static final Logger logger = LoggerFactory.getLogger(PlayersController.class);

    // Nutzen des neuen RestClient.Builder anstelle des veralteten RestTemplateBuilder
    public PlayersController(RestClient.Builder restClientBuilder) {
        this.restClient = restClientBuilder
                .baseUrl("http://localhost:8080/football/ranking") // Basis-URL zentral hinterlegt
                .build();
    }

    @GetMapping
    public List<PlayerRanking> getPlayers() {

        logger.info("Preparing ranking for all players");
        List<String> players = List.of("Aitana Bonmatí", "Alexia Putellas", "Andrea Falcón", "Andrea Pereira",
                "Andressa Alves", "Asisat Oshoala", "Candela Andújar", "Caroline Graham Hansen", "Hamraoui",
                "Jana Fernández", "Jennifer Hermoso", "Kheira Hamraoui", "Leila Ouahabi", "Lieke Martens",
                "Ludmila da Silva", "Mariona Caldentey", "Martens", "Melanie Serrano", "Misa Rodríguez",
                "Patri Guijarro", "Patri López", "Pereira", "Sandra Paños");

        return players.stream().map(player -> {
            // Typsicherer und flüssiger Aufruf mit dem RestClient
            int ranking = this.restClient.get()
                    .uri("/{player}", player) // Nutzt URI-Templates (schützt vor Zeichenkodierungs-Fehlern im Namen)
                    .retrieve()
                    .body(Integer.class);
            return new PlayerRanking(player, ranking);
        }).collect(Collectors.toList());
    }
}
