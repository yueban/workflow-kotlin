package workflow.tutorial

import com.squareup.workflow1.testing.testRender
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertSame

class TodoEditWorkflowTest {
  @Test fun `save emits model`() {
    val props = TodoEditWorkflow.Props(TodoModel(title = "Title", note = "Note"))

    TodoEditWorkflow.testRender(props)
      .render { screen ->
        screen.title.textValue = "New Title"
        screen.note.textValue = "New Note"
        screen.onSavePressed()
      }.verifyActionResult { _, output ->
        val expected =
          TodoEditWorkflow.Output.SaveChanges(TodoModel(title = "New Title", note = "New Note"))
        assertEquals(expected, output?.value)
      }
  }

  @Test fun `back press discards`() {
    val props = TodoEditWorkflow.Props(TodoModel(title = "Title", note = "Note"))

    TodoEditWorkflow.testRender(props)
      .render { screen -> screen.onBackPressed() }
      .verifyActionResult { _, output ->
        assertSame(TodoEditWorkflow.Output.DiscardChanges, output?.value)
      }
  }
}
