package server_part;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;

public class MyServer {
    private ServerSocket serverSocket;
    private Vector<ClientHandler> clientHandlers;
    private AuthService authService;
    public AuthService getAuthService() {
        return authService;
    }
    private final int PORT = 8189;

    public MyServer() {
        try {
            serverSocket = new ServerSocket(PORT);
            Socket socket;
            authService = new BaseAuthService();
            authService.start();
            clientHandlers = new Vector<>();
            while (true) {
                System.out.println("Сервер ожидает подключения");
                socket = serverSocket.accept();
                System.out.println("Клиент подключился");
                new ClientHandler(this, socket);
            }
        } catch (IOException e) {
            System.out.println("Ошибка при работе сервера: " + e);
        } finally {
            authService.stop();
        }
    }

    public synchronized boolean isNickBusy(String nick){
        for (ClientHandler client : clientHandlers) {
            if(client.getName().equals(nick)) return true;
        }
        return false;
    }

    public synchronized void broadcastMsg(String msg){
        for (ClientHandler client : clientHandlers) {
                client.sendMsg(msg);
        }
    }

    public synchronized void privateMsg(String msg, String clientNick, ClientHandler clientSender){
        for (ClientHandler client : clientHandlers) {
            if (client.getName().equals(clientNick)) {
                client.sendMsg(msg);
            } else {
                clientSender.sendMsg("Bot : С таким ником никто не зарегистирован");
            }
        }

    }

    public synchronized void unsubscribe(ClientHandler client){
        clientHandlers.remove(client);
    }

    public synchronized void subscribe(ClientHandler client){
        clientHandlers.add(client);
    }

}
