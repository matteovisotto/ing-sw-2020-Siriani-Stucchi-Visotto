package it.polimi.ingsw.client.GUI;


import it.polimi.ingsw.client.GUIClient;
import it.polimi.ingsw.model.*;
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
    private ArrayList<String> opponentsNames = new ArrayList<>();
    private HashMap<String, String> opponentGods = new HashMap<>();
    private JPanel mainPanel, leftPanel, centerPanel, rightPanel, overlayPanel, initialBoardPanel, southPanel;
    private JLabel messageLabel;
    private JButton startPlayBtn;
    private MessageType messageType=MessageType.PLAYER_NAME;
    private Player player;
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

    public Game(final GUIClient guiClient){
        customCursor();
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
        JLabel backgroud = new JLabel();
        Toolkit tk = Toolkit.getDefaultToolkit();
        Dimension d = tk.getScreenSize();
        //this.setSize(d);
        setContentPane(backgroud);
        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        //togliere il commento qua sotto per metterlo completo full screen.
        //this.setUndecorated(true);
        BufferedImage img;
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
        southPanel = new JPanel(true);
        southPanel.setLayout(new BorderLayout(10, 10));
        southPanel.setPreferredSize(new Dimension(mainPanel.getWidth(), (int)(mainPanel.getHeight() * value))); //150
        southPanel.setSize(mainPanel.getWidth(), (int)(mainPanel.getHeight() * value));
        southPanel.setOpaque(false);
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
        messageLabel.setHorizontalTextPosition(SwingConstants.CENTER);
        messageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        messageLabel.setFont(new Font("Comic Sans MS", Font.PLAIN, 18));
        messageLabel.setPreferredSize(new Dimension(centerPanel.getWidth(), (int)(mainPanel.getHeight() * value))); //120
        messageLabel.setSize(centerPanel.getWidth(), (int)(mainPanel.getHeight() * value)); //120


        centerPanel.add(messageLabel, BorderLayout.NORTH);
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
        addInitialBoard();
        messageLabel.setForeground(Color.WHITE);
        //revalidation();
    }

    private void clearGui(){
        for(Component component: mainPanel.getComponents()){
            mainPanel.remove(component);
        }
        getContentPane().removeAll();
        remove(mainPanel);
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
                playerPanel.setSize(opponentsPanel.getWidth() * 3 / 2, opponentsPanel.getHeight() / opponentGods.size());
                playerPanel.setOpaque(false);
                playerPanel.setLayout(new BorderLayout(0, 0));
                JLabel nameLabel = new JLabel();
                JLabel godLabel = new JLabel();
                value = 0.304347826;
                nameLabel.setSize((playerPanel.getWidth()) / opponentGods.size() + 1, (int)(playerPanel.getHeight() * value)); //70
                value = 1.7391304347826;
                godLabel.setSize((playerPanel.getWidth()) / opponentGods.size() + 1, (int)(playerPanel.getHeight() * value) / opponentGods.size() + 1); //400
                BufferedImage god, frame;
                try {
                    frame = ImageIO.read(new File("images/opponentNameFrame.png"));
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
                nameLabel.setFont(new Font("Comic Sans MS", Font.PLAIN, 20));
                nameLabel.setText(opponentName);
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
            JPanel opponentsPanel = new JPanel(true);
            opponentsPanel.setOpaque(false);
            opponentsPanel.setLayout(new GridLayout(opponentsNames.size(), 1, 0, 0));
            opponentsPanel.setSize(rightPanel.getWidth()/2, 230 * opponentsNames.size());
            for (String opponentName : opponentsNames) {
                JPanel playerPanel = new JPanel(true);
                playerPanel.setSize(opponentsPanel.getWidth() * 3 / 2, opponentsPanel.getHeight() / opponentsNames.size());
                playerPanel.setOpaque(false);
                playerPanel.setLayout(new BorderLayout(0, 0));
                JLabel nameLabel = new JLabel();
                JLabel enemyLabel = new JLabel();
                value = 0.304347826;
                nameLabel.setSize((playerPanel.getWidth() * 9/10) / opponentsNames.size() + 1, (int)(playerPanel.getHeight() * value)); //70
                value = 1.7391304347826;
                enemyLabel.setSize((playerPanel.getWidth() * 9/10) / opponentsNames.size() + 1, (int)(playerPanel.getHeight() * value) / opponentsNames.size() + 1); //500
                BufferedImage enemy, frame;
                try {
                    frame = ImageIO.read(new File("images/opponentNameFrame.png"));
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
                nameLabel.setFont(new Font("Comic Sans MS", Font.PLAIN, 20));
                nameLabel.setText(opponentName);
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
        centerPanel.remove(overlayPanel);
        centerPanel.revalidate();
        centerPanel.repaint();
    }

    private void resetOverlayPanel(){
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++){
                try{
                    ((JButton)overlayPanel.getComponent(i*5+j)).removeActionListener(((JButton)overlayPanel.getComponent(i*5+j)).getActionListeners()[0]);
                    ((JButton)overlayPanel.getComponent(i*5+j)).setVisible(false);
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
                    ((JButton)board[i][j].getComponent(i*5+j)).removeActionListener(((JButton)board[i][j].getComponent(i*5+j)).getActionListeners()[0]);
                }catch(ArrayIndexOutOfBoundsException e){
                    //e.printStackTrace();
                }

            }
        }
    }

    private void addOverlayPanel() {
        BufferedImage image;
        overlayPanel = new JPanel(true);
        overlayPanel.setLayout(new GridLayout(5,5,0,10));
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

    public void placeWorker(Board board) {
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++){
                if(board.getCell(i,j).isFree()){
                    ((JButton)overlayPanel.getComponent(i*5+j)).setVisible(true);
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
                        //removeOverlayPanel();
                        if(messageType==MessageType.SET_WORKER_2){
                            player.setWorkers(new Worker(new Cell(Integer.parseInt(chosenCellX), Integer.parseInt(chosenCellY))));
                        }
                        resetOverlayPanel();
                    }
                });
            }
        }
        //centerPanel.add(overlayPanel,BorderLayout.CENTER);
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
                                }

                            }
                            response = stringBuilder.toString();
                            guiClient.send(response);
                            multipleSelections.clear();
                            centerPanel.remove(panel);
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
                }
            });
            panel.add(god);

        }
        centerPanel.add(panel);
        centerPanel.revalidate();
        centerPanel.repaint();
    }

    private void setCell(JButton cell, Blocks blocks, boolean isFree, boolean mine){
        BufferedImage image=null;
        String path="images/Blocks/";
        try{
            cell.setVisible(true);
            switch(blocks){
                case EMPTY:
                    cell.setVisible(false);
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
                cell.setVisible(true);
                if(mine){
                    path+="_me";
                }
                else{
                    path+="_enemy";
                }
            }
            path+=".png";
            if(cell.isVisible()){
                image = ImageIO.read(new File(path));
                Image normal = image.getScaledInstance(cell.getWidth(), cell.getHeight(), Image.SCALE_AREA_AVERAGING);
                cell.setIcon(new ImageIcon(normal));
                cell.setDisabledIcon(new ImageIcon(normal));
                if(mine){
                    cell.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            boolean stop=false;
                            ((JButton)e.getSource()).setSelected(false);
                            for(int i=0; i<5; i++){
                                for(int j=0; j<5; j++){
                                    if((JButton)e.getSource() == board[i][j]){
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

        }catch(IOException e){
            e.printStackTrace();
        }

    }

    private void updateBoard(Board board, boolean enable){
        try{
            for(int i=0; i<5; i++){
                for(int j=0; j<5; j++){
                    boolean mine=false;
                    Cell cell = board.getCell(i,j);
                    try{
                        if(player.getWorker(0).getCell().equals(cell) || player.getWorker(1).getCell().equals(cell)){
                            mine=true;
                        }
                    }catch (IndexOutOfBoundsException e2){
                        mine=false;
                    }

                    JButton jButton = ((JButton) initialBoardPanel.getComponent(i * 5 + j));
                    setCell(jButton, cell.getLevel(), cell.isFree(), mine);
                    jButton.setEnabled(enable);

                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }

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
                addOverlayPanel();
                placeWorker(((GameBoardMessage)gameMessage).getBoard());
                break;
            case SET_WORKER_2:
                placeWorker(((GameBoardMessage)gameMessage).getBoard());
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
                        ((JButton)overlayPanel.getComponent(i*5+j)).setVisible(true);
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
                    }

                }catch(Exception e){
                    e.printStackTrace();
                }

            }
        }

        /*else {
            guiClient.send(this.selectedWorker+","+x+","+y);
            this.selectedWorker=-1;
        }*/
    }

    private void performBuild(){
        resetOverlayPanel();
        resetBoardPanel();
        int x=player.getWorker(selectedWorker).getCell().getX();
        int y=player.getWorker(selectedWorker).getCell().getY();
        for (int i = x-1; i <= x+1; i++) {
            for (int j = y-1; j <= y+1; j++){
                try{
                    if(!(i==x && j==y) && i>=0 && j>=0 && i<=4 && j<=4 && !(player.getWorker(0).getCell().getX()==i && player.getWorker(0).getCell().getY()==j) && !(player.getWorker(1).getCell().getX()==i && player.getWorker(1).getCell().getY()==j)){
                        ((JButton)overlayPanel.getComponent(i*5+j)).setVisible(true);
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
                                resetOverlayPanel();
                                guiClient.send(response);
                                resetOverlayPanel();
                                resetBoardPanel();
                            }
                        });
                    }

                }catch(Exception e){
                    e.printStackTrace();
                }

            }
        }
    }

    private void phaseManager(GameMessage gameMessage){
        if(gameMessage.getPlayer()!=this.player){
            return;
        }
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
                break;
            case BUILD://non viene chiamata...probabile errore nell'invio dei messaggi
                performBuild();
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
            phaseManager(arg);
            if(arg instanceof GameBoardMessage){
                updateBoard(((GameBoardMessage)arg).getBoard(), false);
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
