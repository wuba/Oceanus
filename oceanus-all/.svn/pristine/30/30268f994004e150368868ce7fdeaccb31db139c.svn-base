package com.bj58.oceanus.plugins.alarm.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * poster 服务的内部udp发送短信端口
 *
 * <p>
 * 这个poster服务端口仅供内部报警使用，传递参数手机号与短信内容，格式为json
 * </p>
 * @author chenyang@58.com 2014年10月27日
 * @see
 * @since 0.0.2
 */
public final class PosterUdpSms {
	
	private static Logger logger = LoggerFactory.getLogger(PosterUdpSms.class);
	
	private static final String HOST_POSTER = "poster1.service.58dns.org";
	private static final int PORT_POSTER = 10086;

	public static void sendPostSms(String[] mobiles, String content) {
		StringBuilder udpData = new StringBuilder();
		udpData.append("{\"mobile\":[");
		for(int i=0; i<mobiles.length; i++){
			if(i>0){
				udpData.append(",");
			}
			udpData.append("\"").append(mobiles[i]).append("\"");
		}
		udpData.append("],\"content\":\"");
		udpData.append(content);
		udpData.append("\"}");
		
		try {
			UDPClient.sendMsg(udpData.toString(), HOST_POSTER, PORT_POSTER);
		} catch (Exception e) {
			logger.error("", e);
			e.printStackTrace();
		}
	}
	
}

