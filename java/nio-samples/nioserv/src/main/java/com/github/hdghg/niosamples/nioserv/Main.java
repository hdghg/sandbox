package com.github.hdghg.niosamples.nioserv;

import com.github.hdghg.niosamples.common.Common;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Main {
    public static void main(String[] args) throws IOException {
        try (ServerSocketChannel socketChannel = ServerSocketChannel.open()) {
            socketChannel.configureBlocking(false);
            socketChannel.bind(new InetSocketAddress("0.0.0.0", 7171));
            System.err.println("Server started at " + socketChannel.getLocalAddress());
            Selector readSelector = Selector.open();
            Selector writeSelector = Selector.open();
            socketChannel.register(readSelector, SelectionKey.OP_ACCEPT);
            List<ByteBuffer> bufferList = new ArrayList<>();
            while (true) {
                readSelector.select();
                Iterator<SelectionKey> iterator = readSelector.selectedKeys().iterator();
                while (iterator.hasNext()) {
                    SelectionKey nextKey = iterator.next();
                    if (nextKey.isAcceptable()) {
                        SocketChannel client = ((ServerSocketChannel) nextKey.channel()).accept();
                        System.err.println("Client connected: " + client.getRemoteAddress());
                        client.configureBlocking(false);
                        client.register(readSelector, SelectionKey.OP_READ);
                        client.register(writeSelector, SelectionKey.OP_WRITE);
                    }
                    if (nextKey.isReadable()) {
                        ByteBuffer buffer = ByteBuffer.allocate(Common.MESSAGE_SIZE);
                        try {
                            ((SocketChannel) nextKey.channel()).read(buffer);
                            buffer.flip();
                            bufferList.add(buffer);
                        } catch (IOException e) {
                            System.err.println("Client disconnected");
                            nextKey.cancel();
                            nextKey.channel().close();
                        }
                    }
                    iterator.remove();
                }
                if (!bufferList.isEmpty()) {
                    ByteBuffer[] array = bufferList.toArray(new ByteBuffer[0]);
                    writeSelector.select(125);
                    Iterator<SelectionKey> writeIterator = writeSelector.selectedKeys().iterator();
                    while (writeIterator.hasNext()) {
                        SelectionKey next = writeIterator.next();
                        try {
                            ((SocketChannel) next.channel()).write(array);
                            bufferList.forEach(Buffer::flip);
                        } catch (IOException e) {
                            System.err.println("Write key cancel");
                            next.cancel();
                        }
                        writeIterator.remove();
                    }
                    bufferList.clear();
                }
            }
        }
    }
}
