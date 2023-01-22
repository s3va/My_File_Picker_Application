package tk.kvakva.myfilepickerapplication

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.TransitionListenerAdapter
import coil.ImageLoader
import coil.load
import coil.request.ImageRequest
import coil.transition.CrossfadeTransition
import coil.transition.Transition
import kotlinx.coroutines.Dispatchers
import kotlin.math.min


class RecViewAdapter(val openPicture: (Int) -> Unit) :
    RecyclerView.Adapter<RecViewAdapter.MyViewHolder>() {
    var data = listOf<String>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun getItemCount(): Int = data.size
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(data[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder.from(parent, viewType, openPicture)
    }

    class MyViewHolder private constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.pic_image_view)

        fun bind(d: String) {
            //fun bind(item: String) {
//            imageView.setImageDrawable(
//                ContextCompat.getDrawable(
//                    itemView.context,
//                    android.R.drawable.ic_menu_gallery
//                )
//            )
            val secondIndex = d.lastIndexOf('/')
            if (secondIndex > 8) {
                val firstIndex = d.lastIndexOf('/', secondIndex - 1)
                if (firstIndex > 8) {
                    val w = d.substring(min(firstIndex + 1, d.length), min(d.length, secondIndex))
                    val h = d.substring(min(secondIndex + 1, d.length - 1), d.length)
                    val nh = 320 * (h.toIntOrNull() ?: 1) / (w.toIntOrNull() ?: 1)

                    //val u = d.replaceRange(firstIndex, d.length, "/300/200")
                    val u = d.replaceRange(firstIndex, d.length, "/320/$nh")
                    Log.v(TAG, "u = $u")
                    ImageLoader(itemView.context).enqueue(
                        ImageRequest.Builder(imageView.context)
                            .data(u)
                            //.data(d)
                            .placeholder(android.R.drawable.ic_menu_gallery)
                            .error(android.R.drawable.stat_notify_error)
                            .target(imageView)
                            .build()
                    )
                    //imageView.load(u)
                    return
                }
            }
            imageView.setImageDrawable(
                ContextCompat.getDrawable(
                    itemView.context,
                    R.drawable.baseline_broken_image_24
                )
            )
        }

        companion object {
            fun from(parent: ViewGroup, viewType: Int, openPicture: (Int) -> Unit): MyViewHolder {
                val view =
                    LayoutInflater.from(parent.context).inflate(R.layout.pic_layout, parent, false)
                val viewHolder = MyViewHolder(view)
                view.setOnClickListener {
                    val position = viewHolder.adapterPosition
                    Log.v(TAG, "posision $position clicked")
                    if (position != RecyclerView.NO_POSITION) {
                        openPicture(position)
                    }
                }
                return viewHolder
            }
        }
    }
}

private const val TAG = "RecViewAdapter"