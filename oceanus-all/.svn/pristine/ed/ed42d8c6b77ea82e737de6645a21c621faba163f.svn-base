package com.bj58.oceanus.exchange.thread;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.Callable;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;

import org.junit.Test;

public class TestExecuteThread {
	@Test
	public void testCyclicBarrier() throws Exception, BrokenBarrierException {

		ThreadPoolExecutor threadPool = (ThreadPoolExecutor) Executors
				.newFixedThreadPool(50,new ThreadFactory(){

					@Override
					public Thread newThread(Runnable r) {
						Thread t=new Thread(r);
						t.setName("helllllllll===");
						return t;
					}});
		int n = 10;
		final CyclicBarrier barrier = new CyclicBarrier(n+3);
		List<Future> futureList=new ArrayList<Future>();
		for (int i = 0; i < n; i++) {
			Future future=threadPool.submit(new Callable() {

				@Override
				public Object call() throws Exception {
					System.out.println("begin thread=" + Thread.currentThread());
					Random random=new Random();
					Thread.sleep(1000*Math.abs(random.nextInt(5)));
					barrier.await();
					System.out.println("end thread=" + Thread.currentThread());
					return Thread.currentThread().getClass();
				}
			}); 
			futureList.add(future);
		}
		barrier.await();
		System.out.println("================================");
		for(Future future:futureList){
			System.out.println(future.get());
		}
	}

}
