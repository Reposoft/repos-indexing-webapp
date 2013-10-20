package se.repos.indexing.webapp;

import java.io.File;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedList;
import java.util.Map;

import javax.inject.Singleton;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import se.repos.indexing.webapp.config.BackendModule;
import se.simonsoft.cms.backend.svnkit.CmsRepositorySvn;
import se.simonsoft.cms.item.CmsRepository;
import se.simonsoft.cms.item.RepoRevision;
import se.simonsoft.cms.item.info.CmsRepositoryLookup;

import com.google.inject.Injector;
import com.google.inject.Module;

/**
 * Elaborate on the mapping from query parameters to per-repository context and CmsRepository + RepoRevision instances.
 * 
 * Same REST methods as IndexingResource but takes repository as argument.
 */
@Path("/index")
@Singleton
public class IndexingResourceParent {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	private Map<CmsRepository, Injector> context;

	private Injector parent;
	private Module[] modules;
	// global configuration of this is not really supported //private CmsRepositoryLookup repositoryLookup;
	
	// we can't get repository URLs from here anyway //@javax.ws.rs.core.Context javax.ws.rs.core.UriInfo contextUri;

	public IndexingResourceParent(Injector parent, Module... repositoryModulesExcludingBackend) {
		this.parent = parent;
		//this.repositoryLookup = parent.getInstance(CmsRepositoryLookup.class);
		this.modules = repositoryModulesExcludingBackend;
	}
	
	protected Iterable<Module> getRepositoryConfig(CmsRepositorySvn repository) {
		Collection<Module> rm = new LinkedList<Module>();
		rm.add(new BackendModule(repository));
		for (Module m : modules) rm.add(m);
		return rm;
	}

	protected Injector getContext(String repositoryUrl, File svnAdminPath) {
		CmsRepositorySvn repository = new CmsRepositorySvn(repositoryUrl, svnAdminPath);
		return getContext(repository);
	}

	protected Injector getContext(CmsRepositorySvn repository) {
		if (context.containsKey(repository)) {
			return context.get(repository);
		}
		logger.debug("Creating new repository context for {}", repository);
		Injector rc = parent.createChildInjector(getRepositoryConfig(repository));
		context.put(repository, rc);
		return rc;
	}
	
	protected RepoRevision getRevision(CmsRepository repository, Long revisionNumber) {
		//Date date = repositoryLookup.getRevisionTimestamp(repository, revisionNumber);
		Date date = null;
		return new RepoRevision(revisionNumber, date);
	}
	
	@POST
	@Path("/sync")
	public Response syncSvn(@FormParam("url") String repository, @FormParam("path") File adminpath, @FormParam("rev") Long revisionNumber) {
		Injector rc = getContext(repository, adminpath);
		RepoRevision revision = getRevision(rc.getInstance(CmsRepository.class), revisionNumber);
		return rc.getInstance(IndexingResources.class).sync(revision);
	}
	
	@POST
	@Path("/clear")
	public Response clearSvn(@FormParam("url") String repository, @FormParam("path") File adminpath) {
		return getContext(repository, adminpath).getInstance(IndexingResources.class).clear();
	}
	
}
