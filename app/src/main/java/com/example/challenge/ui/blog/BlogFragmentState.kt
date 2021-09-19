package com.example.challenge.ui.blog

/**
 * Handle fragment actions for [BlogFragment]
 */
sealed class BlogFragmentState {
    data class OpenDetailView(val url : String) : BlogFragmentState()
}