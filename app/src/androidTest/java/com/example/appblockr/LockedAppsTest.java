package com.example.appblockr;

import static org.junit.Assert.assertEquals;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;

import com.example.appblockr.model.AppModel;
import com.example.appblockr.shared.SharedPrefUtil;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class LockedAppsTest {

    private LockedApps lockedApps;

    @Before
    public void setUp() {
        lockedApps = new LockedApps();
    }

    @Test
    public void testGetLockedApps() {
        // Mocking the behavior for the getLockedAppsList method
        SharedPrefUtil sharedPrefUtil = new SharedPrefUtil();
        List<String> lockedAppList = new ArrayList<>();
        lockedAppList.add("com.example.app1");
        lockedAppList.add("com.example.app2");
        sharedPrefUtil.setLockedAppsList(lockedAppList);

        // Mocking the behavior for the getInstalledApplications method
        PackageManager packageManager = new MockPackageManager();
        ApplicationInfo app1 = new ApplicationInfo();
        app1.packageName = "com.example.app1";
        app1.icon = 1;
        ApplicationInfo app2 = new ApplicationInfo();
        app2.packageName = "com.example.app2";
        app2.icon = 1;
        List<ApplicationInfo> installedApps = new ArrayList<>();
        installedApps.add(app1);
        installedApps.add(app2);

        // Setting the mocked objects in LockedApps class
        lockedApps.setPackageManager(packageManager);
        lockedApps.setSharedPrefUtil(sharedPrefUtil);

        // Calling the method under test
        lockedApps.getLockedApps();

        // Asserting that the correct apps are added to the list
        List<AppModel> apps = lockedApps.getApps();
        assertEquals(2, apps.size());
        assertEquals("com.example.app1", apps.get(0).getPackageName());
        assertEquals("com.example.app2", apps.get(1).getPackageName());
    }
}
