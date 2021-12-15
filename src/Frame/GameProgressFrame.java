package Frame;

import Game.Client;
import Game.Tag;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class GameProgressFrame extends JFrame {
    //메세지에 대한 상세 코드
    Tag tag = new Tag();

    /* number */
    String card1 = "000";
    String card2 = "000";
    String card3 = "000";
    String card4 = "000";

    /* Panel */
    JPanel basePanel = new JPanel();
    JPanel player1_Panel = new JPanel();
    JPanel player2_Panel = new JPanel();

    /* Label */
    public JLabel player1_card1 = new JLabel();
    public JLabel player1_card2 = new JLabel();
    public JLabel player2_card1 = new JLabel();
    public JLabel player2_card2 = new JLabel();
    public JLabel player1_Nick = new JLabel("Player1_Nickname");
    public JLabel player2_Nick = new JLabel("Player2_Nickname");
    public JLabel player1_cardType = new JLabel("Player1_cardType");
    public JLabel player2_cardType = new JLabel("Player2_cardType");

    /* List */
    public JList<String> chat_List = new JList<String>();

    /* JScrollPane */
    JScrollPane chatting_sp = new JScrollPane(chat_List);

    /* Button */
    String chat_text = "보내기";
    String returnLobby_text="<<   로비로 돌아가기";
    String reGame_textX = "재시작 준비";
    String reGame_textO = "재시작 준비 완료";
    JButton chat_Btn = new JButton(chat_text);
    public JButton restart_Btn = new JButton(reGame_textX);
    JButton lobby_Btn = new JButton(returnLobby_text);

    /* TextField */
    JTextField chat_Field = new JTextField();

    public String playerType = "";
    Client client = null;

    public GameProgressFrame(Client client_) {


        this.client = client_;

        /** 기본 GUI 설정 */
        setLocationRelativeTo(null); // 창을 화면 가운데에 띄움
        setResizable(false); // 창의 크기 조절 불가능
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // 종료시 프로레스 종료
        setBounds(100, 100, 800, 500);

        /** GUI 디자인 */
        basePanel.setBackground(Color.LIGHT_GRAY);
        basePanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        restart_Btn.setFont(new Font("굴림", Font.PLAIN, 14));
        restart_Btn.setForeground(Color.WHITE);
        restart_Btn.setBackground(Color.GRAY);
        restart_Btn.setBounds(624, 14, 150, 38);
        lobby_Btn.setFont(new Font("굴림", Font.PLAIN, 12));
        lobby_Btn.setForeground(Color.WHITE);
        lobby_Btn.setBackground(Color.GRAY);
        lobby_Btn.setBounds(12, 14, 150, 38);

        setContentPane(basePanel);
        basePanel.setLayout(null);
        basePanel.add(player1_Panel);
        basePanel.add(player2_Panel);
        basePanel.add(chatting_sp);
        basePanel.add(chat_Field);
        chat_Btn.setForeground(Color.WHITE);
        chat_Btn.setBackground(Color.GRAY);
        basePanel.add(chat_Btn);
        basePanel.add(restart_Btn);
        basePanel.add(lobby_Btn);


        /** Player1의 Panel 설정 */
        player1_Panel.setBackground(Color.WHITE);
        player1_cardType.setFont(new Font("굴림", Font.PLAIN, 20));
        player1_cardType.setBackground(Color.LIGHT_GRAY);

        player1_Panel.setBounds(12, 58, 380, 230);
        player1_cardType.setBounds(12, 190, 356, 30);
        player1_cardType.setHorizontalAlignment(SwingConstants.CENTER);

        player1_card1.setSize(90, 122);
        player1_card2.setSize(90, 122);
        player1_card1.setLocation(74, 50);
        player1_card2.setLocation(220, 50);

        player1_Panel.setLayout(null);
        player1_Panel.add(player1_card1);
        player1_Panel.add(player1_card2);
        player1_Panel.add(player1_cardType);
        player1_Nick.setFont(new Font("굴림", Font.PLAIN, 20));
        player1_Nick.setBounds(12, 10, 356, 30);
        player1_Panel.add(player1_Nick);
        player1_Nick.setBackground(Color.WHITE);

        player1_Nick.setHorizontalAlignment(SwingConstants.CENTER);

        /** Player2의 Panel 설정 */
        player2_Panel.setBackground(Color.WHITE);
        player2_cardType.setFont(new Font("굴림", Font.PLAIN, 20));
        player2_cardType.setBackground(Color.LIGHT_GRAY);

        player2_card1.setBounds(65, 50, 90, 122);
        player2_card2.setBounds(220, 50, 90, 122);
        player2_Panel.setBounds(404, 58, 370, 230);
        player2_cardType.setBounds(12, 190, 346, 30);
        player2_cardType.setHorizontalAlignment(SwingConstants.CENTER);

        player2_Panel.setLayout(null);
        player2_Panel.add(player2_card1);
        player2_Panel.add(player2_card2);
        player2_Panel.add(player2_cardType);
        player2_Nick.setFont(new Font("굴림", Font.PLAIN, 20));
        player2_Nick.setBounds(12, 10, 346, 30);
        player2_Panel.add(player2_Nick);
        player2_Nick.setBackground(Color.WHITE);

        player2_Nick.setHorizontalAlignment(SwingConstants.CENTER);


        /** 채팅 관련 */
        chat_Field.setBounds(12, 428, 670, 25);
        chat_Btn.setBounds(684, 428, 90, 25);
        chatting_sp.setBounds(12, 295, 762, 130);
        
        chatting_sp.setViewportView(chat_List);
        chat_Field.setColumns(10);

        /**  Button 이벤트 리스너 */
        ButtonListener bl = new ButtonListener();

        restart_Btn.addActionListener(bl);
        lobby_Btn.addActionListener(bl);
        chat_Btn.addActionListener(bl);

        /** Keyboard 이벤트 리스너 추가 */
        KeyBoardListener kl = new KeyBoardListener();

        chat_Field.addKeyListener(kl);
    }

    /**
     * Main Button 겸 게임 이벤트 리스너
     */
    class ButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            JButton b = (JButton) e.getSource();

            /** 채팅 전송 버튼 이벤트 */
            if (b.getText().equals(chat_text)) {
                String chatMSG = chat_Field.getText();
                if (!chatMSG.equals("")) {
                    client.sendMsg(tag.chatGameProTag + "//" + chatMSG); //서버에 채팅 정보 전송
                    System.out.println("[CLIENT] 게임룸 채팅 전송 >> " + chatMSG);
                    chat_Field.setText("");
                }
            }
            /** 로비로 돌아가기 버튼 이벤트 */
            else if(b.getText().equals(returnLobby_text)) {
                System.out.println("[CLIENT] 게임룸 진행방 퇴장 버튼 눌림");
                client.gameProgressFrame.setVisible(false);
                client.lobbyFrame.setVisible(true);
                client.sendMsg(tag.compBackLobbySignTag + "//" + tag.successTag);
            }
            /** 게임 재시작 버튼 이벤트 */
            // 해당 유저가 준비 상태를 ON 할때
            else if (b.getText().equals(reGame_textX)) {
                client.sendMsg(tag.reGame + "//" + playerType + "//");
                System.out.println("[CLIENT] 게임룸 진행방 재시작 준비 완료!");
                restart_Btn.setText(reGame_textO);
            }
            // 해당 유저가 준비 상태를 OFF 할때
            else if (b.getText().equals(reGame_textO)) {
                client.sendMsg(tag.reGame + "//" + playerType + "//");
                System.out.println("[CLIENT] 게임룸 진행방 재시작 준비 취소!");
                restart_Btn.setText(reGame_textX);
            }
        }
    }

    /**
     * Key 이벤트 리스너
     */
    class KeyBoardListener implements KeyListener {
        @Override
        public void keyPressed(KeyEvent e) {
            /* Enter 키 이벤트 */
            if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                String chatMSG = chat_Field.getText();
                if (!chatMSG.equals("")) {
                    client.sendMsg(tag.chatGameProTag + "//" + chatMSG); //서버에 채팅 정보 전송
                    System.out.println("[CLIENT] 게임룸 채팅 전송 >> " + chatMSG);
                    chat_Field.setText("");
                }
            }
        }

        public void keyTyped(KeyEvent e) {
        }

        public void keyReleased(KeyEvent e) {
        }
    }
}