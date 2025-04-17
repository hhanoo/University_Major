package com.example.subway_dijsktra_algorithm;

class station {
    int linesCount;
    int lineCount;
    int[] lines;
    int line;
    int code;
    String name;
    boolean transfer;

    public station() {
        this.linesCount = 0;
        this.lineCount = 0;
        this.lines = new int[5];
        this.line = 0;
        this.code = -1;
        this.name = "";
        this.transfer = false;
    }
}

public class algorithm {

    static class codeGraph {
        private int n; //노드의 수
        private final double[][] map; //노드간 간선의 가중치
        private station[] stationDatas;
        private final int[] shortestRoute; //역 경로를 저장하기 위한 배열

        public codeGraph(int n) {
            this.n = n;
            map = new double[n + 1][n + 1];
            shortestRoute = new int[n + 1];
        }

        //양방향
        public void codeInput(int i, int j, double w) {
            map[i][j] = w;
            map[j][i] = w;
        }

        //단방향
        public void codeInput_1way(int i, int j, double w) {
            map[i][j] = w;
        }

        // 역 개수 입력
        public void stationData(int stationN) {
            stationDatas = new station[stationN + 1];
            for (int i = 0; i < stationN + 1; i++) {
                stationDatas[i] = new station();
            }
        }

        //역 정보 입력
        public void stationDataInput(int lines, int line, int code, String name, boolean transfer) {
            if (stationDatas[code].lineCount != 0) {
                for (int j = 0; j < stationDatas[code].lineCount; j++) {
                    if (stationDatas[code].line == line)
                        break;
                    if (stationDatas[code].line != line && j == lines - 1)
                        stationDatas[code].line = line;
                }
            } else {
                stationDatas[code].line = line; //호선
                stationDatas[code].code = code; //역 코드(공식X)
                stationDatas[code].name = name; //역 이름
                stationDatas[code].transfer = transfer; //환승역 여부
            }
            stationDatas[code].lines[stationDatas[code].linesCount] = lines;

            stationDatas[code].lineCount++; //호선의 개수
            stationDatas[code].linesCount++; //철도 갈리는 케이스의 라인들의 개수
        }


        public information dijkstra(int type, int startLine, int destLine, String startStationName, String destStationName) {
            information data = new information();

            int startStationCode = 0; //출발역 코드
            int destStationCode = 0; //도착역 코드

            try {
                if (type == 0) {
                    //출발역 이름을 통해 출발역 코드 추출
                    for (int i = 0; i < n; i++) {
                        if (startStationName.equals(stationDatas[i].name)) {
                            startStationCode = stationDatas[i].code;
                            break;
                        }
                    }
                    //도착역 이름을 통해 도착역 코드 추출
                    for (int i = 0; i < n; i++) {
                        if (destStationName.equals(stationDatas[i].name)) {
                            destStationCode = stationDatas[i].code;
                            break;
                        }
                    }
                } else {
                    //출발역 이름을 통해 출발역 코드 추출
                    for (int i = 0; i < n; i++) {
                        if (startLine == stationDatas[i].line && startStationName.equals(stationDatas[i].name)) {
                            startStationCode = stationDatas[i].code;
                            break;
                        }
                    }
                    //도착역 이름을 통해 도착역 코드 추출
                    for (int i = 0; i < n; i++) {
                        if (destLine == stationDatas[i].line && destStationName.equals(stationDatas[i].name)) {
                            destStationCode = stationDatas[i].code;
                            break;
                        }
                    }
                }
                //역이 존재하지않아서 code 중 0이 있을 때
                if (startStationCode == 0 || destStationCode == 0) {
                    throw new Exception("존재하지않는 역입니다.");
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
            } finally {


                double[] distance = new double[n + 1]; //최단 가중치
                boolean[] check = new boolean[n + 1]; //역 방문 유무 체크

                //시작 노드에서부터 모든 노드의 거리 초기화
                for (int i = 1; i < n + 1; i++)
                    distance[i] = Integer.MAX_VALUE;


                distance[startStationCode] = 0; //출발역에서부터의 가중치를 0으로 초기화
                check[startStationCode] = true; //출발역의 방문 체크
                shortestRoute[startStationCode] = -1;

                //출발역에서부터 각 역의 걸리는 시간(가중치)을 갱신
                for (int i = 1; i < n + 1; i++) {
                    //해당 역을 지나지 않았고, 출발역과 인접하다면 가중치를 저장
                    if (!check[i] && map[startStationCode][i] != 0) {
                        distance[i] = map[startStationCode][i];
                        shortestRoute[i] = startStationCode;
                    }
                }

                for (int i = 0; i < n - 1; i++) {
                    //노드가 n개 있을 때 다익스트라를 위해서 반복수는 n-1번이면 된다.
                    int minTime = Integer.MAX_VALUE; //최소 가중치 임시변수 초기화
                    int min_station = 0; //최소 가중치를 가질 수 있는 역의 임시변수 초기화

                    //최소값 찾기
                    for (int j = 1; j < n + 1; j++) {
                        //방문하지 않았고, 연결된 역이 있을 경우 (MAX_Value가 있다는 뜻은 아직 직간접이든 연결이 안되어있다는 뜻)
                        if (!check[j] && distance[j] != Integer.MAX_VALUE) {
                            if (distance[j] < minTime) {
                                minTime = (int) distance[j];
                                min_station = j;
                            }
                        }
                    }
                    check[min_station] = true; //위 반복문을 통해서 가장 짧은 역에 방문 표시
                    for (int k = 1; k < n + 1; k++) {
                        //방문하지 않았고, min_station 최소 가중치로 정해진 역와 연결되어 있는 역
                        if (!check[k] && map[min_station][k] != 0) {
                            //현재 합산된 최소가중치 합이 다른 루트를 통해 합산된 가중치보다 크면 최소거리 값 교체
                            if (distance[k] > distance[min_station] + map[min_station][k]) {
                                distance[k] = distance[min_station] + map[min_station][k];
                                shortestRoute[k] = min_station;
                            }
                        }
                    }
                }
                //최소 시간
                if (type != 3) {
                    //출발역에서 도착역까지의 경로 출력
                    int[] route = new int[481]; // 지나가는 지하철역을 저장하기위한 배열
                    int passedCount = 0; //지나간 역 개수
                    int transferCount = 0;
                    int tempDest = destStationCode; //현재 도착한 역
                    StringBuilder leastPath = new StringBuilder();

                    while (passedCount != 150) {
                        if (shortestRoute[tempDest] == -1)
                            break;
                        route[passedCount] = shortestRoute[tempDest];
                        tempDest = shortestRoute[tempDest];
                        passedCount++;
                    }
                    double stop_time = 0.5;//역에서 정차하는 시간
                    int totalTime = (int) (distance[destStationCode] + (int) Math.floor(stop_time * (passedCount - 1)));
                    for (int i = passedCount - 1; i >= 0; i--) {
                        int k = route[i];
                        int j = 0;
                        if (i != passedCount - 1)
                            j = route[i + 1];
                        if (i < passedCount - 2 && stationDatas[j].name.equals(stationDatas[k].name)) {
                            passedCount--; // 이름이 같은 환승역은 하나 뺌
                            transferCount++; //환승 회수 추가
                            if (stationDatas[j].line == 101)
                                leastPath.append("[ " + "수인분당" + "  -->  " + stationDatas[k].line + " 환승 ] - ");
                            else if (stationDatas[k].line == 101)
                                leastPath.append("[ " + stationDatas[j].line + "  -->  " + "수인분당" + " 환승 ] - ");
                            else
                                leastPath.append("[ " + stationDatas[j].line + "  -->  " + stationDatas[k].line + " 환승 ] - ");
                        }
                        leastPath.append(stationDatas[k].name + " - ");
                    }
                    leastPath.append(destStationName);
                    data.Time = String.valueOf(totalTime);
                    data.PassedCount = String.valueOf(passedCount);
                    data.TransferCount = String.valueOf(transferCount);
                    data.Path = leastPath;
                }
                //최소 환승
                else if (type == 3) {
                    //출발역에서 도착역까지의 경로 출력
                    int[] route = new int[481]; // 지나가는 지하철역을 저장하기위한 배열
                    int passedCount = 0; //지나간 역 개수S
                    int transferCount = 0; //환승 회수
                    int tempDest = destStationCode; //현재 도착한 역
                    StringBuilder shortPath = new StringBuilder();

                    while (passedCount != 150) {
                        if (shortestRoute[tempDest] == -1)
                            break;
                        route[passedCount] = shortestRoute[tempDest];
                        tempDest = shortestRoute[tempDest];
                        passedCount++;
                    }
                    double stop_time = 0.5;//역에서 정차하는 시간
                    int totalTime = (int) ((distance[destStationCode] % 1000) + (int) Math.floor(stop_time * (passedCount - 1)));
                    for (int i = passedCount - 1; i >= 0; i--) {
                        int k = route[i];
                        int j = 0;
                        if (i != passedCount - 1)
                            j = route[i + 1];
                        if (i < passedCount - 2 && stationDatas[j].name.equals(stationDatas[k].name)) {
                            passedCount--; // 이름이 같은 환승역은 하나 뺌
                            transferCount++; //환승 회수 추가
                            if (stationDatas[j].line == 101)
                                shortPath.append("[ " + "수인분당" + "  -->  " + stationDatas[k].line + " 환승 ] - ");
                            else if (stationDatas[k].line == 101)
                                shortPath.append("[ " + stationDatas[j].line + "  -->  " + "수인분당" + " 환승 ] - ");
                            else
                                shortPath.append("[ " + stationDatas[j].line + "  -->  " + stationDatas[k].line + " 환승 ] - ");
                        }
                        shortPath.append(stationDatas[k].name + " - ");
                    }
                    shortPath.append(destStationName);
                    data.Time = String.valueOf(totalTime);
                    data.PassedCount = String.valueOf(passedCount);
                    data.TransferCount = String.valueOf(transferCount);
                    data.Path = shortPath;
                }
            }
            return data;
        }
    }
}
