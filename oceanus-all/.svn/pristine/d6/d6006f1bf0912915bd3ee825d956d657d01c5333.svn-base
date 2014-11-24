package com.bj58.oceanus.plugins.alarm;

import com.bj58.oceanus.core.alarm.AlarmType;
import com.bj58.oceanus.core.alarm.PeriodicAlarm;
import com.bj58.oceanus.core.exception.ConfigurationException;

/**
 * http发送报警短信抽象接口
 *
 * <p>
 * 子类需要实现相应的参数值返回，报警短信格式见：
 * @see getMessage()
 * </p>
 * @author chenyang@58.com 2014年10月28日
 * @see
 * @since 0.0.2
 */
public abstract class HttpSmsPeriodicAlarm extends PeriodicAlarm{

	/**
	 * 接收报警短信的手机号
	 * @return
	 */
	protected abstract String[] getMobiles();
	
	/**
	 * 自定义报警内容
	 * 
	 * 短信全格式：“{dataNodeId} {AlarmType} {message}”
	 * 建议自定义内容包含机器IP，便于说明哪台机器出了问题
	 * @return
	 */
	protected abstract String getMessage();
	
	/**
	 * 短信模板ID（poster服务必填参数）
	 * @return
	 */
	protected abstract String getTemplateId();
	
	
	/**
	 * 密码（poster服务必填参数）
	 * @return
	 */
	protected abstract String getPassword();	
	
	@Override
	protected void excuteAlarm(AlarmType type, String dataNodeId) {
		
		
		// TODO 接口待提供
		throw new ConfigurationException("HTTP Alarm not allowd !");
	}

	
}

