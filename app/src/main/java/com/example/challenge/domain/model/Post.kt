package com.example.challenge.domain.model

/**
 * Data Class to get the response for the blog posts
 * @property title String
 * @property description String
 * @property author String
 * @property image String
 * @property article_date String
 * @property link String
 * @property uuid String
 * @constructor
 */
data class Post(
    val title: String,
    val description: String,
    val author: String,
    val image: String,
    val article_date: String,
    val link: String,
    val uuid: String
)
