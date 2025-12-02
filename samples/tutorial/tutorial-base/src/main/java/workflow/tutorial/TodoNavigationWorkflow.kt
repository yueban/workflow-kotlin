package workflow.tutorial

import com.squareup.workflow1.Snapshot
import com.squareup.workflow1.StatefulWorkflow
import com.squareup.workflow1.action
import com.squareup.workflow1.ui.Screen
import workflow.tutorial.TodoNavigationWorkflow.Back
import workflow.tutorial.TodoNavigationWorkflow.State
import workflow.tutorial.TodoNavigationWorkflow.State.Step
import workflow.tutorial.TodoNavigationWorkflow.TodoProps

object TodoNavigationWorkflow : StatefulWorkflow<TodoProps, State, Back, List<Screen>>() {

  data class TodoProps(val username: String)

  data class State(
    val todos: List<TodoModel>,
    val step: Step
  ) {
    sealed interface Step {
      object List : Step
      data class Edit(val index: Int) : Step
    }
  }

  object Back

  override fun initialState(
    props: TodoProps,
    snapshot: Snapshot?
  ): State = State(
    todos = listOf(
      TodoModel(
        title = "Take the cat for a walk",
        note = "Cats really need their outside sunshine time. Don't forget to walk " +
          "Charlie. Hamilton is less excited about the prospect."
      )
    ),
    step = Step.List
  )

  override fun render(
    renderProps: TodoProps,
    renderState: State,
    context: RenderContext<TodoProps, State, Back>
  ): List<Screen> {
    val todoListScreen = context.renderChild(
      TodoListWorkflow,
      props = TodoListWorkflow.ListProps(
        username = renderProps.username,
        todos = renderState.todos
      )
    ) { output ->
      when (output) {
        TodoListWorkflow.Output.BackPressed -> goBack()
        is TodoListWorkflow.Output.TodoSelected -> editTodo(output.index)
        TodoListWorkflow.Output.AddPressed -> createTodo()
      }
    }

    return when (val step = renderState.step) {
      Step.List -> listOf(todoListScreen)

      is Step.Edit -> {
        val todoEditScreen = context.renderChild(
          TodoEditWorkflow,
          TodoEditWorkflow.Props(renderState.todos[step.index])
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

  private fun goBack() = action("goBack") {
    setOutput(Back)
  }

  private fun editTodo(index: Int) = action("editText") {
    state = state.copy(step = Step.Edit(index))
  }

  private fun createTodo() = action("createTodo") {
    state = state.copy(todos = state.todos + TodoModel(title = "New Todo", note = ""))
  }

  private fun discardChanges() = action("discardChanges") {
    state = state.copy(step = Step.List)
  }

  private fun saveChanges(
    todo: TodoModel,
    index: Int
  ) = action("saveChanges") {
    state = state.copy(
      todos = state.todos.toMutableList().also { it[index] = todo },
      step = Step.List
    )
  }
}
