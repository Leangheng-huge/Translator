package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.regex.*;

public class TranslateMachine extends JFrame {

    private static final String[] SOURCE_LANGS = {
            "Auto Detect", "English", "Khmer", "Chinese", "Japanese",
            "Korean", "French", "Spanish", "German",
            "Vietnamese", "Indonesian", "Arabic", "Portuguese", "Russian", "Hindi"
    };
    private static final String[] TARGET_LANGS = {
            "English", "Khmer", "Chinese", "Japanese", "Korean",
            "French", "Spanish", "German", "Vietnamese",
            "Indonesian", "Arabic", "Portuguese", "Russian", "Hindi"
    };

    private final JComboBox<String> sourceLangBox = UIHelper.styledCombo(SOURCE_LANGS);
    private final JComboBox<String> targetLangBox = UIHelper.styledCombo(TARGET_LANGS);
    private final JButton swapBtn;
    private final JButton translateBtn;
    private final JLabel statusLabel;
    private final TranslatePanel translatePanel;
    private final ApiClient apiClient = new ApiClient();

    public TranslateMachine() {
        setTitle("Translate Machine");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 620);
        setMinimumSize(new Dimension(700, 500));
        setLocationRelativeTo(null);
        getContentPane().setBackground(AppColors.BG);
        setLayout(new BorderLayout());

        translatePanel = new TranslatePanel();
        swapBtn        = buildSwapBtn();
        translateBtn   = buildTranslateBtn();
        statusLabel    = buildStatusLabel();

        add(buildHeader(), BorderLayout.NORTH);
        add(translatePanel, BorderLayout.CENTER);
        add(buildFooter(), BorderLayout.SOUTH);

        // Ctrl+Enter shortcut
        KeyStroke ctrlEnter = KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, InputEvent.CTRL_DOWN_MASK);
        getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(ctrlEnter, "translate");
        getRootPane().getActionMap().put("translate", new AbstractAction() {
            public void actionPerformed(ActionEvent e) { doTranslate(); }
        });

        setVisible(true);
    }

    // header
    private JPanel buildHeader() {
        JPanel header = new JPanel();
        header.setBackground(AppColors.BG);
        header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));
        header.setBorder(BorderFactory.createEmptyBorder(28, 30, 18, 30));


        JLabel title = new JLabel("Translate Machine");
        title.setFont(new Font("Dialog", Font.BOLD, 36));
        title.setForeground(AppColors.TEXT);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel langRow = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        langRow.setBackground(AppColors.BG);
        langRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, 44));
        langRow.add(sourceLangBox);
        langRow.add(swapBtn);
        langRow.add(targetLangBox);

        header.add(Box.createVerticalStrut(8));
        header.add(title);
        header.add(Box.createVerticalStrut(20));
        header.add(langRow);
        return header;
    }

    // footer
    private JPanel buildFooter() {
        JPanel footer = new JPanel(new BorderLayout(10, 0));
        footer.setBackground(AppColors.BG);
        footer.setBorder(BorderFactory.createEmptyBorder(14, 30, 24, 30));
        footer.add(translateBtn, BorderLayout.CENTER);
        footer.add(statusLabel, BorderLayout.SOUTH);
        return footer;
    }

    private JButton buildSwapBtn() {
        JButton btn = new JButton("⇄");
        btn.setFont(new Font("Dialog", Font.BOLD, 16));
        btn.setForeground(AppColors.MUTED);
        btn.setBackground(AppColors.SURFACE);
        btn.setBorder(BorderFactory.createLineBorder(AppColors.BORDER_CLR));
        btn.setPreferredSize(new Dimension(44, 36));
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.addActionListener(e -> swapLanguages());
        UIHelper.addHover(btn, AppColors.ACCENT);
        return btn;
    }

    private JButton buildTranslateBtn() {
        JButton btn = new JButton("⚡  TRANSLATE  (Ctrl+Enter)");
        btn.setFont(new Font("Dialog", Font.BOLD, 14));
        btn.setBackground(AppColors.ACCENT);
        btn.setForeground(Color.BLACK);
        btn.setBorder(BorderFactory.createEmptyBorder(12, 24, 12, 24));
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.addActionListener(e -> doTranslate());
        return btn;
    }

    private JLabel buildStatusLabel() {
        JLabel lbl = new JLabel(" ");
        lbl.setFont(new Font("Monospaced", Font.PLAIN, 11));
        lbl.setForeground(AppColors.MUTED);
        return lbl;
    }

    // logic of translator
    private void doTranslate() {
        String text = translatePanel.inputArea.getText().trim();
        if (text.isEmpty()) return;

        String src = (String) sourceLangBox.getSelectedItem();
        String tgt = (String) targetLangBox.getSelectedItem();

        translateBtn.setEnabled(false);
        translatePanel.outputArea.setText("");
        statusLabel.setForeground(AppColors.ACCENT);
        statusLabel.setText("Translating…");

        SwingWorker<String, Void> worker = new SwingWorker<>() {
            protected String doInBackground() throws Exception {
                return apiClient.translate(text, src, tgt);
            }
            protected void done() {
                try {
                    String result = get();
                    if (src.equals("Auto Detect")) {
                        Matcher m = Pattern.compile("\\[Detected:\\s*(.+?)\\]").matcher(result);
                        if (m.find()) {
                            translatePanel.detectedLabel.setText("Detected: " + m.group(1));
                            result = result.replaceAll("\\[Detected:.*?\\]", "").trim();
                        }
                    }
                    translatePanel.outputArea.setText(result);
                    statusLabel.setForeground(AppColors.MUTED);
                    statusLabel.setText("✓ Done");
                } catch (Exception ex) {
                    statusLabel.setForeground(AppColors.ERROR_CLR);
                    statusLabel.setText("Error: " + ex.getMessage());
                }
                translateBtn.setEnabled(true);
            }
        };
        worker.execute();
    }

    private void swapLanguages() {
        String src = (String) sourceLangBox.getSelectedItem();
        String tgt = (String) targetLangBox.getSelectedItem();
        if (src.equals("Auto Detect")) return;

        for (int i = 0; i < sourceLangBox.getItemCount(); i++)
            if (sourceLangBox.getItemAt(i).equals(tgt)) { sourceLangBox.setSelectedIndex(i); break; }
        for (int i = 0; i < targetLangBox.getItemCount(); i++)
            if (targetLangBox.getItemAt(i).equals(src)) { targetLangBox.setSelectedIndex(i); break; }

        String inputTxt  = translatePanel.inputArea.getText();
        String outputTxt = translatePanel.outputArea.getText();
        translatePanel.inputArea.setText(outputTxt);
        translatePanel.outputArea.setText(inputTxt);
    }

    // Main class
    public static void main(String[] args) {
        try { UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName()); }
        catch (Exception ignored) {}
        SwingUtilities.invokeLater(TranslateMachine::new);
    }
}
