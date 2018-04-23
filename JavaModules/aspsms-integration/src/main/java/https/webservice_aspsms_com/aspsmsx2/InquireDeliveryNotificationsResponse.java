
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
 *         &lt;element name="InquireDeliveryNotificationsResult" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
        "inquireDeliveryNotificationsResult"
})
@XmlRootElement(name = "InquireDeliveryNotificationsResponse")
public class InquireDeliveryNotificationsResponse {

    @XmlElement(name = "InquireDeliveryNotificationsResult")
    protected String inquireDeliveryNotificationsResult;

    /**
     * Gets the value of the inquireDeliveryNotificationsResult property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getInquireDeliveryNotificationsResult() {
        return inquireDeliveryNotificationsResult;
    }

    /**
     * Sets the value of the inquireDeliveryNotificationsResult property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setInquireDeliveryNotificationsResult(String value) {
        this.inquireDeliveryNotificationsResult = value;
    }

}
