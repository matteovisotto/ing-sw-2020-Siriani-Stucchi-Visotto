package it.polimi.ingsw.client.GUI;

import it.polimi.ingsw.client.GUIClient;
import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.messageModel.*;
import it.polimi.ingsw.observer.Observer;
import it.polimi.ingsw.utils.Parser;

import java.util.*;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Timer;

public class Game extends JFrame implements Observer<Object> {

    private final GUIClient guiClient;
    private boolean initedBoard = false;
    private boolean isSimplePlay = true;
    private final ArrayList<String> opponentsNames = new ArrayList<>();
    private final HashMap<String, String> opponentGods = new HashMap<>();
    private final HashMap<String, String> myGod = new HashMap<>();
    private JPanel mainPanel, leftPanel, centerPanel, rightPanel, overlayPanel, initialBoardPanel, godPanel, endGamePanel, endGamePanelPlayers, exitGame, playAgain;
    private JLabel messageLabel, background, endGameImage, southPanel;
    private JButton startPlayBtn;
    private MessageType messageType = MessageType.PLAYER_NAME;
    private Player player;
    private ClientConfigurator clientConfigurator;
    private String response;
    private final ArrayList<String> multipleSelections = new ArrayList<>();
    private String chosenCellX;
    private String chosenCellY;
    private final JButton[][] board = new JButton[5][5];
    private double value = 0;
    protected int selectedWorker;
    private final HashMap<JButton, Integer> cellsX = new HashMap<>();
    private final HashMap<JButton, Integer> cellsY = new HashMap<>();
    private GraphicsEnvironment ge;
    private Font customFont;

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

    private void setJButtonProperties(JButton button){
        button.setBorder(BorderFactory.createEmptyBorder());
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setOpaque(false);
        button.setContentAreaFilled(false);
    }

    private void setJPanelProperties(JPanel panel, int hgap, int vgap, int width, int height){
        panel.setLayout(new BorderLayout(hgap, vgap));
        panel.setPreferredSize(new Dimension(width, height));
        panel.setSize(width, height);
        panel.setOpaque(false);
    }

    private void setJLabelProperties(JLabel label, int hgap, int vgap, float fontDimension, Color color, int width, int height){
        label.setLayout(new BorderLayout(hgap, vgap));
        label.setPreferredSize(new Dimension(width, height));
        label.setSize(width, height);
        label.setOpaque(false);
        try {
            customFont = Font.createFont(Font.TRUETYPE_FONT, new File("Fonts/LillyBelle.ttf")).deriveFont(fontDimension);
            ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(customFont);
        } catch (IOException | FontFormatException e) {
            e.printStackTrace();
        }
        label.setHorizontalTextPosition(SwingConstants.CENTER);
        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setFont(customFont);
        label.setForeground(color);
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
        setJButtonProperties(startPlayBtn);
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

    private ImageIcon loadImage(String path, int width, int height){
        BufferedImage img;
        ImageIcon imageIcon;
        try {
            img = ImageIO.read(new File(path));
            Image dimg = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
            imageIcon = new ImageIcon(dimg);
        } catch (IOException e) {
            e.printStackTrace();
            imageIcon=null;
        }
        return imageIcon;
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

        background.setIcon(loadImage("images/SantoriniBoard.png", d.width, d.height));

        centerPanel = new JPanel(true);
        setJPanelProperties(centerPanel, 10,10,(int)(mainPanel.getWidth() * value),(int)(mainPanel.getHeight() - mainPanel.getHeight() * 0.1389));
        mainPanel.add(centerPanel, BorderLayout.CENTER);

        value = 0.1389;
        southPanel = new JLabel();
        setJLabelProperties(southPanel, 10,10,40f, Color.RED,mainPanel.getWidth(), (int)(mainPanel.getHeight() * value));
        mainPanel.add(southPanel, BorderLayout.SOUTH);

        value = 0.2942708;
        leftPanel = new JPanel(true);
        setJPanelProperties(leftPanel,10,50, (int)(mainPanel.getWidth() * value), mainPanel.getHeight());
        mainPanel.add(leftPanel, BorderLayout.WEST);

        rightPanel = new JPanel(true);
        setJPanelProperties(rightPanel,10,10,(int)(mainPanel.getWidth() * value), mainPanel.getHeight());
        mainPanel.add(rightPanel, BorderLayout.EAST);

        value = 0.1111111111;
        messageLabel = new JLabel();
        setJLabelProperties(messageLabel,10,10, 25f, Color.WHITE,mainPanel.getWidth(), (int)(mainPanel.getHeight() * value));

        messageLabel.setIcon(loadImage("images/Santorini_GenericPopup.png", messageLabel.getWidth(), messageLabel.getHeight()));

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
            setJLabelProperties(nameLabel,0,0,25f,Color.WHITE,(playerPanel.getWidth()), (int) (playerPanel.getHeight() * value) );
            value = 2;
            setJLabelProperties(godLabel, 0,0,25f, Color.WHITE,(playerPanel.getWidth()), (int) (playerPanel.getHeight() * value) / 2);
            String path;
            if (isSimplePlay){
                path="images/enemy_player.png";
            }
            else {
                myGod.put(gameMessage.getPlayer().getPlayerName(), gameMessage.getPlayer().getGodCard().getName());
                //System.out.println(gameMessage.getPlayer().getPlayerName());
                String godName = myGod.get(gameMessage.getPlayer().getPlayerName());
                path="images/God_with_frame/" + Parser.toCapitalize(godName) + ".png";
            }
            nameLabel.setIcon(loadImage("images/myNameFrame.png", nameLabel.getWidth(), nameLabel.getHeight()));
            godLabel.setIcon(loadImage(path, godLabel.getWidth(), godLabel.getHeight()));
            int maxLength = Math.min(gameMessage.getPlayer().getPlayerName().length(), 9);
            nameLabel.setText(gameMessage.getPlayer().getPlayerName().substring(0,maxLength));
            playerPanel.add(nameLabel, BorderLayout.NORTH);
            playerPanel.add(godLabel, BorderLayout.CENTER);
            myPanel.add(playerPanel, BorderLayout.NORTH);
            myPanel.setAlignmentX(SwingConstants.CENTER);
            leftPanel.add(myPanel, BorderLayout.NORTH);
        }catch (Exception e){
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
                //System.out.println(opponentName);
                String godName = opponentGods.get(opponentName);
                JPanel playerPanel = new JPanel(true);
                playerPanel.setSize(opponentsPanel.getWidth() * 3 / 2, opponentsPanel.getHeight() * 11/10 / opponentGods.size());
                playerPanel.setOpaque(false);
                playerPanel.setLayout(new BorderLayout(0, 0));
                JLabel nameLabel = new JLabel();
                JLabel godLabel = new JLabel();
                value = 0.304347826;
                setJLabelProperties(nameLabel, 0,0,20f, Color.WHITE,(playerPanel.getWidth()) / opponentGods.size() + 1, (int)(playerPanel.getHeight() * value));
                value = 1.7391304347826;
                setJLabelProperties(godLabel, 0,0, 20f, Color.WHITE,(playerPanel.getWidth()) / opponentGods.size() + 1, (int)(playerPanel.getHeight() * value) / opponentGods.size() + 1);
                String filename;
                if (clientConfigurator.getOpponentsNames().get(opponentName).equals("red")){
                    filename = "images/opponentNameFrame.png";
                }
                else {
                    filename = "images/opponentGreenNameFrame.png";
                }
                nameLabel.setIcon(loadImage(filename, nameLabel.getWidth(), nameLabel.getHeight()));
                godLabel.setIcon(loadImage("images/Podium/" + Parser.toCapitalize(godName) + "_podium.png", godLabel.getWidth(), godLabel.getHeight()));
                int maxLength = Math.min(opponentName.length(), 9);
                nameLabel.setText(opponentName.substring(0,maxLength));
                playerPanel.add(nameLabel, BorderLayout.SOUTH);
                playerPanel.add(godLabel, BorderLayout.CENTER);
                opponentsPanel.add(playerPanel, BorderLayout.SOUTH);
            }
            opponentsPanel.setAlignmentX(SwingConstants.CENTER);
            rightPanel.add(opponentsPanel, BorderLayout.SOUTH);

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void addOpponentsSimpleMode() {
        try {
            String filename = "";
            JPanel opponentsPanel = new JPanel(true);
            opponentsPanel.setOpaque(false);
            opponentsPanel.setLayout(new GridLayout(opponentsNames.size(), 1, 0, 0));
            opponentsPanel.setSize(rightPanel.getWidth()/2, rightPanel.getHeight()*2/3);
            for (String opponentName : opponentsNames) {
                JPanel playerPanel = new JPanel(true);
                playerPanel.setSize(opponentsPanel.getWidth() * 3 / 2, opponentsPanel.getHeight() / opponentsNames.size());
                playerPanel.setOpaque(false);
                playerPanel.setLayout(new BorderLayout(0, 0));
                JLabel nameLabel = new JLabel();
                JLabel enemyLabel = new JLabel();
                value = 0.2;
                setJLabelProperties(nameLabel,0,0,20f,Color.WHITE,(playerPanel.getWidth() * 9/10), (int)(playerPanel.getHeight() * value) );
                value = 0.75;
                setJLabelProperties(enemyLabel, 0,0,20f, Color.WHITE,(playerPanel.getWidth() * 9/(10*opponentsNames.size())), (int)(playerPanel.getHeight() * value));
                BufferedImage enemy, frame;
                if (clientConfigurator.getOpponentsNames().get(opponentName).equals("red")){
                    filename = "images/opponentNameFrame.png";
                }
                else {
                    filename = "images/opponentGreenNameFrame.png";
                }
                nameLabel.setIcon(loadImage(filename,nameLabel.getWidth(), nameLabel.getHeight()));
                enemyLabel.setIcon(loadImage("images/enemy_player.png", enemyLabel.getWidth(), enemyLabel.getHeight()));
                int maxLength = Math.min(opponentName.length(), 9);
                nameLabel.setText(opponentName.substring(0,maxLength));
                playerPanel.add(nameLabel, BorderLayout.SOUTH);
                playerPanel.add(enemyLabel, BorderLayout.CENTER);
                opponentsPanel.add(playerPanel, BorderLayout.SOUTH);
            }
            opponentsPanel.setAlignmentX(SwingConstants.CENTER);
            rightPanel.add(opponentsPanel, BorderLayout.SOUTH);

        }catch (Exception e){
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
                    //ignore
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
                    //ignore
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
                setJButtonProperties(cell);
                cell.setSize(overlayPanel.getWidth() / 5, overlayPanel.getHeight() / 5);
                cell.setIcon(loadImage("images/blue_square.png",cell.getWidth(), cell.getHeight() ));
                overlayPanel.add(cell,BorderLayout.CENTER);
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
                setJButtonProperties(board[i][j]);
                board[i][j].setPreferredSize(new Dimension(initialBoardPanel.getWidth()/5, initialBoardPanel.getHeight()/5));
                board[i][j].setSize(initialBoardPanel.getWidth()/5, initialBoardPanel.getHeight()/5);
                board[i][j].setEnabled(false);
                board[i][j].setVisible(false);
                initialBoardPanel.add(board[i][j]);
            }
        }
        centerPanel.add(initialBoardPanel,BorderLayout.CENTER);
    }

    private void drawCards(){
        BufferedImage image, imagePressed;
        final HashMap<JButton, Integer> gods = new LinkedHashMap<>();
        setMessageOnPopup("Please select " + clientConfigurator.getNumberOfPlayer() + " god cards");

        final JPanel panel = new JPanel(true);
        panel.setSize(centerPanel.getWidth() - 100, (int)(mainPanel.getHeight() - mainPanel.getHeight() * 0.1389));
        panel.setOpaque(false);
        panel.setLayout(new GridLayout(3,3,10,10));

        final JLabel centerSouthLabel = new JLabel();
        setJLabelProperties(centerSouthLabel, 0,0, 40f, Color.BLUE, southPanel.getWidth(),southPanel.getHeight());
        centerSouthLabel.setText("Simple Gods");
        southPanel.add(centerSouthLabel);

        final JPanel arrowPanel=new JPanel(true);
        arrowPanel.setLayout(new GridLayout(1,2,100,10));
        arrowPanel.setSize(centerPanel.getWidth() - 100, (int)(mainPanel.getHeight() - mainPanel.getHeight()/10));
        arrowPanel.setOpaque(false);
        JButton leftArrow=new JButton();
        setJButtonProperties(leftArrow);
        leftArrow.setSize(southPanel.getWidth()/10,southPanel.getHeight()/2);
        leftArrow.setIcon(loadImage("images/Miscellaneous/btn_back.png",leftArrow.getWidth(), leftArrow.getHeight()));
        leftArrow.setPressedIcon(loadImage("images/Miscellaneous/btn_back_pressed.png",leftArrow.getWidth(), leftArrow.getHeight()));
        leftArrow.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                for (Component component:gods.keySet()) {
                    panel.remove(component);
                }
                for (JButton god:gods.keySet()) {
                    if(gods.get(god)>=0 && gods.get(god)<=8)
                    panel.add(god);
                }
                centerSouthLabel.setText("Simple Gods");
                revalidate();
                repaint();
            }
        });
        arrowPanel.add(leftArrow);


        JButton rightArrow=new JButton();
        setJButtonProperties(rightArrow);
        rightArrow.setSize(southPanel.getWidth()/10,southPanel.getHeight()/2);
        rightArrow.setIcon(loadImage("images/Miscellaneous/btn_front.png", rightArrow.getWidth(), rightArrow.getHeight()));
        rightArrow.setPressedIcon(loadImage("images/Miscellaneous/btn_front_pressed.png", rightArrow.getWidth(), rightArrow.getHeight()));
        rightArrow.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                for (Component component:gods.keySet()) {
                    panel.remove(component);
                }
                for (JButton god:gods.keySet()) {
                    if(gods.get(god)>=9 && gods.get(god)<=13)
                        panel.add(god);
                }
                centerSouthLabel.setText("Advanced Gods");
                revalidate();
                repaint();
            }
        });
        arrowPanel.add(rightArrow);
        southPanel.add(arrowPanel);

        for (int i=0; i<14; i++) {
            final JButton god = new JButton();
            setJButtonProperties(god);
            god.setSize(panel.getWidth()/3 - 30,panel.getHeight()/3 - 100);
            String fileName = Gods.getGod(i).toString();
            fileName = fileName.substring(fileName.lastIndexOf('.')+1, fileName.indexOf('@'));
            god.setIcon(loadImage("images/God_with_frame/"+ fileName +".png", god.getWidth(), god.getHeight()));
            if(i<=8){
                panel.add(god);
            }
            gods.put(god, i);
            god.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    multipleSelections.add(gods.get((JButton) e.getSource()).toString());
                    gods.remove((JButton) e.getSource());
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
                            southPanel.remove(arrowPanel);
                            multipleSelections.clear();
                            centerSouthLabel.setText("");
                            centerPanel.remove(panel);
                            centerPanel.revalidate();
                            centerPanel.repaint();
                            southPanel.revalidate();
                            southPanel.repaint();
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
            setJButtonProperties(god);
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

    private void sendCell(ActionEvent e){
        StringBuilder stringBuilder = new StringBuilder();
        chosenCellX = cellsX.get((JButton) e.getSource()).toString();
        chosenCellY = cellsY.get((JButton) e.getSource()).toString();
        stringBuilder.append(chosenCellX);
        stringBuilder.append(",");
        stringBuilder.append(chosenCellY);
        response = stringBuilder.toString();
        guiClient.send(response);
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
                        sendCell(e);
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
                    //ignore
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
        int x=player.getWorker(player.getUsedWorker()).getCell().getX();
        int y=player.getWorker(player.getUsedWorker()).getCell().getY();
        for (int i = x-1; i <= x+1; i++) {
            for (int j = y-1; j <= y+1; j++){
                try{
                    if((!(i==x && j==y) || player.getGodCard().getCardGod()==Gods.ZEUS) && i>=0 && j>=0 && i<=4 && j<=4 &&
                            (!(player.getWorker(0).getCell().getX()==i && player.getWorker(0).getCell().getY()==j) || player.getGodCard().getCardGod()==Gods.ZEUS) &&
                            (!(player.getWorker(1).getCell().getX()==i && player.getWorker(1).getCell().getY()==j) || player.getGodCard().getCardGod()==Gods.ZEUS)){
                        (overlayPanel.getComponent(i*5+j)).setVisible(true);
                        (overlayPanel.getComponent(i*5+j)).setEnabled(true);
                        ((JButton)overlayPanel.getComponent(i*5+j)).addActionListener(new ActionListener() {
                            @Override
                            public void actionPerformed(ActionEvent e) {
                                sendCell(e);
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
                    //ignore
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
        removeBoard();
        endGamePanel = new JPanel();
        endGamePanel.setLayout(new BorderLayout(0,0));
        endGamePanel.setPreferredSize(new Dimension(mainPanel.getWidth(), mainPanel.getHeight()));
        endGamePanel.setSize(mainPanel.getWidth(), mainPanel.getHeight());
        endGamePanel.setOpaque(false);
        mainPanel.add(endGamePanel);
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
    }

    private void setJPanelsOnEndGame(HashMap<Player, Integer> podium){
        endGamePanelPlayers = new JPanel();
        endGamePanelPlayers.setLayout(new BorderLayout(10,60));
        value = 0.552083;
        endGamePanelPlayers.setPreferredSize(new Dimension((int)(endGamePanel.getWidth() * value), (int)(endGamePanel.getHeight() - endGamePanel.getHeight() * 0.1389)));
        endGamePanelPlayers.setSize((int)(endGamePanel.getWidth() * value), (int)(endGamePanel.getHeight() - endGamePanel.getHeight() * 0.1389));
        endGamePanelPlayers.setOpaque(false);
        endGamePanel.add(endGamePanelPlayers, BorderLayout.CENTER);

        value = 0.2129629;
        southPanel = new JLabel();
        southPanel.setLayout(new BorderLayout(10, 10));
        southPanel.setPreferredSize(new Dimension(endGamePanel.getWidth(), (int)(endGamePanel.getHeight() * value)));
        southPanel.setSize(endGamePanel.getWidth(), (int)(endGamePanel.getHeight() * value));
        southPanel.setOpaque(false);
        endGamePanel.add(southPanel, BorderLayout.SOUTH);

        value = 0.260416;
        playAgain = new JPanel(true);
        playAgain.setLayout(new BorderLayout(10, 50));
        playAgain.setPreferredSize(new Dimension((int)(endGamePanel.getWidth() * value), endGamePanel.getHeight()));
        playAgain.setSize((int)(endGamePanel.getWidth() * value), endGamePanel.getHeight());
        playAgain.setOpaque(false);
        JButton playAgainButton = new JButton();
        playAgainButton.setSize(playAgain.getWidth()/2, playAgain.getHeight()/4);
        setJButtonProperties(playAgainButton);

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
            }
        });
        endGamePanel.add(playAgain, BorderLayout.WEST);

        exitGame = new JPanel(true);
        setJPanelProperties(exitGame, 10,10,(int)(endGamePanel.getWidth() * value), endGamePanel.getBounds().height);
        JButton exitButton = new JButton();
        exitButton.setSize(exitGame.getWidth()/2, exitGame.getHeight()/4);
        setJButtonProperties(exitButton);

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
            customFont = Font.createFont(Font.TRUETYPE_FONT, new File("Fonts/LillyBelle.ttf")).deriveFont(25f);
            ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
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
        setJPanelProperties(winnerPanel,0,0,endGamePanelPlayers.getWidth()/2,endGamePanelPlayers.getHeight()/2);

        JLabel winnerName = new JLabel();
        setJLabelProperties(winnerName, 0,0,25f,Color.WHITE, (int)(winnerPanel.getWidth() * name),(int)(winnerPanel.getHeight() * height));
        int maxLength = Math.min(realPodiumNames[0].length(), 9);
        winnerName.setText(realPodiumNames[0].substring(0,maxLength));
        winnerName.setHorizontalTextPosition(SwingConstants.CENTER);
        JLabel winner = new JLabel();
        value = 0.6;
        setJLabelProperties(winner,0,0, 25f,Color.WHITE, (int)(winnerPanel.getWidth() * value),(int)(winnerPanel.getHeight() * value2));

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
        setJPanelProperties(loserPanel1,0,0,losers.getWidth()/2,losers.getHeight());
        setJPanelProperties(loserPanel2,0,0,losers.getWidth()/2,losers.getHeight());
        BufferedImage imgLoser, imgNameLoser;
        JLabel loser1 = new JLabel();
        JLabel loserName1 = new JLabel();
        JLabel loser2 = new JLabel();
        JLabel loserName2 = new JLabel();

        setJLabelProperties(loser1, 0,0, 25f, Color.WHITE,(int)(loserPanel1.getWidth() * value),(int)(loserPanel1.getHeight() * value2));
        setJLabelProperties(loserName1, 0,0, 25f, Color.WHITE,(int)(loserPanel1.getWidth() * name),(int)(loserPanel1.getHeight() * height));
        int maxLength2 = Math.min(realPodiumNames[1].length(), 9);
        loserName1.setText(realPodiumNames[1].substring(0,maxLength2));

        try {
            imgLoser = ImageIO.read(new File("images/Podium_silver/"+Parser.toCapitalize(s[1])+"_podium_silver.png"));
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
            setJLabelProperties(loser2, 0,0, 25f, Color.WHITE,(int)(loserPanel2.getWidth() * value),(int)(loserPanel2.getHeight() * value2));
            setJLabelProperties(loserName2, 0,0, 25f, Color.WHITE,(int)(loserPanel2.getWidth() * name),(int)(loserPanel2.getHeight() * height));
            int maxLength3 = Math.min(realPodiumNames[2].length(), 9);
            loserName2.setText(realPodiumNames[2].substring(0,maxLength3));
            try {
                imgLoser = ImageIO.read(new File("images/Podium_bronze/"+Parser.toCapitalize(s[2])+"_podium_bronze.png"));
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

        JButton yes = new JButton();
        JButton no = new JButton();
        yes.setSize(internalPanel.getWidth()/4, internalPanel.getHeight()/5);
        no.setSize(internalPanel.getWidth()/4, internalPanel.getHeight()/5);
        setJButtonProperties(yes);
        setJButtonProperties(no);

        yes.setHorizontalAlignment(SwingConstants.LEFT);
        no.setHorizontalAlignment(SwingConstants.RIGHT);
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
                    //ignore
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
                    BufferedImage image=ImageIO.read(new File("images/GodPower/select_worker.png"));
                    Image normal = image.getScaledInstance(leftPanel.getWidth(), leftPanel.getHeight()/2, Image.SCALE_AREA_AVERAGING);
                    g.drawImage(normal, 0, 0, null);
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        };
        setJPanelProperties(godPanel,10,10,leftPanel.getWidth(),leftPanel.getHeight()/2);

        godPanel.setAlignmentX(SwingConstants.CENTER);
        godPanel.setAlignmentY(SwingConstants.CENTER);

        JPanel internalPanel=new JPanel(true);
        internalPanel.setLayout(new GridLayout(1,1,10,0));
        internalPanel.setSize(godPanel.getWidth(), godPanel.getHeight()/3);
        internalPanel.setOpaque(false);

        JButton yes = new JButton();
        setJButtonProperties(yes);
        yes.setSize(internalPanel.getWidth()/4, internalPanel.getHeight()/5);
        yes.setHorizontalAlignment(SwingConstants.CENTER);
        Image n;
        Image np;
        try{
            image=ImageIO.read(new File("images/GodPower/btn_ok.png"));
            n = image.getScaledInstance(yes.getWidth(), yes.getHeight(), Image.SCALE_AREA_AVERAGING);
            yes.setIcon(new ImageIcon(n));
            image=ImageIO.read(new File("images/GodPower/btn_ok_pressed.png"));
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
            case SET_WORKER_1:
                try {
                    if (!isSimplePlay && opponentGods.size() != clientConfigurator.getNumberOfPlayer() - 1) {
                        Player opponent = gameMessage.getPlayer();
                        if (!opponentGods.containsKey(opponent.getPlayerName())) {
                            opponentGods.put(opponent.getPlayerName(), opponent.getGodCard().getName());
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
            case USE_POWER:
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
            //ignore
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
