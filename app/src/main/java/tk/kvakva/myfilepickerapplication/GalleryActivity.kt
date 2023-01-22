package tk.kvakva.myfilepickerapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import androidx.core.view.MenuProvider
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import coil.load
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import tk.kvakva.myfilepickerapplication.datalevel.Repository

val urlsListLiveData = MutableLiveData(
    listOf(
        "qwe", "asd", "zxc",
        "qwe", "asd", "zxc",
        "qwe", "asd", "zxc"
    )
)
private const val TAG = "GalleryActivity"

class GalleryActivity : AppCompatActivity() {

    lateinit var hidePictureMenuItem: MenuItem
    val fullPictureIv by lazy { findViewById<ImageView>(R.id.full_picture_view) }

    val recView by lazy {
        findViewById<RecyclerView>(R.id.rec_view)
    }
    lateinit var recAdapter: RecViewAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gallery)

        addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                hidePictureMenuItem = menu.add(0, 5, 0, "hide full picture").apply {
                    setIcon(android.R.drawable.ic_menu_close_clear_cancel)
                    isVisible = false
                    setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM)
                }
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return if (menuItem == hidePictureMenuItem) {
                    fullPictureIv.visibility = View.INVISIBLE
                    hidePictureMenuItem.isVisible = false
                    true
                } else {
                    false
                }
            }
        })
        fullPictureIv.setOnClickListener {
            fullPictureIv.visibility = View.INVISIBLE
            hidePictureMenuItem.isVisible = false
        }
        recAdapter = RecViewAdapter(::showFullPicture)
        //recView.layoutManager=LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false)
        recView.layoutManager = GridLayoutManager(this, 4)
        recView.adapter = recAdapter
        urlsListLiveData.observe(this) {
            recAdapter.data = it
            Log.v(TAG, "$it")
        }
        updateList()
    }

    fun updateList() {
        lifecycleScope.launch(Dispatchers.IO) {
            urlsListLiveData.postValue(Repository().gUrlsList())
        }
    }

    fun showFullPicture(p: Int) {
        val u = recAdapter.data[p]

        Log.v(TAG, "showFullPicture($p) u=$u")

        fullPictureIv.apply {
            hidePictureMenuItem.isVisible = true
            visibility = View.VISIBLE
            load(u)
        }
    }
}