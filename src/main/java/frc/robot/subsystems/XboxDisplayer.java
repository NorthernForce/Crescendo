package frc.robot.subsystems;

import java.awt.Color;
import java.awt.Dimension;
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
import edu.wpi.first.wpilibj2.command.button.CommandXboxController;
import edu.wpi.first.wpilibj2.command.button.Trigger;

public class XboxDisplayer extends JPanel {
    private Image xboxController;

    private int[][] buttonDimensions = {{266,119}, // y
                                        {266,79}, // a
                                        {245,99}, // b
                                        {286,99}}; // x
    private int[][] arrowDimensions = {{154,165}, // down
                                        {169,149}, // right
                                        {139,149}, // left
                                        {154,133}}; // up
    private JFrame frame;
    private static final String TITLE = "Xbox Driver";
    private CommandXboxController driver = null;
    // private Trigger[] triggers = {driver.y(), driver.a(), driver.b(), driver.x(), driver.povDown(), driver.povRight(), driver.povLeft(), driver.povUp()};
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

        int circleSize = 22;
        int joystickSize = 35;
        Dimension squareSize = new Dimension(15,16);

        // buttons
        for(int i = 0; i < buttonDimensions.length + arrowDimensions.length; i++)
        {
            if(i < buttonDimensions.length)
            {
                g.fillOval(buttonDimensions[i][0], buttonDimensions[i][1], circleSize, circleSize);
            } 
            else if(i < buttonDimensions.length + arrowDimensions.length)
            {
                g.fillRect(arrowDimensions[i - buttonDimensions.length][0], arrowDimensions[i - buttonDimensions.length][1], squareSize.width, squareSize.height);
            } 
        }
        g.fillOval(106, 93, joystickSize, joystickSize);

        // joysticks
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new XboxDisplayer();
        });
    }
}
