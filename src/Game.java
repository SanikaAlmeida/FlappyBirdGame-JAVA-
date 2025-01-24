import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.*;
import javax.swing.*;
import javax.swing.Timer;


public class Game extends JPanel implements ActionListener,KeyListener{
    int width=360,height=640;
    Image bg,toppipe,bottompipe,bird;
    boolean gameover=false;
    double score=0;

    //bird pos
    int birdX=width/8;
    int birdY=height/2;
    int birdWidth=34;
    int birdHeight=24;
    
    //pipe
    int pipeX=width;
    int pipeY=0;
    int pipeWidth=64;
    int pipeHeight=512;

    int velocityX=-4;//motion of pipes
    int velocityY=0;//birds ascent
    int gravity=1;

    Timer gameloop;
    Timer placepipes;

    class Pipe{
        int width=pipeWidth,height=pipeHeight;
        int X=pipeX,Y=pipeY;
        Image img;
        boolean passed=false;
        double score=0;

        Pipe(Image img){
            this.img=img;
        }
    }

    ArrayList<Pipe> pipes;
    Random random=new Random();

    Game(){  
        setPreferredSize(new Dimension(width,height));
        setFocusable(true);
        addKeyListener(this);

        bg=new ImageIcon(getClass().getResource("./flappybirdbg.png")).getImage();
        toppipe=new ImageIcon(getClass().getResource("./toppipe.png")).getImage();
        bottompipe=new ImageIcon(getClass().getResource("./bottompipe.png")).getImage();
        bird=new ImageIcon(getClass().getResource("./flappybird.png")).getImage();

        gameloop=new Timer(1000/60,this);
        gameloop.start();

        pipes=new ArrayList<>();
        placepipes=new Timer(1500,new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e){
                placepipe();
            }
        });
        placepipes.start();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g){
        //(img,x-coordinate start,y-coordinate start,width,height)
        g.drawImage(bg, 0,0,width,height,null);
        g.drawImage(bird, birdX,birdY,birdWidth,birdHeight,null);

        for(int i=0;i<pipes.size();i++){
            Pipe p=pipes.get(i);
            g.drawImage(p.img, p.X,p.Y,p.width,p.height,null);
        }

        g.setColor(Color.white);
        g.setFont(new Font("Arial", Font.PLAIN, 32));
        if (gameover) {
            g.drawString("Game Over: " + String.valueOf((int) score), 10, 35);
        }
        else {
            g.drawString(String.valueOf((int) score), 10, 35);
        }
    }

    public void move(){
        velocityY+=gravity;
        birdY+=velocityY;
        birdY=Math.max(birdY,0);

        for(int i=0;i<pipes.size();i++){
            Pipe p=pipes.get(i);
            p.X+=velocityX;

            if(!p.passed && birdX> p.X+p.width){
                p.passed=true;
                score+=0.5;
            }

            if(collision(p))gameover=true;
        }

        if(birdY>height)gameover=true;
    }

    public boolean collision(Pipe p){
        return  birdX <p.X+p.width&&
                birdX+birdWidth >p.X &&
                birdY <p.Y+p.height &&
                birdY+birdHeight >p.Y;
    }
    public void placepipe(){
        int randomY=(int)(pipeY - (pipeHeight/4) -Math.random()*(pipeHeight/2));
        int pipespace=pipeHeight/4;

        Pipe top=new Pipe(toppipe);
        top.Y=randomY;
        pipes.add(top);

        Pipe bottom=new Pipe(bottompipe);
        bottom.Y=top.Y+pipeHeight+pipespace;
        pipes.add(bottom);

    }
    @Override
    public void actionPerformed(ActionEvent e) {
        move();
        repaint();
        if(gameover){
            placepipes.stop();
            gameloop.stop();
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if(e.getKeyCode()==KeyEvent.VK_SPACE)velocityY=-9;

        if (gameover) {
            birdY=width/8;
            velocityY = 0;
            pipes.clear();
            gameover = false;
            score = 0;
            gameloop.start();
            placepipes.start();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {}

    @Override
    public void keyTyped(KeyEvent e) {}


}
