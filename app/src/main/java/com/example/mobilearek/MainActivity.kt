package com.example.mobilearek


import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.mobilearek.fragment.*
import com.example.mobilearek.helper.SharedPref
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private val fragmentHome: Fragment = HomeFragment ()
    private val fragmentRwt: Fragment = RiwayatFragment ()
    private val fragmentAkun: Fragment = AkunFragment ()
    private val fragmentBaru: Fragment = AkunBaruFragment ()
    private val fragmentPesanan: Fragment = PesananFragment ()
    private val fm : FragmentManager = supportFragmentManager
    private var active: Fragment = fragmentHome
    private lateinit var menu: Menu
    private lateinit var menuItem: MenuItem
    private lateinit var bottomNavigationView: BottomNavigationView
    private var statusLogin = false
    private var backPressedTime = 0L

    private lateinit var s:SharedPref

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        s = SharedPref(this)

        setUpBottomNav()

    }
    fun setUpBottomNav(){
        fm.beginTransaction().add(R.id.container,fragmentHome).show(fragmentHome).commit()
        fm.beginTransaction().add(R.id.container,fragmentPesanan).hide(fragmentPesanan).commit()
        fm.beginTransaction().add(R.id.container,fragmentRwt).hide(fragmentRwt).commit()
        fm.beginTransaction().add(R.id.container,fragmentAkun).hide(fragmentAkun).commit()
        fm.beginTransaction().add(R.id.container,fragmentBaru).hide(fragmentBaru).commit()

        bottomNavigationView = findViewById(R.id.nav_view)
        menu = bottomNavigationView.menu
        menuItem = menu.getItem(0)
        menuItem.isChecked = true
        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when(item.itemId){
                R.id.navigation_home ->{
                    callFragment(0,fragmentHome)
                }
                R.id.navigation_pesanan->{
                    callFragment(1,fragmentPesanan)
                }
                R.id.navigation_riwayat ->{
                    callFragment(2,fragmentRwt)
                }
                R.id.navigation_akun ->{
                    if (s.getStatusLogin()){
                        callFragment( 3,fragmentAkun)
                    }else{
                        callFragment(3,fragmentBaru)
                    }
                }
            }
            false

        }
    }

    fun callFragment(int: Int, fragment: Fragment) {
        menuItem = menu.getItem(int)
        menuItem.isChecked = true
        fm.beginTransaction().hide(active).show(fragment).commit()
        active = fragment
    }
    override fun onBackPressed() {
        if(bottomNavigationView.selectedItemId==R.id.navigation_home && backPressedTime + 2000 > System.currentTimeMillis()) {
            super.onBackPressed()
        }else if (bottomNavigationView.selectedItemId==R.id.navigation_home && backPressedTime + 2000 < System.currentTimeMillis())
        {
            Toast.makeText(this@MainActivity, "Tekan lagi untuk keluar", Toast.LENGTH_SHORT).show()
        }
        else{
            bottomNavigationView.selectedItemId = R.id.navigation_home
        }
        backPressedTime = System.currentTimeMillis()
    }

}