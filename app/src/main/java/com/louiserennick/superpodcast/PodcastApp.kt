package com.louiserennick.superpodcast

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.louiserennick.superpodcast.model.PodCast
import androidx.compose.runtime.collectAsState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PodcastApp(viewModel: PodcastViewModel) {
    val podcasts by viewModel.filteredPodcasts.collectAsState()
    var searchTerm by remember { mutableStateOf("technology") }
    val isEmpty = podcasts.isEmpty()
    val context = LocalContext.current

    // Holds list of favorite podcast IDs
    val favoriteIds = remember { mutableStateListOf<String>() }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("SuperPodcast") })
        }
    ) { paddingValues ->
        Column(modifier = Modifier
            .padding(paddingValues)
            .padding(8.dp)) {

            OutlinedTextField(
                value = searchTerm,
                onValueChange = { searchTerm = it },
                label = { Text("Search podcasts") },
                modifier = Modifier.fillMaxWidth()
            )

            Button(
                onClick = { viewModel.search(searchTerm) },
                modifier = Modifier.padding(vertical = 8.dp)
            ) {
                Text("Search")
            }

            if (isEmpty) {
                Text(
                    text = "No podcasts found. Try a different search.",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(top = 16.dp)
                )
            } else {
                LazyColumn {
                    items(podcasts) { podcast ->
                        PodcastItem(
                            podcast = podcast,
                            isFavorite = favoriteIds.contains(podcast.id),
                            onToggleFavorite = {
                                if (favoriteIds.contains(podcast.id)) {
                                    favoriteIds.remove(podcast.id)
                                } else {
                                    favoriteIds.add(podcast.id)
                                }
                            },
                            onClick = {
                                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(podcast.podcastUrl))
                                context.startActivity(intent)
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun PodcastItem(
    podcast: PodCast,
    isFavorite: Boolean,
    onToggleFavorite: () -> Unit,
    onClick: (PodCast) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick(podcast) }
            .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row {
            Image(
                painter = rememberAsyncImagePainter(podcast.imageUrl),
                contentDescription = podcast.title,
                modifier = Modifier.size(64.dp),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.width(8.dp))
            Column {
                Text(text = podcast.title, style = MaterialTheme.typography.titleMedium)
                Text(text = podcast.author, style = MaterialTheme.typography.bodySmall)
            }
        }

        // Star icon toggle
        IconButton(onClick = onToggleFavorite) {
            Text(if (isFavorite) "★" else "☆", style = MaterialTheme.typography.titleMedium)
        }
    }
}
