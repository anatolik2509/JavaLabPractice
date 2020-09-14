package ru.itis.pool;

public class ThreadPoolTest {
    public static void main(String[] args) {
        ThreadPool threadPool = new ThreadPool(3);
        threadPool.submit(()-> {
            for(int i = 0; i < 100; i++){
                System.out.println("1_" + i);
            }
        });
        threadPool.submit(()-> {
            for(int i = 0; i < 100; i++){
                System.out.println("2_" + i);
            }
        });
        threadPool.submit(()-> {
            for(int i = 0; i < 100; i++){
                System.out.println("3_" + i);
            }
        });
        threadPool.submit(()-> {
            for(int i = 0; i < 100; i++){
                System.out.println("4_" + i);
            }
        });

    }
}
