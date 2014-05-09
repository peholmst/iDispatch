
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
 *         &lt;element name="CheckCreditsResult" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
        "checkCreditsResult"
})
@XmlRootElement(name = "CheckCreditsResponse")
public class CheckCreditsResponse {

    @XmlElement(name = "CheckCreditsResult")
    protected String checkCreditsResult;

    /**
     * Gets the value of the checkCreditsResult property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getCheckCreditsResult() {
        return checkCreditsResult;
    }

    /**
     * Sets the value of the checkCreditsResult property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setCheckCreditsResult(String value) {
        this.checkCreditsResult = value;
    }

}
