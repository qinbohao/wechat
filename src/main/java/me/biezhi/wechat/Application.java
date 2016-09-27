package me.biezhi.wechat;

import com.blade.kit.base.Config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
public class Application {
	private static final Logger LOGGER = LoggerFactory.getLogger(Application.class);
	public static void main(String[] args) {
		try {
			
			Constant.config = Config.load("classpath:config.properties");
			
			WechatRobot wechatRobot = new WechatRobot();
			wechatRobot.showQrCode();
			while(!Constant.HTTP_OK.equals(wechatRobot.waitForLogin())){
				Thread.sleep(2000);
			}
			wechatRobot.closeQrWindow();
			wechatRobot.start(); 
			
		}catch(Exception e){
			LOGGER.error(e.getMessage());
		}
	}
	
}