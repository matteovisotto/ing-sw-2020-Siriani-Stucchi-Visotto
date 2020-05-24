package it.polimi.ingsw.client.GUI;

import it.polimi.ingsw.client.GUIClient;
import it.polimi.ingsw.model.messageModel.MessageType;
import it.polimi.ingsw.model.messageModel.ViewMessage;
import it.polimi.ingsw.observer.Observer;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;

public class Initialization extends JDialog implements Observer<Object> {
    private JPanel mainPanel; //This contains server status on bottom and container on top
    private final GUIClient guiClient;
    private JLabel serverStatusLabel = new JLabel(); //Label for server status
    JButton sendButton = new JButton("Next");
    private JPanel contentPanel; //This contains all elements
    private JLabel messageLabel;
    private JTextField jTextField;
    private String returnedMessage = "";
    public Initialization(GUIClient guiClient) {
        setTitle("Initialization");
        setPreferredSize(new Dimension(400, 170));
        setResizable(false);
        this.guiClient = guiClient;
        try{
            setLayout();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public GUIClient getGuiClient() {
        return guiClient;
    }
    private void setLayout() throws IOException {
        mainPanel = new JPanel(true);
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setBorder(new EmptyBorder(10,10,10,10)); //Add padding
        mainPanel.setLayout(new BorderLayout(10, 10));
        serverStatusLabel.setText("Connection to server established");
        serverStatusLabel.setForeground(new Color(1,140,8));
        mainPanel.add(serverStatusLabel, BorderLayout.SOUTH);
        contentPanel = new JPanel(true);
        contentPanel.setLayout(new BorderLayout(10, 10));
        mainPanel.add(contentPanel, BorderLayout.CENTER);
        contentPanel.add(sendButton, BorderLayout.SOUTH);
        messageLabel = new JLabel("prova");
        contentPanel.add(messageLabel, BorderLayout.NORTH);
        contentPanel.setOpaque(false);
        sendButton.setBackground(new Color(76, 166, 220));
        sendButton.setOpaque(true);
        sendButton.setPreferredSize(new Dimension(150, 25));
        sendButton.setForeground(Color.WHITE);
        sendButton.setBorder(new LineBorder(new Color(62, 136, 180), 2));
        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                guiClient.send(returnedMessage);
            }
        });
        add(mainPanel);
        pack();
    }

    @Override
    public void update(Object msg) {
        if(msg instanceof ViewMessage) {
            setPanelContent(((ViewMessage) msg).getMessageType());
        } else if (msg instanceof String) {

        }
    }

    private void resetPanelContent(){
        Component[] components = contentPanel.getComponents();
        for (Component component : components) {
            if(component != messageLabel && component!=sendButton){
                contentPanel.remove(component);
            }
        }
        this.setSize(400, 170);

    }

    private void setPanelContent(MessageType messageType){
        resetPanelContent();
        switch (messageType){
            case PLAYER_NAME:
                jTextField = new JTextField();
                contentPanel.add(jTextField, BorderLayout.CENTER);
                messageLabel.setText("Please, insert your name");
                jTextField.addKeyListener(new KeyAdapter() {
                    @Override
                    public void keyReleased(KeyEvent e) {
                        JTextField textField = (JTextField) e.getSource();
                        returnedMessage = textField.getText();
                    }
                });
                break;
            case JOIN_OR_CREATE_LOBBY:
                JPanel buttonPanel = new JPanel();
                buttonPanel.setOpaque(false);
                buttonPanel.setLayout(new GridLayout(1, 2, 10, 10));
                final JButton createLobby = new JButton("Create a new lobby");
                final JButton joinLobby = new JButton("Join a lobby");
                createLobby.setPreferredSize(new Dimension(50,60));
                buttonPanel.add(createLobby);
                buttonPanel.add(joinLobby);
                messageLabel.setText("");
                contentPanel.add(buttonPanel, BorderLayout.CENTER);
                createLobby.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        returnedMessage = "1";
                        createLobby.setEnabled(false);
                        joinLobby.setEnabled(true);
                    }
                });
                joinLobby.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        returnedMessage = "2";
                        createLobby.setEnabled(true);
                        joinLobby.setEnabled(false);
                    }
                });
                break;
            case LOBBY_SELECTOR:
                messageLabel.setText("Please select the lobby you want to join");
                JPanel lobbyPanel = new JPanel(new BorderLayout(10,10));
                lobbyPanel.setOpaque(false);
                JButton backButton = new JButton("Back");
                backButton.setSize(70, 45);
                backButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        guiClient.send("0");
                    }
                });
                lobbyPanel.add(backButton, BorderLayout.NORTH);
                this.setSize(400, 400);
                contentPanel.add(lobbyPanel, BorderLayout.CENTER);
                break;
            case LOBBY_NAME:
                messageLabel.setText("Please insert a name for the lobby");
                jTextField = new JTextField();
                contentPanel.add(jTextField, BorderLayout.CENTER);
                jTextField.addKeyListener(new KeyAdapter() {
                    @Override
                    public void keyReleased(KeyEvent e) {
                        JTextField textField = (JTextField) e.getSource();
                        returnedMessage = textField.getText();
                    }
                });
                break;
            case NUMBER_OF_PLAYERS:
                messageLabel.setText("Select how many player you want in the lobby");
                Integer[] num = new Integer[2];
                num[0] = 2; num[1] = 3;
                JComboBox<Integer> numPlayers = new JComboBox<>(num);
                returnedMessage = "2";
                numPlayers.addItemListener(new ItemListener() {
                    // Listening if a new items of the combo box has been selected.
                    public void itemStateChanged(ItemEvent event) {
                        Object item = event.getItem();
                        returnedMessage = item.toString();
                    }
                });
                contentPanel.add(numPlayers, BorderLayout.CENTER);
                break;
            case SIMPLE_OR_NOT:
                messageLabel.setText("Would you like to play in simple mode?");
                JCheckBox simpleMode = new JCheckBox("Use simple mode");
                returnedMessage = "n";
                simpleMode.addChangeListener(new ChangeListener() {
                    @Override
                    public void stateChanged(ChangeEvent e) {
                        AbstractButton abstractButton =
                                (AbstractButton)e.getSource();
                        ButtonModel buttonModel = abstractButton.getModel();
                        boolean selected = buttonModel.isSelected();
                        if(selected){
                            returnedMessage = "y";
                        } else {
                            returnedMessage = "n";
                        }
                    }
                });
                contentPanel.add(simpleMode, BorderLayout.CENTER);

                break;
            default:
                guiClient.closeInitializator();
                dispose();
                return;
        }

        contentPanel.revalidate();
        contentPanel.repaint();
    }
}
