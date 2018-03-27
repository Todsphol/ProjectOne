package th.co.todsphol.add.projectone.activity

import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.matcher.ViewMatchers.isDisplayed
import android.support.test.espresso.matcher.ViewMatchers.withId
import android.support.test.rule.ActivityTestRule
import kotlinx.android.synthetic.main.activity_splash.view.*
import org.hamcrest.CoreMatchers.not
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import th.co.todsphol.add.projectone.R


class SplashActivityTest {
    public @Rule
    var menuActivityTestRule: ActivityTestRule<SplashActivity> = ActivityTestRule(SplashActivity::class.java, true, true)
    @Test
    fun check_splash_activity_display() {
        onView(withId(R.id.bg_splash))
                .check(matches(not(isDisplayed())))
    }
}