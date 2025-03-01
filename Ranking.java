
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;


public class Ranking extends JFrame implements ActionListener {

    private final int X_RANK = 150;
    private final int X_NAME = 250;
    private final int X_SCORE = 350;
    private final int X_SCORE_R = 450;
    private final int Y = 75;
    private final int WIDTH_LABEL = 100;
    private final int HEIGHT_LABEL = 50;


    private JButton bt_StartGame, bt_Quit;
    private JLabel labelRank, labelName, labelScore, labelScoreR;
    
    private Image imageBackGround;

    private ArrayList<String> ls = new ArrayList<>(); //수정
    
    private static final int MAX_RANK_DISPLAY = 5; //수정
    
    private String playerName; 
    
  
    

    Ranking() {
        setTitle("Eat Coin - Ranking");
        setUndecorated(true);
        setResizable(false);
        setLayout(null);
        setBounds(100, 100, GameFrame.FRAME_WIDTH, GameFrame.FRAME_HEIGHT);

        
        bt_StartGame = new JButton("한판더!!");
        bt_StartGame.setFont(new Font(null, 0, 20));
        bt_StartGame.setBounds(0, 500, 100, 100);
        bt_StartGame.addActionListener(this);

        bt_Quit = new JButton("게임종료");
        bt_Quit.setFont(new Font(null, 0, 20));
        bt_Quit.setBounds(500, 500, 100, 100);
        bt_Quit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) { // 추가 
                System.exit(0); // 추가
            }
        });
        
        
        
        printRanking();
        add(bt_StartGame);
        add(bt_Quit);
        setVisible(true);
    }

    private void printRanking() {
        printRankingTitle();
        printActualRanking();
    }

    private void printRankingTitle() {
        labelRank = new JLabel("순위");
        labelRank.setBounds(X_RANK, Y, WIDTH_LABEL, HEIGHT_LABEL);
        add(labelRank);

        labelName = new JLabel("이름");
        labelName.setBounds(X_NAME, Y, WIDTH_LABEL, HEIGHT_LABEL);
        add(labelName);

        labelScore = new JLabel("점수");
        labelScore.setBounds(X_SCORE, Y, WIDTH_LABEL, HEIGHT_LABEL);
        add(labelScore);

        labelScoreR = new JLabel("Repaint()");
        labelScoreR.setBounds(X_SCORE_R, Y, WIDTH_LABEL, HEIGHT_LABEL);
        add(labelScoreR);

    }

    private void printActualRanking() {
        try (
                FileReader fr = new FileReader("test.txt");
                BufferedReader br = new BufferedReader(fr);
        ) {
            String readLine = null;
            while ((readLine = br.readLine()) != null) { 
                ls.add(readLine);
            }
        } catch (IOException e) {
        }


        ArrayList<Integer> lsScore = new ArrayList<Integer>();
        for (int i = 1; i <= ls.size() / 3; i++) {
            lsScore.add(Integer.valueOf((String) ls.get(3 * i - 2)));
        }
        Collections.sort(lsScore); //정렬
        

        int rank = 0; 
        for (int i = lsScore.size(); i >= 1; i--) { ////수정
        	int x = ls.indexOf(String.valueOf(lsScore.get(i - 1))); //수정
            rank++;
            if (rank <= MAX_RANK_DISPLAY) { // 일정 순위 이하만 표시 (수정)
                callAllGen(x, rank);
            } else { //수정
                break; // 표시할 순위를 초과하면 반복문 종료 (수정)
            }
        }

    }



    private void callAllGen(int x, int rank) { // x는 스코어가 높은 점수대로 넣어줘야함
        // y는 x가 몇개들어가는지에따라서 1~n까지 for문으로 넣어줌 -> callAllGen메소드출력갯수만큼 똑같겠다
        genName(x - 1, rank);
        genScore(x, rank);
        genScoreR(x + 1, rank);
        genRank(String.valueOf(rank), rank);
    }

    private void genRank(String number, int rank) {
        labelRank = new JLabel(number);
        labelRank.setBounds(X_RANK, Y + 25 * rank, WIDTH_LABEL, HEIGHT_LABEL);
        add(labelRank);
    }


    private void genName(int index, int rank) {
        labelName = new JLabel((String) ls.get(index));
        labelName.setBounds(X_NAME, Y + 25 * rank, WIDTH_LABEL, HEIGHT_LABEL);
        add(labelName);
    }

    private void genScore(int index, int rank) {
        labelScore = new JLabel((String) ls.get(index));
        labelScore.setBounds(X_SCORE, Y + 25 * rank, WIDTH_LABEL, HEIGHT_LABEL);
        add(labelScore);
    }

    private void genScoreR(int index, int rank) {
        labelScoreR = new JLabel((String) ls.get(index));
        labelScoreR.setBounds(X_SCORE_R, Y + 25 * rank, WIDTH_LABEL, HEIGHT_LABEL);
        add(labelScoreR);
    }

    private void reset() { 
        this.setVisible(false);
        this.dispose(); //해당프레임만종료
//        new GameFrame(); //새게임
        startGame(); // 추가 (수정) 
    }
    private void startGame() { // 추가
        GameFrame gameFrame = new GameFrame(); // 추가
        gameFrame.setVisible(true); // 추가
    } 


@Override
   public void actionPerformed(ActionEvent e) {
   reset();
   }
}


    
   