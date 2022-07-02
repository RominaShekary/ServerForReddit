import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
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

class RequestHandler extends Thread {
    Socket socket;
    DataInputStream dis;
    DataOutputStream dos;

    public RequestHandler(Socket socket) throws IOException {
        this.socket = socket;
        dis = new DataInputStream(socket.getInputStream());
        dos = new DataOutputStream(socket.getOutputStream());
    }

    String read() {
        try {
            StringBuilder message = new StringBuilder();
            int c;
            while ((c = dis.read()) != 0) {
                message.append((char) c);
            }
            System.out.println("Message from client: " + message.toString());
            return message.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    void write(String str) {
        try {
            dos.write(str.getBytes(StandardCharsets.UTF_8));
            dos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        System.out.print("one request accepted + ");
        String command = read();
        System.out.println(command);

        String[] split = command.split("-");
        switch (split[0]) {
            //signup-username-password-email
            case "signup": {
                String accountRow = null;
                try {
                    accountRow = DataBase.getInstance().getController("Accounts").getRow(split[1]);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                if (!Objects.equals(accountRow, "invalid"))
                    write("account already exists");
                else {
                    Map<String, String> data = new HashMap<>(Map.of("username", split[1], "password", split[2], "email", split[3]));
                    StringBuilder str = new StringBuilder(data.get("username"));
                    str.append(":").append(data.get("password")).append("-").append(data.get("email")).append("\n");
                    DataBase.getInstance().getController("Accounts").write(str.toString());
                    DataBase.accounts++;
                    write("done");
                }
                break;
            }
            //login-username-password
            case "login": {
                String accountRow = "invalid";
                try {
                    accountRow = DataBase.getInstance().getController("Accounts").getRow(split[1]);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                if (accountRow.equals("invalid"))
                    write("account did not exist");
                else if (!accountRow.split(":")[0].equals(split[1]))
                    write("account did not exist");
                else if (!accountRow.split(":")[1].split("-")[0].equals(split[2]))
                    write("incorrect password");
                else write("done");
                break;
            }
            //addChannel-channel name-admin username
            case "addChannel": {
                Map<String, String> data = new HashMap<>(Map.of("c name", split[1], "admin uname", split[2]));
                StringBuilder str = new StringBuilder();
                str.append(data.get("c name")).append("-").append(data.get("admin uname")).append("\n");
                DataBase.getInstance().getController("Channels").write(str.toString());
                DataBase.channels++;
                write("done");
                break;
            }
            //addPost-title-description-channel
            case "addPost": {
                Map<String, String> data = new HashMap<>(Map.of("title", split[1], "desc", split[2], "channel", split[3]));
                StringBuilder str = new StringBuilder();
                str.append(data.get("title")).append("-").append(data.get("desc")).append("-").append(data.get("channel")).append("\n");
                DataBase.getInstance().getController("Posts").write(str.toString());
                write("done");
                break;
            }
            //getChannelPosts-c name
            case "getChannelPosts": {
                Map<String, String> data = new HashMap<>(Map.of("channel", split[1]));
                StringBuilder str = new StringBuilder();
                try {
                    String[] rows = DataBase.getInstance().getController("Posts").getRows("-" + data.get("channel"));
                    for (int i = 0; i < rows.length; i++) {
                        str.append(rows[i]).append("/");
                    }
                    str.append(rows.length);
                    write(str.toString());
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                break;
            }
            //getUser-uname
            case "getUser": {
                String accountRow = "invalid";
                try {
                    accountRow = DataBase.getInstance().getController("Accounts").getRow(split[1]);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                if (accountRow.equals("invalid"))
                    write("account did not exist");
                else if (!accountRow.split(":")[0].equals(split[1]))
                    write("account did not exist");
                else
                    write(accountRow);
            }
            //getChannel-c name
            case "getChannel": {
                String channelRow = "invalid";
                try {
                    channelRow = DataBase.getInstance().getController("Channels").getRow(split[1]);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                if (channelRow.equals("invalid"))
                    write("channel did not exist");
                else
                    write(channelRow);
            }
            //getAllChannels
            case "getAllChannels": {
                StringBuilder str = new StringBuilder();
                try {
                    String channels = DataBase.getInstance().getController("Channels").read();
                    String[] eachChannel = channels.split("\n");
                    Arrays.stream(eachChannel).forEach(s -> str.append(s).append("/"));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                write(str.toString());
            }
            //addComment-comment-username
            case "addComment": {


            }
            default:
                System.out.println("invalid request");
                break;
        }
    }

}
