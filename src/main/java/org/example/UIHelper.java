package org.example;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.MatteBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class UIHelper {

    public static JComboBox<String> styledCombo(String[] items) {
        JComboBox<String> box = new JComboBox<>(items);
        box.setBackground(AppColors.SURFACE);
        box.setForeground(AppColors.TEXT);
        box.setFont(new Font("Dialog", Font.BOLD, 13));
        box.setPreferredSize(new Dimension(200, 36));
        box.setBorder(BorderFactory.createLineBorder(AppColors.BORDER_CLR));
        box.setFocusable(false);
        return box;
    }

    public static JButton iconButton(String label) {
        JButton btn = new JButton(label);
        btn.setFont(new Font("Monospaced", Font.PLAIN, 11));
        btn.setForeground(AppColors.MUTED);
        btn.setBackground(AppColors.SURFACE);
        btn.setBorder(BorderFactory.createLineBorder(AppColors.BORDER_CLR));
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        addHover(btn, AppColors.TEXT);
        return btn;
    }

    public static JLabel monoLabel(String text) {
        JLabel lbl = new JLabel(text);
        lbl.setFont(new Font("Monospaced", Font.PLAIN, 10));
        lbl.setForeground(AppColors.MUTED);
        return lbl;
    }

    public static JPanel topBar(JComponent left, JComponent right) {
        JPanel bar = new JPanel(new BorderLayout());
        bar.setBackground(AppColors.SURFACE);
        bar.setBorder(new CompoundBorder(
                new MatteBorder(0, 0, 1, 0, AppColors.BORDER_CLR),
                BorderFactory.createEmptyBorder(8, 16, 8, 16)
        ));
        bar.add(left, BorderLayout.WEST);
        bar.add(right, BorderLayout.EAST);
        return bar;
    }

    public static JPanel bottomBar(JButton... buttons) {
        JPanel bar = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 6));
        bar.setBackground(AppColors.SURFACE);
        bar.setBorder(new MatteBorder(1, 0, 0, 0, AppColors.BORDER_CLR));
        for (JButton btn : buttons) bar.add(btn);
        return bar;
    }

    public static JTextArea styledTextArea(boolean editable) {
        JTextArea area = new JTextArea();
        area.setBackground(AppColors.SURFACE);
        area.setForeground(AppColors.TEXT);
        area.setCaretColor(AppColors.ACCENT);
        area.setFont(new Font("Dialog", Font.PLAIN, 14));
        area.setLineWrap(true);
        area.setWrapStyleWord(true);
        area.setEditable(editable);
        area.setBorder(BorderFactory.createEmptyBorder(14, 16, 14, 16));
        return area;
    }

    public static JScrollPane styledScroll(JTextArea area) {
        JScrollPane scroll = new JScrollPane(area);
        scroll.setBorder(null);
        scroll.setBackground(AppColors.SURFACE);
        scroll.getViewport().setBackground(AppColors.SURFACE);
        return scroll;
    }

    public static void addHover(JButton btn, Color hoverColor) {
        Color original = btn.getForeground();
        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { btn.setForeground(hoverColor); }
            public void mouseExited(MouseEvent e)  { btn.setForeground(original); }
        });
    }
}