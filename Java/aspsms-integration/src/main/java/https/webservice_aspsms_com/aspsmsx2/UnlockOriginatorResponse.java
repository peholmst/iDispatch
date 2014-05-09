
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
 *         &lt;element name="UnlockOriginatorResult" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
        "unlockOriginatorResult"
})
@XmlRootElement(name = "UnlockOriginatorResponse")
public class UnlockOriginatorResponse {

    @XmlElement(name = "UnlockOriginatorResult")
    protected String unlockOriginatorResult;

    /**
     * Gets the value of the unlockOriginatorResult property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getUnlockOriginatorResult() {
        return unlockOriginatorResult;
    }

    /**
     * Sets the value of the unlockOriginatorResult property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setUnlockOriginatorResult(String value) {
        this.unlockOriginatorResult = value;
    }

}
