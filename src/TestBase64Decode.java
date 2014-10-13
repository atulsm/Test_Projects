import sun.misc.BASE64Decoder;


public class TestBase64Decode {

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception{
		BASE64Decoder decoder = new BASE64Decoder();
		
		byte[] bytes = decoder.decodeBuffer("H4sIAAAAAAAAAL3TS0+DQBSG4Wm91lq13i8Lj4kaE6Om1rjXJkpMiNqF+yMdWxIYkBmsP98ZU1YgEPXAgoRZfA+zeOtHe4eMsWY/9rjNpcQh1593Df26SZ/XGWux5Km19y384IDgu8L1Yx+CN7gCZ4QROopH8jxj+d4s3+Yu1/QyJMv4mSx3rgumbTNt5U7X9fRx+qe7EAv3PeYFwKMBHnKBKQ2c9AKh0BWACjyOUsEleMGYRw7KfyCmfyDiMCxF9A1h5xIzmjhNEV1ALxzhK1eug97fLzKbeZEOiNjnUQnixRDPucScJi6yiECcfV9mYhVIlpF6udK8lg6eUMpxEA3AmZARl0rPKz4Acy5/l1qDLLUFutSa1Kkt0qfWok9tqZLUlulTW6kstTZhaqtkqa3RpbZOndoGfWqb9KltVZLaNn1qO5WltlsqtS9XidI8RAoAAA==");
		System.out.println(new String(bytes));
		
	}

}
