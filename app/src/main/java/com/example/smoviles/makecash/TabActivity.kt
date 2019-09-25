package com.example.smoviles.makecash

import android.app.Fragment
import android.content.Context
import android.graphics.Color
import android.os.Bundle
//import android.support.v4.app.FragmentManager
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import androidx.viewpager.widget.ViewPager
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.FragmentManager
import com.example.smoviles.makecash.ui.main.SectionsPagerAdapter
import java.text.FieldPosition

class TabActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tab)

        val sectionsPagerAdapter = SectionsPagerAdapter(this, supportFragmentManager)
        val viewPager: ViewPager = findViewById(R.id.view_pager)
        viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = findViewById(R.id.tabs)
        tabs.setupWithViewPager(viewPager)
        tabs.setTabTextColors(Color.parseColor("#ffa500"), Color.parseColor("#ffa500"))



        val fab: FloatingActionButton = findViewById(R.id.fab)
        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.activity_tab_menu, menu)
        return true
    }

    private val TAB_TITLES = arrayOf(
       "Mis Anuncios",
       "Favoritos",
       "Mensajes"
   )
/*
    class SectionsPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

        override fun getItem(position: Int): Fragment {
            when(position){
                0 -> return MisAnunciosFragment()
                1 -> return FavoritosFragment()
                else -> return MensajesFragment()

            }
        }
    }*/

    fun onNavigationItemSelected(item: MenuItem): Boolean {
        val manager = supportFragmentManager
        val transaction=manager.beginTransaction()

        when (item.itemId) {
            R.id.nav_mis_anuncios-> {
                val anunciosFragment = MisAnunciosFragment()
                transaction.replace(R.id.contenedor,anunciosFragment).commit()
            }
            R.id.nav_favoritos-> {
                val favoritosFragment = FavoritosFragment()
                transaction.replace(R.id.contenedor,favoritosFragment).commit()
            }
            R.id.nav_mensajes -> {
                val mensajesFragment = MensajesFragment()
                transaction.replace(R.  id.contenedor, mensajesFragment).commit()
            }
        }
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }
}




