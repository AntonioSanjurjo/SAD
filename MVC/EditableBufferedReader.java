
import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.logging.Level;
import java.util.logging.Logger;

public class EditableBufferedReader extends BufferedReader {

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

    private Line line;
    private Console console;

    public EditableBufferedReader(Reader in) {
        super(in);
        this.line = new Line();
        this.console = new Console(line);
        this.line.addObserver(console);
    }

    public void setRaw() throws IOException {
        try {
            String[] cmd = {"/bin/sh", "-c", "stty raw </dev/tty"};
            Runtime.getRuntime().exec(cmd).waitFor();

        } catch (InterruptedException ex) {
            Logger.getLogger(EditableBufferedReader.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void unsetRaw() throws IOException {
        try {
            String[] cmd = {"/bin/sh", "-c", "stty cooked </dev/tty"};
            Runtime.getRuntime().exec(cmd).waitFor();

        } catch (InterruptedException ex) {
            Logger.getLogger(EditableBufferedReader.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public int read() throws IOException {
        int r = super.read();
        if (r == ESC) {
            r = super.read();
            if (r == BRACKET) {
                r = super.read();
                switch (r) {
                    case C:
                        return Constants.RIGHT_ARROW;
                    case D:
                        return Constants.LEFT_ARROW;
                    case H:
                          return Constants.HOME;
                    case F:
                          return Constants.END;
                    case THREE:
                        r = super.read();
                        if(r == TILDE){
                            return Constants.SUPR;
                        } else {
                            break;
                        }
                    case TWO:
                        r = super.read();
                        if(r == TILDE){
                            return Constants.INSERT;
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
                case Constants.CARRIAGE_RETURN:
                    line.exit();
                    loop = false;
                    break;
                case Constants.RIGHT_ARROW:
                    line.moveCursorRight();
                    break;
                case Constants.LEFT_ARROW:
                    line.moveCursorLeft();
                    break;
                case Constants.DELETE:
                    str = line.delChar();
                    break;
                case Constants.HOME:
                    line.goHome();
                    break;
                case Constants.END:
                    line.goEnd();
                    break;
                case Constants.SUPR:
                    str = line.suprChar();
                    break;
                case Constants.INSERT:
                    line.insert = !line.insert;
                    line.noti();
                    break;
                default:
                    str = line.addChar(c);
                    break;
            }

        }
        this.unsetRaw();
        return str;

    }

}
