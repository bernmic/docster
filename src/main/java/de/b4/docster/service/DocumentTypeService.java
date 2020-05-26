package de.b4.docster.service;

import de.b4.docster.model.DocumentType;
import org.jboss.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.WebApplicationException;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@ApplicationScoped
public class DocumentTypeService {
    private final static Logger LOGGER = Logger.getLogger(DocumentTypeService.class.getName());

    @Inject
    ArchiveService archiveService;

    public Set<DocumentType> getDocumentTypes() {
        return archiveService.getRoot().getDocumentTypes();
    }

    public Set<DocumentType> getDocumentTypeByMimetype(String mimetype) {
        return archiveService.getRoot().getDocumentTypes().stream().filter(dt -> dt.getMimetype() != null && dt.getMimetype().equals(mimetype)).collect(Collectors.toSet());
    }

    public DocumentType getDocumentTypeByExtension(String extension) {
        Optional<DocumentType> odt = archiveService.getRoot().getDocumentTypes().stream().filter(dt -> dt.getExtension() != null && dt.getExtension().equals(extension)).findFirst();
        return odt.isEmpty() ? null : odt.get();
    }

    public DocumentType createDocumentType(DocumentType documentType) {
        if (getDocumentTypeByExtension(documentType.getExtension()) != null) {
            throw new WebApplicationException("There is already a documenttype with extension " + documentType.getExtension(), 409);
        }
        archiveService.getRoot().getDocumentTypes().add(documentType);
        archiveService.getStorageManager().store(archiveService.getRoot().getDocumentTypes());
        return documentType;
    }

    public DocumentType modifyDocumentType(String extension, DocumentType documentType) {
        DocumentType existingDocumentType = getDocumentTypeByExtension(extension);
        if (existingDocumentType == null) {
            throw new WebApplicationException("There is no documenttype with extension " + extension, 404);
        }
        existingDocumentType.setExtension(documentType.getExtension());
        existingDocumentType.setName(documentType.getName());
        existingDocumentType.setMimetype(documentType.getMimetype());
        archiveService.getStorageManager().store(existingDocumentType);
        return existingDocumentType;
    }

    public void deleteDocumentType(String extension) {
        DocumentType existingDocumentType = getDocumentTypeByExtension(extension);
        if (existingDocumentType == null) {
            throw new WebApplicationException("There is no documenttype with extension " + extension, 404);
        }
        if (!archiveService.getRoot().getDocumentTypes().remove(existingDocumentType)) {
            throw new WebApplicationException("Error deleting documenttype with extension " + extension, 500);
        }
        archiveService.getStorageManager().store(archiveService.getRoot().getDocumentTypes());
    }
}
