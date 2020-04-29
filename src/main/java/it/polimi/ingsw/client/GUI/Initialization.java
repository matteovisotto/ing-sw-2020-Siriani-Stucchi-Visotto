package it.polimi.ingsw.client.GUI;

import it.polimi.ingsw.client.GUIClient;
import it.polimi.ingsw.model.messageModel.MessageType;
import it.polimi.ingsw.model.messageModel.ViewMessage;
import it.polimi.ingsw.observer.Observer;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Initialization extends JFrame implements Observer<ViewMessage> {
    private JPanel jPanel;
    private final GUIClient GC;
    private JLabel messageLabel;
    private JTextField jTF;
    //private JComboBox<> jCB;
    private String returnedMessage = "";
    public Initialization(GUIClient GC){
        this.GC=GC;
        setLayout();
    }

    public GUIClient getGC() {
        return GC;
    }
    private void setLayout(){
        jPanel=new JPanel();
        messageLabel=new JLabel("prova");
        jTF=new JTextField();
        JButton jButton=new JButton("SEND");
        jPanel.setLayout(new BorderLayout());

        jPanel.add(new JLabel("Connection to server established"), BorderLayout.PAGE_END);
        jPanel.add(messageLabel, BorderLayout.PAGE_START);
        jPanel.add(jTF,BorderLayout.CENTER);
        jPanel.add(jButton, BorderLayout.PAGE_END);
        jButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                GC.send(returnedMessage);
            }
        });
        add(jPanel);
        pack();
    }

    @Override
    public void update(ViewMessage msg) {
        if(msg.getMessageType() == MessageType.PLAYER_NAME)
            // mostri TF
            //jPanel.remove(jTF)
        messageLabel.setText(msg.getMessage());
    }
}
