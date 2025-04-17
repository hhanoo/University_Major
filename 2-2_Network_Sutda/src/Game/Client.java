package Game;

import Frame.*;

import java.io.*;
import java.net.*;
import javax.swing.*;

/**
 * 서버와 연결과 인터페이스 관리
 */
public class Client {
    Socket mySocket = null;

    // 메세지를 송신하기 위한 선언
    OutputStream outputStream = null;
    DataOutputStream dataOutput = null;

    // 각 인터페이스를 관리하기 위한 선언
    public LoginFrame loginFrame = null;
    public JoinFrame joinFrame = null;
    public LobbyFrame lobbyFrame = null;
    public GameReadyFrame gameReadyFrame = null;
    public GameProgressFrame gameProgressFrame = null;

    public static void main(String[] args) {
        Client client = new Client();
        try {
            /** 서버와 연결 */
            client.mySocket = new Socket("localhost", 9998);
            System.out.println("[CLIENT] 서버 연결 성공");

            client.outputStream = client.mySocket.getOutputStream();
            client.dataOutput = new DataOutputStream(client.outputStream);

            /** 인터페이스 선언 */
            client.loginFrame = new LoginFrame(client);
            client.joinFrame = new JoinFrame(client);
            client.lobbyFrame = new LobbyFrame(client);
            client.gameReadyFrame = new GameReadyFrame(client);
            client.gameProgressFrame = new GameProgressFrame(client);

            MessageListener messageListener = new MessageListener(client, client.mySocket);
            messageListener.start(); // thread 시작
        } catch (SocketException e) {
            System.out.println("[CLIENT] 서버 소켓 오류 >> " + e);
        } catch (IOException e) {
            System.out.println("[CLIENT] 입출력 오류 >> " + e);
        }
    }

    /**
     * 서버에 메시지 전송
     */
    public void sendMsg(String message) {
        try {
            dataOutput.writeUTF(message);
            System.out.println(">> 서버로 보내는 순수 메세지 >> " + message);
        } catch (Exception e) {
            System.out.println("[CLIENT] 메시지 전송 오류 >> " + e);
        }
    }
}

/**
 * 서버와 메시지 송수신 관리
 */
class MessageListener extends Thread {
    Socket socket;
    Client client;

    // 메세지를 수신하기위한 선언
    InputStream inputStream;
    DataInputStream dataInput;

    // 메세지 저상 변수
    String message;
    String roomName;

    //메세지에 대한 상세 코드
    Tag tag = new Tag();

    MessageListener(Client client_, Socket socket_) {
        this.client = client_;
        this.socket = socket_;
    }

    public void run() {
        try {
            inputStream = this.socket.getInputStream();
            dataInput = new DataInputStream(inputStream);

            while (true) {
                message = dataInput.readUTF(); // 메시지 상시 수신
                System.out.println(">> 서버로 부터 받은 순수 메세지 >> " + message);
                String[] msgData = message.split("//");
                /** 1. 로그인 */
                if (msgData[0].equals(tag.LoginTag)) {
                    loginCheck(msgData[1]);
                }/** 2. 회원가입 */
                if (msgData[0].equals(tag.JoinTag)) {
                    joinCheck(msgData[1]);
                }/** 3. 중복확인 */
                if (msgData[0].equals(tag.OverTag)) {
                    overCheck(msgData[1]);
                    System.out.println("[CLIENT] 회원 정보 중복 확인");
                }
                /** 4. 게임 룸 생성 */
                if (msgData[0].equals(tag.roomCreateTag)) {
                    createRoom(msgData[1], msgData[2]);
                }
                /** 5 - 1. 게임 룸 입장 */
                else if (msgData[0].equals(tag.roomEnterTag)) {
                    if (msgData.length > 2)
                        enterRoom(msgData[1], msgData[2]);
                    else
                        System.out.println("[CLIENT] 방 입장 실패");
                }
                /** 5 - 2. 게임룸 강제 퇴장*/
                else if (msgData[0].equals(tag.compBackLobbySignTag)) {
                    client.gameProgressFrame.setVisible(false);
                    client.lobbyFrame.setVisible(true);
                    client.sendMsg(tag.compBackLobbyTag + "//");
                }
                /** 6. 게임룸 목록  */
                else if (msgData[0].equals(tag.roomListTag)) {
                    if (msgData.length > 1) { // 게임룸이 존재할 때
                        roomList(msgData[1]);
                    } else { // 게임룸이 존재하지 않을 때
                        String[] room = {""}; // 목록 공백으로 설정
                        client.lobbyFrame.room_List.setListData(room);
                    }
                }
                /** 7. 게임 */
                /** 7 - 1. 게임 시작 */
                else if (msgData[0].equals(tag.startGame)) {
                    startGame(msgData[1], msgData[2], msgData[3], msgData[4]);
                }
                /** 7 - 2. 게임 재시작 */
                else if (msgData[0].equals(tag.reGame)) {
                    reStartGame(msgData[1], msgData[2], msgData[3], msgData[4]);
                }
                /** 8. 로비 유저 목록 */
                else if (msgData[0].equals(tag.lobbyUserTag)) {
                    viewLobbyUser(msgData[1]);
                }
                /** 9. 로비로 돌아가기 */
                else if (msgData[0].equals(tag.backLobbyTag)) {
                    backLobby(msgData[1]);
                }
                /** 10. 로비 채팅 */
                else if (msgData[0].equals(tag.chatLobbyTag)) {
                    viewLobbyChat(msgData[1]);
                }
                /** 11 - 1. 게임룸 대기방 채팅 */
                else if (msgData[0].equals(tag.chatGameReadyTag)) {
                    if (msgData.length > 1)
                        viewGameReadyChat(msgData[1]);
                }
                /** 11 - 2. 게임룸 진행방 채팅 */
                else if (msgData[0].equals(tag.chatGameProTag)) {
                    if (msgData.length > 1)
                        viewGameProChat(msgData[1]);
                }
                /** 12. 게임룸 인원 입장 또는 퇴장으로 룸 정보 업데이트 */
                else if (msgData[0].equals(tag.gameUpdateTag)) {
                    viewGameInfo(msgData[1]);
                }

            }
        } catch (IOException e) {
            System.out.println("[CLIENT] Error: 메시지 수신 오류 >> " + e);
        }
    }

    /**
     * 1. 로그인 확인 메소드
     */
    void loginCheck(String message) {
        String[] data = message.split("&");
        if (data[0].equals(tag.successTag)) { // 로그인 성공
            System.out.println("[CLIENT] 로그인 성공 >> 로그인 인터페이스 OFF >> 메인 인터페이스 ON");
            client.loginFrame.dispose(); // 로그인 인터페이스 닫기
            client.lobbyFrame.setVisible(true); // 메인 인터페이스 열기
        } else { // 로그인 실패
            System.out.println("[CLIENT] 로그인 실패 >> 회원 정보 불일치");
            JOptionPane.showMessageDialog(null, "로그인 실패", "로그인 실패", JOptionPane.ERROR_MESSAGE);
            client.loginFrame.id.setText("");
            client.loginFrame.pw.setText("");
        }
    }

    /**
     * 2. 회원 가입 확인 메소드
     */
    void joinCheck(String message) {
        if (message.equals(tag.successTag)) { // 회원가입 성공
            JOptionPane.showMessageDialog(null, "회원가입 성공!", "회원가입 성공", JOptionPane.INFORMATION_MESSAGE);
            client.joinFrame.dispose();
            System.out.println("[CLIENT] 회원가입 성공 >> 회원가입 인터페이스 OFF");
        } else { // 회원가입 실패
            JOptionPane.showMessageDialog(null, "회원가입 실패!", "회원가입 실패", JOptionPane.ERROR_MESSAGE);
            System.out.println("[CLIENT] 회원가입 실패 >> 회원가입 기입내역 초기화");
            /** 인터페이스 입력칸 빈칸으로 초기화 */
            client.joinFrame.name.setText("");
            client.joinFrame.nickname.setText("");
            client.joinFrame.id.setText("");
            client.joinFrame.pw.setText("");
            client.joinFrame.email.setText("");
            client.joinFrame.email.setText("");
        }
    }

    /**
     * 3. 회원 정보 중복 확인 메소드
     */
    void overCheck(String message) {
        if (message.equals(tag.successTag)) { // 중복한 내용이 없음 >> 사용 가능
            System.out.println("[CLIENT] 사용 가능 >> 회원정보 중복 없음");
            JOptionPane.showMessageDialog(null, "사용 가능!", "중복 확인", JOptionPane.INFORMATION_MESSAGE);
        } else { // 중복한 내용이 존재 >> 사용 불가능
            System.out.println("[CLIENT] 사용 불가능 >> 회원정보 중복 존재");
            JOptionPane.showMessageDialog(null, "사용 불가능!", "중복 확인", JOptionPane.ERROR_MESSAGE);
            client.joinFrame.nickname.setText("");
            client.joinFrame.id.setText("");
        }
    }

    /**
     * 4. 게임룸 생성 메소드
     */
    void createRoom(String message, String info) {
        if (message.equals(tag.successTag)) { // 게임룸 생성 성공
            String[] data = info.split("&");
            System.out.println("[CLIENT] GameRoom 생성 성공");
            client.lobbyFrame.setVisible(false); // 로비 창 안보이게함
            client.gameReadyFrame.setVisible(true); // 게임룸 준비방 보이게함
            client.gameReadyFrame.setTitle(client.lobbyFrame.roomCreatName); // 게임룸 이름 설정
            client.gameReadyFrame.playerType = tag.player1_Tag;
            roomName = client.lobbyFrame.roomCreatName;

            client.gameReadyFrame.player1_Nickname.setText(data[0]);
            client.gameReadyFrame.player1_GameRecord.setText(data[1]);
            client.gameProgressFrame.playerType = tag.player1_Tag;
        }
    }

    /**
     * 5. 게임룸 입장 메소드
     */
    void enterRoom(String message, String info) {
        if (message.equals(tag.successTag)) { // 게임룸 입장 성공
            String[] data = info.split("&");
            System.out.println("[CLIENT] GameRoom 입장 성공");
            client.lobbyFrame.setVisible(false); // 로비 창 안보이게함
            client.gameReadyFrame.setVisible(true); // 게임룸 준비방 보이게함
            client.gameReadyFrame.setTitle(client.lobbyFrame.roomSelectName); // 게임룸 이름 설정
            client.gameReadyFrame.playerType = tag.player2_Tag;
            roomName = client.lobbyFrame.roomSelectName;

            if (data[0].equals(tag.player1_Tag)) {
                client.gameReadyFrame.player1_Nickname.setText(data[1]);
                client.gameReadyFrame.player1_GameRecord.setText(data[2]);
            } else {
                client.gameReadyFrame.player2_Nickname.setText(data[1]);
                client.gameReadyFrame.player2_GameRecord.setText(data[2]);
            }
            client.gameProgressFrame.playerType = tag.player2_Tag;

        } else { // 게임룸 입장 실패
            System.out.println("[CLIENT] GameRoom 입장 실패");
            JOptionPane.showMessageDialog(null, "인원이 2명으로 꽉 찼습니다!", "입장 실패", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * 6. 게임룸 목록 출력 메소드
     */
    void roomList(String message) {
        if (!message.equals("")) {
            String[] room = message.split("&");

            client.lobbyFrame.room_List.setListData(room);
        }
    }

    /**
     * 7. 게임
     */
    // 7-1. 게임 시작
    void startGame(String cardList, String cardTypeList, String winner, String nickList) {
        client.gameReadyFrame.ready_Btn.setText("게임 준비");
        client.gameReadyFrame.setVisible(false);
        client.gameProgressFrame.setVisible(true);
        client.gameProgressFrame.setTitle(roomName);

        String[] card = cardList.split("&");
        ImageIcon card1Image = new ImageIcon("src/Image/" + card[0] + ".png");
        ImageIcon card2Image = new ImageIcon("src/Image/" + card[1] + ".png");
        ImageIcon card3Image = new ImageIcon("src/Image/" + card[2] + ".png");
        ImageIcon card4Image = new ImageIcon("src/Image/" + card[3] + ".png");
        client.gameProgressFrame.player1_card1.setIcon(card1Image);
        client.gameProgressFrame.player1_card2.setIcon(card2Image);
        client.gameProgressFrame.player2_card1.setIcon(card3Image);
        client.gameProgressFrame.player2_card2.setIcon(card4Image);

        String[] nick = nickList.split("&");
        client.gameProgressFrame.player1_Nick.setText(nick[0]);
        client.gameProgressFrame.player2_Nick.setText(nick[1]);

        String[] type = cardTypeList.split("&");
        client.gameProgressFrame.player1_cardType.setText(type[0]);
        client.gameProgressFrame.player2_cardType.setText(type[1]);

        JOptionPane.showMessageDialog(null, winner + "가 승리했습니다!", "결과", JOptionPane.INFORMATION_MESSAGE);
    }

    // 7-2. 게임 재시작
    void reStartGame(String cardList, String cardTypeList, String winner, String nickList) {
        client.gameProgressFrame.restart_Btn.setText("재시작 준비");

        String[] card = cardList.split("&");
        ImageIcon card1Image = new ImageIcon("src/Image/" + card[0] + ".png");
        ImageIcon card2Image = new ImageIcon("src/Image/" + card[1] + ".png");
        ImageIcon card3Image = new ImageIcon("src/Image/" + card[2] + ".png");
        ImageIcon card4Image = new ImageIcon("src/Image/" + card[3] + ".png");
        client.gameProgressFrame.player1_card1.setIcon(card1Image);
        client.gameProgressFrame.player1_card2.setIcon(card2Image);
        client.gameProgressFrame.player2_card1.setIcon(card3Image);
        client.gameProgressFrame.player2_card2.setIcon(card4Image);

        String[] nick = nickList.split("&");
        client.gameProgressFrame.player1_Nick.setText(nick[0]);
        client.gameProgressFrame.player2_Nick.setText(nick[1]);

        String[] type = cardTypeList.split("&");
        client.gameProgressFrame.player1_cardType.setText(type[0]);
        client.gameProgressFrame.player2_cardType.setText(type[1]);

        if (!winner.equals(tag.sameTag))
            JOptionPane.showMessageDialog(null, winner + "가 승리했습니다!", "결과", JOptionPane.INFORMATION_MESSAGE);
        else
            JOptionPane.showMessageDialog(null, "동점입니다!", "결과", JOptionPane.INFORMATION_MESSAGE);

    }

    /**
     * 8. 로비 유저 목록 출력 메소드
     */
    void viewLobbyUser(String message) {
        if (!message.equals("")) {
            String[] user = message.split("&");
            client.lobbyFrame.user_List.setListData(user);
        }
    }

    /**
     * 9. 게임레디룸에서 로비로 돌아가는 메소드 = 게임레디룸 퇴장
     */
    void backLobby(String message) {
        if (message.equals(tag.successTag)) { // 게임룸 준비방 퇴장 성공
            System.out.println("[CLIENT] 게임룸 준비방 퇴장 성공!");
            client.gameReadyFrame.setVisible(false); // 게임룸 준비방 안보이게 함
            client.lobbyFrame.setVisible(true); // 로비 창 보이게함
            client.sendMsg(tag.ExitTag + "//");
        } else { // 게임룸 준비방 퇴장 실패
            System.out.println("[CLIENT] 게임룸 준비방 퇴장 실패");
            JOptionPane.showMessageDialog(null, "퇴장에 실패했습니다!", "퇴장 실패", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * 10. 로비 유저 채팅 목록 출력 메소드
     */
    void viewLobbyChat(String message) {
        if (!message.equals("")) {
            String[] chat = message.split("&");
            client.lobbyFrame.chat_List.setListData(chat);
        }
    }

    /**
     * 11 - 1. 개임룸 대기방 유저 채팅 목록 출력 메소드
     */
    void viewGameReadyChat(String message) {
        if (!message.equals("")) {
            String[] chat = message.split("&");
            client.gameReadyFrame.chat_List.setListData(chat);
        }
    }

    /**
     * 11 - 2. 게임 진행방 유저 채팅 목록 출력 메소드
     */
    void viewGameProChat(String message) {
        if (!message.equals("")) {
            String[] chat = message.split("&");
            client.gameProgressFrame.chat_List.setListData(chat);
        }
    }

    /**
     * 12. 게임룸 인원 입장 또는 퇴장으로 룸 정보 업데이트
     */
    void viewGameInfo(String message) {
        if (!message.equals("")) {
            String[] data = message.split("&");
            if (data[0].equals(tag.player1_Tag)) {
                client.gameReadyFrame.player1_Nickname.setText(data[1]);
                client.gameReadyFrame.player1_GameRecord.setText(data[2]);
            } else {
                client.gameReadyFrame.player2_Nickname.setText(data[1]);
                client.gameReadyFrame.player2_GameRecord.setText(data[2]);
            }
        }
    }

}


