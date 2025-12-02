package workflow.tutorial

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.squareup.workflow1.ui.compose.ComposeScreen
import workflow.tutorial.views.R

data class TodoListScreen(
  val username: String,
  val todoTitles: List<String>,
  val onBackPressed: () -> Unit,
  val onRowPressed: (Int) -> Unit,
  val onAddPressed: () -> Unit,
) : ComposeScreen {

  @Composable
  override fun Content() {
    TodoListScreenContent(this)
  }
}

@Composable
private fun TodoListScreenContent(
  screen: TodoListScreen,
  modifier: Modifier = Modifier,
) {
  BackHandler(onBack = screen.onBackPressed)

  Box(modifier = modifier.fillMaxSize()) {
    Column(modifier = Modifier.fillMaxSize()) {
      Text(
        text = stringResource(R.string.todo_list_welcome, screen.username),
        style = MaterialTheme.typography.bodyMedium,
        modifier = Modifier
          .align(Alignment.CenterHorizontally)
          .padding(16.dp)
      )

      Text(
        text = stringResource(R.string.todo_list_title),
        style = MaterialTheme.typography.bodyLarge,
        modifier = Modifier
          .align(Alignment.CenterHorizontally)
          .padding(bottom = 16.dp)
      )

      LazyColumn(
        modifier = Modifier
          .fillMaxWidth()
          .weight(1f)
      ) {
        itemsIndexed(screen.todoTitles) { i, item ->
          TodoListItem(
            text = item,
            onClick = { screen.onRowPressed(i) }
          )
        }
      }
    }

    Button(
      onClick = screen.onAddPressed,
      modifier = Modifier
        .align(Alignment.BottomEnd)
        .padding(16.dp)
    ) {
      Text(text = stringResource(id = R.string.add))
    }
  }
}

@Composable
fun TodoListItem(
  text: String,
  onClick: () -> Unit,
  modifier: Modifier = Modifier
) {
  Box(
    modifier = modifier
      .fillMaxWidth()
      .clickable(onClick = onClick)
      .padding(16.dp)
  ) {
    Text(
      text = text,
      style = MaterialTheme.typography.bodyLarge
    )
  }
}
