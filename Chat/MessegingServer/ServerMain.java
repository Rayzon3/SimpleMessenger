import javax.swing.JFrame;

public class ServerMain{
    public static void main(String[] args) {
        Server asuna = new Server();
        asuna.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        asuna.startServer();
    }
}