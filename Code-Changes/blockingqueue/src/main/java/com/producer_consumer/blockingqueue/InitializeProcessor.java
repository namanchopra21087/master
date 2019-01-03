/**
 * 
 */
package com.producer_consumer.blockingqueue;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.producer_consumer.pojo.Person;
import com.producer_consumer.processor.ConsumerProcessor;
import com.producer_consumer.processor.ProducerProcessor;

/**
 * @author Naman
 *
 */
@Component
public class InitializeProcessor {
	
	@Autowired
    ProducerProcessor scheduleInterview;
	
	InitializeProcessor(){
	}

	public void init(){
		BlockingQueue<Person> queue=new ArrayBlockingQueue<>(3);
        scheduleInterview.setQueue(queue);
        ConsumerProcessor takeInterview=new ConsumerProcessor(queue);
        new Thread(scheduleInterview).start();
        new Thread(takeInterview).start();
	}
	
}
