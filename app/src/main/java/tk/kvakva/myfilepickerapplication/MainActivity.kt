package tk.kvakva.myfilepickerapplication

import android.content.Intent
import android.content.res.ColorStateList
import android.content.res.Resources
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat.ThemeCompat
import androidx.core.view.MenuProvider
import java.io.BufferedReader
import java.io.InputStream
import java.nio.charset.Charset

private const val TAG = "MainActivity"
private const val idMenuReadFile = 2
private const val idMenuGoToRecView = 3
private const val idMenuGoToComposeActivity = 4
class MainActivity : AppCompatActivity() {
    val getContent = registerForActivityResult(ActivityResultContracts.GetContent()){uri: Uri? ->
        Log.v(TAG,"$uri")
        Log.v(TAG,"${uri?.path}")
        findViewById<TextView>(R.id.txt_view).text=uri?.let{ parseTextFromUri(uri)}?:"No Uri!!!!!"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        addMenuProvider(object : MenuProvider{
            /**
             * Called by the [MenuHost] to allow the [MenuProvider]
             * to inflate [MenuItem]s into the menu.
             *
             * @param menu         the menu to inflate the new menu items into
             * @param menuInflater the inflater to be used to inflate the updated menu
             */
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.main_act_menu,menu)
//                menu.add(0, idMenuReadFile,0,"read file").apply {
//                    this.setIcon(android.R.drawable.ic_menu_view)
//                    this.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM)
//                    Log.v(TAG,"idMenuReadFile itemId=$itemId")
//                }
//                menu.add(0, idMenuGoToRecView,0,"go to rec view").apply {
//                    //this.setIcon(android.R.drawable.ic_menu_gallery)
//                    this.setIcon(R.drawable.baseline_grid_4x4_24)
//                    this.setIconTintList(ColorStateList.valueOf(resources.getColor(R.color.white,this@MainActivity.theme)))
//                    this.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM)
//                    Log.v(TAG,"idMenuGoToRecView itemId=$itemId")
//                }
//                menu.add(0, idMenuGoToComposeActivity,0,"go to compose activity").apply {
//                    //this.setIcon(android.R.drawable.ic_menu_gallery)
//                    //this.setIcon(R.drawable.baseline_grid_view_24)
//                    this.setIcon(android.R.drawable.ic_menu_preferences)
//                    this.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM)
//                    Log.v(TAG,"idMenuGoToRecView itemId=$itemId")
//                }
            }

            /**
             * Called by the [MenuHost] when a [MenuItem] is selected from the menu.
             *
             * @param menuItem the menu item that was selected
             * @return `true` if the given menu item is handled by this menu provider,
             * `false` otherwise
             */
            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when(menuItem.itemId){
                    R.id.pick_and_read_text_file_id -> {
                        Log.v(TAG,"idMenuReadFile clicked!")
                        getContent.launch("*/*")
                        true
                    }
                    R.id.lorem_picsum_id -> {
                        Log.v(TAG,"idMenuGoToRecView clicked!")
                        val intent = Intent(this@MainActivity.applicationContext,GalleryActivity::class.java)
                        startActivity(intent)
                        true
                    }
                    R.id.settings_activity_id -> {
                        Log.v(TAG,"idMenuGoToComposeActivity clicked!")
                        val intent = Intent(this@MainActivity.applicationContext,SettingsActivity::class.java)
                        startActivity(intent)
                        true
                    }

                    else -> false
                }
            }

        })
    }
    private fun parseTextFromUri(u: Uri): String {
        var s: String?=null
        contentResolver.openInputStream(u)?.use {inputStream: InputStream ->
            //inputStream.bufferedReader(Charset.forName("WINDOWS-1251")).use { bufferedReader: BufferedReader ->
            inputStream.bufferedReader().use { bufferedReader: BufferedReader ->
                s=bufferedReader.readText()
            }
        }

        //var inputStream: InputStream? = u?.let { applicationContext.contentResolver.openInputStream(it) }
//        val inputStream: InputStream? = u.let { applicationContext.contentResolver.openInputStream(it) }
//        val reader = BufferedReader(inputStream?.reader(Charset.forName("WINDOWS-1251")))
//        val content = StringBuilder()
//        try {
//            var line = reader.readLine()
//            while (line != null) {
//                content.append(line)
//                line = reader.readLine()
//            }
//        } finally {
//            reader.close()
//        }

        return s?:"SomeThing Wrong"
        //return content.toString()
    }

}
