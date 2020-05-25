package de.b4.docster.resource;

import com.github.junrar.Archive;
import de.b4.docster.ArchiveService;
import de.b4.docster.model.ArchiveRoot;

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
    public ArchiveRoot getArchive() {
        return archiveService.getArchiveRoot();
    }
}
