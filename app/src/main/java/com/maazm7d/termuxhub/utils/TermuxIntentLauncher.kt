package com.maazm7d.termuxhub.utils

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build

object TermuxIntentLauncher {
    private const val TERMUX_PACKAGE = "com.termux"
    private const val RUN_COMMAND_SERVICE = "$TERMUX_PACKAGE.app.RunCommandService"
    private const val ACTION_RUN_COMMAND = "$TERMUX_PACKAGE.RUN_COMMAND"

    private const val EXTRA_COMMAND_PATH = "$TERMUX_PACKAGE.RUN_COMMAND_PATH"
    private const val EXTRA_COMMAND_ARGUMENTS = "$TERMUX_PACKAGE.RUN_COMMAND_ARGUMENTS"
    private const val EXTRA_COMMAND_WORKDIR = "$TERMUX_PACKAGE.RUN_COMMAND_WORKDIR"
    private const val EXTRA_COMMAND_TERMINAL = "$TERMUX_PACKAGE.RUN_COMMAND_TERMINAL"
    private const val EXTRA_COMMAND_SESSION_ACTION = "$TERMUX_PACKAGE.RUN_COMMAND_SESSION_ACTION"

    fun isTermuxInstalled(context: Context): Boolean =
        try {
            context.packageManager.getPackageInfo(TERMUX_PACKAGE, 0)
            true
        } catch (_: PackageManager.NameNotFoundException) {
            false
        }

    fun run(
        context: Context,
        command: String,
        workingDir: String = "/data/data/com.termux/files/home"
    ) {
        val intent = Intent(ACTION_RUN_COMMAND).apply {
            setClassName(TERMUX_PACKAGE, RUN_COMMAND_SERVICE)
            putExtra(EXTRA_COMMAND_PATH, "/data/data/com.termux/files/usr/bin/bash")
            putExtra(EXTRA_COMMAND_ARGUMENTS, arrayOf("-c", command))
            putExtra(EXTRA_COMMAND_WORKDIR, workingDir)
            putExtra(EXTRA_COMMAND_TERMINAL, true) // opens Termux window
            putExtra(EXTRA_COMMAND_SESSION_ACTION, "0")
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(intent)
        } else {
            context.startService(intent)
        }
    }
}
