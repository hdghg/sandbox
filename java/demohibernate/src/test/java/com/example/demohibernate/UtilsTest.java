package com.example.demohibernate;

import org.junit.jupiter.api.Test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class UtilsTest {


    @Test
    void testRegexNamedMatcher() {
        Pattern compile = Pattern.compile("^.*BACK-(?<jira>\\d+).*$");
        Matcher matcher = compile.matcher("feature/BACK-1234(2)-allow-delete-smth");
        assertTrue(matcher.matches());
        assertEquals("1234", matcher.group("jira"));
    }
}
