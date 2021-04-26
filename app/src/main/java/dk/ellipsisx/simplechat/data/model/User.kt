package dk.ellipsisx.simplechat.data.model

import java.io.Serializable
import java.util.*

data class User(
    val id: String,
    val name: String,
    val createdDateTime: Date,
) : Serializable {
    companion object {
        const val users = "users"
        const val name = "name"
        const val createdDateTime = "createdDateTime"
    }
}