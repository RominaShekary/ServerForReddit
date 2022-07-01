import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Server {
    public static void main(String[] args) {
        try {
            ServerSocket ss = new ServerSocket(555);
            DataBase.getInstance().addDataBase("Accounts", new Controller("src/Accounts.txt"));
            DataBase.getInstance().addDataBase("Channels", new Controller("src/Channels.txt"));
            DataBase.getInstance().addDataBase("Posts", new Controller("src/Posts.txt"));
            while (true) {
                Socket socket = ss.accept();
                new RequestHandler(socket).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}