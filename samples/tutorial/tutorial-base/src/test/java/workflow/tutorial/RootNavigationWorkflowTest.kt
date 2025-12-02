package workflow.tutorial

import com.squareup.workflow1.WorkflowOutput
import com.squareup.workflow1.testing.expectWorkflow
import com.squareup.workflow1.testing.launchForTestingFromStartWith
import com.squareup.workflow1.testing.testRender
import workflow.tutorial.RootNavigationWorkflow.State.ShowingTodo
import workflow.tutorial.RootNavigationWorkflow.State.ShowingWelcome
import workflow.tutorial.WelcomeWorkflow.LoggedIn
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class RootNavigationWorkflowTest {
  @Test fun `welcome rendering`() {
    RootNavigationWorkflow
      .testRender(initialState = ShowingWelcome, props = Unit)
      .expectWorkflow(
        workflowType = WelcomeWorkflow::class,
        rendering = WelcomeScreen(
          promptText = "Well hello there!",
          onLogInTapped = {}
        )
      )
      .render { rendering ->
        val frames = rendering.frames
        assertEquals(1, frames.size)

        val welcomeScreen = frames[0] as WelcomeScreen
        assertEquals("Well hello there!", welcomeScreen.promptText)
      }
      .verifyActionResult { _, output ->
        assertNull(output)
      }
  }

  @Test fun `login event`() {
    RootNavigationWorkflow
      .testRender(initialState = ShowingWelcome, props = Unit)
      .expectWorkflow(
        workflowType = WelcomeWorkflow::class,
        rendering = WelcomeScreen(
          promptText = "yo",
          onLogInTapped = {}
        ),
        output = WorkflowOutput(LoggedIn(username = "Alice"))
      )
      .render { rendering ->
        val backstack = rendering.frames
        assertEquals(1, backstack.size)

        assertEquals(WelcomeScreen::class, backstack[0]::class)
      }
      .verifyActionResult { newState, _ ->
        assertEquals(ShowingTodo(username = "Alice"), newState)
      }
  }

  @Test fun `app flow`() {
    RootNavigationWorkflow.launchForTestingFromStartWith {
      awaitNextRendering().let { rendering ->
        assertEquals(1, rendering.frames.size)
        val welcomeScreen = rendering.frames[0] as WelcomeScreen
        welcomeScreen.onLogInTapped("Alice")
      }

      awaitNextRendering().let { rendering ->
        assertEquals(2, rendering.frames.size)
        assertEquals(WelcomeScreen::class, rendering.frames[0]::class)
        val todoListScreen = rendering.frames[1] as TodoListScreen
        assertEquals(1, todoListScreen.todoTitles.size)
        todoListScreen.onRowPressed(0)
      }

      awaitNextRendering().let { rendering ->
        assertEquals(3, rendering.frames.size)
        assertEquals(WelcomeScreen::class, rendering.frames[0]::class)
        assertEquals(TodoListScreen::class, rendering.frames[1]::class)
        val editTodoScreen = rendering.frames[2] as TodoEditScreen
        editTodoScreen.title.textValue = "New Title"
        editTodoScreen.onSavePressed()
      }

      awaitNextRendering().let { rendering ->
        assertEquals(2, rendering.frames.size)
        assertEquals(WelcomeScreen::class, rendering.frames[0]::class)
        val todoListScreen = rendering.frames[1] as TodoListScreen
        assertEquals(1, todoListScreen.todoTitles.size)
        assertEquals("New Title", todoListScreen.todoTitles[0])
      }
    }
  }
}
