package net.pkhsolutions.idispatch;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@EnableWebSecurity
@Configuration
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
                .withUser("admin").password("tantalus").roles("ADMIN")
                .and()
                .withUser("dispatcher").password("chosen").roles("DISPATCHER")
                .and()
                .withUser("root").password("moviedrome").roles("ADMIN", "DISPATCHER");
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/VAADIN/**");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.authorizeRequests()
                .antMatchers("/rest/*").permitAll()
                .antMatchers("/ui/admin/**").hasRole("ADMIN")
                .antMatchers("/ui/dws/**").hasRole("DISPATCHER")
                .antMatchers("/ui/**").authenticated()
                .anyRequest().denyAll()
                .and()
                .httpBasic();
    }
}
