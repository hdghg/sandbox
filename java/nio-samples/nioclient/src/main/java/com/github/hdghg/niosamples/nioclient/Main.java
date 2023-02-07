package com.github.hdghg.niosamples.nioclient;

import com.github.hdghg.niosamples.common.Arguments;
import com.github.hdghg.niosamples.common.Common;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.charset.UnsupportedCharsetException;
import java.util.Arrays;
import java.util.Optional;

public class Main {
    public static void main(String[] args) throws IOException {
        Arguments arguments = Arguments.parse(args);
        InetSocketAddress hostAddress = new InetSocketAddress(
                Optional.ofNullable(arguments.host).orElse("localhost"),
                Optional.ofNullable(arguments.port).map(Integer::parseInt).orElse(7171));
        SocketChannel socketChannel = SocketChannel.open(hostAddress);
        socketChannel.configureBlocking(false);
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
        InputStreamReader inputStreamReader = new InputStreamReader(System.in, consoleEncoding);
        BufferedReader br = new BufferedReader(inputStreamReader);
        String line = "";
        ByteBuffer readBuffer = ByteBuffer.allocate(Common.MESSAGE_SIZE);
        Selector selector = Selector.open();
        socketChannel.register(selector, SelectionKey.OP_READ);
        while (!"quit".equals(line)) {
            if (br.ready()) {
                line = br.readLine();
                byte[] bytes = Arrays.copyOf(line.getBytes(StandardCharsets.UTF_8), Common.MESSAGE_SIZE);
                ByteBuffer byteBuffer = ByteBuffer.wrap(bytes);
                socketChannel.write(byteBuffer);
            }
            while ((selector.select(125) > 0) && (Common.MESSAGE_SIZE == socketChannel.read(readBuffer))) {
                selector.selectedKeys().clear();
                byte[] array = readBuffer.array();
                System.err.println(Common.messageToString(array));
                readBuffer.clear();
            }
        }
        socketChannel.close();
    }
}