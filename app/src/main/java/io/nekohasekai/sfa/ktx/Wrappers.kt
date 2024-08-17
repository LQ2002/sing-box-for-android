package io.nekohasekai.sfa.ktx

import android.net.IpPrefix
import android.os.Build
import androidx.annotation.RequiresApi
import io.nekohasekai.libbox.RoutePrefix
import io.nekohasekai.libbox.StringIterator
import java.net.InetAddress

fun Iterable<String>.toStringIterator(): StringIterator {
    val list = this.toList()
    return object : StringIterator {
        private var index = 0

        override fun hasNext(): Boolean {
            return index < list.size
        }

        override fun next(): String {
            return list[index++]
        }

        override fun Len(): Int {
            return list.size
        }
    }
}

fun StringIterator.toList(): List<String> {
    return buildList {
        while (hasNext()) {
            add(next())
        }
    }
}

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
fun RoutePrefix.toIpPrefix() = IpPrefix(InetAddress.getByName(address()), prefix())
