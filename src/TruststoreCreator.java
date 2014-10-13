
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PushbackInputStream;
import java.security.KeyStore;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.util.Arrays;


public class TruststoreCreator {

	private static String SEPERATOR = ","; //$NON-NLS-1$

	public static void main(String[] args) {
		String keystorePath = null, keyStorePass = null;
		String[] certs = new String[0];

		InputStreamReader reader = new InputStreamReader(System.in);
		BufferedReader in = new BufferedReader(reader);

		try {

			System.out.print("Enter the file name for the truststore: "); //$NON-NLS-1$
			keystorePath = in.readLine();
			//keyStorePass = in.readLine();

			System.out.print("Enter the list of certificates to import (comma seperated): "); //$NON-NLS-1$
			String temp = in.readLine();
			certs = temp.split(SEPERATOR);

			if (keystorePath == null || certs.length <= 0){ // || keyStorePass == null) {
				System.out.println("Missing required parameters"); //$NON-NLS-1$
				printUsuage();
				return;
			}
			keystorePath = new File(keystorePath).getAbsolutePath();

			// The new keystore with the certificates added
			FileOutputStream ksos = new FileOutputStream(keystorePath,true);

			KeyStore ks = KeyStore.getInstance("jks"); //$NON-NLS-1$

			FileInputStream is = new FileInputStream(keystorePath);
			ks.load(is, null);

			for (String cert : certs) {
				// The certificate files, to be added to keystore
				FileInputStream certFile1 = new FileInputStream(cert);

				CertificateFactory cf = CertificateFactory.getInstance("X.509"); //$NON-NLS-1$

				// The certificate files, to be added to keystore
				Certificate cert1 = cf.generateCertificate(certFile1);

				String fileName = new File(cert).getName();
				// Add certificates to keystore
				ks.setCertificateEntry(fileName, cert1);
			}

			// Write modified keystore to file system
			ks.store(ksos, null);

			ksos.close();

			System.out.println("Created " + keystorePath + " successfully"); //$NON-NLS-1$ //$NON-NLS-2$

		} catch (Exception e) {
			e.printStackTrace();
			printUsuage();
		}
	}

	private static void printUsuage() {
		System.out
			.println("\nUsuage: \nTruststoreCreator.bat \nEnter the file name for the truststore: <destination keystore path> \nEnter the password to create truststore: <keystore password> \nEnter the list of certificates to import (comma seperated): <cert1>,<cert2>"); //$NON-NLS-1$

		System.out
			.println("\nExample: \nTruststoreCreator.bat \nEnter the file name for the truststore: C:\\my.keystore \nEnter the password to create truststore:****** \nEnter the list of certificates to import (comma seperated): C:\\appcert.pem,C:\\cacert.pem");//$NON-NLS-1$

		System.out
			.println("\nCreates a keystore with the certificates <cert1> and <cert2> from the above command. The certificates are expected to be in  either DER or PEM format ");//$NON-NLS-1$

		System.out.println("Note:- The tool uses the name of the certificate file as alias while creating truststore.");//$NON-NLS-1$

	}

private static class PasswordField {

	  /**
	   *@param input stream to be used (e.g. System.in)
	   *@param prompt The prompt to display to the user.
	   *@return The password as entered by the user.
	   */

	   public static final char[] getPassword(InputStream in, String prompt) throws IOException {
	      MaskingThread maskingthread = new MaskingThread(prompt);
	      Thread thread = new Thread(maskingthread);
	      thread.start();
		
	      char[] lineBuffer;
	      char[] buf;
	      int i;

	      buf = lineBuffer = new char[128];

	      int room = buf.length;
	      int offset = 0;
	      int c;

	      loop:   while (true) {
	         switch (c = in.read()) {
	            case -1:
	            case '\n':
	               break loop;

	            case '\r':
	               int c2 = in.read();
	               if ((c2 != '\n') && (c2 != -1)) {
	                  if (!(in instanceof PushbackInputStream)) {
	                     in = new PushbackInputStream(in);
	                  }
	                  ((PushbackInputStream)in).unread(c2);
	                } else {
	                  break loop;
	                }

	                default:
	                   if (--room < 0) {
	                      buf = new char[offset + 128];
	                      room = buf.length - offset - 1;
	                      System.arraycopy(lineBuffer, 0, buf, 0, offset);
	                      Arrays.fill(lineBuffer, ' ');
	                      lineBuffer = buf;
	                   }
	                   buf[offset++] = (char) c;
	                   break;
	         }
	      }
	      maskingthread.stopMasking();
	      if (offset == 0) {
	         return null;
	      }
	      char[] ret = new char[offset];
	      System.arraycopy(buf, 0, ret, 0, offset);
	      Arrays.fill(buf, ' ');
	      return ret;
	   }
	   
	   /**
	    * This class attempts to erase characters echoed to the console.
	    */
	   private static class MaskingThread extends Thread {
	      private volatile boolean stop;
	      private char echochar = '*';

	     /**
	      *@param prompt The prompt displayed to the user
	      */
	      public MaskingThread(String prompt) {
	         System.out.print(prompt);
	      }

	     /**
	      * Begin masking until asked to stop.
	      */
	      public void run() {

	         int priority = Thread.currentThread().getPriority();
	         Thread.currentThread().setPriority(Thread.MAX_PRIORITY);

	         try {
	            stop = true;
	            while(stop) {
	              System.out.print("\010" + echochar);
	              try {
	                 // attempt masking at this rate
	                 Thread.sleep(1);
	              }catch (InterruptedException iex) {
	                 Thread.currentThread().interrupt();
	                 return;
	              }
	            }
	         } finally { // restore the original priority
	            Thread.currentThread().setPriority(priority);
	         }
	      }

	     /**
	      * Instruct the thread to stop masking.
	      */
	      public void stopMasking() {
	         this.stop = false;
	      }
	   }
	}
}