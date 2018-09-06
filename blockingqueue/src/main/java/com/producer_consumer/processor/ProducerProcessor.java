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
public class ProducerProcessor implements Runnable{
	
	BlockingQueue<Person> queue=null;
	
	public ProducerProcessor(BlockingQueue<Person> queue){
		this.queue=queue;
	}

	@Override
	public void run() {
		Person p=null;
		System.out.println("Scheduling interview of 10 people");
		for(int i=0;i<10;i++){
			p=new Person();
			p.setName("Interviewer"+i);
			p.setAge(30+i);
			try {
				queue.put(p);
				System.out.println("Scheduling Interviewer"+i);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		p=new Person();
		p.setName("Stop");
		try {
			queue.put(p);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
	}

}
