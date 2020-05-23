package it.polimi.ingsw.client.GUI;

import it.polimi.ingsw.client.GUIClient;
import it.polimi.ingsw.model.Phase;
import it.polimi.ingsw.model.messageModel.MessageType;
import it.polimi.ingsw.model.messageModel.ViewMessage;
import it.polimi.ingsw.observer.Observer;

import javax.imageio.ImageIO;
import javax.naming.spi.DirectoryManager;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Game extends JFrame implements Observer<ViewMessage> {

    private GUIClient guiClient;
    private Phase phase;
    private JPanel mainPanel;
    private MessageType messageType=MessageType.PLAYER_NAME;

    public Game(final GUIClient guiClient){
        this.guiClient = guiClient;
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setTitle("Santorini");
        setResizable(false);
        setLayout();

        JButton startPlayBtn = new JButton();
        startPlayBtn.setOpaque(false);
        startPlayBtn.setContentAreaFilled(false);
        startPlayBtn.setBorderPainted(false);
        startPlayBtn.setSize(526/2,644/2);
        BufferedImage normalImage, pressedImage;
        try {
            normalImage = ImageIO.read(new File("images/button-play-normal.png"));
            pressedImage = ImageIO.read(new File("images/button-play-down.png"));
            Image normal = normalImage.getScaledInstance(startPlayBtn.getWidth(), startPlayBtn.getHeight(),
                    Image.SCALE_SMOOTH);
            Image pressed = pressedImage.getScaledInstance(startPlayBtn.getWidth(), startPlayBtn.getHeight(),
                    Image.SCALE_SMOOTH);
            startPlayBtn.setIcon(new ImageIcon(normal));
            startPlayBtn.setPressedIcon(new ImageIcon(pressed));
        } catch (IOException e) {
            e.printStackTrace();
        }


        mainPanel.add(startPlayBtn, BorderLayout.CENTER);
        startPlayBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                guiClient.openInitializator();
            }
        });
    }

    private void setLayout() {
        JLabel backgroud = new JLabel();
        Toolkit tk = Toolkit.getDefaultToolkit();
        Dimension d = tk.getScreenSize();
        this.setSize(d);
        setContentPane(backgroud);
        BufferedImage img = null;
        try {
            img = ImageIO.read(new File("images/SantoriniBoard.png"));
            Image dimg = img.getScaledInstance(d.width, d.height,
                    Image.SCALE_SMOOTH);
            ImageIcon imageIcon = new ImageIcon(dimg);
            backgroud.setIcon(imageIcon);
        } catch (IOException e) {
            e.printStackTrace();
        }
        mainPanel = new JPanel(true);
        mainPanel.setLayout(new BorderLayout(10, 10));
        mainPanel.setSize(d);
        mainPanel.setOpaque(false);

        add(mainPanel);
        pack();

    }

    private void phaseManager() {
        try{
            switch (messageType) {
                case WAIT_FOR_START:
                    this.setEnabled(true);
                    this.mainPanel.remove(0);
                    break;
                default:
                    break;
            }
        }catch(Exception e){
            e.printStackTrace();
        }

    }

    @Override
    public void update(ViewMessage msg) {
        this.phase = msg.getPhase();
        this.messageType=messageType;
        this.phaseManager();

    }
}
