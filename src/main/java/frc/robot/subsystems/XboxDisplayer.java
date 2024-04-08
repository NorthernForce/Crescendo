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

import edu.wpi.first.hal.DriverStationJNI;
import edu.wpi.first.wpilibj.DriverStation;

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
    private int[][] flushDimensionsDriver = joystickHasStadiaControls(1)
            ? new int[][] { { 160, 90 }, { 220, 90 }, { 170, 105 }, { 210, 105 } }
            : new int[][] { { 160, 90 }, { 220, 90 } };

    private int[][] flushDimensionsManipulator = joystickHasStadiaControls(0)
            ? new int[][] { { 160, 90 }, { 220, 90 }, { 170, 105 }, { 210, 105 } }
            : new int[][] { { 160, 90 }, { 220, 90 } };
    private static JFrame frame;
    private static final String TITLE = "Key Logger ( >:) XDXDXDXDXXDXDXDXD OLOLOLOLOLOLOLO HEHEHEHEHHHEHHEHEEHE )";
    private ArrayList<Supplier<Double>> axis = new ArrayList<>();
    private ArrayList<Supplier<Boolean>> trigs = new ArrayList<>();
    private int joyCurrent;
    private Supplier<int[][]> flushDimensions = ((Supplier<int[][]>) () -> {
        return joyCurrent == 1 ? flushDimensionsDriver : flushDimensionsManipulator;
    });

    private enum JoystickButtons {
        /** Left bumper. */
        kLeftBumper(5),
        /** Right bumper. */
        kRightBumper(6),
        /** Left stick. */
        kLeftStick(9),
        /** Right stick. */
        kRightStick(10),
        /** A. */
        kA(1),
        /** B. */
        kB(2),
        /** X. */
        kX(3),
        /** Y. */
        kY(4),
        /** Back. */
        kBack(7),
        /** Left stick button. */
        kLeftStickStadia(7),
        /** Right stick button. */
        kRightStickStadia(8),
        /** Ellipses button. */
        kEllipses(9),
        /** Hamburger button. */
        kHamburger(10),
        /** Stadia button. */
        kStadia(11),
        /** Google button. */
        kGoogle(14),
        /** Frame button. */
        kFrame(15),
        /** Start. */
        kStart(8),
        /** Right trigger button. */
        kRightTriggerStadia(12),
        /** Left trigger button. */
        kLeftTriggerStadia(13);

        public final int value;

        JoystickButtons(int value) {
            this.value = value;
        }

    }

    private enum JoystickAxis {
        /** Left X. */
        kLeftX(0),
        /** Right Y axis. */
        kRightYStadia(4),
        /** Right X. */
        kRightX(4),
        /** Right X axis. */
        kRightXStadia(3),
        /** Left Y. */
        kLeftY(1),
        /** Right Y. */
        kRightY(5),
        /** Left trigger. */
        kLeftTrigger(2),
        /** Right trigger. */
        kRightTrigger(3);

        /** Axis value. */
        public final int value;

        JoystickAxis(int value) {
            this.value = value;
        }

    }

    public XboxDisplayer() {
        xboxFile = new File("images/xbox_no_back.png");
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
        // currentController = new CommandXboxController(0);

        // else if(currentControllerStation.get)
        // {

        // }
        frame.add(this);
        frame.setVisible(true);
        setTrigs(0);
        setTrigs(1);
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

        // buttons
        for (int ii = 0; ii < 2; ii++) {
            int totalLength = 0;
            if (DriverStation.getJoystickName(ii).equals("")) {
                if (ii == 1) {
                    break;
                } else {
                    ii++;
                }
            }
            joyCurrent = ii;
            int change = ii == 0 ? 0 : 500;
            if (xboxController != null) {
                g.drawImage(xboxController, 50 + change, 50, 300, 200, this);
            }

            // System.out.println(
            // "For " + "i " + "equaling " + i + ", range[0] = " + iRange[0] + " and
            // range[1] = " + iRange[1]);
            // if(DriverStation.getStickButton(joyCurrent, Button.kA.value))
            // System.out.println("A
            // pressed");
            for (int i = 0; i < buttonDimensions.length; i++) {
                g.setColor(trigs.get(i + totalLength).get() ? Color.YELLOW : Color.RED);
                g.fillOval(buttonDimensions[i][0] + change, buttonDimensions[i][1], circleSize, circleSize);
            }
            totalLength += buttonDimensions.length;
            for (int i = 0; i < bumperDimensions.length; i++) {
                g.setColor(trigs.get(i + totalLength).get() ? Color.YELLOW : Color.RED);
                g.fillRect(bumperDimensions[i][0] + change, bumperDimensions[i][1],
                        (int) bumperDimension.height, (int) bumperDimension.width);

            }
            totalLength += bumperDimensions.length;
            for (int i = 0; i < arrowDimensions.length; i++) {
                g.setColor(trigs.get(i + totalLength).get() ? Color.YELLOW : Color.RED);
                g.fillRect(arrowDimensions[i][0] + change, arrowDimensions[i][1],
                        squareSize.width,
                        squareSize.height);
            }
            totalLength += arrowDimensions.length;
            int totalAxisLength = 0;
            for (int i = 0; i < joySticksDimensions.length; i++) {
                g.setColor(trigs.get(i + totalLength).get() ? Color.YELLOW : Color.RED);
                g.fillOval(
                        joySticksDimensions[i][0] + change
                                + (int) ((double) (axis.get(totalAxisLength + 2 * i).get()) * 7),
                        joySticksDimensions[i][1]
                                + (int) ((double) (axis.get(totalAxisLength + 1 + 2 * i).get()) * 7),
                        joystickSize, joystickSize);

            }
            totalAxisLength += joySticksDimensions.length * 2;
            totalLength += joySticksDimensions.length;

            Graphics2D g2d = (Graphics2D) g;
            float stroke = 2.0f;
            Color orig = g2d.getColor();
            for (int i = 0; i < triggerDimensions.length; i++) {
                g2d.setColor(Color.BLACK);
                g2d.setStroke(new BasicStroke(stroke));
                g2d.drawRect(triggerDimensions[i][0] + change, triggerDimensions[i][1],
                        (int) triggerDimension.height, (int) triggerDimension.width);
                g2d.setColor(axis.get(i + totalAxisLength).get() > 0.5 ? Color.YELLOW : Color.RED);

                g2d.fillRect((int) (triggerDimensions[i][0] + change + (int) stroke / 2),
                        (int) ((triggerDimension.width + (triggerDimensions[i][1]) + (int) stroke / 2
                                + (1 - (triggerDimensions[i][1] + triggerDimension.width))
                                        * axis.get(i + totalAxisLength).get())
                                - (int) stroke / 2
                                + (triggerDimensions[i][1]) * axis.get(i + totalAxisLength).get()),
                        (int) (triggerDimension.height - (int) stroke / 2),
                        (int) ((triggerDimension.width + triggerDimensions[i][1])
                                - (((triggerDimension.width + (triggerDimensions[i][1])
                                        + (int) stroke / 2
                                        + (1 - (triggerDimensions[i][1] + triggerDimension.width))
                                                * axis.get(i + totalAxisLength).get())
                                        - (int) stroke / 2
                                        + (triggerDimensions[i][1]) * axis.get(i + totalAxisLength).get()))));
                g2d.setColor(orig);

            }
            for (int i = 0; i < flushDimensions.get().length; i++) {
                g.setColor(trigs.get(i + totalLength).get() ? Color.YELLOW : Color.RED);
                g.fillRect(flushDimensions.get()[i][0] + change, flushDimensions.get()[i][1],
                        flushDimension.height, flushDimension.width);
            }

        }
        Color orig = g.getColor();
        g.setColor(Color.BLACK);
        g.fillRect(440, 0, 10, frame.getHeight());
        g.setFont(new Font("arial", Font.BOLD, 30));
        g.drawString("Driver " + (joystickConnected(0) ? "(Connected)" : "(Not connected)"), 10, 30);
        g.drawString("Manipulator " + (joystickConnected(1) ? "(Connected)" : "(Not connected)"), 460,
                30);
        g.setColor(orig);

        // joysticks
        DriverStationJNI.setJoystickOutputs((byte) 0, 1, (short) 0, (short) 0);
    }

    public void setTrigs(int port) {

        trigs.add(() -> DriverStation.getStickButton(joyCurrent, JoystickButtons.kA.value));
        trigs.add(() -> DriverStation.getStickButton(joyCurrent, JoystickButtons.kY.value));
        trigs.add(() -> DriverStation.getStickButton(joyCurrent, JoystickButtons.kX.value));
        trigs.add(() -> DriverStation.getStickButton(joyCurrent, JoystickButtons.kB.value));

        trigs.add(() -> DriverStation.getStickButton(joyCurrent, JoystickButtons.kLeftBumper.value));
        trigs.add(() -> DriverStation.getStickButton(joyCurrent, JoystickButtons.kRightBumper.value));

        trigs.add(() -> DriverStation.getStickPOV(joyCurrent, 0) == 180);
        trigs.add(() -> DriverStation.getStickPOV(joyCurrent, 0) == 90);
        trigs.add(() -> DriverStation.getStickPOV(joyCurrent, 0) == 270);
        trigs.add(() -> DriverStation.getStickPOV(joyCurrent, 0) == 0);
        trigs.add(() -> DriverStation.getStickPOV(joyCurrent, 0) == 135);
        trigs.add(() -> DriverStation.getStickPOV(joyCurrent, 0) == 315);
        trigs.add(() -> DriverStation.getStickPOV(joyCurrent, 0) == 45);
        trigs.add(() -> DriverStation.getStickPOV(joyCurrent, 0) == 225);

        axis.add(() -> DriverStation.getStickAxis(joyCurrent, JoystickAxis.kLeftX.value));
        axis.add(() -> DriverStation.getStickAxis(joyCurrent, JoystickAxis.kLeftY.value));

        if (joystickHasStadiaControls(port)) {
            axis.add(() -> DriverStation.getStickAxis(joyCurrent, JoystickAxis.kRightXStadia.value));
            axis.add(() -> DriverStation.getStickAxis(joyCurrent, JoystickAxis.kRightYStadia.value));
            axis.add(() -> (double) (DriverStation.getStickButton(joyCurrent, JoystickButtons.kLeftTriggerStadia.value)
                    ? 1
                    : 0));
            axis.add(() -> (double) (DriverStation.getStickButton(joyCurrent, JoystickButtons.kRightTriggerStadia.value)
                    ? 1
                    : 0));

        }

        if (joystickHasXboxControls(port)) {
            axis.add(() -> DriverStation.getStickAxis(joyCurrent, JoystickAxis.kRightX.value));
            axis.add(() -> DriverStation.getStickAxis(joyCurrent, JoystickAxis.kRightY.value));
            axis.add(() -> DriverStation.getStickAxis(joyCurrent, JoystickAxis.kLeftTrigger.value));
            axis.add(() -> DriverStation.getStickAxis(joyCurrent, JoystickAxis.kRightTrigger.value));
        }

        if (joystickHasXboxControls(port)) {
            trigs.add(() -> DriverStation.getStickButton(joyCurrent, JoystickButtons.kLeftStick.value));
            trigs.add(() -> DriverStation.getStickButton(joyCurrent, JoystickButtons.kRightStick.value));
            trigs.add(() -> DriverStation.getStickButton(joyCurrent, JoystickButtons.kBack.value));
            trigs.add(() -> DriverStation.getStickButton(joyCurrent, JoystickButtons.kStart.value));
        }

        if (joystickHasStadiaControls(port)) {
            trigs.add(() -> DriverStation.getStickButton(joyCurrent, JoystickButtons.kLeftStickStadia.value));
            trigs.add(() -> DriverStation.getStickButton(joyCurrent, JoystickButtons.kRightStickStadia.value));
            trigs.add(() -> DriverStation.getStickButton(joyCurrent, JoystickButtons.kEllipses.value));
            trigs.add(() -> DriverStation.getStickButton(joyCurrent, JoystickButtons.kHamburger.value));
            trigs.add(() -> DriverStation.getStickButton(joyCurrent, JoystickButtons.kGoogle.value));
            trigs.add(() -> DriverStation.getStickButton(joyCurrent, JoystickButtons.kFrame.value));
        }

    }

    public boolean joystickHasXboxControls(int port) {
        return DriverStation.getJoystickName(port).contains("Xbox")
                || DriverStation.getJoystickName(port).contains("Logitech");
    }

    public boolean joystickHasStadiaControls(int port) {
        return DriverStation.getJoystickName(port).contains("Stadia");
    }

    public boolean joystickConnected(int port) {
        return !DriverStation.getJoystickName(port).equals("");
    }
}
