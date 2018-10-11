package net.pkhapps.idispatch.web.ui.dispatch.view;

import com.vaadin.navigator.View;
import com.vaadin.spring.annotation.SpringComponent;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;

import javax.annotation.PostConstruct;

@SpringComponent
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class ErrorView extends VerticalLayout implements View {

    @PostConstruct
    void init() {
        addComponent(new Label("ERROR!!")); // TODO Implement me!
    }
}
