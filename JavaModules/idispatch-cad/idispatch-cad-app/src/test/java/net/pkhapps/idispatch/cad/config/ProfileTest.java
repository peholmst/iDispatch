package net.pkhapps.idispatch.cad.config;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit test for {@link Profile}.
 */
public class ProfileTest {

    @Test
    public void activeProfiles() {
        System.setProperty("profiles.active", "production, testProfile, HelloWorld");
        Profile.reloadActiveProfiles();
        assertThat(Profile.activeProfiles()).containsOnly("production", "testprofile", "helloworld");
    }

    @Test
    public void activateProfile() {
        System.setProperty("profiles.active", "production, testProfile, HelloWorld");
        Profile.reloadActiveProfiles();
        Profile.activateProfile("Me2");
        assertThat(Profile.activeProfiles()).containsOnly("production", "testprofile", "helloworld", "me2");
    }

    @Test
    public void deactivateProfile() {
        System.setProperty("profiles.active", "production, testProfile, HelloWorld");
        Profile.reloadActiveProfiles();
        Profile.deactivateProfile("helloWorld");
        assertThat(Profile.activeProfiles()).containsOnly("production", "testprofile");
    }
}
