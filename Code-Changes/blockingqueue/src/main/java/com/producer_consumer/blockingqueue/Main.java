package com.producer_consumer.blockingqueue;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

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
    	ApplicationContext factory=new AnnotationConfigApplicationContext(AppConfig.class);
    	InitializeProcessor initialize=factory.getBean(InitializeProcessor.class);
    	initialize.init();
    }
}
