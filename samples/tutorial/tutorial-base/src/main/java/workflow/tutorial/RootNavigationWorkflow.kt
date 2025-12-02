package workflow.tutorial

import com.squareup.workflow1.Snapshot
import com.squareup.workflow1.StatefulWorkflow
import com.squareup.workflow1.action
import com.squareup.workflow1.renderChild
import com.squareup.workflow1.ui.navigation.BackStackScreen
import com.squareup.workflow1.ui.navigation.toBackStackScreen
import workflow.tutorial.RootNavigationWorkflow.State

object RootNavigationWorkflow : StatefulWorkflow<Unit, State, Nothing, BackStackScreen<*>>() {

  sealed interface State {
    object ShowingWelcome : State
    data class ShowingTodo(val username: String) : State
  }

  override fun initialState(
    props: Unit,
    snapshot: Snapshot?
  ): State = State.ShowingWelcome

  override fun render(
    renderProps: Unit,
    renderState: State,
    context: RenderContext<Unit, State, Nothing>
  ): BackStackScreen<*> {
    val welcomeScreen = context.renderChild(WelcomeWorkflow) {
      logIn(it.username)
    }
    return when (renderState) {
      State.ShowingWelcome -> BackStackScreen(welcomeScreen)
      is State.ShowingTodo -> {
        val todoBackStack = context.renderChild(
          child = TodoListWorkflow,
          props = TodoListWorkflow.ListProps(renderState.username),
          handler = { logOut() }
        )
        (listOf(welcomeScreen) + todoBackStack).toBackStackScreen()
      }
    }
  }

  override fun snapshotState(state: State): Snapshot? = null

  private fun logIn(username: String) = action("logIn") {
    state = State.ShowingTodo(username)
  }

  private fun logOut() = action("logOut") {
    state = State.ShowingWelcome
  }
}
