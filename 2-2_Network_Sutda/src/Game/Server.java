package Game;

import DB.Database;

import java.io.*;
import java.util.*;
import java.net.*;

/**
 * 클라이언트의 연결 요청, 입출력 관리
 */
public class Server {
    ServerSocket ss = null;

    /**
     * 객체를 Vector 통해 관리
     */
    Vector<connectUser> lobbyUser; // 모든 클라이언트
    Vector<connectUser> roomUser; // 개임 룸 안에 있는 클라이언트
    Vector<Room> room;  // 생성된 게임 룸
    Vector<String> lobbyChat; // 로비 채팅

    public static void main(String[] args) {
        Server server = new Server();
        server.lobbyUser = new Vector<>();
        server.roomUser = new Vector<>();
        server.room = new Vector<>();
        server.lobbyChat = new Vector<>();

        try {
            /** 서버 소켓 준비 */
            server.ss = new ServerSocket(9998);
            System.out.println("[Server] 서버 소켓 준비");

            /** 클라이언트 요청 상시 대기*/
            while (true) {
                Socket socket = server.ss.accept();
                connectUser user = new connectUser(socket, server);

                user.start(); //유저 thread 시작
            }
        } catch (SocketException e) {
            System.out.println("[SERVER] 서버 소켓 오류 >> " + e);
        } catch (IOException e) {
            System.out.println("[SERVER] 입출력 오류 >> " + e);
        }
    }
}

/**
 * 서버에 접속한 유저와 메세지 송수신 관리
 */
class connectUser extends Thread {
    Server server;
    Socket socket;

    /**
     * 객체를 Vector 통해 관리
     */
    Vector<connectUser> lobbyUser; // 모든 클라이언트
    Vector<connectUser> roomUser; // 개임 룸 안에 있는 클라이언트
    Vector<Room> room;  // 생성된 게임 룸
    Vector<String> lobbyChat; // 로비 채팅

    Database db = new Database();

    /**
     * 메시지 송수신을 위한 필드
     */
    OutputStream outputStream;
    DataOutputStream dataOutput;
    InputStream inputStream;
    DataInputStream dataInput;

    String message; //수신 메시지를 저장할 필드
    String nickname; //클라이언트의 닉네임을 저장할 필드
    String win; //클라이언트의 승리을 저장할 필드
    String lose; //클라이언트의 패배을 저장할 필드
    String playerNum = null; // 게임룸 유저 번호

    Room myRoom; //입장한 방 객체를 저장할 필드

    //메세지에 대한 상세 코드
    Tag tag = new Tag();


    connectUser(Socket socket_, Server server_) {
        this.socket = socket_;
        this.server = server_;

        lobbyUser = server.lobbyUser;
        roomUser = server.roomUser;
        room = server.room;
        lobbyChat = server.lobbyChat;
    }

    public void run() {
        try {
            System.out.println("[SERVER] 클라이언트 접속 >> " + this.socket.toString());
            outputStream = this.socket.getOutputStream();
            inputStream = this.socket.getInputStream();
            dataOutput = new DataOutputStream(outputStream);
            dataInput = new DataInputStream(inputStream);


            while (true) {
                message = dataInput.readUTF(); // 메시지 상시 수신
                String[] msgData = message.split("//");

                /** 1. 로그인 */
                if (msgData[0].equals(tag.LoginTag)) {
                    String mm = db.loginCheck(msgData[1], msgData[2]);

                    if (!mm.equals("null")) { //로그인 성공
                        nickname = mm; //로그인한 사용자의 닉네임을 필드에 저장

                        String info = db.InfoData(nickname);// 연결되자마자 전적 조회
                        if (!info.equals("null")) {
                            String[] data = info.split("//");
                            win = data[0];
                            lose = data[1];
                        }

                        lobbyUser.add(this); //모든 접속 인원에 추가
                        roomUser.add(this); //대기실 접속 인원에 추가

                        dataOutput.writeUTF(tag.LoginTag + "//SUCCESS&");

                        sendLobbyUser(lobbyInUserData()); //대기실 접속 유저에 모든 접속 인원을 전송

                        if (room.size() > 0) { //생성된 방의 개수가 0 이상일 때
                            sendRoomUser(roomListData()); //대기실 접속 인원에 방 목록을 전송
                        }

                    } else { //로그인 실패
                        dataOutput.writeUTF(tag.LoginTag + "//FAIL");
                    }
                }

                /** 2. 회원가입 */
                else if (msgData[0].equals(tag.JoinTag)) {
                    if (db.joinCheck(msgData[1], msgData[2], msgData[3], msgData[4], msgData[5], msgData[6])) { // 회원가입 성공
                        dataOutput.writeUTF(tag.JoinTag + "//SUCCESS");
                    } else { // 회원가입 실패
                        dataOutput.writeUTF(tag.JoinTag + "//FAIL");
                    }
                }

                /** 3. 중복확인 */
                else if (msgData[0].equals(tag.OverTag)) {
                    if (!db.overCheck(msgData[1], msgData[2])) {    // 사용 가능
                        dataOutput.writeUTF(tag.OverTag + "//SUCCESS");
                    } else {    // 사용 불가능
                        dataOutput.writeUTF(tag.OverTag + "//FAIL");
                    }
                }

                /** 4. 게임 룸 생성 */
                else if (msgData[0].equals(tag.roomCreateTag)) {
                    myRoom = new Room(); // 게임룸 생성
                    myRoom.name = msgData[1]; // 게임룸 이름 설정
                    myRoom.userCount++; // 게임룸 인원 수 추가

                    room.add(myRoom); // 생성된 myRoom을 room 목록에 추가

                    // 게임룸 생성하자마자 전적 조회
                    String info = db.InfoData(nickname);
                    if (!info.equals("null")) {
                        String[] data = info.split("//");
                        win = data[0];
                        lose = data[1];
                    }

                    // 생성자 = player1 의 전적 저장
                    myRoom.p1_nick = nickname;
                    myRoom.p1_win = win;
                    myRoom.p1_lose = lose;
                    playerNum = tag.player1_Tag;

                    myRoom.userList.add(this); // 해당 인원 myRoom의 roomUser 배열에 추가
                    lobbyUser.remove(this); // 해당 인원 lobbyUser 배열에서 삭제

                    dataOutput.writeUTF(tag.roomCreateTag + "//SUCCESS//" + nickname + "&" + win + " / " + lose + "&");
                    System.out.println("[SERVER] " + nickname + "GameRoom 생성 >> " + msgData[1]);

                    sendLobbyUser(roomListData()); // 모든 유저에게 최신 방 목록 전송
                    sendRoomUser(myInfoData()); // 게임룸 유저에게 게임룸 정보 업데이트 전송


                }

                /** 5. 게임 룸 입장 */
                else if (msgData[0].equals(tag.roomEnterTag)) {
                    for (int i = 0; i < room.size(); i++) { // 현재 게임룸의 모든 목록
                        Room room_ = room.get(i);
                        String tempRoomName = "[ " + room_.name + " 방 ]";
                        if (tempRoomName.equals(msgData[1])) { // 들어가고자 하는 게임룸과 일치할 때
                            if (room_.userCount < 2) { // 게임룸의 인원수가 2몀보다 적을 때
                                myRoom = room_; // 들어가는 게임룸을 클라이언트의 myRoom으로 선언
                                myRoom.userCount++; // 유저가 게임룸에 들어갔으니 인원 수 증가

                                // 게임룸 입장하자마자 전적 조회
                                String info = db.InfoData(nickname);
                                if (!info.equals("null")) {
                                    String[] data = info.split("//");
                                    win = data[0];
                                    lose = data[1];
                                }

                                if (myRoom.p1_nick != null) { // 안에 플레이어1 있을 때
                                    myRoom.p2_nick = nickname;
                                    myRoom.p2_win = win;
                                    myRoom.p2_lose = lose;
                                    playerNum = tag.player2_Tag;
                                } else { // 안에 플레이어2 있을때
                                    myRoom.p1_nick = nickname;
                                    myRoom.p1_win = win;
                                    myRoom.p1_lose = win;
                                    playerNum = tag.player1_Tag;
                                }

                                lobbyUser.remove(this); // 로비 인원에서 해당 유저 제거
                                myRoom.userList.add(this); // 게임 룸 인원에 해당 유저 추가

                                sendLobbyUser(roomListData()); // 모든 유저에게 최신 방 목록 전송
                                sendRoomUser(myInfoData()); // 게임룸 유저에게 게임룸 정보 업데이트 전송

                                dataOutput.writeUTF(tag.roomEnterTag + "//" + tag.successTag + "//" + playerNum + "&" + nickname + "&" + win + " / " + lose + "&");
                                System.out.println("[SERVER] " + nickname + " GameRoom 입장");
                            } else { // 게임룸의 인원수가 2명 이상일 때
                                dataOutput.writeUTF(tag.roomEnterTag + "//FAIL");
                                System.out.println("[SERVER] 인원 초과로 인해 입장 불가능");
                            }
                        } else { // 들어가고자 하는 게임룸에 일치하는 게임룸이 존재하지 않을 때
                            dataOutput.writeUTF(tag.roomEnterTag + "//FAIL");
                            System.out.println("[SERVER] " + nickname + " GameRoom 입장 에러");
                        }
                    }
                }

                /** 6 - 1. 게임 룸 퇴장 */
                else if (msgData[0].equals(tag.backLobbyTag)) {
                    myRoom.userList.remove(this); // 입장해 있던 게임룸의 유저 목록에서 삭제
                    myRoom.userCount--; // 입장해 있던 게임룸의 유저 수 감소
                    lobbyUser.add(this); // 게임룸에서 나온 유저를 로비유저 목록에 추가

                    System.out.println("[SERVER] " + nickname + " " + myRoom.name + "퇴장");

                    if (myRoom.userCount == 0) { // 게임룸안에 인원에 아무도 없다면 게임룸 삭제
                        room.remove(myRoom); // room 목록에서 삭제
                        myRoom.chatReadyList.clear();
                        myRoom.chatProList.clear();
                    } else { // 게임룸 안에 인원이 있다면 최신화된 게임룸 인원 목록 전송
                        if (playerNum.equals(tag.player1_Tag)) {
                            myRoom.p1_nick = null;
                            myRoom.p1_win = null;
                            myRoom.p1_lose = null;
                        } else {
                            myRoom.p2_nick = null;
                            myRoom.p2_win = null;
                            myRoom.p2_lose = null;
                        }
                        String msg = tag.gameUpdateTag + "//" + playerNum + "&Player&Win / Lose&";
                        sendRoomUser(msg); // 룸 유저에게 퇴장 알림
                        playerNum = null;
                    }

                    sendLobbyUser(roomListData()); // 모든 유저들에게 최신 게임룸 목록 전송
                    sendLobbyUser(lobbyInUserData()); // 로비 유저들에게 최신 유저 목록 전송
                }
                /** 6 - 2. 강제 게임 룸 퇴장 요청 */
                else if (msgData[0].equals(tag.compBackLobbySignTag)) {
                    myRoom.chatReadyList.clear();
                    myRoom.chatProList.clear();
                    sendRoomUser(tag.compBackLobbySignTag + "//");
                }
                /** 6 - 2. 강제 게임 룸 퇴장 진행*/
                else if (msgData[0].equals(tag.compBackLobbyTag)) {
                    myRoom.userList.remove(this); // 입장해 있던 게임룸의 유저 목록에서 삭제
                    lobbyUser.add(this); // 게임룸에서 나온 유저를 로비유저 목록에 추가
                    room.remove(myRoom); // room 목록에서 삭제
                    sendLobbyUser(roomListData()); // 모든 유저들에게 최신 게임룸 목록 전송
                    sendLobbyUser(lobbyInUserData()); // 로비 유저들에게 최신 유저 목록 전송
                }

                /** 7. 게임 */
                /** 7 - 1. 게임 준비 ON */
                else if (msgData[0].equals(tag.readyONTag)) {
                    myRoom.ready++;
                    System.out.println("[SERVER] 준비 카운트 >> " + myRoom.ready);
                    // 유저 2명 다 준비 완료했다면 게임 시작 & 기본 세팅
                    if (myRoom.ready == 2) {
                        myRoom.ready = 0;
                        System.out.println("[SERVER] 준비 카운트 >> " + myRoom.ready);
                        sendRoomUser(gameChatProData());
                        do {
                            myRoom.RandomCard(); // 게임이 시작했으므로 카드 뽑기
                            myRoom.player1_CardType = myRoom.cardGenealogy(myRoom.userCard[0], myRoom.userCard[1]); // 플레이어 1의 카드 타입 구하기
                            myRoom.player2_CardType = myRoom.cardGenealogy(myRoom.userCard[2], myRoom.userCard[3]); // 플레이어 2의 카드 타입 구하기
                        } while (myRoom.player1_CardType == Room.CardType.None || myRoom.player2_CardType == Room.CardType.None);

                        String nickNameList = myRoom.p1_nick + "&" + myRoom.p2_nick;
                        String cardList = myRoom.userCard[0] + "&" + myRoom.userCard[1] + "&" + myRoom.userCard[2] + "&" + myRoom.userCard[3]; // cardList 에 카드 1, 2, 3, 4 저장
                        String typeList = myRoom.player1_CardType.getName() + "&" + myRoom.player2_CardType.getName();
                        String winPlayerNum = myRoom.winner(myRoom.player1_CardType, myRoom.player2_CardType);
                        String winPlayerNick = null;

                        if (winPlayerNum.equals(tag.player1_Tag))
                            winPlayerNick = myRoom.p1_nick;
                        else {
                            winPlayerNick = myRoom.p2_nick;
                        }

                        // 보내는 메세지 >> 게임시작코드//카드4개리스트//플레이어별 카드타입2개//승리자 닉네임//전체닉네임
                        String startMSG = tag.startGame + "//" + cardList + "//" + typeList + "//" + winPlayerNick + "//" + nickNameList;
                        System.out.println("SERVER MSG >> " + startMSG);
                        sendRoomUser(startMSG);

                        // 승리 플레이어 업데이트
                        if (myRoom.p1_nick.equals(winPlayerNick)) {
                            db.winRecord(myRoom.p1_nick);
                            db.loseRecord(myRoom.p2_nick);
                        } else if (myRoom.p2_nick.equals(winPlayerNick)) {
                            db.winRecord(myRoom.p2_nick);
                            db.loseRecord(myRoom.p1_nick);
                        }
                    }
                }
                /** 7 - 2. 게임 준비 OFF */
                else if (msgData[0].equals(tag.readyOFFTag)) {
                    myRoom.ready--;
                }
                /** 7 - 3. 게임 재시작 */
                else if (msgData[0].equals(tag.reGame)) {
                    myRoom.ready++;
                    System.out.println("[SERVER] 준비 카운트 >> " + myRoom.ready);
                    // 유저 2명 다 준비 완료했다면 게임 시작 & 기본 세팅
                    if (myRoom.ready == 2) {
                        myRoom.ready = 0;
                        sendRoomUser(gameChatProData());
                        do {
                            myRoom.RandomCard(); // 게임이 시작했으므로 카드 뽑기
                            myRoom.player1_CardType = myRoom.cardGenealogy(myRoom.userCard[0], myRoom.userCard[1]); // 플레이어 1의 카드 타입 구하기
                            myRoom.player2_CardType = myRoom.cardGenealogy(myRoom.userCard[2], myRoom.userCard[3]); // 플레이어 2의 카드 타입 구하기
                        } while (myRoom.player1_CardType == Room.CardType.None || myRoom.player2_CardType == Room.CardType.None);

                        String nickNameList = myRoom.p1_nick + "&" + myRoom.p2_nick;
                        String cardList = myRoom.userCard[0] + "&" + myRoom.userCard[1] + "&" + myRoom.userCard[2] + "&" + myRoom.userCard[3]; // cardList 에 카드 1, 2, 3, 4 저장
                        String typeList = myRoom.player1_CardType.getName() + "&" + myRoom.player2_CardType.getName();
                        String winPlayerNum = myRoom.winner(myRoom.player1_CardType, myRoom.player2_CardType);
                        String winPlayerNick = null;

                        if (winPlayerNum.equals(tag.player1_Tag))
                            winPlayerNick = myRoom.p1_nick;
                        else if (winPlayerNum.equals(tag.player2_Tag)) {
                            winPlayerNick = myRoom.p2_nick;
                        } else {
                            winPlayerNick = tag.sameTag;
                        }

                        // 보내는 메세지 >> 게임시작코드//카드4개리스트//플레이어별 카드타입2개//승리자 닉네임//전체닉네임
                        String startMSG = tag.reGame + "//" + cardList + "//" + typeList + "//" + winPlayerNick + "//" + nickNameList;
                        System.out.println("SERVER MSG >> " + startMSG);
                        sendRoomUser(startMSG);

                        // 승리 플레이어 업데이트
                        if (myRoom.p1_nick.equals(winPlayerNick)) {
                            db.winRecord(myRoom.p1_nick);
                            db.loseRecord(myRoom.p2_nick);
                        } else if (myRoom.p2_nick.equals(winPlayerNick)) {
                            db.winRecord(myRoom.p2_nick);
                            db.loseRecord(myRoom.p1_nick);
                        }
                    }
                }

                /** 8. 로비 채팅 */
                else if (msgData[0].equals(tag.chatLobbyTag)) {
                    String temp = nickname + " : " + msgData[1];
                    lobbyChat.add(temp);
                    sendLobbyUser(lobbyChatData()); // 로비 유저에게 최신 채팅 목록 전송
                }

                /** 9 - 1. 게임룸 대기방 채팅 */
                else if (msgData[0].equals(tag.chatGameReadyTag)) {
                    String temp = nickname + " : " + msgData[1];
                    myRoom.chatReadyList.add(temp);
                    sendRoomUser(gameChatReadyData());
                }
                /** 9 - 2. 게임룸 진행방 채팅 */
                else if (msgData[0].equals(tag.chatGameProTag)) {
                    String temp = nickname + " : " + msgData[1];
                    myRoom.chatProList.add(temp);
                    sendRoomUser(gameChatProData());
                }
                /** 10. 프로그램 종료 */
                else if (msgData[0].equals(tag.ExitTag)) {
                    lobbyUser.remove(this);
                    sendLobbyUser(lobbyInUserData());
                }
            }

        } catch (IOException e) {
            System.out.println("[SERVER] 입출력 오류 >> " + e);
        }
    }

    /**
     * 게임룸 리스트 출력
     */
    String roomListData() {
        String msg = tag.roomListTag + "//";
        for (int i = 0; i < room.size(); i++) {
            msg = msg + "[ " + room.get(i).name + " 방 ] : " + room.get(i).userCount + " 명&";
        }
        return msg;
    }

    /**
     * 게임룸의 다른 유저에게 플레이어번호, 나의 닉네임 전적 전송
     */
    String myInfoData() {
        String msg = tag.gameUpdateTag + "//" + playerNum + "&" + nickname + "&" + win + " / " + lose + "&";
        return msg;
    }

    /**
     * 로비 인원 목록 출력
     */
    String lobbyInUserData() {
        String msg = tag.lobbyUserTag + "//";
        for (int i = 0; i < lobbyUser.size(); i++) {
            msg = msg + lobbyUser.get(i).nickname + "&";
        }
        return msg;
    }

    /**
     * 로비 채팅 목록 출력
     */
    String lobbyChatData() {
        String msg = tag.chatLobbyTag + "//";
        for (int i = 0; i < lobbyChat.size(); i++) {
            msg = msg + lobbyChat.get(i) + "&";
        }
        return msg;
    }

    /**
     * 게임룸 채팅 목록 출력
     */
    String gameChatReadyData() {
        String msg = tag.chatGameReadyTag + "//";
        for (int i = 0; i < myRoom.chatReadyList.size(); i++) {
            msg = msg + myRoom.chatReadyList.get(i) + "&";
        }
        return msg;
    }

    /**
     * 게임룸 진행 채팅 목록 출력
     */
    String gameChatProData() {
        String msg = tag.chatGameProTag + "//";
        for (int i = 0; i < myRoom.chatProList.size(); i++) {
            msg = msg + myRoom.chatProList.get(i) + "&";
        }
        return msg;
    }

    /**
     * 로비 유저들에게 일괄 메시지 전송
     */
    void sendLobbyUser(String msg) {
        for (int i = 0; i < lobbyUser.size(); i++) {
            try {
                lobbyUser.get(i).dataOutput.writeUTF(msg);
            } catch (IOException e) {
                lobbyUser.remove(i);
            }
        }
    }

    /**
     * 게임룸 안에 있는 모든 유저들에게 일괄 메시지 전송
     */
    void sendRoomUser(String msg) {
        for (int i = 0; i < roomUser.size(); i++) {
            try {
                roomUser.get(i).dataOutput.writeUTF(msg);
            } catch (IOException e) {
                roomUser.remove(i);
            }
        }
    }
}