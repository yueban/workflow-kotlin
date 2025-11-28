package workflow.tutorial

import com.squareup.workflow1.Snapshot
import com.squareup.workflow1.StatefulWorkflow
import com.squareup.workflow1.parse
import workflow.tutorial.WelcomeWorkflow.LoggedIn
import workflow.tutorial.WelcomeWorkflow.State
import java.nio.charset.Charset

object WelcomeWorkflow : StatefulWorkflow<Unit, State, LoggedIn, WelcomeScreen>() {
  data class State(val prompt: String)

  data class LoggedIn(val username: String)

  override fun initialState(
    props: Unit,
    snapshot: Snapshot?
  ): State =
    snapshot?.bytes?.parse { it.readString(Charset.defaultCharset()) }?.let { State(prompt = it) }
      ?: State(prompt = "Hello Workflow!")

  override fun render(
    renderProps: Unit,
    renderState: State,
    context: RenderContext<Unit, State, LoggedIn>
  ): WelcomeScreen {
    return WelcomeScreen(
      promptText = renderState.prompt,
      onLogInTapped = context.eventHandler("onLogInTapped") { name ->
        if (name.isEmpty()) {
          state = state.copy(prompt = "name required to log in")
        } else {
          setOutput(LoggedIn(name))
        }
      }
    )
  }

  override fun snapshotState(state: State): Snapshot = Snapshot.of(state.prompt)
}
