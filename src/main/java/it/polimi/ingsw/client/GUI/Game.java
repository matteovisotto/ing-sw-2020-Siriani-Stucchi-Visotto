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
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class Game extends JFrame implements Observer<Object> {

    private final GUIClient guiClient;
    private boolean initedBoard = false;
    private boolean isSimplePlay = true;
    private HashMap<String, String> opponentGods = new HashMap<>();
    private Phase phase;
    public JPanel mainPanel, leftPanel, centerPanel, rightPanel, overlayPanel;
    private JLabel messageLabel;
    private JButton startPlayBtn;
    private MessageType messageType=MessageType.PLAYER_NAME;
    private Player player;
    private ClientConfigurator clientConfigurator;
    private String response;
    private final ArrayList<String> multipleSelections = new ArrayList<>();
    private String chosenCell;
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
            clearGui();
            JLabel background = new JLabel();
            Toolkit tk = Toolkit.getDefaultToolkit();
            Dimension d = tk.getScreenSize();
            this.setSize(d);
            setContentPane(background);
            add(mainPanel);
            BufferedImage img = null;
            try {
                img = ImageIO.read(new File("images/SantoriniBoard.png"));
                Image dimg = img.getScaledInstance(d.width, d.height,
                        Image.SCALE_SMOOTH);
                ImageIcon imageIcon = new ImageIcon(dimg);
                background.setIcon(imageIcon);
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
            rightPanel.setSize(mainPanel.getBounds().width/ 4, mainPanel.getBounds().height);
            rightPanel.setOpaque(false);


            mainPanel.add(rightPanel, BorderLayout.EAST);

            messageLabel = new JLabel();
            messageLabel.setHorizontalTextPosition(SwingConstants.CENTER);
            messageLabel.setHorizontalAlignment(SwingConstants.CENTER);
            messageLabel.setFont(new Font("Comic Sans MS", Font.PLAIN, 18));
            messageLabel.setSize(centerPanel.getWidth(), 100);


            centerPanel.add(messageLabel, BorderLayout.NORTH);
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
            revalidate();
            repaint();
            //revalidation();
            messageLabel.setForeground(Color.WHITE);
    }

    private void revalidation(){
        centerPanel.revalidate();
        centerPanel.repaint();
        leftPanel.revalidate();
        leftPanel.repaint();
        rightPanel.revalidate();
        rightPanel.repaint();

    }

    private void clearGui(){
        for(Component component: mainPanel.getComponents()){
            mainPanel.remove(component);
        }
        getContentPane().removeAll();
        remove(mainPanel);
    }

    private void addOpponents() { try {
        JPanel opponentsPanel = new JPanel(true);
        opponentsPanel.setOpaque(false);
        opponentsPanel.setLayout(new GridLayout(2, 1, 0, 0));
        opponentsPanel.setSize(rightPanel.getWidth()-20, 230 * opponentGods.size());
        for (String opponentName : opponentGods.keySet()) {
            System.out.println(opponentName);
            String godName = opponentGods.get(opponentName);
            JPanel playerPanel = new JPanel(true);
            playerPanel.setSize(opponentsPanel.getWidth(), opponentsPanel.getHeight() / opponentGods.size());
            playerPanel.setOpaque(false);
            playerPanel.setLayout(new BorderLayout(0, 0));
            JLabel nameLabel = new JLabel();
            JLabel godLabel = new JLabel();
            nameLabel.setSize(playerPanel.getWidth(), 70);
            godLabel.setSize(playerPanel.getWidth(), 160);
            BufferedImage god, frame;
            try {
                frame = ImageIO.read(new File("images/opponentNameFrame.png"));
                god = ImageIO.read(new File("images/Podium/" + Parser.toCapitalize(godName) + "_podium.png"));
                Image frameImage = frame.getScaledInstance(nameLabel.getWidth(), nameLabel.getHeight(), Image.SCALE_SMOOTH);
                Image godImage = god.getScaledInstance(godLabel.getWidth(), godLabel.getHeight(), Image.SCALE_SMOOTH);
                nameLabel.setIcon(new ImageIcon(frameImage));
                godLabel.setIcon(new ImageIcon(godImage));
            } catch (IOException e) {
                e.printStackTrace();
            }
            nameLabel.setHorizontalTextPosition(SwingConstants.CENTER);
            nameLabel.setHorizontalAlignment(SwingConstants.CENTER);
            nameLabel.setFont(new Font("Comic Sans MS", Font.PLAIN, 20));
            nameLabel.setText(opponentName);
            nameLabel.setForeground(Color.WHITE);
            playerPanel.add(nameLabel, BorderLayout.SOUTH);
            playerPanel.add(godLabel, BorderLayout.CENTER);
            opponentsPanel.add(playerPanel);
        }
        rightPanel.add(opponentsPanel, BorderLayout.SOUTH);
        revalidate();
        repaint();
    }catch (Exception e){
        System.out.println("From function");
        e.printStackTrace();
    }
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

    public void createBoard() {
        final JPanel boardLayout = new JPanel(true);
        BufferedImage image;
        boardLayout.setSize(centerPanel.getWidth(), centerPanel.getHeight());
        boardLayout.setOpaque(false);
        boardLayout.setLayout(new GridLayout(5,5,0,0));
        for (int i = 0; i < 25; i++) {
            final JButton cell = new JButton();
            cell.setOpaque(false);
            cell.setContentAreaFilled(false);
            cell.setBorderPainted(false);
            cell.setSize(boardLayout.getWidth() / 6 - 30, boardLayout.getHeight() / 6 - 30);
            try{
                image=ImageIO.read(new File("images/metalPanel_plate.png"));
                Image normal = image.getScaledInstance(cell.getWidth(), cell.getHeight(), Image.SCALE_AREA_AVERAGING);
                cell.setIcon(new ImageIcon(normal));
                boardLayout.add(cell,BorderLayout.CENTER);
            }catch (IOException e){
                e.printStackTrace();
            }
            //boardLayout.add(cell);
            cell.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    chosenCell = e.getSource().toString();
                    guiClient.send(chosenCell);
                }
            });
        }
        centerPanel.add(boardLayout);
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

    private void turnPhaseManager(GameMessage gameMessage) {
            switch (gameMessage.getMessageType()) {
                case DRAW_CARD:
                    //mostra a video le carte da selezionare
                    isSimplePlay = false;
                    addOverlayPanel();
                    drawCards();
                    break;
                case PICK_CARD:
                    isSimplePlay = false;
                    addOverlayPanel();
                    pickCard(gameMessage);
                    break;
                case SET_WORKER_1:
                    createBoard();
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

    private void phaseManager(GameMessage gameMessage){
            switch (gameMessage.getMessageType()) {
                case PICK_CARD:
                    break;
                case BEGINNING:
                    break;
                case SET_WORKER_1:
                    try {
                        if (!isSimplePlay && opponentGods.size() != clientConfigurator.getNumberOfPlayer() - 1) {
                            Player opponent = gameMessage.getPlayer();
                            if (!opponentGods.containsKey(opponent.getPlayerName())) {
                                opponentGods.put(opponent.getPlayerName(), opponent.getGodCard().getName());
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case SET_WORKER_2:
                    try {
                        if (!isSimplePlay && opponentGods.size() == clientConfigurator.getNumberOfPlayer() - 1) {
                            addOpponents();
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
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

    private void handleTurnMessage(GameMessage arg, Player player) {
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
