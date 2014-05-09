
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
 *         &lt;element name="SendTextSMSResult" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
        "sendTextSMSResult"
})
@XmlRootElement(name = "SendTextSMSResponse")
public class SendTextSMSResponse {

    @XmlElement(name = "SendTextSMSResult")
    protected String sendTextSMSResult;

    /**
     * Gets the value of the sendTextSMSResult property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getSendTextSMSResult() {
        return sendTextSMSResult;
    }

    /**
     * Sets the value of the sendTextSMSResult property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setSendTextSMSResult(String value) {
        this.sendTextSMSResult = value;
    }

}
