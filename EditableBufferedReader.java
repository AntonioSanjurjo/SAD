
import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.logging.Level;
import java.util.logging.Logger;


public class EditableBufferedReader extends BufferedReader {

    static final int CARRIAGE_RETURN = 13;
    static final int ESC = 27;

    static final int TWO = 50;
    static final int THREE = 51;
    static final int C = 67;
    static final int D = 68;
    static final int F = 70;
    static final int H = 72;
    static final int O = 79;

    static final int BRACKET = 91;
    static final int TILDE = 126;
    static final int DELETE = 127;

    static final int RIGHT_ARROW = 500;
    static final int LEFT_ARROW = 501;
    static final int HOME = 502;
    static final int END = 503;
    static final int SUPR = 504;
    static final int INSERT = 505;

    private Line line;

    public EditableBufferedReader(Reader in) {
        super(in);
        this.line = new Line();
    }

    public void setRaw() {
      try{
      String[] cmd = {"/bin/sh", "-c", "stty -echo raw </dev/tty"};
      Runtime.getRuntime().exec(cmd);
        }catch(IOException ioe){
        System.out.println (ioe);
        }
    }

    public void unsetRaw() {
      try{
      String[] cmd = {"/bin/sh", "-c", "stty echo cooked </dev/tty"};
      Runtime.getRuntime().exec(cmd);
        }catch(IOException ioe){
        System.out.println (ioe);
        }
    }

    /*
     * Cursor Right -> ESC [ C
     * Cursor Left -> ESC [ D
     * HOME -> ESC O H
     * END -> ESC O F
     * INSERT -> ESC [ 2 ~
     * SUPR -> ESC [ 3 ~
     *
     */
    @Override
    public int read() throws IOException {
        int r = super.read();
        if (r == ESC) {
            r = super.read();
            if (r == BRACKET) {
                r = super.read();
                switch (r) {
                    case C:
                        return RIGHT_ARROW;
                    case D:
                        return LEFT_ARROW;
                    case H:
                        return HOME;
                    case F:
                        return END;
                    case THREE:
                        r = super.read();
                        if(r == TILDE){
                            return SUPR;
                        } else {
                            break;
                        }
                    case TWO:
                        r = super.read();
                        if(r == TILDE){
                            return INSERT;
                        } else {
                            break;
                        }
                    default:
                        break;
                }
            }
        }
        return r;
    }

    @Override
    public String readLine() throws IOException {
        this.setRaw();
        int c;
        String str = "";
        boolean loop = true;
        while (loop) {
            c = this.read();
            switch (c) {
                case CARRIAGE_RETURN:
                    loop = false;
                    break;
                case RIGHT_ARROW:
                    line.moveCursorRight();
                    break;
                case LEFT_ARROW:
                    line.moveCursorLeft();
                    break;
                case DELETE:
                    str = line.delChar();
                    break;
                case HOME:
                    line.home();
                    break;
                case END:
                    line.end();
                    break;
                case SUPR:
                    str = line.suprChar();
                    break;
                case INSERT:
                    line.insert = !line.insert;
                    break;
                default:
                    str = line.addChar(c);
                    break;
            }

            System.out.print("\u001b[1000D"); // Place the cursor in the first possition
            System.out.print("\u001b[0K"); // Delete the line
            System.out.print(str); // Print the new line
            System.out.print("\u001b[1000D"); // Place the cursor on the first possition
            if (line.getPos() != 0) { // If the possition is not the first, this will place it in the right one
                System.out.print("\u001b[" + line.getPos() + "C");
            }

        }
        this.unsetRaw();
        return str;

            }

        }
