package scanner.app.scan.qrcode.reader.activity

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentPagerAdapter
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import scanner.app.scan.qrcode.reader.R
import scanner.app.scan.qrcode.reader.data.constant.Constants
import scanner.app.scan.qrcode.reader.databinding.DashboardActivityBinding
import scanner.app.scan.qrcode.reader.fragment.QRGenerateFragment
import scanner.app.scan.qrcode.reader.fragment.QRHistoryFragment
import scanner.app.scan.qrcode.reader.fragment.QRScanFragment
import scanner.app.scan.qrcode.reader.fragment.QRSettingsFragment

class DashboardActivity : AppCompatActivity() {

    private var mActivity: Activity? = null
    private var mContext: Context? = null


    //View Binding
    private lateinit var dashboardActivityBinding: DashboardActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        dashboardActivityBinding = DashboardActivityBinding.inflate(layoutInflater)
        setContentView(dashboardActivityBinding.root)

        val navController = this.findNavController(R.id.qr_nav_host_fragment)
        // Find reference to bottom navigation view
        val navView: BottomNavigationView = findViewById(R.id.navigation_menu)
        // Hook your navigation controller to bottom navigation view
        navView.setupWithNavController(navController)
    }



    override fun onResume() {
        super.onResume()
        Constants.selectFromMain = true
        Constants.isSelectingFile = false
    }

    private fun initVars() {
        mActivity = this@DashboardActivity
        mContext = (mActivity as DashboardActivity).applicationContext
    }


}

private class ViewPagerAdapter(fm: androidx.fragment.app.FragmentManager) :
    FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
    private val fragmentList = ArrayList<Fragment>()

    fun addFragment(fragment: Fragment) {
        fragmentList.add(fragment)
    }

    override fun getItem(position: Int): Fragment {
        return fragmentList[position]
    }

    override fun getCount(): Int {
        return fragmentList.size
    }
}
