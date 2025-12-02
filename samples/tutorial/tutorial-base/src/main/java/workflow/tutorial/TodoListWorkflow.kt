package workflow.tutorial

import com.squareup.workflow1.Snapshot
import com.squareup.workflow1.StatefulWorkflow
import com.squareup.workflow1.action
import com.squareup.workflow1.ui.Screen
import workflow.tutorial.TodoListWorkflow.BackPressed
import workflow.tutorial.TodoListWorkflow.ListProps
import workflow.tutorial.TodoListWorkflow.State

object TodoListWorkflow : StatefulWorkflow<ListProps, State, BackPressed, List<Screen>>() {

  data class TodoModel(
    val title: String,
    val note: String
  )

  data class State(
    val todos: List<TodoModel>,
    val step: Step
  ) {
    sealed interface Step {
      object ShowList : Step
      data class EditItem(val index: Int) : Step
    }
  }

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
    ),
    State.Step.ShowList
  )

  override fun render(
    renderProps: ListProps,
    renderState: State,
    context: RenderContext<ListProps, State, BackPressed>
  ): List<Screen> {
    val titles = renderState.todos.map { it.title }
    val todoListScreen = TodoListScreen(
      username = renderProps.username,
      todoTitles = renderState.todos.map { it.title },
      onBackPressed = context.eventHandler("onBackPressed") { setOutput(BackPressed) },
      onRowPressed = context.eventHandler("onRowPressed") { index ->
        state = state.copy(step = State.Step.EditItem(index))
      }
    )

    return when (val step = renderState.step) {
      State.Step.ShowList -> listOf(todoListScreen)

      is State.Step.EditItem -> {
        val todoEditScreen = context.renderChild(
          TodoEditWorkflow,
          props = TodoEditWorkflow.Props(renderState.todos[step.index]),
        ) { output ->
          when (output) {
            TodoEditWorkflow.Output.DiscardChanges -> discardChanges()
            is TodoEditWorkflow.Output.SaveChanges -> saveChanges(output.todo, step.index)
          }
        }
        listOf(todoListScreen, todoEditScreen)
      }
    }
  }

  override fun snapshotState(state: State): Snapshot? = null

  private fun discardChanges() = action("discardChanges") {
    state = state.copy(step = State.Step.ShowList)
  }

  private fun saveChanges(
    todo: TodoModel,
    index: Int
  ) = action("saveChanges") {
    state = state.copy(
      todos = state.todos.toMutableList().also { it[index] = todo },
      step = State.Step.ShowList
    )
  }
}
