package app

import com.github.hdghg.sandbox.gspock.UtilString
import spock.lang.Specification

class AppTest extends Specification {

    def "application has a greeting"() {
        setup:
        def app = new App()

        when:
        def result = UtilString.join("a", "b")

        then:
        result == "ab"
    }
}
