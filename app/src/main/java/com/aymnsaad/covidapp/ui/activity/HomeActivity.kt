package com.aymnsaad.covidapp.ui.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.aymnsaad.covidapp.R
import com.aymnsaad.covidapp.data.DataManger
import com.aymnsaad.covidapp.databinding.ActivityHomeBinding
import com.aymnsaad.covidapp.ui.fragments.*
import com.aymnsaad.covidapp.util.CsvParser
import com.aymnsaad.covidapp.util.log
import io.ak1.OnBubbleClickListener
import java.io.BufferedReader
import java.io.InputStreamReader


class HomeActivity : AppCompatActivity() {

    private val fragmentHome = HomeFragment()
    private val fragmentSearch = SearchFragment()
    private val fragmentInfo = InfoFragment()

    private val vaccineTypeFragment = VaccineTypeFragment()
    private val covidPreventFragment = CovidPreventFragment()

    private lateinit var binding: ActivityHomeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //set the style back to normal after activating splash screen
        setTheme(R.style.Theme_CovidApple)

        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        openFile()
        addNavigationListener()
    }

    private fun addNavigationListener() {

        binding.navigationBar.addBubbleListener(object : OnBubbleClickListener {
            override fun onBubbleClick(id: Int) {
                replaceFragment(
                    when (id) {
                        R.id.nav_home -> fragmentHome
                        R.id.nav_info -> fragmentInfo
                        R.id.nav_search -> fragmentSearch
                        R.id.nav_details -> vaccineTypeFragment
                        R.id.nav_Tips -> covidPreventFragment
                        else -> return
                    }
                )
            }
        })
    }


    private fun replaceFragment(newFragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(binding.container.id, newFragment)
        transaction.commit()
    }

    private fun openFile() {
        val inputStream = assets.open("country_vaccinations.csv")
        val buffer = BufferedReader(InputStreamReader(inputStream))
        val parser = CsvParser()
        buffer.forEachLine { line ->
            val vaccine = parser.parse(line)
            DataManger.addVaccine(vaccine)
        }
        DataManger.getTotalVaccinatedForAllCountries().log()

    }

}
