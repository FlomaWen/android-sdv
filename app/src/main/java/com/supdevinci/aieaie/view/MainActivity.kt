package com.supdevinci.aieaie.view

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.ColorRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.supdevinci.aieaie.model.response.GeneratedAnswer
import com.supdevinci.aieaie.ui.theme.AIEAIETheme
import com.supdevinci.aieaie.viewmodel.OpenAiViewModel
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.window.Dialog
import com.supdevinci.paradisechat.R
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.vectorResource



class MainActivity : ComponentActivity() {
    private val openAiViewModel = OpenAiViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        openAiViewModel.fetchMessages()
        setContent {
            AIEAIETheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = colorResource(R.color.black_theme)
                ) {
                    MessageScreen(openAiViewModel)
                }
            }
        }
    }
}



@Composable
fun MessageScreen(viewModel: OpenAiViewModel) {
    val messagesList by viewModel.openAiResponse.collectAsState()

    val showDialogThemes = remember {
        mutableStateOf(false)
    }

    println("TEST TEST ${messagesList}")
    Column {
        Header(onSettingsClick = { showDialogThemes.value = true })
        Text(text ="Choisissez un sujet",
            style = MaterialTheme.typography.labelLarge.copy(fontSize = 24.sp),
            modifier = Modifier
                .padding(16.dp)
                .align(Alignment.CenterHorizontally),
            color = Color.White
        )
        SectionGrille()
    }
    if (showDialogThemes.value) {
        Dialog(onDismissRequest = { showDialogThemes.value = false }) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
            ) {
                Surface(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .width(IntrinsicSize.Max)
                        .height(IntrinsicSize.Max)
                        .padding(horizontal = 16.dp),
                    color = Color.Black
                ) {
                    Text(
                        text = "Custom your theme :)",
                        color = Color.White,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }
        }
    }
}
@Composable
fun SectionGrille() {
    val items = listOf(
        Pair("Musique", R.drawable.la_musique),
        Pair("Cinéma", R.drawable.cinema),
        Pair("Sport", R.drawable.sports_de_balles),
        Pair("Cuisine", R.drawable.cuisine),
        Pair("Jeux vidéo", R.drawable.jeux_video),
        Pair("Littérature", R.drawable.litterature),
        Pair("Informatique", R.drawable.support_informatique),
        Pair("Histoire", R.drawable.evolution),
        Pair("Science", R.drawable.science),
        Pair("Politique", R.drawable.maire)
    )

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        content = {
            items(items.size) { index ->
                val (sectionName, iconResId) = items[index]
                val icon: ImageVector = ImageVector.vectorResource(iconResId)
                Box(
                    modifier = Modifier
                        .height(150.dp)
                        .padding(20.dp)
                        .background(color = colorResource(R.color.pink_custom_light), shape = RoundedCornerShape(16.dp))
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .padding(vertical = 8.dp)
                            .clickable { /* TODO */ }
                    ) {
                        Image(
                            imageVector = icon,
                            contentDescription = "Icone de la section",
                            modifier = Modifier
                                .size(50.dp)
                        )
                        Text(
                            text = sectionName,
                            style = MaterialTheme.typography.bodySmall.copy(fontSize = 16.sp),
                            modifier = Modifier.padding(top = 12.dp)
                        )
                    }
                }
            }
        }
    )
}


@Composable
fun MessagesItemList(
    messagesList: GeneratedAnswer
) {
    LazyColumn(modifier = Modifier.fillMaxSize()) {
        items(messagesList.choices) { message ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Column(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth()
                ) {
                    Text(
                        text = "${message.message.role}",
                        style = MaterialTheme.typography.headlineMedium
                    )
                    Text(
                        text = "${message.message.content}",
                        style = MaterialTheme.typography.headlineMedium
                    )
                }
            }
        }
    }
}
@Composable
fun Header(onSettingsClick:() -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "ParadiseCHAT",
            style = MaterialTheme.typography.labelLarge.copy(fontSize = 24.sp),
            textAlign = TextAlign.Center,
            modifier = Modifier.weight(1f),
            color = Color.White
        )
        IconButton(
            onClick = onSettingsClick,
            modifier = Modifier.padding(start = 8.dp)
        ) {
            Icon(
                imageVector = Icons.Outlined.Settings,
                contentDescription = "Bouton Theme",
                tint = Color.White
            )
        }
    }
}