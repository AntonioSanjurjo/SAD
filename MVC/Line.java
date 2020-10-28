import java.util.Observable;

public class Line extends Observable {

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

    public int getPos(){
        return pos;
    }

    public char getChar(){
        return str.charAt(pos);
    }

    public void moveCursorLeft(){
        if (pos != 0){
            pos --;
        }
        this.setChanged();
        this.notifyObservers(Constants.UPDATE);
    }

    public void moveCursorRight(){
        if (pos != str.length()){
            pos ++;
        }
        this.setChanged();
        this.notifyObservers(Constants.UPDATE);
    }

    public String addChar(int c){
        if(!insert && str.length()>pos){
            str.setCharAt(pos, (char)c);
        } else {
            str.insert(pos,(char)c);
        }
        pos ++;
        this.setChanged();
        this.notifyObservers(Constants.UPDATE);
        return str.toString();
    }

    public String delChar(){
        if(pos == 0){
            this.setChanged();
            this.notifyObservers(Constants.UPDATE);
            return str.toString();
        } else {
            str.deleteCharAt(pos-1);
            pos--;
            this.setChanged();
            this.notifyObservers(Constants.UPDATE);
            return str.toString();
        }
    }

    public String suprChar(){
        if(str.length() == 0 || pos == str.length()){
            this.setChanged();
            this.notifyObservers(Constants.UPDATE);
            return str.toString();
        } else {
            str.deleteCharAt(pos);
            this.setChanged();
            this.notifyObservers(Constants.UPDATE);
            return str.toString();
        }

    }

    public void goHome(){
        pos = 0;
        this.setChanged();
        this.notifyObservers(Constants.UPDATE);
    }

    public void goEnd(){
        pos = str.length();
        this.setChanged();
        this.notifyObservers(Constants.UPDATE);
    }

    public void noti(){
        this.setChanged();
        this.notifyObservers(Constants.UPDATE);
    }

    public void exit(){
        this.setChanged();
        this.notifyObservers(Constants.UPDATE);
    }

}
