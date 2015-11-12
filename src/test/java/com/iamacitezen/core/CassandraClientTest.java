package com.iamacitezen.core;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Host;
import com.datastax.driver.core.Metadata;
import com.datastax.driver.core.Session;

public class CassandraClientTest {

	private Cluster cluster;
	private Session session;

	@BeforeClass
	public void setUp() {
		String node = "127.0.0.1";
		cluster = Cluster.builder().addContactPoint(node)
//		 .withSSL() // Uncomment if using client to node encryption
				.build();
		session = cluster.connect();
	}

	@Test(enabled=false)
	public void connectLocalhost() {

		Metadata metadata = cluster.getMetadata();
		System.out.printf("Connected to cluster: %s\n",
				metadata.getClusterName());
		for (Host host : metadata.getAllHosts()) {
			System.out.printf("Datacenter: %s; Host: %s; Rack: %s\n",
					host.getDatacenter(), host.getAddress(), host.getRack());
		}
	}
	
	@AfterClass
	public void tearDown() {
		session.shutdown();
		cluster.shutdown();
		
	}

}
