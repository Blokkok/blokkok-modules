package com.blokkok.mod.aapem

import com.blokkok.modsys.communication.CommunicationContext
import com.blokkok.modsys.modinter.Module
import java.io.File

class AAPEModule : Module() {
    override val namespace: String = "blokkok-aapem"
    override val flags: List<String> = listOf("project-manager-impl")

    override fun onLoaded(comContext: CommunicationContext) {
        comContext.run {

        // project manager implementation
        namespace("pm-impl") {
            createFunction("name") { "Blokkok AAPEM" }
            createFunction("new_project_config") {
                listOf("App Name", "App Package")
            }

            @Suppress("UNCHECKED_CAST")
            createFunction("initialize_project") {
                val projectDir = it["project_dir"] as File
                val conf = it["project_conf"] as Map<String, String>

                val appName = conf["App Name"]
                val packageName = conf["App Package"]

                val src = projectDir.resolve("src")     // where the source code is located
                src.mkdirs()

                // populate the java directory with the package name
                val javaPackage = src.resolve("java/${packageName!!.replace(".", "/")}")
                javaPackage.mkdirs()

                // initialize the res folder (and layout)
                val layout = src.resolve("res/layout")
                layout.mkdirs()

                // initialize MainActivity, layout, and manifest
                javaPackage.resolve("MainActivity.java")
                    .writeText("""
                        package $packageName;
                        
                        import android.app.Activity;
                        import android.os.Bundle;
                        
                        class MainActivity extends Activity {
                            @Override
                            public void onCreate(Bundle savedInstanceState) {
                                super.onCreate();
                                setContentView(R.layout.main);
                            }
                        }
                    """.trimIndent())

                layout.resolve("main.xml")
                    .writeText("""
                        <?xml version="1.0" encoding="utf-8"?>
                        <LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
                            xmlns:app="http://schemas.android.com/apk/res-auto"
                            android:id="@+id/root"
                            android:layout_width="match_parent"
                            android:layout_height="match_parent"
                            android:gravity="center_horizontal|center_vertical"
                            android:orientation="vertical">
                        
                            <TextView
                                android:id="@+id/hello_blokkok"
                                android:layout_width="wrap_content"
                                android:layout_height="wrap_content"
                                android:text="Hello Blokkok!" />
                                
                        </LinearLayout>
                    """.trimIndent())

                src.resolve("AndroidManifest.xml")
                    .writeText("""
                        <?xml version="1.0" encoding="utf-8"?>
                        <manifest xmlns:android="http://schemas.android.com/apk/res/android"
                            package="$packageName">

                            <uses-sdk
                                android:minSdkVersion="21"
                                android:targetSdkVersion="30"/>

                            <application
                                android:allowBackup="true"
                                android:label="@string/$appName">
                                <activity android:name=".MainActivity">
                                    <intent-filter>
                                        <action android:name="android.intent.action.MAIN" />

                                        <category android:name="android.intent.category.LAUNCHER" />
                                    </intent-filter>
                                </activity>
                            </application>

                        </manifest>
                    """.trimIndent())

                projectDir.resolve("cache").mkdirs() // folder for build cache

                null
            }

            createFunction("show_project_editor") {
                
            }
        }
        }
    }

    override fun onUnloaded(comContext: CommunicationContext) {

    }
}