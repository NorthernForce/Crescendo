package frc.robot.subsystems;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.XboxController;

public class XboxDisplayer extends JPanel {
    private Image xboxController;
    private JFrame frame;
    private static final String TITLE = "Xbox Driver";
    private GenericHID driver = null;
    public XboxDisplayer() {
        frame = new JFrame();
        frame.setBounds(0, 0, 1000, 350);
        setBackground(new Color(100, 100, 100));

        try {
            xboxController = ImageIO.read(new File("images/xbox_no_back.png"));
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error loading image: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }

        //if(DriverStation.getJoystickIsXbox(0))
        //{
        //    driver = new XboxController(0);
        //}
        frame.add(this);
        frame.setVisible(true);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (xboxController != null) {
            g.drawImage(xboxController, 50, 50, 300, 200,this);
        }
        g.setColor(Color.RED);
        g.fillOval(267,120,21,21); // y
        g.fillOval(267,80,21,21); // a
        g.fillOval(246,100,21,21); // b
        g.fillOval(287,100,21,21); // x

        g.fillRect(154,164,15,15); // down POV
        g.fillRect(169,149,15,15); // right POV
        g.fillRect(139,149,15,15); // left POV
        g.fillRect(154,134,15,15); // left POV
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new XboxDisplayer();
        });
    }
}
