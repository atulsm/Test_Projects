import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStreamReader;
import java.util.zip.GZIPInputStream;

import sun.misc.BASE64Decoder;
import esecurity.base.security.SecurityHandler;


public class TestBase64DecodeGzip {
	
	public static void main(String[] args) {
		//read from stdin - compress - write to stdout - uncompress and write to stdout
		try{
			String input = "1:H4sIAAAAAAAAAK2UbWvbQAzHv0rI++4ug3VrubiMroHCkpY8dLB3F59i37BP5k5Om29fnRO62DTQeTH3Qiejn/+SLKmbl7IYbMEHi248HH2SwwG4FI112Xi4Wk4uvg1vEnW3BUcLrH0Kt+g2Nqu9Jo5I1PcCPP3Kwc3whyadjJToutTS14GOEEtbQiKVeNevFuDMHCrQBGZPaIAhRpx8p/a3Ze4h5FiY5FLyo0TXrRZ6C3P93AiLwKOr4tQcpDGvKRqYaVZT6kqJd/wqRsxZxk9bWoqktkM9eqxYmYXwZu9YZYyt0BMrazBPuqghGX35zHXb2+o+3HJZsIzQN1uJv5AOzqxpV0Eb+OB1WkBvZMxlgr7UHZ1NNQ7M09E5BnJstGMJAvUWNOdg7WlWl2vwba7sDa10CM/oTacX+fRushbZb3TCrEzl5YrSbDzu3x8u5lqHc5bj3lmyuljEmjxsYrc6OVx9lRdyxGcg5XVzPtA3vhKmWDztt0EbefkhRB3An7n1xlteT3+0t2UcHOi0i3zd/z/PgA7Df0YobjYB6BELm+7a2Krx8TYh6/7jA4dhmDKEK9vp/D+MgzjeUeLkin8FlHjhSiMGAAA=";
			byte[] inArr = new BASE64Decoder().decodeBuffer(input);
			
			System.out.println(SecurityHandler.decryptAESHardKey(input));
			
			GZIPInputStream gin = new GZIPInputStream(new ByteArrayInputStream(inArr));
			InputStreamReader in = new InputStreamReader(gin);
			System.out.println(new BufferedReader(in).readLine());
			in.close();
			
		}catch (Exception e) {
			e.printStackTrace();
		}
		
	}

}
