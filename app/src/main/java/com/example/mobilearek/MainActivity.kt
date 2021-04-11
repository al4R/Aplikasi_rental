package com.example.mobilearek


import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.add
import com.example.mobilearek.activity.LoginActivity
import com.example.mobilearek.fragment.AkunFragment
import com.example.mobilearek.fragment.HomeFragment
import com.example.mobilearek.fragment.PembayaranFragment
import com.example.mobilearek.fragment.RiwayatFragment
import com.example.mobilearek.helper.SharedPref
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private val fragmentHome: Fragment = HomeFragment ()
    private val fragmentRwt: Fragment = RiwayatFragment ()
    private val fragmentBayar: Fragment = PembayaranFragment ()
    private val fragmentAkun: Fragment = AkunFragment ()
    private val fm : FragmentManager = supportFragmentManager
    private var active: Fragment = fragmentHome

    private lateinit var menu: Menu
    private lateinit var menuItem: MenuItem
    private lateinit var bottomNavigationView: BottomNavigationView
    private var statusLogin = false

    private lateinit var s:SharedPref

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        s = SharedPref(this)

        setUpBottomNav()

    }
    fun setUpBottomNav(){
        fm.beginTransaction().add(R.id.container,fragmentHome).show(fragmentHome).commit()
        fm.beginTransaction().add(R.id.container,fragmentRwt).hide(fragmentRwt).commit()
        fm.beginTransaction().add(R.id.container,fragmentBayar).hide(fragmentBayar).commit()
        fm.beginTransaction().add(R.id.container,fragmentAkun).hide(fragmentAkun).commit()

        bottomNavigationView = findViewById(R.id.nav_view)
        menu = bottomNavigationView.menu
        menuItem = menu.getItem(0)
        menuItem.isChecked = true
        bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when(item.itemId){
                R.id.navigation_home ->{
                    callFragment(0,fragmentHome)
                }
                R.id.navigation_pembayaran ->{
                    callFragment(1,fragmentBayar)
                }
                R.id.navigation_riwayat ->{
                    callFragment(2,fragmentRwt)
                }
                R.id.navigation_akun ->{
                    if (s.getStatusLogin()){
                        callFragment( 3,fragmentAkun)
                    }else{
                        startActivity(Intent(this,LoginActivity::class.java))
                    }
                }
            }
            false

        }
    }
    fun callFragment(int: Int, fragment: Fragment){
        menuItem = menu.getItem(int)
        menuItem.isChecked = true
        fm.beginTransaction().hide(active).show(fragment).commit()
        active = fragment
    }
}