package tk.kvakva.myfilepickerapplication

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.MenuProvider
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.lifecycleScope
import androidx.preference.PreferenceDataStore
import androidx.preference.PreferenceFragmentCompat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "my_super_settings")
private const val TAG = "SettingsActivity"
class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.settings_activity)



        if (savedInstanceState == null) {
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.settings, SettingsFragment())
                .commit()
        }
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {

            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    android.R.id.home -> {
                        finish()
                        true
                    }
                    else -> false
                }
            }
        })

    }

    class SettingsFragment : PreferenceFragmentCompat() {

        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            preferenceManager.setPreferenceDataStore(object : PreferenceDataStore() {
                /**
                 * Sets a [String] value to the data store.
                 *
                 *
                 * Once the value is set the data store is responsible for holding it.
                 *
                 * @param key   The name of the preference to modify
                 * @param value The new value for the preference
                 * @see .getString
                 */
                override fun putString(key: String?, value: String?) {
                    if(key!=null&&value!=null) {
                        lifecycleScope.launch(Dispatchers.IO) {
                            context?.dataStore?.edit { pref ->
                                pref[stringPreferencesKey(key)] = value
                            }
                        }
                    }
                    //super.putString(key, value)
                }

                /**
                 * Retrieves a [String] value from the data store.
                 *
                 * @param key      The name of the preference to retrieve
                 * @param defValue Value to return if this preference does not exist in the storage
                 * @return The value from the data store or the default return value
                 * @see .putString
                 */
                override fun getString(key: String?, defValue: String?): String? {
                    var s: String? = null
                    if(key!=null) {
                        runBlocking {
                            s=context?.dataStore?.data?.first()?.get(stringPreferencesKey(key))
                                ?: defValue
                        }
                    }
                    Log.v(TAG,"PreferenceManager getString($key,$defValue)=$s")
                    return s
                }

                /**
                 * Retrieves a [Boolean] value from the data store.
                 *
                 * @param key      The name of the preference to retrieve
                 * @param defValue Value to return if this preference does not exist in the storage
                 * @return the value from the data store or the default return value
                 * @see .getBoolean
                 */
                override fun getBoolean(key: String?, defValue: Boolean): Boolean {
                    var b=false
                    if(key!=null) {
                        runBlocking {
                            b=context?.dataStore?.data?.first()?.get(booleanPreferencesKey(key))
                                ?: defValue
                        }
                    }
                    return b
                }

                /**
                 * Sets a [Boolean] value to the data store.
                 *
                 *
                 * Once the value is set the data store is responsible for holding it.
                 *
                 * @param key   The name of the preference to modify
                 * @param value The new value for the preference
                 * @see .getBoolean
                 */
                override fun putBoolean(key: String?, value: Boolean) {
                    if(key!=null) {
                        lifecycleScope.launch(Dispatchers.IO) {
                            context?.dataStore?.edit { pref ->
                                pref[booleanPreferencesKey(key)] = value
                            }
                        }
                    }
                    //super.putBoolean(key, value)
                }
            })



            setPreferencesFromResource(R.xml.root_preferences, rootKey)
        }
    }
}


