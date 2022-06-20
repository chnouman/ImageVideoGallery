package com.chnouman.mycustomgalleryapp

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.NavHostFragment
import com.chnouman.mycustomgalleryapp.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    private var continueAfterPermission = 0
    private val myPermissionRequestStoragePermissions = 10
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding =
            DataBindingUtil.setContentView(this, R.layout.activity_main)


        if (isStoragePermissionGranted()) {
            setupNavGraph()
        } else {
            continueAfterPermission = 1
            requestStoragePermission()
        }
    }

    private fun setupNavGraph() {
        //Setup the navGraph for this activity
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val inflater = navHostFragment.navController.navInflater
        val graph = inflater.inflate(R.navigation.main_nav_graph)
        navHostFragment.navController.graph = graph
    }

    private fun requestStoragePermission() {
        val permissions = arrayOf(
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
        ActivityCompat.requestPermissions(
            this,
            permissions,
            myPermissionRequestStoragePermissions
        )
    }

    private fun isStoragePermissionGranted(): Boolean {
        val granted = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
        } else {
            true
        }
        return granted
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode != myPermissionRequestStoragePermissions) {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults)
            return
        }
        if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            //Log.d(TAG, "Storage permission granted")
            continueAfterPermissionGrant()
        } else {
            Toast.makeText(
                this,
                "You must Grant Storage Permission to continue",
                Toast.LENGTH_LONG
            ).show()
        }
    }


    private fun continueAfterPermissionGrant() {
        setupNavGraph()
    }

}