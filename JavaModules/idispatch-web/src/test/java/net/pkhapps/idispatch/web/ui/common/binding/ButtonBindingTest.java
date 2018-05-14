package net.pkhapps.idispatch.web.ui.common.binding;

import com.vaadin.server.SerializableConsumer;
import com.vaadin.ui.Button;
import net.pkhapps.idispatch.web.ui.common.model.AbstractAction;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

/**
 * Unit test for {@link ButtonBinding}.
 */
public class ButtonBindingTest {

    @Test
    public void noResultHandler_buttonIsClicked_actionIsExecuted() {
        var button = new Button();
        var action = new TestAction();
        ButtonBinding.forButton(button).bind(action);

        button.click();

        assertThat(action.executionCount.get()).isEqualTo(1);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void resultHandler_buttonIsClicked_actionIsExecutedAndResultPassedToHandler() {
        var button = new Button();
        var action = new TestAction();
        var resultHandler = mock(SerializableConsumer.class);
        ButtonBinding.forButton(button).bind(action, resultHandler);

        button.click();

        var result = ArgumentCaptor.forClass(Integer.class);
        verify(resultHandler).accept(result.capture());
        assertThat(result.getValue()).isEqualTo(action.executionCount.get());
    }

    @Test
    public void isExecutableFlagChanges_buttonEnabledStateAlsoChanges() {
        var button = new Button();
        var action = new TestAction();
        action.setExecutable(false);
        ButtonBinding.forButton(button).bind(action);

        assertThat(button.isEnabled()).isFalse();

        action.setExecutable(true);

        assertThat(button.isEnabled()).isTrue();
    }

    private static class TestAction extends AbstractAction<Integer> {

        final AtomicInteger executionCount = new AtomicInteger();

        @Override
        protected Integer doExecute() {
            return executionCount.incrementAndGet();
        }
    }
}
