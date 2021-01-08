import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.*;


public class ClientSwing {

    private static String host;
    private static int port;
    private static String nickname;

    public static void main(String[] args) {

        host = args[0];
        port = Integer.parseInt(args[1]);
        nickname = args[2];


        // Schedule a job for the event-dispatching thread:
        // Creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                createAndShowGUI();
            }
        });

    }

    private static void createAndShowGUI() {

        MySocket serverSocket = new MySocket(host, port);

        //Set the look and feel.
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e){}
        JFrame.setDefaultLookAndFeelDecorated(true);

        // Create and set up the window.
        JFrame frame = new JFrame("Swing Chat");

        frame.addWindowListener(new WindowAdapter() {
        @Override
        public void windowClosing(WindowEvent e) {
            serverSocket.println("-><-=()");
            System.exit(0);
        }
          });

        // Create and set up the content pane
        LaminaClientScreen clientScreen = new LaminaClientScreen();
        clientScreen.setOpaque(true);
        frame.setContentPane(clientScreen);
        frame.getRootPane().setDefaultButton(clientScreen.getSendButton());

        // Display the window
        frame.pack();
        frame.setVisible(true);

        // Input thread
        new Thread(){
            @Override
            public void run(){
                String line;
                while ((line = serverSocket.readLine()) != null) {

                    // If the line contains the keyword, add the nickname to the List Model
                    if (line.contains("?¿=() ")){
                        line = line.replace("?¿=() ", "");
                        if (clientScreen.getListModel().contains(line)){
                            clientScreen.getListModel().removeElement(line);
                            clientScreen.getListModel().addElement(line);
                        } else {
                            clientScreen.getListModel().addElement(line);
                        }

                    // If the line contains the keyword, we notify other users than a customer
                    // has left and we remove it from the List Model
                    } else if (line.contains(" left the chat")){
                        clientScreen.getReceiveMessage().append(line + "\n");
                        line = line.replace(" left the chat", "");
                        clientScreen.getListModel().removeElement(line);

                    // If the line does not match a keyword, we display the message on the screen
                    } else {
                        clientScreen.getReceiveMessage().append(line + "\n");
                    }
                }
            }

        }.start();


        // Output thread
        new Thread(){
            @Override
            public void run(){
                serverSocket.println(nickname);
                String line;

                while(true){
                    line = clientScreen.getSendMessage().getText();
                    if (clientScreen.getButtonPressed()){
                        serverSocket.println(line);
                    }
                    clientScreen.setButtonPressed(false);
                }


            }
        }.start();
    }

}

class LaminaClientScreen extends JPanel implements ActionListener{

    private JTextArea receiveMessage;
    private JButton sendButton;
    private JTextField sendMessage;
    private JPanel in, out, listPanel;
    private JList<String> list;
    private DefaultListModel<String> listModel;
    private Boolean buttonPressed = false;

    public LaminaClientScreen(){
        super(new BorderLayout());

        /*OUT PANEL*/
        out = new JPanel();
        receiveMessage = new JTextArea(20,30);
        receiveMessage.setEditable(false);
        // We add the component to the out panel
        out.add(new JScrollPane(receiveMessage));
        // We add the JPanel out
        add(out, BorderLayout.CENTER);

        /*LIST PANEL*/
        listModel = new DefaultListModel<>();
        //Create the list and put it in a scroll pane.
        list = new JList<>(listModel);
        JScrollPane listScrollPane = new JScrollPane(list);
        listPanel = new JPanel();
        listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.LINE_AXIS)); // Or PAGE_AXIS if we want vertically
        // we add a scroll pane to the list
        listPanel.add(new JScrollPane(list));
        // We add the JPanel to the list and place it
        add(listPanel, BorderLayout.EAST);

        /*IN PANEL*/
        in = new JPanel();
        in.setLayout(new BoxLayout(in, BoxLayout.LINE_AXIS));
        sendMessage = new JTextField(25);
        sendButton = new JButton("Send");
        // Events
        sendMessage.addActionListener(this);
        sendButton.addActionListener(this);
        // Add components to JPanel
        in.add(sendMessage);
        in.add(sendButton);
        // Add JPanel in and place it
        add(in, BorderLayout.PAGE_END);
    }

    @Override
    public void actionPerformed(ActionEvent event){
        Object source = event.getSource();
        String txt = sendMessage.getText();
        // If the event matches sending a message, it displays it
        if (source == sendButton){
            receiveMessage.append("You> " + txt + "\n");
            buttonPressed = true;
        }

    }

    public JButton getSendButton() {
        return sendButton;
    }

    public JTextArea getReceiveMessage() {
        return receiveMessage;
    }

    public JTextField getSendMessage() {
        return sendMessage;
    }

    public Boolean getButtonPressed() {
        return buttonPressed;
    }

    public void setButtonPressed(Boolean buttonPressed) {
        this.buttonPressed = buttonPressed;
    }

    public DefaultListModel<String> getListModel() {
        return listModel;
    }

}
