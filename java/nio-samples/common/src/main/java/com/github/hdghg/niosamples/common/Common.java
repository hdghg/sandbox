package com.github.hdghg.niosamples.common;

import java.io.Console;
import java.lang.reflect.Field;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public interface Common {
    int MESSAGE_SIZE = 128;

    static String messageToString(byte[] array) {
        int end = array.length;
        for (int i = 0; i < array.length; i++) {
            if (0 == array[i]) {
                end = i;
                break;
            }
        }
        return new String(array, 0, end, StandardCharsets.UTF_8);
    }

    static Charset guessConsoleEncoding() {
        try {
            Field f = Console.class.getDeclaredField("cs");
            f.setAccessible(true);
            Console console = System.console();
            if (console != null) {
                Object res = f.get(console);
                if (res instanceof Charset) {
                    return (Charset) res;
                }
            }
        } catch (Exception e) {
            // fall-through
        }

        return Charset.defaultCharset();
    }
}
