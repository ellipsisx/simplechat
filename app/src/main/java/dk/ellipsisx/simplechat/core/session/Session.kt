package dk.ellipsisx.simplechat.core.session

import dk.ellipsisx.simplechat.data.model.User

class Session() {
    var user: User? = null
        private set


    fun userLoggedIn(user: User) {
        this.user = user
    }

    fun isLoggedIn(): Boolean {
        return user != null
    }

    fun logout() {
        this.user = null
    }

}