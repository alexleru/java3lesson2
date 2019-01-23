package server_part;

public interface AuthService {
    void start();
    String getNickByLoginPass(String login, String pass);
    void stop();
}
