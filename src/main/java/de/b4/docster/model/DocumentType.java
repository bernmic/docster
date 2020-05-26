package de.b4.docster.model;

public class DocumentType {
    public String mimetype;
    public String extension;
    public String name;

    public DocumentType() {
    }

    public DocumentType(String name, String extension, String mimetype) {
        this.mimetype = mimetype;
        this.extension = extension;
        this.name = name;
    }

    public String getMimetype() {
        return mimetype;
    }

    public void setMimetype(String mimetype) {
        this.mimetype = mimetype;
    }

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
