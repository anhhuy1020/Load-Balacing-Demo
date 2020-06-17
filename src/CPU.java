

import java.util.*;

/**
 * Representation of a central processing unit, which executes processes
 * assigned to it.
 */
public class CPU {
    /**
     * The {@link Machine} in which this CPU is located.
     */
    private Machine master;

    /**
     * The {@link Algorithm} being used by this CPU to balance the load.
     */
    private Algorithm algorithm;

    /**
     * CPU's identifier.
     */
    private Integer ID;

    /**
     * The set of currently running processes.
     */
    private Set<Process> processSet;

    /**
     * The set of ready processes waiting to be dispatched.
     */
    private Queue<Process> awaitingProcesses;

    /**
     * The sum of every CPU usage calculated every cycle.
     */
    private Double totalUsage;

    /**
     * The amount of cycles passed.
     */
    private Integer cycleCount;

    /**
     * Class constructor.
     *
     * @param master The {@link Machine} in which this CPU is located.
     * @param algorithm The {@link Algorithm} being used by this CPU to
     *                  balance the load.
     * @param ID CPU's identifier.
     */
    public CPU(Machine master, Algorithm algorithm, Integer ID) {
        if(master == null || algorithm == null || ID == null) {
            throw new IllegalArgumentException();
        }
        this.master = master;
        this.algorithm = algorithm;
        this.ID = ID;
        processSet = new HashSet<>();
        awaitingProcesses = new LinkedList<>();
        totalUsage = 0d;
        cycleCount = 0;
    }

    /**
     * Dispatches the process on this CPU only if there's enough resources
     * available, the process is moved to the ready queue otherwise.
     *
     * @param process the process to be added
     */
    public void addProcess(Process process) {
        if(getUsage() + process.getUsage() <= 1) {
            processSet.add(process);
            return;
        }
        awaitingProcesses.add(process);
    }

    /**
     * Dispatches multiple processes sequentially.
     *
     * @param processes a collection of processes to be added
     * @see #addProcess(Process)
     */
    public void addProcesses(Collection<Process> processes) {
        for(Process process : processes) {
            addProcess(process);
        }
    }

    /**
     * Dispatches all processes in the ready queue for which there are enough
     * resources available.
     */
    private void addAwaitingProcesses() {
        while(awaitingProcesses.peek() != null && getUsage() + awaitingProcesses.peek().getUsage() <= 1) {
            algorithm.handleProcess(this, awaitingProcesses.poll());
        }
    }

    /**
     * Returns this CPU's usage.
     *
     * @return this CPU's usage
     */
    public Double getUsage() {
        Double totalUsage = 0d;
        for(Process process : processSet) {
            totalUsage += process.getUsage();
        }
        return totalUsage;
    }

    /**
     * Uses this CPU's algorithm to balance the load.
     *
     * @param process the process to be sent
     * @return <code>true</code> if the process has been added successfully;
     *         <code>false</code> otherwise.
     */
    public boolean sendProcess(Process process) {
        return algorithm.handleProcess(this, process);
    }

    /**
     * Makes one CPU cycle. Calculated the usage and executes all running
     * processes once.
     *
     * @see #burstProcesses()
     */
    public void makeCycle() {
        cycleCount++;
        totalUsage += getUsage();
        burstProcesses();
    }

    /**
     * Attempts to burst all running processes by executing them once. Uses this
     * CPU's algorithm's step method if it's defined. Dispatches processes in the
     * ready queue.
     *
     * @see #addAwaitingProcesses()
     */
    private void burstProcesses() {
        boolean freedSpace = false;
        Iterator<Process> iter = processSet.iterator();
        while(iter.hasNext()) {
            if(iter.next().execute()) {
                iter.remove();
                freedSpace = true;
            }
        }
        if(freedSpace) {
            addAwaitingProcesses();
        }
    }

    /**
     * Returns the machine in which this CPU is located.
     *
     * @return the machine in which this CPU is located.
     */
    public Machine getMaster() {
        return master;
    }

    /**
     * Returns this CPU's average usage.
     *
     * @return average usage
     */
    public Double getAverageUsage() {
        return totalUsage / cycleCount;
    }

    /**
     * A static method which selects a random CPU in the specified set.
     *
     * @param set a set from which the random CPU is selected
     * @return a random CPU in the {@code set}
     */
    public static CPU randomCPU(Set<CPU> set) {
        int item = new Random().nextInt(set.size());
        int i = 0;
        for(CPU cpu : set) {
            if (i == item) {
                return cpu;
            }
            i++;
        }
        return null;
    }

    /**
     * Shares a specified portion of all processes in this CPU.
     *
     * @param portion a number between 0 and 1 specifying the portion of all
     *                processes in this CPU to be shared
     * @return set of all shared processes
     */
    public Set<Process> getProcessPortion(Double portion) {
        Set<Process> set = new HashSet<>();
        set.addAll(Collections.unmodifiableSet(processSet));
        set.addAll(Collections.unmodifiableSet(new HashSet<>(awaitingProcesses)));
        List<Process> list = new LinkedList<>(set);
        Collections.shuffle(list);
        Set<Process> randomSet = new HashSet<>(list.subList(0, Double.valueOf(list.size() * portion).intValue()));
        processSet.removeAll(randomSet);
        awaitingProcesses.removeAll(randomSet);
        return set;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object o) {
        if(this == o) {
            return true;
        }
        if(o == null || getClass() != o.getClass()) {
            return false;
        }
        CPU cpu = (CPU) o;
        return ID.equals(cpu.ID);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return ID.hashCode();
    }
}