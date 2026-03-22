package org.example;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.datatransfer.StringSelection;

public class TranslatePanel extends JPanel {

    public final JTextArea inputArea;
    public final JTextArea outputArea;
    public final JLabel charCountLabel;
    public final JLabel detectedLabel;
    private final JButton clearBtn;
    private final JButton copyBtn;

    public TranslatePanel() {
        setLayout(new GridLayout(1, 2, 2, 0));
        setBackground(AppColors.BORDER_CLR);
        setBorder(BorderFactory.createEmptyBorder(0, 30, 0, 30));

        charCountLabel = UIHelper.monoLabel("0 / 2000");
        detectedLabel  = UIHelper.monoLabel("");
        detectedLabel.setForeground(AppColors.ACCENT);

        inputArea  = UIHelper.styledTextArea(true);
        outputArea = UIHelper.styledTextArea(false);

        clearBtn = UIHelper.iconButton("✕ Clear");
        copyBtn  = UIHelper.iconButton("⎘ Copy");

        add(buildInputPane());
        add(buildOutputPane());

        wireListeners();
    }

    private JPanel buildInputPane() {
        JPanel pane = new JPanel(new BorderLayout());
        pane.setBackground(AppColors.SURFACE);
        pane.add(UIHelper.topBar(UIHelper.monoLabel("INPUT"), charCountLabel), BorderLayout.NORTH);
        pane.add(UIHelper.styledScroll(inputArea), BorderLayout.CENTER);
        pane.add(UIHelper.bottomBar(clearBtn), BorderLayout.SOUTH);
        return pane;
    }

    private JPanel buildOutputPane() {
        JPanel pane = new JPanel(new BorderLayout());
        pane.setBackground(AppColors.SURFACE);
        pane.add(UIHelper.topBar(UIHelper.monoLabel("TRANSLATION"), detectedLabel), BorderLayout.NORTH);
        pane.add(UIHelper.styledScroll(outputArea), BorderLayout.CENTER);
        pane.add(UIHelper.bottomBar(copyBtn), BorderLayout.SOUTH);
        return pane;
    }

    private void wireListeners() {
        // char counter
        inputArea.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { updateCount(); }
            public void removeUpdate(DocumentEvent e) { updateCount(); }
            public void changedUpdate(DocumentEvent e) { updateCount(); }
        });

        // clear
        clearBtn.addActionListener(e -> {
            inputArea.setText("");
            outputArea.setText("");
            detectedLabel.setText("");
        });

        // copy
        copyBtn.addActionListener(e -> {
            String t = outputArea.getText();
            if (!t.isBlank()) {
                Toolkit.getDefaultToolkit().getSystemClipboard()
                        .setContents(new StringSelection(t), null);
                copyBtn.setText("✓ Copied!");
                Timer timer = new Timer(1500, ev -> copyBtn.setText("⎘ Copy"));
                timer.setRepeats(false);
                timer.start();
            }
        });
    }

    private void updateCount() {
        int len = inputArea.getText().length();
        if (len > 2000) inputArea.setText(inputArea.getText().substring(0, 2000));
        charCountLabel.setText(Math.min(len, 2000) + " / 2000");
    }
}