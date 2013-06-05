package cc.java.userinfor;

/**
 * �û�����
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
	 * ����û�
	 */
	public String addUser(Node n)
	{
		p = root;
		while((p.next != null))
		{			
			if(p.ID == n.ID)  //�û��Ƿ��Ѿ�����
				return new String("���û����Ѿ����ڣ��뻻���û���!");
			if(p.socket == n.socket)
				return new String("�ظ���¼!");
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
	 * ɾ���û�
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
	 * ����
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
