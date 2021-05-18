package de.htwg.se.connect4

import scala.concurrent.duration._

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._

class RecordedSimulation extends Simulation {

	val httpProtocol = http
		.baseUrl("http://localhost:9002")
		.inferHtmlResources(BlackList(""".*\.js""", """.*\.css""", """.*\.gif""", """.*\.jpeg""", """.*\.jpg""", """.*\.ico""", """.*\.woff""", """.*\.woff2""", """.*\.(t|o)tf""", """.*\.png""", """.*detectportal\.firefox\.com.*"""), WhiteList())
		.acceptHeader("text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9")
		.acceptEncodingHeader("gzip, deflate")
		.acceptLanguageHeader("de-DE,de;q=0.9,en-US;q=0.8,en;q=0.7")
		.upgradeInsecureRequestsHeader("1")
		.userAgentHeader("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/90.0.4430.212 Safari/537.36")

	val headers_0 = Map(
		"Sec-Fetch-Dest" -> "document",
		"Sec-Fetch-Mode" -> "navigate",
		"Sec-Fetch-Site" -> "none",
		"Sec-Fetch-User" -> "?1",
		"sec-ch-ua" -> """ Not A;Brand";v="99", "Chromium";v="90", "Google Chrome";v="90""",
		"sec-ch-ua-mobile" -> "?0")





	val spikeTest = scenario("Test DB - spike")
		.exec(http("Load Games list (concurrent)")
			.get("/games")
			.headers(headers_0))
	
	val reloadTest = scenario("Test DB - reload")
	    .repeat(300) {
			exec(http("Load games list (sequence)")
			.get("/games")
			.headers(headers_0))
		}

	val rampLoad = scenario("Test DB - ramp")
		.exec(http("Load games with ramping users")
		.get("/games")
		.headers(headers_0))
	
	val createTest = scenario("Test creation")
		.exec(http("creating Games (concurrent)")
		.get("/test")
		.headers(headers_0))
		

	setUp(
		createTest
			.inject(atOnceUsers(400))
		.andThen(
			reloadTest
			.inject(atOnceUsers(1)))
		.andThen(
			spikeTest
			.inject(atOnceUsers(400))
			
		).andThen(
			rampLoad
			.inject(rampUsersPerSec(1).to(400).during(1.minutes))
		)
	).protocols(httpProtocol)
}