package frc.robot.subsystems;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.function.Supplier;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.GenericHID;
import edu.wpi.first.wpilibj.XboxController;
import edu.wpi.first.wpilibj.XboxController.Axis;
import edu.wpi.first.wpilibj.XboxController.Button;

public class XboxDisplayer extends JPanel {
    private Image xboxController;
    private int[][] buttonDimensions = { { 266, 119 }, // y
            { 266, 79 }, // a
            { 245, 99 }, // b
            { 286, 99 } }; // x

    private int[][] arrowDimensions = { { 154, 165 }, // down
            { 169, 149 }, // right
            { 139, 149 }, // left
            { 154, 133 }, // up?
            { 169, 165 }, // down right
            { 139, 133 }, // up left
            { 169, 133 }, // up right
            { 139, 165 } }; // down left

    private int[][] joySticksDimensions = { { 106, 93 }, { 222, 138 } };
    private File xboxFile;
    private int[][] bumperDimensions = { { 95, 50 }, { 233, 50 } };
    private int[][] triggerDimensions = { { 45, 53 }, { 340, 53 } };
    private int[][] flushDimensions = { { 160, 90 }, { 220, 90 } };
    private static JFrame frame;
    private static final String TITLE = "Key Logger ( >:) XDXDXDXDXXDXDXDXD OLOLOLOLOLOLOLO HEHEHEHEHHHEHHEHEEHE )";
    private GenericHID driver;
    private ArrayList<Supplier<Double>> axis = new ArrayList<>();
    private ArrayList<Supplier<Boolean>> trigs = new ArrayList<>();

    public XboxDisplayer() {
        xboxFile = new File("images/xbox_no_back.png");
        if (DriverStation.getJoystickIsXbox(0)) {
            driver = new XboxController(0);
        }
        driver = new XboxController(0);
        frame = new JFrame();
        frame.setBounds(0, 0, 950, 350);
        frame.setTitle(TITLE);
        setBackground(new Color(100, 100, 100));

        try {
            xboxController = ImageIO.read(xboxFile);
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Error loading image: " + e.getMessage(), "Error bad",
                    JOptionPane.ERROR_MESSAGE);
        }
        // driver = new CommandXboxController(0);

        // else if(DriverStation.get)
        // {

        // }
        frame.add(this);
        frame.setVisible(true);
        setTrigs();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.RED);

        int circleSize = 22;
        int joystickSize = 35;
        Dimension squareSize = new Dimension(15, 16);
        Dimension bumperDimension = new Dimension(25, 75);
        Dimension triggerDimension = new Dimension(75, 15);
        Dimension flushDimension = new Dimension(7, 15);
        int lengths[] = { buttonDimensions.length, arrowDimensions.length, joySticksDimensions.length,
                bumperDimensions.length, triggerDimensions.length, flushDimensions.length };

        // buttons
        for (int ii = 0; ii < 2; ii++) {
            int changeTrig = 0;
            int changeBy = 0;
            int change = ii == 0 ? 0 : 500;
            if (xboxController != null) {
                g.drawImage(xboxController, 50 + change, 50, 300, 200, this);
            }
            for (int i = 0; i < ((Supplier<Integer>) () -> {
                int result = 0;
                for (int y : lengths)
                    result += y;
                return result;
            }).get(); i++) {
                final int current = i;
                int[] iRange = ((Supplier<int[]>) () -> {
                    int[] result = { 0, 0 };
                    int currentLength = lengths[0];
                    for (int x = 1; x < lengths.length; x++) {

                        if (currentLength <= current && current < lengths[x] + currentLength) {
                            result[0] = x;
                            // System.out.print("hi ");

                            result[1] = currentLength;
                            break;
                        }
                        currentLength += lengths[x];
                    }

                    return result;
                }).get();
                // System.out.println(
                // "For " + "i " + "equaling " + i + ", range[0] = " + iRange[0] + " and
                // range[1] = " + iRange[1]);
                if (iRange[0] == 0) {
                    g.setColor(trigs.get(i).get() ? Color.YELLOW : Color.RED);
                    // if(driver.getRawButton(Button.kA.value)) System.out.println("A pressed");
                    g.fillOval(buttonDimensions[i][0] + change, buttonDimensions[i][1], circleSize, circleSize);
                } else if (iRange[0] == 1) {
                    g.setColor(trigs.get(i).get() ? Color.YELLOW : Color.RED);
                    g.fillRect(arrowDimensions[i - iRange[1]][0] + change, arrowDimensions[i - iRange[1]][1],
                            squareSize.width,
                            squareSize.height);
                } else if (iRange[0] == 2) {
                    g.setColor(trigs.get(i).get() ? Color.YELLOW : Color.RED);
                    g.fillOval(
                            joySticksDimensions[i - iRange[1]][0] + change
                                    + (int) ((double) (axis.get(iRange[1] - 12 + changeBy).get()) * 7),
                            joySticksDimensions[i - iRange[1]][1]
                                    + (int) ((double) (axis.get(iRange[1] - 11 + changeBy).get()) * 7),
                            joystickSize, joystickSize);
                    changeBy += 2;
                } else if (iRange[0] == 3) {
                    g.setColor(trigs.get(i).get() ? Color.YELLOW : Color.RED);
                    g.fillRect(bumperDimensions[i - iRange[1]][0] + change, bumperDimensions[i - iRange[1]][1],
                            (int) bumperDimension.height, (int) bumperDimension.width);
                } else if (iRange[0] == 4) {
                    Graphics2D g2d = (Graphics2D) g;
                    float stroke = 2.0f;
                    Color orig = g2d.getColor();
                    g2d.setColor(Color.BLACK);
                    g2d.setStroke(new BasicStroke(stroke));
                    g2d.drawRect(triggerDimensions[i - iRange[1]][0] + change, triggerDimensions[i - iRange[1]][1],
                            (int) triggerDimension.height, (int) triggerDimension.width);
                    g2d.setColor(axis.get(iRange[1] - 12 + changeTrig).get() > 0.5 ? Color.YELLOW : Color.RED);

                    g2d.fillRect((int) (triggerDimensions[i - iRange[1]][0] + change + (int) stroke / 2),
                            (int) ((triggerDimension.width + (triggerDimensions[i - iRange[1]][1]) + (int) stroke / 2
                                    + (1 - (triggerDimensions[i - iRange[1]][1] + triggerDimension.width))
                                            * axis.get(i - 12).get())
                                    - (int) stroke / 2
                                    + (triggerDimensions[i - iRange[1]][1]) * axis.get(i - 12).get()),
                            (int) (triggerDimension.height - (int) stroke / 2),
                            (int)((triggerDimension.width+triggerDimensions[i-iRange[1]][1])-(((triggerDimension.width + (triggerDimensions[i - iRange[1]][1]) + (int) stroke / 2
                            + (1 - (triggerDimensions[i - iRange[1]][1] + triggerDimension.width))
                                    * axis.get(i - 12).get())
                            - (int) stroke / 2
                            + (triggerDimensions[i - iRange[1]][1]) * axis.get(i - 12).get()))));
                    g2d.setColor(orig);
                    changeTrig++;

                } else if (iRange[0] == 5) {
                    g.setColor(trigs.get(i-2).get() ? Color.YELLOW : Color.RED);
                    g.fillRect(flushDimensions[i - iRange[1]][0] + change, flushDimensions[i - iRange[1]][1],
                            flushDimension.height, flushDimension.width);
                }

            }
            Color orig = g.getColor();
            g.setColor(Color.BLACK);
            g.fillRect(440, 0, 10, frame.getHeight());
            g.setFont(new Font("arial", Font.BOLD, 30));
            g.drawString("Driver", 10, 30);
            g.drawString("Manipulator", 460, 30);
            g.setColor(orig);
        }
        // joysticks
    }

    public void setTrigs() {
        trigs.add(() -> driver.getRawButton(Button.kA.value));
        trigs.add(() -> driver.getRawButton(Button.kY.value));
        trigs.add(() -> driver.getRawButton(Button.kX.value));
        trigs.add(() -> driver.getRawButton(Button.kB.value));

        trigs.add(() -> driver.getPOV(0) == 180);
        trigs.add(() -> driver.getPOV(0) == 90);
        trigs.add(() -> driver.getPOV(0) == 270);
        trigs.add(() -> driver.getPOV(0) == 0);
        trigs.add(() -> driver.getPOV(0) == 135);
        trigs.add(() -> driver.getPOV(0) == 315);
        trigs.add(() -> driver.getPOV(0) == 45);
        trigs.add(() -> driver.getPOV(0) == 225);

        trigs.add(() -> driver.getRawButton(Button.kLeftStick.value));
        trigs.add(() -> driver.getRawButton(Button.kRightStick.value));

        trigs.add(() -> driver.getRawButton(Button.kLeftBumper.value));
        trigs.add(() -> driver.getRawButton(Button.kRightBumper.value));

        trigs.add(() -> driver.getRawButton(Button.kBack.value));
        trigs.add(() -> driver.getRawButton(Button.kStart.value));

        axis.add(() -> driver.getRawAxis(Axis.kLeftX.value));
        axis.add(() -> driver.getRawAxis(Axis.kLeftY.value));
        axis.add(() -> driver.getRawAxis(Axis.kRightX.value));
        axis.add(() -> driver.getRawAxis(Axis.kRightY.value));
        axis.add(() -> driver.getRawAxis(Axis.kLeftTrigger.value));
        axis.add(() -> driver.getRawAxis(Axis.kRightTrigger.value));
    }

}
