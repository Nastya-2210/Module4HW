public class LiveLock {
    public static void main(String[] args) {
        new Worker3().main();
    }
}

class Worker3 {
    final Object lock1 = new Object();
    final Object lock2 = new Object();

    private volatile boolean isThread1Turn = true;
    private volatile boolean isThread2Turn = false;

    public void output1() {
        while (true) {
            synchronized (lock1) {
                if (isThread1Turn) {
                    System.out.println("1");
                    isThread1Turn = false;
                    isThread2Turn = true;
                } else {
                    System.out.println("Thread 1: yielding...");
                    try {
                        Thread.sleep(100); // Имитация "вежливого" ожидания
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        return;
                    }
                }
            }
        }
    }

    public void output2() {
        while (true) {
            synchronized (lock2) {
                if (isThread2Turn) {
                    System.out.println("2");
                    isThread2Turn = false;
                    isThread1Turn = true;
                } else {
                    System.out.println("Thread 2: yielding...");
                    try {
                        Thread.sleep(100); // Имитация "вежливого" ожидания
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        return;
                    }
                }
            }
        }
    }

    public void main() {
        Thread thread1 = new Thread(this::output1);
        Thread thread2 = new Thread(this::output2);

        thread1.start();
        thread2.start();

        try {
            thread1.join();
            thread2.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
