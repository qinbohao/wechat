package me.command;

import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import org.apache.ibatis.session.SqlSession;

import me.sql.java.MysqlConn;
import me.sql.java.lesson;
import me.biezhi.wechat.robot.MoLiRobot;
import me.biezhi.wechat.robot.Robot;
import me.sql.java.Info;

public class Command {
	public static final List<String> CommTYPE= Arrays.asList("学科竞赛","学生工作","本科生教务","研究生教务","通知公告");
	private static Robot robot = new MoLiRobot();
	public static String getContent(String command){
		String result="";
		
		if(CommTYPE.contains(command)){
			String stat="me.sql.mapper.Infomapper.getdate";
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

		}else if(command.contains("课表")){
			Calendar rightNow = Calendar.getInstance();
			String stat="me.sql.mapper.lessonmapper.getlesson";
			SqlSession session=MysqlConn.getSession();
			lesson les=new lesson();
			les.setId("213150192");
			les=(lesson)session.selectOne(stat,les);
			if((rightNow.get(Calendar.DAY_OF_WEEK)-1)>=1&&(rightNow.get(Calendar.DAY_OF_WEEK)-1)<=5){
				result="今日课表:\r\n【上午】"+les.getAll().get((rightNow.get(Calendar.DAY_OF_WEEK)-2)*2)+"\r\n【下午】"+les.getAll().get((rightNow.get(Calendar.DAY_OF_WEEK)-2)*2+1);
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
	public static void main(String []args){
		//System.out.println(Command.getContent("课表"));
	}

}
