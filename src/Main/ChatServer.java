package Main;

import java.io.*;
import java.net.*;
import java.util.*;

public class ChatServer
{
    ArrayList clientOutputStreams;
    
    public class Client implements Runnable {
        BufferedReader reader;
        Socket sock;
        
        public Client(Socket clientSocket) {
            try {
                sock = clientSocket;
                InputStreamReader isReader = new InputStreamReader(sock.getInputStream());
                reader = new BufferedReader(isReader);
                
            } catch (Exception ex) { ex.printStackTrace(); }
        }
        
        public void run() {
            String message;
            try {
                while ((message = reader.readLine()) != null) {
                    System.out.println("read " + message);
                    shareMessages(message);
                }
            } catch (Exception ex) { ex.printStackTrace(); }
        }
    }

    public static void main(String[] args) {
        new ChatServer().go();
    }
    
    public void go() {
        clientOutputStreams = new ArrayList();
        try {
            ServerSocket serverSocket = new ServerSocket(5001);
            while(true) {
                Socket clientSocket = serverSocket.accept();
                PrintWriter writer = new PrintWriter(clientSocket.getOutputStream());
                clientOutputStreams.add(writer);

                Thread t = new Thread(new Client(clientSocket));
                t.start();
                System.out.println("Соединение установлено.");
            }
        } catch (Exception ex) { ex.printStackTrace(); }
    }
    
    public void shareMessages(String message) {
        Iterator it = clientOutputStreams.iterator();
        while (it.hasNext()) {
            try {
                PrintWriter writer = (PrintWriter) it.next();
                writer.println(message);
                writer.flush();
            } catch (Exception ex) { ex.printStackTrace(); }
        }
    }
}
