# so-5
Simulation of distributed CPU load balancing algorithms.

This is a program I made for the Operating Systems course I took on the second semester in university. I'm not planning on developing this project any further, feel free to use the code however you want.

Results approved by Dr Katarzyna Tworek.

## Rough explanation
The system uses N identical CPUs. On each of them appear new processes with different frequency and with different requirements of CPU's resources.

A process appears on CPU _x_. It could he handled by these three algorithms:
1. _x_ requests the usage of another randomly chosen CPU. If the usage is lower than some threshold _p_, the process is added there. Otherwise, it chooses again, attempting this up to _z_ times. If every chosen CPU has a usage above _p_, the process is added to _x_.
2. If the usage of _x_ exceeds some threshold _p_, the process is sent to a randomly chosen CPU _y_ with usage lower than _p_ (if _y_'s usage is greater than _p_, a CPU is chosen again). If the usage doesn't exceed said threshold, the process is sent to _x_.
3. The process is handled the same way as in algorithm #2, but additionally CPUs with usage lower than some minimal threshold _r_ ask randomly chosen CPUs for usage and if it exceeds _p_, the asking CPU takes a portion of its processes.

## Input
The user enters one of the commands specified by the program and is able to run the simulation or change its parameters. Some of the available commands consist of:

- ```run``` &ndash; runs the simulation with specified parameters
- ```N <integer>``` &ndash; sets the CPU count
- ```r <integer>``` &ndash; sets the lower threshold
- ```p <integer>``` &ndash; sets the upper threshold
- ```z <integer>``` &ndash; sets the attempt count

For more detailed command list, use ```help``` in the command line.
