package codes.rusty.jagar;

public class Main {
    
    public static void main(String[] args) {
        Server server = new Server();
        Core.setServer(server);
        server.start();
    }
    
}
