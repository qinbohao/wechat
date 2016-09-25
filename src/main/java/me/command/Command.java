package me.command;

import org.apache.ibatis.session.SqlSession;

import me.sql.java.MysqlConn;
import me.biezhi.wechat.robot.MoLiRobot;
import me.biezhi.wechat.robot.Robot;
import me.sql.java.Info;

public class Command {
	private static Robot robot = new MoLiRobot();
	public static String getContent(String command){
		String result="";
		String stat="me.sql.mapper.Infomapper.getdate";
		if(command.contains("竞赛")){
			Info tmp=new Info();
			tmp.setType("学科竞赛");
			SqlSession session=MysqlConn.getSession();
			java.util.List<Object> list=(java.util.List<Object>) session.selectList(stat,tmp);
			for(Object info:list){
				result+= ((Info)info).getTxt()+"\r\n";
			}
			session.close();

		}else{
			try{
				result="不好意思，网络超时啦~~~~";
				result = robot.talk(command);
			}catch(Exception e){
				e.printStackTrace();

			}
		}
		return result;
	}

}
