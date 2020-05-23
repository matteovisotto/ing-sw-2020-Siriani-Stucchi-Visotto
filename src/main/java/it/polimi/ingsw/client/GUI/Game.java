package it.polimi.ingsw.client.GUI;

import it.polimi.ingsw.client.GUIClient;
import it.polimi.ingsw.model.Gods;
import it.polimi.ingsw.model.Phase;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.messageModel.GameBoardMessage;
import it.polimi.ingsw.model.messageModel.GameMessage;
import it.polimi.ingsw.model.messageModel.MessageType;
import it.polimi.ingsw.model.messageModel.ViewMessage;
import it.polimi.ingsw.observer.Observer;
import it.polimi.ingsw.view.View;

import javax.imageio.ImageIO;
import javax.naming.spi.DirectoryManager;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Game extends JFrame implements Observer<Object> {

    private GUIClient guiClient;
    private Phase phase;
    private JPanel mainPanel;
    private JLabel messageLabel;
    private JButton startPlayBtn;
    private MessageType messageType=MessageType.PLAYER_NAME;
    private Player player;
    private String response;

    public Game(final GUIClient guiClient){
        this.guiClient = guiClient;
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setTitle("Santorini");
        setResizable(false);
        setLayout();

        startPlayBtn = new JButton();
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

    public void setPlayer(Player player) {
        this.player = player;
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

    private void initGame() {
        this.setEnabled(true);
        this.mainPanel.remove(startPlayBtn);
        messageLabel = new JLabel();
        messageLabel.setHorizontalTextPosition(SwingConstants.CENTER);
        messageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        messageLabel.setFont(new Font("Comic Sans MS", Font.PLAIN, 18));
        messageLabel.setSize(mainPanel.getWidth()/2, 100);
        mainPanel.add(messageLabel, BorderLayout.NORTH);
        BufferedImage img = null;
        try {
            img = ImageIO.read(new File("images/Santorini_GenericPopup.png"));
            Image dimg = img.getScaledInstance(messageLabel.getWidth(), messageLabel.getHeight(),
                    Image.SCALE_SMOOTH);
            ImageIcon imageIcon = new ImageIcon(dimg);
            messageLabel.setIcon(imageIcon);
        } catch (IOException e) {
            e.printStackTrace();
        }

        messageLabel.setForeground(Color.WHITE);
        revalidate();
        repaint();
    }

    private void setMessageOnPopup(String message) {
        try{
            messageLabel.setText(message);
        } catch (Exception e) {

        }
    }

    private void drawCards(){
        JButton gods[]=new JButton[9];
        BufferedImage image;
        //JPanel panel
        for (int i=0; i<9; i++) {
            JButton god=gods[i];
            god.setOpaque(false);
            god.setContentAreaFilled(false);
            god.setBorderPainted(false);
            god.setSize(330,512);
            try{
                image=ImageIO.read(new File("images/gods/"+ Gods.getGod(i).toString().substring(0, 1).toUpperCase() + Gods.getGod(i).toString().substring(1).toLowerCase()+".png"));
                Image normal = image.getScaledInstance(god.getWidth(), god.getHeight(), Image.SCALE_SMOOTH);
                god.setIcon(new ImageIcon(normal));
            }catch (IOException e){
                e.printStackTrace();
            }

        }


        mainPanel.add(startPlayBtn, BorderLayout.CENTER);
        startPlayBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                guiClient.openInitializator();
            }
        });
    }

    private void phaseManager(ViewMessage viewMessage){
        try{
            switch (viewMessage.getMessageType()) {
                case WAIT_FOR_START:
                    initGame();
                    setMessageOnPopup(viewMessage.getMessage());
                    break;
                case DRAW_CARD:
                    //mostra a video le carte da selezionare
                    drawCards();
                    break;
                case PICK_CARD:
                    //mostra le carte selezionate e permette di sceglierne una
                    break;
                case SET_WORKER_1:
                    break;
                case SET_WORKER_2:
                    break;
                case BEGINNING:
                    break;
                case MOVE:
                    break;
                case BUILD:
                    break;
                case USE_POWER:
                    break;
                case PROMETHEUS:
                    break;
                case VICTORY:
                    break;
                case LOSE:
                    break;
                case END_GAME:
                    break;
                default:
                    break;
            }
        }catch(Exception e){
            e.printStackTrace();
        }

    }

    private synchronized void handleTurnMessage(ViewMessage arg, Player player) {
        if (this.player.equals(player)) {
            if(arg instanceof GameBoardMessage){
                //Update the bord
            }
            setMessageOnPopup(arg.getMessage());
        } else if ((arg.getPhase() == Phase.BEGINNING) && !this.player.equals(player)) {
            if(arg instanceof GameBoardMessage){
               //Update board with disabled control
            }
            setMessageOnPopup("It's now " + player.getPlayerName() + "'s turn");
        }
    }

    @Override
    public void update(Object msg) {
        if(msg instanceof String){

        } else if (msg instanceof ViewMessage) {
            ViewMessage viewMessage = (ViewMessage) msg;
            this.messageType=viewMessage.getMessageType();
            if(viewMessage instanceof GameMessage) {
                GameMessage gameMessage = (GameMessage) viewMessage;
                handleTurnMessage(gameMessage, gameMessage.getPlayer());
            } else {
               setMessageOnPopup(viewMessage.getMessage());
            }
            this.phaseManager(viewMessage);
        }


    }
}
