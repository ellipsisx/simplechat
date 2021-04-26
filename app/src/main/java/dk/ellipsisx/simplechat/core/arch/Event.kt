package dk.ellipsisx.simplechat.core.arch

class Event<T>(private val content: T) {
    private var hasBeenHandled = false

    /**
     *
     * @return
     */
    val contentIfNotHandled: T?
        get() {
            return if (hasBeenHandled) {
                null
            } else {
                hasBeenHandled = true
                content
            }
        }

    /**
     * Returns true if event has not been handled and should be handled
     * @return
     */
    fun handleIfNotHandled(): Boolean {
        return if (hasBeenHandled) {
            false
        } else {
            hasBeenHandled = true
            true
        }
    }
}
