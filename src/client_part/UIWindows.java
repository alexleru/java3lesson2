package client_part;

import javax.swing.*;
import java.awt.*;

public class UIWindows extends JFrame {
    JTextArea jTextArea;
    JTextField jTextField;
    JButton jButton;
    Client client = new Client(this);
    public UIWindows(){
        setTitle("Chat++");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setBounds(100, 100, 400, 500);
        setResizable(false);

        setLayout(new BorderLayout());

        jTextArea = new JTextArea(27, 50);
        jTextArea.setText("Для авторизации введите " +
                "\n/auth login1 pass1 " +
                "\nили " +
                "\n/auth login2 pass2 " +
                "\nдля приватного сообщения введите " +
                "\n/w NickName и далее сообщение");
        jTextArea.setEditable(false);

        JScrollPane jScroll = new JScrollPane(jTextArea);
        jScroll.setLocation(0,0);
        jTextArea.setLineWrap(true);
        add(BorderLayout.NORTH, jScroll);

        jTextField = new JTextField();
        add(BorderLayout.CENTER, jTextField);

        jButton = new JButton("Send");
        add(BorderLayout.EAST, jButton);

        jButton.addActionListener(e -> sendAction());

        jTextField.addActionListener(e -> sendAction());

        setVisible(true);
    }


    private void sendAction(){
        String text = jTextField.getText();
        jTextField.setText("");
        client.sendMsg(text);
    }

    public void recieveMsg(String msg){
        jTextArea.append(msg + "\r\n");
        jTextArea.setCaretPosition(jTextArea.getText().length());
    }
}