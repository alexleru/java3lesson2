package client_part;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class Client {

    private DataInputStream in;
    private DataOutputStream out;
    final int SERVER_PORT = 8189;
    final String SERVER_ADDRESS = "localhost";
    Socket socket;


    public Client(UIWindows uiWindows) {

        try {
            socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());
            Thread t = new Thread(() -> {
               try {
                   while (true) {
                       String str = in.readUTF();
                       System.out.println(str);
                       uiWindows.recieveMsg(str);
                   }
               } catch (IOException e) {
                   e.printStackTrace();
               } finally {
                   try {
                       socket.close();
                   } catch (IOException e) {
                       e.printStackTrace();
                   }
               }
            });
            t.setDaemon(true);
            t.start();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendMsg(String msg) {
        try {
            out.writeUTF(msg);
        } catch (IOException e) {
            System.out.println("Ошибка отправки сообщения на клиенте");
        }

    }
}
