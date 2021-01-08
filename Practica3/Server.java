import java.io.IOException;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Server {

    public static void main(String[] args) throws IOException{

        MyServerSocket serverSocket = new MyServerSocket(Integer.parseInt(args[0]));
        Map<String,MySocket> users = new ConcurrentHashMap<>();

        while (true) {
            MySocket socket = serverSocket.accept();
            new Thread() {
                @Override
                public void run() {
                    
                    String line;
                    String myNickname = socket.readLine();
                    while (users.containsKey(myNickname)){
                        socket.println("Nickname already used, try another one!");
                        myNickname = socket.readLine();
                    }
                    users.put(myNickname, socket);


                    System.out.println(myNickname + " joined the chat");


                    for(String nickname : users.keySet()){
                        if (!nickname.equals(myNickname)){
                            users.get(nickname).println(myNickname + " joined the chat");
                        }
                    }

                    socket.println("WELCOME TO THE CHAT, " + myNickname);


                    String[] nicks = users.keySet().toArray(new String[0]);
                    Arrays.sort(nicks);
                    for(String nickname : users.keySet()){
                        for (int i = 0; i < nicks.length; i ++){
                            users.get(nickname).println("?¿=() " + nicks[i]);
                        }
                    }

                    while ((line = socket.readLine()) != null) {


                        if (line.equals("-><-=()")){
                            System.out.println(myNickname + " left the chat");
                        } else {
                            System.out.println(myNickname + "> " + line);
                        }


                        for(String nickname : users.keySet()){
                            if (!nickname.equals(myNickname)){
                                if (line.equals("-><-=()")){
                                    users.get(nickname).println(myNickname + " left the chat");
                                } else {
                                    users.get(nickname).println(myNickname + "> " + line);
                                }

                            }
                        }

                        if(line.equals("-><-=()")){
                            users.remove(myNickname);
                            break;
                        }
                    }
                    socket.close();
                }
            }.start();
        }
    }
}
