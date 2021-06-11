package meli.desafio_quality.dto;

public class ErrorMessageDTO {

    private String field;
    private String message;

    public ErrorMessageDTO() {
    }

    public ErrorMessageDTO(String field, String message) {
        this.field = field;
        this.message = message;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
