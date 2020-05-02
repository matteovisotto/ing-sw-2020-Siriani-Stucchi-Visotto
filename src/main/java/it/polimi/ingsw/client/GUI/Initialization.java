package it.polimi.ingsw.client.GUI;

import it.polimi.ingsw.client.GUIClient;
import it.polimi.ingsw.model.messageModel.MessageType;
import it.polimi.ingsw.model.messageModel.ViewMessage;
import it.polimi.ingsw.observer.Observer;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.*;

public class Initialization extends JFrame implements Observer<ViewMessage> {
    private JPanel mainPanel; //This contains server status on bottom and container on top
    private final GUIClient guiClient;
    private JLabel serverStatusLabel = new JLabel(); //Label for server status
    JButton sendButton =new JButton("SEND");
    private JPanel contentPanel; //This contains all elements
    private JLabel messageLabel;
    private JTextField jTextField;
    private String returnedMessage = "";
    public Initialization(GUIClient guiClient){
        this.guiClient = guiClient;
        setLayout();
    }

    public GUIClient getGuiClient() {
        return guiClient;
    }

    private void setLayout(){
        mainPanel=new JPanel();
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setBounds(5,5,5,5);
        mainPanel.setLayout(new BorderLayout(10, 10));
        serverStatusLabel.setText("Connection to server established");
        mainPanel.add(serverStatusLabel, BorderLayout.SOUTH);
        contentPanel = new JPanel();
        contentPanel.setLayout(new BorderLayout(10, 10));
        mainPanel.add(contentPanel, BorderLayout.NORTH);
        contentPanel.add(sendButton, BorderLayout.SOUTH);
        messageLabel=new JLabel("prova");
        contentPanel.add(messageLabel, BorderLayout.NORTH);
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
    public void update(ViewMessage msg) {
        setPanelContent(msg.getMessageType());
    }
    private void resetPanelContent(){
        Component[] components = contentPanel.getComponents();
        for (Component component : components) {
            if(component != messageLabel && component!=sendButton){
                contentPanel.remove(component);
            }
        }

        contentPanel.revalidate();
        contentPanel.repaint();
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
                buttonPanel.setLayout(new GridLayout(1, 2, 10, 10));
                final JButton createLobby = new JButton("Create a new lobby");
                final JButton joinLobby = new JButton("Join a lobby");
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
            case WAIT_FOR_START:
            default:
                dispose();
        }
    }
}
