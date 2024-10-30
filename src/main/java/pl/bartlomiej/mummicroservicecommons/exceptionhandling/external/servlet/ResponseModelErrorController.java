package pl.bartlomiej.mummicroservicecommons.exceptionhandling.external.servlet;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.bartlomiej.mummicroservicecommons.model.response.ResponseModel;

@RestController
public class ResponseModelErrorController implements ErrorController {

    @RequestMapping("/error")
    public ResponseEntity<ResponseModel<Void>> handleError(HttpServletRequest request) {
        Integer statusCode = (Integer) request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        HttpStatus httpStatus = HttpStatus.valueOf(statusCode);
        return ResponseEntity.status(httpStatus)
                .body(ResponseModel.buildBasicErrorResponseModel(httpStatus, httpStatus.getReasonPhrase()));
    }
}