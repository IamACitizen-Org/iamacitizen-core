package com.iamacitizen.core.database.cassandra;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Session;

public class CassandraConnectionManager {
	
	private Cluster cluster;
	private Session session;
	
	public CassandraConnectionManager(String node) {
		cluster = Cluster.builder()
	            .addContactPoint(node).build();
		session = cluster.connect();
	}
	
	public Session getSession() {
		return session;
	}
	
	public void closeSession() {
		session.shutdown();
	}
	
	

}
