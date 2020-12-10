
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Client {

    public static void main(String[] args){

        MySocket sc = new MySocket(args[0], Integer.parseInt(args[1]));
        String nickname = args[2];
        Boolean nickUsed = false;

        new Thread(){         // Input
            @Override
            public void run(){
                String line;
                while ((line = sc.readLine()) != null) {
                    System.out.println(line);
                }
            }
        }.start();

        new Thread(){         // Output
            @Override
            public void run(){
                sc.println(nickname);
                String line;
                BufferedReader keyboard = new BufferedReader(new InputStreamReader(System.in));
                try {
                    while((line = keyboard.readLine()) != null){
                        sc.println(line);
                    }
                    sc.println("exit");
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }.start();
    }
}
