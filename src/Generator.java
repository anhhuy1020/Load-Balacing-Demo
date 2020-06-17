import java.util.*;

/**
 * This class contains only the static method for generating {@link Schedule}
 * queues used by a {@link Scheduler}.
 */
public class Generator {
    /**
     * Generates a set of schedule queues specified by the parameters.
     *
     * @param CPUCount amount of generated schedule queues
     * @param minLength the minimum length of a queue
     * @param maxLength the maximum length of a queue
     * @param minUsage the minimum usage of a process
     * @param maxUsage the maximum usage of a process
     * @param minDelay the minimum delay of a schedule
     * @param maxDelay the maximum delay of a schedule
     * @param minBurstTime the minimum burst time of a process
     * @param maxBurstTime the maximum burst time of a process
     * @return the set of generated schedule queues
     */
    public static Set<Queue<Schedule>> generate(Integer CPUCount,
                                                Integer minLength, Integer maxLength,
                                                Double minUsage, Double maxUsage,
                                                Integer minDelay, Integer maxDelay,
                                                Integer minBurstTime, Integer maxBurstTime) {
        if(maxLength - minLength < 0 || maxUsage - minUsage < 0 || maxDelay - minDelay < 0 || maxBurstTime - minBurstTime < 0) {
            throw new IllegalArgumentException();
        }
        Random random = new Random();
        Set<Queue<Schedule>> set = new HashSet<>();
        int id = 0;

        for(int i = 0; i < CPUCount; i++) {
            Queue<Schedule> queue = new LinkedList<>();
            Integer length = minLength + random.nextInt(maxLength - minLength);

            for(int j = 0; j < length; j++) {
                Double usage = minUsage + random.nextDouble() * (maxUsage - minUsage);
                Integer delay = minDelay + random.nextInt(maxDelay - minDelay);
                Integer burstTime = minBurstTime + random.nextInt(maxBurstTime - minBurstTime);

                queue.add(new Schedule(new Process(id++, usage, burstTime), delay));
            }

            set.add(queue);
        }

        return set;
    }
}