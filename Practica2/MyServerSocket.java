
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MyServerSocket extends ServerSocket {

    ServerSocket serverSocket;

    MySocket mySocket;

    public MyServerSocket(int port) throws IOException{
        this.serverSocket = new ServerSocket(port);
    }

    @Override
    public MySocket accept(){

        try {
            mySocket = new MySocket(serverSocket.accept());
            return mySocket;
        } catch (IOException ex) {
            Logger.getLogger(MyServerSocket.class.getName()).log(Level.SEVERE, null, ex);
        }

        return null;
    }

    @Override
    public void close(){
        try {
            serverSocket.close();
        } catch (IOException ex) {
            Logger.getLogger(MyServerSocket.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
