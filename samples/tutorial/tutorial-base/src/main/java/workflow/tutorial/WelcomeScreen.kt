package workflow.tutorial

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.squareup.workflow1.ui.compose.ComposeScreen
import workflow.tutorial.views.R

data class WelcomeScreen(
  val promptText: String,
  val onLogInTapped: (String) -> Unit
) : ComposeScreen {

  @Composable
  override fun Content() {
    WelcomeScreenContent(this)
  }
}

@Composable
private fun WelcomeScreenContent(
  screen: WelcomeScreen,
  modifier: Modifier = Modifier,
) {
  var username by remember { mutableStateOf("") }
  val margin = 16.dp

  Column(
    modifier = modifier
      .fillMaxSize()
      .padding(horizontal = margin),
    verticalArrangement = Arrangement.Center,
    horizontalAlignment = Alignment.CenterHorizontally
  ) {

    Text(
      text = stringResource(R.string.welcome),
      style = MaterialTheme.typography.bodyMedium
    )

    Text(
      text = screen.promptText,
      style = MaterialTheme.typography.bodyLarge,
      fontStyle = FontStyle.Italic,
      modifier = Modifier.padding(top = margin)
    )

    Spacer(modifier = Modifier.height(margin))

    OutlinedTextField(
      value = username,
      onValueChange = { username = it },
      label = { Text(stringResource(R.string.log_in_hint)) },
      modifier = Modifier.fillMaxWidth(),
      textStyle = LocalTextStyle.current.copy(textAlign = TextAlign.Center),
      singleLine = true
    )

    Spacer(modifier = Modifier.height(margin))

    Button(
      onClick = { screen.onLogInTapped(username) },
      modifier = Modifier.fillMaxWidth()
    ) {
      Text(text = stringResource(R.string.log_in))
    }
  }
}
