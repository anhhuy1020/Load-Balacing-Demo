/**
 * Representation of a process with an identifier, CPU usage in percent
 * and the time required to complete the process.
 */
public final class Process {
    /**
     * Process identifier.
     */
    private Integer PID;

    /**
     * Usage of CPU's resources.
     */
    private Double usage;

    /**
     * Amount of CPU cycles required to burst this process.
     */
    private Integer burstTime;

    /**
     * Class constructor.
     *
     * @param PID a unique identification number
     * @param usage a number between 0 and 1 specifying the usage of CPU's
     *              resources in percent
     * @param burstTime an integer specifying the amount of CPU cycles required
     *                  to burst the process
     * @throws IllegalArgumentException If the usage is not between 0 and 1 or
     *                                  the burst time is negative
     */
    public Process(Integer PID, Double usage, Integer burstTime) {
        if(PID == null || usage == null || burstTime == null || usage < 0 || usage > 1 || burstTime <= 0) {
            throw new IllegalArgumentException();
        }
        this.PID = PID;
        this.usage = usage;
        this.burstTime = burstTime;
    }

    /**
     * Copying constructor.
     *
     * @param process a process to be copied
     */
    public Process(Process process) {
        this(process.PID, process.usage, process.burstTime);
    }

    /**
     * Returns this process' usage.
     *
     * @return this process' usage
     */
    public Double getUsage() {
        return usage;
    }

    /**
     * Decreases the remaining time and returns the status of a process.
     *
     * @return <code>true</code> if the process is finished and is supposed
     *         to be removed; <code>false</code> otherwise.
     */
    public boolean execute() {
        return --burstTime == 0;
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
        Process process = (Process) o;
        return PID.equals(process.PID);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return PID.hashCode();
    }
}