package workflow.tutorial

import com.squareup.workflow1.testing.testRender
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class WelcomeWorkflowTest {
  @Test fun `successful log in`() {
    WelcomeWorkflow
      .testRender(props = Unit)
      .render { screen ->
        screen.onLogInTapped("Alice")
      }
      .verifyActionResult { _, output ->
        assertEquals(WelcomeWorkflow.LoggedIn("Alice"), output?.value)
      }
  }

  @Test fun `failed log in`() {
    WelcomeWorkflow
      .testRender(props = Unit)
      .render { screen ->
        screen.onLogInTapped("")
      }
      .verifyActionResult { _, output ->
        assertNull(output)
      }
      .testNextRender()
      .render { screen ->
        assertEquals("name required to log in", screen.promptText)
      }
  }
}
