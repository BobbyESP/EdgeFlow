package com.bobbyesp.edgeflow.util.ext

val Int.KB: Long
    get() = this * 1024L

val Int.MB: Long
    get() = this * 1024L * 1024L

val Int.GB: Long
    get() = this * 1024L * 1024L * 1024L