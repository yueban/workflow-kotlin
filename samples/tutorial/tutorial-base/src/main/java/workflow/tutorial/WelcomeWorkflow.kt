package workflow.tutorial

import com.squareup.workflow1.Snapshot
import com.squareup.workflow1.StatefulWorkflow
import com.squareup.workflow1.parse
import workflow.tutorial.WelcomeWorkflow.Output
import workflow.tutorial.WelcomeWorkflow.State
import java.nio.charset.Charset

object WelcomeWorkflow : StatefulWorkflow<Unit, State, Output, WelcomeScreen>() {
  data class State(
    val prompt: String
  )

  object Output

  override fun initialState(
    props: Unit,
    snapshot: Snapshot?
  ): State =
    snapshot?.bytes?.parse { it.readString(Charset.defaultCharset()) }?.let { State(prompt = it) }
      ?: State(prompt = "Hello Workflow!")

  override fun render(
    renderProps: Unit,
    renderState: State,
    context: RenderContext<Unit, State, Output>
  ): WelcomeScreen {
    return WelcomeScreen(
      promptText = renderState.prompt,
      onLogInTapped = context.eventHandler("onLogInTapped") { name ->
        state = when {
          name.isEmpty() -> state.copy(prompt = "name required to log in")
          else -> state.copy(prompt = "logging in as \"$name\"…")
        }
      }
    )
  }

  override fun snapshotState(state: State): Snapshot = Snapshot.of(state.prompt)
}
