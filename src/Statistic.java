import netscape.javascript.JSObject;
import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.Queue;
import java.util.Scanner;
import java.util.Set;

public class Statistic {

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

        int startN = 10;
        int endN = 100;
        int interval = 10;

        JSONArray averageUsages = new JSONArray();
        JSONArray averageUsageDeviations = new JSONArray();
        JSONArray usageRequestCounts = new JSONArray();
        JSONArray relocationCounts = new JSONArray();
        JSONArray durations = new JSONArray();

        try {
            for (CPUCount = startN; CPUCount <= endN; CPUCount += interval) {
                System.out.println("Time " + CPUCount/interval);
                Set<Queue<Schedule>> schedules = Generator.generate(CPUCount, minLength, maxLength, minUsage, maxUsage,
                        minDelay, maxDelay, minBurstTime, maxBurstTime);

                Algorithm[] algorithms = {new First(),
                        new Second(upperThreshold),
                        new Third(upperThreshold)};

                JSONObject averageUsage = new JSONObject();
                JSONObject averageUsageDeviation = new JSONObject();
                JSONObject usageRequestCount = new JSONObject();
                JSONObject relocationCount = new JSONObject();
                JSONObject duration = new JSONObject();

                averageUsage.put("category", CPUCount);
                averageUsageDeviation.put("category", CPUCount);
                usageRequestCount.put("category", CPUCount);
                relocationCount.put("category", CPUCount);
                duration.put("category", CPUCount);

                for (Algorithm algorithm : algorithms) {
                    Machine machine = new Machine(CPUCount, algorithm, schedules);
                    machine.run();


                    averageUsage.put(algorithm.toString(), formatPercent(machine.getAverageUsage()));
                    averageUsageDeviation.put(algorithm.toString(), formatPercent(machine.getAverageUsageDeviation()));
                    usageRequestCount.put(algorithm.toString(), machine.getUsageRequestCount());
                    relocationCount.put(algorithm.toString(), machine.getRelocationCount());
                    duration.put(algorithm.toString(), machine.getDuration());


                }

                averageUsages.put(averageUsage);
                averageUsageDeviations.put(averageUsageDeviation);
                usageRequestCounts.put(usageRequestCount);
                relocationCounts.put(relocationCount);
                durations.put(duration);
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        System.out.println("");
    }

    public static double formatPercent(Double d) {
        return  (((int) (d*10000)) / (double)100);
    }
}
