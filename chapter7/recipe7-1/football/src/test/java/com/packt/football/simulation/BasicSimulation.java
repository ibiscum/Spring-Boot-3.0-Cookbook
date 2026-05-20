package com.packt.football.simulation;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.*;

import io.gatling.javaapi.core.*;
import io.gatling.javaapi.http.*;

public class BasicSimulation extends Simulation {

    // 1. Define the target HTTP Protocol targeting your Spring Boot Local environment
    HttpProtocolBuilder httpProtocol = http
        .baseUrl("http://localhost:8080")
        .acceptHeader("application/json")
        .userAgentHeader("Gatling Performance Test");

    // 2. Define the Scenario (User Journey)
    ScenarioBuilder scn = scenario("Spring Boot REST API Performance Test")
        .exec(http("Get Actuator Health")
            .get("/actuator/health")
            .check(status().is(200)))
        .pause(1)
        .exec(http("Get Core Resource")
            .get("/api/v1/resource")
            .check(status().is(200)));

    // 3. Define the Load Injection Profile
    {
        setUp(
            scn.injectOpen(
                nothingFor(2), // Warm-up delay
                atOnceUsers(10), // Instantly inject 10 users
                rampUsers(50).during(20) // Ramp up 50 users over 20 seconds
            )
        ).protocols(httpProtocol);
    }
}
