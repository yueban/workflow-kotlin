package workflow.tutorial

import com.squareup.workflow1.StatelessWorkflow
import workflow.tutorial.TodoListWorkflow.ListProps
import workflow.tutorial.TodoListWorkflow.Output

object TodoListWorkflow : StatelessWorkflow<ListProps, Output, TodoListScreen>() {

  data class ListProps(
    val username: String,
    val todos: List<TodoModel>
  )

  sealed interface Output {
    object BackPressed : Output
    data class TodoSelected(val index: Int) : Output
    object AddPressed : Output
  }

  override fun render(
    renderProps: ListProps,
    context: RenderContext<ListProps, Output>
  ): TodoListScreen = TodoListScreen(
    username = renderProps.username,
    todoTitles = renderProps.todos.map { it.title },
    onBackPressed = context.eventHandler("onBackPressed") { setOutput(Output.BackPressed) },
    onRowPressed = context.eventHandler("onRowPressed") { index ->
      setOutput(Output.TodoSelected(index))
    },
    onAddPressed = context.eventHandler("onAddPressed") {
      setOutput(Output.AddPressed)
    }
  )
}
