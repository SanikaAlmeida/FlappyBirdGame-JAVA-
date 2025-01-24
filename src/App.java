import javax.swing.*;

public class App {
    public static void main(String[] args) throws Exception {
        System.out.println("Hello, World!");
        int width=360,height=640;

        JFrame frame=new JFrame("Flappy Bird");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setSize(width,height);

        Game g=new Game();
        frame.add(g);
        frame.pack();
        g.requestFocus();
        frame.setVisible(true);
    }
}
