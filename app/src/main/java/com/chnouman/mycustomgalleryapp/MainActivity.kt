package com.chnouman.mycustomgalleryapp

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.NavHostFragment
import com.chnouman.mycustomgalleryapp.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    private var continueAfterPermission = 0
    private val MY_PERMISSIONS_REQUEST_STORAGE_PERMISSIONS = 10
    private var TAG = "MainActivity"
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
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE
        )
        ActivityCompat.requestPermissions(
            this,
            permissions,
            MY_PERMISSIONS_REQUEST_STORAGE_PERMISSIONS
        )
    }

    private fun isStoragePermissionGranted(): Boolean {
        var granted = false
        granted = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
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
        if (requestCode != MY_PERMISSIONS_REQUEST_STORAGE_PERMISSIONS) {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults)
            return
        }
        if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG, "Storage permission granted")
            continueAfterPermissionGrant()
        } else {
            Toast.makeText(
                this,
                "You must Grants Storage Permission to continue",
                Toast.LENGTH_LONG
            ).show()
        }
    }


    private fun continueAfterPermissionGrant() {
        setupNavGraph()
    }

}