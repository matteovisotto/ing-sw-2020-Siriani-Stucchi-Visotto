package it.polimi.ingsw.client.GUI;


import it.polimi.ingsw.client.GUIClient;
import it.polimi.ingsw.model.Gods;
import it.polimi.ingsw.model.Phase;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.messageModel.*;
import it.polimi.ingsw.observer.Observer;
import it.polimi.ingsw.utils.Parser;


import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class Game extends JFrame implements Observer<Object> {

    private GUIClient guiClient;
    private boolean initedBoard = false;
    private Phase phase;
    private JPanel mainPanel, leftPanel, centerPanel, rightPanel, overlayPanel;
    private JLabel messageLabel;
    private JButton startPlayBtn;
    private MessageType messageType=MessageType.PLAYER_NAME;
    private Player player;
    private ClientConfigurator clientConfigurator;
    private String response;
    private final ArrayList<String> multipleSelections = new ArrayList<>();
    private int choice = 1;

    public Game(final GUIClient guiClient){
        this.guiClient = guiClient;
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setTitle("Santorini");
        setResizable(false);
        setLayout();

    }

    public void setPlayer(Player player) {
        this.player = player;
    }
    public void setClientConfigurator(ClientConfigurator clientConfigurator) {
        this.clientConfigurator = clientConfigurator;
    }

    private void setLayout() {
        JLabel backgroud = new JLabel();
        Toolkit tk = Toolkit.getDefaultToolkit();
        Dimension d = tk.getScreenSize();
        this.setSize(d);
        setContentPane(backgroud);
        BufferedImage img = null;
        try {
            img = ImageIO.read(new File("images/home/SantoriniHomeBackground.png"));
            Image dimg = img.getScaledInstance(d.width, d.height,
                    Image.SCALE_SMOOTH);
            ImageIcon imageIcon = new ImageIcon(dimg);
            backgroud.setIcon(imageIcon);
        } catch (IOException e) {
            e.printStackTrace();
        }
        mainPanel = new JPanel(true);
        mainPanel.setLayout(new BorderLayout(0, 0));
        mainPanel.setSize(d);
        mainPanel.setOpaque(false);

        add(mainPanel);

        startPlayBtn = new JButton();
        startPlayBtn.setOpaque(false);
        startPlayBtn.setContentAreaFilled(false);
        startPlayBtn.setBorderPainted(false);
        startPlayBtn.setSize(526*3/4,644*3/4);
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


        mainPanel.add(startPlayBtn, BorderLayout.SOUTH);

        //Add logo
        BufferedImage logo = null;
        try {
            logo = ImageIO.read(new File("images/home/SantoriniHomeLogo.png"));
            Image dimg = logo.getScaledInstance(d.width*3/4, d.height/3,
                    Image.SCALE_SMOOTH);
            ImageIcon imageIcon = new ImageIcon(dimg);
            mainPanel.add(new JLabel(imageIcon), BorderLayout.NORTH);
        } catch (IOException e) {
            e.printStackTrace();
        }

        startPlayBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                guiClient.openInitializator();
            }
        });


        pack();

    }

    private void initGame() {
            this.setEnabled(true);
            clearMainPanel();
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

            leftPanel = new JPanel(true);
            leftPanel.setLayout(new BorderLayout(10, 50));
            leftPanel.setSize(mainPanel.getWidth() / 4, mainPanel.getHeight());
            leftPanel.setOpaque(false);

            mainPanel.add(leftPanel, BorderLayout.WEST);

            centerPanel = new JPanel(true);
            centerPanel.setLayout(new BorderLayout(10, 10));
            centerPanel.setSize(mainPanel.getWidth() / 2, mainPanel.getHeight());
            centerPanel.setOpaque(false);
            mainPanel.add(centerPanel, BorderLayout.CENTER);

            rightPanel = new JPanel(true);
            rightPanel.setLayout(new BorderLayout(10, 10));
            rightPanel.setSize(mainPanel.getWidth() / 4, mainPanel.getHeight());
            rightPanel.setOpaque(false);


            mainPanel.add(rightPanel, BorderLayout.EAST);

            messageLabel = new JLabel();
            messageLabel.setHorizontalTextPosition(SwingConstants.CENTER);
            messageLabel.setHorizontalAlignment(SwingConstants.CENTER);
            messageLabel.setFont(new Font("Comic Sans MS", Font.PLAIN, 18));
            messageLabel.setSize(centerPanel.getWidth(), 100);
            JLabel bottom = new JLabel("");
            bottom.setHorizontalTextPosition(SwingConstants.CENTER);
            bottom.setHorizontalAlignment(SwingConstants.CENTER);
            bottom.setFont(new Font("Comic Sans MS", Font.PLAIN, 18));
            bottom.setSize(centerPanel.getWidth(), 100);

            centerPanel.add(messageLabel, BorderLayout.NORTH);
            centerPanel.add(bottom, BorderLayout.SOUTH);
            BufferedImage messageBoard = null;
            try {
                messageBoard = ImageIO.read(new File("images/Santorini_GenericPopup.png"));
                Image dimg = messageBoard.getScaledInstance(messageLabel.getWidth(), messageLabel.getHeight(),
                        Image.SCALE_SMOOTH);
                ImageIcon imageIcon = new ImageIcon(dimg);
                messageLabel.setIcon(imageIcon);
            } catch (IOException e) {
                e.printStackTrace();
            }

            messageLabel.setForeground(Color.WHITE);
            mainPanel.revalidate();
            mainPanel.repaint();
    }

    private void clearMainPanel(){
        for(Component component: mainPanel.getComponents()){
            mainPanel.remove(component);
        }
        mainPanel.revalidate();
        mainPanel.repaint();
    }

    private void addOpponents() {
        JPanel opponetsPanel = new JPanel(true);
        opponetsPanel.setLayout(new GridLayout(2,1,10,10));
        opponetsPanel.setSize(100,100);
        for(String name: clientConfigurator.getOpponentsNames()){
            JLabel nameLabel = new JLabel();
            nameLabel.setText(name);
            opponetsPanel.add(nameLabel);
        }
        leftPanel.add(opponetsPanel, BorderLayout.NORTH);
    }

    private void setMessageOnPopup(String message) {
        try{
            messageLabel.setText(message);
        } catch (Exception e) {

        }
    }

    private void resetOverlayPanelContent(){
        Component[] components = overlayPanel.getComponents();
        for (Component component : components) {
            overlayPanel.remove(component);
        }
    }

    protected void removeOverlayPanel() {
        resetOverlayPanelContent();
        centerPanel.remove(overlayPanel);
        centerPanel.revalidate();
        centerPanel.repaint();
    }

    private void addOverlayPanel() {
        overlayPanel = new JPanel(true);
        int dim = (mainPanel.getWidth()/2)-5;
        overlayPanel.setSize(dim, dim);
        overlayPanel.setBackground(Color.LIGHT_GRAY);
        overlayPanel.setOpaque(false);
        /*BufferedImage image ;
        try{
            image=ImageIO.read(new File("images/metalPanel_plate.png"));
            Image normal = image.getScaledInstance(overlayPanel.getWidth(), overlayPanel.getHeight(), Image.SCALE_SMOOTH);
            overlayPanel.add(new JLabel(new ImageIcon(normal)));
        }catch (IOException e){
            e.printStackTrace();
        }*/
        centerPanel.add(overlayPanel, BorderLayout.CENTER);
    }

    private void drawCards(){
        final HashMap<JButton, Integer> gods = new HashMap<>();
        setMessageOnPopup("Please select "+ clientConfigurator.getNumberOfPlayer()+ " god cards");
        BufferedImage image;
        final JPanel panel = new JPanel(true);
        panel.setSize(overlayPanel.getWidth()-100, overlayPanel.getHeight());
        panel.setOpaque(false);
        panel.setLayout(new GridLayout(3,3,0,0));
        for (int i=0; i<9; i++) {
            final JButton god = new JButton();
            god.setOpaque(false);
            god.setContentAreaFilled(false);
            god.setBorderPainted(false);
            god.setSize(panel.getWidth()/3,panel.getHeight()/3);
            try{
                String fileName = Gods.getGod(i).toString();
                fileName = fileName.substring(fileName.lastIndexOf('.')+1, fileName.indexOf('@'));
                image=ImageIO.read(new File("images/gods/"+ fileName +".png"));
                Image normal = image.getScaledInstance(god.getWidth(), god.getHeight(), Image.SCALE_AREA_AVERAGING);
                god.setIcon(new ImageIcon(normal));
                panel.add(god);
            }catch (IOException e){
                e.printStackTrace();
            }
            gods.put(god, i);
            god.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    multipleSelections.add(gods.get(e.getSource()).toString());
                        panel.remove((JButton) e.getSource());
                        panel.revalidate();
                        panel.repaint();
                        synchronized (multipleSelections) {
                            if (multipleSelections.size() == clientConfigurator.getNumberOfPlayer()) {
                                StringBuilder stringBuilder = new StringBuilder();
                                for (int i = 0; i < multipleSelections.size(); i++) {
                                    stringBuilder.append(multipleSelections.get(i));
                                    if (i < multipleSelections.size()-1) {
                                        stringBuilder.append(',');
                                        choice++;
                                    }

                                }
                                response = stringBuilder.toString();
                                guiClient.send(response);
                                multipleSelections.clear();
                                removeOverlayPanel();
                            }
                        }
                }
            });
        }

        overlayPanel.add(panel);
    }

    private void pickCard(ViewMessage viewMessage) {

                final ArrayList<String> godsName = new ArrayList<>();
                //Parser
                String[] splitted = viewMessage.getMessage().split("\n");
                String firstGod = Parser.toCapitalize(splitted[1].substring(4).trim());
                String secondGod = Parser.toCapitalize(splitted[2].substring(4).trim());
                godsName.add(firstGod);
                godsName.add(secondGod);
                if (clientConfigurator.getNumberOfPlayer() == 3 && splitted.length > 3) {
                    String thirdGod = Parser.toCapitalize(splitted[3].substring(4).trim());
                    godsName.add(thirdGod);
                }

                final HashMap<JButton, Integer> gods = new HashMap<>();
                setMessageOnPopup("Please select a god card");
                BufferedImage image;
                final JPanel panel = new JPanel(true);
                panel.setSize(overlayPanel.getWidth() - 100, overlayPanel.getHeight());
                panel.setOpaque(false);
                panel.setLayout(new GridLayout(1, clientConfigurator.getNumberOfPlayer(), 0, 0));
                for (int i = 0; i < godsName.size(); i++) {
                    final JButton god = new JButton();
                    god.setOpaque(false);
                    god.setContentAreaFilled(false);
                    god.setBorderPainted(false);
                    god.setSize(panel.getWidth() / clientConfigurator.getNumberOfPlayer(), panel.getHeight() / 3);
                    try {
                        String fileName = godsName.get(i);
                        image = ImageIO.read(new File("images/gods/" + fileName + ".png"));
                        Image normal = image.getScaledInstance(god.getWidth(), god.getHeight(), Image.SCALE_AREA_AVERAGING);
                        god.setIcon(new ImageIcon(normal));
                        panel.add(god);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    gods.put(god, i);
                    god.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            response = gods.get((JButton) e.getSource()).toString();
                            guiClient.send(response);
                            removeOverlayPanel();
                        }
                    });
                    panel.add(god);
                }
                overlayPanel.add(panel);

    }

    private void turnPhaseManager(ViewMessage viewMessage) {
            switch (viewMessage.getMessageType()) {
                case DRAW_CARD:
                    //mostra a video le carte da selezionare
                    //addOpponents();
                    addOverlayPanel();
                    drawCards();
                    break;
                case PICK_CARD:
                    //addOpponents();
                    addOverlayPanel();
                    pickCard(viewMessage);
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
            revalidate();
            repaint();

    }

    private void phaseManager(ViewMessage viewMessage){
            switch (viewMessage.getMessageType()) {
                case PICK_CARD:

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
            revalidate();
            repaint();
    }

    private void configuratorHandler(ViewMessage viewMessage){
        if(viewMessage.getMessageType() == MessageType.WAIT_FOR_START){
            initGame();
            this.initedBoard = true;
            setMessageOnPopup(viewMessage.getMessage());
        }
    }

    private void handleTurnMessage(ViewMessage arg, Player player) {
        if (this.player.equals(player)) {

            if(arg instanceof GameBoardMessage){
                //Update the bord
            }
            setMessageOnPopup(arg.getMessage());
            turnPhaseManager(arg);
        } else {
            phaseManager(arg);
            if(arg instanceof GameBoardMessage){
               //Update board with disabled control
            }
            setMessageOnPopup("It's now " + player.getPlayerName() + "'s turn");
        }
    }

    @Override
    public void update(Object msg) {
        if(msg instanceof String){
            //JOptionPane.showMessageDialog(null, (String) msg);
        } else if (msg instanceof ViewMessage) {
            ViewMessage viewMessage = (ViewMessage) msg;

            this.messageType = viewMessage.getMessageType();
            if(viewMessage.getMessageType() == MessageType.BEGINNING && !this.initedBoard){
                initGame();
                initedBoard = true;
            }
            if(viewMessage instanceof GameMessage) {
                GameMessage gameMessage = (GameMessage) viewMessage;
                handleTurnMessage(gameMessage, gameMessage.getPlayer());
            } else {
               setMessageOnPopup(viewMessage.getMessage());
            }
            configuratorHandler(viewMessage);
        }


    }
}
