package net.kanzi.kz.advice;

import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.log4j.Log4j2;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import java.lang.annotation.Annotation;

@Log4j2
@RestControllerAdvice(basePackages = "net.kanzi.kz")
public class CustomResponseBodyAdvice implements ResponseBodyAdvice {


    @Override
    public Object beforeBodyWrite(Object body,
                                  MethodParameter returnType,
                                  MediaType selectedContentType,
                                  Class selectedConverterType,
                                  ServerHttpRequest request,
                                  ServerHttpResponse response) {
        log.info("bodywrite");
        HttpServletResponse servletResponse = ((ServletServerHttpResponse) response).getServletResponse();

        int status = servletResponse.getStatus();
        HttpStatus resolve = HttpStatus.resolve(status);

        if (resolve == null) {
            return body;
        }
        if (resolve.is2xxSuccessful()) {
            return new SuccessResponseDto(status, body);
        }
        return body;
    }

    @Override
    public boolean supports(MethodParameter returnType, Class converterType) {

        Annotation[] declaredAnnotations = returnType.getMethod().getDeclaredAnnotations();
        for(Annotation annot: declaredAnnotations){
            if(annot instanceof DataWrapper){
                return true;
            }
        }

        return false;
    }


}