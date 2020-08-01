import javax.swing.JFrame;

public class ClientMain {
    public static void main(String[] args) {
        Client Yuki;
        Yuki = new Client("127.0.0.1");
        Yuki.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Yuki.startRunning();
    }
}