
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
 *         &lt;element name="SimpleWAPPushResult" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
        "simpleWAPPushResult"
})
@XmlRootElement(name = "SimpleWAPPushResponse")
public class SimpleWAPPushResponse {

    @XmlElement(name = "SimpleWAPPushResult")
    protected String simpleWAPPushResult;

    /**
     * Gets the value of the simpleWAPPushResult property.
     *
     * @return possible object is
     * {@link String }
     */
    public String getSimpleWAPPushResult() {
        return simpleWAPPushResult;
    }

    /**
     * Sets the value of the simpleWAPPushResult property.
     *
     * @param value allowed object is
     *              {@link String }
     */
    public void setSimpleWAPPushResult(String value) {
        this.simpleWAPPushResult = value;
    }

}
