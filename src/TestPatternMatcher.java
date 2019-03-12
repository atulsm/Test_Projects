import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TestPatternMatcher {

	public static String MSG1 = "CEF:0|Security|threatmanager|1.0|100|worm successfully stopped|10|src=10.0.0.1 dst=2.1.2.2 spt=1232";
	public static String MSG ="MSWinEventLog\\t1\\tSecurity\\t78\\tMon Aug 11 21:01:44 2014\\t4624\\tMicrosoft-Windows-Security-Auditing\\tATUL-SAM\\\\Administrator\\tN/A\\tSuccess Audit\\tATUL-SAM\\tLogon\\t\\tAn account was successfully logged on.    Subject:   Security ID:  S-1-5-18   Account Name:  ATUL-SAM$   Account Domain:  WORKGROUP   Logon ID:  0x3e7    Logon Type:   10    New Logon:   Security ID:  S-1-5-21-976411532-455443073-982928273-500   Account Name:  Administrator   Account Domain:  ATUL-SAM   Logon ID:  0xb34c7   Logon GUID:  {00000000-0000-0000-0000-000000000000}    Process Information:   Process ID:  0x101c   Process Name:  C:\\\\Windows\\\\System32\\\\winlogon.exe    Network Information:   Workstation Name: ATUL-SAM   Source Network Address: 164.99.202.15   Source Port:  2506    Detailed Authentication Information:   Logon Process:  User32    Authentication Package: Negotiate   Transited Services: -   Package Name (NTLM only): -   Key Length:  0    This event is generated when a logon session is created. It is generated on the computer that was accessed.    The subject fields indicate the account on the local system which requested the logon. This is most commonly a service such as the Server service, or a local process such as Winlogon.exe or Services.exe.    The logon type field indicates the kind of logon that occurred. The most common types are 2 (interactive) and 3 (network).    The New Logon fields indicate the account for whom the new logon was created, i.e. the account that was logged on.    The network fields indicate where a remote logon request originated. Workstation name is not always available and may be left blank in some cases.    The authentication information fields provide detailed information about this specific logon request.   - Logon GUID is a unique identifier that can be used to correlate this event with a KDC event.   - Transited services indicate which intermediate services have participated in this logon request.   - Package name indicates which sub-protocol was used among the NTLM protocols.   - Key length indicates the length of the generated session key. This will be 0 if no session key was requested.  This event is generated when a logon session is created. It is generated on the computer that was accessed.    The subject fields indicate the account on the local system which requested the logon. This is most commonly a service such as the Server service, or a local process such as Winlogon.exe or Services.exe.    The logon type field indicates the kind of logon that occurred. The most common types are 2 (interactive) and 3 (network).    The New Logon fields indicate the account for whom the new logon was created, i.e. the account that was logged on.    The network fields indicate where a remote logon request originated. Workstation name is not always available and may be left blank in some cases.    The authentication information fields provide detailed information about this specific logon request.   - Logon GUID is a unique identifier that can be used to correlate this event with a KDC event.   - Transited services indicate which intermediate services have participated in this logon request.   - Package name indicates which sub-protocol was used among the NTLM protocols.   - Key length indicates the length of the generated session key. This will be 0 if no session key was requested. This event is generated when a logon session is created. It is generated on the computer that was accessed.    The subject fields indicate the account on the local system which requested the logon. This is most commonly a service such as the Server service, or a local process such as Winlogon.exe or Services.exe.    The logon type field indicates the kind of logon that occurred. The most common types are 2 (interactive) and 3 (network).    The New Logon fields indicate the account for whom the new logon was created, i.e. the account that was logged on.    The network fields indicate where a remote logon request originated. \\t57";
	
	public static void main(String[] args) {
		long start = System.currentTimeMillis();
		for(int i=0;i<1000;i++) {
			Pattern m_matchingRulePattern = Pattern.compile("CEF:\\d");
			Matcher	m = m_matchingRulePattern.matcher(MSG);
			//System.out.println(m.find());
			m.find();
		}
		long end = System.currentTimeMillis();
		System.out.println(end-start);
	}

}