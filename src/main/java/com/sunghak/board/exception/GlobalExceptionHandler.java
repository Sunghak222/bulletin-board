package com.sunghak.board.exception;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ResponseStatusException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(EntityNotFoundException.class)
    public String handleEntityNotFound(EntityNotFoundException e, Model model) {
        model.addAttribute("errorMessage", e.getMessage());
        return "error/404";
    }

    @ExceptionHandler(ResponseStatusException.class) // ← 추가
    public String handleResponseStatus(ResponseStatusException e, Model model) {
        model.addAttribute("errorMessage", e.getReason()); // getMessage()는 상태코드 포함됨
        model.addAttribute("statusCode", e.getStatusCode().value()); // 선택적
        return "error/403"; // 또는 404/500 등 상황에 맞는 뷰
    }
}
