package az.edu.itbrains.ecommerce.exceptions;

public class ResourceNotFoundException extends RuntimeException{


    private Long id;
    private String resourceName;

    public ResourceNotFoundException(Long id, String resourceName) {
        super(String.format("Resource not found with id: %s and resource name: %s", id, resourceName));
    }
}
