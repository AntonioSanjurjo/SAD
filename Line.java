
public class Line {

    int pos; // possition of the cursor on the line
    StringBuilder str;
    boolean insert; // true = insert mode
                    // false = overwrite mode

    public Line() {
        str = new StringBuilder();
        pos = 0;
        insert = true;
    }

    public String getLine(){
        return str.toString();
    }

    public void home(){
        pos = 0;

    }

    public void end(){
        pos = str.length();
    }

    public void moveCursorLeft(){
        if (pos != 0){
            pos --;
        }
    }

    public void moveCursorRight(){
        if (pos != str.length()){
            pos ++;
        }
    }

    public int getPos(){
        return pos;
    }

    public String addChar(int c){
        if(!insert && str.length()>pos){
            str.setCharAt(pos, (char)c);
        } else {
            str.insert(pos,(char)c);
        }
        pos ++;
        return str.toString();

    }

    public String delChar(){
        if(str.length() == 0){
            return str.toString();
        } else {
            str.deleteCharAt(pos-1);
            pos--;
            return str.toString();
        }

    }

    public String suprChar(){
        if(str.length() == 0 || pos == str.length()){
            return str.toString();
        } else {
            str.deleteCharAt(pos);
            return str.toString();
        }
    }

  }
