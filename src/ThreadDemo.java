public class ThreadDemo extends Thread {
    private static final int NUM_THREADS = 5;

    public static void main(String[] args) {
        ThreadDemo[] td = new ThreadDemo[NUM_THREADS];
        for (int i = 0; i < NUM_THREADS; i++) {
            td[i] = new ThreadDemo("" + i);
        }

        // Wait for the threads to complete before exiting
        for (int i = 0; i < NUM_THREADS; i++) {
            try {
                td[i].join();
            } catch (InterruptedException ignored) {}
        }
    }

    private final String name;

    public ThreadDemo(String name) {
        this.name = name;
        setDaemon(true);
        start();
    }

    public void run() {
        System.out.println(name + " running");
        try {
            Thread.sleep(1 * 1000);
        } catch (InterruptedException ignored) {}
        System.out.println(name + " exiting");
    }
}
