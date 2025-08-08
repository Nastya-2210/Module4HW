public class Print {
    public static void main(String[] args) {
    new Worker1().main();
}
}

class Worker1 {
    final Object lock1 = new Object();

    private volatile boolean isThread1Turn = true;

    public void output1() {
        synchronized (lock1){
            while(true){
                if(isThread1Turn) {
                    System.out.println("1");
                    isThread1Turn = false;
                    lock1.notifyAll();
                }    else{
                    try{
                       lock1.wait();
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        return;
                    }
                }
        }}
    }

    public void output2() {
        synchronized (lock1){
            while(true){
                if(!isThread1Turn) {
                    System.out.println("2");
                    isThread1Turn = true;
                    lock1.notifyAll();
                }    else{
                    try{
                        lock1.wait();
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        return;
                    }
                }
            }}
    }

    public void main(){
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

        thread1.setPriority(10);
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
