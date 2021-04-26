package dk.ellipsisx.simplechat.ui.conversation

import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import dk.ellipsisx.simplechat.R
import dk.ellipsisx.simplechat.data.model.Message
import dk.ellipsisx.simplechat.data.model.User
import dk.ellipsisx.simplechat.util.DateTimeUtil
import dk.ellipsisx.simplechat.util.UnitConvertUtil
import kotlinx.android.synthetic.main.item_message_received.view.*

class ConversationAdapter(private val currentUser: User) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var data: List<Message> = emptyList()
    private var onItemClickListener: OnItemClickListener? = null

    class ItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val rowLayout: View = view.root_layout
        private val messageCardView: MaterialCardView = view.message_cardview
        private val messageTextView: TextView = view.message_textview
        private val userNameTextView: TextView = view.username_textview
        private val timestampTextView: TextView = view.timestamp_textview

        fun bind(item: Message, currentUser: User, onItemClickListener: OnItemClickListener?) {
            messageTextView.text = item.text
            userNameTextView.text = item.fromName
            timestampTextView.text = DateTimeUtil.formatDateTime(item.createdDateTime)

            val cardViewLayoutParams = messageCardView.layoutParams as ConstraintLayout.LayoutParams
            val largeMargin = UnitConvertUtil.convertDpToPixel(80f).toInt()
            val smallMargin = messageCardView.resources.getDimension(R.dimen.activity_horizontal_margin).toInt()
            if (item.fromName == currentUser.name) {
                cardViewLayoutParams.setMargins(largeMargin, 0, smallMargin, 0)
                userNameTextView.gravity = Gravity.END
                timestampTextView.gravity = Gravity.END
            } else {
                cardViewLayoutParams.setMargins(smallMargin, 0, largeMargin, 0)
                userNameTextView.gravity = Gravity.START
                timestampTextView.gravity = Gravity.START
            }
            messageCardView.layoutParams = cardViewLayoutParams

            onItemClickListener?.let {
                rowLayout.setOnClickListener {
                    onItemClickListener.onMessageClicked(item)
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ItemViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_message_received, parent, false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = data.get(position)
        (holder as? ItemViewHolder)?.bind(item, currentUser, onItemClickListener)
    }

    fun setData(data: List<Message>) {
        this.data = data
        notifyDataSetChanged()
    }

    //region OnItemClickListener
    //************************************************************

    interface OnItemClickListener {
        fun onMessageClicked(message: Message)
    }

    fun setOnItemClickListener(onItemClickListener: OnItemClickListener) {
        this.onItemClickListener = onItemClickListener
    }

    //endregion
}