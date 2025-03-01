import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;  
import javax.sound.sampled.Clip; 
import javax.sound.sampled.LineUnavailableException; 
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File; 
import java.io.IOException; 
import java.util.ArrayList; 
import java.util.Random; 
public class GameFrame extends JFrame implements KeyListener, Runnable {
private boolean isRunning = true;
private boolean isKeyUp, isKeyDown, isKeyLeft, isKeyRight = false;
private JButton bt_StartGame, bt_Quit; 
private boolean gameOver; 
private int playerLife; 
private Clip clip;

static final int FRAME_WIDTH = 600;
static final int FRAME_HEIGHT = 600;
private int xPlayer, yPlayer, xCoin, yCoin;
private int score = 0;
private int count = 0;
Image imageBackGround = new ImageIcon("src/images/92E3BCCF-E471-4BE0-AAEE-0E7A184BFF55_1_201_a.jpeg").getImage(); 
Image imagePlayer = new ImageIcon("src/images/player2.png").getImage();
Image imageCoin = new ImageIcon("src/images/coin.png").getImage();
int widthPlayer = imagePlayer.getWidth(null);
int heightPlayer = imagePlayer.getHeight(null);
int widthCoin = imageCoin.getWidth(null);
int heightCoin = imageCoin.getHeight(null);
Image bufferImage;
Graphics gp;
Thread t;
Random rd = new Random();

Insets insets = getInsets();
int TOP = insets.top;
int BOTTOM = FRAME_HEIGHT - insets.bottom;
int LEFT = insets.left;
int RIGHT = FRAME_WIDTH - insets.right;

ArrayList balls = new ArrayList();
public GameFrame() {
addKeyListener(this);
t = new Thread(this);
t.start();
setTitle("동전을 모아보자 !");
setSize(600, 600);
setResizable(false);
setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
Dimension frameSize = this.getSize();
Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
this.setLocation((screenSize.width - frameSize.width) / 2, (screenSize.height - frameSize.height) / 2);
this.setResizable(false); 
playerLife = 3; 
setVisible(true);
score = 0; // 게임이 1점에서 부터 시작했기에 
}
public void run() {
new BallGenerator().start();
while (isRunning) {
try {
Thread.sleep(20);
} catch (Exception e) {
}
takeBallsOutFromArray();
movePlayer(); 
checkPlayerNWallBumped();
checkPlayerNCoinBumped();
checkPlayerNBallBumped();
repaint(); 
count++;
}
}
public void update(Graphics g) {
g.drawImage(bufferImage, 0, 0, this);
}
public void paint(Graphics g) {

bufferImage = createImage(FRAME_WIDTH, FRAME_HEIGHT);
gp = bufferImage.getGraphics();
gp.drawImage(imageBackGround, 0, 0, FRAME_WIDTH, FRAME_HEIGHT, this); 
gp.drawImage(imagePlayer, xPlayer, yPlayer, this);
gp.drawImage(imageCoin, xCoin, yCoin, this);
gp.setColor(Color.WHITE); 
gp.drawString(("repaint() 카운트: " + Integer.toString(count)), 50, 50);
gp.drawString(("SCORE: " + Integer.toString(score)), 300, 50);
gp.drawString(("Heart: " + Integer.toString(playerLife)), 500, 50);
gp.setColor(Color.red);
int size = balls.size();
for (int i = 0; i < size; i++) {
Ball b = (Ball) balls.get(i); 
gp.fillOval(b.x, b.y, b.SIZE, b.SIZE); 
}
update(g);
}
class BallGenerator extends Thread { 
public void run() {
int x, y;
while (true) {
x = (int) (Math.random() * (RIGHT - Ball.SIZE));
y = (int) (Math.random() * (BOTTOM - Ball.SIZE));
balls.add(new Ball(x, y));
try {
Thread.sleep(3 * 1000);
} catch (Exception e) {
}
}
}
}
private void takeBallsOutFromArray() {
int size = balls.size();
for (int i = 0; i < size; i++) { 
Ball b = (Ball) balls.get(i); 
b.x += b.xStep;
b.y += b.yStep;
if (b.y <= TOP) {
b.y = TOP;
b.yStep = -b.yStep; 
}
if (b.y >= BOTTOM - b.SIZE) {
b.y = BOTTOM - b.SIZE;
b.yStep = -b.yStep;
}
if (b.x <= LEFT) {
b.x = LEFT;
b.xStep = -b.xStep;
}
if (b.x >= RIGHT - b.SIZE) {
b.x = RIGHT - b.SIZE;
b.xStep = -b.xStep;
}
}
}
private void movePlayer() {
if (isKeyUp) {
yPlayer -= 5;
}
if (isKeyDown) {
yPlayer += 5;
}
if (isKeyLeft) {
xPlayer -= 5;
}
if (isKeyRight) {
xPlayer += 5;
}
}
private void genCoin() {

xCoin = rd.nextInt(FRAME_WIDTH);
yCoin = rd.nextInt(FRAME_HEIGHT);

while (xCoin > FRAME_WIDTH - widthCoin) {
xCoin = rd.nextInt(FRAME_WIDTH);
}
while (yCoin < 30 || yCoin > FRAME_HEIGHT - heightCoin) {
yCoin = rd.nextInt(FRAME_HEIGHT);
}
}
private void checkPlayerNCoinBumped() {
if (xPlayer + widthPlayer > xCoin && yPlayer + heightPlayer > yCoin && xCoin + widthCoin > xPlayer && yCoin + heightCoin > yPlayer) {
score++;
genCoin();
if (count > 0) // 추가 
{
playSound("src/Audio/CoinEat.wav", false); 
}
}
}
private void playSound(String pathName, boolean isLoop) {
try {
Clip clip = AudioSystem.getClip();
File audioFile = new File(pathName);
AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioFile);
clip.open(audioStream);
clip.start();
if(isLoop)
clip.loop(Clip.LOOP_CONTINUOUSLY);
} catch(LineUnavailableException e) {
e.printStackTrace();
}  catch(UnsupportedAudioFileException e) {
e.printStackTrace();
}  catch(IOException e) {
e.printStackTrace();
}
}  
private void checkPlayerNWallBumped() {
if (yPlayer <= TOP) {
yPlayer = TOP;
}
if (yPlayer >= BOTTOM - heightPlayer) {
yPlayer = BOTTOM - heightPlayer;
}
if (xPlayer <= LEFT) {
xPlayer = LEFT;
}
if (xPlayer >= RIGHT - widthPlayer) {
xPlayer = RIGHT - widthPlayer;
}
}
private void checkPlayerNBallBumped() {
int size = balls.size();
for (int i = 0; i < size; i++) {
Ball b = (Ball) balls.get(i);
int xCircleCenter = b.x + Ball.SIZE / 2;
int yCircleCenter = b.y + Ball.SIZE / 2;
if (xPlayer + widthPlayer >= xCircleCenter - Ball.SIZE / 2 &&
yPlayer + heightPlayer >= yCircleCenter - Ball.SIZE / 2 &&
xPlayer <= xCircleCenter + Ball.SIZE / 2 &&
yPlayer <= yCircleCenter + Ball.SIZE / 2) {
playerLife--;
playSound("src/Audio/CoinEat.wav", false);
if (playerLife <= 0) {
GameOverFrame.SCORE = score;
GameOverFrame.SCORE_REPAINT = count;
isRunning = false;
this.setVisible(false);
this.dispose();
new GameOverFrame();
} else {

xPlayer = FRAME_WIDTH / 2 - widthPlayer / 2;
yPlayer = FRAME_HEIGHT / 2 - heightPlayer / 2;
}
}
}
} 
@Override
public void keyPressed(KeyEvent e) {
switch (e.getKeyCode()) { 
case KeyEvent.VK_UP:
isKeyUp = true;
break;
case KeyEvent.VK_DOWN:
isKeyDown = true;
break;
case KeyEvent.VK_LEFT:
isKeyLeft = true;
break;
case KeyEvent.VK_RIGHT:
isKeyRight = true;
break;
}
}
@Override
public void keyReleased(KeyEvent e) {
switch (e.getKeyCode()) { 
case KeyEvent.VK_UP:
isKeyUp = false;
break;
case KeyEvent.VK_DOWN:
isKeyDown = false;
break;
case KeyEvent.VK_LEFT:
isKeyLeft = false;
break;
case KeyEvent.VK_RIGHT:
isKeyRight = false;
break;
}
}
@Override
public void keyTyped(KeyEvent e) {
}
}
