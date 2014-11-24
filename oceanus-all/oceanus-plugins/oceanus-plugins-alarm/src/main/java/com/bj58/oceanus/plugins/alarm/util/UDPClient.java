package com.bj58.oceanus.plugins.alarm.util;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;

/**
 * 简单的UDP发送工具
 *
 * @author chenyang@58.com 2014年10月24日
 * @see
 * @since 0.0.2
 */
public class UDPClient {
	
	public static void sendMsg(String msg, String ip, int port) throws Exception {
		ByteArrayOutputStream bytesOut = null;
		DataOutputStream dataOut = null;
		DatagramSocket ds = null;
		try {
			bytesOut = new ByteArrayOutputStream();
			dataOut = new DataOutputStream(bytesOut);
	
			ds = new DatagramSocket();
			 
			dataOut.writeBytes(msg);
			dataOut.flush();
			byte[] buffer =msg.getBytes("UTF-8"); //bytesOut.toByteArray();
			DatagramPacket dp = new DatagramPacket(buffer, 
													buffer.length,
													new InetSocketAddress(ip, port));
			
			ds.send(dp);
		} catch (Exception e) {
			throw e;
		} finally {
			if(dataOut != null) {
				try {
					dataOut.close();
				} catch (IOException e) {
					throw e;
				}
			}
			
			if(bytesOut != null) {
				try {
					bytesOut.close();
				} catch (IOException e) {
					throw e;
				}
			}
			
			if(ds != null) {
				ds.close();
			}
		}	
	}

}

