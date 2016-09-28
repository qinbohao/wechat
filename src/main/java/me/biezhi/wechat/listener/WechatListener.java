package me.biezhi.wechat.listener;

import java.util.Calendar;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.blade.kit.json.JSONObject;

import me.biezhi.wechat.model.WechatMeta;
import me.biezhi.wechat.service.WechatService;
import me.command.Command;

public class WechatListener {

	private static final Logger LOGGER = LoggerFactory.getLogger(WechatListener.class);

	int playWeChat = 0;
	boolean dateflag=true;
	public void start(final WechatService wechatService, final WechatMeta wechatMeta){
		new Thread(new Runnable() {
			@Override
			public void run() {
				LOGGER.info("进入消息监听模式 ...");
				wechatService.choiceSyncLine(wechatMeta);
				while(true){
					try {
						//LOGGER.info("DDDD");
						int[] arr = wechatService.syncCheck(wechatMeta);
						// LOGGER.info("DDDDD");
						Calendar rightNow = Calendar.getInstance();
						LOGGER.debug("retcode={}, selector={}", arr[0], arr[1]);

						if(arr[0] == 1100||arr[0]==1101){
							LOGGER.info("你在手机上登出了微信，债见");
							break;
						}
						if(arr[0] == 0){
							if(arr[1] == 2){
								//LOGGER.info("arr[1]==2");
								JSONObject data = wechatService.webwxsync(wechatMeta);
								wechatService.handleMsg(wechatMeta, data);
							//	LOGGER.info("arr[1]==2完毕");
							} else if(arr[1] == 6){
								//LOGGER.info("arr[1]==6");
								JSONObject data = wechatService.webwxsync(wechatMeta);
								wechatService.handleMsg(wechatMeta, data);
								//LOGGER.info("arr[1]==2完毕");
							} else if(arr[1] == 7){
								playWeChat += 1;
								LOGGER.info("你在手机上玩微信被我发现了 {} 次", playWeChat);
								wechatService.webwxsync(wechatMeta);
							} else if(arr[1] == 3){
								//continue;
							} else if(arr[1] == 0){
								//continue;
							}													
						} else {
							// 
						}
						//每天推送信息
						if(dateflag&&(rightNow.get(Calendar.MINUTE)>=47)&&rightNow.get(Calendar.HOUR_OF_DAY)==6){
							dateflag=false;
							wechatService.webwxsendmsg(wechatMeta, Command.getContent("课表"), wechatService.getUserName("洋洋洋洋洋"));
							wechatService.webwxsendmsg(wechatMeta, Command.getContent("课表"), wechatService.getUserName("空白"));
							LOGGER.info("发送课表");	
						}else if(rightNow.get(Calendar.MINUTE)==50){
							dateflag=true;
						}
						LOGGER.info("等待~2000ms...");
						System.gc();
						Thread.sleep(2000);
					} catch (Exception e) {
						LOGGER.info(e.getMessage());
					}
				}
			}
		}, "wechat-listener-thread").start();
	}

}
