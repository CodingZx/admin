package lol.cicco.admin.common;

import com.google.common.collect.Lists;
import lol.cicco.admin.common.exception.UnknownException;
import lol.cicco.admin.common.model.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;

@Slf4j
@ResponseBody
@ControllerAdvice
public class WebExceptionConfiguration {
	private static List<Class<? extends Exception>> IGNORE_EXCEPTION = Lists.newLinkedList();

	static {
		IGNORE_EXCEPTION.add(MissingServletRequestParameterException.class);
		IGNORE_EXCEPTION.add(HttpRequestMethodNotSupportedException.class);
	}

	@ResponseStatus(HttpStatus.OK)
	@ExceptionHandler(Exception.class)
	public R handleException(Exception e) {
		// 不打印异常
		if (IGNORE_EXCEPTION.contains(e.getClass())) {
			return R.error(e.getMessage());
		}
		log.warn("程序异常:{}", e.getMessage(), e);
		return R.error(e.getMessage());
	}

	/**
	 * 属性校验未通过
	 */
	@ResponseBody
	@ExceptionHandler({BindException.class, MethodArgumentNotValidException.class})
	@ResponseStatus(HttpStatus.OK)
	public R handleValidationFailure(Exception ex) {
		FieldError err;
		if (ex instanceof BindException) {
			err = ((BindException) ex).getFieldError();
		} else {
			err = ((MethodArgumentNotValidException) ex).getBindingResult().getFieldError();
		}
		if (err != null) {
			return R.other(err.getDefaultMessage());
		}
		throw UnknownException.make(ex);
	}
}
