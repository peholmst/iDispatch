
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
 *         &lt;element name="GetStatusCodeDescriptionResult" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
        "getStatusCodeDescriptionResult"
})
@XmlRootElement(name = "GetStatusCodeDescriptionResponse")
public class GetStatusCodeDescriptionResponse {

    @XmlElement(name = "GetStatusCodeDescriptionResult")
    protected String getStatusCodeDescriptionResult;

    /**
     * Gets the value of the getStatusCodeDescriptionResult property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getGetStatusCodeDescriptionResult() {
        return getStatusCodeDescriptionResult;
    }

    /**
     * Sets the value of the getStatusCodeDescriptionResult property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setGetStatusCodeDescriptionResult(String value) {
        this.getStatusCodeDescriptionResult = value;
    }

}
