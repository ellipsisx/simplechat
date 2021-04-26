package dk.ellipsisx.simplechat.data.model

import java.io.Serializable
import java.util.*

data class Message(
    val id: String,
    val text: String,
    val createdDateTime: Date,
    val fromName: String
) : Serializable {
    companion object {
        const val messages = "messages"
        const val text = "text"
        const val createdDateTime = "createdDateTime"
        const val fromName = "fromName"
    }
}