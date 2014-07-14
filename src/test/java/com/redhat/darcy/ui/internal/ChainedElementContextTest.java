package com.redhat.darcy.ui.internal;

import static com.redhat.darcy.ui.internal.ChainedElementContext.makeChainedElementContext;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;

import com.redhat.darcy.ui.api.ElementContext;
import com.redhat.darcy.ui.testing.doubles.DummyContext;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class ChainedElementContextTest {
    @Test
    public void shouldImplementAllInterfacesOfParentContext() {
        ElementContext context = makeChainedElementContext(new TestContext(), null);

        assertThat(context, instanceOf(TestInterface.class));
    }

    interface TestInterface {}
    class TestContext extends DummyContext implements TestInterface {}
}
