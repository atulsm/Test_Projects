package sentinel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.novell.db.object.bean.AlertBean;
import com.novell.db.object.bean.AlertBeanMetaData;
import com.novell.sentinel.client.bean.BeanSerializationException;
import com.novell.sentinel.client.bean.field.BeanBooleanValue;
import com.novell.sentinel.client.bean.field.BeanDateValue;
import com.novell.sentinel.client.bean.field.BeanIntegerValue;
import com.novell.sentinel.client.bean.field.BeanStringValue;
import com.novell.sentinel.client.bean.field.BeanValue;
import com.novell.sentinel.client.bean.java.BeanTransporterSync;
import com.novell.sentinel.client.http.HttpException;
import com.novell.sentinel.client.http.java.JSONHttpRequestorJavaSync;
import com.novell.sentinel.client.tinyq.TinyQBuilder.InvalidExpressionTypeException;
import com.novell.sentinel.client.tinyq.TinyQBuilder.InvalidOperandException;
import com.novell.sentinel.json.JSONParseException;
import com.novell.sentinel.json.java.BeanTransporterJSONSync;
import com.novell.sentinel.uri.GenericURI;
import com.novell.sentinel.uri.UriConstructorFactory;
import com.novell.sentinel.uri.UriConstructorFactoryImpl;

import esecurity.base.datamodel.attribute.EsecBoolean;
import esecurity.base.datamodel.attribute.EsecDate;
import esecurity.base.datamodel.attribute.EsecDouble;
import esecurity.base.datamodel.attribute.EsecIP;
import esecurity.base.datamodel.attribute.EsecInteger;
import esecurity.base.datamodel.attribute.EsecLong;
import esecurity.base.datamodel.attribute.EsecMAC;
import esecurity.base.datamodel.attribute.EsecString;
import esecurity.base.datamodel.attribute.EsecUuid;
import esecurity.base.datamodel.attribute.EsecValue;
import esecurity.base.metadata.AttributeMD;
import esecurity.ccs.comp.event.JDBCEventStoreTest;
import esecurity.db.object.Alert;
import esecurity.db.object.AlertFactory;
import esecurity.db.object.Event;

public class TestAlertBean {
	
	public String protocol = "https";
	public String host = "164.99.175.165";
	public int port = 8443;
	public String user = "admin";
	public String password = "n";


	private boolean initalized = false;
	private BeanTransporterSync<AlertBean> alertTransporter;
	private UriConstructorFactory uriFactory;
	
	public static void main(String args[]){
		TestAlertBean sink = new TestAlertBean();
		
		List<Alert> alerts = new ArrayList<Alert>();
		alerts.add(generateAlert());
		sink.update(alerts);
	}
	
	public static Alert generateAlert(){
		Event event = JDBCEventStoreTest.generateEvent(true, true);

		Alert alert = AlertFactory.create();
		for(Map.Entry<String, EsecValue> field : event.getAttributeMap().entrySet()){
			alert.setAttribute(field.getKey(), field.getValue());
		}		
		
		alert.setAlertStoredDate(alert.getEventTime());
		alert.setAlertModifiedDate(alert.getEventTime());
		return alert;
	}
	
	private void initialize() throws HttpException {
		if (!initalized) {
			// Set up the base of the URL we need to communicate with the
			// Sentinel server
			GenericURI sentinelBaseURI = new GenericURI();
			sentinelBaseURI.setScheme(protocol).setHost(host).setPort(port);
			// Create a synchronous requestor for simplicity
			JSONHttpRequestorJavaSync requestor = createRequestor(sentinelBaseURI.toString(), user, password);
			// Set up the base of the URLs we need to access data objects
			GenericURI objectsBaseURI = new GenericURI(sentinelBaseURI);
			objectsBaseURI.appendPathComponent("SentinelRESTServices").appendPathComponent("objects");

			// Need a factory that will construct URLs for the transporters
			uriFactory = new UriConstructorFactoryImpl(objectsBaseURI.toString());

			alertTransporter = new BeanTransporterJSONSync<AlertBean>(AlertBeanMetaData.getInstance(), requestor, uriFactory);
		}
	}

	/**
	 * Create a synchronous HttpRequestor instance for demonstration use.
	 * 
	 * @param sentinelBaseURI
	 *            A URI that addresses the Sentinel server via https.
	 * @param userName
	 *            The name of a Sentinel user account with admin privileges.
	 * @param userPassword
	 *            The password for the account.
	 * @return A requestor instance that has been authenticated to the Sentinel
	 *         server.
	 * @throws HttpException
	 *             if an error occurs during authentication.
	 */
	public static JSONHttpRequestorJavaSync createRequestor(
			String sentinelBaseURI, String userName, String userPassword)
			throws HttpException {
		GenericURI sentinelServerURI = new GenericURI(sentinelBaseURI);
		JSONHttpRequestorJavaSync requestor = new JSONHttpRequestorJavaSync();
		// NOTE: do NOT use the TestTrustManager for production - it doesn't
		// check TLS/SSL certs at all.
		requestor
				.setTrustManager(new JSONHttpRequestorJavaSync.TestTrustManager());
		requestor.authenticate(userName, userPassword, sentinelServerURI);
		return requestor;
	}


	public void update(Iterable<? extends Event> alerts) {
		for(Event alert : alerts){
			try {
				postAlert((Alert)alert);
			} catch (InvalidOperandException | InvalidExpressionTypeException
					| HttpException | JSONParseException | IOException
					| BeanSerializationException e) {
				e.printStackTrace();
				throw new RuntimeException(e);
			}
		}
	}
	

	private void postAlert(Alert alert) throws HttpException,
			JSONParseException, IOException, InvalidOperandException,
			InvalidExpressionTypeException, BeanSerializationException {

		initialize();
		
		AlertBean alertBean = new AlertBean();
		for(AttributeMD attr : alert.getMetaData().getAttrMDs()){
			String name = attr.getAttrName();
			String beanName = attr.getBeanName();

			EsecValue value = alert.getAttribute(name);
			if(value == null) {
				continue;
			}
			BeanValue beanValue;
			if (value instanceof EsecBoolean) {
				beanValue = new BeanBooleanValue();
				beanValue.setValue(value.getBooleanValue());
				
			} else if (value instanceof EsecDate) {
				beanValue = new BeanDateValue();
				beanValue.setValue(value.getDateValue());
				
			} else if (value instanceof EsecInteger) {
				beanValue = new BeanIntegerValue();
				beanValue.setValue(value.getIntValue());
				
			} else if (value instanceof EsecUuid) {
				// TODO figure out how to handle this value.
				beanValue = new BeanStringValue();
				
			} else if (value instanceof EsecString
					|| value instanceof EsecIP
					|| value instanceof EsecDouble
					|| value instanceof EsecLong
					|| value instanceof EsecMAC
					|| value instanceof EsecUuid) {
				beanValue = new BeanStringValue();
				beanValue.setValue(value.getValue());
				
			} else {
				throw new UnsupportedOperationException("Can't handle attr: "+attr);
			}

			alertBean.setField(beanName, beanValue);
		}

//		BeanValue sentinelId = new BeanStringValue();
////		sentinelId.setValue("{\"@href\":\"https:\/\/10.204.100.193:8443\/SentinelRESTServices\/objects\/sentinel-system\/2DAA8580-305B-1032-870D-000C29E90B7D\"}");
//		sentinelId.setValue("https://164.99.19.85:8443/SentinelRESTServices/objects/sentinel-system/97C013E0-3074-1032-AAB1-000C29E90B7D");
//		alertBean.setField("rv121", sentinelId);
		alertTransporter.post(alertBean);
	}
}
