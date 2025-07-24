package com.jmc.home.data.source

import com.jmc.home.data.repository.Remote
import javax.inject.Inject


class Factory @Inject constructor(private val remote: Remote) {

    fun getRemote(): Remote = remote
}