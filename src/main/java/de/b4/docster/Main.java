package de.b4.docster;

import de.b4.docster.model.Archive;
import one.microstream.storage.types.EmbeddedStorage;
import one.microstream.storage.types.EmbeddedStorageManager;

public class Main {
    public static void main(String[] args) {
        EmbeddedStorageManager storageManager = EmbeddedStorage.start();
        if (storageManager.root() == null) {
            Archive archive = new Archive();
            archive.setName("Test");
            archive.setPath("/docs");
            archive.setDescription("Description");
            storageManager.setRoot(archive);
            storageManager.storeRoot();
            System.out.println("Created a new archive");
        }
        else {
            System.out.println(storageManager.root().getClass().getName());
            Archive root = (Archive) storageManager.root();
            System.out.println("Loaded existing archive with name " + root.getName());
        }
        storageManager.shutdown();
        System.exit(0);
    }
}
