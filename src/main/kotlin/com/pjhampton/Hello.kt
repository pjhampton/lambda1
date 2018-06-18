package com.pjhampton

import spark.Spark.get
import org.apache.log4j.BasicConfigurator

fun main(args: Array<String>) {
    BasicConfigurator.configure()
    get("/ping") { req, res -> "pong" }
}

