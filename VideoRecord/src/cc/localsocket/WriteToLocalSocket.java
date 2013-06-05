package cc.localsocket;

import getFilePath.FileName;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import android.net.LocalServerSocket;
import android.net.LocalSocket;
import android.net.LocalSocketAddress;

public class WriteToLocalSocket extends Thread{
	public LocalSocket receiver,sender;
	private LocalServerSocket lss;
	private static final int size = 500000;
	private final int MAX_FRAME_BUFFER = 1024*20;  //20 kb
	private byte []h264frame;
	private byte []head;
	private byte []sps;
	private byte []pps;
	private File  mediaFile;
	public WriteToLocalSocket()
	{
		receiver = new LocalSocket();
		h264frame = new byte[MAX_FRAME_BUFFER];
		head = new byte[]{0x00,0x00,0x00,0x01};    //头部信息
		sps = new byte[]{(byte)0x67,(byte)0x42,(byte)0x00,(byte)0x0A,(byte)0x96,(byte)0x54,(byte)0x05,
				(byte)0x01,(byte)0xE8,(byte) 0x80};
		pps = new byte[]{(byte)0x68,(byte) 0xCE,(byte)0x38,(byte) 0x80};
		try {
			lss = new LocalServerSocket("H264");			
			receiver.connect(new LocalSocketAddress("H264"));
			receiver.setReceiveBufferSize(size);
			receiver.setSendBufferSize(size);
			
			sender = lss.accept();					
			sender.setReceiveBufferSize(size);
			sender.setSendBufferSize(size);
			mediaFile = FileName.getOutputMediaFile(2);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		start(); //开始线程
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		super.run();
		try {
		//	s = new Socket("192.168.1.125",6000);
			DataInputStream in = new DataInputStream(receiver.getInputStream());
			OutputStream out = new FileOutputStream(mediaFile);	
			//DataOutputStream out = new DataOutputStream(s.getOutputStream());
			
			int count = 0;
			
			in.read(h264frame, 0, 32);  //读取开始的32比特，过滤ftpy
			out.write(h264frame,0,32);
			out.write(head);
			out.write(sps);
			out.write(head);
			out.write(pps);
			int h264len = 0;
			int read = 0;
//			while((count = in.read(h264frame) ) != -1)
//			{
//				out.write(h264frame,0,count);
//				out.flush();
//			}		
			while(true)
			{
				try{
					h264len = in.readInt();
					System.out.println(h264len);
					out.write(head); //写入头部信息
					while(read < h264len)
					{
						int lost = h264len - read;
						count = in.read(h264frame,0,1024 < lost ? 1024:lost);
						read += count;
						out.write(h264frame,0,count);
					}
					System.out.println("实际读到的数据:"+read);
//					byte []h264 = new byte[h264len];
//					System.arraycopy(h264frame, 0, h264, 0, h264len);
//					System.out.println("写入一帧数据");
//					out.write(head);
//					out.write(h264);
					}catch (IOException e) {
						// TODO Auto-generated catch block
						in.close();
						out.close();
						e.printStackTrace();
				}
			}
			//in.close();
			//out.close();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block			
			e.printStackTrace();
		}
	}
}
