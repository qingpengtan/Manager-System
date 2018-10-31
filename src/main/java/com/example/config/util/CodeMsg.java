package com.example.config.util;

public class CodeMsg {
	
	private int code;
	private String msg;
	
	//通用的错误码
	public static CodeMsg SUCCESS = new CodeMsg(0, "success");
	public static CodeMsg SERVER_ERROR = new CodeMsg(500100, "服务端异常");
	public static CodeMsg BIND_ERROR = new CodeMsg(500101, "参数校验异常：%s");
	//登录模块 5002XX
	public static CodeMsg OUT_LINE = new CodeMsg(500210, "账号未登录");
	public static CodeMsg ACCOUNT_QUIT= new CodeMsg(500211, "账号已过期");
	public static CodeMsg MOBILE_NOT_EXIST = new CodeMsg(500213, "手机号不存在");
	public static CodeMsg DISABLED_ACCOUNT = new CodeMsg(500214, "账号已被禁用");
	public static CodeMsg PASSWORD_ERROR = new CodeMsg(500215, "密码错误");
	public static CodeMsg ACCOUNT_IS_EXIT = new CodeMsg(500216, "号码已被注册");
	public static CodeMsg NO_PERMISSION = new CodeMsg(500217, "您暂无权限查看");
	public static CodeMsg NO_PERMISSION_EDITOR = new CodeMsg(500218, "谢谢你对本文章的意见");
	public static CodeMsg UPLOAD_FAIL = new CodeMsg(600100, "文件太大了，上传失败啦");
	public static CodeMsg UPLOAD_MUSIC_FAIL = new CodeMsg(600101, "您暂无权限上传音乐文件");

	private CodeMsg( ) {
	}
			
	public CodeMsg( int code,String msg ) {
		this.code = code;
		this.msg = msg;
	}
	
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	
	public CodeMsg fillArgs(Object... args) {
		int code = this.code;
		String message = String.format(this.msg, args);
		return new CodeMsg(code, message);
	}

	@Override
	public String toString() {
		return "CodeMsg [code=" + code + ", msg=" + msg + "]";
	}
	
	
}
