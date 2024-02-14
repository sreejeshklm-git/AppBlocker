package com.example.appblockr.model;

import android.graphics.drawable.Drawable;

public class AppUsageModel {

        private String appName;
        private String packageName;
        private long startTime;
        private long endTime;
        private String usageTime;
        private Drawable appIcon;
        private int usageCount;

        public AppUsageModel(String appName, String packageName, long startTime, long endTime, String usageTime, Drawable appIcon, int usageCount) {
            this.appName = appName;
            this.packageName = packageName;
            this.startTime = startTime;
            this.endTime = endTime;
            this.usageTime = usageTime;
            this.appIcon = appIcon;
            this.usageCount = usageCount;
        }

        public String getAppName() {
            return appName;
        }

        public String getPackageName() {
            return packageName;
        }

        public long getStartTime() {
            return startTime;
        }

        public long getEndTime() {
            return endTime;
        }

        public String getUsageTime() {
            return usageTime;
        }

        public Drawable getAppIcon() {
            return appIcon;
        }

        public int getUsageCount() {
            return usageCount;
        }

        public void setUsageCount(int usageCount) {
            this.usageCount = usageCount;
        }

        @Override
        public String toString() {
            return "AppUsage{" +
                    "appName='" + appName + '\'' +
                    ", packageName='" + packageName + '\'' +
                    ", startTime=" + startTime +
                    ", endTime=" + endTime +
                    ", usageTime=" + usageTime +
                    ", appIcon=" + appIcon +
                    ", usageCount=" + usageCount +
                    '}';
        }
    }


