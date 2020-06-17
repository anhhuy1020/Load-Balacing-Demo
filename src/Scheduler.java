import java.util.*;

/**
 * Class representing a part of an operating system which assigns processes
 * to {@link CPU}s at certain points in time.
 */
public class Scheduler {
    /**
     * A machine which uses this scheduler.
     */
    Machine master;

    /**
     * Schedule queues mapped to CPUs, ready to be assigned.
     */
    Map<CPU, Queue<Schedule>> assignedSchedules;

    /**
     * An internal timer measured in cycles.
     */
    Integer timer;

    /**
     * Class constructor.
     *
     * @param master machine which uses this scheduler
     * @param schedules schedule queues mapped to CPUs
     */
    public Scheduler(Machine master, Set<Queue<Schedule>> schedules) {
        if(master == null || schedules == null) {
            throw new IllegalArgumentException();
        }
        this.master = master;
        assignedSchedules = new HashMap<>();
        timer = 0;
        Iterator<Queue<Schedule>> iter = copySchedules(schedules).iterator();
        for(CPU cpu : master.getCPUSet()) {
            if(iter.hasNext()) {
                assignedSchedules.put(cpu, iter.next());
            }
            else {
                throw new IllegalArgumentException();
            }
        }
    }

    /**
     * Recieve all processes assigned to a CPU in this cycle.
     *
     * @return all processes assigned to a CPU in this cycle
     */
    public Map<CPU, Queue<Process>> getProcesses() {
        Map<CPU, Queue<Process>> map = new HashMap<>();

        boolean resetTimer = false;
        Schedule currentSchedule;
        for(CPU cpu : assignedSchedules.keySet()) {
            Queue<Process> queue = new LinkedList<>();

            if((currentSchedule = assignedSchedules.get(cpu).peek()) != null && currentSchedule.getDelay().equals(timer)) {
                resetTimer = true;
                queue.add(assignedSchedules.get(cpu).poll().getProcess());
                while((currentSchedule = assignedSchedules.get(cpu).peek()) != null && currentSchedule.getDelay().equals(0)) {
                    queue.add(assignedSchedules.get(cpu).poll().getProcess());
                }
            }
            map.put(cpu, queue);
        }
        timer = resetTimer ? 0 : timer + 1;

        return map;
    }

    /**
     * Checks whether the scheduler has finished assigning the schedules.
     *
     * @return <code>true</code> if there are no more schedules in every queue;
     *         <code>false</code> otherwise.
     */
    public boolean isDone() {
        for(CPU cpu : assignedSchedules.keySet()) {
            if(!assignedSchedules.get(cpu).isEmpty()) {
                return false;
            }
        }
        return true;
    }

    /**
     * A static method which copies all schedules in a set.
     *
     * @param set a set to be copied
     * @return a copied set
     */
    public static Set<Queue<Schedule>> copySchedules(Set<Queue<Schedule>> set) {
        Set<Queue<Schedule>> retSet = new HashSet<>();
        for(Queue<Schedule> queue : set) {
            Queue<Schedule> retQueue = new LinkedList<>();
            for(Schedule schedule : queue) {
                retQueue.add(new Schedule(schedule));
            }
            retSet.add(retQueue);
        }
        return retSet;
    }
}