package com.github.hdghg.niosamples.ioclient;

import com.github.hdghg.niosamples.common.Arguments;
import com.github.hdghg.niosamples.common.Common;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.charset.UnsupportedCharsetException;
import java.util.Arrays;
import java.util.Optional;

public class Main {
    public static void main(String[] args) throws IOException {
        byte[] buffer = new byte[128];
        Arguments arguments = Arguments.parse(args);
        InetSocketAddress hostAddress = new InetSocketAddress(
                Optional.ofNullable(arguments.host).orElse("localhost"),
                Optional.ofNullable(arguments.port).map(Integer::parseInt).orElse(7171));
        Charset consoleEncoding = null;
        if (arguments.consoleEncoding != null) {
            try {
                consoleEncoding = Charset.forName(arguments.consoleEncoding);
                System.out.println("Console encoding set manually as " + consoleEncoding);
            } catch (UnsupportedCharsetException e) {
                System.err.println("Unknown charset " + arguments.consoleEncoding + ". Falling back to autodetection");
            }
        }
        if (consoleEncoding == null) {
            consoleEncoding = Common.guessConsoleEncoding();
            System.out.println("Console encoding detected as " + consoleEncoding);
        }
        try (Socket socket = new Socket(hostAddress.getAddress(), hostAddress.getPort());
             InputStream inputStream = socket.getInputStream();
             OutputStream outputStream = socket.getOutputStream();
             InputStreamReader inputStreamReader = new InputStreamReader(System.in, consoleEncoding);
             BufferedReader br = new BufferedReader(inputStreamReader)
        ) {
            String s = "";
            while (!"quit".equals(s)) {
                while (0 < inputStream.available()) {
                    inputStream.read(buffer);
                    System.out.println(Common.messageToString(buffer));
                }
                s = br.readLine();
                byte[] bytes = s.getBytes(StandardCharsets.UTF_8);
                outputStream.write(Arrays.copyOf(bytes, 128));
            }
        }
    }
}