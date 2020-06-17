import java.time.Duration;
import java.time.Period;
import java.util.*;

/**
 * Class representing a single machine, that is the set of {@link CPU}s and an
 * operating system represented by a {@link Scheduler}.
 */
public class Machine {
    /**
     * The CPUs installed in this machine.
     */
    private Set<CPU> CPUSet;

    /**
     * A scheduler being used by this machine to assign schedules.
     */
    private Scheduler scheduler;

    /**
     * A total number of CPU usage requests.
     */
    private Integer usageRequestCount;

    /**
     * A total number of process relocations.
     */
    private Integer relocationCount;

    /**
     * The time of executing
     * */
    private Date timeStart;

    /**
     * The time when all processing is done
     * */
    private Date timeStop;

    /**
     * Class constructor.
     *
     * @param CPUCount amount of CPUs
     * @param algorithm an algorithm to be used
     * @param schedules a set of schedule queues to be assigned to CPUs
     */
    public Machine(Integer CPUCount, Algorithm algorithm, Set<Queue<Schedule>> schedules) {
        if(CPUCount == null || CPUCount <= 0 || algorithm == null || schedules == null) {
            throw new IllegalArgumentException();
        }
        CPUSet = new HashSet<>();
        for(int i = 0; i < CPUCount; i++) {
            CPUSet.add(new CPU(this, algorithm, i));
        }
        scheduler = new Scheduler(this, schedules);
        usageRequestCount = 0;
        relocationCount = 0;
    }

    /**
     * todo
     */
    public void run() {
        timeStart = new Date();
        Map<CPU, Queue<Process>> processQueues = new HashMap<>();
        for(CPU cpu : CPUSet) {
            processQueues.put(cpu, new LinkedList<>());
        }

        Queue<Process> processQueue;

        while(!scheduler.isDone() || !queuesEmpty(processQueues)) {
            Map<CPU, Queue<Process>> currentProcesses = scheduler.getProcesses();
            for(CPU cpu : processQueues.keySet()) {
                processQueues.get(cpu).addAll(currentProcesses.get(cpu));
            }

            for(CPU cpu : CPUSet) {
                processQueue = processQueues.get(cpu);
                while(!processQueue.isEmpty()) {
                    if(!cpu.sendProcess(processQueue.peek())) {
                        break;
                    }
                    processQueue.poll();
                }
                cpu.makeCycle();
            }
        }
        timeStop = new Date();
    }

    /**
     * A static method to determine if all queues in a map are empty.
     *
     * @param processQueues set of queues mapped to CPUs
     * @return <code>true</code> is all queues in a map are empty;
     *         <code>false</code> otherwise.
     */
    private static boolean queuesEmpty(Map<CPU, Queue<Process>> processQueues) {
        for(CPU cpu : processQueues.keySet()) {
            if(!processQueues.get(cpu).isEmpty()) {
                return false;
            }
        }
        return true;
    }

    /**
     * Returns a set of all CPUs installed in this machine.
     *
     * @return a set of all CPUs installed in this machine
     */
    public Set<CPU> getCPUSet() {
        return Collections.unmodifiableSet(CPUSet);
    }

    /**
     * Increases the CPU usage request count.
     */
    public void increaseUsageRequestCount() {
        usageRequestCount++;
    }

    /**
     * Increases the process relocation count by one.
     *
     * @see #increaseRelocationCount(Integer)
     */
    public void increaseRelocationCount() {
        increaseRelocationCount(1);
    }

    /**
     * Increases the process relocation count by a specified amount.
     *
     * @param i a number to be increased by
     */
    public void increaseRelocationCount(Integer i) {
        relocationCount += i;
    }

    /**
     * Returns an average usage of all CPU's.
     *
     * @return average usage of all CPU's
     */
    public Double getAverageUsage() {
        Double totalUsage = 0d;
        for(CPU cpu : CPUSet) {
            totalUsage += cpu.getAverageUsage();
        }
        return totalUsage / CPUSet.size();
    }

    /**
     * Returns an average deviation of every CPU usage.
     *
     * @return average deviation of every CPU usage
     */
    public Double getAverageUsageDeviation() {
        Double averageUsage = getAverageUsage();
        Double deviation = 0d;
        for(CPU cpu : CPUSet) {
            deviation += Math.abs(cpu.getAverageUsage() - averageUsage);
        }
        return deviation / CPUSet.size();
    }

    /**
     * Returns the CPU usage request count.
     *
     * @return CPU usage request count
     */
    public Integer getUsageRequestCount() {
        return usageRequestCount;
    }


    /**
     * Returns the process relocation count.
     *
     * @return process relocation count
     */
    public Integer getRelocationCount() {
        return relocationCount;
    }

    public Long getDuration(){
        return timeStop.getTime() - timeStart.getTime();
    }
}
