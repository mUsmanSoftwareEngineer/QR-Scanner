package scanner.app.scan.qrcode.reader.adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import java.util.ArrayList;

import scanner.app.scan.qrcode.reader.fragment.GenerateFragment;
import scanner.app.scan.qrcode.reader.fragment.HistoryFragment;
import scanner.app.scan.qrcode.reader.fragment.ScanFragment;
import scanner.app.scan.qrcode.reader.fragment.SettingsFragment;


/**
 * Created by Bezruk on 16/10/18.
 */
public class MainPagerAdapter extends FragmentStatePagerAdapter {

    private final ArrayList<String> mFragmentItems;

    public MainPagerAdapter(FragmentManager fm, ArrayList<String> fragmentItems) {
        super(fm);
        this.mFragmentItems = fragmentItems;
    }

    @Override
    public Fragment getItem(int i) {

        Fragment fragment = null;

        if (i == 0) {
            fragment = new ScanFragment();
//            fragment = new ScanningFragment();
        } else if (i == 1) {
            fragment = new GenerateFragment();
        } else if (i == 2) {
            fragment = new HistoryFragment();
        } else if (i == 3) {
            fragment = new SettingsFragment();
        }

        return fragment;
    }

    @Override
    public int getCount() {
        return mFragmentItems.size();
    }

}
