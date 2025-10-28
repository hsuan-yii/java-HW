import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import javax.sound.sampled.*;

public class cat extends JFrame implements ActionListener{
    private static String labelString = "count:";
    private int num = 0;
    private JLabel label;
    private Clip meowClip;

    public cat(){
        Container c = getContentPane();
        c.setLayout(new FlowLayout());

        label = new JLabel(labelString + num);
        label.setBorder(BorderFactory.createLineBorder(Color.blue));
        
        ImageIcon icon = new ImageIcon("cat_icon.jpg");
        Image scaled = icon.getImage().getScaledInstance(120, 100, Image.SCALE_SMOOTH);
        icon = new ImageIcon(scaled);

        JButton button = new JButton("click(k)");
        button.setIcon(icon);
        button.setHorizontalTextPosition(SwingConstants.RIGHT);
        button.setIconTextGap(8);
        button.setMnemonic(KeyEvent.VK_K);
        button.addActionListener(this);
        
        JPanel jpane = new JPanel();
        jpane.setBorder(BorderFactory.createLineBorder(Color.yellow));
        jpane.add(label);
        jpane.add(button);
        c.add(jpane);

        meowClip = loadClip("meow.wav");

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(300, 100);
        setLayout(new FlowLayout());
        // setLocationRelativeTo(null);
    }
    public void actionPerformed(ActionEvent evt){
        num++;
        label.setText(labelString+num);

        if(meowClip != null){
            if(meowClip.isRunning()) meowClip.stop();
            meowClip.setFramePosition(0);
            meowClip.start();
        }
        else
            Toolkit.getDefaultToolkit().beep();
    }
    public static void main(String[] args){
    SwingUtilities.invokeLater(()->{
            cat app = new cat();
            app.setVisible(true);
            });
    }
private static Clip loadClip(String path) {
        try {
            File f = new File(path);
            if (!f.exists()) {
                System.out.println("找不到音效檔：" + f.getAbsolutePath());
                return null;
            }
            AudioInputStream ais = AudioSystem.getAudioInputStream(f);
            Clip clip = AudioSystem.getClip();
            clip.open(ais);
            return clip;
        } catch (Exception e) {
            System.out.println("音效載入失敗：" + e.getMessage());
            return null;
        }
    }}
