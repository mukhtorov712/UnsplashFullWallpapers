package uz.pdp.unsplashfullwallpapers.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.SearchView
import android.widget.Toast
import androidx.navigation.findNavController
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView.*
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.ui.*
import com.google.android.material.navigation.NavigationView
import eightbitlab.com.blurview.BlurView
import eightbitlab.com.blurview.RenderScriptBlur
import uz.pdp.unsplashfullwallpapers.R
import uz.pdp.unsplashfullwallpapers.databinding.ActivityMainBinding
import uz.pdp.unsplashfullwallpapers.room.database.AppDatabase
import uz.pdp.unsplashfullwallpapers.ui.viewmodel.MainViewModel

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private val idd = R.id.nav_host_fragment_content_main
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navView: NavigationView
    lateinit var navController: NavController
    private var t = false
    lateinit var appDatabase: AppDatabase
    lateinit var mainViewModel: MainViewModel

    private var desId = -1

    @SuppressLint("RestrictedApi")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)



        loadBlurMain(binding.appBarMain.contentMain.blurView)

        setSupportActionBar(binding.appBarMain.toolbar)

        drawerLayout = binding.drawerLayout
        navView = binding.navView




        navController = findNavController(idd)
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home, R.id.nav_popular, R.id.nav_random, R.id.nav_liked
            ), drawerLayout
        )

        navView.itemIconTintList = null

        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        navView.setNavigationItemSelectedListener(this)




        navController.addOnDestinationChangedListener { _, destination, _ ->
            Log.d("AAAA", destination.displayName)
            desId = destination.id
            when (destination.id) {
                R.id.nav_home -> {
                    show()
                    showSearch()
                }
                R.id.nav_popular -> {
                    show()
                    showSearch()
                }
                R.id.nav_random -> {
                    show()
                    showReload()
                }
                R.id.nav_liked -> {
                    show()
                    hideAll()
                }
                else -> hide()
            }
        }


//    <-----=================================================================-------->
        appDatabase = AppDatabase.getInstance(this)
        mainViewModel = ViewModelProvider(this)[MainViewModel::class.java]

        mainViewModel.fetchTopics().observe(this, {
            appDatabase.topicDao().addTopic(it)
        })

//    <-----=================================================================-------->


        binding.appBarMain.searchView.setOnQueryTextListener(object : OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
//                navController.navigate(R.id.searchFragment, Bundle().apply {
//                    putString("query", query)
//                })
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                Toast.makeText(this@MainActivity, newText, Toast.LENGTH_SHORT).show()
                return false
            }
        })

        binding.appBarMain.searchView.setOnCloseListener(OnCloseListener {
            navController.popBackStack(R.id.nav_home, false)
            navController.navigate(R.id.nav_home)
            setInvisible(0)
            t = true
            return@OnCloseListener false
        })


        binding.appBarMain.contentMain.blurCardView.visibility = View.VISIBLE

        binding.appBarMain.reload.setOnClickListener {
            navController.popBackStack(R.id.nav_home, false)
            navController.navigate(R.id.nav_random)
            setInvisible(2)
            t = false
        }

        binding.appBarMain.contentMain.blurHome.setOnClickListener { v ->
            if (desId != R.id.nav_home) {
                navController.popBackStack(R.id.nav_home, false)
                navController.navigate(R.id.nav_home)
                setInvisible(0)
                t = true
            }
        }

        binding.appBarMain.contentMain.blurPopular.setOnClickListener { v ->
            if (desId != R.id.nav_popular) {
                navController.popBackStack(R.id.nav_home, false)
                navController.navigate(R.id.nav_popular)
                setInvisible(1)
                t = false
            }
        }

        binding.appBarMain.contentMain.blurRandom.setOnClickListener { v ->
            if (desId != R.id.nav_random) {
                navController.popBackStack(R.id.nav_home, false)
                navController.navigate(R.id.nav_random)
                setInvisible(2)
                t = false
            }
        }

        binding.appBarMain.contentMain.blurLiked.setOnClickListener { v ->
            if (desId != R.id.nav_liked) {
                navController.popBackStack(R.id.nav_home, false)
                navController.navigate(R.id.nav_liked)
                setInvisible(3)
                t = false
            }
        }


    }

    //    <-----=================================================================-------->
    private fun hide() {
        binding.appBarMain.contentMain.blurCardView.visibility = View.GONE
        binding.appBarMain.toolbar.visibility = View.GONE
    }

    private fun show() {
        binding.appBarMain.contentMain.blurCardView.visibility = View.VISIBLE
        binding.appBarMain.toolbar.visibility = View.VISIBLE
    }

    private fun showSearch() {
        binding.appBarMain.reload.visibility = View.GONE
        binding.appBarMain.searchView.visibility = View.VISIBLE
    }

    private fun showReload() {
        binding.appBarMain.searchView.visibility = View.GONE
        binding.appBarMain.reload.visibility = View.VISIBLE
    }

    private fun hideAll() {
        binding.appBarMain.searchView.visibility = View.GONE
        binding.appBarMain.reload.visibility = View.GONE
    }

    //    <-----=================================================================-------->


    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    //    <-----=================================================================-------->
    private fun setInvisible(count: Int) {
        binding.appBarMain.contentMain.circleHome.visibility = View.GONE
        binding.appBarMain.contentMain.circlePopular.visibility = View.GONE
        binding.appBarMain.contentMain.circleRandom.visibility = View.GONE
        binding.appBarMain.contentMain.circleLiked.visibility = View.GONE
        when (count) {
            0 -> binding.appBarMain.contentMain.circleHome.visibility = View.VISIBLE
            1 -> binding.appBarMain.contentMain.circlePopular.visibility = View.VISIBLE
            2 -> binding.appBarMain.contentMain.circleRandom.visibility = View.VISIBLE
            3 -> binding.appBarMain.contentMain.circleLiked.visibility = View.VISIBLE
        }
    }

    //    <-----=================================================================-------->
    private fun loadBlurMain(blurView: BlurView) {
        val radius = 12f
        blurView.setupWith(binding.appBarMain.contentMain.root)
            .setBlurAlgorithm(RenderScriptBlur(this))
            .setBlurRadius(radius)
            .setBlurAutoUpdate(true)
            .setHasFixedTransformationMatrix(true)
    }


    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_home -> setInvisible(0)
            R.id.nav_popular -> setInvisible(1)
            R.id.nav_random -> setInvisible(2)
            R.id.nav_liked -> setInvisible(3)
        }
        //This is for maintaining the behavior of the Navigation view
        NavigationUI.onNavDestinationSelected(item, navController)
        //This is for closing the drawer after acting on it
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    override fun onBackPressed() {
        super.onBackPressed()
        if (t) {
            finish()
        }
        setInvisible(0)
    }

}