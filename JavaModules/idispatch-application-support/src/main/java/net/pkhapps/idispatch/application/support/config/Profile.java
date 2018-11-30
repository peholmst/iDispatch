package net.pkhapps.idispatch.application.support.config;

import lombok.extern.slf4j.Slf4j;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.ThreadSafe;
import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Class for accessing the active system profiles. These profiles are activated by setting the
 * {@value #ACTIVE_PROFILES_SYSTEM_PROPERTY} system property to a comma separated string of profile names.
 */
@ThreadSafe
@Slf4j
public final class Profile {

    private static final String ACTIVE_PROFILES_SYSTEM_PROPERTY = "profiles.active";

    private static Set<String> ACTIVE_PROFILES;

    private Profile() {
        // NOP
    }

    /**
     * Returns the currently active profiles. All profile names are lower case.
     *
     * @return an unmodifiable set of profile names.
     */
    @Nonnull
    public static Set<String> activeProfiles() {
        if (ACTIVE_PROFILES == null) {
            // No need to synchronize here since it does not really matter if loadActiveProfiles() is called more
            // than once by different threads in the beginning. The end result will be the same anyway.
            loadActiveProfiles();
        }
        return ACTIVE_PROFILES;
    }

    /**
     * Forces the profile names to be re-read from the system property when requested the next time.
     */
    static synchronized void reloadActiveProfiles() {
        ACTIVE_PROFILES = null;
    }

    private static synchronized void loadActiveProfiles() {
        var activeProfilesString = System.getProperty(ACTIVE_PROFILES_SYSTEM_PROPERTY);
        if (activeProfilesString == null) {
            log.info("No active profiles declared in system property '{}'", ACTIVE_PROFILES_SYSTEM_PROPERTY);
            ACTIVE_PROFILES = Collections.emptySet();
        } else {
            ACTIVE_PROFILES = Stream.of(activeProfilesString.split(","))
                    .map(String::trim)
                    .map(String::toLowerCase)
                    .collect(Collectors.toUnmodifiableSet());
            log.info("Active profiles in system property '{}': {}", ACTIVE_PROFILES_SYSTEM_PROPERTY, ACTIVE_PROFILES);
        }
    }

    /**
     * Checks whether the given profile is currently active.
     *
     * @param profileName the name of the profile to check (case insensitive).
     * @return true if the profile is active, false otherwise.
     */
    public static boolean isProfileActive(@Nonnull String profileName) {
        Objects.requireNonNull(profileName, "profileName must not be null");
        return activeProfiles().contains(profileName.trim().toLowerCase());
    }

    /**
     * Forces the given profile to become active regardless of the value of the
     * {@value #ACTIVE_PROFILES_SYSTEM_PROPERTY} system property. This will be forgotten as soon as
     * {@link #reloadActiveProfiles()} is called.
     *
     * @param profileName the profile to activate.
     */
    public static synchronized void activateProfile(@Nonnull String profileName) {
        Objects.requireNonNull(profileName, "profileName must not be null");
        var existingProfiles = new HashSet<String>(activeProfiles());
        var trimmedProfileName = profileName.trim().toLowerCase();
        existingProfiles.add(trimmedProfileName);
        ACTIVE_PROFILES = Set.copyOf(existingProfiles);
        log.info("Active profiles after explicitly activating '{}': {}", trimmedProfileName, ACTIVE_PROFILES);
    }

    /**
     * Forces the given profile to become inactive regardless of the value of the
     * {@value #ACTIVE_PROFILES_SYSTEM_PROPERTY} system property. This will be forgotten as soon as
     * {@link #reloadActiveProfiles()} is called.
     *
     * @param profileName the profile to deactivate.
     */
    public static synchronized void deactivateProfile(@Nonnull String profileName) {
        Objects.requireNonNull(profileName, "profileName must not be null");
        var existingProfiles = new HashSet<String>(activeProfiles());
        var trimmedProfileName = profileName.trim().toLowerCase();
        existingProfiles.remove(trimmedProfileName);
        ACTIVE_PROFILES = Set.copyOf(existingProfiles);
        log.info("Active profiles after explicitly deactivating '{}': {}", trimmedProfileName, ACTIVE_PROFILES);
    }
}
