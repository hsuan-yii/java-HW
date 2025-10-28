import java.awt.*;                 // GUI 版面配置與顏色
import javax.swing.*;              // Swing 元件
import java.util.List;             // List 介面
import java.util.ArrayList;        // ArrayList 實作

// ===== 聯絡人類別：資料模型 =====
class Contact {
    // 欄位：使用 final 表示建立後不可更改
    private final String name, zip, address, gender, email;
    private final boolean married;

    // 建構子：建立一筆聯絡人時傳入所有欄位
    public Contact(String name, String zip, String address,
                   boolean married, String gender, String email) {
        this.name = name;
        this.zip = zip;
        this.address = address;
        this.married = married;
        this.gender = gender;
        this.email = email;
    }

    // toString：轉成好看的輸出文字格式
    public String toString() {
        return String.format("Name:%s, Zip:%s, Address:%s, Married:%s, Gender:%s, Email:%s",
                name, zip, address, married ? "Yes" : "No", gender, email);
    }
}

// ===== 主類別：通訊錄 GUI 視窗 =====
public class ContactBook1 extends JFrame {

    // 資料結構：儲存多筆聯絡人
    private final List<Contact> contacts = new ArrayList<>();

    // ==== 介面元件宣告 ====
    private final JTextField tfName = new JTextField(12);   // 名字
    private final JComboBox<String> cbZip = new JComboBox<>(new String[]{
        "100","104","106","110","220","300","330","407","701","800"});  // 郵遞區號
    private final JTextField tfAddress = new JTextField(18); // 地址
    private final JCheckBox chkMarried = new JCheckBox("Married"); // 是否已婚
    private final JRadioButton rbMale = new JRadioButton("Man", true);  // 男性（預設）
    private final JRadioButton rbFemale = new JRadioButton("Female");   // 女性
    private final JTextField tfEmail = new JTextField(18);   // Email

    // 四個按鈕
    private final JButton btnAdd = new JButton("Add");
    private final JButton btnClear = new JButton("Clear");
    private final JButton btnList = new JButton("List");
    private final JButton btnExit = new JButton("Exit");

    // ===== 建構子：建立視窗與介面 =====
    public ContactBook1() {
        super("ContactBook1"); // 視窗標題
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // 關閉視窗結束程式
        setLayout(new GridLayout(4, 2, 6, 6));          // 主畫面 4列×2欄、間距6px

        // 建立八個面板，讓內容分行排列
        JPanel p1 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JPanel p2 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JPanel p3 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JPanel p4 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JPanel p5 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JPanel p6 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JPanel p7 = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JPanel p8 = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        // ==== 每行內容配置 ====
        p1.add(new JLabel("Name: "));     p1.add(tfName);
        p2.add(new JLabel("Zip: "));      p2.add(cbZip);
        p3.add(new JLabel("Address: "));  p3.add(tfAddress);
        p4.add(chkMarried);

        // 建立性別群組（只能二選一）
        ButtonGroup g = new ButtonGroup();
        g.add(rbMale);
        g.add(rbFemale);
        p5.add(new JLabel("Gender: ")); p5.add(rbMale); p5.add(rbFemale);

        // Email 與按鈕區
        p6.add(new JLabel("Email: "));  p6.add(tfEmail);
        p7.add(btnAdd);  p7.add(btnClear);
        p8.add(btnList); p8.add(btnExit);

        // 將所有面板放進視窗
        add(p1); add(p2); add(p3); add(p4);
        add(p5); add(p6); add(p7); add(p8);

        // ==== 綁定按鈕事件 ====
        btnAdd.addActionListener(e -> onAdd());   // 新增
        btnClear.addActionListener(e -> onClear()); // 清除
        btnList.addActionListener(e -> onList());   // 顯示清單
        btnExit.addActionListener(e -> System.exit(0)); // 離開

        // ==== 視窗設定 ====
        pack();                     // 自動調整大小以容納內容
        setLocationRelativeTo(null); // 視窗置中
        setVisible(true);            // 顯示視窗
    }

    // ===== 功能1：新增聯絡人 =====
    private void onAdd() {
        String name = tfName.getText().trim();
        String zip = (String) cbZip.getSelectedItem();
        String address = tfAddress.getText().trim();
        boolean married = chkMarried.isSelected();
        String gender = rbMale.isSelected() ? "Man" : "Female";
        String email = tfEmail.getText().trim();

        // 檢查輸入是否有效
        if (name.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Name couldn't be empty!");
            return;
        }
        if (!email.contains("@")) {
            JOptionPane.showMessageDialog(this, "Email format is incorrect!");
            return;
        }

        // 建立新聯絡人並加入清單
        contacts.add(new Contact(name, zip, address, married, gender, email));
        JOptionPane.showMessageDialog(this,
                "Add success! Total: " + contacts.size());
    }

    // ===== 功能2：清除輸入欄 =====
    private void onClear() {
        tfName.setText("");
        cbZip.setSelectedIndex(0);     // 改正：使用 setSelectedIndex 而非 setSelectedItem
        tfAddress.setText("");
        chkMarried.setSelected(false);
        rbMale.setSelected(true);
        tfEmail.setText("");
        tfName.requestFocus();         // 游標回到名字欄
    }

    // ===== 功能3：列出所有聯絡人 =====
    private void onList() {
        if (contacts.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No data!");
            return;
        }

        // 將所有聯絡人轉成文字
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < contacts.size(); i++)
            sb.append(i + 1).append(". ").append(contacts.get(i)).append("\n");

        // 建立多行文字區顯示結果
        JTextArea ta = new JTextArea(sb.toString(), 12, 50);
        ta.setEditable(false);

        // 用捲軸顯示（避免內容太長）
        JOptionPane.showMessageDialog(this, new JScrollPane(ta),
                "ContactList", JOptionPane.INFORMATION_MESSAGE);
    }

    // ===== 主程式進入點 =====
    public static void main(String[] args) {
        SwingUtilities.invokeLater(ContactBook1::new);
    }
}
