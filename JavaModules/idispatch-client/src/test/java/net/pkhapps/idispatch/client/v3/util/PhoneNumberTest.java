package net.pkhapps.idispatch.client.v3.util;

import org.assertj.core.api.Java6Assertions;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit test for {@link PhoneNumber}.
 */
public class PhoneNumberTest extends JsonObjectTest {

    @Test
    public void toE164_domesticNumbers() {
        assertThat(new PhoneNumber("0401234567").toE164()).isEqualTo("+358401234567");
        assertThat(new PhoneNumber("021234567").toE164()).isEqualTo("+35821234567");
        assertThat(new PhoneNumber("+358401234567").toE164()).isEqualTo("+358401234567");
        assertThat(new PhoneNumber("+35821234567").toE164()).isEqualTo("+35821234567");
    }

    @Test
    public void toE164_internationalNumbers() {
        assertThat(new PhoneNumber("+447911123456").toE164()).isEqualTo("+447911123456");
    }

    @Test
    public void construct_stripSpecialCharactersAndWhitespace() {
        assertThat(new PhoneNumber("(040)-123 4567").toE164()).isEqualTo("+358401234567");
        assertThat(new PhoneNumber("+358-40-123-4567").toE164()).isEqualTo("+358401234567");
        assertThat(new PhoneNumber("+44.791.112.3456").toE164()).isEqualTo("+447911123456");
    }

    @Test(expected = IllegalArgumentException.class)
    public void construct_emptyNumber() {
        new PhoneNumber("");
    }

    @Test(expected = IllegalArgumentException.class)
    public void construct_illegalPhoneNumber() {
        new PhoneNumber("+358-This-is-not-a-number");
    }

    @Test
    public void serializeAndDeserialize() {
        var original = new PhoneNumber("+358401234567");
        var json = getGson().toJson(original);
        System.out.println(json);
        var deserialized = getGson().fromJson(json, PhoneNumber.class);
        Java6Assertions.assertThat(deserialized).isNotSameAs(original);
        Java6Assertions.assertThat(deserialized).isEqualTo(original);
    }
}
