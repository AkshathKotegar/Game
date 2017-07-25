
package com.improstech.housie.app.helper;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.improstech.housie.app.R;
import com.improstech.housie.app.activity.GameActivity;
import com.improstech.housie.app.activity.HomeActivity;
import com.improstech.housie.app.activity.SplashScreen;

public class MenuListFragment extends Fragment {

    private ImageView ivMenuUserProfilePhoto;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_menu, container,
                false);
        ivMenuUserProfilePhoto = (ImageView) view.findViewById(R.id.ivMenuUserProfilePhoto);
        NavigationView vNavigation = (NavigationView) view.findViewById(R.id.vNavigation);
        vNavigation.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                Toast.makeText(getActivity(), menuItem.getTitle(), Toast.LENGTH_SHORT).show();
                if (menuItem.getTitle().toString().equals("Games")) {
                    Intent intent = new Intent(getActivity(), GameActivity.class);
                    startActivity(intent);
                }
                return false;
            }
        });
        //setupHeader();
        return view;
    }

   /* private void setupHeader() {
        int avatarSize = getResources().getDimensionPixelSize(R.dimen.global_menu_avatar_size);
        //String profilePhoto = getResources().getString(R.string.user_profile_photo);

        Picasso.with(getActivity())
                .load(R.drawable.ic_logo)
                .placeholder(R.drawable.img_circle_placeholder)
                .resize(avatarSize, avatarSize)
                .centerCrop()
                .transform(new CircleTransformation())
                .into(ivMenuUserProfilePhoto);
    }*/
}
