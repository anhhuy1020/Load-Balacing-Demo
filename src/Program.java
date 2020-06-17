import java.text.DecimalFormat;
import java.util.Queue;
import java.util.Scanner;
import java.util.Set;


public class Program {
    /**
     * The main method, which takes a user command and performs a specified function.
     * <p>Available commands:
     * <ul>
     *     <li><code>run</code> - runs the simulation with specified parameters
     *     <li><code>N</code> - sets the CPU count
     *     <li><code>r</code> - sets the lower threshold
     *     <li><code>p</code> - sets the upper threshold
     *     <li><code>z</code> - sets the attempt count
     *     <li><code>portion</code> - sets the share portion
     *     <li><code>length</code> - sets the process queue minimum and maximum length
     *     <li><code>usage</code> - sets the minimum and maximum usage of a process
     *     <li><code>delay</code> - sets the minimum and maximum delay of a schedule
     *     <li><code>bursttime</code> - sets the minimum and maximum burst time
     *                                  of a process
     *     <li><code>params</code> - displays the current parameters
     *     <li><code>help</code> - displays all available commands
     *     <li><code>quit</code> - exits the program
     * </ul>
     *
     * @param args none
     */
    public static void main(String[] args) {
        Integer CPUCount = 50;
        Double upperThreshold = 0.6;

        Integer minLength = 500;
        Integer maxLength = 1000;
        Double minUsage = 0.03;
        Double maxUsage = 0.1;
        Integer minDelay = 0;
        Integer maxDelay = 2;
        Integer minBurstTime = 10;
        Integer maxBurstTime = 20;

        Scanner scanner = new Scanner(System.in);
        String string = scanner.next();

        while(!string.equals("quit")) {
            switch(string) {
                case "run":
                    Set<Queue<Schedule>> schedules = Generator.generate(CPUCount, minLength, maxLength, minUsage, maxUsage,
                            minDelay, maxDelay, minBurstTime, maxBurstTime);

                    Algorithm[] algorithms = { new First(),
                            new Second(upperThreshold),
                            new Third(upperThreshold)};

                    for(Algorithm algorithm : algorithms) {
                        System.out.println(algorithm + ":");
                        Machine machine = new Machine(CPUCount, algorithm, schedules);
                        machine.run();
                        System.out.println("\tAverage usage: " + formatPercent(machine.getAverageUsage()));
                        System.out.println("\tAverage usage deviation: " + formatPercent(machine.getAverageUsageDeviation()));
                        System.out.println("\tUsage request count: " + machine.getUsageRequestCount());
                        System.out.println("\tRelocation count: " + machine.getRelocationCount());
                        System.out.println("\tDuration (millis): " + machine.getDuration());
                    }
                    break;
                case "N":
                    CPUCount =  scanner.nextInt();;
                    System.out.println("CPU count set to " + CPUCount);
                    break;
                case "p":
                    upperThreshold = scanner.nextDouble();
                    System.out.println("Upper threshold set to " + upperThreshold);
                    break;
                case "length":
                    minLength = scanner.nextInt();
                    maxLength = scanner.nextInt();
                    System.out.println("Length set to (" + minLength + ", " + maxLength + ")");
                    break;
                case "usage":
                    minUsage = scanner.nextDouble();
                    maxUsage = scanner.nextDouble();
                    System.out.println("Length set to (" + formatPercent(minUsage) + ", " + formatPercent(maxUsage) + ")");
                    break;
                case "delay":
                    minDelay = scanner.nextInt();
                    maxDelay = scanner.nextInt();
                    System.out.println("Delay set to (" + minDelay + ", " + maxDelay + ")");
                    break;
                case "bursttime":
                    minBurstTime = scanner.nextInt();
                    maxBurstTime = scanner.nextInt();
                    System.out.println("Burst time set to (" + minBurstTime + ", " + maxBurstTime + ")");
                    break;
                case "params":
                    System.out.println("N: " + CPUCount);
                    System.out.println("p: " + upperThreshold);
                    break;
                case "help":
                    System.out.println("Available commands: run, N, r, p, z, portion, length, usage, delay, bursttime, params");
                    break;
                default:
                    System.out.println("Unknown command");
                    break;
            }

            string = scanner.next();
        }

        scanner.close();
    }


    /**
     * Basically a class converting a number to a percentage with a percent sign.
     *
     * @param d number to be converted
     * @return representation of a percentage
     */
    public static String formatPercent(Double d) {
        return new DecimalFormat("#.##").format(d * 100) + "%";
    }
}