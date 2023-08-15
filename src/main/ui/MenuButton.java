package ui;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

// Stylized JButton for the JMenuBar
public class MenuButton extends JButton {
    private final int height = 25;
    private final int width = 45;

    // EFFECTS: instantiate new MenuButton with styling
    public MenuButton(String label) {
        super(label);
        setStyle();
        addListener();
    }

    // EFFECTS: instantiate new MenuButton with icon
    public MenuButton(ImageIcon icon) {
        super(icon);
        setStyle();
        addListener();
    }

    // EFFECTS: sets the style of the button
    // MODIFIES: this
    private void setStyle() {
        this.setPreferredSize(new Dimension(this.width, this.height));
        this.setSize(new Dimension(this.width, this.height));
        this.setMinimumSize(new Dimension(this.width, this.height));
        this.setMaximumSize(new Dimension(this.width, this.height));
        this.setForeground(Color.BLACK);
        this.setBackground(Color.WHITE);
        this.setFont(new Font("Arial", Font.BOLD, 25));
        this.setBorder(new LineBorder(Color.lightGray,0, true));
    }

    // EFFECTS: add hover listeners on button to change background color
    // MODIFIES: this
    private void addListener() {
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                super.mouseEntered(e);
                setBackground(new Color(197, 243, 252));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                super.mouseExited(e);
                setBackground(Color.white);
            }
        });
    }


}
