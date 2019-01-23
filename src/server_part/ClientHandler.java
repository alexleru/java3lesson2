package server_part;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ClientHandler {
    private MyServer myServer;
    private Socket socket;
    private DataInputStream in;
    private DataOutputStream out;
    private String name;
    public String getName() {
        return name;
    }

    public ClientHandler(MyServer myServer, Socket socket) {
        try {
        this.myServer = myServer;
        this.socket = socket;
        this.in = new DataInputStream(socket.getInputStream());
        this.out = new DataOutputStream(socket.getOutputStream());
        this.name = "";
        new Thread(() -> {
            try {
                while (true) {
                    String str = null;

                    try {
                        str = in.readUTF();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    if (str.startsWith("/auth")) {
                        String[] parts = str.split("\\s");
                        String nick = myServer.getAuthService().getNickByLoginPass(parts[1], parts[2]);
                        if (nick != null) {
                            if (!myServer.isNickBusy(nick)) {
                                sendMsg("/auth ok " + nick);
                                name = nick;
                                myServer.broadcastMsg(name + " зашел в чат");
                                myServer.subscribe(ClientHandler.this);
                                break;
                            } else sendMsg("Учетная запись уже используется");
                        } else sendMsg("Неверные логин/пароль");
                    }
                }
                while (true) {
                    String str = null;
                    try {
                        str = in.readUTF();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    System.out.println("от " + name + " : " + str);
                    if (str.equals("/end")) break;
                    if(str.startsWith("/w")){
                        String[] arrStr =  str.split("\\s", 3);
                        myServer.privateMsg("Приватное сообщение от " + name + " : " + arrStr[2], arrStr[1], this);
                    } else {
                        myServer.broadcastMsg(name + " : " + str);
                    }
                }
            } finally {
                myServer.unsubscribe(ClientHandler.this);
                myServer.broadcastMsg(name + " вышел из чата");
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
        } catch (IOException e) {
            throw  new RuntimeException("Проблемы при создании обработчика клиента");
        }

    }

    public void sendMsg(String msg) {
        try {
            out.writeUTF(msg);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
