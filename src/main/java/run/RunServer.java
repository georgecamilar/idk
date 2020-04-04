package run;

import server.SocketConcurrentServer;

public class RunServer {
    public static void main(String[] args) {
        try {
            SocketConcurrentServer server = new SocketConcurrentServer(12345);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
