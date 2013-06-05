import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;


public class GetSPSandPPS {
	public static byte []sps;
	public static byte []pps;
	public void obtainSPSandPPS(String filename) throws IOException,FileNotFoundException
	{
		File file = new File(filename);
		FileInputStream fin = new FileInputStream(file);
		int filelen = (int) file.length();
		byte[] filedata = new byte[filelen];
		fin.read(filedata);
		
		byte[] avcC = new byte[]{0x61,0x76,0x63,0x43};
		int avcCRecord = 0; //avcC起始位置
		for(int index = 0; index < filelen; ++index)
		{
			if(filedata[index] == avcC[0] && filedata[index+1] == avcC[1] &&
					filedata[index+2] == avcC[2] && filedata[index+3] == avcC[3])
			{
				avcCRecord = index + 4;  //avcC的起始位置
				break;
			}
		}
		if(avcCRecord == 0)
		{
			System.out.println("avcCRecord 没找到!");
		}
		int spspos = avcCRecord + 6 ; //跳过6字节首部说明
		byte []spsbt = new byte[]{filedata[spspos],filedata[spspos+1]};   //获取sps字节数
		int spslen = bytetoInt(spsbt);
		sps = new byte[spslen];
		spspos += 2;   //跳过表示sps长度的两字节
		System.arraycopy(filedata, spspos, sps, 0, spslen);
		printResult("SPS",sps,spslen);
		
		int ppspos = spspos + spslen + 1;  //跳过一字节的表示pps数量
		
		byte []ppsbt = new byte[]{filedata[ppspos],filedata[ppspos + 1]}; //
		int ppslen = bytetoInt(ppsbt);
		pps = new byte[ppslen];
		ppspos += 2;
		System.arraycopy(filedata, ppspos, pps, 0, ppslen);
		printResult("PPS",pps,ppslen);
		fin.close();
	}
	private int bytetoInt(byte[] bt) {
		// TODO Auto-generated method stub
		int ret = bt[0];
		ret <<= 8;
		ret |= bt[1];
		return ret;
	}
	private void printResult(String type,byte[] bt,int len)
	{
		System.out.println(type+"长度为:"+len);
		String cont = type + "的内容为:";
		System.out.println(cont);
		for(int i =0; i < len; i++)
		{
			System.out.printf("%02x ",bt[i]);
		}
		System.out.println("\n");
	}
	public static void main(String []args) throws FileNotFoundException, IOException
	{
		new GetSPSandPPS().obtainSPSandPPS("myvideo.mp4");
	}
}
