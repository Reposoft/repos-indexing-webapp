package se.repos.indexing.webapp;

import static org.junit.Assert.*;

import org.junit.Test;

public class ReposIndexingApplicationTest {

	@Test
	public void testGetSingletons() {
		ReposIndexingApplication app = new ReposIndexingApplication();
		app.getSingletons();
	}

}
