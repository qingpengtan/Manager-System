package com.example.config.exception;

import com.example.config.util.CodeMsg;
import com.example.config.util.Result;
import com.example.controller.IndexControl;
import com.example.entity.LogEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BindException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;

@ControllerAdvice
@ResponseBody
public class GlobalExceptionHandler {

	private Logger log = LoggerFactory.getLogger(this.getClass());

	@ExceptionHandler(value=Exception.class)
	public Result<String> exceptionHandler(HttpServletRequest request, Exception e){
		e.printStackTrace();
		if(e instanceof GlobalException) {
			GlobalException ex = (GlobalException)e;
			return Result.error(ex.getCm());
		}else if(e instanceof BindException) {
			BindException ex = (BindException)e;
			List<ObjectError> errors = ex.getAllErrors();
			ObjectError error = errors.get(0);
			String msg = error.getDefaultMessage();
			return Result.error(CodeMsg.BIND_ERROR.fillArgs(msg));
		}else {
			log.error(this.getTrace(e));
			return Result.error(new CodeMsg(500100,"服务器出错啦"));
		}
	}


	public  String getTrace(Throwable t) {
		StringWriter stringWriter= new StringWriter();
		PrintWriter writer= new PrintWriter(stringWriter);
		t.printStackTrace(writer);
		StringBuffer buffer= stringWriter.getBuffer();
		return buffer.toString();
	}

	/**
	 * 默认未知异常
	 * @param request
	 * @param e
	 * @return
	 * @throws Exception
	 */
//	@ExceptionHandler(value = Exception.class)
//	@ResponseBody
//	public BaseView defaultErrorHandler(HttpServletRequest request, Exception e) throws Exception {
//		BaseView baseView = new BaseView(CodeConstants.SYSTEM_EXCEPTION,CodeConstants.SYSTEM_EXCEPTION_MSG);
//		printLog(request,e,baseView);
//		return baseView;
//	}
//
//	/**
//	 * 参数异常
//	 * @param request
//	 * @param e
//	 * @return
//	 * @throws Exception
//	 */
//	@ExceptionHandler(value = {HttpMessageNotReadableException.class, MissingServletRequestPartException.class ,MissingServletRequestParameterException.class, MultipartException.class})
//	@ResponseBody
//	public BaseView httpMessageNotReadableExceptionErrorHandler(HttpServletRequest request, Exception e) throws Exception {
//		BaseView baseView = new BaseView(CodeConstants.PARAMETER_ERROR,CodeConstants.PARAMETER_ERROR_MSG);
//		printLog(request,e,baseView);
//		return baseView;
//	}
//	/**
//	 * contentType异常
//	 * @param request
//	 * @param e
//	 * @return
//	 * @throws Exception
//	 */
//	@ExceptionHandler(value = {HttpMediaTypeNotSupportedException.class})
//	@ResponseBody
//	public BaseView httpMediaTypeNotSupportedExceptionHandler(HttpServletRequest request, Exception e) throws Exception {
//		BaseView baseView =  new BaseView(CodeConstants.CONTENTTYPE_ERROR,CodeConstants.CONTENTTYPE_ERROR_MSG);
//		printLog(request,e,baseView);
//		return baseView;
//	}
//
//	/**
//	 * 异常信息打印日志
//	 * @param request
//	 * @param e
//	 * @param baseView
//	 */
//	private void printLog(HttpServletRequest request,Exception e,BaseView baseView){
//		log.error(AppUtil.getExceptionDetail(e));
//		LogEntity logEntity = new LogEntity();
//		logEntity.setHttpMethod(request.getMethod());
//		logEntity.setUrl(request.getRequestURL().toString());
//		logEntity.setIp(request.getRemoteAddr());
//		logEntity.setArgs(AppUtil.getRequestBody(request));
//		logEntity.setLogType(AppConstants.LOG_TYPE_HTTP);
//		logEntity.setReqParams(JsonUtil.objectToJson(request.getParameterMap()));
//		logEntity.setRespParams(JsonUtil.objectToJson(baseView));
//		log.error(">>>"+JsonUtil.objectToJson(logEntity));
//	}
}
