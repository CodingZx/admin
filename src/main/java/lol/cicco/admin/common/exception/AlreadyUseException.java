package lol.cicco.admin.common.exception;

import lombok.Data;

@Data
public class AlreadyUseException extends RuntimeException {

    private String msg;

    public AlreadyUseException(String msg){
        this.msg = msg;
    }
}
