package Game;

import java.util.*;

// multi room 객체로 관리
public class Room {
    Tag tag = new Tag();
    Vector<connectUser> userList; // 룸의 인원 객체
    Vector<String> chatReadyList = new Vector<>(); // 레디 룸의 채팅
    Vector<String> chatProList = new Vector<>(); // 진행 룸의 채팅
    String name; // 룸의 이름
    String p1_nick = null;
    String p1_win = null;
    String p1_lose = null;
    String p2_nick = null;
    String p2_win = null;
    String p2_lose = null;
    String winner = null;
    int userCount = 0; // 룸의 인원 수
    int ready = 0; // 준비 완료 수

    int[] userCard = new int[4];
    CardType player1_CardType = null;
    CardType player2_CardType = null;

    Room() {
        userList = new Vector<>();
    }

    public enum CardType {
        ThreeEightGwangDdang("삼팔광땡", 38000, "삼팔광땡"),
        OneEightGwangDdang("일팔광땡", 18000, "광땡"),
        OneThreeGwangDdang("일삼광땡", 13000, "광땡"),
        JangDdang("장땡", 10100, "땡"),
        NineDdang("구땡", 9900, "땡"),
        EightDdang("팔땡", 8800, "땡"),
        SevenDdang("칠땡", 7700, "땡"),
        SixDdang("육땡", 6600, "땡"),
        FiveDdang("오땡", 5500, "땡"),
        FourDdang("사땡", 4400, "땡"),
        ThreeDdang("삼땡", 3300, "땡"),
        TwoDdang("이땡", 2200, "땡"),
        OneDdang("일땡", 1100, "땡"),
        Arli("알리", 1021, "알리"),
        DockSa("독사", 1014, "독사"),
        GuBbing("구삥", 901, "구삥"),
        Jangbbing("장삥", 110, "장삥"),
        JangSa("장사", 104, "장사"),
        SeRyuk("세륙", 64, "세륙"),
        GabO("갑오", 9, "끗"),
        EightGguk("여덟끗", 8, "끗"),
        SevenGguk("일곱끗", 7, "끗"),
        SixDGguk("여섯끗", 6, "끗"),
        FiveGguk("다섯끗", 5, "끗"),
        FourGguk("네끗", 4, "끗"),
        ThreeGguk("세끗", 3, "끗"),
        TwoGguk("두끗", 2, "끗"),
        OneGguk("한끗", 1, "끗"),
        MangTong("망통", 0, "끗"),
        SecretAgent("암행어사", 1, "특수"),
        DdangHunter("땡잡이", 0, "특수"),
        None("오류", -1, "시스템");

        private final String name;
        private final String classification;
        private final int rankPoint;

        private CardType(String name, int rankPoint, String classification) {
            this.name = name;
            this.rankPoint = rankPoint;
            this.classification = classification;
        }

        public String getName() {
            return name;
        }
    }

    private boolean exists(int n[], int index) {
        for (int i = 0; i < index; i++) {
            if (n[i] == index)
                return true;
        }
        return false;
    }

    // 화투패 뽑기
    public void RandomCard() {
        Random rand = new Random();
        for (int i = 0; i < 4; i++) {
            do {
                userCard[i] = ((rand.nextInt(11) + 1) * 10) + rand.nextInt(4);
                // 백, 십의 자리수는 화투의 월, 일의 자리수는 화투의 세부 종류 (4가지)
            } while (exists(userCard, i));
            // 새로 넣은 숫자(카드)가 이미 존재하는 숫자(카드와)와 같은지 확인
            // true: 같은 패가 존재, false: 같은 패가 존재하지 않음
        }
    }


    // 광이 존재 유무 확인 함수
    private boolean cardTypeGwangExists(int card) {
        int[] Gwangs = {13, 33, 83};
        for (int i = 0; i < 3; i++) {
            if (Gwangs[i] == card)
                return true;
        }
        return false;
    }

    // 카드에 따른 족보 확인 함수
    public CardType cardGenealogy(int card1, int card2) {
        // 항상 card1 < card2 이도록 확인
        if (card1 > card2) {
            int temp = card1;
            card1 = card2;
            card2 = card1;
        }
        if (cardTypeGwangExists(card1) && cardTypeGwangExists(card2)) {   //card1, card2가 광인지 확인
            // 38광땡
            if (card1 == 33 && card2 == 83) {
                return CardType.ThreeEightGwangDdang;
            }
            // 18광땡
            else if (card1 == 13 && card2 == 83) {
                return CardType.OneEightGwangDdang;
            }
            //13광땡
            else if (card1 == 13 && card2 == 33) {
                return CardType.OneThreeGwangDdang;
            }
        } else {
            //4월끗, 7월끗 = 암행어사
            if (card1 == 42 && card2 == 72) {
                return CardType.SecretAgent;
            }
            //3월광, 7월끗 = 땡잡이
            else if (card1 == 33 && card2 == 72) {
                return CardType.DdangHunter;
            }

            //광땡, 암행어사, 땡잡이 이후 화투 패의 월만 비교하기 카드를 월로 만듬
            card1 = card1 / 10;
            card2 = card2 / 10;

            // 중간족보
            if (card1 == 1) {
                switch (card2) {
                    case 2: // 알리 = 1월, 2월
                        return CardType.Arli;
                    case 4: // 독사 = 1월, 4월
                        return CardType.DockSa;
                    case 9: // 구삥 = 1월, 9월
                        return CardType.GuBbing;
                    case 10:// 장삥 = 1월, 10월
                        return CardType.Jangbbing;
                    default:
                        break;
                }
            } else if (card1 == 4) {
                switch (card2) {
                    case 10:// 장사 = 4월, 10월
                        return CardType.JangSa;
                    case 6: // 세륙 = 4월, 6월
                        return CardType.SeRyuk;
                    default:
                        break;
                }
            }

            // 땡
            if (card1 == card2) {
                switch (card1) {
                    case 10:// 장땡
                        return CardType.JangDdang;
                    case 9: // 9땡
                        return CardType.NineDdang;
                    case 8: // 8땡
                        return CardType.EightDdang;
                    case 7: // 7땡
                        return CardType.SevenDdang;
                    case 6: // 6땡
                        return CardType.SixDdang;
                    case 5: // 5땡
                        return CardType.FiveDdang;
                    case 4: // 4땡
                        return CardType.FourDdang;
                    case 3: // 3땡
                        return CardType.ThreeDdang;
                    case 2: // 2땡
                        return CardType.TwoDdang;
                    case 1: // 1땡
                        return CardType.OneDdang;
                    default:
                        break;
                }
            }
            // 끗
            else {
                switch ((card1 + card2) % 10) {
                    case 9: // 갑오
                        return CardType.GabO;
                    case 8: // 8끗
                        return CardType.EightGguk;
                    case 7: // 7끗
                        return CardType.SevenGguk;
                    case 6: // 6끗
                        return CardType.SixDGguk;
                    case 5: // 5끗
                        return CardType.FiveGguk;
                    case 4: // 4끗
                        return CardType.FourGguk;
                    case 3: // 3끗
                        return CardType.ThreeGguk;
                    case 2: // 2끗
                        return CardType.TwoGguk;
                    case 1: // 1끗
                        return CardType.OneGguk;
                    case 0: // 망통
                        return CardType.MangTong;
                    default:
                        break;
                }
            }
        }
        return CardType.None;
    }


    public String winner(CardType user1, CardType user2) {
        // return 값에 따라 (-1: error, 1: User1의 승리, 2: User2의 승리)

        // 카드 둘 중 하나라도 None 일 경우 error (-1) return
        if (user1 == CardType.None || user2 == CardType.None)
            return tag.sameTag;

        // 카드 중 하나가 특수카드(암행어사, 땡잡이)일 경우
        // User1의 카드가 특수카드 일때
        if (user1.classification.equals("특수")) {
            if (user1 == CardType.DdangHunter && user2.classification.equals("땡")) {
                return tag.player1_Tag;
            } else if (user1 == CardType.SecretAgent && user2.classification.equals("광땡")) {
                return tag.player1_Tag;
            }
        }// User1의 카드가 특수카드 일때
        else if (user2.classification.equals("특수")) {
            if (user2 == CardType.DdangHunter && user1.classification.equals("땡")) {
                return tag.player2_Tag;
            } else if (user2 == CardType.SecretAgent && user1.classification.equals("광땡")) {
                return tag.player2_Tag;
            }
        }

        // 특수카드가 없거나, 특수카드가 존재하여도 특수카드가 이길 수 있는 카드가 존재하지 않을때
        // 각 카드의 rankPoint 비교
        if (user1.rankPoint > user2.rankPoint) {  //User1이 이길 때
            return tag.player1_Tag;
        } else if (user1.rankPoint < user2.rankPoint) { //User2가 이길때
            return tag.player2_Tag;
        } else {    //무승부 일 때
            return tag.sameTag;
        }

    }
}
