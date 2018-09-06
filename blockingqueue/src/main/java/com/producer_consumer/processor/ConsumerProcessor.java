/**
 * 
 */
package com.producer_consumer.processor;

import java.util.concurrent.BlockingQueue;

import com.producer_consumer.pojo.Person;

/**
 * @author Naman
 *
 */
public class ConsumerProcessor implements Runnable {

	BlockingQueue<Person> queue = null;

	public ConsumerProcessor(BlockingQueue<Person> queue) {
		this.queue = queue;
	}

	@Override
	public void run() {
		String name = null;
		try {
			while (!(name = queue.take().getName()).equals("Stop")) {
				System.out.println("Interview taken for candidate" + name);
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("Interview of 10 people conducted");
	}

}
