//package esecurity.ccs.testcases.evtsrcmgt.connector.wms;

import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.charset.Charset;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Enumeration;
import java.util.List;
import java.util.Random;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.GZIPInputStream;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;
//import com.esecurity.util.SocketUtil;




public class TestSSLServer {
	
	private static String PORT= "12890";

	private static String connectionID;

	public static final String KEYSTORE_TYPE = "JKS"; //$NON-NLS-1$

	public static final String DEFAULT_ALIAS = "syslog_alias"; //$NON-NLS-1$

	private String sysCertB64;

	private String sysPKeyB64;

	private String keyAlg;

	private ConnectorX509TrustManager trustManager;

	private String trustStoreCerts = null;

	private String authType;
	
	private static ServerSocket server = null;

	public static void main(String[] args) {
		try{
			TestSSLServer serv = new TestSSLServer(PORT,ConnectorX509TrustManager.DEFAULT_CERT,
				ConnectorX509TrustManager.DEFAULT_PKEY,"RSA",null,"OPEN");
			serv.processAccept();
			server.close();
		}catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Creates DeviceSensorTCPListener
	 */
	public TestSSLServer(String serverPort,
			String slsCertB64, String slsPKeyB64, String keyAlg, String trustStoreCerts, String authType) {
		initSocket(serverPort);
		this.sysCertB64 = slsCertB64;
		this.sysPKeyB64 = slsPKeyB64;
		this.keyAlg = keyAlg;
		this.trustStoreCerts = trustStoreCerts;
		this.authType = authType;
	}
	
	private void initSocket(String serverPort) {
		try {
			int port = -1;
			InetAddress addr = null;

			if (serverPort.indexOf(':') > -1) {
				String[] portParts = serverPort.split(":"); //$NON-NLS-1$
				if (portParts.length > 1) {
					addr = InetAddress.getByName(portParts[0]);
					port = Integer.parseInt(portParts[1]);
				}
			} else {
				port = Integer.parseInt(serverPort);
			}

			InetSocketAddress iNetSocketAddress = null;
			if (addr != null) {
				iNetSocketAddress = new InetSocketAddress(addr, port);
			} else {
				iNetSocketAddress = new InetSocketAddress(port);
			}
			server = new ServerSocket();
			server.bind(iNetSocketAddress);

			System.out.println("Listening for TCP connections on " 
					+ iNetSocketAddress.getAddress() //$NON-NLS-1$
					+ ":" + port); //$NON-NLS-1$

		} catch (UnknownHostException unKnownHostException) {
			throw new RuntimeException("TCPListener initSocket", //$NON-NLS-1$
				unKnownHostException);
		} catch (IOException ioException) {
			throw new RuntimeException("TCPListener initSocket", ioException); //$NON-NLS-1$
		}
	}

	BufferedWriter mgmtBos = null;
	InputStream mgmtBis = null;
	
	protected void processAccept() throws IOException {
		// Convert to an ssl socket then do the normal processing.
		
		Socket socket = server.accept();
		try{
		socket = convertToSSL(socket);
		
		mgmtBos = new BufferedWriter(new PrintWriter(socket.getOutputStream()));
		mgmtBis = socket.getInputStream();
		Boolean compress = false;
		sendMgmtInitializationdata(compress);
		//read();
		sendADDServer();
		final InputStream in;
		if( compress){
			in = new GZIPInputStream(mgmtBis);
		}else{
			in = mgmtBis;
		}

		String inComplete = "";
		
		new Thread(){

			@Override
			public void run() {
				try{
					sleep(10000);
					System.out.println("Before close()");
					in.close();
					System.out.println("After close()");
				}catch (Exception e) {
					e.printStackTrace();
				}
				
			}
			
		}.start();
		
		while(true){
			read(in,inComplete);
		}
		}catch (Exception e) {
			e.printStackTrace();
		}finally{
			socket.close();
		}

	}
	
	private void read( InputStream in, String inComplete  ) throws IOException{
		String str = null;
		int BUF_MAX = 8192;
		int length = 0;

		byte[] buf = new byte[BUF_MAX];

		// wouldn't it be nice if there where a TextInputStream
		while((length=in.read(buf, 0, BUF_MAX)) >0) {					
			str = new String(Arrays.copyOf(buf, length),Charset.defaultCharset());
			System.out.println("Received:" + str);
//			inComplete += str;		
		}
		
	}
	
	private void sendMgmtInitializationdata( Boolean compression ) throws IOException {
		if( compression )
			mgmtBos.write("#$%MANAGEMENT%$#EB8A2C20-1243-102E-A509-005056C00008%$#Compression=true\n");
		else 
			mgmtBos.write("#$%MANAGEMENT%$#EB8A2C20-1243-102E-A509-005056C00008%$#Compression=false\n");
		mgmtBos.flush();
	}
	
	private void sendADDServer() throws IOException {
		mgmtBos.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?><EventLog_Config>	<Config_Node RequestType=\"ADD_SERVER\" Server_Name=\"localhost\" Unique_Id=\"EB8A2C20-1243-102E-A509-005056C00001\" User_Name=\"a\" Secret=\""+EncryptDecrypt.encryptHardKey("TestEncryption")+"\">		<Log_Type Date_Offset=\"19700101053000.000000+330\" Log_Name=\"Application\"/>	</Config_Node></EventLog_Config>\n");
		mgmtBos.flush();
	}
	/**
	 * Converts a plain socket, received as an parameter and returns it as an SSL Socket
	 * 
	 * @param soc
	 *            -plain socket
	 * @return SSL Socket over the existing socket
	 */
	private SSLSocket convertToSSL(Socket soc) {
		Socket skt = soc;
		SSLSocket sslSocket = null;
		connectionID = skt.getRemoteSocketAddress().toString();
		// logger.audit("{0} Generating the required streams",
		// connectionID);
			try {
			SSLContext context = getContext();
				SSLSocketFactory factory = context.getSocketFactory();
				sslSocket = (SSLSocket) factory.createSocket(skt, skt.getLocalAddress().toString(), skt.getPort(),
				false);
			sslSocket.setWantClientAuth(false);
			sslSocket.setUseClientMode(false);
			// Restrict weak protocols and ciphers
			SocketUtil.restrictSocket(sslSocket);
			// Enable SSLv2Hello to have compatability with some SSL clients sending the SSLv3 hello message in
			// SSLv2
			ArrayList<String> enabledProtocols = new ArrayList<String>();
			enabledProtocols.add("SSLv3"); //$NON-NLS-1$
			//enabledProtocols.add("SSLv2Hello"); //$NON-NLS-1$
			enabledProtocols.add("TLSv1"); //$NON-NLS-1$
			/* IBMJSSE2 Provider doesn't support "SSLv2Hello" protocol for the SSLSocket class. But the server
			 * side connection always accepts an "SSLv2Hello". This is because majority of SSL clients like
			 * IE, firefox still use "SSLv2Hello". So, add the "SSLv2Hello" to the enabled protocols list
			 * ONLY IF it is supported in underlying JRE (eg. SUN).
			 * http://www.ibm.com/developerworks/java/jdk/security/142/secguides/jsse2docs/JSSE2RefGuide.html
			 * The following code ensures "SSLv2Hello" is added in case of Sun JRE and omitted in case of
			 * IBM JRE.
			 */
				
			ArrayList<String> supportedProtocols = new ArrayList<String>(Arrays.asList(sslSocket.getSupportedProtocols()));
			if (supportedProtocols.contains("SSLv2Hello")) {//$NON-NLS-1$
				enabledProtocols.add("SSLv2Hello"); //$NON-NLS-1$
			}
				sslSocket.setEnabledProtocols(enabledProtocols.toArray(new String[enabledProtocols.size()]));
			} catch (Exception e) {
				e.printStackTrace();
		}
		return sslSocket;
	}

	private SSLContext getContext() {
		SSLContext context = null;
		try {
			context = SSLContext.getInstance("SSLv3"); //$NON-NLS-1$
			// construct a temporary keystore in memory
			/* Specify only the keystore type "JKS". No need to specify the package provider like "SUN"
			 * The system will determine the provider from the environment
			 */
			KeyStore keyStore = KeyStore.getInstance("JKS"); //$NON-NLS-1$
			Random rand = new Random();
			String keyStorePass = String.valueOf(rand.nextInt(2147483647));
			keyStore.load(null, keyStorePass.toCharArray());
			ByteArrayInputStream bais = null;
			DataInputStream dis = null;

			// private key; must be unencrypted PKCS#8 w/DER encoding
			KeyFactory keyFactory = KeyFactory.getInstance(keyAlg); 
			bais = new ByteArrayInputStream(Base64Coder.decode(sysPKeyB64.toCharArray()));
			dis = new DataInputStream(bais);
			byte[] key = new byte[dis.available()];
			dis.readFully(key);
			PKCS8EncodedKeySpec keysp = new PKCS8EncodedKeySpec(key);
			PrivateKey pKey = keyFactory.generatePrivate(keysp);

			// public certificate; must be X.509 w/DER encoding
			CertificateFactory certFactory = CertificateFactory.getInstance("X.509"); //$NON-NLS-1$
			bais = new ByteArrayInputStream(Base64Coder.decode(sysCertB64.toCharArray()));
			dis = new DataInputStream(bais);
			byte[] cert = new byte[dis.available()];
			dis.readFully(cert);
			bais = new ByteArrayInputStream(cert);
			InputStream is = bais;
			Collection c = certFactory.generateCertificates(is);
			Certificate[] certs = new Certificate[c.toArray().length];
			if (c.size() == 1) {
				byte[] bytes = Base64Coder.decode(sysCertB64.toCharArray());
				bais = new ByteArrayInputStream(bytes);
				Certificate pubCert = certFactory.generateCertificate(bais);
				certs[0] = pubCert;
			} else {
				certs = (Certificate[]) c.toArray();
			}

			// use the same random password on both the private key and keystore
			keyStore.setKeyEntry("WMI", pKey, keyStorePass.toCharArray(),
				certs);

			/* Get the default keymanager algorithm defined in java.security file (JRE_HOME/lib/security/java.security)
			 * Default algorithm is SunX509 case of Sun JRE and IbmX509 in case of IBM JRE
			 */
			KeyManagerFactory keyMgrFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
			keyMgrFactory.init(keyStore, keyStorePass.toCharArray());

			// construct a temporary truststore in memory
			/* Specify only the keystore type "JKS". No need to specify the package provider like "SUN"
			 * The system will determine the provider from the environment
			 */
			KeyStore trustStore = KeyStore.getInstance("JKS"); //$NON-NLS-1$
			trustStore.load(null, keyStorePass.toCharArray());

			Random randomAlias = new Random();
			// initialize truststore
			if (trustStoreCerts != null) {
				String[] tempCerts = trustStoreCerts.split("~~");

				for (String tempCert : tempCerts) {
					bais = new ByteArrayInputStream(Base64Coder.decode(tempCert.toCharArray()));
					dis = new DataInputStream(bais);
					byte[] crt = new byte[dis.available()];
					dis.readFully(crt);
					bais = new ByteArrayInputStream(crt);
					InputStream istream = bais;
					Certificate certi = certFactory.generateCertificate(istream);
					trustStore.setCertificateEntry(String.valueOf(randomAlias.nextInt()), certi);
				}
			}

			// define our trust manager
			trustManager = new ConnectorX509TrustManager(trustStore, authType);

			context.init(keyMgrFactory.getKeyManagers(), new TrustManager[] { trustManager }, null);

		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return context;
	}
	
	static class EncryptDecrypt {
		public final static String UTF8 = "UTF-8";
		
		private static byte[] key_ =
		{
			'a',
			'P',
			'e',
			'P',
			'g',
			'K',
			'i',
			'V',
			't',
			'v',
			't',
			'j',
			'a',
			'O',
			'8',
			'Y',
			'8',
			'7',
			'J',
			's',
			'H',
			'e',
			'b',
			'V',
			'x',
			'P',
			'3',
			'a',
			'W',
			'R',
			'1',
			'J' };
		
		/**
	     * Encodes a byte array to a base64 string
	     */
	    public static String encode(byte[] rawData)
	    {
	        return new String(Base64Coder.encode(rawData));
	    }
	    
	    /**
	     * Decodes a base64 string to a byte array
	     */
	    public static byte[] decode(String codedStr)
	    {
	        byte[] retData = null;

            retData = Base64Coder.decode(codedStr.toCharArray());

	        return retData;
	    }
	    
	    /**
		 * Encrypts a password using a hard-coded key.
		 */
		public final static String encryptHardKey(String password) {
			String ret = null;
			try {
				Cipher cipherObject = Cipher.getInstance("DESede");

				byte[] key = decode(newUTF8String(key_));

				SecretKeySpec keySpec_ =
					new SecretKeySpec(key, 0, key.length, "DESede");

				cipherObject.init(Cipher.ENCRYPT_MODE, keySpec_);

				byte[] encryptedPassword =
					cipherObject.doFinal(getUTF8Bytes(password));

				ret = encode(encryptedPassword);

			} catch (Exception e) {
				//ExceptionHandler.handle(logger, e);
				//throw new RuntimeException(e);
				System.out.println(e.getMessage());
			}
			return ret;
		}
		
		/**
		 * Decrypts a password using a hard-coded key.
		 */
		public final static String decryptHardKey(String password) {
			String ret= null;


			try {
				Cipher cipherObject = Cipher.getInstance("DESede");

				byte[] key = decode(newUTF8String(key_));

				SecretKeySpec keySpec_ =
					new SecretKeySpec(key, 0, key.length, "DESede");

				cipherObject.init(Cipher.DECRYPT_MODE, keySpec_);

				byte[] decryptedPassword =
					cipherObject.doFinal(decode(password));

				ret = newUTF8String(decryptedPassword);

			} catch (Exception e) {
				//ExceptionHandler.handle(logger, e);
				//throw new RuntimeException(e);
				System.out.println(e.getMessage());
			}
			return ret;
		}
		
		public static String newUTF8String(byte [] bytes) {
			try {
				return new String(bytes, UTF8);
			} catch (UnsupportedEncodingException e) {
				//ExceptionHandler.handle(logger, e);
				return new String(bytes);
			}
		}
		public static byte [] getUTF8Bytes(String s) {
			try {
				return s.getBytes(UTF8);
			} catch (UnsupportedEncodingException e) {
				//ExceptionHandler.handle(logger, e);
				return s.getBytes();
			}
			
		}

	}
	
	private static class Base64Coder {

		// Mapping table from 6-bit nibbles to Base64 characters.
		private static char[]    map1 = new char[64];
		   static {
		      int i=0;
		      for (char c='A'; c<='Z'; c++) map1[i++] = c;
		      for (char c='a'; c<='z'; c++) map1[i++] = c;
		      for (char c='0'; c<='9'; c++) map1[i++] = c;
		      map1[i++] = '+'; map1[i++] = '/'; }

		// Mapping table from Base64 characters to 6-bit nibbles.
		private static byte[]    map2 = new byte[128];
		   static {
		      for (int i=0; i<map2.length; i++) map2[i] = -1;
		      for (int i=0; i<64; i++) map2[map1[i]] = (byte)i; }


		/**
		* Encodes a string into Base64 format.
		* No blanks or line breaks are inserted.
		* @param s  a String to be encoded.
		* @return   A String with the Base64 encoded data.
		*/
		public static String encode (String s) {
		   return new String(encode(s.getBytes())); }

		/**
		* Encodes a byte array into Base64 format.
		* No blanks or line breaks are inserted.
		* @param in  an array containing the data bytes to be encoded.
		* @return    A character array with the Base64 encoded data.
		*/
			public static char[] encode(byte[] in) {
				int iLen = in.length;
				int iLeni = (iLen / 3) * 3;
				int oLen = ((iLen + 2) / 3) * 4; // output length including padding
				char[] out = new char[oLen];
				int ip = 0;
				int op = 0;
				int i0,i1,i2;
				int ix;
				while (ip < iLeni) {
					i0 = in[ip++]& 0xff;
					i1 = in[ip++]& 0xff;
					i2 = in[ip++]& 0xff;
					ix = (i0<<16) | (i1<<8) | i2;
					out[op++] = map1[ix >> 18];
					out[op++] = map1[(ix >> 12) & 0x3f];
					out[op++] = map1[(ix >> 6) & 0x3f];
					out[op++] = map1[ix & 0x3f];
				}
				if (ip < iLen) {
					i0 = in[ip++] & 0xff;
					out[op++] = map1[i0 >>> 2];
					if (ip < iLen) {
						i1 = in[ip++] & 0xff;
						out[op++] = map1[((i0 & 3) << 4) | (i1 >>> 4)];
						out[op] = map1[((i1 & 0xf) << 2)];
					} else {
						out[op++] = map1[((i0 & 3) << 4)];
						out[op] = '=';
					}
					out[++op] = '=';
				}
				return out;
			}

			/**
			* Decodes a Base64 string.
			* @param s  a Base64 String to be decoded.
			* @return   A String containing the decoded data.
			* @throws   IllegalArgumentException if the input is not valid Base64 encoded data.
			*/
			public static String decode (String s) {
			   return new String(decode(s.toCharArray())); }


			public static byte[] decode(char[] in) {
				int iLen = in.length;
				if (iLen % 4 != 0)
					throw new IllegalArgumentException(
							"Length of Base64 encoded input string is not a multiple of 4."); //$NON-NLS-1$
				while (iLen > 0 && in[iLen - 1] == '=')
					iLen--;
				int oLen = (iLen * 3) / 4;
				int iLeni = (iLen /4) * 4;
				byte[] out = new byte[oLen];
				int ip = 0;
				int op = 0;
				int b0,b1,b2=0,b3;
				int bx;
				try {
				while (ip < iLeni) {
					b0 = map2[in[ip++]];
					b1 = map2[in[ip++]];
					b2 = map2[in[ip++]];
					b3 = map2[in[ip++]];

					bx = (b0<<18) | (b1<<12) | (b2<<6) | b3;
					out[op++] = (byte) (bx>>16);
					out[op++] = (byte) ((bx>>8)&0xff);
					out[op++] = (byte) (bx & 0xff);

					if (b0 < 0 || b1 < 0 || b2 < 0 || b3 < 0)
						throw new IllegalArgumentException(
								"Illegal character in Base64 encoded data."); //$NON-NLS-1$
				}

				if (ip < iLen) {
					b0 = map2[in[ip++]];
					b1 = map2[in[ip++]];
			 		out[op++] = (byte) ((b0 << 2) | (b1 >>> 4));

					if (ip < iLen) {
						b2 = map2[in[ip++]];
						out[op++] = (byte) (((b1 & 0xf) << 4) | (b2 >>> 2));
					}
					if (b0 < 0 || b1 < 0 || b2 < 0 )
						throw new IllegalArgumentException(
								"Illegal character in Base64 encoded data."); //$NON-NLS-1$
				}
				} catch (Exception e)
				{
					throw new IllegalArgumentException(
						"Illegal character in Base64 encoded data."); //$NON-NLS-1$
				}
				return out;
			}

		}
	
	public static class ConnectorX509TrustManager implements X509TrustManager {
		private static final Logger logger = Logger.getLogger(ConnectorX509TrustManager.class.getName());

		public static final String INSTANCE_JKS = "JKS"; //$NON-NLS-1$

		public static final String ALGORITHM_RSA = "RSA"; //$NON-NLS-1$

		/* Get the default trustmanager algorithm defined in java.security file (JRE_HOME/lib/security/java.security)
		 * Default algorithm is PKIX. It is alias for SUNPKIX in case of Sun JRE and IBMPKIX in case of IBM JRE
		 * http://cr.openjdk.java.net/~xuelei/6586258/webrev.00/src/share/classes/sun/security/ssl/SunJSSE.java.sdiff.html
		 * https://developer-content.emc.com/docs/rsashare/share_for_java/1.1/jsse/com/rsa/jsse/JsseProvider.html
		 */
		public static final String ALGORITHM_PKIX = TrustManagerFactory.getDefaultAlgorithm();
		//public static final String ALGORITHM_SUNPKIX = "SunPKIX"; //$NON-NLS-1$

		public static final int PEM_CERTIFICATE = 1;

		public static final int PEM_PUBLIC_KEY = 2;

		public static final int PEM_PRIVATE_KEY = 3;

		public static final int PEM_KEY_ALG = 4;

		public static final String PEM_CERT_PREFIX = "-----BEGIN CERTIFICATE-----"; //$NON-NLS-1$

		public static final String PEM_CERT_SUFFIX = "-----END CERTIFICATE-----";// //$NON-NLS-1$

		private X509TrustManager defTrustManager = null;

		private KeyStore keyStore = null;

		private boolean clientTrusted = false;

		/*******************************************************************************************************************
		 * Trust anything given us.
		 * 
		 * Default setting.
		 * 
		 * <p>
		 * See <a href="http://javaalmanac.com/egs/javax.net.ssl/TrustAll.html"> e502. Disabling Certificate Validation in
		 * an HTTPS Connection</a> from the java almanac for how to trust all.
		 */
		public final static String OPEN = "OPEN";

		/*******************************************************************************************************************
		 * Trust any valid cert including self-signed certificates.
		 */
		public final static String LOOSE = "LOOSE";

		/*******************************************************************************************************************
		 * Normal jsse behavior.
		 * 
		 * Seemingly any certificate that supplies valid chain of trust.
		 */
		public final static String NORMAL = "NORMAL";

		/*******************************************************************************************************************
		 * All the levels of trust as an array from open to strict.
		 */
		public static String[] LEVELS_AS_ARRAY = { OPEN, LOOSE, NORMAL };

		/*******************************************************************************************************************
		 * Levels as a list.
		 */
		private static List<String> LEVELS = Arrays.asList(LEVELS_AS_ARRAY);

		/*******************************************************************************************************************
		 * Default setting for trust level.
		 */
		public final static String DEFAULT = NORMAL;

		/*******************************************************************************************************************
		 * Trust level.
		 */
		private String trustLevel = DEFAULT;

		public static final String DEFAULT_CERT = "" + //$NON-NLS-1$
				"MIIBpDCCAU6gAwIBAgIEJwUZdzANBgkqhkiG9w0BAQQFADAqMQswCQYDVQQGEwJV" + //$NON-NLS-1$
				"UzEbMBkGA1UEAxMSTm92ZWxsIE5TdXJlIEF1ZGl0MB4XDTAzMDMzMDAzMDgyNloX" + //$NON-NLS-1$
				"DTEzMDMyNzAzMDgyNlowKjELMAkGA1UEBhMCVVMxGzAZBgNVBAMTEk5vdmVsbCBO" + //$NON-NLS-1$
				"U3VyZSBBdWRpdDBcMA0GCSqGSIb3DQEBAQUAA0sAMEgCQQDAfCh6d4GOmYd16WB0" + //$NON-NLS-1$
				"gkdFxutR9O0R7sceFY4di2GJCCol+Nl2iQjo8q1KVz1dautEFoQiS/JzqwLJmmg5" + //$NON-NLS-1$
				"PqBzAgMBAAGjXDBaMA8GA1UdEwEB/wQFMAMBAf8wDgYDVR0PAQH/BAQDAgEGMB0G" + //$NON-NLS-1$
				"A1UdDgQWBBTApKjt/Z4YUzX2MlmGJhNs/Xco5jAYBgxghkgBhvg3AYJbCgEECBYG" + //$NON-NLS-1$
				"TkF1ZGl0MA0GCSqGSIb3DQEBBAUAA0EAmoj+rclCSq9OZYwrljfzElFWtzDPgEe0" + //$NON-NLS-1$
				"6V6y0W0ooRj4bDbDFQ2of6HRbJfhsPHT8wAs/MV/4LsvVYOXGAiH8g=="; //$NON-NLS-1$

		public static final String DEFAULT_PKEY = "" + //$NON-NLS-1$
				"MIIBVAIBADANBgkqhkiG9w0BAQEFAASCAT4wggE6AgEAAkEAwHwoeneBjpmHdelg" + //$NON-NLS-1$
				"dIJHRcbrUfTtEe7HHhWOHYthiQgqJfjZdokI6PKtSlc9XWrrRBaEIkvyc6sCyZpo" + //$NON-NLS-1$
				"OT6gcwIDAQABAkAVuy66vNlEpzKy7jk0jizLxOx2YUeOmb2jON3FmiHV3YLgalOh" + //$NON-NLS-1$
				"q7XGZkK9f1iSflvwVqEdkm0BOqbgVaXIf6V5AiEA7VExwk1xGLYJx968oJZ6XxC6" + //$NON-NLS-1$
				"2FCOFORa1p5EILWaXz0CIQDPo26eVweOsb/bSoYpbb6+jZLFqHItSdmHKnQ0IjH5" + //$NON-NLS-1$
				"bwIgWXlkXlKeUmfH1kCHM7CoYJFJMOdmOkBCc2pmW0K8+i0CIE0gAJux6e232nLC" + //$NON-NLS-1$
				"6LiEuiAfiBABNH/b+X+6ZjVjKjrjAiEA6CAXsmRnLIaySQyVs+sn37flDx3MJu4E" + //$NON-NLS-1$
				"EGReCWANit0="; //$NON-NLS-1$

		public ConnectorX509TrustManager(KeyStore keyStore) throws NoSuchAlgorithmException, KeyStoreException {
			this(keyStore, DEFAULT);
		}

		public ConnectorX509TrustManager(KeyStore keyStore, String trustLevel) throws NoSuchAlgorithmException,
			KeyStoreException {
			super();
			this.trustLevel = trustLevel;
			this.keyStore = keyStore;
			TrustManagerFactory tmFactory = TrustManagerFactory.getInstance(ALGORITHM_PKIX);
			tmFactory.init(this.keyStore);
			TrustManager[] trustManagers = tmFactory.getTrustManagers();
			if (trustManagers.length == 0) {
				throw new NoSuchAlgorithmException(ALGORITHM_PKIX + " not supported."); //$NON-NLS-1$
			}
			defTrustManager = (X509TrustManager) trustManagers[0];
		}

		/**
		 * 
		 * @param certs
		 * @param authType
		 *            (ignored)
		 * @return
		 * @exception
		 */
		public final void checkClientTrusted(X509Certificate[] certs, String authType) throws CertificateException {

			try {
				/**
				 * Trust all the incoming audit connections
				 */
				if (trustLevel.equalsIgnoreCase(OPEN))
					return;

				clientTrusted = false;
				logger.log(Level.FINE, "Examining whether Client is trusted: ([{0}],[{1}])", //$NON-NLS-1$
					new Object[] { certs == null ? "0" : Integer.toString(certs.length), authType }); //$NON-NLS-1$
				if ((certs != null) && (authType != null)) {
					// with the current architecture, we check only the first certificate in the stack
					// for (int n = 0; n < certs.length; n++) {
					// logger.log(Level.FINER, "Certificate information ({0}): (IssuerDN={1}, SubjectDN={2}, Data={3})",
					// //$NON-NLS-1$
					// new Object[]{Integer.toString(n), certs[n].getIssuerDN(), certs[n].getSubjectDN(),
					// certs[n].toString()});
					// }
					logger.log(Level.FINEST, "Client certificate information: {0}", certs[0].toString()); //$NON-NLS-1$

					// Check 1:
					// Quoting Sun Microsystems:
					// "Given the partial or complete certificate chain provided by the peer, build a
					// certificate path to a trusted root and return if it can be validated and is
					// trusted for client SSL authentication based on the authentication type."
					// NOTE: Throws an Exception upon failure.
					defTrustManager.checkClientTrusted(certs, authType);

					X509Certificate x509Cert = (X509Certificate) certs[0];
					// Check 2: Verify the application certificate against the server certificate.
					// x509Cert.verify();

					clientTrusted = true;
				}
			} catch (Exception e) {
				if (this.trustLevel.equals(LOOSE) && certs != null && certs.length == 1) {
					// If only one cert and its valid and it caused a
					// CertificateException, assume its self signed.
					X509Certificate certificate = certs[0];
					certificate.checkValidity();

				} else {
					logger.log(Level.SEVERE, "Certificate authentication of the client failed.");//$NON-NLS-1$

					throw new CertificateException("Certificate authentication of the client failed."); //$NON-NLS-1$
				}
			}
		}

		/**
		 * 
		 * @param certs
		 * @param trustType
		 * @exception
		 */
		public final void checkServerTrusted(X509Certificate[] certs, String authType) throws CertificateException {
			logger.log(Level.FINE, "Examining whether Server is trusted: ([{0}],[{1}])", //$NON-NLS-1$
				new Object[] { certs == null ? "0" : Integer.toString(certs.length), authType }); //$NON-NLS-1$
		}

		/**
		 * Retrieve a specific component (certificate, public key, or private key) from a keystore and return it in PEM
		 * format.
		 * 
		 * @param ksFile
		 *            The keystore's absolute file name.
		 * @param ksPass
		 *            The keystore's password.
		 * @param alias
		 *            The alias for the certificate from which data will be extracted.
		 * @param component
		 *            Indicates which component should be returned in PEM format.
		 * @return
		 * @exception
		 */
		public final static String getPEMFromKeyStore(File ksFile, char[] ksPass, String alias, int component) throws Exception {
			String data = null;
			KeyStore keyStore = null;

			keyStore = KeyStore.getInstance(INSTANCE_JKS);
			if (keyStore == null) {
				throw new Exception("Supported instance not found in keystore."); //$NON-NLS-1$
			}
			keyStore.load(new FileInputStream(ksFile), ksPass);

			Certificate cert = keyStore.getCertificate(alias);
			if (cert == null) {
				throw new Exception("Malformed keystore; no certificates found for alias \"" + alias + "\"."); //$NON-NLS-1$ //$NON-NLS-2$
			}

			String base64 = null;
			if (component == PEM_CERTIFICATE) {
				base64 = String.valueOf(Base64Coder.encode(cert.getEncoded()));
				data = new String();
				for (int n = 0; n < base64.length(); n += 64) {
					data = data + base64.substring(n, Math.min(n + 64, base64.length() - 1)) + '\n';
				}
			} else if (component == PEM_PUBLIC_KEY) {
				PublicKey publicKey = cert.getPublicKey();
				base64 = String.valueOf(Base64Coder.encode(publicKey.getEncoded()));
				data = new String();
				for (int n = 0; n < base64.length(); n += 64) {
					data = data + base64.substring(n, Math.min(n + 64, base64.length() - 1)) + '\n';
				}
			} else if (component == PEM_PRIVATE_KEY) {
				Key key = keyStore.getKey(alias, ksPass);
				if ((key != null) && (key instanceof PrivateKey)) {
					PrivateKey privateKey = (PrivateKey) key;
					base64 = String.valueOf(Base64Coder.encode(privateKey.getEncoded()));
					data = new String();
					for (int n = 0; n < base64.length(); n += 64) {
						data = data + base64.substring(n, Math.min(n + 64, base64.length() - 1)) + '\n';
					}
				}
			} else if (component == PEM_KEY_ALG) {
				Key key = keyStore.getKey(alias, ksPass);
				if (key != null)
					return key.getAlgorithm();

			}

			return (base64);
		}

		public final static String getKeyAlgorithm(File ksFile, char[] ksPass, String alias) throws Exception {
			KeyStore keyStore = null;

			keyStore = KeyStore.getInstance(INSTANCE_JKS);
			if (keyStore == null) {
				throw new Exception("Supported instance not found in keystore."); //$NON-NLS-1$
			}
			keyStore.load(new FileInputStream(ksFile), ksPass);

			Key key = keyStore.getKey(alias, ksPass);

			if (key == null)
				return null;

			return key.getAlgorithm();
		}

		/**
		 * Retrieve a list of certificate aliases from a keystore.
		 * 
		 * @param ksFile
		 *            The keystore's absolute file name.
		 * @param ksPass
		 *            The keystore's password.
		 * @return A list of identified aliases.
		 * @throws Exception
		 * 
		 */
		public final static Vector getAliasesFromKeyStore(File ksFile, char[] ksPass) throws Exception {
			Vector<Object> data = null;
			KeyStore keyStore = null;
			Enumeration aliases = null;

			keyStore = KeyStore.getInstance(INSTANCE_JKS);
			if (keyStore == null) {
				throw new Exception("Supported instance not found in keystore."); //$NON-NLS-1$
			}
			keyStore.load(new FileInputStream(ksFile), ksPass);

			for (aliases = keyStore.aliases(); aliases.hasMoreElements();) {
				if (data == null) {
					data = new Vector<Object>();
				}
				data.add(aliases.nextElement());
			}

			return data;
		}

		public final static List<String> getCertsFromTrustStore(File ksFile, char[] ksPass) throws Exception {
			List<String> certs = null;
			KeyStore keyStore = null;
			Enumeration aliases = null;

			keyStore = KeyStore.getInstance(INSTANCE_JKS);
			if (keyStore == null) {
				throw new Exception("Supported instance not found in keystore."); //$NON-NLS-1$
			}
			keyStore.load(new FileInputStream(ksFile), ksPass);

			for (aliases = keyStore.aliases(); aliases.hasMoreElements();) {
				if (certs == null) {
					certs = new ArrayList<String>();
				}

				Certificate  cert = keyStore.getCertificate((String) aliases.nextElement());

				// We only support X509 Certificates
				X509Certificate tempCert = (X509Certificate) cert;
				certs.add(new String(Base64Coder.encode(tempCert.getEncoded())));
			}

			return certs;
		}

		/**
		 * 
		 * @return
		 */
		public final X509Certificate[] getAcceptedIssuers() {
			return defTrustManager.getAcceptedIssuers();
		}

		/**
		 * 
		 * @return
		 */
		public final boolean isClientTrusted() {
			return clientTrusted;
		}

		public static boolean saveCertificate(File f, String cert) {
			boolean success = false;
			FileWriter writer = null;
			try {

				writer = new FileWriter(f);
				writer.write(PEM_CERT_PREFIX);
				writer.write("\n");
				String data = "";

				for (int n = 0; n < cert.length(); n += 64) {
					data = data + cert.substring(n, Math.min(n + 64, cert.length())) + '\n';
				}

				writer.write(data);

				writer.write(PEM_CERT_SUFFIX);
				writer.write("\n");
			} catch (IOException e) {
				// TODO Send this message as status
				e.printStackTrace();
			} finally {
				if (writer != null) {
					try {
						writer.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}

			return success;
		}

	}
	
	public static class SocketUtil {
		//TODO: disable MD5, uncomment to do this, too risky to do now
	    private static final String[] excludeCipher = {"export", "null", "low", "_des" /*, "md5" */}; 
	    private static final String[] excludeProtocol = {"v2"};
	    private static final Logger logger = Logger.getLogger(SocketUtil.class.getName());
	    
	    /**
	     * Restricts the capability of the socket to disable weak protocols and ciphers.
	     * This complies with Novell Security Review Board.
	     * @param socket
	     */
	    public static void restrictSocket( Socket socket ) {
	        narrowSocketCiphers( socket );
	        narrowSocketProtocols( socket );
	    }

	    /**
	     * Restricts the capability of the socketServer to disable weak protocols and ciphers.
	     * This complies with Novell Security Review Board.
	     * @param serverSocket
	     */
	    public static void restrictSrvSocket( ServerSocket serverSocket ) {
	        narrowServerSocketCipherSuites( serverSocket );
	        narrowServerSocketProtocols( serverSocket );
	    }
	    
	    private static void narrowSocketProtocols( Socket socket ) {
	        
	        logger.finest( "\n\n Entering SocketUitl:narrowSocketProtocols " );
	        
	        if( socket instanceof SSLSocket ) {
	            SSLSocket sslSocket = (SSLSocket) socket;
	            
	            String[] before = sslSocket.getEnabledProtocols();
	            logger.finest("Before the filter, the sslSocket has "+ before.length +" enabled protocols" );
	            for( int i=0; i<before.length; i++ )
	                logger.finest( before[i] );
	            
	            String[] filtered = filterSuites( before, excludeProtocol );
	            
	            sslSocket.setEnabledProtocols( filtered );
	            
	            //if( logger.getLevel() != null && logger.getLevel().equals( Level.finest ) ) {
	                String after[] = sslSocket.getEnabledProtocols();
	                
	                logger.finest( "After the filter, the sslSocket has " + after.length + " enabled protocols." );
	                for( int i=0; i< after.length; i++ )
	                    logger.finest( after[i] );
	            //}
	            
	        }
	        
	        logger.finest("Existing SocketUtil:narrowSocketProtocols \n\n" );
	    }
	    
	    private static void narrowSocketCiphers( Socket socket ) {
	        
	        logger.finest("\n\n Entering SocketUtil:narrowSocketCiphers .... " );
	        
	        if( socket instanceof SSLSocket ) {
	            SSLSocket sslSocket = (SSLSocket) socket;
	            
	            String[] before = sslSocket.getEnabledCipherSuites();
	            
	            logger.finest( "Before filtering there were " + before.length + " cipher suites enabled" );
	            //if( logger.getLevel() != null && logger.getLevel().equals( Level.finest ) ) 
	            {
	                for( int i=0; i<before.length; i++ ) {
	                    logger.finest( before[i] );
	                }
	            }
	            
	            
	            String[] filtered = filterSuites( before, excludeCipher );
	            
	            sslSocket.setEnabledCipherSuites( filtered );
	            
	            //if( logger.getLevel() != null && logger.getLevel().equals( Level.finest ) ) {
	                String after[] = sslSocket.getEnabledCipherSuites();
	                logger.finest( "After filtering, there are " + after.length + " enabled cipher suites" );
	                for( int i=0; i<after.length; i++ )
	                {
	                    logger.finest( after[i] );
	                }
	            //}
	        }
	        
	        logger.finest("Exiting SocketUtil:narrowSocketCiphers \n\n" );
	    }
	    
	    private static void narrowServerSocketProtocols( ServerSocket serverSocket ) {
	        
	        logger.finest( "\n\nEntering SocketUitl:narrowServerSocketProtocols .. " );
	        if( !( serverSocket instanceof SSLServerSocket ) )
	            return;
	        
	        SSLServerSocket sslServerSocket = (SSLServerSocket) serverSocket;
	        
	        logger.fine( "Entering ProxiedClientListener:narrowProtocols ... " );
	        
	        String[] before = sslServerSocket.getEnabledProtocols();
	        
	        String[] filtered = filterSuites( before, excludeProtocol );
	        
	        logger.finest( "The protocols are filterd from " + before.length + " to " + filtered.length );
	        sslServerSocket.setEnabledProtocols( filtered );
	        
	        logger.finest("Exiting SocketUtil:narrwoServerSocketProtocols ... \n\n" );
	    }
	    
	    private static void narrowServerSocketCipherSuites( ServerSocket serverSocket ) {
	        
	        
	        if( !( serverSocket instanceof SSLServerSocket ) )
	            return;
	        
	        logger.finest("Entering ProxiedClientListener:narrowCipherSuites ... ");
	        
	        SSLServerSocket sslServerSocket = (SSLServerSocket) serverSocket;
	        String[] suites = sslServerSocket.getEnabledCipherSuites();
	        logger.finest("Defaulty Support cipher suites are:");
	        for (int i = 0; i < suites.length; i++) {
	            logger.finest( suites[i] );
	        }
	        
	        String[] filteredSuites = filterSuites(suites, excludeCipher );
	        logger.finest("nerrowed from " + suites.length + " to " + filteredSuites.length );
	        for( int i=0; i<filteredSuites.length; i++ )
	            logger.finest( filteredSuites[i] );
	        
	        //after this line, the socket's enabledCipherSuites should be only 3
	        sslServerSocket.setEnabledCipherSuites( filteredSuites );
	        
	        //if( logger.getLevel().equals( Level.finest ) )
	        {
	            String[] newSuites = sslServerSocket.getEnabledCipherSuites();
	            logger.finest( "Supported cipher suites are: " );
	            for(int i=0; i<newSuites.length; i++  ) {
	                logger.finest( newSuites[i] );
	            }
	        }
	    }
	    
	    private static String[] filterSuites( String[] origSuites , String[] disabled ) {
	        List list = new ArrayList( );
	        for( int i=0; i<origSuites.length; i++ )
	        {
	            boolean allow = true;
	            
	            for( int j=0; j<disabled.length; j++ )
	            {
	                //if( origSuites[i].toLowerCase().contains( disabled[j] )) 
	                if( origSuites[i].toLowerCase().indexOf( disabled[j] ) > -1 ) //String.contains only since java 1.5
	                {
	                    logger.finest("original value: " + origSuites[i] + " is not allowd, because it contains: " + disabled[j]);
	                    allow = false;
	                    break;
	                }
	            }
	            
	            
	            if( allow ) {
	                list.add( origSuites[i] );
	            }
	            
	        }
	        
	        return (String[]) list.toArray( new String[ list.size()]);
	    }
	    
	    
	    
	}
}
