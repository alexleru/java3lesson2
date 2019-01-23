package server_part;

import java.util.ArrayList;

public class BaseAuthService implements AuthService {
    private class Entry{
        private String login;
        private String password;
        private String nick;
        public Entry(String login, String password, String nick) {
            this.login = login;
            this.password = password;
            this.nick = nick;
        }
    }

    private ArrayList<Entry> entries;

    @Override
    public void start() {    }

    @Override
    public void stop() {    }

    public BaseAuthService() {
        entries = new ArrayList<>();
        entries.add(new Entry("login1", "pass1", "nick1"));
        entries.add(new Entry("login2", "pass2", "nick2"));
        entries.add(new Entry("login3", "pass3", "nick3"));
    }

    @Override
    public String getNickByLoginPass(String login, String pass) {
        for (Entry entry : entries) {
            if (entry.login.equals(login) && entry.password.equals(pass))
                return entry.nick;
        }
        return null;
    }
}
