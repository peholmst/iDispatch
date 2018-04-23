
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
 *         &lt;element name="OriginatorUnlockCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
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
        "originatorUnlockCode",
        "affiliateId"
})
@XmlRootElement(name = "UnlockOriginator")
public class UnlockOriginator {

    @XmlElement(name = "UserKey")
    protected String userKey;
    @XmlElement(name = "Password")
    protected String password;
    @XmlElement(name = "Originator")
    protected String originator;
    @XmlElement(name = "OriginatorUnlockCode")
    protected String originatorUnlockCode;
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
     * Gets the value of the originatorUnlockCode property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getOriginatorUnlockCode() {
        return originatorUnlockCode;
    }

    /**
     * Sets the value of the originatorUnlockCode property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setOriginatorUnlockCode(String value) {
        this.originatorUnlockCode = value;
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
