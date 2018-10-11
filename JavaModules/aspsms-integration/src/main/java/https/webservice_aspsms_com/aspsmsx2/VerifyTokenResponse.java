
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
 *         &lt;element name="VerifyTokenResult" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
        "verifyTokenResult"
})
@XmlRootElement(name = "VerifyTokenResponse")
public class VerifyTokenResponse {

    @XmlElement(name = "VerifyTokenResult")
    protected String verifyTokenResult;

    /**
     * Gets the value of the verifyTokenResult property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getVerifyTokenResult() {
        return verifyTokenResult;
    }

    /**
     * Sets the value of the verifyTokenResult property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setVerifyTokenResult(String value) {
        this.verifyTokenResult = value;
    }

}
