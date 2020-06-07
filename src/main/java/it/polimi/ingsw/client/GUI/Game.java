package it.polimi.ingsw.client.GUI;

import it.polimi.ingsw.client.GUIClient;
import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.messageModel.*;
import it.polimi.ingsw.observer.Observer;
import it.polimi.ingsw.utils.Parser;

import java.util.Timer;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.TimerTask;

public class Game extends JFrame implements Observer<Object> {

    private final GUIClient guiClient;
    private boolean initedBoard = false;
    private boolean isSimplePlay = true;
    private ArrayList<String> opponentsNames = new ArrayList<>();
    private HashMap<String, String> opponentGods = new HashMap<>();
    private HashMap<String, String> myGod = new HashMap<>();
    private JPanel mainPanel, leftPanel, centerPanel, rightPanel, overlayPanel, initialBoardPanel, godPanel, endGamePanel, endGamePanelPlayers, exitGame, playAgain;
    private JLabel messageLabel, background, endGameImage, southPanel;
    private JButton startPlayBtn;
    private MessageType messageType = MessageType.PLAYER_NAME;
    private Player player;
    private Player actualPlayer;
    private ClientConfigurator clientConfigurator;
    private String response;
    private final ArrayList<String> multipleSelections = new ArrayList<>();
    private String chosenCellX;
    private String chosenCellY;
    private JButton[][] board = new JButton[5][5];
    private double value = 0;
    protected int selectedWorker;
    private final HashMap<JButton, Integer> cellsX = new HashMap<>();
    private final HashMap<JButton, Integer> cellsY = new HashMap<>();
    private GraphicsEnvironment ge;
    private Font customFont;
    private boolean isEnded = false;
    private boolean alreadySend = false;

    public Game(final GUIClient guiClient){
        //customCursor();
        setIconImage(Toolkit.getDefaultToolkit().getImage("/images/icon.png"));
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

    public void customCursor() {
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        toolkit.getBestCursorSize(32, 32);
        Image image = toolkit.getImage("images/godpower_hand_select2.png");
        Point hotspot = new Point(0,0);
        Cursor cursor = toolkit.createCustomCursor(image, hotspot, "hand");
        setCursor(cursor);
    }

    private void setLayout() {
        background = new JLabel();
        Toolkit tk = Toolkit.getDefaultToolkit();
        Dimension d = tk.getScreenSize();
        //this.setSize(d);
        setContentPane(background);
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        //togliere il commento qua sotto per metterlo completo full screen.
        //this.setUndecorated(true);
        BufferedImage img;
        try {
            img = ImageIO.read(new File("images/home/SantoriniHomeBackground.png"));
            Image dimg = img.getScaledInstance(d.width, d.height,
                    Image.SCALE_SMOOTH);
            ImageIcon imageIcon = new ImageIcon(dimg);
            background.setIcon(imageIcon);
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
        BufferedImage logo;
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

    private synchronized void initGame() {
        value = 0.484375;
        this.setEnabled(true);
        clearGui();
        JLabel background = new JLabel();
        Toolkit tk = Toolkit.getDefaultToolkit();
        Dimension d = tk.getScreenSize();
        this.setSize(d);
        setContentPane(background);
        add(mainPanel);
        BufferedImage img;
        try {
            img = ImageIO.read(new File("images/SantoriniBoard.png"));
            Image dimg = img.getScaledInstance(d.width, d.height,
                    Image.SCALE_SMOOTH);
            ImageIcon imageIcon = new ImageIcon(dimg);
            background.setIcon(imageIcon);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //mainPanel.setBorder(BorderFactory.createLineBorder(Color.black));

        centerPanel = new JPanel(true);
        centerPanel.setLayout(new BorderLayout(10, 10));
        centerPanel.setPreferredSize(new Dimension((int)(mainPanel.getWidth() * value), (int)(mainPanel.getHeight() - mainPanel.getHeight() * 0.1389))); //930
        centerPanel.setSize((int)(mainPanel.getWidth() * value), (int)(mainPanel.getHeight() - mainPanel.getHeight() * 0.1389));
        centerPanel.setOpaque(false);
        //centerPanel.setBorder(BorderFactory.createLineBorder(Color.black));
        mainPanel.add(centerPanel, BorderLayout.CENTER);

        value = 0.1389;
        southPanel = new JLabel();
        southPanel.setLayout(new BorderLayout(10, 10));
        southPanel.setPreferredSize(new Dimension(mainPanel.getWidth(), (int)(mainPanel.getHeight() * value))); //150
        southPanel.setSize(mainPanel.getWidth(), (int)(mainPanel.getHeight() * value));
        southPanel.setOpaque(false);

        try {
            //create the font to use. Specify the size!
            customFont = Font.createFont(Font.TRUETYPE_FONT, new File("Fonts/LillyBelle.ttf")).deriveFont(40f);
            ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            //register the font
            ge.registerFont(customFont);
        } catch (IOException | FontFormatException e) {
            e.printStackTrace();
        }
        southPanel.setHorizontalTextPosition(SwingConstants.CENTER);
        southPanel.setHorizontalAlignment(SwingConstants.CENTER);
        southPanel.setFont(customFont);
        southPanel.setForeground(Color.RED);


        //centerPanel.setBorder(BorderFactory.createLineBorder(Color.black));
        mainPanel.add(southPanel, BorderLayout.SOUTH);

        value = 0.2942708;
        leftPanel = new JPanel(true);
        leftPanel.setLayout(new BorderLayout(10, 50));
        leftPanel.setPreferredSize(new Dimension((int)(mainPanel.getWidth() * value), mainPanel.getHeight())); //565
        leftPanel.setSize((int)(mainPanel.getWidth() * value), mainPanel.getHeight());
        leftPanel.setOpaque(false);
        //leftPanel.setBorder(BorderFactory.createLineBorder(Color.black));
        mainPanel.add(leftPanel, BorderLayout.WEST);

        rightPanel = new JPanel(true);
        rightPanel.setLayout(new BorderLayout(10, 10));
        rightPanel.setPreferredSize(new Dimension((int)(mainPanel.getWidth() * value), mainPanel.getBounds().height)); //565
        rightPanel.setSize((int)(mainPanel.getWidth() * value), mainPanel.getBounds().height);
        rightPanel.setOpaque(false);
        //rightPanel.setBorder(BorderFactory.createLineBorder(Color.black));


        mainPanel.add(rightPanel, BorderLayout.EAST);

        value = 0.1111111111;
        messageLabel = new JLabel();
        try {
            //create the font to use. Specify the size!
            customFont = Font.createFont(Font.TRUETYPE_FONT, new File("Fonts/LillyBelle.ttf")).deriveFont(25f);
            ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            //register the font
            ge.registerFont(customFont);
        } catch (IOException | FontFormatException e) {
            e.printStackTrace();
        }
        messageLabel.setHorizontalTextPosition(SwingConstants.CENTER);
        messageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        messageLabel.setFont(customFont);
        messageLabel.setPreferredSize(new Dimension(centerPanel.getWidth(), (int)(mainPanel.getHeight() * value))); //120
        messageLabel.setSize(centerPanel.getWidth(), (int)(mainPanel.getHeight() * value)); //120
        messageLabel.setForeground(Color.WHITE);
        BufferedImage messageBoard;
        try {
            messageBoard = ImageIO.read(new File("images/Santorini_GenericPopup.png"));
            Image dimg = messageBoard.getScaledInstance(messageLabel.getWidth(), messageLabel.getHeight(),
                    Image.SCALE_SMOOTH);
            ImageIcon imageIcon = new ImageIcon(dimg);
            messageLabel.setIcon(imageIcon);
        } catch (IOException e) {
            e.printStackTrace();
        }
        centerPanel.add(messageLabel, BorderLayout.NORTH);

        addInitialBoard();
        revalidate();
        repaint();
    }

    private void clearGui(){
        for(Component component: mainPanel.getComponents()){
            mainPanel.remove(component);
        }
        getContentPane().removeAll();
        remove(mainPanel);
    }

    private void addMyCard(GameMessage gameMessage) {
        try {
            JPanel myPanel = new JPanel(true);
            myPanel.setOpaque(false);
            myPanel.setLayout(new BorderLayout());
            myPanel.setSize(leftPanel.getWidth(), leftPanel.getHeight() / 3);
            JPanel playerPanel = new JPanel(true);
            playerPanel.setSize(myPanel.getWidth() / 2, myPanel.getHeight());
            playerPanel.setOpaque(false);
            playerPanel.setLayout(new BorderLayout(0, 0));
            JLabel nameLabel = new JLabel();
            JLabel godLabel = new JLabel();
            value = 0.2;
            nameLabel.setSize((playerPanel.getWidth()), (int) (playerPanel.getHeight() * value)); //70
            //value = 1.7391304347826;
            value = 2;
            godLabel.setSize((playerPanel.getWidth()), (int) (playerPanel.getHeight() * value) / 2); //400
            BufferedImage god, frame;
            if (isSimplePlay){
                try {
                    frame = ImageIO.read(new File("images/myNameFrame.png"));
                    god = ImageIO.read(new File("images/enemy_player.png"));
                    Image frameImage = frame.getScaledInstance(nameLabel.getWidth(), nameLabel.getHeight(), Image.SCALE_AREA_AVERAGING);
                    Image godImage = god.getScaledInstance(godLabel.getWidth(), godLabel.getHeight(), Image.SCALE_AREA_AVERAGING);
                    nameLabel.setIcon(new ImageIcon(frameImage));
                    godLabel.setIcon(new ImageIcon(godImage));
                    godLabel.setHorizontalAlignment(SwingConstants.CENTER);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            else {
                myGod.put(gameMessage.getPlayer().getPlayerName(), gameMessage.getPlayer().getGodCard().getName());
                //myPanel.setBorder(BorderFactory.createLineBorder(Color.black));
                System.out.println(gameMessage.getPlayer().getPlayerName());
                String godName = myGod.get(gameMessage.getPlayer().getPlayerName());
                try {
                    frame = ImageIO.read(new File("images/myNameFrame.png"));
                    god = ImageIO.read(new File("images/God_with_frame/" + Parser.toCapitalize(godName) + ".png"));
                    Image frameImage = frame.getScaledInstance(nameLabel.getWidth(), nameLabel.getHeight(), Image.SCALE_AREA_AVERAGING);
                    Image godImage = god.getScaledInstance(godLabel.getWidth(), godLabel.getHeight(), Image.SCALE_AREA_AVERAGING);
                    nameLabel.setIcon(new ImageIcon(frameImage));
                    godLabel.setIcon(new ImageIcon(godImage));
                    godLabel.setHorizontalAlignment(SwingConstants.CENTER);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            nameLabel.setHorizontalTextPosition(SwingConstants.CENTER);
            nameLabel.setHorizontalAlignment(SwingConstants.CENTER);
            nameLabel.setVerticalTextPosition(SwingConstants.CENTER);
            nameLabel.setVerticalAlignment(SwingConstants.CENTER);
            nameLabel.setFont(customFont);
            int maxLength = Math.min(gameMessage.getPlayer().getPlayerName().length(), 9);
            nameLabel.setText(gameMessage.getPlayer().getPlayerName().substring(0,maxLength));
            nameLabel.setForeground(Color.WHITE);
            playerPanel.add(nameLabel, BorderLayout.NORTH);
            playerPanel.add(godLabel, BorderLayout.CENTER);
            myPanel.add(playerPanel, BorderLayout.NORTH);
            myPanel.setAlignmentX(SwingConstants.CENTER);
            leftPanel.add(myPanel, BorderLayout.NORTH);
        }catch (Exception e){
            System.out.println("From function");
            e.printStackTrace();
        }
    }

    private void addOpponents() {
        try {
            JPanel opponentsPanel = new JPanel(true);
            opponentsPanel.setOpaque(false);
            opponentsPanel.setLayout(new GridLayout(opponentGods.size(), 1, 0, 0));
            opponentsPanel.setSize(rightPanel.getWidth()/2, 230 * opponentGods.size());
            for (String opponentName : opponentGods.keySet()) {
                System.out.println(opponentName);
                String godName = opponentGods.get(opponentName);
                JPanel playerPanel = new JPanel(true);
                playerPanel.setSize(opponentsPanel.getWidth() * 3 / 2, opponentsPanel.getHeight() * 11/10 / opponentGods.size());
                playerPanel.setOpaque(false);
                playerPanel.setLayout(new BorderLayout(0, 0));
                JLabel nameLabel = new JLabel();
                JLabel godLabel = new JLabel();
                value = 0.304347826;
                nameLabel.setSize((playerPanel.getWidth()) / opponentGods.size() + 1, (int)(playerPanel.getHeight() * value)); //70
                value = 1.7391304347826;
                godLabel.setSize((playerPanel.getWidth()) / opponentGods.size() + 1, (int)(playerPanel.getHeight() * value) / opponentGods.size() + 1); //400
                BufferedImage god, frame;
                String filename = "";
                if (clientConfigurator.getOpponentsNames().get(opponentName).equals("red")){
                    filename = "images/opponentNameFrame.png";
                }
                else {
                    filename = "images/opponentGreenNameFrame.png";
                }
                try {
                    frame = ImageIO.read(new File(filename));
                    god = ImageIO.read(new File("images/Podium/" + Parser.toCapitalize(godName) + "_podium.png"));
                    Image frameImage = frame.getScaledInstance(nameLabel.getWidth(), nameLabel.getHeight(), Image.SCALE_AREA_AVERAGING);
                    Image godImage = god.getScaledInstance(godLabel.getWidth(), godLabel.getHeight(), Image.SCALE_AREA_AVERAGING);
                    nameLabel.setIcon(new ImageIcon(frameImage));
                    godLabel.setIcon(new ImageIcon(godImage));
                    godLabel.setHorizontalAlignment(SwingConstants.CENTER);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                nameLabel.setHorizontalTextPosition(SwingConstants.CENTER);
                nameLabel.setHorizontalAlignment(SwingConstants.CENTER);
                nameLabel.setFont(customFont);
                int maxLength = Math.min(opponentName.length(), 9);
                nameLabel.setText(opponentName.substring(0,maxLength));
                nameLabel.setForeground(Color.WHITE);
                playerPanel.add(nameLabel, BorderLayout.SOUTH);
                playerPanel.add(godLabel, BorderLayout.CENTER);
                opponentsPanel.add(playerPanel, BorderLayout.SOUTH);
            }
            opponentsPanel.setAlignmentX(SwingConstants.CENTER);
            rightPanel.add(opponentsPanel, BorderLayout.SOUTH);

        }catch (Exception e){
            System.out.println("From function");
            e.printStackTrace();
        }
    }

    private void addOpponentsSimpleMode() {
        try {
            //rightPanel.setBorder(BorderFactory.createLineBorder(Color.black));
            String filename = "";
            JPanel opponentsPanel = new JPanel(true);
            opponentsPanel.setOpaque(false);
            opponentsPanel.setLayout(new GridLayout(opponentsNames.size(), 1, 0, 0));
            opponentsPanel.setSize(rightPanel.getWidth()/2, rightPanel.getHeight()*2/3);
            //opponentsPanel.setBorder(BorderFactory.createLineBorder(Color.black));
            for (String opponentName : opponentsNames) {
                JPanel playerPanel = new JPanel(true);
                playerPanel.setSize(opponentsPanel.getWidth() * 3 / 2, opponentsPanel.getHeight() / opponentsNames.size());
                playerPanel.setOpaque(false);
                playerPanel.setLayout(new BorderLayout(0, 0));
                JLabel nameLabel = new JLabel();
                JLabel enemyLabel = new JLabel();
                value = 0.2;
                nameLabel.setSize((playerPanel.getWidth() * 9/10), (int)(playerPanel.getHeight() * value)); //70
                value = 0.75;
                enemyLabel.setSize((playerPanel.getWidth() * 9/(10*opponentsNames.size())), (int)(playerPanel.getHeight() * value)); //500
                //enemyLabel.setBorder(BorderFactory.createLineBorder(Color.black));
                BufferedImage enemy, frame;
                if (clientConfigurator.getOpponentsNames().get(opponentName).equals("red")){
                    filename = "images/opponentNameFrame.png";
                }
                else {
                    filename = "images/opponentGreenNameFrame.png";
                }
                try {
                    frame = ImageIO.read(new File(filename));
                    enemy = ImageIO.read(new File("images/enemy_player.png"));
                    Image frameImage = frame.getScaledInstance(nameLabel.getWidth(), nameLabel.getHeight(), Image.SCALE_AREA_AVERAGING);
                    Image enemyImage = enemy.getScaledInstance(enemyLabel.getWidth(), enemyLabel.getHeight(), Image.SCALE_AREA_AVERAGING);
                    nameLabel.setIcon(new ImageIcon(frameImage));
                    enemyLabel.setIcon(new ImageIcon(enemyImage));
                    enemyLabel.setHorizontalAlignment(SwingConstants.CENTER);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                nameLabel.setHorizontalTextPosition(SwingConstants.CENTER);
                nameLabel.setHorizontalAlignment(SwingConstants.CENTER);
                nameLabel.setFont(customFont);
                int maxLength = Math.min(opponentName.length(), 9);
                nameLabel.setText(opponentName.substring(0,maxLength));
                nameLabel.setForeground(Color.WHITE);
                playerPanel.add(nameLabel, BorderLayout.SOUTH);
                playerPanel.add(enemyLabel, BorderLayout.CENTER);
                opponentsPanel.add(playerPanel, BorderLayout.SOUTH);
            }
            opponentsPanel.setAlignmentX(SwingConstants.CENTER);
            rightPanel.add(opponentsPanel, BorderLayout.SOUTH);

        }catch (Exception e){
            System.out.println("From function");
            e.printStackTrace();
        }
    }

    private void setMessageOnPopup(String message) {
        try{
            messageLabel.setText(message);
        } catch (Exception e) {
            //e.printStackTrace();
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
        for (Component component: overlayPanel.getComponents()){
            overlayPanel.remove(component);
        }
        centerPanel.remove(overlayPanel);
        centerPanel.revalidate();
        centerPanel.repaint();
    }

    private void resetOverlayPanel(){
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++){
                try{
                    for (ActionListener actionListener :((JButton)overlayPanel.getComponent(i*5+j)).getActionListeners()) {
                        ((JButton)overlayPanel.getComponent(i*5+j)).removeActionListener(actionListener);
                    }
                    overlayPanel.getComponent(i*5+j).setVisible(false);
                }catch(ArrayIndexOutOfBoundsException e){
                    //e.printStackTrace();
                }

            }
        }
    }

    private void resetBoardPanel(){
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++){
                try{
                    for (ActionListener actionListener:((JButton)initialBoardPanel.getComponent(i*5+j)).getActionListeners()) {
                        ((JButton)initialBoardPanel.getComponent(i*5+j)).removeActionListener(actionListener);
                    }
                }catch(ArrayIndexOutOfBoundsException e){
                    //e.printStackTrace();
                }

            }
        }
    }

    private void resetGodPanel(){
        try{
            for (Component component: godPanel.getComponents()){
                godPanel.remove(component);
            }
            leftPanel.remove(godPanel);
            leftPanel.revalidate();
            leftPanel.repaint();
        }catch(NullPointerException e){
            //ignore
        }

    }

    private void addOverlayPanel() {
        BufferedImage image;
        overlayPanel = new JPanel(true);
        overlayPanel.setLayout(new GridLayout(5,5,0,0));
        overlayPanel.setPreferredSize(new Dimension(centerPanel.getWidth(), centerPanel.getHeight()));
        overlayPanel.setSize(centerPanel.getWidth(), centerPanel.getHeight());
        overlayPanel.setOpaque(false);
        overlayPanel.setBorder(BorderFactory.createEmptyBorder());
        centerPanel.add(overlayPanel, BorderLayout.CENTER);
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++){
                final JButton cell = new JButton();
                cell.setBorder(BorderFactory.createEmptyBorder());
                cell.setOpaque(false);
                cell.setContentAreaFilled(false);
                cell.setBorderPainted(false);
                cell.setSize(overlayPanel.getWidth() / 5, overlayPanel.getHeight() / 5);
                try{
                    image = ImageIO.read(new File("images/blue_square.png"));
                    Image normal = image.getScaledInstance(cell.getWidth(), cell.getHeight(), Image.SCALE_AREA_AVERAGING);
                    cell.setIcon(new ImageIcon(normal));
                    overlayPanel.add(cell,BorderLayout.CENTER);
                }catch (IOException e){
                    e.printStackTrace();
                }
                cell.setVisible(false);
                cellsX.put(cell,i);
                cellsY.put(cell,j);
            }
        }
        centerPanel.revalidate();
        centerPanel.repaint();
    }

    public void addInitialBoard(){
        initialBoardPanel = new JPanel();
        initialBoardPanel.setLayout(new GridLayout(5,5,0,0));
        initialBoardPanel.setPreferredSize(new Dimension(centerPanel.getWidth(), centerPanel.getHeight()));
        initialBoardPanel.setSize(centerPanel.getWidth(), centerPanel.getHeight());
        initialBoardPanel.setOpaque(false);
        initialBoardPanel.setBorder(BorderFactory.createEmptyBorder());
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++){
                board[i][j] = new JButton();
                board[i][j].setOpaque(false);
                board[i][j].setContentAreaFilled(false);
                board[i][j].setBorderPainted(false);
                board[i][j].setPreferredSize(new Dimension(initialBoardPanel.getWidth()/5, initialBoardPanel.getHeight()/5));
                board[i][j].setSize(initialBoardPanel.getWidth()/5, initialBoardPanel.getHeight()/5);
                board[i][j].setBorder(BorderFactory.createEmptyBorder());
                board[i][j].setEnabled(false);
                board[i][j].setVisible(false);
                initialBoardPanel.add(board[i][j]);
            }
        }
        centerPanel.add(initialBoardPanel,BorderLayout.CENTER);
    }

    private void drawCards(){
        final HashMap<JButton, Integer> gods = new HashMap<>();
        setMessageOnPopup("Please select " + clientConfigurator.getNumberOfPlayer() + " god cards");
        BufferedImage image;
        final JPanel panel = new JPanel(true);
        panel.setSize(centerPanel.getWidth() - 100, (int)(mainPanel.getHeight() - mainPanel.getHeight() * 0.1389));
        panel.setOpaque(false);
        panel.setLayout(new GridLayout(3,3,10,10));
        for (int i=0; i<9; i++) {
            final JButton god = new JButton();
            god.setOpaque(false);
            god.setContentAreaFilled(false);
            god.setBorderPainted(false);
            god.setSize(panel.getWidth()/3 - 30,panel.getHeight()/3 - 100);
            try{
                String fileName = Gods.getGod(i).toString();
                fileName = fileName.substring(fileName.lastIndexOf('.')+1, fileName.indexOf('@'));
                image=ImageIO.read(new File("images/God_with_frame/"+ fileName +".png"));
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
                    multipleSelections.add(gods.get((JButton) e.getSource()).toString());
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
                                }
                            }
                            response = stringBuilder.toString();
                            guiClient.send(response);
                            multipleSelections.clear();
                            centerPanel.remove(panel);
                            centerPanel.revalidate();
                            centerPanel.repaint();
                        }
                    }
                }
            });
        }

        centerPanel.add(panel);
        centerPanel.revalidate();
        centerPanel.repaint();
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
        panel.setSize(centerPanel.getWidth() - 100, centerPanel.getHeight());
        panel.setOpaque(false);
        panel.setLayout(new GridLayout(2,2,0,0));
        for (int i = 0; i < godsName.size(); i++) {
            final JButton god = new JButton();
            god.setOpaque(false);
            god.setContentAreaFilled(false);
            god.setBorderPainted(false);
            god.setSize(panel.getWidth() / 3 - 30, panel.getHeight() / 3 - 30);
            try {
                String fileName = godsName.get(i);
                image = ImageIO.read(new File("images/God_with_frame/" + fileName + ".png"));
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
                    centerPanel.remove(panel);
                    centerPanel.revalidate();
                    centerPanel.repaint();
                }
            });
            panel.add(god);

        }
        centerPanel.add(panel);
        centerPanel.revalidate();
        centerPanel.repaint();
    }

    public void placeWorker(Board board) {
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++){
                if(board.getCell(i,j).isFree()){
                    (overlayPanel.getComponent(i*5+j)).setVisible(true);
                }
                ((JButton)overlayPanel.getComponent(i*5+j)).addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        StringBuilder stringBuilder = new StringBuilder();
                        chosenCellX = cellsX.get((JButton) e.getSource()).toString();
                        chosenCellY = cellsY.get((JButton) e.getSource()).toString();
                        stringBuilder.append(chosenCellX);
                        stringBuilder.append(",");
                        stringBuilder.append(chosenCellY);
                        response = stringBuilder.toString();
                        guiClient.send(response);
                        if(messageType==MessageType.SET_WORKER_2){
                            player.setWorkers(new Worker(new Cell(Integer.parseInt(chosenCellX), Integer.parseInt(chosenCellY))));
                        }
                        resetOverlayPanel();
                    }
                });
            }
        }
        centerPanel.revalidate();
        centerPanel.repaint();
    }

    private void prepareMove(GameMessage gameMessage){
        for(int i=0; i<5; i++){
            for(int j=0;j<5;j++){
                boolean mine=false;
                Cell cell = ((GameBoardMessage)gameMessage).getBoard().getCell(i,j);
                try{
                    if(player.getWorker(0).getCell().equals(cell) || player.getWorker(1).getCell().equals(cell)){
                        mine=true;
                    }
                }catch (IndexOutOfBoundsException e2){
                    //mine=false;
                }
                if(mine){
                    board[i][j].addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            boolean stop=false;
                            ((JButton)e.getSource()).setSelected(false);
                            for(int i=0; i<5; i++){
                                for(int j=0; j<5; j++){
                                    if(e.getSource() == board[i][j]){
                                        performMove(i,j);
                                        stop=true;
                                        break;
                                    }
                                }
                                if(stop){
                                    break;
                                }
                            }
                        }
                    });
                }
            }
        }
    }

    private void performMove(int x, int y) {
        if(player.getWorker(0).getCell().getX()==x && player.getWorker(0).getCell().getY()==y){
            this.selectedWorker=0;
        } else if(player.getWorker(1).getCell().getX()==x && player.getWorker(1).getCell().getY()==y){
            this.selectedWorker=1;
        } else {
            setMessageOnPopup("This is not your worker");
            return;
        }
        resetOverlayPanel();
        for (int i = x-1; i <= x+1; i++) {
            for (int j = y-1; j <= y+1; j++){
                try{
                    if(!(i==x && j==y) && i>=0 && j>=0 && i<=4 && j<=4 && !(player.getWorker(0).getCell().getX()==i && player.getWorker(0).getCell().getY()==j) && !(player.getWorker(1).getCell().getX()==i && player.getWorker(1).getCell().getY()==j)){
                        (overlayPanel.getComponent(i*5+j)).setVisible(true);
                        (overlayPanel.getComponent(i*5+j)).setEnabled(true);
                        ((JButton)overlayPanel.getComponent(i*5+j)).addActionListener(new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                ((JButton)e.getSource()).setSelected(false);
                                StringBuilder stringBuilder = new StringBuilder();
                                chosenCellX = cellsX.get((JButton) e.getSource()).toString();
                                chosenCellY = cellsY.get((JButton) e.getSource()).toString();
                                stringBuilder.append(selectedWorker);
                                stringBuilder.append(",");
                                stringBuilder.append(chosenCellX);
                                stringBuilder.append(",");
                                stringBuilder.append(chosenCellY);
                                response = stringBuilder.toString();
                                guiClient.send(response);
                            }
                        });
                        final int ii=i;
                        final int jj=j;
                        initialBoardPanel.getComponent(i*5+j).setEnabled(true);
                        ((JButton)initialBoardPanel.getComponent(i*5+j)).addActionListener(new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                ((JButton)e.getSource()).setSelected(false);
                                StringBuilder stringBuilder = new StringBuilder();
                                chosenCellX = String.valueOf(ii);
                                chosenCellY = String.valueOf(jj);
                                stringBuilder.append(selectedWorker);
                                stringBuilder.append(",");
                                stringBuilder.append(chosenCellX);
                                stringBuilder.append(",");
                                stringBuilder.append(chosenCellY);
                                response = stringBuilder.toString();
                                guiClient.send(response);
                            }
                        });
                    }

                }catch(Exception e){
                    e.printStackTrace();
                }

            }
        }
    }

    private void performBuild(){
        int x=player.getWorker(selectedWorker).getCell().getX();
        int y=player.getWorker(selectedWorker).getCell().getY();
        for (int i = x-1; i <= x+1; i++) {
            for (int j = y-1; j <= y+1; j++){
                try{
                    if(!(i==x && j==y) && i>=0 && j>=0 && i<=4 && j<=4 && !(player.getWorker(0).getCell().getX()==i && player.getWorker(0).getCell().getY()==j) && !(player.getWorker(1).getCell().getX()==i && player.getWorker(1).getCell().getY()==j)){
                        (overlayPanel.getComponent(i*5+j)).setVisible(true);
                        (overlayPanel.getComponent(i*5+j)).setEnabled(true);
                        ((JButton)overlayPanel.getComponent(i*5+j)).addActionListener(new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                StringBuilder stringBuilder = new StringBuilder();
                                chosenCellX = cellsX.get((JButton) e.getSource()).toString();
                                chosenCellY = cellsY.get((JButton) e.getSource()).toString();
                                stringBuilder.append(chosenCellX);
                                stringBuilder.append(",");
                                stringBuilder.append(chosenCellY);
                                response = stringBuilder.toString();
                                guiClient.send(response);
                            }
                        });
                        (initialBoardPanel.getComponent(i*5+j)).setEnabled(true);
                        ((JButton)initialBoardPanel.getComponent(i*5+j)).addActionListener(new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                StringBuilder stringBuilder = new StringBuilder();
                                for(int i1=0; i1<5; i1++){
                                    for(int j1=0; j1<5; j1++){
                                        if(e.getSource() == board[i1][j1]){
                                            chosenCellX = String.valueOf(i1);
                                            chosenCellY = String.valueOf(j1);
                                        }
                                    }
                                }
                                stringBuilder.append(chosenCellX);
                                stringBuilder.append(",");
                                stringBuilder.append(chosenCellY);
                                response = stringBuilder.toString();
                                guiClient.send(response);
                            }
                        });
                    }

                }catch(Exception e){
                    e.printStackTrace();
                }

            }
        }
    }

    private void setCell(JButton cell, Blocks blocks, boolean isFree, String color){
        BufferedImage image;
        String path="images/Blocks/";
        boolean isVisible=true;
        try{
            switch(blocks){
                case EMPTY:
                    isVisible=!isFree;
                    break;
                case LEVEL1:
                    path+="level1";
                    break;
                case LEVEL2:
                    path+="level2";
                    break;
                case LEVEL3:
                    path+="level3";
                    break;
                case DOME:
                    path+="dome";
                    break;
            }
            if(!isFree){
                if(color.equals("blue")){
                    path+="_me";
                }
                else {
                    path+="_enemy_" + color;
                }
            }
            path+=".png";
            if(isVisible){
                image = ImageIO.read(new File(path));
                Image normal = image.getScaledInstance(cell.getWidth(), cell.getHeight(), Image.SCALE_AREA_AVERAGING);
                cell.setIcon(new ImageIcon(normal));
                cell.setDisabledIcon(new ImageIcon(normal));
            }
            cell.setVisible(isVisible);

        }catch(IOException e){
            e.printStackTrace();
        }

    }

    private void removeBoard(){
        for (Component component: leftPanel.getComponents()){
            leftPanel.remove(component);
        }
        mainPanel.remove(leftPanel);
        mainPanel.revalidate();
        mainPanel.repaint();
        for (Component component: rightPanel.getComponents()){
            rightPanel.remove(component);
        }
        mainPanel.remove(rightPanel);
        mainPanel.revalidate();
        mainPanel.repaint();
        for (Component component: southPanel.getComponents()){
            southPanel.remove(component);
        }
        mainPanel.remove(southPanel);
        mainPanel.revalidate();
        mainPanel.repaint();
        for (Component component: initialBoardPanel.getComponents()){
            initialBoardPanel.remove(component);
        }
        centerPanel.remove(initialBoardPanel);
        centerPanel.revalidate();
        centerPanel.repaint();
        for (Component component: messageLabel.getComponents()){
            messageLabel.remove(component);
        }
        centerPanel.remove(messageLabel);
        centerPanel.revalidate();
        centerPanel.repaint();
        removeOverlayPanel();
        for (Component component: centerPanel.getComponents()){
            centerPanel.remove(component);
        }
        mainPanel.remove(centerPanel);
        mainPanel.revalidate();
        mainPanel.repaint();
        for (Component component: mainPanel.getComponents()){
            mainPanel.remove(component);
        }
        mainPanel.revalidate();
        mainPanel.repaint();
    }

    private void endGame(HashMap<Player, Integer> podium){
        isEnded = true;
        removeBoard();
        endGamePanel = new JPanel();
        endGamePanel.setLayout(new BorderLayout(0,0));
        endGamePanel.setPreferredSize(new Dimension(mainPanel.getWidth(), mainPanel.getHeight()));
        endGamePanel.setSize(mainPanel.getWidth(), mainPanel.getHeight());
        //endGamePanel.setBorder(BorderFactory.createLineBorder(Color.black));
        endGamePanel.setOpaque(false);
        mainPanel.add(endGamePanel);
        //da qua sotto in avanti dovrebbe cambiare immagine ma non la cambia
        endGameImage = new JLabel();
        endGameImage.setPreferredSize(new Dimension(endGamePanel.getWidth(), endGamePanel.getHeight()));
        endGameImage.setSize(endGamePanel.getWidth(), endGamePanel.getHeight());
        BufferedImage img;
        try {
            img = ImageIO.read(new File("images/End_game.png"));
            Image dimg = img.getScaledInstance(endGameImage.getWidth(),endGameImage.getHeight(), Image.SCALE_SMOOTH);
            ImageIcon imageIcon = new ImageIcon(dimg);
            endGameImage.setIcon(imageIcon);
            mainPanel.add(endGameImage, BorderLayout.CENTER);
        } catch (IOException e) {
            e.printStackTrace();
        }
        mainPanel.revalidate();
        mainPanel.repaint();
        setJPanelsOnEndGame(podium);
    }

    private void removeEndGameLayout(){
        for (Component component: southPanel.getComponents()){
            southPanel.remove(component);
        }
        endGamePanel.remove(southPanel);
        endGamePanel.revalidate();
        endGamePanel.repaint();
        for (Component component: endGamePanelPlayers.getComponents()){
            endGamePanelPlayers.remove(component);
        }
        endGamePanel.remove(endGamePanelPlayers);
        endGamePanel.revalidate();
        endGamePanel.repaint();
        for (Component component: playAgain.getComponents()){
            playAgain.remove(component);
        }
        endGamePanel.remove(playAgain);
        endGamePanel.revalidate();
        endGamePanel.repaint();
        for (Component component: exitGame.getComponents()){
            exitGame.remove(component);
        }
        endGamePanel.remove(exitGame);
        endGamePanel.revalidate();
        endGamePanel.repaint();
        for (Component component: messageLabel.getComponents()){
            messageLabel.remove(component);
        }
        endGamePanel.remove(messageLabel);
        endGamePanel.revalidate();
        endGamePanel.repaint();
        for (Component component: endGamePanel.getComponents()){
            endGamePanel.remove(component);
        }
        remove(endGamePanel);
        mainPanel.revalidate();
        mainPanel.repaint();
        resetNewGame();
        revalidate();
        repaint();
    }

    private void resetNewGame(){
        isEnded = false;
        initedBoard = false;
        opponentsNames.clear();
        opponentGods.clear();
        myGod.clear();
        selectedWorker = -1;
        response = "";
        multipleSelections.clear();
        chosenCellY = "";
        chosenCellX = "";
        cellsX.clear();
        cellsY.clear();
        messageType = MessageType.PLAYER_NAME;
        //clientConfigurator = null;
        //player = null;
    }

    private void setJPanelsOnEndGame(HashMap<Player, Integer> podium){
        endGamePanelPlayers = new JPanel();
        endGamePanelPlayers.setLayout(new BorderLayout(10,60));
        value = 0.552083;
        endGamePanelPlayers.setPreferredSize(new Dimension((int)(endGamePanel.getWidth() * value), (int)(endGamePanel.getHeight() - endGamePanel.getHeight() * 0.1389))); //1060
        endGamePanelPlayers.setSize((int)(endGamePanel.getWidth() * value), (int)(endGamePanel.getHeight() - endGamePanel.getHeight() * 0.1389));
        //endGamePanelPlayers.setBorder(BorderFactory.createLineBorder(Color.black));
        endGamePanelPlayers.setOpaque(false);
        endGamePanel.add(endGamePanelPlayers, BorderLayout.CENTER);

        value = 0.2129629;
        southPanel = new JLabel();
        southPanel.setLayout(new BorderLayout(10, 10));
        southPanel.setPreferredSize(new Dimension(endGamePanel.getWidth(), (int)(endGamePanel.getHeight() * value))); //210
        southPanel.setSize(endGamePanel.getWidth(), (int)(endGamePanel.getHeight() * value));
        southPanel.setOpaque(false);
        endGamePanel.add(southPanel, BorderLayout.SOUTH);

        value = 0.260416;
        playAgain = new JPanel(true);
        playAgain.setLayout(new BorderLayout(10, 50));
        playAgain.setPreferredSize(new Dimension((int)(endGamePanel.getWidth() * value), endGamePanel.getHeight())); //500
        playAgain.setSize((int)(endGamePanel.getWidth() * value), endGamePanel.getHeight());
        playAgain.setOpaque(false);
        JButton playAgainButton = new JButton();
        playAgainButton.setSize(playAgain.getWidth()/2, playAgain.getHeight()/4);
        playAgainButton.setOpaque(false);
        playAgainButton.setContentAreaFilled(false);
        playAgainButton.setBorderPainted(false);

        playAgainButton.setHorizontalAlignment(SwingConstants.CENTER);
        BufferedImage imagePlay, imagePlayPressed;
        try{
            imagePlay = ImageIO.read(new File("images/button-play-again-normal.png"));
            imagePlayPressed = ImageIO.read(new File("images/button-play-again-down.png"));
            Image playAgainImage = imagePlay.getScaledInstance(playAgainButton.getWidth(), playAgainButton.getHeight(), Image.SCALE_AREA_AVERAGING);
            Image playAgainImagePressed = imagePlayPressed.getScaledInstance(playAgainButton.getWidth(), playAgainButton.getHeight(), Image.SCALE_AREA_AVERAGING);
            playAgainButton.setIcon(new ImageIcon(playAgainImage));
            playAgainButton.setPressedIcon(new ImageIcon(playAgainImagePressed));
            playAgain.add(playAgainButton,BorderLayout.CENTER);
        }catch(Exception e){
            e.printStackTrace();
        }
        playAgainButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                guiClient.send("y");
                removeEndGameLayout();
                //initGame();
                //setMessageOnPopup("Waiting for other players");
            }
        });

        endGamePanel.add(playAgain, BorderLayout.WEST);

        exitGame = new JPanel(true);
        exitGame.setLayout(new BorderLayout(10, 10));
        exitGame.setPreferredSize(new Dimension((int)(endGamePanel.getWidth() * value), endGamePanel.getBounds().height)); //500
        exitGame.setSize((int)(endGamePanel.getWidth() * value), endGamePanel.getBounds().height);
        exitGame.setOpaque(false);
        JButton exitButton = new JButton();
        exitButton.setSize(exitGame.getWidth()/2, exitGame.getHeight()/4);
        exitButton.setOpaque(false);
        exitButton.setContentAreaFilled(false);
        exitButton.setBorderPainted(false);

        exitButton.setHorizontalAlignment(SwingConstants.CENTER);
        BufferedImage image, image2;
        try{
            image = ImageIO.read(new File("images/btn-exit-normal.png"));
            Image exit = image.getScaledInstance(exitButton.getWidth(), exitButton.getHeight(), Image.SCALE_AREA_AVERAGING);
            image2 = ImageIO.read(new File("images/btn-exit-down.png"));
            Image exit_pressed = image2.getScaledInstance(exitButton.getWidth(), exitButton.getHeight(), Image.SCALE_AREA_AVERAGING);
            exitButton.setIcon(new ImageIcon(exit));
            exitButton.setPressedIcon(new ImageIcon(exit_pressed));
            exitGame.add(exitButton,BorderLayout.CENTER);
        }catch(Exception e){
            e.printStackTrace();
        }

        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                guiClient.send("n");
                dispose();
            }
        });
        endGamePanel.add(exitGame, BorderLayout.EAST);

        value = 0.199074;
        messageLabel = new JLabel();
        try {
            //create the font to use. Specify the size!
            customFont = Font.createFont(Font.TRUETYPE_FONT, new File("Fonts/LillyBelle.ttf")).deriveFont(25f);
            ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            //register the font
            ge.registerFont(customFont);
        } catch (IOException | FontFormatException e) {
            e.printStackTrace();
        }
        messageLabel.setHorizontalTextPosition(SwingConstants.CENTER);
        messageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        messageLabel.setFont(customFont);
        messageLabel.setPreferredSize(new Dimension(endGamePanelPlayers.getWidth(), (int)(endGamePanel.getHeight() * value))); //215
        messageLabel.setSize(endGamePanelPlayers.getWidth(), (int)(endGamePanel.getHeight() * value));
        endGamePanel.add(messageLabel, BorderLayout.NORTH);

        BufferedImage messageBoard;
        try {
            messageBoard = ImageIO.read(new File("images/Santorini_GenericPopup.png"));
            Image dimg = messageBoard.getScaledInstance(messageLabel.getWidth(), messageLabel.getHeight(), Image.SCALE_SMOOTH);
            ImageIcon imageIcon = new ImageIcon(dimg);
            messageLabel.setIcon(imageIcon);
        } catch (IOException e) {
            e.printStackTrace();
        }
        messageLabel.setForeground(Color.WHITE);
        addPlayersEndGame(podium);
    }

    private void addPlayersEndGame(HashMap<Player, Integer> podium){
        double value2 = 0.8;
        double name = 0.5;
        double height = 0.2;
        String[] podiumNames = new String[3];
        String[] realPodiumNames = new String[3];
        JLabel centerEnd = new JLabel();
        String[] s=new String[3];
        centerEnd.setLayout(new BorderLayout());
        centerEnd.setPreferredSize(new Dimension(2,50));
        centerEnd.setSize(2,50);
        centerEnd.setOpaque(false);
        endGamePanelPlayers.add(centerEnd,BorderLayout.CENTER);
        if(!isSimplePlay){
            for (Player player: podium.keySet()) {
                s[podium.get(player)-1]=player.getGodCard().getName();
            }
        }
        else{
            for (Player player: podium.keySet()) {
                if(player.equals(this.player)){
                    s[podium.get(player)-1] = "our";
                }
                else if (clientConfigurator.getOpponentsNames().get(player.getPlayerName()).equals("red")){
                    s[podium.get(player)-1] = "Enemy_red";
                }
                else {
                    s[podium.get(player)-1] = "Enemy_green";
                }
            }
        }
        for (Player player: podium.keySet()) {
            if(player.equals(this.player)){
                podiumNames[podium.get(player)-1] = "Our";
                realPodiumNames[podium.get(player)-1] = player.getPlayerName();
            }
            else if (clientConfigurator.getOpponentsNames().get(player.getPlayerName()).equals("red")){
                podiumNames[podium.get(player)-1] = "Enemy_red";
                realPodiumNames[podium.get(player)-1] = player.getPlayerName();
            }
            else {
                podiumNames[podium.get(player)-1] = "Enemy_green";
                realPodiumNames[podium.get(player)-1] = player.getPlayerName();
            }
        }

        JPanel winnerPanel = new JPanel();
        winnerPanel.setLayout(new BorderLayout(0,0));
        winnerPanel.setPreferredSize(new Dimension(endGamePanelPlayers.getWidth()/2,endGamePanelPlayers.getHeight()/2));
        winnerPanel.setSize(endGamePanelPlayers.getWidth()/2,endGamePanelPlayers.getHeight()/2);
        winnerPanel.setOpaque(false);
        JLabel winnerName = new JLabel();
        winnerName.setPreferredSize(new Dimension((int)(winnerPanel.getWidth() * name),(int)(winnerPanel.getHeight() * height)));
        winnerName.setSize((int)(winnerPanel.getWidth() * name),(int)(winnerPanel.getHeight() * height));
        winnerName.setOpaque(false);
        winnerName.setHorizontalAlignment(SwingConstants.CENTER);
        winnerName.setFont(customFont);
        int maxLength = Math.min(realPodiumNames[0].length(), 9);
        winnerName.setText(realPodiumNames[0].substring(0,maxLength));
        winnerName.setForeground(Color.WHITE);
        winnerName.setHorizontalTextPosition(SwingConstants.CENTER);
        JLabel winner = new JLabel();
        value = 0.6;
        winner.setPreferredSize(new Dimension((int)(winnerPanel.getWidth() * value),(int)(winnerPanel.getHeight() * value2)));
        winner.setSize((int)(winnerPanel.getWidth() * value),(int)(winnerPanel.getHeight() * value2));
        winner.setOpaque(false);
        winner.setHorizontalAlignment(SwingConstants.CENTER);
        BufferedImage imgWinner, imgNameWinner;
        try {
            imgWinner = ImageIO.read(new File("images/Podium_win/"+Parser.toCapitalize(s[0])+"_podium_win.png"));
            Image dimg = imgWinner.getScaledInstance(winner.getWidth(),winner.getHeight(), Image.SCALE_AREA_AVERAGING);
            if (podiumNames[0].equals("Our")){
                imgNameWinner = ImageIO.read(new File("images/myNameFrame.png"));
            }
            else if (podiumNames[0].equals("Enemy_red")){
                imgNameWinner = ImageIO.read(new File("images/opponentNameFrame.png"));
            }
            else {
                imgNameWinner = ImageIO.read(new File("images/opponentGreenNameFrame.png"));
            }
            Image dimg2 = imgNameWinner.getScaledInstance(winnerName.getWidth(),winnerName.getHeight(), Image.SCALE_AREA_AVERAGING);
            ImageIcon imageIcon = new ImageIcon(dimg);
            ImageIcon imageIcon2 = new ImageIcon(dimg2);
            winner.setIcon(imageIcon);
            winnerName.setIcon(imageIcon2);
            winnerPanel.add(winner,BorderLayout.CENTER);
            winnerPanel.add(winnerName, BorderLayout.SOUTH);
            endGamePanelPlayers.add(winnerPanel, BorderLayout.NORTH);
        } catch (IOException e) {
            e.printStackTrace();
        }
        JPanel losers = new JPanel();
        JPanel loserPanel1 = new JPanel();
        JPanel loserPanel2 = new JPanel();
        losers.setLayout(new GridLayout(1,2,0,0));
        losers.setPreferredSize(new Dimension(endGamePanelPlayers.getWidth(),endGamePanelPlayers.getHeight()/2));
        losers.setSize(endGamePanelPlayers.getWidth(),endGamePanelPlayers.getHeight()/2);
        losers.setOpaque(false);
        loserPanel1.setLayout(new BorderLayout(0,0));
        loserPanel1.setPreferredSize(new Dimension(losers.getWidth()/2,losers.getHeight()));
        loserPanel1.setSize(losers.getWidth()/2,losers.getHeight());
        loserPanel1.setOpaque(false);
        loserPanel2.setLayout(new BorderLayout(0,0));
        loserPanel2.setPreferredSize(new Dimension(losers.getWidth()/2,losers.getHeight()));
        loserPanel2.setSize(losers.getWidth()/2,losers.getHeight());
        loserPanel2.setOpaque(false);
        BufferedImage imgLoser, imgNameLoser;
        JLabel loser1 = new JLabel();
        JLabel loserName1 = new JLabel();
        JLabel loser2 = new JLabel();
        JLabel loserName2 = new JLabel();
        loser1.setPreferredSize(new Dimension((int)(loserPanel1.getWidth() * value),(int)(loserPanel1.getHeight() * value2)));
        loser1.setSize((int)(loserPanel1.getWidth() * value),(int)(loserPanel1.getHeight() * value2));
        loser1.setOpaque(false);
        loser1.setHorizontalAlignment(SwingConstants.CENTER);
        loserName1.setPreferredSize(new Dimension((int)(loserPanel1.getWidth() * name),(int)(loserPanel1.getHeight() * height)));
        loserName1.setSize((int)(loserPanel1.getWidth() * name),(int)(loserPanel1.getHeight() * height));
        loserName1.setOpaque(false);
        loserName1.setHorizontalAlignment(SwingConstants.CENTER);
        loserName1.setFont(customFont);
        int maxLength2 = Math.min(realPodiumNames[1].length(), 9);
        loserName1.setText(realPodiumNames[1].substring(0,maxLength2));
        loserName1.setForeground(Color.WHITE);
        loserName1.setHorizontalTextPosition(SwingConstants.CENTER);
        try {
            imgLoser = ImageIO.read(new File("images/Podium/"+Parser.toCapitalize(s[1])+"_podium.png"));
            Image dimg = imgLoser.getScaledInstance(loser1.getWidth(),loser1.getHeight(), Image.SCALE_AREA_AVERAGING);
            if (podiumNames[1].equals("Our")){
                imgNameLoser = ImageIO.read(new File("images/myNameFrame.png"));
            }
            else if (podiumNames[1].equals("Enemy_red")){
                imgNameLoser = ImageIO.read(new File("images/opponentNameFrame.png"));
            }
            else {
                imgNameLoser = ImageIO.read(new File("images/opponentGreenNameFrame.png"));
            }
            Image dimg2 = imgNameLoser.getScaledInstance(loserName1.getWidth(),loserName1.getHeight(), Image.SCALE_AREA_AVERAGING);
            ImageIcon imageIcon = new ImageIcon(dimg);
            ImageIcon imageIcon2 = new ImageIcon(dimg2);
            loser1.setIcon(imageIcon);
            loserName1.setIcon(imageIcon2);
            loserPanel1.add(loser1,BorderLayout.CENTER);
            loserPanel1.add(loserName1, BorderLayout.SOUTH);
            losers.add(loserPanel1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(s[2] != null){
            loser2.setPreferredSize(new Dimension((int)(loserPanel2.getWidth() * value),(int)(loserPanel2.getHeight() * value2)));
            loser2.setSize((int)(loserPanel2.getWidth() * value),(int)(loserPanel2.getHeight() * value2));
            loser2.setOpaque(false);
            loser2.setHorizontalAlignment(SwingConstants.CENTER);
            loserName2.setPreferredSize(new Dimension((int)(loserPanel2.getWidth() * name),(int)(loserPanel2.getHeight() * height)));
            loserName2.setSize((int)(loserPanel2.getWidth() * name),(int)(loserPanel2.getHeight() * height));
            loserName2.setOpaque(false);
            loserName2.setHorizontalAlignment(SwingConstants.CENTER);
            loserName2.setFont(customFont);
            int maxLength3 = Math.min(realPodiumNames[2].length(), 9);
            loserName2.setText(realPodiumNames[2].substring(0,maxLength3));
            loserName2.setForeground(Color.WHITE);
            loserName2.setHorizontalTextPosition(SwingConstants.CENTER);
            try {
                imgLoser = ImageIO.read(new File("images/Podium/"+Parser.toCapitalize(s[2])+"_podium.png"));
                Image dimg = imgLoser.getScaledInstance(loser2.getWidth(),loser2.getHeight(), Image.SCALE_AREA_AVERAGING);
                if (podiumNames[2].equals("Our")){
                    imgNameLoser = ImageIO.read(new File("images/myNameFrame.png"));
                }
                else if (podiumNames[2].equals("Enemy_red")){
                    imgNameLoser = ImageIO.read(new File("images/opponentNameFrame.png"));
                }
                else {
                    imgNameLoser = ImageIO.read(new File("images/opponentGreenNameFrame.png"));
                }
                Image dimg2 = imgNameLoser.getScaledInstance(loserName2.getWidth(),loserName2.getHeight(), Image.SCALE_AREA_AVERAGING);
                ImageIcon imageIcon = new ImageIcon(dimg);
                ImageIcon imageIcon2 = new ImageIcon(dimg2);
                loser2.setIcon(imageIcon);
                loserName2.setIcon(imageIcon2);
                loserPanel2.add(loser2,BorderLayout.CENTER);
                loserPanel2.add(loserName2, BorderLayout.SOUTH);
                losers.add(loserPanel2);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        setMessageOnPopup("Would you like to play again?");
        endGamePanelPlayers.add(losers, BorderLayout.SOUTH);
        endGamePanelPlayers.revalidate();
        endGamePanelPlayers.repaint();
    }

    private void updateBoard(Board board, boolean enable){
        Player[] players = board.getPlayers();
        try{
            for(int i=0; i<5; i++){
                for(int j=0; j<5; j++){
                    String color = "";
                    Cell cell = board.getCell(i,j);
                    if (!cell.isFree()){
                        try{
                            for (int p = 0; p < clientConfigurator.getNumberOfPlayer(); p++){
                                Player play = players[p];
                                if(play.equals(player)){
                                    if (player.getWorker(0).getCell().equals(cell)){
                                        color = "blue";
                                        break;
                                    }
                                    else if(player.getWorker(1).getCell().equals(cell)){
                                        color = "blue";
                                        break;
                                    }
                                } else {
                                    if (play.getWorker(0).getCell().equals(cell)){
                                        color = clientConfigurator.getOpponentsNames().get(play.getPlayerName());
                                        break;
                                    }
                                    else if (play.getWorker(1).getCell().equals(cell)){
                                        color = clientConfigurator.getOpponentsNames().get(play.getPlayerName());
                                        break;
                                    }
                                }


                            }
                        }catch (IndexOutOfBoundsException e2){
                            e2.printStackTrace();
                        }
                    }
                    JButton jButton = ((JButton) initialBoardPanel.getComponent(i * 5 + j));
                    setCell(jButton, cell.getLevel(), cell.isFree(), color);
                    jButton.setEnabled(enable);

                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }

    }

    private void usePower(){
        resetBoardPanel();
        BufferedImage image;
        final Image normal;
        setMessageOnPopup("Make a choice");
        resetOverlayPanel();


        godPanel = new JPanel(true){
            @Override
            protected void paintComponent(Graphics g) {

                super.paintComponent(g);
                try{
                    BufferedImage image=ImageIO.read(new File("images/GodPower/main.png"));
                    Image normal = image.getScaledInstance(leftPanel.getWidth(), leftPanel.getHeight()/2, Image.SCALE_AREA_AVERAGING);
                    g.drawImage(normal, 0, 0, null);
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        };
        godPanel.setLayout(new BorderLayout(10,10));
        godPanel.setOpaque(false);
        godPanel.setPreferredSize(new Dimension(leftPanel.getWidth(), leftPanel.getHeight()/2));
        godPanel.setSize(leftPanel.getWidth(), leftPanel.getHeight()/2);

        godPanel.setAlignmentX(SwingConstants.CENTER);
        godPanel.setAlignmentY(SwingConstants.CENTER);

        JPanel internalPanel=new JPanel(true);
        internalPanel.setLayout(new GridLayout(1,2,10,0));
        internalPanel.setSize(godPanel.getWidth(), godPanel.getHeight()/3);
        internalPanel.setOpaque(false);
        //internalPanel.setBorder(BorderFactory.createLineBorder(Color.black));

        JButton yes = new JButton();
        JButton no = new JButton();
        /*yes.setBorder(BorderFactory.createLineBorder(Color.black));
        no.setBorder(BorderFactory.createLineBorder(Color.black));*/
        yes.setSize(internalPanel.getWidth()/4, internalPanel.getHeight()/5);
        no.setSize(internalPanel.getWidth()/4, internalPanel.getHeight()/5);
        yes.setOpaque(false);
        no.setOpaque(false);
        yes.setContentAreaFilled(false);
        no.setContentAreaFilled(false);
        yes.setBorderPainted(false);
        no.setBorderPainted(false);

        yes.setHorizontalAlignment(SwingConstants.LEFT);
        no.setHorizontalAlignment(SwingConstants.RIGHT);
        /*yes.setVerticalAlignment(SwingConstants.SOUTH);
        no.setVerticalAlignment(SwingConstants.SOUTH);*/
        Image n, n2;
        Image np, n2p;
        try{
            image=ImageIO.read(new File("images/GodPower/btn_green.png"));
            n = image.getScaledInstance(yes.getWidth(), yes.getHeight(), Image.SCALE_AREA_AVERAGING);
            yes.setIcon(new ImageIcon(n));
            image=ImageIO.read(new File("images/GodPower/btn_green_pressed.png"));
            np = image.getScaledInstance(yes.getWidth(), yes.getHeight(), Image.SCALE_AREA_AVERAGING);
            yes.setPressedIcon(new ImageIcon(np));
            image=ImageIO.read(new File("images/GodPower/btn_coral.png"));
            n2 = image.getScaledInstance(no.getWidth(), no.getHeight(), Image.SCALE_AREA_AVERAGING);
            no.setIcon(new ImageIcon(n2));
            image=ImageIO.read(new File("images/GodPower/btn_coral_pressed.png"));
            n2p = image.getScaledInstance(yes.getWidth(), yes.getHeight(), Image.SCALE_AREA_AVERAGING);
            no.setPressedIcon(new ImageIcon(n2p));
        }catch(Exception e){
            e.printStackTrace();
        }

        yes.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                guiClient.send("y");
                resetGodPanel();
            }
        });
        no.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                guiClient.send("n");
                resetGodPanel();
            }
        });


        //internalPanel.setPreferredSize(new Dimension(godPanel.getWidth(), godPanel.getHeight()/3));
        internalPanel.add(no);
        internalPanel.add(yes);
        internalPanel.setPreferredSize(new Dimension(godPanel.getWidth()/2, (int)(godPanel.getHeight()*0.6)));
        godPanel.add(internalPanel, BorderLayout.SOUTH);

        leftPanel.add(godPanel, BorderLayout.SOUTH);
        leftPanel.revalidate();
        leftPanel.repaint();
    }

    private void prometheusPower(){
        BufferedImage image;
        final Image normal;
        setMessageOnPopup("Make a choice");
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++){
                try{
                    ((JButton)initialBoardPanel.getComponent(i*5+j)).removeActionListener(((JButton)initialBoardPanel.getComponent(i*5+j)).getActionListeners()[0]);
                }catch(ArrayIndexOutOfBoundsException e){
                    //e.printStackTrace();
                }
                if(player.getWorker(0).getCell().getX()==i && player.getWorker(0).getCell().getY()==j){
                    ((JButton)initialBoardPanel.getComponent(i*5+j)).addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            selectedWorker=0;
                        }
                    });
                }else if(player.getWorker(1).getCell().getX()==i && player.getWorker(1).getCell().getY()==j){
                    ((JButton)initialBoardPanel.getComponent(i*5+j)).addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            selectedWorker=1;
                        }
                    });
                }

            }
        }
        godPanel=new JPanel(true){
            @Override
            protected void paintComponent(Graphics g) {

                super.paintComponent(g);
                try{
                    BufferedImage image=ImageIO.read(new File("images/GodPower/select_worker.png")); //da cambiare in prometheus.png dove viene cambiata la scritta con "select which worker u want to use and click ok"
                    Image normal = image.getScaledInstance(leftPanel.getWidth(), leftPanel.getHeight()/2, Image.SCALE_AREA_AVERAGING);
                    g.drawImage(normal, 0, 0, null);
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        };
        godPanel.setLayout(new BorderLayout(10,10));
        godPanel.setOpaque(false);
        godPanel.setPreferredSize(new Dimension(leftPanel.getWidth(), leftPanel.getHeight()/2));
        godPanel.setSize(leftPanel.getWidth(), leftPanel.getHeight()/2);

        godPanel.setAlignmentX(SwingConstants.CENTER);
        godPanel.setAlignmentY(SwingConstants.CENTER);

        JPanel internalPanel=new JPanel(true);
        internalPanel.setLayout(new GridLayout(1,1,10,0));
        internalPanel.setSize(godPanel.getWidth(), godPanel.getHeight()/3);
        internalPanel.setOpaque(false);
        //internalPanel.setBorder(BorderFactory.createLineBorder(Color.black));

        JButton yes = new JButton();
        /*yes.setBorder(BorderFactory.createLineBorder(Color.black));
        no.setBorder(BorderFactory.createLineBorder(Color.black));*/
        yes.setSize(internalPanel.getWidth()/4, internalPanel.getHeight()/5);
        yes.setOpaque(false);
        yes.setContentAreaFilled(false);
        yes.setBorderPainted(false);
        yes.setHorizontalAlignment(SwingConstants.CENTER);
        /*yes.setVerticalAlignment(SwingConstants.SOUTH);
        no.setVerticalAlignment(SwingConstants.SOUTH);*/
        Image n, n2;
        Image np, n2p;
        try{
            image=ImageIO.read(new File("images/GodPower/btn_ok.png")); //DA CAMBIARE CON UN'IMMAGINE CON SCRITTO SOLO OK
            n = image.getScaledInstance(yes.getWidth(), yes.getHeight(), Image.SCALE_AREA_AVERAGING);
            yes.setIcon(new ImageIcon(n));
            image=ImageIO.read(new File("images/GodPower/btn_ok_pressed.png"));//STESSA ROBA DI SOPRA
            np = image.getScaledInstance(yes.getWidth(), yes.getHeight(), Image.SCALE_AREA_AVERAGING);
            yes.setPressedIcon(new ImageIcon(np));
        }catch(Exception e){
            e.printStackTrace();
        }

        yes.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                guiClient.send(Integer.toString(selectedWorker));
                resetGodPanel();
            }
        });


        //internalPanel.setPreferredSize(new Dimension(godPanel.getWidth(), godPanel.getHeight()/3));
        internalPanel.add(yes);
        internalPanel.setPreferredSize(new Dimension(godPanel.getWidth()/2, (int)(godPanel.getHeight()*0.6)));
        godPanel.add(internalPanel, BorderLayout.SOUTH);

        leftPanel.add(godPanel, BorderLayout.SOUTH);
        leftPanel.revalidate();
        leftPanel.repaint();
    }

    private void turnPhaseManager(GameMessage gameMessage) {
        switch (gameMessage.getMessageType()) {
            case DRAW_CARD:
                isSimplePlay = false;
                drawCards();
                break;
            case PICK_CARD:
                isSimplePlay = false;
                pickCard(gameMessage);
                break;
            case SET_WORKER_1:
                addMyCard(gameMessage);
                addOverlayPanel();
                placeWorker(((GameBoardMessage)gameMessage).getBoard());
                break;
            case SET_WORKER_2:
                placeWorker(((GameBoardMessage)gameMessage).getBoard());
                break;
            case BEGINNING:
                break;
            case MOVE:
                resetOverlayPanel();
                resetBoardPanel();
                prepareMove(gameMessage);
                break;
            case BUILD:
                resetOverlayPanel();
                resetBoardPanel();
                performBuild();
                break;
            case USE_POWER:
                usePower();
                break;
            case PROMETHEUS:
                prometheusPower();
                break;
            case VICTORY:
            case LOSE:
                resetGodPanel();
                break;
            case END_GAME:
                resetGodPanel();
                endGame(((EndGameMessage) gameMessage).getPodium());
                break;
            default:
                break;
        }


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
                            //opponentsNames.add(gameMessage.getPlayer().getPlayerName());
                        }
                    }
                    else if(isSimplePlay && opponentsNames.size() != clientConfigurator.getNumberOfPlayer() - 1){
                        opponentsNames.add(gameMessage.getPlayer().getPlayerName());
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
                    else if(isSimplePlay && opponentsNames.size() == clientConfigurator.getNumberOfPlayer() - 1){
                        addOpponentsSimpleMode();
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
                break;
            case MOVE:
            case BUILD:
                resetOverlayPanel();
                break;
            case USE_POWER:
                break;
            case PROMETHEUS:
                resetOverlayPanel();
                break;
            case VICTORY:
            case LOSE:
                resetGodPanel();
                break;
            case END_GAME:
                endGame(((EndGameMessage) gameMessage).getPodium());
                break;
            default:
                break;
        }


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
            this.player=arg.getPlayer();
            if(arg instanceof GameBoardMessage){
                updateBoard(((GameBoardMessage)arg).getBoard(), true);
                actualPlayer = arg.getPlayer();
            }
            setMessageOnPopup(arg.getMessage());
            turnPhaseManager(arg);
        } else {
            int maxLength = Math.min(player.getPlayerName().length(), 9);
            setMessageOnPopup("It's now " + player.getPlayerName().substring(0,maxLength) + "'s turn");
            phaseManager(arg);
            if(arg instanceof GameBoardMessage){
                updateBoard(((GameBoardMessage)arg).getBoard(), false);
            }

        }
    }

    private void showError(String s){
        try{
            southPanel.setText(s);
            Timer timer=new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    southPanel.setText("");
                }
            }, 3000);


        } catch (Exception e) {
            //e.printStackTrace();
        }
    }

    @Override
    public void update(Object msg) {

        if(msg instanceof String){
            String message = (String) msg;
            if(message.startsWith("ERROR: ")){
                String errorString = message.substring(7);
                showError(errorString);
            }
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
