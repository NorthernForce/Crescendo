package frc.robot.utils;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryUsage;

public class MemoryCalculator {
    public static double[] calcStorage(boolean print)
    {
        MemoryMXBean memoryBean = ManagementFactory.getMemoryMXBean();
        MemoryUsage heapMemoryUsage = memoryBean.getHeapMemoryUsage();
        MemoryUsage nonHeapMemoryUsage = memoryBean.getNonHeapMemoryUsage();
        
        if(print)
        {
            System.out.println("Heap Memory Usage: " + heapMemoryUsage);
            System.out.println("Non-Heap Memory Usage: " + nonHeapMemoryUsage);
        }
        double[] result = new double[2];
        result[0] = heapMemoryUsage.getCommitted();
        result[1] = nonHeapMemoryUsage.getCommitted();

        return result;
        
    }
}
