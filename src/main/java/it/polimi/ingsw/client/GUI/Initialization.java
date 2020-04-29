package it.polimi.ingsw.client.GUI;

import it.polimi.ingsw.client.GUIClient;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

public class Initialization extends JFrame {
    private JPanel jPanel;
    private final GUIClient GC;
    private JLabel messageLabel;
    private JTextField jTF;
    //private JComboBox<> jCB;

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

        jPanel.setLayout(new BorderLayout());

        jPanel.add(new JLabel("Connection to server established"), BorderLayout.PAGE_END);
        jPanel.add(messageLabel, BorderLayout.PAGE_START);
        jPanel.add(jTF,BorderLayout.CENTER);

        add(jPanel);
        pack();
    }
}
