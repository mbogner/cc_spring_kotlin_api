package {{cookiecutter.package}}

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class Api

fun main(args: Array<String>) {
    runApplication<Api>(*args)
}
