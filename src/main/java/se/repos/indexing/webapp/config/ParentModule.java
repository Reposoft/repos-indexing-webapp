/**
 * Copyright (C) 2004-2012 Repos Mjukvara AB
 */
package se.repos.indexing.webapp.config;

import se.repos.indexing.scheduling.IndexingSchedule;
import se.repos.indexing.scheduling.IndexingScheduleBlockingOnly;
import se.simonsoft.cms.backend.svnkit.info.CmsRepositoryLookupSvnkit;
import se.simonsoft.cms.item.info.CmsRepositoryLookup;

import com.google.inject.AbstractModule;

public class ParentModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(IndexingSchedule.class).to(IndexingScheduleBlockingOnly.class);
	}
	
}
