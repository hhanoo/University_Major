package Frame;

import Game.Client;
import Game.Tag;

import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.Color;
import java.awt.Font;

public class LobbyFrame extends JFrame {

    /* Panel */
    JPanel contentPane = new JPanel();

    /* Label */
    JLabel room_Label = new JLabel("게임룸 목록");
    JLabel user_Label = new JLabel("접속 유저 목록");
    JLabel chat_Label = new JLabel("채팅");

    /* List */
    public JList<String> room_List = new JList<>();
    public JList<String> user_List = new JList<>();
    public JList<String> chat_List = new JList<>();

    /* JScrollPane */
    JScrollPane roomList_sp = new JScrollPane(room_List);
    JScrollPane userList_sp = new JScrollPane(user_List);
    JScrollPane chatting_sp = new JScrollPane(chat_List);

    /* Button */
    JButton makeRoom_Btn = new JButton("게임룸 생성");
    JButton enterRoom_Btn = new JButton("게임룸 입장");
    JButton chatSend_Btn = new JButton("보내기");
    JButton exit_Btn = new JButton("종료");

    /* TextField */
    JTextField chat_Field = new JTextField();

    /* 게임룸 변수*/
    public String roomCreatName = null; // 게임룸 생성 이름
    public String roomSelectName = null; // 게임룸 선택 이름

    //메세지에 대한 상세 코드
    Tag tag = new Tag();

    Client client = null;

    public LobbyFrame(Client client_) {
        this.client = client_;

        /** 기본 GUI 설정 */
        setTitle("로비");
        setLocationRelativeTo(null); // 창을 화면 가운데에 띄움
        setResizable(false); // 창의 크기 조절 불가능
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // 종료시 프로레스 종료


        /** GUI 디자인 */
        setBounds(100, 100, 624, 550);
        contentPane.setBackground(Color.LIGHT_GRAY);
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);
        exit_Btn.setForeground(Color.WHITE);
        exit_Btn.setBackground(Color.GRAY);
        contentPane.add(exit_Btn);
        exit_Btn.setBounds(10, 5, 60, 25);


        /** 게임룸 관련 GUI*/
        room_Label.setFont(new Font("굴림", Font.BOLD, 15));
        room_Label.setBounds(40, 16, 320, 15);
        roomList_sp.setBounds(10, 37, 380, 158);
        makeRoom_Btn.setBackground(Color.GRAY);
        makeRoom_Btn.setForeground(Color.WHITE);
        makeRoom_Btn.setBounds(10, 201, 185, 30);
        enterRoom_Btn.setForeground(Color.WHITE);
        enterRoom_Btn.setBackground(Color.GRAY);
        enterRoom_Btn.setBounds(207, 201, 185, 30);

        room_Label.setHorizontalAlignment(SwingConstants.CENTER);
        room_List.setBorder(new EmptyBorder(5, 5, 5, 5));

        contentPane.add(room_Label);
        contentPane.add(roomList_sp);
        contentPane.add(makeRoom_Btn);
        contentPane.add(enterRoom_Btn);


        /** 유저 관련 GUI*/
        user_Label.setFont(new Font("굴림", Font.BOLD, 15));
        user_Label.setBounds(402, 16, 196, 15);
        userList_sp.setBounds(400, 37, 200, 457);

        user_Label.setHorizontalAlignment(SwingConstants.CENTER);
        user_List.setBorder(new EmptyBorder(5, 5, 5, 5));

        contentPane.add(user_Label);
        contentPane.add(userList_sp);


        /** 채팅 관련 GUI*/
        chat_Label.setFont(new Font("굴림", Font.BOLD, 15));
        chat_Label.setBounds(10, 248, 380, 15);
        chatting_sp.setBounds(10, 269, 380, 189);
        chat_Field.setBounds(10, 464, 283, 30);
        chatSend_Btn.setBackground(Color.GRAY);
        chatSend_Btn.setForeground(Color.WHITE);
        chatSend_Btn.setBounds(295, 464, 95, 30);

        chat_Label.setHorizontalAlignment(SwingConstants.CENTER);
        chat_List.setBorder(new EmptyBorder(5, 5, 5, 5));
        chat_Field.setColumns(10);

        contentPane.add(chat_Label);
        contentPane.add(chatting_sp);
        contentPane.add(chat_Field);
        contentPane.add(chatSend_Btn);


        /** Main Button 이벤트 리스너 */
        ButtonListener bl = new ButtonListener();

        makeRoom_Btn.addActionListener(bl);
        enterRoom_Btn.addActionListener(bl);
        chatSend_Btn.addActionListener(bl);
        exit_Btn.addActionListener(bl);

        /** Keyboard 이벤트 리스너 추가 */
        KeyBoardListener kl = new KeyBoardListener();

        chat_Field.addKeyListener(kl);

        /** Mouse 이벤트 추가 */
        room_List.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (!room_List.isSelectionEmpty()) {
                    String[] m = room_List.getSelectedValue().split(" : ");
                    roomSelectName = m[0];
                }
            }

            public void mousePressed(MouseEvent e) {
            }

            public void mouseReleased(MouseEvent e) {
            }

            public void mouseEntered(MouseEvent e) {
            }

            public void mouseExited(MouseEvent e) {
            }
        });
    }

    /**
     * Button 이벤트 리스너
     */
    class ButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            JButton b = (JButton) e.getSource();


            /** 게임룸 생성 버튼 이벤트 */
            if (b.getText().equals("게임룸 생성")) {
                // 생성하는 방 이름 입력
                roomCreatName = JOptionPane.showInputDialog(null, "생성할 게임룸 이름 입력", "게임룸 생성", JOptionPane.QUESTION_MESSAGE);

                if (roomCreatName != null) { // 게임룸 이름이 정상적으로 입력되었을 때
                    client.sendMsg(tag.roomCreateTag + "//" + roomCreatName);
                    System.out.println("[CLIENT] 게임룸 생성 성공");
                } else { // 게임룸 이름을 작성하지 않았을 때
                    JOptionPane.showMessageDialog(null, "게임룸 이름이 입력되지 않음", "게임룸 생성 실패", JOptionPane.ERROR_MESSAGE);
                    System.out.println("[CLIENT] 게임룸 생성 실패 >> 게임룸 이름 입력되지 않음");
                }
            }

            /** 게임룸 입장 버튼 이벤트 */
            else if (b.getText().equals("게임룸 입장")) {
                if (roomSelectName != null) { // 선택된 게임룸 이름이 정상일 때
                    client.sendMsg(tag.roomEnterTag + "//" + roomSelectName);
                    System.out.println("[CLIENT] 게임룸 입장 성공");
                } else { // 선태된 게임룸 이룸이 null 일 때
                    JOptionPane.showMessageDialog(null, "입장할 게임룸이 선택되지 않음", "게임룸 입장 실패", JOptionPane.ERROR_MESSAGE);
                    System.out.println("[CLIENT] 게임룸 입장 실패 >> 게임룸 선택되지 않음");
                }
            }
            /** 채팅 전송 버튼 이벤트 */
            else if (b.getText().equals("보내기")) {
                String chatMSG = chat_Field.getText();
                if (!chatMSG.equals("")) {
                    client.sendMsg(tag.chatLobbyTag + "//" + chatMSG); //서버에 채팅 정보 전송
                    System.out.println("[CLIENT] 로비 채팅 전송 >> " + chatMSG);
                    chat_Field.setText("");
                }
            }
            /** 게임 종료 버튼 이벤트 */
            else if (b.getText().equals("종료")) {
                System.out.println("[CLIENT] 게임 종료");
                client.sendMsg(tag.ExitTag + "//");
                System.exit(0);
            }
        }
    }

    /** Key 이벤트 리스너 */
    class KeyBoardListener implements KeyListener {
        @Override
        public void keyPressed(KeyEvent e) {
            /* Enter 키 이벤트 */
            if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                String chatMSG = chat_Field.getText();
                if(!chatMSG.equals("")){
                    client.sendMsg(tag.chatLobbyTag + "//" + chatMSG); //서버에 채팅 정보 전송
                    System.out.println("[CLIENT] 로비 채팅 전송 >> " + chatMSG);
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
