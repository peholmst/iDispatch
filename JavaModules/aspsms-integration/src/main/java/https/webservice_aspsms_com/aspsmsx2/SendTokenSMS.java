
package https.webservice_aspsms_com.aspsmsx2;

import javax.xml.bind.annotation.*;


/**
 * <p>Java class for anonymous complex type.
 * <p>
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;complexType>
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="UserKey" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Password" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Originator" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Recipients" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="MessageData" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="TokenReference" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="TokenValidity" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="TokenMask" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="VerificationCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="TokenCaseSensitive" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="URLBufferedMessageNotification" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="URLDeliveryNotification" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="URLNonDeliveryNotification" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="AffiliateId" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
        "userKey",
        "password",
        "originator",
        "recipients",
        "messageData",
        "tokenReference",
        "tokenValidity",
        "tokenMask",
        "verificationCode",
        "tokenCaseSensitive",
        "urlBufferedMessageNotification",
        "urlDeliveryNotification",
        "urlNonDeliveryNotification",
        "affiliateId"
})
@XmlRootElement(name = "SendTokenSMS")
public class SendTokenSMS {

    @XmlElement(name = "UserKey")
    protected String userKey;
    @XmlElement(name = "Password")
    protected String password;
    @XmlElement(name = "Originator")
    protected String originator;
    @XmlElement(name = "Recipients")
    protected String recipients;
    @XmlElement(name = "MessageData")
    protected String messageData;
    @XmlElement(name = "TokenReference")
    protected String tokenReference;
    @XmlElement(name = "TokenValidity")
    protected String tokenValidity;
    @XmlElement(name = "TokenMask")
    protected String tokenMask;
    @XmlElement(name = "VerificationCode")
    protected String verificationCode;
    @XmlElement(name = "TokenCaseSensitive")
    protected String tokenCaseSensitive;
    @XmlElement(name = "URLBufferedMessageNotification")
    protected String urlBufferedMessageNotification;
    @XmlElement(name = "URLDeliveryNotification")
    protected String urlDeliveryNotification;
    @XmlElement(name = "URLNonDeliveryNotification")
    protected String urlNonDeliveryNotification;
    @XmlElement(name = "AffiliateId")
    protected String affiliateId;

    /**
     * Gets the value of the userKey property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getUserKey() {
        return userKey;
    }

    /**
     * Sets the value of the userKey property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setUserKey(String value) {
        this.userKey = value;
    }

    /**
     * Gets the value of the password property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets the value of the password property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setPassword(String value) {
        this.password = value;
    }

    /**
     * Gets the value of the originator property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getOriginator() {
        return originator;
    }

    /**
     * Sets the value of the originator property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setOriginator(String value) {
        this.originator = value;
    }

    /**
     * Gets the value of the recipients property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getRecipients() {
        return recipients;
    }

    /**
     * Sets the value of the recipients property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setRecipients(String value) {
        this.recipients = value;
    }

    /**
     * Gets the value of the messageData property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getMessageData() {
        return messageData;
    }

    /**
     * Sets the value of the messageData property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setMessageData(String value) {
        this.messageData = value;
    }

    /**
     * Gets the value of the tokenReference property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getTokenReference() {
        return tokenReference;
    }

    /**
     * Sets the value of the tokenReference property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setTokenReference(String value) {
        this.tokenReference = value;
    }

    /**
     * Gets the value of the tokenValidity property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getTokenValidity() {
        return tokenValidity;
    }

    /**
     * Sets the value of the tokenValidity property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setTokenValidity(String value) {
        this.tokenValidity = value;
    }

    /**
     * Gets the value of the tokenMask property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getTokenMask() {
        return tokenMask;
    }

    /**
     * Sets the value of the tokenMask property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setTokenMask(String value) {
        this.tokenMask = value;
    }

    /**
     * Gets the value of the verificationCode property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getVerificationCode() {
        return verificationCode;
    }

    /**
     * Sets the value of the verificationCode property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setVerificationCode(String value) {
        this.verificationCode = value;
    }

    /**
     * Gets the value of the tokenCaseSensitive property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getTokenCaseSensitive() {
        return tokenCaseSensitive;
    }

    /**
     * Sets the value of the tokenCaseSensitive property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setTokenCaseSensitive(String value) {
        this.tokenCaseSensitive = value;
    }

    /**
     * Gets the value of the urlBufferedMessageNotification property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getURLBufferedMessageNotification() {
        return urlBufferedMessageNotification;
    }

    /**
     * Sets the value of the urlBufferedMessageNotification property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setURLBufferedMessageNotification(String value) {
        this.urlBufferedMessageNotification = value;
    }

    /**
     * Gets the value of the urlDeliveryNotification property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getURLDeliveryNotification() {
        return urlDeliveryNotification;
    }

    /**
     * Sets the value of the urlDeliveryNotification property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setURLDeliveryNotification(String value) {
        this.urlDeliveryNotification = value;
    }

    /**
     * Gets the value of the urlNonDeliveryNotification property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getURLNonDeliveryNotification() {
        return urlNonDeliveryNotification;
    }

    /**
     * Sets the value of the urlNonDeliveryNotification property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setURLNonDeliveryNotification(String value) {
        this.urlNonDeliveryNotification = value;
    }

    /**
     * Gets the value of the affiliateId property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getAffiliateId() {
        return affiliateId;
    }

    /**
     * Sets the value of the affiliateId property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setAffiliateId(String value) {
        this.affiliateId = value;
    }

}
