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

public class GameReadyFrame extends JFrame {
    /* Panel */
    JPanel basePanel = new JPanel();
    JPanel player1_Panel = new JPanel();
    JPanel player2_Panel = new JPanel();

    /* Label */
    //Player 1의 label
    public JLabel player1_Nickname = new JLabel("Player1_Nickname");
    JLabel player1_record_text = new JLabel("Game recode (W/L) : ");
    public JLabel player1_GameRecord = new JLabel("Win / Lose");

    //Player 2의 label
    public JLabel player2_Nickname = new JLabel("Player2_Nickname");
    public JLabel player2_GameRecord = new JLabel("Win / Lose");
    JLabel player2_record_text = new JLabel("Game recode (W/L) : ");

    /* List */
    public JList<String> chat_List = new JList<String>();

    /* JScrollPane */
    JScrollPane chatting_sp = new JScrollPane(chat_List);

    /* Button */
    String backLobby_text = "<<   대기실로";
    String chat_text = "보내기";
    String readyX = "게임 준비";
    String readyO = "게임 준비 완료";
    JButton backLobby_Btn = new JButton(backLobby_text);
    public JButton ready_Btn = new JButton(readyX);
    JButton chat_Btn = new JButton(chat_text);

    /* TextField */
    JTextField chat_Field = new JTextField();

    //메세지에 대한 상세 코드
    Tag tag = new Tag();

    public String playerType = "";
    Client client = null;

    public GameReadyFrame(Client client_) {
    	chat_List.setBorder(new EmptyBorder(5, 5, 5, 5));

        this.client = client_;

        /** 기본 GUI 설정 */
        setTitle("게임룸 준비");
        setLocationRelativeTo(null); // 창을 화면 가운데에 띄움
        setResizable(false); // 창의 크기 조절 불가능
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // 종료시 프로레스 종료
        setBounds(100, 100, 800, 550);

        /** GUI 디자인 */
        basePanel.setBackground(Color.LIGHT_GRAY);
        basePanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        backLobby_Btn.setFont(new Font("굴림", Font.PLAIN, 14));
        backLobby_Btn.setForeground(Color.WHITE);
        backLobby_Btn.setBackground(Color.GRAY);
        backLobby_Btn.setBounds(12, 10, 150, 38);
        player1_Panel.setBounds(12, 58, 370, 210);
        player2_Panel.setBounds(404, 58, 370, 210);

        setContentPane(basePanel);
        basePanel.setLayout(null);
        basePanel.add(player1_Panel);
        basePanel.add(player2_Panel);
        basePanel.add(backLobby_Btn);
        basePanel.add(chatting_sp);
        basePanel.add(chat_Field);
        chat_Btn.setFont(new Font("굴림", Font.PLAIN, 14));
        chat_Btn.setForeground(Color.WHITE);
        chat_Btn.setBackground(Color.GRAY);
        basePanel.add(chat_Btn);


        /** Player1의 Panel 설정 */
        player1_Panel.setBackground(Color.WHITE);
        player1_record_text.setBounds(12, 110, 230, 50);
        player1_GameRecord.setBounds(243, 110, 115, 50);
        player1_Nickname.setBounds(12, 10, 346, 75);

        player1_record_text.setFont(new Font("굴림", Font.BOLD, 20));
        player1_GameRecord.setFont(new Font("굴림", Font.BOLD, 20));
        player1_Nickname.setFont(new Font("굴림", Font.BOLD, 20));

        player1_Panel.setLayout(null);
        player1_Panel.add(player1_record_text);
        player1_Panel.add(player1_GameRecord);
        player1_Panel.add(player1_Nickname);


        /** Player2의 Panel 설정 */
        player2_Panel.setBackground(Color.WHITE);
        player2_Nickname.setBounds(12, 10, 356, 75);
        player2_record_text.setBounds(12, 110, 230, 50);
        player2_GameRecord.setBounds(243, 110, 115, 50);

        player2_Nickname.setFont(new Font("굴림", Font.BOLD, 20));
        player2_record_text.setFont(new Font("굴림", Font.BOLD, 20));
        player2_GameRecord.setFont(new Font("굴림", Font.BOLD, 20));

        player2_Panel.setLayout(null);
        player2_Panel.add(player2_Nickname);
        player2_Panel.add(player2_record_text);
        player2_Panel.add(player2_GameRecord);


        /** 채팅 관련 */
        chatting_sp.setBounds(12, 278, 762, 177);
        chat_Btn.setBounds(685, 465, 89, 38);
        chat_Field.setBounds(12, 465, 671, 38);
        ready_Btn.setFont(new Font("굴림", Font.PLAIN, 14));
        ready_Btn.setForeground(Color.WHITE);
        ready_Btn.setBackground(Color.GRAY);
        ready_Btn.setBounds(628, 10, 150, 38);
        chatting_sp.setViewportView(chat_List);
        chat_Field.setColumns(10);
        basePanel.add(ready_Btn);

        /** Button 이벤트 리스너 추가 */
        ButtonListener bl = new ButtonListener();

        backLobby_Btn.addActionListener(bl);
        chat_Btn.addActionListener(bl);
        ready_Btn.addActionListener(bl);

        /** Keyboard 이벤트 리스너 추가 */
        KeyBoardListener kl = new KeyBoardListener();

        chat_Field.addKeyListener(kl);
    }

    /**
     * Button 이벤트 리스너
     */
    class ButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            JButton b = (JButton) e.getSource();

            /** 게임룸 준비방에서 퇴장 이벤트 */
            if (b.getText().equals(backLobby_text)) {
                System.out.println("[CLIENT] 게임룸 준비방 퇴장 버튼 눌림");
                client.gameReadyFrame.setVisible(false);
                client.lobbyFrame.setVisible(true);
                client.sendMsg(tag.backLobbyTag + "//" + tag.successTag);
            }
            /** 채팅 전송 버튼 이벤트 */
            else if (b.getText().equals(chat_text)) {
                String chatMSG = chat_Field.getText();
                if (!chatMSG.equals("")) {
                    client.sendMsg(tag.chatGameReadyTag + "//" + chatMSG); //서버에 채팅 정보 전송
                    System.out.println("[CLIENT] 게임룸 채팅 전송 >> " + chatMSG);
                    chat_Field.setText("");
                }
            }
            /** 준비 버튼 이벤트 */
            // 해당 유저가 준비 상태를 ON 할때
            else if (b.getText().equals(readyX)) {
                client.sendMsg(tag.readyONTag + "//" + playerType + "//");
                System.out.println("[CLIENT] player 준비 완료!");
                ready_Btn.setText(readyO);
            }
            // 해당 유저가 준비 상태를 OFF 할때
            else if (b.getText().equals(readyO)) {
                client.sendMsg(tag.readyOFFTag + "//" + playerType + "//");
                System.out.println("[CLIENT] player 준비 취소!");
                ready_Btn.setText(readyX);
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
                    client.sendMsg(tag.chatGameReadyTag + "//" + chatMSG); //서버에 채팅 정보 전송
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