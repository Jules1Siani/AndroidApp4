package com.louiserennick.superpodcast

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.louiserennick.superpodcast.model.PodCast
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class PodcastViewModel : ViewModel() {

    // Mutable state holding the list of podcasts
    private val _filteredPodcasts = MutableStateFlow<List<PodCast>>(emptyList())

    // Public read-only access to podcast list
    val filteredPodcasts: StateFlow<List<PodCast>> = _filteredPodcasts

    // Retrofit instance for accessing the iTunes Search API
    private val retrofit = Retrofit.Builder()
        .baseUrl("https://itunes.apple.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    // API client implementation
    private val api = retrofit.create(PodcastApiService::class.java)

    // Function to search for podcasts based on user input
    fun search(term: String) {
        viewModelScope.launch {
            try {
                // Call the iTunes API and get results
                val response = api.searchPodcasts(term)

                // Map API result into our internal PodCast model
                val podcasts = response.results.map {
                    PodCast(
                        id = it.collectionId.toString(),
                        title = it.collectionName,               // No Elvis operator needed
                        author = it.artistName,
                        imageUrl = it.artworkUrl100,
                        description = "",                         // Optional for now
                        feedUrl = it.feedUrl,
                        podcastUrl = it.collectionViewUrl ?: ""   // This one might be null
                    )
                }.filter {
                    // Example: only include podcasts with more than 2 words in title
                    it.title.split(" ").size > 2
                }

                _filteredPodcasts.value = podcasts

            } catch (e: Exception) {
                e.printStackTrace()
                _filteredPodcasts.value = emptyList() // In case of error
            }
        }
    }

    // Optionally used to store selected podcast (e.g., for details screen later)
    fun selectPodcast(podcast: PodCast) {
        // Not yet implemented
    }
}
