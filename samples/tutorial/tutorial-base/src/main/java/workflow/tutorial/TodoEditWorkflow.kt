package workflow.tutorial

import com.squareup.workflow1.Snapshot
import com.squareup.workflow1.StatefulWorkflow
import com.squareup.workflow1.ui.TextController
import workflow.tutorial.TodoEditWorkflow.Output
import workflow.tutorial.TodoEditWorkflow.Props
import workflow.tutorial.TodoEditWorkflow.State
import workflow.tutorial.TodoListWorkflow.TodoModel

object TodoEditWorkflow : StatefulWorkflow<Props, State, Output, TodoEditScreen>() {
  data class Props(
    val initialTodo: TodoModel
  )

  data class State(
    val editedTitle: TextController,
    val editedNote: TextController
  ) {
    fun toModel(): TodoModel = TodoModel(editedTitle.textValue, editedNote.textValue)

    companion object {
      fun forModel(model: TodoModel): State = State(
        editedTitle = TextController(model.title),
        editedNote = TextController(model.note)
      )
    }
  }

  sealed interface Output {
    object DiscardChanges : Output
    data class SaveChanges(val todo: TodoModel) : Output
  }

  override fun initialState(
    props: Props,
    snapshot: Snapshot?
  ): State = State.forModel(props.initialTodo)

  override fun render(
    renderProps: Props,
    renderState: State,
    context: RenderContext<Props, State, Output>
  ): TodoEditScreen = TodoEditScreen(
    title = renderState.editedTitle,
    note = renderState.editedNote,
    onBackPressed = context.eventHandler("onBackPressed") {
      setOutput(Output.DiscardChanges)
    },
    onSavePressed = context.eventHandler("onSavePressed") {
      setOutput(Output.SaveChanges(renderState.toModel()))
    }
  )

  override fun snapshotState(state: State): Snapshot? = null
}
