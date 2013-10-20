package se.repos.indexing.webapp;

import javax.inject.Inject;
import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.core.Response;

import se.repos.indexing.IndexAdmin;
import se.repos.indexing.ReposIndexing;
import se.simonsoft.cms.item.RepoRevision;

public class IndexingResources {

	private ReposIndexing indexing;
	private IndexAdmin indexAdmin;

	@Inject
	public void setReposIndexing(ReposIndexing indexing) {
		this.indexing = indexing;
	}
	
	@Inject
	public void setIndexAdmin(IndexAdmin indexAdmin) {
		this.indexAdmin = indexAdmin;
	}
	
	@POST
	public Response sync(@FormParam("rev") RepoRevision revision) {
		indexing.sync(revision);
		return Response.noContent().build();
	}
	
	@POST
	public Response clear() {
		indexAdmin.clear();
		return Response.noContent().build();
	}

}
