public class RunnableDemo implements Runnable {
    private static final int NUM_THREADS = 5;

    public static void main(String[] args) {
        for (int i = 0; i < NUM_THREADS; i++) {
            new RunnableDemo("" + i);
        }
    }

    private final String name;

    public RunnableDemo(String name) {
        this.name = name;
        Thread t = new Thread(this);
        t.start();
    }

    public void run() {
        System.out.println(name + " running");
    }
}
