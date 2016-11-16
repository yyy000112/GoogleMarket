package android.ye.googlemarket.Manager;

import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 线程管理
 * Created by ye on 2016/11/15.
 */
public class ThreadManager {
    private static ThreadPool mThreadPool;

    public static ThreadPool getThreadPool(){
       if (mThreadPool == null){
           synchronized (ThreadPool.class){

               if (mThreadPool == null){
                   //获取CPU个数
                   int cpuCount = Runtime.getRuntime().availableProcessors();
                   //随意定义进程总数为10，或者按cpu个数来定义int threadCount = cpuCount * 2 + 1;
                   int threadCount = 10;
                   //传入最大线程数和核心线程数以及休息时间
                   mThreadPool = new ThreadPool(threadCount,threadCount,1);
               }

           }

       }
        return mThreadPool;
    }

    //线程池
    public  static class ThreadPool{
        private int corePoolSize;//核心进程数
        private int maxPoolsize;//最大进程数
        private long keepAliveTime;//休息时间
        private ThreadPoolExecutor executor;

        private ThreadPool(int corePoolSize,int maxPoolsize,long keepAliveTime){
            this.corePoolSize = corePoolSize;
            this.maxPoolsize = maxPoolsize;
            this.keepAliveTime = keepAliveTime;

        }

        public void execute(Runnable r){
            if (executor == null){
                //TimeUnit时间单位;LinkedBlockingQueue:线程队列;defaultThreadFactory:生产线程的工厂;AbortPolicy:线程异常处理策略
                executor = new ThreadPoolExecutor(corePoolSize,maxPoolsize, keepAliveTime, TimeUnit.SECONDS,
                        new LinkedBlockingQueue<Runnable>(), Executors.defaultThreadFactory(),new ThreadPoolExecutor.AbortPolicy());
            }
            // 线程池执行一个Runnable对象, 具体运行时机线程池说了算
            executor.execute(r);
        }

        //取消进程
        public void cancle(Runnable r){
            if (executor != null){
                // 从线程队列中移除对象
                executor.getQueue().remove(r);
            }
        }

    }
}
