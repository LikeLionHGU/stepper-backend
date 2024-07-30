package com.likelionhgu.stepper

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaAuditing

@SpringBootApplication
@EnableJpaAuditing
class StepperApplication

fun main(args: Array<String>) {
    runApplication<StepperApplication>(*args)
}
