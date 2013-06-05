package cc.java.userinfor;

/**
 * 用户链表
 * @author Administrator
 *
 */
public class UserInfor {
	public Node root;
	Node p;
	int count;
	public UserInfor()
	{
		root = new Node(null);
		root.next = null;
		p = null;
		count = 0;
		
	}
	/*
	 * 添加用户
	 */
	public String addUser(Node n)
	{
		p = root;
		while((p.next != null))
		{			
			if(p.ID == n.ID)  //用户是否已经存在
				return new String("该用户名已经存在！请换个用户名!");
			if(p.socket == n.socket)
				return new String("重复登录!");
			p = p.next;
		}		
		p.next = n;
	//	n.next = null;
		p = p.next;
		p.next = null;
		count++;
		return null;
	}
	
	/*
	 * 删除用户
	 */
	public boolean deleteUser(Node n)
	{
		p = root;
		while(p.next != null)
		{
			if(p.next == n)
			{
				p.next = p.next.next ;
				count--;
				break;
			}
			p = p.next;
		}
		return true;
	}
	
	/*
	 * 查找
	 */
	public Node SearchUser(String ID)
	{
		if(count == 0) return null;
		p = root;	
		while(p.next != null)
		{
			p = p.next ;
			if(p.ID.equalsIgnoreCase(ID)) return p;
		}
		return null;		
	}
	public void PrintID()
	{
		if(count == 0) return ;
		p = root;	
		while(p.next != null)
		{
			System.out.println(p.next.ID);
			p = p.next;
		}
	}
}
