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
import edu.wpi.first.wpilibj.XboxController;

public class XboxDisplayer extends JPanel {
    private Image xboxController;
    private int[][] buttonDimensions = { { 266, 119 }, // y
            { 286, 99 }, // a
            { 245, 99 }, // b
            { 266, 79 } }; // x

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
        for (int port = 0; port < 2; port++) {
//            if (joystickHasXboxControls(port)) {
                setTrigs(port, XboxButtons.values(), XboxAxis.values());
            // } else if (joystickHasStadiaControls(port)) {
            //     setTrigs(port, StadiaButtons.values(), StadiaAxis.values());
            // } else if (joystickHasDualsenseControls(port)) {
            //     setTrigs(port, PS5Buttons.values(), PS5Axis.values());
            // } else if (joystickHasPS4Controls(port)) {
            //     setTrigs(port, PS4Buttons.values(), PS4Axis.values());
            // }
        }

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
            if (!joystickConnected(ii)) {
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
            for (int i = 0; i < arrowDimensions.length; i++) {
                g.setColor(trigs.get(i + totalLength).get() ? Color.YELLOW : Color.RED);
                g.fillRect(arrowDimensions[i][0] + change, arrowDimensions[i][1],
                        squareSize.width,
                        squareSize.height);
            }
            totalLength += arrowDimensions.length;


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

    public void setTrigs(int port, Enum<?>[] buttons, Enum<?>[] axes) {
        for (Enum<?> button : buttons) {
            trigs.add(() -> DriverStation.getStickButton(joyCurrent, ((Enum<?>) button).ordinal() + 1));
        }
        for(int i = 0; i < 360; i+=45) 
        {
            final int index = i;
            trigs.add(() -> DriverStation.getStickPOV(port, 0) == index);
        }
        for (Enum<?> axe : axes) {
            axis.add(() -> DriverStation.getStickAxis(joyCurrent, ((Enum<?>) axe).ordinal()));
        }
    }

    /**
     * This is the getter method to get if a controller has the same controls as an
     * Xbox controller. This uses the get name method.
     * 
     * @param port the port for the method to check (0 = driver, 1 = manipulator)
     * @return returns whether the controller has the same controls as an Xbox
     *         controller as a boolean.
     */
    public boolean joystickHasXboxControls(int port) {
        return DriverStation.getJoystickName(port).contains("Xbox")
                || DriverStation.getJoystickName(port).contains("Logitech");
    }

    /**
     * This is the getter method to get if a controller has the same controls as a
     * Stadia controller. This uses the get name method.
     * 
     * @param port the port for the method to check (0 = driver, 1 = manipulator)
     * @return returns whether the controller has the same controls as a Stadia
     *         controller as a boolean.
     */
    public boolean joystickHasStadiaControls(int port) {
        return DriverStation.getJoystickName(port).contains("Stadia");
    }

    /**
     * This is the getter method to get if a controller has the same controls as a
     * Dualsense controller. This uses the get name method.
     * 
     * @param port the port for the method to check (0 = driver, 1 = manipulator)
     * @return returns whether the controller has the same controls as a PS5
     *         controller (Dualsense) as a boolean.
     */
    public boolean joystickHasDualsenseControls(int port) {
        return DriverStation.getJoystickName(port).contains("Dualsense");
    }
    /**
     * This is the getter method to get if a controller has the same controls as a
     * Dualsense controller. This uses the get name method.
     * 
     * @param port the port for the method to check (0 = driver, 1 = manipulator)
     * @return returns whether the controller has the same controls as a PS5
     *         controller (Dualsense) as a boolean.
     */
    public boolean joystickHasPS4Controls(int port) {
        return DriverStation.getJoystickName(port).contains("Dualsense");
    }

    /**
     * This is the getter method to check if a controller is connected to a port.
     * 
     * @param port the port for the method to check (0 = driver, 1 = manipulator)
     * @return returns whether a controller is connected to a port as a boolean
     */
    public boolean joystickConnected(int port) {
        return !DriverStation.getJoystickName(port).equals("");
    }

    public enum XboxButtons {
        /** A. */
        kA(1),
        /** B. */
        kB(2),
        /** X. */
        kX(3),
        /** Y. */
        kY(4),
        /** Left bumper. */
        kLeftBumper(5),
        /** Right bumper. */
        kRightBumper(6),
        /** Left stick. */
        kLeftStick(9),
        /** Right stick. */
        kRightStick(10),
        /** Back. */
        kBack(7),
        /** Start. */
        kHamburger(8);

        /** Button value. */
        public final int value;

        XboxButtons(int value) {
            this.value = value;
        }

    }

    public enum XboxAxis {
        /** Left X. */
        kLeftX(0),
        /** Right X. */
        kRightX(4),
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

        XboxAxis(int value) {
            this.value = value;
        }
    }

    public enum PS5Buttons {
        /** Square button. */
        kX(1),
        /** X button. */
        kA(2),
        /** Circle button. */
        kB(3),
        /** Triangle button. */
        kY(4),
        /** Left trigger 1 button. */
        kLeftBumper(5),
        /** Right trigger 1 button. */
        kRightBumper(6),
        /** Left trigger 2 button. */
        kLeftTrigger(7),
        /** Right trigger 2 button. */
        kRightTrigger(8),
        /** Create button. */
        kBack(9),
        /** Options button. */
        kHamburger(10),
        /** Left stick button. */
        kLeftStick(11),
        /** Right stick button. */
        kRightStick(12),
        /** PlayStation button. */
        kHome(13),
        /** Touchpad click button. */
        kTouchpad(14);

        /** Button value. */
        public final int value;

        PS5Buttons(int index) {
            this.value = index;
        }
    }

    public enum PS5Axis {
        /** Left X axis. */
        kLeftX(0),
        /** Left Y axis. */
        kLeftY(1),
        /** Right X axis. */
        kRightX(2),
        /** Right Y axis. */
        kRightY(5),
        /** Left Trigger 2. */
        kLeftTrigger(3),
        /** Right Trigger 2. */
        kRightTrigger(4);

        /** Axis value. */
        public final int value;

        PS5Axis(int index) {
            value = index;
        }
    }

    public enum PS4Buttons {
        /** Square button. */
        kX(1),
        /** X button. */
        kA(2),
        /** Circle button. */
        kB(3),
        /** Triangle button. */
        kY(4),
        /** Left Trigger 1 button. */
        kLeftBumper(5),
        /** Right Trigger 1 button. */
        kRightBumper(6),
        /** Left Trigger 2 button. */
        kLeftTrigger(7),
        /** Right Trigger 2 button. */
        kRightTrigger(8),
        /** Share button. */
        kBack(9),
        /** Option button. */
        kHamburger(10),
        /** Left stick button. */
        kLeftStick(11),
        /** Right stick button. */
        kRightStick(12),
        /** PlayStation button. */
        kHome(13),
        /** Touchpad click button. */
        kTouchpad(14);

        /** Button value. */
        public final int value;

        PS4Buttons(int index) {
            this.value = index;
        }
    }

    public enum PS4Axis {
        /** Left X axis. */
        kLeftX(0),
        /** Left Y axis. */
        kLeftY(1),
        /** Right X axis. */
        kRightX(2),
        /** Right Y axis. */
        kRightY(5),
        /** Left Trigger 2. */
        kLeftTrigger(3),
        /** Right Trigger 2. */
        kRightTrigger(4);

        /** Axis value. */
        public final int value;

        PS4Axis(int index) {
            value = index;
        }
    }

    public enum StadiaButtons {
        /** A button. */
        kA(1),
        /** B button. */
        kB(2),
        /** X Button. */
        kX(3),
        /** Y Button. */
        kY(4),
        /** Left bumper button. */
        kLeftBumper(5),
        /** Right bumper button. */
        kRightBumper(6),
        /** Left stick button. */
        kLeftStick(7),
        /** Right stick button. */
        kRightStick(8),
        /** Ellipses button. */
        kBack(9),
        /** Hamburger button. */
        kHamburger(10),
        /** Stadia button. */
        kHome(11),
        /** Right trigger button. */
        kRightTrigger(12),
        /** Left trigger button. */
        kLeftTrigger(13),
        /** Google button. */
        kGoogle(14),
        /** Frame button. */
        kFrame(15);

        /** Button value. */
        public final int value;

        StadiaButtons(int value) {
            this.value = value;
        }
    }

    public enum StadiaAxis {
        /** Left X axis. */
        kLeftX(0),
        /** Right X axis. */
        kRightX(3),
        /** Left Y axis. */
        kLeftY(1),
        /** Right Y axis. */
        kRightY(4);

        /** Axis value. */
        public final int value;

        StadiaAxis(int value) {
            this.value = value;
        }
    }
}
