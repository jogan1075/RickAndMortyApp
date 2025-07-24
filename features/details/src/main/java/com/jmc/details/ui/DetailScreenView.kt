package com.jmc.details.ui

import android.graphics.drawable.Icon
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SegmentedButtonDefaults.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.jmc.details.data.model.DetailResult
import com.jmc.details.presentation.DetailState
import com.jmc.details.presentation.DetailViewModel

@Composable
fun DetailScreenView(
    viewModel: DetailViewModel = hiltViewModel(),
    onBack: () -> Unit
) {

    when (val state = viewModel.state) {
        is DetailState.Loading -> {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }

        is DetailState.Error -> {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(text = "Error: ${state.message}")
            }
        }

        is DetailState.Success -> {
            val character = state.character
            ContentView(character, onBack)
        }

    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ContentView(character: DetailResult, onBack: () -> Unit) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {},
                modifier = Modifier.fillMaxWidth(),
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.Black,
                            modifier = Modifier.size(36.dp)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White,
                    titleContentColor = Color.Black
                )
            )
        }) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.Start
        ) {

            Card(
                modifier = Modifier.size(400.dp),
                shape = MaterialTheme.shapes.medium
            ) {
                val painter = rememberAsyncImagePainter(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(character.image)
                        .crossfade(true)
                        .error(coil.base.R.drawable.notification_bg)
                        .placeholder(com.jmc.details.R.drawable.ic_launcher_background)
                        .build()
                )
                Image(
                    painter = painter,
                    modifier = Modifier.fillMaxSize(),
                    contentDescription = "Character Image",
                    contentScale = ContentScale.Crop
                )
            }
            Text(
                text = character.name,
                fontSize = 23.sp,
                fontWeight = FontWeight.Black,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                textAlign = TextAlign.Center
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()

            ) {
                val color = when (character.status) {
                    "Alive" -> Color.Green
                    "Dead" -> Color.Red
                    else -> Color.Gray
                }
                Box(
                    modifier = Modifier
                        .size(10.dp)
                        .background(color = color, shape = CircleShape)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(text = character.status)
            }
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 80.dp)
            ) {
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = CenterHorizontally
                ) {
                    Text(text = character.species, fontSize = 18.sp)
                    Text(text = "Species", fontWeight = FontWeight.Light, fontSize = 10.sp)
                }
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = CenterHorizontally
                ) {
                    Text(text = character.gender, fontSize = 18.sp)
                    Text(text = "Gender", fontWeight = FontWeight.Light, fontSize = 10.sp)
                }
            }
            Spacer(modifier = Modifier.height(16.dp))

            Text(text = character.origin.name)
            Text(text = "Origin", fontWeight = FontWeight.Light)
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = character.location.name)
            Text(text = "Last Known Location", fontWeight = FontWeight.Light)


        }
    }

}