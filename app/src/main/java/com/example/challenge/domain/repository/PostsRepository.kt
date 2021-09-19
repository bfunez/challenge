package com.example.challenge.domain.repository

import com.example.challenge.domain.model.Post
import com.example.challenge.utils.Resource

/**
 * Definition of the Post repository
 */
interface PostsRepository {
    suspend fun getPosts() : Resource<List<Post>>
}