package com.github.hdghg.niosamples.common;

public class Arguments {
    public final String host;
    public final String port;
    public final String consoleEncoding;

    public Arguments(String host, String port, String consoleEncoding) {
        this.host = host;
        this.port = port;
        this.consoleEncoding = consoleEncoding;
    }

    public static Arguments parse(String[] args) {
        String host = null;
        String port = null;
        String consoleEncoding = null;

        for (int i = 0; i < args.length; i++) {
            if (i + 1 < args.length) {
                switch (args[i]) {
                    case "-host":
                        host = args[i + 1];
                        break;
                    case "-port":
                        port = args[i + 1];
                        break;
                    case "-consoleEncoding":
                        consoleEncoding = args[i + 1];
                        break;
                    default:
                }
            }
        }
        return new Arguments(host, port, consoleEncoding);
    }
}
