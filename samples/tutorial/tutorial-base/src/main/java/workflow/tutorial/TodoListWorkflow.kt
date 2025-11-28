package workflow.tutorial

import com.squareup.workflow1.Snapshot
import com.squareup.workflow1.StatefulWorkflow
import workflow.tutorial.TodoListWorkflow.BackPressed
import workflow.tutorial.TodoListWorkflow.ListProps
import workflow.tutorial.TodoListWorkflow.State

object TodoListWorkflow : StatefulWorkflow<ListProps, State, BackPressed, TodoListScreen>() {

  data class TodoModel(
    val title: String,
    val note: String
  )

  data class State(
    val todos: List<TodoModel>
  )

  data class ListProps(val username: String)

  object BackPressed

  override fun initialState(
    props: ListProps,
    snapshot: Snapshot?
  ): State = State(
    listOf(
      TodoModel(
        title = "Take the cat for a walk",
        note = "Cats really need their outside sunshine time. Don't forget to walk " +
          "Charlie. Hamilton is less excited about the prospect."
      )
    )
  )

  override fun render(
    renderProps: ListProps,
    renderState: State,
    context: RenderContext<ListProps, State, BackPressed>
  ): TodoListScreen =
    TodoListScreen(
      username = renderProps.username,
      todoTitles = renderState.todos.map { it.title },
      onBackPressed = context.eventHandler("onBackPressed") { setOutput(BackPressed) }
    )

  override fun snapshotState(state: State): Snapshot? = null
}
