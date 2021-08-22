import server.GatewayServer;

public class App {
    public static void main(String[] args) {
        new GatewayServer("http://localhost:8001").start();
    }
}
