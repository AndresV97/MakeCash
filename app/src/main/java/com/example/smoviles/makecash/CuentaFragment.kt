package com.example.smoviles.makecash

import android.os.Bundle
import androidx.fragment.app.FragmentPagerAdapter
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout


class CuentaFragment : Fragment() {


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.fragment_cuenta, container, false)
        tabLayout = view.findViewById<View>(R.id.tabs) as TabLayout
        viewPager = view.findViewById<View>(R.id.view_pager) as ViewPager

        viewPager.adapter = MyAdapter(childFragmentManager)
        tabLayout.post { tabLayout.setupWithViewPager(viewPager)}

        return view
    }

    internal inner class MyAdapter (fm: FragmentManager) : FragmentPagerAdapter(fm) {

        override fun getItem(position: Int): Fragment {
            when(position){
                0 -> return MisAnunciosFragment()
                1 -> return FavoritosFragment()
                else -> return MensajesFragment()
            }
        }

        override fun getCount(): Int {
            return int_items
        }

        override fun getPageTitle(position: Int): CharSequence? {
            when(position){
                0 -> return "Mis Anuncios"
                1 -> return "Favoritos"
                2 -> return "Mensajes"
            }
            return null
        }
    }

    companion object {
        lateinit var tabLayout: TabLayout
        lateinit var viewPager: ViewPager
        var int_items = 3

    }


}


