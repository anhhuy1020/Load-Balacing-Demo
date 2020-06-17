/**
 * A class used by a scheduler for scheduling processes to CPUs. Contains
 * a {@link Process} and a delay in CPU cycles.
 */
public final class Schedule {
    /**
     * A process to be scheduled.
     */
    private Process process;

    /**
     * The delay of the schedule.
     */
    private Integer delay;

    /**
     * Class constructor.
     *
     * @param process a process to be scheduled
     * @param delay the amount of CPU cycles until this process is scheduled after
     *              the previous one
     * @see Process
     */
    public Schedule(Process process, Integer delay) {
        if(process == null || delay == null) {
            throw new IllegalArgumentException();
        }
        this.process = process;
        this.delay = delay;
    }

    /**
     * Copying constructor.
     *
     * @param schedule a schedule to be copied
     */
    public Schedule(Schedule schedule) {
        this(new Process(schedule.process), schedule.delay);
    }

    /**
     * Returns this schedule's process.
     *
     * @see Process
     */
    public Process getProcess() {
        return process;
    }

    /**
     * Returns the delay of this schedule.
     */
    public Integer getDelay() {
        return delay;
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
        Schedule schedule = (Schedule) o;
        return process.equals(schedule.process) && delay.equals(schedule.delay);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        int result = process.hashCode();
        result = 31 * result + delay.hashCode();
        return result;
    }
}
