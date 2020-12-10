
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Random;

public class Server {

    public static void main(String[] args) throws IOException{

        MyServerSocket serverSocket = new MyServerSocket(Integer.parseInt(args[0]));
        Map<String,MySocket> users = new ConcurrentHashMap<>();
        Map<String,MySocket> color = new ConcurrentHashMap<>();

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
                    Random rand = new Random();
                    String myColor;
                    switch(rand.nextInt(8)){
                      case 0: myColor = Colors.BLACK;
                              break;
                      case 1: myColor = Colors.RED;
                              break;
                      case 2: myColor = Colors.GREEN;
                              break;
                      case 3: myColor = Colors.YELLOW;
                              break;
                      case 4: myColor = Colors.BLUE;
                              break;
                      case 5: myColor = Colors.PURPLE;
                              break;
                      case 6: myColor = Colors.CYAN;
                              break;
                      case 7: myColor = Colors.WHITE;
                              break;
                      default: myColor = Colors.GREEN;
                    }
                    users.put(myNickname, socket);
                    color.put(myColor, socket);
                    System.out.println(myColor + myNickname + Colors.RESET + Colors.GREEN + " joined the chat" + Colors.RESET);
                    for(String nickname : users.keySet()){
                        if (!nickname.equals(myNickname)){
                            users.get(nickname).println(myColor + myNickname + Colors.RESET + Colors.GREEN + " joined the chat" + Colors.RESET);
                        }
                    }

                    socket.println(Colors.YELLOW + "WELCOME TO THE CHAT:" + Colors.RESET);
                    while ((line = socket.readLine()) != null) {

                        if (line.equals("exit")){
                            System.out.println(myColor + myNickname + " left the chat" + Colors.RESET);
                        } else {
                            System.out.println(myColor + myNickname + "$ " + Colors.RESET + line);
                        }

                        for(String nickname : users.keySet()){
                            if (!nickname.equals(myNickname)){
                                if (line.equals("exit")){
                                    users.get(nickname).println(myColor + myNickname + " left the chat" + Colors.RESET);
                                } else {
                                    users.get(nickname).println(myColor+ myNickname + "$ " + Colors.RESET + line);
                                }
                            }
                        }

                        if(line.equals("exit")){
                            users.remove(myNickname);
                            color.remove(myColor);
                            break;
                        }
                    }
                    socket.close();
                }
            }.start();
        }
    }
}
