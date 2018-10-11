package net.pkhapps.idispatch.web.ui.dispatch;

import net.pkhapps.idispatch.web.ui.common.i18n.I18N;
import net.pkhapps.idispatch.web.ui.common.i18n.MessageSourceI18N;
import net.pkhapps.idispatch.web.ui.dispatch.annotation.DispatchQualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Locale;

/**
 * TODO Document me!
 */
@Configuration
public class DispatchConfig {

    @Value("${dispatch.ui.locale}")
    private Locale locale;

    @Bean
    @DispatchQualifier
    I18N dispatchI18N() {
        return new MessageSourceI18N("i18n/dispatch", locale);
    }
}
