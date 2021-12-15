package Frame;

import Game.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

//로그인 기능을 수행하는 인터페이스
public class LoginFrame extends JFrame {

    /* Panel */
    JPanel basePanel = new JPanel(new BorderLayout());
    JPanel centerPanel = new JPanel(new BorderLayout());
    JPanel leftPanel = new JPanel();
    JPanel rightPanel = new JPanel();
    JPanel bottomPanel = new JPanel();

    /* Label */
    JLabel idL = new JLabel("아이디");
    JLabel pwL = new JLabel("비밀번호");

    /* TextField */
    public JTextField id = new JTextField();
    public JPasswordField pw = new JPasswordField();

    /* Button */
    JButton loginBtn = new JButton("로그인");
    JButton joinBtn = new JButton("회원가입");
    JButton exitBtn = new JButton("게임종료");

    //메세지에 대한 상세 코드
    Tag tag = new Tag();

    Client client = null;

    public LoginFrame(Client client_) {
        this.client = client_;

        setTitle("로그인");

        /* Panel 크기 작업 */
        centerPanel.setPreferredSize(new Dimension(260, 80));
        leftPanel.setPreferredSize(new Dimension(210, 75));
        rightPanel.setPreferredSize(new Dimension(90, 75));
        bottomPanel.setPreferredSize(new Dimension(290, 40));

        /* Label 크기 작업 */
        idL.setPreferredSize(new Dimension(50, 30));
        pwL.setPreferredSize(new Dimension(50, 30));

        /* TextField 크기 작업 */
        id.setPreferredSize(new Dimension(140, 30));
        pw.setPreferredSize(new Dimension(140, 30));

        /* Button 크기 작업 */
        loginBtn.setBackground(Color.GRAY);
        loginBtn.setForeground(Color.WHITE);
        loginBtn.setPreferredSize(new Dimension(75, 63));
        joinBtn.setBackground(Color.GRAY);
        joinBtn.setForeground(Color.WHITE);
        joinBtn.setPreferredSize(new Dimension(135, 25));
        exitBtn.setForeground(Color.WHITE);
        exitBtn.setBackground(Color.GRAY);
        exitBtn.setPreferredSize(new Dimension(135, 25));

        /* Panel 추가 작업 */
        basePanel.setBackground(Color.LIGHT_GRAY);
        setContentPane(basePanel); // basePanel 기본으로 설정

        basePanel.add(centerPanel, BorderLayout.CENTER);
        basePanel.add(bottomPanel, BorderLayout.SOUTH);
        centerPanel.add(leftPanel, BorderLayout.WEST);
        centerPanel.add(rightPanel, BorderLayout.EAST);

        leftPanel.setLayout(new FlowLayout());
        rightPanel.setLayout(new FlowLayout());
        bottomPanel.setLayout(new FlowLayout());

        /* leftPanel 컴포넌트 */
        leftPanel.add(idL);
        leftPanel.add(id);
        leftPanel.add(pwL);
        leftPanel.add(pw);

        /* rightPanel 컴포넌트 */
        rightPanel.add(loginBtn);

        /* bottomPanel 컴포넌트 */
        bottomPanel.add(exitBtn);
        bottomPanel.add(joinBtn);

        /* Button 이벤트 리스너 추가 */
        ButtonListener bl = new ButtonListener();

        loginBtn.addActionListener(bl);
        exitBtn.addActionListener(bl);
        joinBtn.addActionListener(bl);

        /* Keyboard 이벤트 리스너 추가 */
        KeyBoardListener kl = new KeyBoardListener();

        id.addKeyListener(kl);
        pw.addKeyListener(kl);

        setSize(310, 150); // 창의 가로,세로 길이 설정
        setLocationRelativeTo(null); // 창을 화면 가운데에 띄움
        setVisible(true); // 창을 화면에 나타냄
        setResizable(false); // 창의 크기 조절 불가능
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // 종료시 프로레스 종료
    }

    /**
     * Button 이벤트 리스너
     */
    class ButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            JButton b = (JButton) e.getSource();

            /** TextField에 입력된 아이디와 비밀번호를 변수에 초기화 */
            String uid = id.getText();
            String upass = "";
            for (int i = 0; i < pw.getPassword().length; i++) {
                upass = upass + pw.getPassword()[i];
            }

            /** 게임종료 버튼 이벤트 */
            if (b.getText().equals("게임종료")) {
                System.out.println("[CLIENT] 게임 종료");
                System.exit(0);
            }

            /** 회원가입 버튼 이벤트 */
            else if (b.getText().equals("회원가입")) {
                System.out.println("[CLIENT] 회원가입 인터페이스 열림");
                client. joinFrame.setVisible(true);
            }

            /** 로그인 버튼 이벤트 **/
            else if (b.getText().equals("로그인")) {
                if (uid.equals("") && !upass.equals("")) { //아이디 미입력 시 로그인 시도 실패
                    JOptionPane.showMessageDialog(null, "아이디를 입력해주세요", "로그인 실패", JOptionPane.ERROR_MESSAGE);
                    System.out.println("[CLIENT] 로그인 실패 >> 아이디 미입력");
                } else if (!uid.equals("") && upass.equals("")) {    //비밀번호 미입력 시 로그인 시도 실패
                    JOptionPane.showMessageDialog(null, "비밀번호를 입력해주세요", "로그인 실패", JOptionPane.ERROR_MESSAGE);
                    System.out.println("[CLIENT] 로그인 실패 >> 비밀번호 미입력");
                } else if (!uid.equals("") && !upass.equals("")) {    //로그인 시도 성공
                    client.sendMsg(tag.LoginTag + "//" + uid + "//" + upass);    //서버에 로그인 정보 전송
                }
            }
        }
    }

    /* Key 이벤트 리스너 */
    class KeyBoardListener implements KeyListener {
        @Override
        public void keyPressed(KeyEvent e) {
            /* Enter 키 이벤트 */
            if (e.getKeyCode() == KeyEvent.VK_ENTER) {

                /* TextField에 입력된 아이디와 비밀번호를 변수에 초기화 */
                String uid = id.getText();
                String upass = "";
                for (int i = 0; i < pw.getPassword().length; i++) {
                    upass = upass + pw.getPassword()[i];
                }

                if (uid.equals("") && !upass.equals("")) {
                    JOptionPane.showMessageDialog(null, "아이디를 입력해주세요", "로그인 실패", JOptionPane.ERROR_MESSAGE);
                    System.out.println("[CLIENT] 로그인 실패 >> 아이디 미입력");
                } else if (!uid.equals("") && upass.equals("")) {
                    JOptionPane.showMessageDialog(null, "비밀번호를 입력해주세요", "로그인 실패", JOptionPane.ERROR_MESSAGE);
                    System.out.println("[CLIENT] 로그인 실패 >> 비밀번호 미입력");
                } else if (!uid.equals("") && !upass.equals("")) {
                    client.sendMsg(tag.LoginTag + "//" + uid + "//" + upass);
                }
            }
        }

        public void keyTyped(KeyEvent e) {
        }

        public void keyReleased(KeyEvent e) {
        }
    }
}