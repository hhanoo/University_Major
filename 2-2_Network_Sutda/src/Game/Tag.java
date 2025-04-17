package Game;

public class Tag {
    public final String LoginTag = "LOGIN"; // 로그인
    public final String JoinTag = "JOIN"; // 회원가입
    public final String OverTag = "OVER"; // 중복확인
    public final String ExitTag = "EXIT"; // 프로그램 종료

    public final String roomCreateTag = "CREROOM"; // 게임룸 생성
    public final String roomEnterTag = "ENROOM"; // 게임룸 입장
    public final String roomListTag = "LISTROOM"; // 게임룸 목록

    public final String backLobbyTag = "RELOBBY"; // 로비로 돌아가기
    public final String compBackLobbySignTag = "COBASILOB"; // 강제성 로비 돌아가기
    public final String compBackLobbyTag = "COBALOB"; // 강제성 로비 돌아가기
    public final String lobbyUserTag = "USLOBBY"; // 로비 접속 유저

    public final String gameUpdateTag = "GAINFUP"; // 게임 내 유저 정보 업데이트
    public final String readyONTag = "READYO"; // 준비상태 ON
    public final String readyOFFTag = "READYX"; // 준비상태 OFF

    public final String startGame = "START"; // 게임 시작
    public final String sameTag = "SAME"; // 무승부

    public final String successTag = "SUCCESS"; // 성공 또는 승인
    public final String failTag = "FAIL"; // 실패 또는 거부
    public final String updateTag = "UPDATE"; // 업데이트

    public final String chatLobbyTag = "CHATLOB"; // 로비 채팅 목록
    public final String chatGameReadyTag = "CHATREADYGAME"; // 게임룸 대기방 채팅
    public final String chatGameProTag = "CHATPROGAME"; // 게임룸 진행방 채팅

    public final String player1_Tag = "player1"; // player1
    public final String player2_Tag = "player2"; // player1
    public final String reGame ="REGAME"; // 게임 재시작

    public void Tag() {
    }
}
