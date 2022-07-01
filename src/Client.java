import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class Client {
    public static void main(String[] args) throws IOException {
        Socket socket = new Socket("127.0.0.1" , 555);
        //new DataOutputStream(socket.getOutputStream()).writeUTF("signup-mj-mjj-email");
        //System.out.println(new DataInputStream(socket.getInputStream()).readUTF());
        //new DataOutputStream(socket.getOutputStream()).writeUTF("login-m-mjj");
//        new DataOutputStream(socket.getOutputStream()).writeUTF("addPost-title-desc-c");
//        System.out.println(1+new DataInputStream(socket.getInputStream()).readUTF());
//        new DataOutputStream(socket.getOutputStream()).writeUTF("addPost-title-desc-s");
//        System.out.println(2+new DataInputStream(socket.getInputStream()).readUTF());
//        new DataOutputStream(socket.getOutputStream()).writeUTF("addPost-title2-desc2-c");
//        System.out.println(3+new DataInputStream(socket.getInputStream()).readUTF());
        new DataOutputStream(socket.getOutputStream()).writeUTF("signup-m-m-m");
        System.out.println(4+new DataInputStream(socket.getInputStream()).readUTF());
        socket.close();
    }
}
