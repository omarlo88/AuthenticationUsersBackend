package omar.lo.web;

import omar.lo.entities.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

//@RestControllerAdvice
@ControllerAdvice
public class AuthenticationNotFoundAdvice {

    @ResponseBody
    @ExceptionHandler(AppUserNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String appUserNotFoundException(AppUserNotFoundException ex){
        return ex.getMessage();
    }

    @ResponseBody
    @ExceptionHandler(AppRoleNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String appRoleNotFoundException(AppRoleNotFoundException ex){
        return ex.getMessage();
    }

    @ResponseBody
    @ExceptionHandler(AppUserExisteException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public String appUserExisteException(AppUserExisteException ex){
        return ex.getMessage();
    }

    @ResponseBody
    @ExceptionHandler(AppRoleExisteException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public String appRoleExisteException(AppRoleExisteException ex){
        return ex.getMessage();
    }

    @ResponseBody
    @ExceptionHandler(RoleUserNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String roleUserNotFoundException(RoleUserNotFoundException ex){
        return ex.getMessage();
    }
}
