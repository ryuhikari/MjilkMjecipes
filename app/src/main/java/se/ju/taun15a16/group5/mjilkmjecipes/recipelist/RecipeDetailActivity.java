package se.ju.taun15a16.group5.mjilkmjecipes.recipelist;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import se.ju.taun15a16.group5.mjilkmjecipes.CommentsFragment;
import se.ju.taun15a16.group5.mjilkmjecipes.R;

public class RecipeDetailActivity extends AppCompatActivity {

    public static final String EXTRA_RECIPE_ID = "id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        tabLayout.addTab(tabLayout.newTab().setText("Recipe"));
        tabLayout.addTab(tabLayout.newTab().setText("Comments"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        final ViewPager viewPager = (ViewPager) findViewById(R.id.recipePager);
        final RecipeDetailActivity.RecipePagerAdapter adapter = new RecipeDetailActivity.RecipePagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }


    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class RecipePagerAdapter extends FragmentPagerAdapter {

        //TODO: Somehow pass commentData from RecipeDetailFragment to CommentsFragment to save data-volume!
        private static final int TAB_COUNT = 2;

        RecipePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            long recipeId = (long) getIntent().getExtras().get(EXTRA_RECIPE_ID);
            RecipeDetailFragment f = null;
            switch (position){
                case 0:
                    f = new RecipeDetailFragment();
                    f.setRecipe( recipeId );
                    return f;
                case 1:
                    return CommentsFragment.newInstance(recipeId);
                default:
                    f = new RecipeDetailFragment();
                    return f;
            }
        }

        @Override
        public int getCount() {
            return TAB_COUNT;
        }
    }
}
