package se.repos.indexing.webapp;

import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.core.Application;

import se.repos.indexing.webapp.config.ConfigModuleDefault;
import se.repos.indexing.webapp.config.IndexingHandlersModuleXml;
import se.repos.indexing.webapp.config.IndexingModule;
import se.repos.indexing.webapp.config.ParentModule;
import se.repos.indexing.webapp.config.SolrCoreProvider;

import com.google.inject.Guice;
import com.google.inject.Injector;

public class ReposIndexingApplication extends Application {
	
	@Override
	public Set<Object> getSingletons() {
		Injector parent = Guice.createInjector(
				new ConfigModuleDefault(),
				new ParentModule());
		
		SolrCoreProvider solrCoreProvider = parent.getInstance(SolrCoreProvider.class);
		
		Set<Object> singletons = new HashSet<Object>();
		singletons.add(new IndexingResourceParent(parent,
				new IndexingModule(solrCoreProvider),
				new IndexingHandlersModuleXml()));
		return singletons;
	}
	
}
