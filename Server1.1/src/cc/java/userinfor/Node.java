package cc.java.userinfor;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;


public class Node {
	public String ID;
	public Socket socket;
	public BufferedReader in;
	public PrintWriter out;
	
	public Node next;
	
	public Node(Socket client)
	{
		this.ID = null;
		this.next = null;
		this.socket = client;
		if(client != null)
		{
			try {
				this.in = new BufferedReader(new InputStreamReader(client.getInputStream()));
				this.out = new PrintWriter(new OutputStreamWriter(client.getOutputStream()),true);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else{
			this.in = null;
			this.out = null;
		}
	}
}
