/*
 * Minecraft Dev for IntelliJ
 *
 * https://minecraftdev.org
 *
 * Copyright (c) 2017 minecraft-dev
 *
 * MIT License
 */

package com.demonwav.mcdev.error

import com.intellij.openapi.progress.ProgressIndicator
import com.intellij.openapi.progress.Task
import com.intellij.openapi.project.Project
import com.intellij.util.net.HttpConfigurable
import java.net.HttpURLConnection

class AnonymousFeedbackTask(
    project: Project?,
    title: String,
    canBeCancelled: Boolean,
    private val params: LinkedHashMap<String, String?>,
    private val callback: (Int) -> Unit,
    private val errorCallback: (Exception) -> Unit
) : Task.Backgroundable(project, title, canBeCancelled) {

    override fun run(indicator: ProgressIndicator) {
        indicator.isIndeterminate = true

        try {
            val token = AnonymousFeedback.sendFeedback(ProxyHttpConnectionFactory(), params)
            callback(token)
        } catch (e: Exception) {
            errorCallback(e)
        }
    }

    private inner class ProxyHttpConnectionFactory : AnonymousFeedback.HttpConnectionFactory() {
        override fun openHttpConnection(url: String): HttpURLConnection {
            return HttpConfigurable.getInstance().openHttpConnection(url)
        }
    }
}
