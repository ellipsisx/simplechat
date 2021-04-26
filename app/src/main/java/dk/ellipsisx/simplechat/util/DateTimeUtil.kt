package dk.ellipsisx.simplechat.util

import android.text.format.DateFormat
import dk.ellipsisx.simplechat.App
import java.util.*

object DateTimeUtil {

    //region Formatting
    //************************************************************

    fun formatDateTime(date: Date): String {
        val datePart = DateFormat.getMediumDateFormat(App.appContext).format(date)
        val timePart = DateFormat.getTimeFormat(App.appContext).format(date)
        return "$datePart, $timePart"
    }

    //endregion

}
