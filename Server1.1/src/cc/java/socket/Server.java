package cc.java.socket;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

import cc.java.userinfor.*;

 public class Server extends Thread{
	Node user;
	UserInfor userList;
	String destination = null;
	String source = null;
	char[] ch = new char[10];
	public Server(Socket client, UserInfor userList) {
		// TODO Auto-generated constructor stub
		this.user = new Node(client);
		this.userList = userList;
		start(); //开始线程
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub						
	   while(true)
	  {
		String str = null;
		try {
			destination = user.in.readLine().substring(12);
			source = user.ID;
			str = user.in.readLine().substring(9);
			if(str.equalsIgnoreCase("quit"))
			{
				user.out.println("quit");
				user.out.flush();
				SendToAll(user.ID + " exit!");
				user.in.close();
				user.out.close();
				user.socket.close();
				System.out.println("client: "+user.socket+" has disconnected!");
				return ;				
			}
			
			System.out.println("From:"+user.ID+"\n-->"+str);
					SendToAll(str);
		} catch (IOException e) {
					// TODO Auto-generated catch block
			try {
				user.in.close();
				user.out.close();
				user.socket.close();
			   } catch (IOException e1) {
						// TODO Auto-generated catch block
					System.out.println("close failed!");
					e1.printStackTrace();
				}
			}
		}
	}
	public void SendToAll(String str)
	{
		Node p;
		p = userList.root;
		while((p.next != null) && (p.next.out != null))
		{
			send(str,p.next.out);
			p = p.next;
		}
	}
	public void send(String str,PrintWriter output)
	{
		output.println(str);
		output.flush();
	}
}