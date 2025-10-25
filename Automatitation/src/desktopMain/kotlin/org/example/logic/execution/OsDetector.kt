package org.example.logic.execution

import org.example.logic.model.OsType

object OsDetector {
    val current: OsType by lazy {
        val osName = System.getProperty("os.name").lowercase()

        when {
            osName.contains("win") -> OsType.WINDOWS
            osName.contains("linux") -> OsType.LINUX
            osName.contains("mac") || osName.contains("darwin") -> OsType.MAC
            else -> OsType.UNKNOWN
        }
    }
}