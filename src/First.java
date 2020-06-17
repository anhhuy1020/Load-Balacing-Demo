import java.util.HashSet;
import java.util.Set;

/**
 * A {@link Process} appears on {@link CPU} <i>x</i>. It is handled the same way
 * as in algorithm #2, but additionally CPUs with usage lower than some minimal
 * threshold <i>r</i> ask randomly chosen CPUs for usage and if it exceeds
 * <i>p</i>, the asking CPU takes a portion of its processes.
 */
public final class First extends Algorithm {

    public First( ) {
    }

    @Override
    public boolean handleProcess(CPU master, Process process) {
        master.addProcess(process);
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return "Algorithm #1";
    }
}