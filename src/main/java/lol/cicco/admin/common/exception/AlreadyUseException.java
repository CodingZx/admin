package lol.cicco.admin.common.exception;

import lombok.Getter;

public class AlreadyUseException extends RuntimeException {

    @Getter
    private String msg;

    public AlreadyUseException(String msg){
        this.msg = msg;
    }
}
