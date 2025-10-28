import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

// 主視窗類別：Signature Pad 簽名板
public class signature extends JFrame {
    public signature() {  // 建構子：建立主視窗
        super("Signature Pad");                         // 設定標題
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  // 關閉視窗即結束程式

        SignatureCanvas canvas = new SignatureCanvas();  // 建立畫布元件

        // ===== 按鈕列 =====
        JPanel controls = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 8));    
        JButton btnR = new JButton("Red");     // 紅色筆
        JButton btnG = new JButton("Green");   // 綠色筆
        JButton btnB = new JButton("Blue");    // 藍色筆
        JButton btnClear = new JButton("Clear"); // 清除畫面

        // 綁定事件（Lambda 寫法）
        btnR.addActionListener(e -> canvas.setDrawColor(Color.RED));
        btnG.addActionListener(e -> canvas.setDrawColor(Color.GREEN));
        btnB.addActionListener(e -> canvas.setDrawColor(Color.BLUE));
        btnClear.addActionListener(e -> canvas.requestClear());

        // 將按鈕加到控制列
        controls.add(btnR);
        controls.add(btnG);
        controls.add(btnB);
        controls.add(btnClear);

        // 視窗使用 BorderLayout：上方按鈕，下方畫布
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(controls, BorderLayout.NORTH);
        getContentPane().add(canvas, BorderLayout.CENTER);

        pack();                        // 自動調整大小
        setLocationRelativeTo(null);   // 視窗置中
        setVisible(true);              // 顯示視窗
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(signature::new);  // 安全啟動 GUI
    }
}

// ===== 繪圖畫布類別 =====
class SignatureCanvas extends JPanel {
    // ===== 用來記錄每一段畫線的資料 =====
    private static class Seg {
        final int x1, y1, x2, y2;  // 起點與終點座標
        final Color c;              // 畫線顏色
        Seg(int x1, int y1, int x2, int y2, Color c) {
            this.x1 = x1; this.y1 = y1; this.x2 = x2; this.y2 = y2; this.c = c;
        }
    }

    private final List<Seg> segs = new ArrayList<>();  // 存所有筆畫
    private Color current = Color.BLACK;               // 預設黑色
    private int px, py;                                // 上一個滑鼠點
    private boolean needClear = false;                 // 是否要清除畫面

    SignatureCanvas() {
        setPreferredSize(new Dimension(800, 500)); // 畫布大小
        setBackground(Color.WHITE);                 // 背景白色

        // 建立滑鼠事件監聽器（按下 + 拖曳）
        MouseAdapter handler = new MouseAdapter() {
            @Override 
            public void mousePressed(MouseEvent e) {
                // 記錄起點
                px = e.getX();
                py = e.getY();
            }
            @Override 
            public void mouseDragged(MouseEvent e) {
                // 拖曳時持續畫線
                int x = e.getX(), y = e.getY();
                segs.add(new Seg(px, py, x, y, current)); // 儲存線段
                px = x; py = y;                           // 更新前一點
                repaint();                                // 重繪畫面
            }
        };
        addMouseListener(handler);
        addMouseMotionListener(handler);
    }

    // 改變畫筆顏色
    void setDrawColor(Color c) { current = c; }

    // 清除請求
    void requestClear() {
        // 標記清除狀態，並重繪畫面
        needClear = true;
        repaint();
    }

    // 重畫元件（系統自動呼叫）
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // 若需清除，清空畫面與紀錄
        if (needClear) {
            g.clearRect(0, 0, getSize().width, getSize().height);
            segs.clear();
            needClear = false;
            return;
        }

        // 逐條繪出筆畫
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setStroke(new BasicStroke(3f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND)); // 筆寬3px、圓角
        for (Seg s : segs) {
            g2.setColor(s.c);
            g2.drawLine(s.x1, s.y1, s.x2, s.y2);
        }
        g2.dispose();
    }
}
