import java.io.*;
import java.net.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Client extends JFrame{
    private JTextField userText;
    private JTextArea chatWindow;
    private ObjectOutputStream output;
    private ObjectInputStream input;
    private String message = "";
    private String serverIP;
    private Socket connection;
    
    //conctructor GUI
    public Client(String host){
        super("Client Chat Window");
        serverIP = host;
        userText = new JTextField();
        userText.setEditable(false);
        userText.addActionListener(
            new ActionListener(){
                public void actionPerformed(ActionEvent event){
                    sendMessage(event.getActionCommand());
                    userText.setText("");
                }
            }
        );
        add(userText, BorderLayout.NORTH);
        chatWindow = new JTextArea();
        add(new JScrollPane(chatWindow), BorderLayout.CENTER);
        setSize(400, 280);
        setVisible(true);
    }
    
    //starting client 
    public void startRunning(){
        try{
            connectToServer();
            setupStreams();
            chatting();
        }catch(EOFException eofException){
            showMessage("Client terminated the connection !! \n");
        }catch(IOException ioException){
            ioException.printStackTrace();
        }finally{
            closeThings();
        }
    }

    //connecting to server
    private void connectToServer() throws IOException{
        showMessage("Trying to connect to the server..... <(^_^)> \n");
        connection = new Socket(InetAddress.getByName(serverIP), 1234);
        showMessage("Connected to server : " + connection.getInetAddress().getHostName());
    }
    
    //setting up streams
    private void setupStreams() throws IOException{
        output = new ObjectOutputStream(connection.getOutputStream());
        output.flush();
        input = new ObjectInputStream(connection.getInputStream());
        showMessage("\n Streams successfully made !! \n");
    }

    //chatting
    private void chatting() throws IOException{
        abilityToType(true);
        do{
            try{
                message = (String) input.readObject();
                showMessage("\n" + message);
            }catch(ClassNotFoundException classNotFoundException){
                showMessage("\n What does this mean ?!  •`_´•");
            }
        }while(!message.equals("SERVER - END"));
    }

    //closing streams and sockets
    private void closeThings(){
        showMessage("Closing streams and sockets.... \n");
        abilityToType(false);
        try{
            output.close();
            input.close();
            connection.close();
        }catch(IOException ioException){
            ioException.printStackTrace();
        }
    }

    //sending messages to server
    private void sendMessage(String message){
        try{
            output.writeObject("CLIENT - " + message);
            output.flush();
            showMessage("\n CLIENT - " + message);
        }catch(IOException ioException){
            chatWindow.append("oops.. something went wrong !! OwO\n");
        }
    }

    //updating chat window
    private void showMessage(final String msg){
        SwingUtilities.invokeLater(
            new Runnable(){
                public void run(){
                    chatWindow.append(msg);
                }
            }
        );
    }

    // giving ability to type in text box
    private void abilityToType(final boolean torf){
        SwingUtilities.invokeLater(
            new Runnable(){
                public void run(){
                    userText.setEditable(torf);
                }
            }
        );
    } 
}