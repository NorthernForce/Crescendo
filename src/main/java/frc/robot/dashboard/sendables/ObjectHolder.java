package frc.robot.dashboard.sendables;


import java.util.function.Function;

import edu.wpi.first.networktables.GenericEntry;
import edu.wpi.first.networktables.NetworkTableValue;

public class ObjectHolder<T> implements AutoCloseable {
    public String name;
    public GenericEntry entry;
    public NetworkTableValue internal;
    public T value;

    /**
   * Package-local constructor.
   *
   * @param name name
   */
    public ObjectHolder(String name) {
        this.name = name;
    }

    @Override
    public void close() {
        if (entry != null) {
            entry.close();
        }
    }

    public void setEntry(GenericEntry entry) {
        this.entry = entry;
    }

    public GenericEntry getEntry(GenericEntry entry) {
        return entry;
    }

    public synchronized void setNetworkTableValue(NetworkTableValue val) {
        this.internal = val;
        updateEntryNetwork(false);
    }

    /**
     * Sets the value of the entry.
     * @param process this is the function to convert from T to {@link NetworkTableValue}.
     * You can use the format `NetworkTableValue::makeYOUR_TYPE_HERE`
     */
    public synchronized void setValue(T val, Function<T, NetworkTableValue> process) {
        this.value = val;
        updateEntry(process, false);
    }

     /**
     * Sets the default value of the entry.
     * @param process this is the function to convert from T to {@link NetworkTableValue}.
     * You can use the format `NetworkTableValue::makeYOUR_TYPE_HERE`
     */
    public synchronized void setDefault(T val, Function<T, NetworkTableValue> process) {
        this.value = val;
        updateEntry(process, true);
    }

    // public synchronized void setValueAtIndex(NetworkTableValue val, int i) {;
    //     if (i < 0 || i >= this.vals.length) {
    //         throw new ArrayIndexOutOfBoundsException("index out of bounds");
    //     }
    //     this.vals[i] = val;
    // }

    public synchronized NetworkTableValue getNetworkTableValue() {
        updateFromEntryNetwork();
        return internal;
    }


    /**
     * Gets the value of the entry.
     * @param process this is the function to convert from {@link NetworkTableValue} to a T.
     * You can use the format `ObjectHolder::getYOUR_TYPE_HERE`
     */
    public synchronized T getValue(Function<NetworkTableValue, T> process) {
        updateFromEntry(process);
        return value;
    }

    // public NetworkTableValue getValueAtIndex(int i) {
    //      if (i < 0 || i >= this.vals.length) {
    //         throw new ArrayIndexOutOfBoundsException("index out of bounds");
    //     }
    //     return vals[i];
    // }


    private synchronized void updateEntry(Function<T, NetworkTableValue> process, boolean setDefault) {
        if (entry == null || value == null) {
            return;
        }

        internal = process.apply(value);

        if (setDefault) {
            entry.setDefault(internal);
        } else {
            entry.set(internal);
        }
    }

    private synchronized void updateEntryNetwork(boolean setDefault) {
        if (entry == null || internal == null) {
            return;
        }

        if (setDefault) {
            entry.setDefault(internal);
        } else {
            entry.set(internal);
        }
    }

    private synchronized void updateFromEntry(Function<NetworkTableValue, T> process) {
        if (entry == null) {
            return;
        }

        internal = entry.get();

        value = process.apply(internal);
    }

    private synchronized void updateFromEntryNetwork() {
        if (entry == null) {
            return;
        }

        internal = entry.get();
    }

    public static boolean getBoolean(NetworkTableValue netVal) {
        return netVal.getBoolean();
    }

    public static long getInteger(NetworkTableValue netVal) {
        return netVal.getInteger();
    }

    public static float getFloat(NetworkTableValue netVal) {
        return netVal.getFloat();
    }

    public static double getDouble(NetworkTableValue netVal) {
        return netVal.getDouble();
    }

    public static String getString(NetworkTableValue netVal) {
        return netVal.getString();
    }

    public static byte[] getRaw(NetworkTableValue netVal) {
        return netVal.getRaw();
    }

    public static boolean[] getBooleanArray(NetworkTableValue netVal) {
        return netVal.getBooleanArray();
    }

    public static long[] getIntegerArray(NetworkTableValue netVal) {
        return netVal.getIntegerArray();
    }

    public static float[] getFloatArray(NetworkTableValue netVal) {
        return netVal.getFloatArray();
    }

    public static double[] getDoubleArray(NetworkTableValue netVal) {
        return netVal.getDoubleArray();
    }

    public static String[] getStringArray(NetworkTableValue netVal) {
        return netVal.getStringArray();
    }
}   
