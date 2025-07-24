package com.jmc.details.data.source

import com.jmc.details.data.repository.Remote
import javax.inject.Inject

internal class Factory  @Inject constructor(private val remote: Remote){

    fun getRemote(): Remote = remote
}