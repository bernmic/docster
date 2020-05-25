package de.b4.docster;

import de.b4.docster.model.ArchiveRoot;
import io.quarkus.runtime.ShutdownEvent;
import io.quarkus.runtime.StartupEvent;
import one.microstream.storage.types.EmbeddedStorage;
import one.microstream.storage.types.EmbeddedStorageManager;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jboss.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import java.nio.file.Paths;

@ApplicationScoped
public class ArchiveService {
    private final static Logger LOGGER = Logger.getLogger(ArchiveService.class.getName());

    @ConfigProperty(name = "docster.datapath", defaultValue = "data")
    String dataPath;

    ArchiveRoot archiveRoot;
    EmbeddedStorageManager storageManager;

    void onStart(@Observes StartupEvent ev) {
        LOGGER.info("The application is starting...");
        storageManager = EmbeddedStorage.start(/*Paths.get(dataPath)*/);
        if (storageManager.root() == null) {
            archiveRoot = new ArchiveRoot();
            archiveRoot.setName("MainArchive");
            archiveRoot.setPath("~/Documents");
            archiveRoot.setDescription("The main archive");
            storageManager.storeRoot();
            LOGGER.info("Created a new archive");
        }
        else {
            archiveRoot = (ArchiveRoot) storageManager.root();
            LOGGER.info("Loaded existing archive");
        }
        //LOGGER.info(storageManager.configuration().toString());
    }

    void onStop(@Observes ShutdownEvent ev) {
        LOGGER.info("The application is stopping...");
        storageManager.shutdown();
    }

    public ArchiveRoot getArchiveRoot() {
        return archiveRoot;
    }
}
