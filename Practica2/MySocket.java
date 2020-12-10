import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MySocket extends Socket{

    Socket socket;
    PrintWriter out;
    BufferedReader in;

    public MySocket(String host, int port){
        try {
            this.socket = new Socket(host, port);
            this.out = new PrintWriter(socket.getOutputStream(),true);
            this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public MySocket(Socket s){
        try {
            this.socket = s;
            this.out = new PrintWriter(socket.getOutputStream(),true);
            this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException ex) {
            Logger.getLogger(MySocket.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void close(){
        try {
            socket.close();
        } catch (IOException ex) {
            Logger.getLogger(MySocket.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void println(String line){
        this.out.println(line);
    }

    public String readLine(){
        String line = null;
        try {
            line = this.in.readLine();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return line;
    }
}
