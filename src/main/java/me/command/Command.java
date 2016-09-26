package me.command;

import java.util.Arrays;
import java.util.List;

import org.apache.ibatis.session.SqlSession;

import me.sql.java.MysqlConn;
import me.biezhi.wechat.robot.MoLiRobot;
import me.biezhi.wechat.robot.Robot;
import me.sql.java.Info;

public class Command {
	public static final List<String> CommTYPE= Arrays.asList("学科竞赛","学生工作","本科生教务","研究生教务","通知公告");
	private static Robot robot = new MoLiRobot();
	public static String getContent(String command){
		String result="";
		String stat="me.sql.mapper.Infomapper.getdate";
		if(CommTYPE.contains(command)){
			Info tmp=new Info();
			tmp.setType(command.trim());
			SqlSession session=MysqlConn.getSession();
			java.util.List<Object> list=(java.util.List<Object>) session.selectList(stat,tmp);
			int i=0;
			for(Object info:list){
				i++;
				result+= i+".  "+((Info)info).getTxt()+"\r\n";
			}
			session.close();

		}else if(command.contains("学生工作")){
			
			
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
