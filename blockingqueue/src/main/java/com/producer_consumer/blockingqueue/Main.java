package com.producer_consumer.blockingqueue;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import com.producer_consumer.pojo.Person;
import com.producer_consumer.processor.ConsumerProcessor;
import com.producer_consumer.processor.ProducerProcessor;

/**
 * Producer-Consumer example.
 * Usecase:- 
 * <li>We need to schedule an interview in a hall.</li>
 * <li>At a time there can not be more than 3 interviews that can take place.
 * <li>Need to schedule interview of 10 people.</li>
 * <li>Producer will schedule the interview</li>
 * <li>Consumer will take the interview</li>
 */
public class Main 
{
    public static void main( String[] args )
    {
        BlockingQueue<Person> queue=new ArrayBlockingQueue<>(3);
        ProducerProcessor scheduleInterview=new ProducerProcessor(queue);
        ConsumerProcessor takeInterview=new ConsumerProcessor(queue);
        
        new Thread(scheduleInterview).start();
        new Thread(takeInterview).start();
       
    }
}
