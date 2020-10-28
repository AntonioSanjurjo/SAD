import java.io.IOException;
import java.util.Observable;
import java.util.Observer;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Console implements Observer {

    Line line;

    public Console(Line l){
        this.line = l;
    }

    @Override
    public void update(Observable o, Object arg){
        int r = (int)arg;
        switch(r){
            case Constants.UPDATE:
              System.out.print("\u001b[1000D"); // Place the cursor in the first possition
              System.out.print("\u001b[0K"); // Delete the line
              System.out.print(line.str); // Print the new line
              System.out.print("\u001b[1000D"); // Place the cursor on the first possition
              if (line.getPos() != 0) { // If the possition is not the first, this will place it in the right one
                  System.out.print("\u001b[" + line.getPos() + "C");
              }
                break;
            default:
                break;
        }
    }

}
