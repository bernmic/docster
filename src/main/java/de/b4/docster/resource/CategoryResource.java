package de.b4.docster.resource;

import de.b4.docster.model.Category;
import de.b4.docster.service.CategoryService;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.Set;

@Path("/api/category")
@Consumes("application/json")
@Produces("application/json")
public class CategoryResource {

    @Inject
    CategoryService categoryService;

    @GET
    public Set<Category> getCategories() {
        return categoryService.getCategories();
    }

    @GET
    @Path("{id}")
    public Category getCategory(@PathParam("id") Long id) {
        Category c = categoryService.getCategory(id);
        if (c == null) {
            throw new WebApplicationException("Category with id " + id + " does not exist.", 404);
        }
        return c;
    }

    @POST
    public Response createRootCategory(Category category) {
        category.setParent(null);
        category = categoryService.createCategory(category);
        return Response.ok(category).status(201).build();
    }

    @POST
    @Path("{parentId}")
    public Response createRootCategory(@PathParam("parentId") Long parentId, Category category) {
        Category parent = categoryService.getCategory(parentId);
        if (parent == null) {
            throw new WebApplicationException("Unknown category with id " + parentId, 404);
        }
        category.setParent(parent);
        category = categoryService.createCategory(category);
        return Response.ok(category).status(201).build();
    }
}
