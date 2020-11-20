package com.example.cse438.cse438_assignment4

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.fragment.app.FragmentStatePagerAdapter
import com.example.cse438.cse438_assignment4.fragments.LoginFragment
import com.example.cse438.cse438_assignment4.fragments.SignupFragment


class LoginPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> {
                LoginFragment()
            }
            else -> {
                return SignupFragment()
            }
        }
    }

    override fun getCount(): Int {
        return 2
    }

    override fun getPageTitle(position: Int): CharSequence {
        return when (position) {
            0 -> "Login"
            else -> {
                return "Sign Up"
            }
        }
    }
}