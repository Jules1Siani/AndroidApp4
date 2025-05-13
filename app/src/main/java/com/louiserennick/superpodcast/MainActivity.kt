package com.louiserennick.superpodcast

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.viewmodel.compose.viewModel
import com.louiserennick.superpodcast.ui.theme.SuperPodCastTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Set the content of the activity using Jetpack Compose
        setContent {
            // Create or retrieve the ViewModel instance
            val viewModel: PodcastViewModel = viewModel()

            // Perform an initial search (optional, for demo purposes)
            viewModel.search("technology")

            // Apply the app theme and launch the main screen
            SuperPodCastTheme {
                PodcastApp(viewModel)
            }
        }
    }
}


