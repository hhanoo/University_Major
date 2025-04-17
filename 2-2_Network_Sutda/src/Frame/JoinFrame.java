package Frame;

import Game.Client;
import Game.Tag;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

// 회원가입 기능 인터페이스
public class JoinFrame extends JFrame {
    /* Panel */
    JPanel panel = new JPanel();

    /* Label */
    JLabel nameL = new JLabel("이름");
    JLabel nicknameL = new JLabel("닉네임");
    JLabel idL = new JLabel("아이디");
    JLabel pwL = new JLabel("비밀번호");
    JLabel emailL = new JLabel("이메일");
    JLabel SNSL = new JLabel("SNS or HomePage");

    /* TextField */
    public JTextField name = new JTextField();
    public JTextField nickname = new JTextField();
    public JTextField id = new JTextField();
    public JPasswordField pw = new JPasswordField();
    public JTextField email = new JTextField();
    public JTextField sns = new JTextField();

    /* Button */
    JButton nickCheckBtn = new JButton("확인");
    JButton idCheckBtn = new JButton("확인");
    JButton joinBtn = new JButton("가입하기");
    JButton cancelBtn = new JButton("가입취소");

    Client client = null;

    //메세지에 대한 상세 코드
    Tag tag = new Tag();

    public JoinFrame(Client client_) {
        client = client_;

        setTitle("회원가입");

        /** Label 크기 작업 */
        nameL.setPreferredSize(new Dimension(60, 30));
        nicknameL.setPreferredSize(new Dimension(60, 30));
        idL.setPreferredSize(new Dimension(60, 30));
        pwL.setPreferredSize(new Dimension(60, 30));
        emailL.setPreferredSize(new Dimension(60, 30));
        SNSL.setPreferredSize(new Dimension(60, 30));
        /** TextField 크기 작업 */
        name.setPreferredSize(new Dimension(210, 30));
        nickname.setPreferredSize(new Dimension(145, 30));
        id.setPreferredSize(new Dimension(145, 30));
        pw.setPreferredSize(new Dimension(210, 30));
        email.setPreferredSize(new Dimension(210, 30));
        sns.setPreferredSize(new Dimension(210, 30));
        /** Button 크기 작업 */
        nickCheckBtn.setForeground(Color.WHITE);
        nickCheckBtn.setBackground(Color.GRAY);
        nickCheckBtn.setPreferredSize(new Dimension(60, 30));
        idCheckBtn.setForeground(Color.WHITE);
        idCheckBtn.setBackground(Color.GRAY);
        idCheckBtn.setPreferredSize(new Dimension(60, 30));
        joinBtn.setBackground(Color.GRAY);
        joinBtn.setForeground(Color.WHITE);
        joinBtn.setPreferredSize(new Dimension(135, 30));
        cancelBtn.setForeground(Color.WHITE);
        cancelBtn.setBackground(Color.GRAY);
        cancelBtn.setPreferredSize(new Dimension(135, 30));

        /** Panel 추가 작업 */
        panel.setBackground(Color.LIGHT_GRAY);
        setContentPane(panel); //panel을 기본 컨테이너로 설정

        panel.add(nameL);
        panel.add(name);

        panel.add(nicknameL);
        panel.add(nickname);
        panel.add(nickCheckBtn);

        panel.add(idL);
        panel.add(id);
        panel.add(idCheckBtn);

        panel.add(pwL);
        panel.add(pw);

        panel.add(emailL);
        panel.add(email);

        panel.add(SNSL);
        panel.add(sns);

        panel.add(cancelBtn);
        panel.add(joinBtn);


        /** Button 이벤트 리스너 추가 */
        ButtonListener bl = new ButtonListener();

        cancelBtn.addActionListener(bl);
        joinBtn.addActionListener(bl);

        /** 닉네임 중복확인 Button 이벤트 */
        nickCheckBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!nickname.getText().equals("")) {
                    System.out.println("[Client] 닉네임 중복 확인 >> 사용 가능!");
                    client.sendMsg(tag.OverTag + "//nickname//" + nickname.getText()); //서버에 중복코드와 닉네임 전송
                }
            }
        });

        /** 아이디 중복확인 Button 이벤트 */
        idCheckBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!id.getText().equals("")) {
                    System.out.println("[Client] 아이디 중복 확인 >> 사용 가능!");
                    client.sendMsg(tag.OverTag + "//id//" + id.getText()); //서버에 중복코드와 아이디 전송

                }
            }
        });

        setSize(310, 290); // 창의 가로,세로 길이 설정
        setLocationRelativeTo(null); // 창을 화면 가운데에 띄움
        setResizable(false); // 창의 크기 조절 불가능
    }

    /* Button 이벤트 리스너 */
    class ButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            JButton b = (JButton) e.getSource();

            /* TextField에 입력된 회원 정보들을 변수에 초기화 */
            String uName = name.getText();
            String uNick = nickname.getText();
            String uID = id.getText();
            String uPW = "";
            for (int i = 0; i < pw.getPassword().length; i++) {
                uPW = uPW + pw.getPassword()[i];
            }
            String uEmail = email.getText();
            String uSNS = sns.getText();
            /* 가입취소 버튼 이벤트 */
            if (b.getText().equals("가입취소")) {
                System.out.println("[Client] 회원가입 인터페이스 종료");
                dispose();    //인터페이스 닫음
            }

            /* 가입하기 버튼 이벤트 */
            else if (b.getText().equals("가입하기")) {
                if (uName.equals("") || uNick.equals("") || uID.equals("") || uPW.equals("") || uEmail.equals("") || uSNS.equals("")) {
                    //모든 정보가 입력되지 않으면 회원가입 시도 실패
                    JOptionPane.showMessageDialog(null, "모든 정보를 기입해주세요", "회원가입 실패", JOptionPane.ERROR_MESSAGE);
                    System.out.println("[Client] 회원가입 실패 >> 회원정보 미입력");
                } else if (!uName.equals("") && !uNick.equals("") && !uID.equals("") && !uPW.equals("") && !uEmail.equals("") && !uSNS.equals("")) {
                    //회원가입 시도 성공
                    client.sendMsg(tag.JoinTag + "//" + uName + "//" + uNick + "//" + uID + "//" + uPW + "//" + uEmail + "//" + uSNS);    //서버에 회원가입 정보 전송
                }
            }
        }
    }
}
