package de.b4.docster.service;

import de.b4.docster.model.Category;
import org.jboss.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.WebApplicationException;
import java.util.HashSet;
import java.util.Optional;
import java.util.OptionalLong;
import java.util.Set;

@ApplicationScoped
public class CategoryService {

    private final static Logger LOGGER = Logger.getLogger(CategoryService.class.getName());

    @Inject
    ArchiveService archiveService;

    public Set<Category> getCategories() {
        return archiveService.getRoot().getCategories();
    }

    public Category getCategory(Long id) {
        Optional<Category> oc = archiveService.getRoot().getCategories().stream().filter(category -> category.getId() != null && category.getId().equals(id)).findFirst();
        return oc.isEmpty() ? null : oc.get();
    }

    public Category createCategory(Category category) {
        if (category.getId() != null) {
            throw new WebApplicationException("id must not be set", 400);
        }
        if (category.getChildren() != null) {
            throw new WebApplicationException("children must not be set", 400);
        }
        Set<Category> categories = archiveService.getRoot().getCategories();
        OptionalLong oid = categories.stream().mapToLong(c -> c.getId()).max();
        if (oid.isPresent()) {
            category.setId(oid.getAsLong() + 1L);
        }
        else {
            category.setId(1L);
        }
        categories.add(category);
        if (category.getParent() != null) {
            if (category.getParent().getChildren() == null)
                category.getParent().setChildren(new HashSet<>());
            category.getParent().getChildren().add(category);
            archiveService.getStorageManager().store(category.getParent());
        }
        archiveService.getStorageManager().store(categories);
        return null;
    }
}
