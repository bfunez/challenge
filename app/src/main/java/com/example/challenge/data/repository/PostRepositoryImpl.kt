package com.example.challenge.data.repository

import com.example.challenge.domain.model.Post
import com.example.challenge.domain.repository.PostsRepository
import com.example.challenge.utils.NetworkClient
import com.example.challenge.utils.Resource
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException

/**
 * Implementation of the [PostsRepository]
 * @property networkClient NetworkClient
 */
class PostRepositoryImpl : PostsRepository {
    private val networkClient = NetworkClient()
    override suspend fun getPosts(): Resource<List<Post>> {
        val url = "https://www.beenverified.com/articles/index.mobile-android.json"
        try {
            val request = networkClient.request(url)
            val articlesList = mutableListOf<Post>()

            val jsonObject = JSONObject(request)
            val jsonArrayArticles = jsonObject.getJSONArray("articles")
            (0 until jsonArrayArticles.length()).forEach {
                val jsonObjectPost = jsonArrayArticles.getJSONObject(it)
                articlesList.add(Post(
                    title = jsonObjectPost.getString("title"),
                    description = jsonObjectPost.getString("description"),
                    author = jsonObjectPost.getString("author"),
                    image = jsonObjectPost.getString("image"),
                    article_date = jsonObjectPost.getString("article_date"),
                    link = jsonObjectPost.getString("link").replace("http", "https"),
                    uuid = jsonObjectPost.getString("uuid")
                ))

            }
            return Resource.Success(articlesList.toList())
        }catch (exception : IOException){
            exception.printStackTrace()
            return Resource.Failure(exception.localizedMessage ?: "")
        }
        catch (exception : JSONException){
            exception.printStackTrace()
            return Resource.Failure(exception.localizedMessage ?: "")
        }
    }
}