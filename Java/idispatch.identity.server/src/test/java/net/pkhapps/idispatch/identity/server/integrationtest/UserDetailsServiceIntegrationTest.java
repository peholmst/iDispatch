package net.pkhapps.idispatch.identity.server.integrationtest;

import net.pkhapps.idispatch.identity.server.domain.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles({"test", "devel"})
@RunWith(SpringRunner.class)
@SpringBootTest
public class UserDetailsServiceIntegrationTest {

    @Autowired
    UserDetailsService userDetailsService;

    @Autowired
    UserRepository userRepository;

    @Autowired
    OrganizationRepository organizationRepository;

    @Test(expected = UsernameNotFoundException.class)
    public void loadUserByUsername_userDoesNotExist_exceptionThrown() {
        userDetailsService.loadUserByUsername("nonexistent user");
    }

    @Test
    public void loadUserByUsername_userExists_userReturned() {
        var org = organizationRepository.save(new Organization("Test Organization"));
        var user = userRepository.save(new User("joecool", UserType.INDIVIDUAL, org));

        var result = userDetailsService.loadUserByUsername("joecool");
        assertThat(result).isEqualTo(user);
    }
}
