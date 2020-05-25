package serverDB;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Network {
    private static final int PORT = 100;
    private static Database db;
    private static Socket socket;
    private static ServerSocket serverSocket;

    private static String bytesToString(byte[] bytes) throws UnsupportedEncodingException {
        return new String(bytes, "UTF-8");
    }

    private static void addUser(String username, String password) throws IOException {
        boolean result = db.putUserInfo(username, password);
        OutputStream outputStream = new DataOutputStream(socket.getOutputStream());
        if (result){
            outputStream.write(new byte[]{1});
        }
        else {
            outputStream.write(new byte[]{0});
        }
        outputStream.close();
    }

    private static void checkUser(String username) throws IOException {
        int result = db.checkUsernameRepeat(username);
        OutputStream outputStream = new DataOutputStream(socket.getOutputStream());
        byte bytes[] = new byte[] {(byte)result};
        outputStream.write(bytes);
        outputStream.close();
    }

    private static void getPassword(String username) throws IOException {
        String result = db.getPassword(username);
        OutputStream outputStream = new DataOutputStream(socket.getOutputStream());
        byte bytes[] = result.getBytes();
        outputStream.write(bytes);
        outputStream.close();
    }



    public static void main(String[] argv){
        db = new Database();
        try {
            serverSocket = new ServerSocket(PORT);
            socket = serverSocket.accept();
            while (true) {
                socket = serverSocket.accept();
                System.out.println("OK");
                DataInputStream in = new DataInputStream(socket.getInputStream());
                String str = bytesToString(in.readAllBytes());
                System.out.println(str);
                System.out.println("END");
                String[] args = str.split("\n");
                System.out.println(args[0]);
                if (args[0].equals("ADDUSR")){
                    System.out.println(args[1]);
                    System.out.println(args[2]);
                    addUser(args[1], args[2]);
                }
                else if (args[0].equals("GETPW")){
                    getPassword(args[1]);
                }
                else if (args[0].equals("CHECKUSR")){
                    checkUser(args[1]);
                }
            }
        }catch (IOException e) {
            e.printStackTrace();
            return;
        }


    }
}
