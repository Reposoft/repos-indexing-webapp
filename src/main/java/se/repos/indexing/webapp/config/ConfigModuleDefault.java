package se.repos.indexing.webapp.config;

import com.google.inject.AbstractModule;

public class ConfigModuleDefault extends AbstractModule {

	@Override
	protected void configure() {
		bind(SolrCoreProvider.class).toInstance(new SolrCoreProviderAssumeExisting("http://locahost:8080/solr/"));
	}

}
