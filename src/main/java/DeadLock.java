public class DeadLock {
    public static void main(String[] args) {
        new Worker2().main();
    }
}

class Worker2 {
    final Object lock1 = new Object();
    final Object lock2 = new Object();

    public void output1() {
        synchronized (lock1) {
            try {
                Thread.sleep(100); //для надежности deadlock
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

            synchronized (lock2) {
                System.out.println("1");
            }
        }
    }

    public void output2() {
        synchronized (lock2) {
            try {
                Thread.sleep(100); // для надежности deadlock
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

            synchronized (lock1) {
                System.out.println("2");
            }
        }
    }

    public void main() {
        Thread thread1 = new Thread(new Runnable() {
            @Override
            public void run() {
                output1();
            }
        });
        Thread thread2 = new Thread(new Runnable() {
            @Override
            public void run() {
                output2();
            }
        });

        thread1.start();
        thread2.start();

        try {
            thread1.join();
            thread2.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}

