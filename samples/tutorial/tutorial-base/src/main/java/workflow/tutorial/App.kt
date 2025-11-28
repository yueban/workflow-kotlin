package workflow.tutorial

import android.util.Log
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
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
    val workflow = remember {
      RootNavigationWorkflow.mapRendering { it.withEnvironment(viewEnvironment) }
    }
    val rendering by workflow
      .renderAsState(
        props = Unit,
        runtimeConfig = RuntimeConfigOptions.ALL,
        onOutput = {}
      )
    LaunchedEffect(rendering) {
      Log.i("navigate", rendering.content.toString())
    }
    WorkflowRendering(rendering)
  }
}
