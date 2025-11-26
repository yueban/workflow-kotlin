package workflow.tutorial

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import com.squareup.workflow1.RuntimeConfigOptions
import com.squareup.workflow1.WorkflowExperimentalRuntime
import com.squareup.workflow1.mapRendering
import com.squareup.workflow1.ui.ViewEnvironment
import com.squareup.workflow1.ui.compose.WorkflowRendering
import com.squareup.workflow1.ui.compose.renderAsState
import com.squareup.workflow1.ui.compose.withComposeInteropSupport
import com.squareup.workflow1.ui.withEnvironment

private val viewEnvironment = ViewEnvironment.EMPTY.withComposeInteropSupport()

@OptIn(WorkflowExperimentalRuntime::class)
@Composable
fun App() {
  MaterialTheme {
    val rendering by WelcomeWorkflow.mapRendering { it.withEnvironment(viewEnvironment) }
      .renderAsState(
        props = Unit,
        runtimeConfig = RuntimeConfigOptions.ALL,
        onOutput = {}
      )
    WorkflowRendering(rendering)
  }
}
