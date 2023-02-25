package com.saadi.preferencedatastore

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.saadi.preferencedatastore.databinding.ActivityMainBinding
import com.saadi.preferencedatastore.preferences.PreferencesManager
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    //binding
    private lateinit var mBinding: ActivityMainBinding

    //preferences
    private lateinit var mPreferences : PreferencesManager


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        //init preferences
        mPreferences = PreferencesManager.getInstance(applicationContext)

        //clearing preferences
        lifecycleScope.launch {
            mPreferences.clear()
        }

        mBinding.apply {

            //btn save click listener
            btnSave.setOnClickListener {
                //saving data in preferences
                lifecycleScope.launch {
                    mPreferences.save(etSaveKey.text.toString(),etSaveValue.text.toString())
                }
            }

            //btn read click listener
            btnRead.setOnClickListener {
                val value = mPreferences.getString(etReadkey.text.toString(),"empty")
                lifecycleScope.launch {
                    value.collect {value ->
                        tvReadValue.text = value
                    }
                }
            }
        }


    }
}