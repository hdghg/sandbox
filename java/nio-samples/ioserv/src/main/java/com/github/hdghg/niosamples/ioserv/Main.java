package com.github.hdghg.niosamples.ioserv;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {
    public static List<OutputStream> outs = new CopyOnWriteArrayList<>();
    static int MESSAGE_SIZE = 128;

    public static void main(String[] args) throws IOException {
        ExecutorService executorService = Executors.newCachedThreadPool();
        try (ServerSocket serverSocket = new ServerSocket(7171)) {
            System.err.println("Server started");
            while (true) {
                final Socket clientSocket = serverSocket.accept();
                System.err.println("Client connected: " + clientSocket.getInetAddress());
                executorService.submit(new ConnectionHandler(clientSocket));
            }
        }
    }

    static class ConnectionHandler implements Runnable {

        private final Socket clientSocket;

        ConnectionHandler(Socket clientSocket) {
            this.clientSocket = clientSocket;
        }

        @Override
        public void run() {
            InputStream in = null;
            OutputStream out = null;
            try {
                in = clientSocket.getInputStream();
                out = clientSocket.getOutputStream();
                outs.add(out);
                byte[] buffer = new byte[128];
                while (true) {
                    try {
                        int total_read = 0;
                        int read = 0;
                        while (total_read < MESSAGE_SIZE) {
                            read = in.read(buffer, read, MESSAGE_SIZE - read);
                            if (-1 == read) {
                                System.err.println("A client has closed data stream");
                                return;
                            }
                            total_read += read;
                        }
                    } catch (SocketException e) {
                        System.err.println("Client seem to be disconnected forcibly");
                        return;
                    } catch (IOException e) {
                        System.err.println("Unexpected error during read");
                        e.printStackTrace();
                        return;
                    }
                    Iterator<OutputStream> iterator = outs.iterator();
                    while (iterator.hasNext()) {
                        OutputStream o = iterator.next();
                        try {
                            o.write(buffer);
                        } catch (Exception ex) {
                            System.err.println("A client unexpectedly disconnected");
                            iterator.remove();
                        }
                    }
                }
            } catch (IOException e) {
                System.err.println("Unexpected IO error");
                e.printStackTrace();
            } finally {
                System.err.println("Client disconnected");
                outs.remove(out);
                IOUtils.closeQuietly(in, out, clientSocket);
            }
        }
    }
}