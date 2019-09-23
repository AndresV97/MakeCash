package com.example.smoviles.makecash

import android.os.Bundle
import android.view.Menu
import androidx.navigation.ui.AppBarConfiguration
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat

class FragmentosActivity : AppCompatActivity(),  NavigationView.OnNavigationItemSelectedListener {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fragmentos)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        val toggle = ActionBarDrawerToggle(
            this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        navView.setNavigationItemSelectedListener(this)

        val manager = supportFragmentManager
        val transaction=manager.beginTransaction()
        val inicioFragment = InicioFragment()
        transaction.add(R.id.contenedor,inicioFragment).commit()
    }

    override fun onBackPressed() {
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.fragmentos, menu)
        return true
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
            val manager = supportFragmentManager
            val transaction=manager.beginTransaction()

            when (item.itemId) {
                R.id.nav_inicio-> {
                    val inicioFragment = InicioFragment()
                    transaction.replace(R.id.contenedor,inicioFragment).commit()

                }
                R.id.nav_vender-> {
                    val venderFragment = VenderFragment()
                    transaction.replace(R.id.contenedor,venderFragment).commit()
                }
                R.id.nav_cuenta -> {
                    val cuentaFragment = CuentaFragment()
                    transaction.replace(R.id.contenedor,cuentaFragment).commit()
                }
            }
            val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
            drawerLayout.closeDrawer(GravityCompat.START)
            return true
        }
    }
