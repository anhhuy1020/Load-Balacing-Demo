/**
 * An abstract class used by {@link Machine}s for balancing the load on
 * {@link CPU}s installed in them.
 */
public abstract class Algorithm {
    /**
     * Handles the process on the specified CPU to balance the load.
     * <p>By default the method adds the process to the current CPU.
     *
     * @param master CPU on which we perform this action
     * @param process process to handle
     * @return <code>true</code> if the process has been handled successfully;
     *         <code>false</code> otherwise.
     */
    public boolean handleProcess(CPU master, Process process) {
        master.addProcess(process);
        return true;
    }
}