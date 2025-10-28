import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class BMI extends JFrame implements ActionListener {
    private JTextField weightField = new JTextField(10);
    private JTextField heightField = new JTextField(10); 
    private JButton calcButton = new JButton("Calculate"); 
    private JButton clearButton = new JButton("Clear"); 

    public BMI() {
        setTitle("Dope BMI Calculation");        // 視窗標題
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(300, 200);                       // 視窗大小

        // 第一區：輸入體重
        JPanel p1 = new JPanel(new FlowLayout());
        p1.add(new JLabel("Weight(kg):"));        // 標籤
        p1.add(weightField);

        // 第二區：輸入身高
        JPanel p2 = new JPanel(new FlowLayout());
        p2.add(new JLabel("Height(cm):"));
        p2.add(heightField);

        // 第三區：按鈕
        JPanel p3 = new JPanel(new FlowLayout());
        p3.add(calcButton);
        p3.add(clearButton);

        // 主要容器：垂直排列三個區塊
        Container c = getContentPane();
        c.setLayout(new BoxLayout(c, BoxLayout.Y_AXIS));
        c.add(p1);
        c.add(p2);
        c.add(p3);

        // 設定事件監聽
        calcButton.addActionListener(this);       // 觸發 actionPerformed()
        clearButton.addActionListener(e -> {      // Lambda 清除輸入
            weightField.setText("");
            heightField.setText("");
        });
    }

    // 當按下 "Calculate" 時執行
    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            double weight = Double.parseDouble(weightField.getText()); // 體重
            double height = Double.parseDouble(heightField.getText()) / 100; // 公尺
            double bmi = weight / (height * height);  // BMI公式

            // 判斷區間
            String result;
            if (bmi < 18.5) result = "Underweight";
            else if (bmi < 24.9) result = "Normal";
            else if (bmi < 29.9) result = "Overweight";
            else result = "Obese";

            // 顯示結果
            JOptionPane.showMessageDialog(this,
                    String.format("BMI = %.2f  %s", bmi, result));
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Invalid input!"); // 錯誤處理
        }
    }

    public static void main(String[] args) {
        new BMI().setVisible(true); // 啟動程式
    }
}
