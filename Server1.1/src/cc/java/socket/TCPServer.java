package cc.java.socket;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import cc.java.userinfor.*;

/*
 * ���������߳�
 */

public class TCPServer{
	private static final int Port = 55555 ;
	byte[] bt = new byte[1024];
	String sys_Message = null;
	
	public static boolean Start(UserInfor userList, Socket client) throws IOException
	{
		 boolean isFalse = false;
		 String name = null,back = null;
		 Node user = new Node(client);
		 user.out.println("����ע��!�������û���(������5���ַ�):");
		 
		 while(!isFalse)
		 {
			 name = new String("");
			 String sys = user.in.readLine();
			
			 name = user.in.readLine();
			 System.out.println(name);
			 if((name.length() > 5) || (name.length() < 0))
			 {
				 user.out.println(name.length()+"\t��������:");
				 user.out.flush();
			 }else if(name.length() < 5){
				 int len = 5 - name.length() ;
				 char[] ch = new char[len];
				 while(len >=0 )  //���䳤��Ϊ5
				 {
					 ch[--len] = '0';
				 }
				 user.ID = new String(String.valueOf(ch)+name);
			 }else
				 user.ID = new String(name);
			 back = userList.addUser(user);
			 if(back != null)
			 {
				 user.out.println(back);					 
				 if(back.equals("�ظ���¼!")) isFalse = true;
			 }
			 
		 }
	   return false;
	}

	public static void main(String s[])
	{
		ServerSocket server = null;
		Socket client = null;
		 UserInfor userList;
		userList = new UserInfor();
		try {
			server = new ServerSocket(Port);
			System.out.println("Server is ready!");
			while(true)
			{
			   client = server.accept();
			   if(Start(userList,client))
			   {
				   new Server(client,userList);
			   }
			}			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
  }
}


	
