import java.io.*;
import java.net.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.Border;



public class Server extends JFrame{

    private JTextField userText; //area to type text
    private JTextArea chatWindow; // duh! the chat window
    private ObjectOutputStream output;
    private ObjectInputStream input;
    private ServerSocket server;
    private Socket connection;
     
    //constructor
    public Server(){
        super("Simple Messenger");
        userText = new JTextField();
        userText.setEditable(false);
        userText.addActionListener(
            new ActionListener(){
                public void actionPerformed(ActionEvent event){
                    sendMessege(event.getActionCommand());   
                    userText.setText(""); 
                }
            }
        );
        add(userText, BorderLayout.NORTH);
        chatWindow = new JTextArea();
        add(new JScrollPane(chatWindow));
        setSize(400, 280);
        setVisible(true);
    }
    //setting up server
    public void startServer(){
        try{
            server = new ServerSocket(1234, 5);
            while(true){
                try{
                    waitForConnection();
                    setupStreams();
                    chatting();
                }catch(EOFException eofException){
                    showMessege("Connection Ended !! \n");
                }finally{
                    closeThings();
                }
            }
        }catch(IOException ioException){
            ioException.printStackTrace();
        }
    }

    private void waitForConnection() throws IOException{
        showMessege("Waiting for someone to connect.... :) \n");
        connection = server.accept(); //to accept any connection
        showMessege("Connected to " + connection.getInetAddress().getHostName());
    }

    private void setupStreams() throws IOException{
        output = new ObjectOutputStream(connection.getOutputStream());
        output.flush();
        input = new ObjectInputStream(connection.getInputStream());
        showMessege("\n Streams are up and running !! \n");
    }

    private void chatting() throws IOException{
        String message = "Start chatting !! \n";
        showMessege(message);
        abilityToType(true);
        do{
            try{
                message = (String) input.readObject();
                showMessege("\n" + message);
            }catch(ClassNotFoundException classNotFoundException){
                showMessege("What does this mean ?! (ㆆ_ㆆ)");
            }
        }while(!message.equals("CLIENT - END"));
    }

    //closing streams and sockets
    private void closeThings(){
        showMessege("Closing connections.... cya :D !! \n");
        abilityToType(false);
        try{
            output.close();
            input.close();
            connection.close();
        }catch(IOException ioException){
            ioException.printStackTrace();
        }
    }  

    // to send messenge to client
    private void sendMessege(String message){
        try{
            output.writeObject("SERVER - " + message);
            output.flush();
            showMessege("\n SERVER - " + message);
        }catch(IOException ioException){
            chatWindow.append(" \n ERROR: Can't send message !!");
        }
    }

    //update chat window
    private void showMessege(final String text){
        SwingUtilities.invokeLater(
            new Runnable(){
                public void run(){
                    chatWindow.append(text);
                }
            }
        );
    }

    // giving ability to use text box to the user
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