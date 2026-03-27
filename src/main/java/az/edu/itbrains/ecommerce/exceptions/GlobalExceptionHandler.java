package az.edu.itbrains.ecommerce.exceptions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import jakarta.servlet.http.HttpServletRequest;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(ResourceNotFoundException.class)
    public ModelAndView handleResourceNotFound(ResourceNotFoundException ex, HttpServletRequest request) {
        log.error("404 Not Found — resource: '{}', id: {}, path: {}",
                ex.getResourceName(), ex.getId(), request.getRequestURI());
        ModelAndView mav = new ModelAndView("error/404");
        mav.setStatus(HttpStatus.NOT_FOUND);
        mav.addObject("errorMessage", ex.getMessage());
        mav.addObject("path", request.getRequestURI());
        return mav;
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ModelAndView handleAccessDenied(AccessDeniedException ex, HttpServletRequest request) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated() || auth instanceof AnonymousAuthenticationToken) {
            return new ModelAndView("redirect:/login");
        }
        log.warn("403 Access Denied — path: {}, message: {}", request.getRequestURI(), ex.getMessage());
        ModelAndView mav = new ModelAndView("error/403");
        mav.setStatus(HttpStatus.FORBIDDEN);
        mav.addObject("path", request.getRequestURI());
        return mav;
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<Void> handleNoResourceFound(NoResourceFoundException ex) {
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(Exception.class)
    public ModelAndView handleGeneral(Exception ex, HttpServletRequest request) {
        log.error("500 Internal Server Error — path: {}, exception: {}",
                request.getRequestURI(), ex.getClass().getSimpleName(), ex);
        ModelAndView mav = new ModelAndView("error/500");
        mav.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        mav.addObject("path", request.getRequestURI());
        return mav;
    }
}
