package de.b4.docster.service;

import de.b4.docster.model.Archive;
import de.b4.docster.model.DocumentType;
import io.quarkus.runtime.ShutdownEvent;
import io.quarkus.runtime.StartupEvent;
import one.microstream.reflect.ClassLoaderProvider;
import one.microstream.storage.types.EmbeddedStorage;
import one.microstream.storage.types.EmbeddedStorageFoundation;
import one.microstream.storage.types.EmbeddedStorageManager;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jboss.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import java.util.HashSet;
import java.util.Set;

@ApplicationScoped
public class ArchiveService {
    private final static Logger LOGGER = Logger.getLogger(ArchiveService.class.getName());

    @ConfigProperty(name = "docster.datapath", defaultValue = "data")
    String dataPath;

    Archive root;
    EmbeddedStorageManager storageManager;

    void onStart(@Observes StartupEvent ev) {
        LOGGER.info("The application is starting...");
        EmbeddedStorageFoundation<?> foundation = EmbeddedStorage.Foundation();
        foundation.getConnectionFoundation().setClassLoaderProvider(ClassLoaderProvider.New(Thread.currentThread().getContextClassLoader()));
        storageManager = foundation.start();
        if (storageManager.root() == null) {
            root = initializeArchive();
            storageManager.setRoot(root);
            storageManager.storeRoot();
            LOGGER.info("Created a new archive");
        }
        else {
            root = (Archive) storageManager.root();
            LOGGER.info("Loaded existing archive with name " + root.getName());
        }
        //LOGGER.info(storageManager.configuration().toString());
    }

    void onStop(@Observes ShutdownEvent ev) {
        LOGGER.info("The application is stopping...");
        storageManager.shutdown();
    }

    public Archive getRoot() {
        return root;
    }

    public EmbeddedStorageManager getStorageManager() {
        return storageManager;
    }

    private Archive initializeArchive() {
        Archive archive = new Archive();
        archive.setName("MainArchive");
        archive.setPath("Documents");
        archive.setDescription("The main archive");
        Set<DocumentType> documentTypes = new HashSet<>();
        documentTypes.add(new DocumentType("Portable Document Format", "pdf", "application/pdf"));
        documentTypes.add(new DocumentType("Text", "txt", "text/plain"));
        documentTypes.add(new DocumentType("Microsoft Word", "doc", "application/msword"));
        documentTypes.add(new DocumentType("Microsoft Word", "docx", "application/vnd.openxmlformats-officedocument.wordprocessingml.document"));
        documentTypes.add(new DocumentType("Microsoft Excel", "xls", "application/msexcel"));
        documentTypes.add(new DocumentType("Microsoft Excel", "xlsx", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"));
        documentTypes.add(new DocumentType("Microsoft Powerpoint", "ppt", "application/msexcel"));
        documentTypes.add(new DocumentType("Microsoft Powerpoint", "pptx", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"));
        documentTypes.add(new DocumentType("Graphics Interchange Format", "gif", "image/gif"));
        documentTypes.add(new DocumentType("Portable Network Graphics", "png", "image/png"));
        documentTypes.add(new DocumentType("JPEG", "jpg", "image/jpeg"));
        documentTypes.add(new DocumentType("JPEG", "jpeg", "image/jpeg"));
        documentTypes.add(new DocumentType("TIFF", "tif", "image/tiff"));
        archive.setDocumentTypes(documentTypes);
        archive.setCategories(new HashSet<>());
        return archive;
    }
}
