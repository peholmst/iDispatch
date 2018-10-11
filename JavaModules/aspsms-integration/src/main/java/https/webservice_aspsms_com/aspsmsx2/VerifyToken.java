
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
 *         &lt;element name="PhoneNumber" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="TokenReference" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="VerificationCode" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
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
        "phoneNumber",
        "tokenReference",
        "verificationCode"
})
@XmlRootElement(name = "VerifyToken")
public class VerifyToken {

    @XmlElement(name = "UserKey")
    protected String userKey;
    @XmlElement(name = "Password")
    protected String password;
    @XmlElement(name = "PhoneNumber")
    protected String phoneNumber;
    @XmlElement(name = "TokenReference")
    protected String tokenReference;
    @XmlElement(name = "VerificationCode")
    protected String verificationCode;

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
     * Gets the value of the phoneNumber property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getPhoneNumber() {
        return phoneNumber;
    }

    /**
     * Sets the value of the phoneNumber property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setPhoneNumber(String value) {
        this.phoneNumber = value;
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

}
