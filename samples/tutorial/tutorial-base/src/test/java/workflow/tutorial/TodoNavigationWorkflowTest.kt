package workflow.tutorial

import com.squareup.workflow1.WorkflowOutput
import com.squareup.workflow1.testing.expectWorkflow
import com.squareup.workflow1.testing.testRender
import com.squareup.workflow1.ui.TextController
import org.junit.Test
import workflow.tutorial.TodoEditWorkflow.Output.SaveChanges
import workflow.tutorial.TodoListWorkflow.Output.TodoSelected
import workflow.tutorial.TodoNavigationWorkflow.State
import workflow.tutorial.TodoNavigationWorkflow.State.Step.Edit
import workflow.tutorial.TodoNavigationWorkflow.State.Step.List
import workflow.tutorial.TodoNavigationWorkflow.TodoProps
import kotlin.test.assertEquals

class TodoNavigationWorkflowTest {
  @Test fun `selecting todo`() {
    val todos = listOf(TodoModel(title = "Title", note = "Note"))

    TodoNavigationWorkflow
      .testRender(
        props = TodoProps(username = "Alice"),
        initialState = State(todos = todos, step = List)
      )
      .expectWorkflow(
        workflowType = TodoListWorkflow::class,
        rendering = TodoListScreen(
          username = "",
          todoTitles = listOf("Title"),
          onRowPressed = {},
          onBackPressed = {},
          onAddPressed = {}
        ),
        output = WorkflowOutput(TodoSelected(index = 0))
      )
      .render { backstack ->
        assertEquals(1, backstack.size)
      }
      .verifyActionResult { newState, _ ->
        assertEqualState(
          State(
            todos = listOf(TodoModel(title = "Title", note = "Note")),
            step = Edit(0)
          ), newState
        )
      }
  }

  @Test fun `saving todo`() {
    val todos = listOf(TodoModel(title = "Title", note = "Note"))
    TodoNavigationWorkflow
      .testRender(
        props = TodoProps(username = "Alice"),
        initialState = State(todos = todos, step = Edit(0))
      )
      .expectWorkflow(
        workflowType = TodoListWorkflow::class,
        rendering = TodoListScreen(
          username = "",
          todoTitles = listOf("Title"),
          onRowPressed = {},
          onBackPressed = {},
          onAddPressed = {}
        )
      )
      .expectWorkflow(
        workflowType = TodoEditWorkflow::class,
        rendering = TodoEditScreen(
          title = TextController("Title"),
          note = TextController("Note"),
          onSavePressed = {},
          onBackPressed = {},
        ),
        output = WorkflowOutput(
          SaveChanges(
            TodoModel(
              title = "Updated Title",
              note = "Updated Note"
            )
          )
        )
      )
      .render { rendering ->
        assertEquals(2, rendering.size)
      }
      .verifyActionResult { newState, _ ->
        assertEqualState(
          State(
            todos = listOf(TodoModel(title = "Updated Title", note = "Updated Note")),
            step = List,
          ), newState
        )
      }
  }

  private fun assertEqualState(
    expected: State,
    actual: State
  ) {
    assertEquals(expected.todos.size, actual.todos.size)
    expected.todos.forEachIndexed { index, _ ->
      assertEquals(
        expected.todos[index].title,
        actual.todos[index].title,
        "todos[$index].title"
      )
      assertEquals(
        expected.todos[index].note,
        actual.todos[index].note,
        "todos[$index].note"
      )
    }
    assertEquals(expected.step, actual.step)
  }
}
