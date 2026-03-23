package az.edu.itbrains.ecommerce.exceptions;

public class ResourceNotFoundException extends RuntimeException{

    private final Long id;
    private final String resourceName;

    public ResourceNotFoundException(Long id, String resourceName) {
        super(String.format("Resource not found with id: %s and resource name: %s", id, resourceName));
        this.id = id;
        this.resourceName = resourceName;
    }

    public Long getId() {
        return id;
    }

    public String getResourceName() {
        return resourceName;
    }
}
