package com.supdevinci.aieaie.view

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.runtime.MutableState
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
import androidx.compose.runtime.setValue
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.window.Dialog
import com.supdevinci.paradisechat.R
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.ViewModel
import kotlin.random.Random

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val currentTheme = remember { mutableStateOf(AppThemes.theme4) }
            AIEAIETheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = colorResource(currentTheme.value.backgroundColor)
                ) {
                    MessageScreen(currentTheme)
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun Preview() {

    val previewTheme = ThemeColors(
        primaryColor = R.color.blue_theme,
        secondaryColor = R.color.blue_theme_light,
        backgroundColor = R.color.blue_theme_lighter
    )

    val previewThemeState = remember { mutableStateOf(previewTheme) }

    AIEAIETheme {
        MessageScreen(previewThemeState)
    }
}


//PAGE ENTIERE
@SuppressLint("UnrememberedMutableState")
@Composable
fun MessageScreen(currentTheme: MutableState<ThemeColors>) {

    val showDialogThemes = remember {
        mutableStateOf(false)
    }

    Column {
        Header(onSettingsClick = { showDialogThemes.value = true })
        Text(text ="Choisissez un sujet",
            style = MaterialTheme.typography    .labelLarge.copy(fontSize = 24.sp),
            modifier = Modifier
                .padding(16.dp)
                .align(Alignment.CenterHorizontally),
            color = Color.White
        )
        SectionGrille(currentTheme)
    }
    if (showDialogThemes.value) {
        Dialog(onDismissRequest = { showDialogThemes.value = false }) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Surface(
                    modifier = Modifier
                        .wrapContentSize(),
                    color = MaterialTheme.colorScheme.surface,
                    shape = RoundedCornerShape(12.dp)
                ) {
                    ThemePicker(currentTheme) { showDialogThemes.value = false }
                }
            }
        }
    }
}

//SECTION GRILLE
@Composable
fun SectionGrille(currentTheme: MutableState<ThemeColors>) {
    val context = LocalContext.current

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
                val brush = Brush.verticalGradient(colors = listOf(colorResource(currentTheme.value.primaryColor), Color.White))
                Box(
                    modifier = Modifier
                        .height(150.dp)
                        .padding(20.dp)
                        .background(
                            brush = brush,
                            shape = RoundedCornerShape(16.dp)
                        )
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(vertical = 8.dp)
                            .clickable {
                                val intent = Intent(context, ChatActivity::class.java).apply {
                                    putExtra("subject", sectionName)
                                    putExtra("theme", createThemeBundle(currentTheme.value))
                                }
                                context.startActivity(intent)
                            }
                    ) {
                            Image(
                                imageVector = icon,
                                contentDescription = "Icone de la section",
                                modifier = Modifier
                                    .size(50.dp)
                            )

                            Text(
                                text = sectionName,
                                style = MaterialTheme.typography.bodySmall.copy(fontSize = 16.sp, fontWeight = FontWeight.Bold),
                                modifier = Modifier.padding(top = 12.dp)
                            )


                    }
                }
            }
        }
    )
}

//HEADER
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

//GESTION DES THEMES
@Composable
fun ThemePicker(currentTheme: MutableState<ThemeColors>, onThemeSelected: () -> Unit) {
    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Card(
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
                .padding(bottom = 16.dp)
        ) {
            Text(
                text = "Choisis ton thème",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(16.dp),
            )
        }
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                ThemeSelectionBox(
                    color = colorResource(R.color.purple_custom),
                    onColorSelected = { currentTheme.value = AppThemes.theme4; onThemeSelected() }
                )
                ThemeSelectionBox(
                    color = colorResource(R.color.red_theme),
                    onColorSelected = { currentTheme.value = AppThemes.theme2; onThemeSelected() }
                )
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
            ) {
                ThemeSelectionBox(
                    color = colorResource(R.color.blue_theme),
                    onColorSelected = { currentTheme.value = AppThemes.theme1; onThemeSelected() }
                )
                ThemeSelectionBox(
                    color = colorResource(R.color.green_theme),
                    onColorSelected = { currentTheme.value = AppThemes.theme3; onThemeSelected() }
                )
            }
        }
    }
}

@Composable
fun ThemeSelectionBox(color: Color, onColorSelected: () -> Unit) {
    Box(
        modifier = Modifier
            .size(50.dp)
            .padding(8.dp)
            .background(color = color, shape = CircleShape)
            .clickable { onColorSelected() }
    )
}

// CLASSE POUR THEMES
data class ThemeColors(
    val primaryColor: Int,
    val secondaryColor: Int,
    val backgroundColor: Int
)

// OBJET

object AppThemes {
    val theme1 = ThemeColors(
        primaryColor = R.color.blue_theme,
        secondaryColor = R.color.blue_theme_light,
        backgroundColor =R.color.blue_theme_darker_dark
    )
    val theme2 = ThemeColors(
        primaryColor = R.color.red_theme,
        secondaryColor = R.color.red_theme,
        backgroundColor = R.color.blue_theme_darker_dark
    )

    val theme3 = ThemeColors(
        primaryColor = R.color.green_theme,
        secondaryColor = R.color.green_theme_light,
        backgroundColor = R.color.blue_theme_darker_dark
    )

    val theme4 = ThemeColors(
        primaryColor =  R.color.purple_custom,
        secondaryColor = R.color.pink_custom,
        backgroundColor = R.color.blue_theme_darker_dark
    )
}
fun createThemeBundle(theme: ThemeColors): Bundle {
    return Bundle().apply {
        putInt("primaryColor", theme.primaryColor)
        putInt("secondaryColor", theme.secondaryColor)
        putInt("backgroundColor", theme.backgroundColor)
    }
}