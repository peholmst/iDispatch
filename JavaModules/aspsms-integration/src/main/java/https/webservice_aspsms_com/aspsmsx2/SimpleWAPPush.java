
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
 *         &lt;element name="Recipients" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="Originator" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="WapDescription" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="WapURL" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="DeferredDeliveryTime" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="FlashingSMS" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="TimeZone" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
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
        "recipients",
        "originator",
        "wapDescription",
        "wapURL",
        "deferredDeliveryTime",
        "flashingSMS",
        "timeZone",
        "urlBufferedMessageNotification",
        "urlDeliveryNotification",
        "urlNonDeliveryNotification",
        "affiliateId"
})
@XmlRootElement(name = "SimpleWAPPush")
public class SimpleWAPPush {

    @XmlElement(name = "UserKey")
    protected String userKey;
    @XmlElement(name = "Password")
    protected String password;
    @XmlElement(name = "Recipients")
    protected String recipients;
    @XmlElement(name = "Originator")
    protected String originator;
    @XmlElement(name = "WapDescription")
    protected String wapDescription;
    @XmlElement(name = "WapURL")
    protected String wapURL;
    @XmlElement(name = "DeferredDeliveryTime")
    protected String deferredDeliveryTime;
    @XmlElement(name = "FlashingSMS")
    protected String flashingSMS;
    @XmlElement(name = "TimeZone")
    protected String timeZone;
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
     * Gets the value of the wapDescription property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getWapDescription() {
        return wapDescription;
    }

    /**
     * Sets the value of the wapDescription property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setWapDescription(String value) {
        this.wapDescription = value;
    }

    /**
     * Gets the value of the wapURL property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getWapURL() {
        return wapURL;
    }

    /**
     * Sets the value of the wapURL property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setWapURL(String value) {
        this.wapURL = value;
    }

    /**
     * Gets the value of the deferredDeliveryTime property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getDeferredDeliveryTime() {
        return deferredDeliveryTime;
    }

    /**
     * Sets the value of the deferredDeliveryTime property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setDeferredDeliveryTime(String value) {
        this.deferredDeliveryTime = value;
    }

    /**
     * Gets the value of the flashingSMS property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getFlashingSMS() {
        return flashingSMS;
    }

    /**
     * Sets the value of the flashingSMS property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setFlashingSMS(String value) {
        this.flashingSMS = value;
    }

    /**
     * Gets the value of the timeZone property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getTimeZone() {
        return timeZone;
    }

    /**
     * Sets the value of the timeZone property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setTimeZone(String value) {
        this.timeZone = value;
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
