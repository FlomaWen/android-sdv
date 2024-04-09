package com.supdevinci.aieaie.view

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import com.supdevinci.aieaie.model.OpenAiMessageBody
import com.supdevinci.aieaie.ui.theme.AIEAIETheme
import com.supdevinci.aieaie.viewmodel.OpenAiViewModel

class ChatActivity : ComponentActivity() {
    private lateinit var subject: String
    private lateinit var openAiViewModel: OpenAiViewModel
    private val messagesList: MutableList<OpenAiMessageBody> = mutableStateListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        subject = intent.getStringExtra("subject") ?: "Musique"

        openAiViewModel = OpenAiViewModel(subject)


        val themeBundle = intent.getBundleExtra("theme")
        val currentTheme = if (themeBundle != null) {
            extractThemeFromBundle(themeBundle)
        } else {
            AppThemes.theme4
        }
        setContent {
            AIEAIETheme {
                var isMenuVisible by remember { mutableStateOf(false) }
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = colorResource(currentTheme.backgroundColor)

                ) {
                    MessageScreen(messagesList, openAiViewModel,currentTheme,onSettingsClicked = {
                        isMenuVisible = true
                    })
                    SubmitForm(messagesList, openAiViewModel,currentTheme)

                    if (isMenuVisible) {
                        ConversationMenu(onDismiss = { isMenuVisible = false }, openAiViewModel, currentTheme)
                    }
                }
            }
        }

        observeOpenAiResponse()
        openAiViewModel.fetchMessages()
    }

    private fun observeOpenAiResponse() {
        lifecycleScope.launchWhenStarted {
            openAiViewModel.openAiResponse.collect { response ->
                response?.choices?.forEach { choice ->
                    messagesList.add(choice.message)
                }
            }
        }
    }
}

@Composable
fun ConversationMenu(onDismiss: () -> Unit, openAiViewModel: OpenAiViewModel, currentTheme: ThemeColors) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Conversations") },
        text = {
            Text("Conversation 1\nConversation 2")
        },
        confirmButton = {
            Button(onClick = onDismiss) {
                Text("Fermer")
            }
        }
    )
}
@Composable
fun MessageScreen(messagesList: MutableList<OpenAiMessageBody>, viewModel: OpenAiViewModel, currentTheme: ThemeColors,onSettingsClicked: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.End
        ) {
            IconButton(onClick = onSettingsClicked) {
                Icon(
                    imageVector = Icons.Default.Settings,
                    contentDescription = "Réglages",
                    tint = colorResource(currentTheme.primaryColor)
                )
            }
        }
        if (messagesList.isEmpty()) {
            Text(
                text = "Chargement ...",
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                color = colorResource(currentTheme.primaryColor)
            )
        } else {
            MessagesItemList(messagesList = messagesList , currentTheme = currentTheme)
        }
    }
}

@Composable
fun MessagesItemList(
    messagesList: MutableList<OpenAiMessageBody>, currentTheme: ThemeColors
) {
    LazyColumn(
        reverseLayout = false,
        modifier = Modifier.fillMaxSize()
    ) {
        items(messagesList) { message ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = if (message.role == "user") Arrangement.End else Arrangement.Start
            ) {
                Card(
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier
                        .wrapContentWidth(
                            align = if (message.role == "user") Alignment.End else Alignment.Start
                        )
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        if (message.role != "user") {
                            Text(
                                text = "ParadiseCHAT",
                                style = MaterialTheme.typography.bodySmall,
                                color = colorResource(id = currentTheme.primaryColor)
                            )
                        } else {
                            Text(
                                text = "Vous",
                                style = MaterialTheme.typography.bodySmall,
                                color = colorResource(id = currentTheme.primaryColor)
                            )
                        }
                        Text(
                            text = message.content,
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.Black
                        )
                    }
                }
            }
        }
    }
}


@Composable
fun SubmitForm(messagesList: MutableList<OpenAiMessageBody>, openAiViewModel: OpenAiViewModel,currentTheme:ThemeColors
) {
    var textState by remember { mutableStateOf("") }
    var showError by remember { mutableStateOf(false) }

    if (showError) {
        Text(
            text = "Veuillez entrer un message.",
            color = Color.Red,
            textAlign = TextAlign.Center
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 16.dp)
    ) {

        Spacer(modifier = Modifier.weight(1f))

        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            BasicTextField(
                value = textState,
                onValueChange = { textState = it },
                modifier = Modifier
                    .weight(2f)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color.White)
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                textStyle = TextStyle(color = Color.Black)
            )

            Spacer(modifier = Modifier.width(8.dp))
            Button(
                onClick = {
                    if (textState.isBlank()) {
                        showError = true
                    } else {
                        val userMessage = OpenAiMessageBody("user", textState)
                        messagesList.add(userMessage)
                        openAiViewModel.addMessage(userMessage)
                        openAiViewModel.fetchMessages()
                        textState = ""
                        showError = false
                    }
                },
                colors = ButtonDefaults.buttonColors(colorResource(currentTheme.primaryColor)),
                modifier = Modifier.padding(start = 8.dp)
            ) {
                Text("Envoyer")
            }
        }
    }
}

fun extractThemeFromBundle(bundle: Bundle): ThemeColors {
    val primaryColor = bundle.getInt("primaryColor")
    val secondaryColor = bundle.getInt("secondaryColor")
    val backgroundColor = bundle.getInt("backgroundColor")
    return ThemeColors(primaryColor, secondaryColor, backgroundColor)
}