package de.b4.docster.resource;

import de.b4.docster.model.DocumentType;
import de.b4.docster.service.DocumentTypeService;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.Set;

@Path("/api/documenttype")
@Consumes("application/json")
@Produces("application/json")
public class DocumentTypeResource {

    @Inject
    DocumentTypeService documentTypeService;

    @GET
    public Set<DocumentType> getAllDocumentTypes(@QueryParam("mimetype") String mimetype) {
        if (mimetype != null) {
            return documentTypeService.getDocumentTypeByMimetype(mimetype);
        }
        return documentTypeService.getDocumentTypes();
    }

    @GET
    @Path("{extension}")
    public DocumentType getDocumentType(@PathParam("extension") String extension) {
        DocumentType documentType = documentTypeService.getDocumentTypeByExtension(extension);
        if (documentType == null) {
            throw new WebApplicationException("Documenttype with extension " + extension + " does not exist.", 404);
        }
        return documentType;
    }

    @POST
    public Response createDocumentType(DocumentType documentType) {
        documentTypeService.createDocumentType(documentType);
        return Response.ok(documentType).status(201).build();
    }

    @PUT
    @Path("{extension}")
    public DocumentType modifyDocumentType(@PathParam("extension") String extension, DocumentType documentType) {
        return documentTypeService.modifyDocumentType(extension, documentType);
    }

    @DELETE
    @Path("{extension}")
    public Response deleteDocumentType(@PathParam("extension") String extension) {
        documentTypeService.deleteDocumentType(extension);
        return Response.status(204).build();
    }
}
