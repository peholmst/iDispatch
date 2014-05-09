
package https.webservice_aspsms_com.aspsmsx2;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each
 * Java content interface and Java element interface
 * generated in the https.webservice_aspsms_com.aspsmsx2 package.
 * <p>An ObjectFactory allows you to programatically
 * construct new instances of the Java representation
 * for XML content. The Java representation of XML
 * content can consist of schema derived interfaces
 * and classes representing the binding of schema
 * type definitions, element declarations and model
 * groups.  Factory methods for each of these are
 * provided in this class.
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _String_QNAME = new QName("https://webservice.aspsms.com/aspsmsx2.asmx", "string");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: https.webservice_aspsms_com.aspsmsx2
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link SendOriginatorUnlockCodeResponse }
     */
    public SendOriginatorUnlockCodeResponse createSendOriginatorUnlockCodeResponse() {
        return new SendOriginatorUnlockCodeResponse();
    }

    /**
     * Create an instance of {@link SendTokenSMS }
     */
    public SendTokenSMS createSendTokenSMS() {
        return new SendTokenSMS();
    }

    /**
     * Create an instance of {@link CheckCreditsResponse }
     */
    public CheckCreditsResponse createCheckCreditsResponse() {
        return new CheckCreditsResponse();
    }

    /**
     * Create an instance of {@link SimpleWAPPushResponse }
     */
    public SimpleWAPPushResponse createSimpleWAPPushResponse() {
        return new SimpleWAPPushResponse();
    }

    /**
     * Create an instance of {@link SendUnicodeSMS }
     */
    public SendUnicodeSMS createSendUnicodeSMS() {
        return new SendUnicodeSMS();
    }

    /**
     * Create an instance of {@link GetStatusCodeDescriptionResponse }
     */
    public GetStatusCodeDescriptionResponse createGetStatusCodeDescriptionResponse() {
        return new GetStatusCodeDescriptionResponse();
    }

    /**
     * Create an instance of {@link VerifyToken }
     */
    public VerifyToken createVerifyToken() {
        return new VerifyToken();
    }

    /**
     * Create an instance of {@link SendOriginatorUnlockCode }
     */
    public SendOriginatorUnlockCode createSendOriginatorUnlockCode() {
        return new SendOriginatorUnlockCode();
    }

    /**
     * Create an instance of {@link VerifyTokenResponse }
     */
    public VerifyTokenResponse createVerifyTokenResponse() {
        return new VerifyTokenResponse();
    }

    /**
     * Create an instance of {@link SendTextSMS }
     */
    public SendTextSMS createSendTextSMS() {
        return new SendTextSMS();
    }

    /**
     * Create an instance of {@link UnlockOriginatorResponse }
     */
    public UnlockOriginatorResponse createUnlockOriginatorResponse() {
        return new UnlockOriginatorResponse();
    }

    /**
     * Create an instance of {@link SendSimpleTextSMS }
     */
    public SendSimpleTextSMS createSendSimpleTextSMS() {
        return new SendSimpleTextSMS();
    }

    /**
     * Create an instance of {@link UnlockOriginator }
     */
    public UnlockOriginator createUnlockOriginator() {
        return new UnlockOriginator();
    }

    /**
     * Create an instance of {@link InquireDeliveryNotificationsResponse }
     */
    public InquireDeliveryNotificationsResponse createInquireDeliveryNotificationsResponse() {
        return new InquireDeliveryNotificationsResponse();
    }

    /**
     * Create an instance of {@link SimpleWAPPush }
     */
    public SimpleWAPPush createSimpleWAPPush() {
        return new SimpleWAPPush();
    }

    /**
     * Create an instance of {@link CheckOriginatorAuthorizationResponse }
     */
    public CheckOriginatorAuthorizationResponse createCheckOriginatorAuthorizationResponse() {
        return new CheckOriginatorAuthorizationResponse();
    }

    /**
     * Create an instance of {@link SendTextSMSResponse }
     */
    public SendTextSMSResponse createSendTextSMSResponse() {
        return new SendTextSMSResponse();
    }

    /**
     * Create an instance of {@link CheckCredits }
     */
    public CheckCredits createCheckCredits() {
        return new CheckCredits();
    }

    /**
     * Create an instance of {@link SendUnicodeSMSResponse }
     */
    public SendUnicodeSMSResponse createSendUnicodeSMSResponse() {
        return new SendUnicodeSMSResponse();
    }

    /**
     * Create an instance of {@link VersionInfo }
     */
    public VersionInfo createVersionInfo() {
        return new VersionInfo();
    }

    /**
     * Create an instance of {@link GetStatusCodeDescription }
     */
    public GetStatusCodeDescription createGetStatusCodeDescription() {
        return new GetStatusCodeDescription();
    }

    /**
     * Create an instance of {@link InquireDeliveryNotifications }
     */
    public InquireDeliveryNotifications createInquireDeliveryNotifications() {
        return new InquireDeliveryNotifications();
    }

    /**
     * Create an instance of {@link CheckOriginatorAuthorization }
     */
    public CheckOriginatorAuthorization createCheckOriginatorAuthorization() {
        return new CheckOriginatorAuthorization();
    }

    /**
     * Create an instance of {@link SendTokenSMSResponse }
     */
    public SendTokenSMSResponse createSendTokenSMSResponse() {
        return new SendTokenSMSResponse();
    }

    /**
     * Create an instance of {@link SendSimpleTextSMSResponse }
     */
    public SendSimpleTextSMSResponse createSendSimpleTextSMSResponse() {
        return new SendSimpleTextSMSResponse();
    }

    /**
     * Create an instance of {@link VersionInfoResponse }
     */
    public VersionInfoResponse createVersionInfoResponse() {
        return new VersionInfoResponse();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link String }{@code >}}
     */
    @XmlElementDecl(namespace = "https://webservice.aspsms.com/aspsmsx2.asmx", name = "string")
    public JAXBElement<String> createString(String value) {
        return new JAXBElement<String>(_String_QNAME, String.class, null, value);
    }

}
