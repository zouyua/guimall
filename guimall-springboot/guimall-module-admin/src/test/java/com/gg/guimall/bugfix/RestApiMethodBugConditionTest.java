package com.gg.guimall.bugfix;

import org.junit.jupiter.api.Test;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Bug Condition Exploration Test for REST API Method Standardization
 * 
 * **Validates: Requirements 1.1, 1.2, 1.3, 1.4, 1.5, 1.6**
 * 
 * This test MUST FAIL on unfixed code - failure confirms the bug exists.
 * The test demonstrates current HTTP method usage that violates RESTful standards.
 * 
 * Expected counterexamples on unfixed code:
 * - /admin/user/info uses POST instead of GET
 * - /cart/update/quantity uses GET instead of POST
 * - /cart/delete uses POST instead of DELETE
 * - /cart/clear uses POST instead of DELETE
 * - /admin/pms/product/{id}/publish uses PUT instead of POST
 * - /admin/pms/product/{id}/unpublish uses PUT instead of POST
 */
public class RestApiMethodBugConditionTest {

    /**
     * Property 1: Bug Condition - Query info endpoints should use GET method
     * 
     * Tests that query information endpoints (like /admin/user/info) use GET method
     * according to RESTful standards.
     * 
     * On unfixed code: This test will FAIL because /admin/user/info uses POST
     * On fixed code: This test will PASS because /admin/user/info uses GET
     */
    @Test
    void testBugCondition_QueryInfoEndpoint_ShouldUseGetMethod() throws Exception {
        // Load the AdminUserController class
        Class<?> controllerClass = Class.forName("com.gg.guimall.admin.controller.AdminUserController");
        
        // Find the user info method
        Method userInfoMethod = findMethodByPath(controllerClass, "/user/info");
        assertNotNull(userInfoMethod, "User info method not found");
        
        // Check if it uses GET method (RESTful standard for query operations)
        boolean usesGetMapping = userInfoMethod.isAnnotationPresent(GetMapping.class);
        boolean usesRequestMapping = false;
        
        if (userInfoMethod.isAnnotationPresent(RequestMapping.class)) {
            RequestMapping requestMapping = userInfoMethod.getAnnotation(RequestMapping.class);
            usesRequestMapping = requestMapping.method().length > 0 && 
                                requestMapping.method()[0] == RequestMethod.GET;
        }
        
        // This assertion will FAIL on unfixed code (uses POST)
        // and PASS on fixed code (uses GET)
        assertTrue(usesGetMapping || usesRequestMapping, 
            "Bug Condition Found: /admin/user/info uses POST method instead of GET. " +
            "Query information endpoints should use GET method according to RESTful standards.");
    }

    /**
     * Property 1: Bug Condition - Update quantity endpoint should use POST method
     * 
     * Tests that update operations (like /cart/update/quantity) use POST method
     * according to RESTful standards.
     * 
     * On unfixed code: This test will FAIL because /cart/update/quantity uses GET
     * On fixed code: This test will PASS because /cart/update/quantity uses POST
     */
    @Test
    void testBugCondition_UpdateQuantityEndpoint_ShouldUsePostMethod() throws Exception {
        // Load the OmsCartItemController class
        Class<?> controllerClass = Class.forName("com.gg.guimall.web.controller.OmsCartItemController");
        
        // Find the update quantity method
        Method updateQuantityMethod = findMethodByPath(controllerClass, "/update/quantity");
        assertNotNull(updateQuantityMethod, "Update quantity method not found");
        
        // Check if it uses POST method (RESTful standard for update operations)
        boolean usesPostMapping = updateQuantityMethod.isAnnotationPresent(PostMapping.class);
        boolean usesRequestMapping = false;
        
        if (updateQuantityMethod.isAnnotationPresent(RequestMapping.class)) {
            RequestMapping requestMapping = updateQuantityMethod.getAnnotation(RequestMapping.class);
            usesRequestMapping = requestMapping.method().length > 0 && 
                                requestMapping.method()[0] == RequestMethod.POST;
        }
        
        // This assertion will FAIL on unfixed code (uses GET)
        // and PASS on fixed code (uses POST)
        assertTrue(usesPostMapping || usesRequestMapping, 
            "Bug Condition Found: /cart/update/quantity uses GET method instead of POST. " +
            "Update operations should use POST method according to RESTful standards.");
    }

    /**
     * Property 1: Bug Condition - Delete cart item endpoint should use DELETE method
     * 
     * Tests that delete operations (like /cart/delete) use DELETE method
     * according to RESTful standards.
     * 
     * On unfixed code: This test will FAIL because /cart/delete uses POST
     * On fixed code: This test will PASS because /cart/delete uses DELETE
     */
    @Test
    void testBugCondition_DeleteCartItemEndpoint_ShouldUseDeleteMethod() throws Exception {
        // Load the OmsCartItemController class
        Class<?> controllerClass = Class.forName("com.gg.guimall.web.controller.OmsCartItemController");
        
        // Find the delete method
        Method deleteMethod = findMethodByPath(controllerClass, "/delete");
        assertNotNull(deleteMethod, "Delete method not found");
        
        // Check if it uses DELETE method (RESTful standard for delete operations)
        boolean usesDeleteMapping = deleteMethod.isAnnotationPresent(DeleteMapping.class);
        boolean usesRequestMapping = false;
        
        if (deleteMethod.isAnnotationPresent(RequestMapping.class)) {
            RequestMapping requestMapping = deleteMethod.getAnnotation(RequestMapping.class);
            usesRequestMapping = requestMapping.method().length > 0 && 
                                requestMapping.method()[0] == RequestMethod.DELETE;
        }
        
        // This assertion will FAIL on unfixed code (uses POST)
        // and PASS on fixed code (uses DELETE)
        assertTrue(usesDeleteMapping || usesRequestMapping, 
            "Bug Condition Found: /cart/delete uses POST method instead of DELETE. " +
            "Delete operations should use DELETE method according to RESTful standards.");
    }

    /**
     * Property 1: Bug Condition - Clear cart endpoint should use DELETE method
     * 
     * Tests that clear/delete operations (like /cart/clear) use DELETE method
     * according to RESTful standards.
     * 
     * On unfixed code: This test will FAIL because /cart/clear uses POST
     * On fixed code: This test will PASS because /cart/clear uses DELETE
     */
    @Test
    void testBugCondition_ClearCartEndpoint_ShouldUseDeleteMethod() throws Exception {
        // Load the OmsCartItemController class
        Class<?> controllerClass = Class.forName("com.gg.guimall.web.controller.OmsCartItemController");
        
        // Find the clear method
        Method clearMethod = findMethodByPath(controllerClass, "/clear");
        assertNotNull(clearMethod, "Clear method not found");
        
        // Check if it uses DELETE method (RESTful standard for delete operations)
        boolean usesDeleteMapping = clearMethod.isAnnotationPresent(DeleteMapping.class);
        boolean usesRequestMapping = false;
        
        if (clearMethod.isAnnotationPresent(RequestMapping.class)) {
            RequestMapping requestMapping = clearMethod.getAnnotation(RequestMapping.class);
            usesRequestMapping = requestMapping.method().length > 0 && 
                                requestMapping.method()[0] == RequestMethod.DELETE;
        }
        
        // This assertion will FAIL on unfixed code (uses POST)
        // and PASS on fixed code (uses DELETE)
        assertTrue(usesDeleteMapping || usesRequestMapping, 
            "Bug Condition Found: /cart/clear uses POST method instead of DELETE. " +
            "Clear/delete operations should use DELETE method according to RESTful standards.");
    }

    /**
     * Property 1: Bug Condition - Publish product endpoint should use POST method
     * 
     * Tests that state change operations (like /admin/pms/product/{id}/publish) use POST method
     * according to RESTful standards.
     * 
     * On unfixed code: This test will FAIL because /{id}/publish uses PUT
     * On fixed code: This test will PASS because /{id}/publish uses POST
     */
    @Test
    void testBugCondition_PublishProductEndpoint_ShouldUsePostMethod() throws Exception {
        // Load the PmsProductController class
        Class<?> controllerClass = Class.forName("com.gg.guimall.admin.controller.PmsProductController");
        
        // Find the publish method
        Method publishMethod = findMethodByPath(controllerClass, "/{id}/publish");
        assertNotNull(publishMethod, "Publish method not found");
        
        // Check if it uses POST method (RESTful standard for state change operations)
        boolean usesPostMapping = publishMethod.isAnnotationPresent(PostMapping.class);
        boolean usesRequestMapping = false;
        
        if (publishMethod.isAnnotationPresent(RequestMapping.class)) {
            RequestMapping requestMapping = publishMethod.getAnnotation(RequestMapping.class);
            usesRequestMapping = requestMapping.method().length > 0 && 
                                requestMapping.method()[0] == RequestMethod.POST;
        }
        
        // This assertion will FAIL on unfixed code (uses PUT)
        // and PASS on fixed code (uses POST)
        assertTrue(usesPostMapping || usesRequestMapping, 
            "Bug Condition Found: /admin/pms/product/{id}/publish uses PUT method instead of POST. " +
            "State change operations should use POST method according to RESTful standards.");
    }

    /**
     * Property 1: Bug Condition - Unpublish product endpoint should use POST method
     * 
     * Tests that state change operations (like /admin/pms/product/{id}/unpublish) use POST method
     * according to RESTful standards.
     * 
     * On unfixed code: This test will FAIL because /{id}/unpublish uses PUT
     * On fixed code: This test will PASS because /{id}/unpublish uses POST
     */
    @Test
    void testBugCondition_UnpublishProductEndpoint_ShouldUsePostMethod() throws Exception {
        // Load the PmsProductController class
        Class<?> controllerClass = Class.forName("com.gg.guimall.admin.controller.PmsProductController");
        
        // Find the unpublish method
        Method unpublishMethod = findMethodByPath(controllerClass, "/{id}/unpublish");
        assertNotNull(unpublishMethod, "Unpublish method not found");
        
        // Check if it uses POST method (RESTful standard for state change operations)
        boolean usesPostMapping = unpublishMethod.isAnnotationPresent(PostMapping.class);
        boolean usesRequestMapping = false;
        
        if (unpublishMethod.isAnnotationPresent(RequestMapping.class)) {
            RequestMapping requestMapping = unpublishMethod.getAnnotation(RequestMapping.class);
            usesRequestMapping = requestMapping.method().length > 0 && 
                                requestMapping.method()[0] == RequestMethod.POST;
        }
        
        // This assertion will FAIL on unfixed code (uses PUT)
        // and PASS on fixed code (uses POST)
        assertTrue(usesPostMapping || usesRequestMapping, 
            "Bug Condition Found: /admin/pms/product/{id}/unpublish uses PUT method instead of POST. " +
            "State change operations should use POST method according to RESTful standards.");
    }

    /**
     * Helper method to find a method by its path mapping
     */
    private Method findMethodByPath(Class<?> controllerClass, String path) {
        for (Method method : controllerClass.getDeclaredMethods()) {
            // Check GetMapping
            if (method.isAnnotationPresent(GetMapping.class)) {
                GetMapping mapping = method.getAnnotation(GetMapping.class);
                if (matchesPath(mapping.value(), path)) {
                    return method;
                }
            }
            
            // Check PostMapping
            if (method.isAnnotationPresent(PostMapping.class)) {
                PostMapping mapping = method.getAnnotation(PostMapping.class);
                if (matchesPath(mapping.value(), path)) {
                    return method;
                }
            }
            
            // Check PutMapping
            if (method.isAnnotationPresent(PutMapping.class)) {
                PutMapping mapping = method.getAnnotation(PutMapping.class);
                if (matchesPath(mapping.value(), path)) {
                    return method;
                }
            }
            
            // Check DeleteMapping
            if (method.isAnnotationPresent(DeleteMapping.class)) {
                DeleteMapping mapping = method.getAnnotation(DeleteMapping.class);
                if (matchesPath(mapping.value(), path)) {
                    return method;
                }
            }
            
            // Check RequestMapping
            if (method.isAnnotationPresent(RequestMapping.class)) {
                RequestMapping mapping = method.getAnnotation(RequestMapping.class);
                if (matchesPath(mapping.value(), path)) {
                    return method;
                }
            }
        }
        return null;
    }

    /**
     * Helper method to check if a path matches
     */
    private boolean matchesPath(String[] paths, String targetPath) {
        if (paths == null || paths.length == 0) {
            return false;
        }
        for (String p : paths) {
            if (p.equals(targetPath)) {
                return true;
            }
        }
        return false;
    }
}
