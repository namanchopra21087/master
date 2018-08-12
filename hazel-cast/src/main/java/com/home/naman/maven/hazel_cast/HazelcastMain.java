package com.home.naman.maven.hazel_cast;

import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;

/**
 * Hello world!
 *
 */
public class HazelcastMain 
{
    public static void main( String[] args )
    {
    	HazelcastInstance hz = Hazelcast.newHazelcastInstance();
    }
}
