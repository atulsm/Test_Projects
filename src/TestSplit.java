
public class TestSplit {

	public static String input ="5:13:CurrentOffset25:20110131120744.000000+3309:EventType1:16:tgUuid36:EAE1DD80-30A8-102E-A50A-0014C206531114:CurrentMessage321:18:6:tgYear4:20117:tgMonth2:015:tgDay2:316:tgHour2:068:tgMinute2:378:tgSecond2:442:RN4:16761:C1:02:CN11:ATUL-LAPTOP2:EC4:70352:EI10:10737488592:ET1:31:L6:System2:SN23:Service Control Manager1:T11:information1:U19:NT AUTHORITY SYSTEM2:XM62:The COH_Mon service was successfully sent a start control. r n2:IS13:COH_Mon start7:LogType6:System#$%5:13:CurrentOffset25:20110131120744.000000+3309:EventType1:16:tgUuid36:EAE1DD80-30A8-102E-A50A-0014C206531114:CurrentMessage321:18:6:tgYear4:20117:tgMonth2:015:tgDay2:316:tgHour2:068:tgMinute2:378:tgSecond2:442:RN4:16761:C1:02:CN11:ATUL-LAPTOP2:EC4:70352:EI10:10737488592:ET1:31:L6:System2:SN23:Service Control Manager1:T11:information1:U19:NT AUTHORITY SYSTEM2:XM62:The COH_Mon service was successfully sent a start control. r n2:IS13:COH_Mon start7:LogType6:System#$%5:13:CurrentOffset25:20110131120744.000000+3309:EventType1:16:tgUuid36:EAE1DD80-30A8-102E-A50A-0014C206531114:CurrentMessage321:18:6:tgYear4:20117:tgMonth2:015:tgDay2:316:tgHour2:068:tgMinute2:378:tgSecond2:442:RN4:16761:C1:02:CN11:ATUL-LAPTOP2:EC4:70352:EI10:10737488592:ET1:31:L6:System2:SN23:Service Control Manager1:T11:information1:U19:NT AUTHORITY SYSTEM2:XM62:The COH_Mon service was successfully sent a start control. r n2:IS13:COH_Mon start7:LogType6:System#$%";
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		//System.out.println(isSubTreeEntry("TEST.NOVELL.COM", "novell.com"));
		//String[] strs = input.split("[#][$][%]");
		
		String ele = "a=b";
		
		long s1 = System.nanoTime();
		int idx = ele.indexOf("=");
		if (idx > 0) {
			String key = ele.substring(0, idx);
			String offset = ele.substring(idx + 1);
		}
		long s2 = System.nanoTime();
		String[] strs = ele.split("=");
		if(strs.length == 2){
			String key = strs[0];
			String value = strs[1];
		}
		long s3 = System.nanoTime();

		System.out.println((s3-s2) +  " -- " + (s2-s1));
		
		System.out.println("obj-component.JasperReportingComponent.properties".split("\\.").length);
	}

	private static boolean isSubTreeEntry(String valueDomain,
			String baseDomainName) {

		String REGEX = "(?ui)";

		String[] hostPrefixArr = valueDomain.split(REGEX + baseDomainName);

		int lengthStrArr = hostPrefixArr[0].split("\\.").length; //$NON-NLS-1$
		// Because hostname will have a "." suffix.
		if (lengthStrArr > 1)
			return true;
		else
			return false;

	}

}
