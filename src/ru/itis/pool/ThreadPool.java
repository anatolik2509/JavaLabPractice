package ru.itis.pool;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedDeque;

public class ThreadPool {
    private Queue<Runnable> taskQueue;
    private Thread[] threads;

    public ThreadPool(int poolCount){
        taskQueue = new ConcurrentLinkedDeque<>();
        threads = new Thread[poolCount];
        for(int i = 0; i < poolCount; i++){
            threads[i] = new WorkingThread();
            threads[i].start();
        }
    }

    public void submit(Runnable r){
        synchronized (taskQueue) {
            taskQueue.add(r);
            taskQueue.notify();
        }
    }

    private class WorkingThread extends Thread{

        private Runnable r;

        @Override
        public void run() {
            try {
                synchronized (taskQueue) {
                    taskQueue.wait();
                }
                while (true) {
                    synchronized (taskQueue) {
                        if (!taskQueue.isEmpty()) {
                            r = taskQueue.poll();
                        } else {
                            taskQueue.wait();
                        }
                    }
                    if(r != null) {
                        r.run();
                        r = null;
                    }
                }
            }
            catch (InterruptedException e){
                throw new RuntimeException(e);
            }
        }
    }
}
