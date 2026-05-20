package com.packt.cards.simulation;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.*;

import io.gatling.javaapi.core.*;
import io.gatling.javaapi.http.*;

public class BreakingPointSimulation extends Simulation {

    HttpProtocolBuilder httpProtocol = http
        .baseUrl("http://localhost:8080")
        .acceptHeader("application/json")
        // High throughput setups should reuse connections to prevent running out of ephemeral ports
        .shareConnections();

    ScenarioBuilder scn = scenario("Breaking Point Stress Test")
        .exec(http("Get Core Resource")
            .get("/cards")
            .check(status().is(200)));

    {
        setUp(
            scn.injectOpen(
                // Step 1: Baseline Warm-up (allow JIT compiler to optimize Java bytecode)
                rampUsersPerSec(1).to(100).during(60),
                nothingFor(5),

                // Step 2: Linear Ramping Stress Test
                // Continuously increase new arrivals from 1000 users/sec to 100000 users/sec over 10 minutes
                rampUsersPerSec(500).to(10000).during(300)
            )
        ).protocols(httpProtocol);
    }
}
