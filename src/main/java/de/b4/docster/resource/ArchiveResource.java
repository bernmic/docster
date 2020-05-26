package de.b4.docster.resource;

import de.b4.docster.service.ArchiveService;
import de.b4.docster.model.Archive;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

@Path("/api/archive")
@Produces("application/json")
public class ArchiveResource {

    @Inject
    ArchiveService archiveService;

    @GET
    public Archive getArchive() {
        return archiveService.getRoot();
    }
}
