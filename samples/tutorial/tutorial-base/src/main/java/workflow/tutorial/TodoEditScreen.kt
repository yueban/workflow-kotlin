package workflow.tutorial

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.squareup.workflow1.ui.TextController
import com.squareup.workflow1.ui.compose.ComposeScreen
import com.squareup.workflow1.ui.compose.asMutableTextFieldValueState
import workflow.tutorial.views.R

data class TodoEditScreen(
  val title: TextController,
  val note: TextController,
  val onBackPressed: () -> Unit,
  val onSavePressed: () -> Unit
) : ComposeScreen {

  @Composable
  override fun Content() {
    TodoEditScreenContent(this)
  }
}

@Composable
private fun TodoEditScreenContent(
  screen: TodoEditScreen,
  modifier: Modifier = Modifier,
) {
  BackHandler(onBack = screen.onBackPressed)
  var title by screen.title.asMutableTextFieldValueState()
  var note by screen.note.asMutableTextFieldValueState()

  Column(modifier = modifier.fillMaxSize()) {
    Row(modifier = Modifier.fillMaxWidth()) {
      TextField(
        value = title,
        onValueChange = { title = it },
        modifier = Modifier
          .weight(1f)
          .alignByBaseline(),
        placeholder = { Text(stringResource(R.string.todo_title_hint)) },
        singleLine = true
      )

      Button(
        onClick = screen.onSavePressed,
        modifier = Modifier.alignByBaseline()
      ) {
        Text(stringResource(R.string.save))
      }
    }

    TextField(
      value = note,
      onValueChange = { note = it },
      modifier = Modifier
        .fillMaxWidth()
        .weight(1f),
      placeholder = { Text(stringResource(R.string.todo_note_hint)) }
    )
  }
}
