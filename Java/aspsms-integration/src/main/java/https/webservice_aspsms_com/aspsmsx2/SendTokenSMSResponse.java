
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
 *         &lt;element name="SendTokenSMSResult" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
        "sendTokenSMSResult"
})
@XmlRootElement(name = "SendTokenSMSResponse")
public class SendTokenSMSResponse {

    @XmlElement(name = "SendTokenSMSResult")
    protected String sendTokenSMSResult;

    /**
     * Gets the value of the sendTokenSMSResult property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getSendTokenSMSResult() {
        return sendTokenSMSResult;
    }

    /**
     * Sets the value of the sendTokenSMSResult property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setSendTokenSMSResult(String value) {
        this.sendTokenSMSResult = value;
    }

}
