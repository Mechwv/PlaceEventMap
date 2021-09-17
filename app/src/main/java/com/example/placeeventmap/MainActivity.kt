package com.example.placeeventmap

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.placeeventmap.DI.RoomModule
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)
    }
}