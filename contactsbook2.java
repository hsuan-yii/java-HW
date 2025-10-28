// ===== 匯入標準函式庫 =====
import javax.swing.*;                     // Swing 視窗元件 (JFrame、JButton、JLabel、JTextField…)
import java.awt.*;                        // AWT 版面配置與顏色等 (BorderLayout、FlowLayout、GridBagLayout…)
import java.awt.event.ActionEvent;        // 事件物件型別 (ActionEvent)
import java.nio.file.*;                   // 檔案路徑與存取 (Path、Paths、Files)
import java.util.*;                       // 基礎集合 (List、ArrayList、Optional)
import java.util.List;                    // 指定使用介面 List
import java.util.stream.Collectors;       // Stream 收集器 (Collectors.toList 等)

// 主視窗類別，繼承 JFrame
public class ContactsBooks extends JFrame {

    // ======== UI 欄位元件定義 ========
    private final JTextField tfAccount = new JTextField(16);     // 帳號輸入框，寬度提示 16
    private final JPasswordField pfPassword = new JPasswordField(16); // 密碼輸入框
    private final JCheckBox cbMarried = new JCheckBox("已婚");   // 已婚勾選
    private final JSlider slHeight = new JSlider(120, 220, 170); // 身高滑桿：最小120、最大220、初始170
    private final JSlider slWeight = new JSlider(30, 200, 65);   // 體重滑桿：最小30、最大200、初始65
    private final JLabel lbHVal = new JLabel("170 cm");          // 顯示身高數值的標籤
    private final JLabel lbWVal = new JLabel("65 kg");           // 顯示體重數值的標籤
    private final JTextArea taBio = new JTextArea(6, 24);        // 簡介，多行文字區

    // ======== 操作按鈕 ========
    private final JButton btAdd = new JButton("新增");
    private final JButton btDelete = new JButton("刪除");
    private final JButton btClear = new JButton("清除");
    private final JButton btQuery = new JButton("查詢");
    private final JButton btSave = new JButton("存檔");
    private final JButton btExit = new JButton("結束");

    // ======== 資料模型 ========
    private final List<Contact> contacts = new ArrayList<>();    // 以 ArrayList 暫存通訊錄資料
    private final Path savePath = Paths.get("context.txt");      // 檔案儲存路徑 (CSV 內容)

    // 建構子：建立 UI、註冊事件、完成擺放
    public ContactsBooks() {
        super("通訊錄 v2");                                      // 設定視窗標題
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);           // 關閉視窗即結束程式
        setLayout(new BorderLayout(8, 8));                        // 使用 BorderLayout，元件周邊 8px 間距

        // ===== 左側表單區：GridBagLayout 可彈性對齊 =====
        JPanel form = new JPanel(new GridBagLayout());            // 容納表單欄位
        GridBagConstraints g = new GridBagConstraints();          // 佈局約束
        g.insets = new Insets(6, 6, 6, 6);                        // 每個元件的外距
        g.anchor = GridBagConstraints.WEST;                       // 元件靠左對齊

        int row = 0;                                              // 表單列索引

        // 每列：左側標籤 + 右側輸入元件
        addRow(form, g, row++, new JLabel("帳號："), tfAccount);  // 第 0 列：帳號
        addRow(form, g, row++, new JLabel("密碼："), pfPassword); // 第 1 列：密碼
        addRow(form, g, row++, new JLabel("婚姻："), cbMarried);  // 第 2 列：婚姻

        // 設定身高滑桿的刻度與事件
        slHeight.setPaintTicks(true);                             // 顯示刻度線
        slHeight.setPaintLabels(true);                            // 顯示刻度值文字
        slHeight.setMajorTickSpacing(20);                         // 主要刻度間距 20
        slHeight.setMinorTickSpacing(5);                          // 次要刻度間距 5
        slHeight.addChangeListener(e ->                          // 監聽滑動改變時
                lbHVal.setText(slHeight.getValue() + " cm"));     // 即時更新右側顯示標籤
        addRow(form, g, row++, new JLabel("身高："),              // 第 3 列：滑桿 + 右側數值
                wrap(slHeight, lbHVal));                          // 用小面板把滑桿與標籤包起來

        // 設定體重滑桿的刻度與事件
        slWeight.setPaintTicks(true);
        slWeight.setPaintLabels(true);
        slWeight.setMajorTickSpacing(20);
        slWeight.setMinorTickSpacing(5);
        slWeight.addChangeListener(e ->
                lbWVal.setText(slWeight.getValue() + " kg"));
        addRow(form, g, row++, new JLabel("體重："),              // 第 4 列：滑桿 + 右側數值
                wrap(slWeight, lbWVal));

        // 簡介文字區：自動換行與捲軸
        taBio.setLineWrap(true);                                   // 文字自動換行
        taBio.setWrapStyleWord(true);                              // 以單字為單位換行
        JScrollPane sp = new JScrollPane(taBio);                   // 加上滾動條
        addRow(form, g, row++, new JLabel("簡介："), sp);          // 第 5 列：簡介 + 捲軸

        add(form, BorderLayout.CENTER);                            // 表單置於視窗中央區域

        // ===== 右下方按鈕列：FlowLayout 置中 =====
        JPanel actions = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 8)); // 水平間距12、垂直8
        actions.add(btAdd);                                        // 依序加入各個按鈕
        actions.add(btDelete);
        actions.add(btClear);
        actions.add(btQuery);
        actions.add(btSave);
        actions.add(btExit);
        add(actions, BorderLayout.SOUTH);                          // 按鈕列放在南邊

        // ===== 事件處理綁定 =====
        btAdd.addActionListener(this::onAdd);                      // 新增
        btDelete.addActionListener(this::onDelete);                // 刪除
        btClear.addActionListener(e -> clearForm());               // 清除表單
        btQuery.addActionListener(this::onQuery);                  // 查詢
        btSave.addActionListener(this::onSave);                    // 存檔
        btExit.addActionListener(e -> dispose());                  // 關閉視窗

        pack();                                                    // 依元件最適大小打包
        setLocationRelativeTo(null);                               // 視窗置中
    }

    // 新增：讀取表單、檢查帳號唯一、加入清單
    private void onAdd(ActionEvent e) {
        String acc = tfAccount.getText().trim();                   // 取帳號並去除空白
        if (acc.isEmpty()) {                                       // 空白則提示
            msg("帳號不可空白");
            return;
        }
        if (contacts.stream().anyMatch(c -> c.account.equals(acc))) { // 檢查是否已存在
            msg("帳號已存在");
            return;
        }
        Contact c = readForm();                                    // 將表單轉為 Contact
        contacts.add(c);                                           // 加入集合
        msg("已新增，共 " + contacts.size() + " 筆");               // 回饋筆數
    }

    // 刪除：依帳號移除
    private void onDelete(ActionEvent e) {
        String acc = tfAccount.getText().trim();                   // 取得帳號
        if (acc.isEmpty()) { msg("請輸入帳號"); return; }           // 必填檢查
        boolean removed = contacts.removeIf(c -> c.account.equals(acc)); // 條件刪除
        msg(removed ? "已刪除" : "找不到該帳號");                     // 結果回饋
    }

    // 查詢：依帳號尋找，若有則回填表單
    private void onQuery(ActionEvent e) {
        String acc = tfAccount.getText().trim();                   // 取得帳號
        if (acc.isEmpty()) { msg("請輸入帳號"); return; }           // 必填檢查
        Optional<Contact> hit = contacts.stream()                  // 以 Stream 篩選
                .filter(c -> c.account.equals(acc))
                .findFirst();
        if (hit.isPresent()) {                                     // 找到則回填
            fillForm(hit.get());
            msg("查詢成功");
        } else {
            msg("無此帳號");                                       // 未找到回饋
        }
    }

    // 存檔：把清單轉為 CSV 文字，寫入檔案
    private void onSave(ActionEvent e) {
        try {
            List<String> lines = new ArrayList<>();                // 建立輸出行集合
            lines.add("account,password,married,height_cm,weight_kg,bio"); // CSV 標頭
            lines.addAll(contacts.stream()                         // 將每筆 Contact 轉 CSV
                    .map(Contact::toCsv)
                    .collect(Collectors.toList()));
            Files.write(savePath, lines);                          // 寫到檔案 (預設覆寫)
            msg("已存檔：" + savePath.toAbsolutePath());             // 顯示完整路徑
        } catch (Exception ex) {
            msg("存檔失敗：" + ex.getMessage());                     // 例外處理
        }
    }

    // 將表單目前值讀入並形成一筆 Contact
    private Contact readForm() {
        String acc = tfAccount.getText().trim();                   // 帳號
        String pw = new String(pfPassword.getPassword());          // 密碼（從 char[] 建字串）
        boolean married = cbMarried.isSelected();                  // 已婚勾選
        int h = slHeight.getValue();                               // 身高值
        int w = slWeight.getValue();                               // 體重值
        String bio = taBio.getText().replace("\n", "\\n");         // 簡介：儲存時以 \n 轉義
        return new Contact(acc, pw, married, h, w, bio);           // 建立資料物件
    }

    // 將某筆 Contact 的值回填到表單元件
    private void fillForm(Contact c) {
        tfAccount.setText(c.account);                              // 帳號
        pfPassword.setText(c.password);                            // 密碼
        cbMarried.setSelected(c.married);                          // 已婚
        slHeight.setValue(c.heightCm);                             // 身高滑桿
        slWeight.setValue(c.weightKg);                             // 體重滑桿
        taBio.setText(c.bio.replace("\\n", "\n"));                 // 簡介：\n 還原為換行
    }

    // 清除表單並將滑桿重設為預設值
    private void clearForm() {
        tfAccount.setText("");                                     // 清空帳號
        pfPassword.setText("");                                    // 清空密碼
        cbMarried.setSelected(false);                              // 取消已婚
        slHeight.setValue(170);                                    // 重設身高
        slWeight.setValue(65);                                     // 重設體重
        taBio.setText("");                                         // 清空簡介
    }

    // GridBagLayout 的一列：左側元件 + 右側元件
    private static void addRow(JPanel p, GridBagConstraints g, int row, Component left, Component right) {
        g.gridx = 0; g.gridy = row; g.weightx = 0;                 // 左側欄，寬度不伸展
        g.fill = GridBagConstraints.NONE;                          // 不水平擴張
        p.add(left, g);                                            // 放入左側元件
        g.gridx = 1; g.gridy = row; g.weightx = 1;                 // 右側欄，允許水平撐滿
        g.fill = GridBagConstraints.HORIZONTAL;                    // 水平填滿
        p.add(right, g);                                           // 放入右側元件
    }

    // 把「滑桿」與「數值標籤」放在同一列的左右兩側
    private static JPanel wrap(JComponent main, JLabel valueLabel) {
        JPanel panel = new JPanel(new BorderLayout(6, 0));         // 左右留 6px
        panel.add(main, BorderLayout.CENTER);                      // 中間放滑桿
        panel.add(valueLabel, BorderLayout.EAST);                  // 右側放數值
        return panel;                                              // 回傳容器面板
    }

    // 彈跳式訊息框
    private static void msg(String s) {
        JOptionPane.showMessageDialog(null, s);                    // 顯示訊息
    }

    // ===== 內部資料類別：一筆聯絡人 =====
    private static class Contact {
        final String account;                                      // 帳號
        final String password;                                     // 密碼（範例示意，未加密）
        final boolean married;                                     // 是否已婚
        final int heightCm;                                        // 身高（公分）
        final int weightKg;                                        // 體重（公斤）
        final String bio;                                          // 簡介（\n 以 \\n 儲存）

        Contact(String account, String password, boolean married, int heightCm, int weightKg, String bio) {
            this.account = account;                                // 指派欄位
            this.password = password;
            this.married = married;
            this.heightCm = heightCm;
            this.weightKg = weightKg;
            this.bio = bio;
        }

        // 轉為一行 CSV 文字，含最基本的逗號與引號轉義
        String toCsv() {
            return String.join(",",
                    csv(account),                                   // 帳號欄
                    csv(password),                                  // 密碼欄
                    married ? "1" : "0",                            // 已婚欄（1/0）
                    String.valueOf(heightCm),                       // 身高欄
                    String.valueOf(weightKg),                       // 體重欄
                    csv(bio));                                      // 簡介欄（已轉義）
        }

        // 針對含有逗號、引號、換行的字串做最基本 CSV 轉義
        private static String csv(String s) {
            String t = s.replace("\"", "\"\"");                    // 引號以兩個引號表示
            if (t.contains(",") || t.contains("\"") || t.contains("\n")) {
                return "\"" + t + "\"";                            // 需要時整體加雙引號
            }
            return t;                                              // 否則原樣
        }
    }

    // 進入點：以 EDT 建立並顯示視窗
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new ContactsBooks().setVisible(true)); // 交給事件派發緒
    }
}
