package {{cookiecutter.package}}.api

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component

@Component
class ExampleApiDelegateImpl : ExampleApiDelegate {

    private val log: Logger = LoggerFactory.getLogger(javaClass)

    override fun getExample(): ResponseEntity<Unit> {
        log.info("getExample")
        return super.getExample()
    }
}
