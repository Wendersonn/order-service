package sboot.service.order.config.exceptions;

public class OrderException extends ServiceException{
    public OrderException(String message) {
        super(message);
    }

    public OrderException(String message, Throwable cause) {
        super(message, cause);
    }
}
