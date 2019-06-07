package lol.cicco.admin.common;

import lol.cicco.admin.common.model.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@Slf4j
@ResponseBody
@ControllerAdvice
public class WebExceptionConfiguration {
	
	@ResponseStatus(HttpStatus.OK)
	@ExceptionHandler(Exception.class)
	public R handleException(Exception e) {
		log.error("服务异常", e);
		return R.error();
	}
}
